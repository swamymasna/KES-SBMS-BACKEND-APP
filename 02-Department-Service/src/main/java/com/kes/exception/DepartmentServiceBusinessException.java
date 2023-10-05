package com.kes.exception;

public class DepartmentServiceBusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DepartmentServiceBusinessException(String message) {
		super(message);
	}
}
