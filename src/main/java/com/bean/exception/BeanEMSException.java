package com.bean.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BeanEMSException {
	
	 @ExceptionHandler(InvoiceException.class)
	    public ResponseEntity<String> handleInvoiceException(InvoiceException ex) {
	        // Log the exception or perform additional handling if needed
	        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }

}
