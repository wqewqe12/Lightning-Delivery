package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

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
}
