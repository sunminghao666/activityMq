package com.sun.activeMq.reliability;

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

public class ReliabilityConsumerAsyn {

    //默认连接用户名
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    //默认连接密码
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    //默认连接地址
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;


    public static void main(String[] args) {
        /* 连接工厂*/
        ConnectionFactory connectionFactory;
        /* 连接*/
        Connection connection = null;
        /* 会话*/
        Session session;
        /* 消息的目的地*/
        Destination destination;
        /* 消息的消费者*/
        MessageConsumer messageConsumer;

        /* 实例化连接工厂*/
        connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);


        try {
            /* 通过连接工厂获取连接*/
            connection = connectionFactory.createConnection();
            /* 启动连接*/
            connection.start();
            /* 创建session*/
            // Session.CLIENT_ACKNOWLEDGE客户端自动确认，需要与message.acknowledge();配合使用
            // Session.AUTO_ACKNOWLEDGE自动确认
            // Session.DUPS_OK_ACKNOWLEDGE批量确认
            // Session.SESSION_TRANSACTED事务确认，需要与sesson.commit();配合使用，程序出现异常需要在finnally里session.rollback()回滚
            session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
            /* 创建一个消息队列*/
            destination = session.createQueue("MsgReliability");

            /* 创建消息消费者*/
            messageConsumer = session.createConsumer(destination);
            /* 设置消费者监听器，监听消息*/
            messageConsumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                        //do my work
                        System.out.println(((TextMessage) message).getText());
                        message.acknowledge();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    //throw new RuntimeException("RuntimeException");
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
