package com.sun.activeMq;

import javax.jms.JMSException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sun.activeMq.delayQueue.delayOrderMq.DelayOrderConsumer;
import com.sun.activeMq.delayQueue.delayOrderMq.DelayOrderProducer;
import com.sun.activeMq.delayQueue.service.SaveDelayOrder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DelayOrderTests {

	@Autowired
	private SaveDelayOrder saveDelayOrder;

	@Autowired
	private DelayOrderProducer delayOrderProducer;

	@Autowired
	private DelayOrderConsumer delayOrderConsumer;

	@Test
	public void saveDelayOrder() {
		saveDelayOrder.saveDelayOrder(5);
	}

	@Test
	public void delayOrderProducer() {
		delayOrderProducer.delayOrderProducer();
	}

	@Test
	public void delayOrderConsumer() throws JMSException {
		delayOrderConsumer.delayOrderConsumer();
	}

}

