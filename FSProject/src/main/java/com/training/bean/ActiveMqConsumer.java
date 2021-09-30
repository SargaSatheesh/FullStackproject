package com.training.bean;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;




@Component

public class ActiveMqConsumer {

	/*
	 * @JmsListener(destination = "testQueue") public void listener(String message)
	 * {
	 * 
	 * System.out.println("Received Message: " + message); }
	 */
	
	 @JmsListener(destination = "productQueue", containerFactory = "myFactory")
	  public void receiveMessage(Product product)
	 {
		 //throw new runtimeexception
	    System.out.println("Received <" + product + ">");
	    
	 }}
