package com.sun.activeMq.SpringBoot.normal.queue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service
public class QueueProducer {

	@Autowired
	private JmsMessagingTemplate jmsTemplate;

	@Autowired
	private JmsTemplate template;//可以做更细微的控制

    // 发送消息，destination是发送到的队列，message是待发送的消息
    public void sendMessage_1(Destination destination, final String message) {

        jmsTemplate.convertAndSend(destination, message);
    }

    // 发送消息，destination是发送到的队列，message是待发送的消息
    public void sendMessage_2(Destination destination, final String message) {

        template.send(destination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {

                TextMessage msg = session.createTextMessage();
                msg.setText("othre information");
                return msg;
            }
        });
    }

}
