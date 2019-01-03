package com.sun.activeMq.boot.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

@Configuration// 配置文件注解
@EnableJms// 告诉Spring容器，这个class文件是与jms相关的配置文件
public class ActiveMqConfig {

	@Value("${spring.activemq.user}")
	private String usrName;

	@Value("${spring.activemq.password}")
	private String password;

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	// 将ActivteMq连接工厂以@Bean的形式，注入到Spring容器中
	@Bean
	public ConnectionFactory connectionFactory () {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(brokerUrl);
		connectionFactory.setUserName(usrName);
		connectionFactory.setPassword(brokerUrl);
		return connectionFactory;
	}

    @Bean("jmsTopicListenerContainerFactory")
    public JmsListenerContainerFactory jmsTopicListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 设置topic模式
        factory.setPubSubDomain(true);
        return factory;
    }
}
