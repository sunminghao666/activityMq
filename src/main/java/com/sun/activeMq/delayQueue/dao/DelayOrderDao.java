package com.sun.activeMq.delayQueue.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.sun.activeMq.delayQueue.dto.DelayOrderDto;

@Mapper
public interface DelayOrderDao {

    int deleteByPrimaryKey(Long id);

    int insert(DelayOrderDto record);

    int insertSelective(DelayOrderDto record);

    DelayOrderDto selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DelayOrderDto record);

    int updateByPrimaryKey(DelayOrderDto record);
    
    /*插入延迟订单*/
    int insertDelayOrder(@Param("order") DelayOrderDto order,
                         @Param("expire_duration") long expire_duration);
    
    /*将指定id且未支付订单的状态改为已过期*/
    int updateExpireOrder(Long id);
    
    /*将表中所有时间上已过期但未支付订单的状态改为已过期*/
    int updateExpireOrders();
    
    /*找出未支付且未过期的订单 */
    List<DelayOrderDto> selectUnPayOrders();
    
//    /*找出未支付且已过期的订单*/
//    List<OrderExp> selectExpiredOrders();
}
