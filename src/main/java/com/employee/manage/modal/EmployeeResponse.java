package com.employee.manage.modal;

import java.util.ArrayList;
import java.util.List;

import com.employee.manage.entity.Employee;

import lombok.Data;

@Data
public class EmployeeResponse extends EmployeeBaseResponse {
	private List<Employee> data = new ArrayList<>();
}
