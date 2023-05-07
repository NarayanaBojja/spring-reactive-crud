package com.employee.manage.exception;

public class EmployeeNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int id;

	public EmployeeNotFoundException(int id) {
		this.id = id;
	}
}
