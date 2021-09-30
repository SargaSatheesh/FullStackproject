package com.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
@EnableJms
public class FsProjectApplication {

	
	
	

	public static void main(String[] args) {
		//SpringApplication.run(FsProjectApplication.class, args);
		
		ConfigurableApplicationContext context = SpringApplication.run(FsProjectApplication.class, args);

		 

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        //System.out.println("The processed record count :" +productItemProcessor.getScount());
        //System.out.println("The failed record count :" +productItemProcessor.getFcount());

	}

}
