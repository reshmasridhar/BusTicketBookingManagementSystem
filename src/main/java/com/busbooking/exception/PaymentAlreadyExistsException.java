package com.busbooking.exception;


public class PaymentAlreadyExistsException extends RuntimeException{
	
	public PaymentAlreadyExistsException(String message)
	{
		super(message);
	}

}

