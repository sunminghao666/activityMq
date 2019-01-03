package com.sun.activeMq.normal.queue;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class QueueConsumerB {

    // 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
    @JmsListener(destination = "springboot.queue")
    //@JmsListener(destination = "springboot.queue") 同一个方法可以侦听多个队列
    public void receiveQueue(String text) {
        System.out.println(this.getClass().getName()+" 收到的报文为:"+text);
    }
}
