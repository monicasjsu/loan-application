package com.esp.project.Exceptions;


public class ApplicationException extends RuntimeException {

	public ApplicationException(){}

	public ApplicationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
