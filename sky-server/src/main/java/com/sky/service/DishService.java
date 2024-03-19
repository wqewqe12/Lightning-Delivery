package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /*
    *
    * 新增菜品对应的口味
    * */
    public void saveWithFlavor(DishDTO dishDTO);


    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品对应的口味数据
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);
    /**
     * 根据id修改菜品基本信息和对应的口味信息
     * @param dishDTO
     * @return
     */
    void updateWithFlavor(DishDTO dishDTO);

    List<Dish> list(Long categoryId);

    /**
     * 起售停售菜品
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
