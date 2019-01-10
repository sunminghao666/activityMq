package com.sun.activeMq.delayQueue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.activeMq.delayQueue.code.DelayQueueCode;
import com.sun.activeMq.delayQueue.dao.DelayOrderDao;
import com.sun.activeMq.delayQueue.dto.DelayOrderDto;

@Service
public class CheckDelayOrder {

	@Autowired
	private DelayOrderDao delayOrderDao;

	/**检查数据库中指定id的订单的状态,如果为未支付，则修改为已过期*/
	public void checkDelayOrder(DelayOrderDto record) {
		DelayOrderDto dbOrder = delayOrderDao.selectByPrimaryKey(record.getId());
		if(dbOrder.getOrderStatus()==0) {
			System.out.println("订单【"+record+"】未支付已过期，需要更改为过期订单！");
			delayOrderDao.updateExpireOrder(record.getId());
		}else {
			System.out.println("已支付订单【"+record+"】，无需修改！");
		}
		
	}
}
