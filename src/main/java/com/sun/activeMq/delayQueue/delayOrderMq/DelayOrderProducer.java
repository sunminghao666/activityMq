package com.sun.activeMq.delayQueue.delayOrderMq;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sun.activeMq.delayQueue.code.DelayQueueCode;
import com.sun.activeMq.delayQueue.dao.DelayOrderDao;
import com.sun.activeMq.delayQueue.dto.DelayOrderDto;

@Service
public class DelayOrderProducer {

	@Autowired
	private DelayOrderDao delayOrderDao;

    //默认连接用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;
    //发送的消息数量
    private static final int ORDER_NO = 3;

    public void delayOrderProducer(DelayOrderDto orderExp, long expireTime) {

        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageProducer messageProducer;

        connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKEURL);

        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("delay.order");

            messageProducer = session.createProducer(destination);
            
    		Random r = new Random();

			Gson gson = new Gson();
			
            String txtMsg = gson.toJson(orderExp);
    		System.out.println("1111订单[超时时长："+expireTime+"秒] 将被发送给消息队列，详情："+orderExp);
    		TextMessage message = session.createTextMessage(txtMsg);
    		message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, expireTime*1000);
    		// 发送消息
    		messageProducer.send(message);

            session.commit();
            messageProducer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


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
