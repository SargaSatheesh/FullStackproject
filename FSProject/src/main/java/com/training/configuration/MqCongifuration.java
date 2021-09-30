package com.training.configuration;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.region.Queue;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.training.errorhandler.DefaultErrorHandler;

@Configuration
public class MqCongifuration {
	
	@Value("${spring.activemq.broker-url}")
	private String mqUrl;
	
	@Value("${myqueue}")
	private String queue;
	@Value("${fqueue}")
	private String failedqueue;
	
	
	@Bean
	public ActiveMQQueue getMQ() 
	{
		return new ActiveMQQueue(queue);
	}
	
	@Bean
	public ActiveMQQueue getfailedMQ() 
	{
		return new ActiveMQQueue(failedqueue);
	}

	@Bean
	public ActiveMQConnectionFactory getMqFactory()
	{
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(mqUrl);
		return factory;
	}
	
	public JmsTemplate getTemplate()
	{
		return new JmsTemplate(getMqFactory());
	}
	
	@Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                            DefaultJmsListenerContainerFactoryConfigurer configurer) {
      DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
      // This provides all boot's default to this factory, including the message converter
      configurer.configure(factory, connectionFactory);
      factory.setErrorHandler(new DefaultErrorHandler());
      // You could still override some of Boot's default if necessary.
      return factory;
    }

	@Bean
    // Serialize message content to json using TextMessage public
    MessageConverter jacksonJmsMessageConverter()
    {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    return converter; }
	
	
	
	
}
