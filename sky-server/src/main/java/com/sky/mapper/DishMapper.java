package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入菜品数据
     * @param
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(DishDTO dishDTO);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Select("select * from dish where  id = #{id}")
    Dish getById(Long id);

    /**
     * 根据主键删除数据
     * @param id
     * @return
     */
    @Delete("DELETE  from dish where  id = #{id}")
    void deleteById(Long id);

    /**
     * 根据菜品id集合批量删除菜品
     * @param ids
     * @return
     */

    void deleteByIds(List<Long> ids);
}
