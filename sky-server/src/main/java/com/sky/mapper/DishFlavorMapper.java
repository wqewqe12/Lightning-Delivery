package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);
    /**
     * 根据菜品id来删除对应的口味数据
     * @param dishId
     * @return
     */
    @Delete("DELETE  from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);


    void deleteByDishIds(List<Long> ids);
    /**
     * 根据菜品id来查询对应口味
     * @return
     */
    @Select("select * from dish_flavor where dish_id =#{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
