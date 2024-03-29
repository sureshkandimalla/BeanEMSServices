package com.bean.exception;

public class EmployeeServiceException extends Exception {

	 public EmployeeServiceException(String message,Exception e) {
	        super(message,e);
	    }

	    public EmployeeServiceException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}
