package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Transactional//一致性
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品表插入1条数据
        dishMapper.insert(dishDTO);
        //获得isnert执行生产的主键值id
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                //遍历，赋值id
                dishFlavor.setDishId(dishId);
            });
            //向口味插入N条数据,批量插入
            dishFlavorMapper.insertBatch(flavors);

        }
        //
    }

/* 菜品分页查询*/
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page =dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

@Transactional
    public void deleteBatch(List<Long> ids) {
       // 1.判断菜品是否正在起售中
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 2.判断菜品是否被关联
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() >0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品表的菜品数据()
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//            dishFlavorMapper.deleteByDishId(id);
//        }
        //上述优化
        //根据菜品id批量删除菜品数据
        dishMapper.deleteByIds(ids);
        //根据菜品id批量删除菜品关联口味数据
        dishFlavorMapper.deleteByDishIds(ids);
        //删除菜品关联的口味表

    }
    /**
     * 根据id查询菜品对应的口味数据
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //根据id查询菜品数据
        Dish dish = dishMapper.getById(id);
        //根据菜品id查询菜品口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        //查询结果封装到VO
        DishVO dishVO =new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        //设置口味
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }
    /**
     * 根据id修改菜品基本信息和对应的口味信息
     * @param dishDTO
     * @return
     */
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //修改菜品表基本信息
        dishMapper.update(dish);
        //删除原先的口味表，然后再更新当前传入的菜品信息。
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
        //判断是否有flavor
        if (flavors != null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                //遍历，赋值id
                dishFlavor.setDishId(dishDTO.getId());
            });
            //向口味插入N条数据,批量插入
            dishFlavorMapper.insertBatch(flavors);
        }
    }
    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /*启动，禁用分类*/
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
//                .updateTime(LocalDateTime.now())
//                .updateUser(BaseContext.getCurrentId())
                .build();
        // 如果是停售操作，还需要将包含当前菜品的套餐也停售
        if (status == StatusConstant.DISABLE){
            //TODO：这里停售起售只传入一个菜品id，为什么要生成一个集合去接收菜品id，并且sql执行 "in (ids)"?
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            //通过菜品id，查找套餐菜品关联表中的套餐id
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            //套餐id有结果
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    //把套餐停售
                    setmealMapper.update(setmeal);
                }
            }
    }
      }
}
