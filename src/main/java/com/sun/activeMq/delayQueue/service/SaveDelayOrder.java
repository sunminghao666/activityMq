package com.sun.activeMq.delayQueue.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sun.activeMq.delayQueue.code.DelayQueueCode;
import com.sun.activeMq.delayQueue.dao.DelayOrderDao;
import com.sun.activeMq.delayQueue.delayOrderMq.DelayOrderProducer;
import com.sun.activeMq.delayQueue.dto.DelayOrderDto;

@Service
public class SaveDelayOrder {

	@Autowired
	private DelayOrderDao delayOrderDao;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private DelayOrderProducer delayOrderProducer;

//	@Autowired
//	@Qualifier("mq")
//	private SaveDelayOrderToQueue saveDelayOrderToQueue;

	public void saveDelayOrder(int orderNo) {

		Random r = new Random();

		for (int i = 0; i < orderNo; i++) {
			long expireTime = r.nextInt(20)+5;//订单的超时时长，单位秒
			DelayOrderDto orderExp = new DelayOrderDto();
            orderExp.setOrderNo(getOrderNo());
            orderExp.setOrderNote("延迟订单——"+i);
            orderExp.setOrderStatus(DelayQueueCode.UN_PAY);
            delayOrderDao.insertDelayOrder(orderExp, expireTime);
    		System.out.println("1111订单[超时时长："+expireTime+"秒] 将被发送给消息队列，详情："+orderExp);
    		delayOrderProducer.delayOrderProducer(orderExp, expireTime);
//    		jmsTemplate.send("order.delay", new CreateMessage(orderExp,expireTime*1000));
//            saveDelayOrderToQueue.orderDelay(orderExp, expireTime);
            
		}
	}

    /**
     *类说明：创建消息的类
     */
    private static class CreateMessage implements MessageCreator{
    	
    	private DelayOrderDto order;
    	private long expireTime;
    	
		public CreateMessage(DelayOrderDto order, long expireTime) {
			super();
			this.order = order;
			this.expireTime = expireTime;
		}

		public Message createMessage(Session session) throws JMSException {
			Gson gson = new Gson();
			String txtMsg = gson.toJson(order);
			Message message = session.createTextMessage(txtMsg);
			message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, expireTime);
			return message;
		}
    }
   
	/**
	 * 获取调仓订单号
	 * 
	 * @param
	 * @since 2018-09-03 10:00:00
	 * @author SunMinghao
	 * @return
	 */
	private String getOrderNo() {

		String code = "";
		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			code += r.nextInt(10);
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = df.format(new Date());
		return time.substring(2, time.length()) + code;
	}

}
