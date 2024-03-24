package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    //用户下单
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    //分页查询历史数据
    PageResult pageQuery(int page, int pageSize, Integer status);

    OrderVO details(Long id);


    //根据订单id取消订单
    void userCancelById(Long id) throws Exception;
    //根据订单id再次下订单
    void repetition(Long id);
}
