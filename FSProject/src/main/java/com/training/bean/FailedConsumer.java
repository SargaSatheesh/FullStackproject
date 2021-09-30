package com.training.bean;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;

import com.training.exception.ProductException;
@Component

public class FailedConsumer {
	@JmsListener(destination = "failedQueue", containerFactory = "myFactory")
	public void receiveMessage(Product product)
	{

		//System.out.println("Received <" + product + ">");
		throw new ProductException("problem occurred while processing the order.");

	}

}
