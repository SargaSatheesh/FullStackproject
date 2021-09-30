package com.training.errorhandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

import com.training.bean.FailedConsumer;

public class DefaultErrorHandler implements ErrorHandler {

    private static Logger log = LoggerFactory.getLogger(FailedConsumer.class);

    @Override
    public void handleError(Throwable t) {
        log.warn("spring jms custom error handling example");
        log.error(t.getCause().getMessage());
    }
}