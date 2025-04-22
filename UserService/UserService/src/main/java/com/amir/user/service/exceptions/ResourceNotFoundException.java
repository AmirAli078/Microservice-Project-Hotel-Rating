package com.amir.user.service.exceptions;

public class ResourceNotFoundException extends RuntimeException{

	// extra properties if you want to add 
	public ResourceNotFoundException() {
		super("Resource Not Found on Server! ");
	}
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
}
