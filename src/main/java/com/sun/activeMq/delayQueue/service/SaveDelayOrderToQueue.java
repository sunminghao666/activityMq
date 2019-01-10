package com.sun.activeMq.delayQueue.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.DelayQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sun.activeMq.delayQueue.code.DelayQueueCode;
import com.sun.activeMq.delayQueue.dao.DelayOrderDao;
import com.sun.activeMq.delayQueue.dto.DelayOrderDto;
import com.sun.activeMq.delayQueue.dto.DelayOrderInfo;

@Service
@Qualifier("dq")
public class SaveDelayOrderToQueue {

	@Autowired
	private CheckDelayOrder checkDelayOrder;

	private Thread takeOrder;

	private static DelayQueue<DelayOrderInfo<DelayOrderDto>> delayOrder
			= new DelayQueue<DelayOrderInfo<DelayOrderDto>>();

    public void orderDelay(DelayOrderDto order, long expireTime) {
    	DelayOrderInfo<DelayOrderDto> itemOrder = new DelayOrderInfo<DelayOrderDto>(expireTime*1000,order);
    	delayOrder.put(itemOrder);
    	System.out.println("订单[超时时长："+expireTime+"秒]被推入检查队列，订单详情："+order);
    }

    private class TakeOrder implements Runnable{
    	
    	private CheckDelayOrder checkDelayOrder;

		public TakeOrder(CheckDelayOrder checkDelayOrder) {
			super();
			this.checkDelayOrder = checkDelayOrder;
		}

		public void run() {
			System.out.println("处理到期订单线程已经启动！");
			while(!Thread.currentThread().isInterrupted()) {
				try {
					DelayOrderInfo<DelayOrderDto> itemOrder = delayOrder.take();
					if (itemOrder!=null) {
						checkDelayOrder.checkDelayOrder(itemOrder.getData());
					}
				} catch (Exception e) {
					System.out.println("The thread :");
				}
			}
			System.out.println("处理到期订单线程准备关闭......");
		}
    }

//  @PostConstruct// 项目启动时，初始化带有该标签的方法并执行
//  public void init() {
//  	takeOrder = new Thread(new TakeOrder(checkDelayOrder));
//  	takeOrder.start();
//  }
//
//  @PreDestroy
//  public void close() {
//  	takeOrder.interrupt();
//  }
}
