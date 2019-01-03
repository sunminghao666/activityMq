package com.sun.activeMq;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sun.activeMq.SpringBoot.normal.queue.QueueProducer;
import com.sun.activeMq.SpringBoot.normal.topic.TopicProducer;
import com.sun.activeMq.SpringBoot.replyTo.ReplyToProducer;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootActiveMqTest {

    @Autowired
    private QueueProducer queueProducer;

    @Autowired
    private TopicProducer topicProducer;

    @Autowired
    private ReplyToProducer replyToProducer;

    // 点对点消息模式
    @Test
    public void testQueueNormal() {
        Destination destination = new ActiveMQQueue("springboot.queue");
        for (int i = 0; i < 10; i++) {
        	queueProducer.sendMessage_1(destination, "NO:"+i+";my name is Mark!!!");
        }
    }

    // 广播消息模式
    @Test
    public void testTopicNormal() {
        Destination destination = new ActiveMQTopic("springboot.topic");
        for (int i = 0; i < 3; i++) {
        	topicProducer.sendMessage(destination, "NO:"+i+";James like 13 !!!");
        }
    }

    // 应答消息模式
    @Test
    public void testReplyTo() {
        Destination destination = new ActiveMQQueue("springboot.replyto.queue");
        for (int i = 0; i < 3; i++) {
        	replyToProducer.sendMessage(destination, "NO:"+i+";my name is Mark!!!");
        }
    }

	@Test
	public void contextLoads() {
	}

}

