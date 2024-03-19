package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /*分页查询*/
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
/*新增套餐关联表*/
    void saveWithDish(SetmealDTO setmealDTO);
    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    List<Dish> list(Long setmealId);
    /**
     * 根据id查询套餐和关联的菜品数据
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    void update(SetmealDTO setmealDTO);
//起售停售套餐
    void startOrStop(Integer status, Long id);
}
