package com.sun.activeMq.delayQueue.delayOrderMq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sun.activeMq.delayQueue.dao.DelayOrderDao;
import com.sun.activeMq.delayQueue.dto.DelayOrderDto;
import com.sun.activeMq.delayQueue.service.CheckDelayOrder;

@Service
public class DelayOrderConsumer {

	@Autowired
	private CheckDelayOrder checkDelayOrder;

    //默认连接用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;

	public void delayOrderConsumer() throws JMSException {

		ConnectionFactory factory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);

		// 通过工厂创建一个连接
		Connection connection = factory.createConnection();
		// 启动连接
		connection.start();
		// 创建一个session会话 事务 自动ack
		Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);
		// 创建一个消息队列
		Destination destination = session.createQueue("delay.order");
		// 创建消费者
		MessageConsumer consumer = session.createConsumer(destination);

		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					String txtMsg = ((TextMessage)message).getText();
					System.out.println("接收到消息队列发出消息："+txtMsg);
					Gson gson = new Gson();
					DelayOrderDto order = (DelayOrderDto)gson.fromJson(txtMsg, DelayOrderDto.class);
					checkDelayOrder.checkDelayOrder(order);
					message.acknowledge();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
