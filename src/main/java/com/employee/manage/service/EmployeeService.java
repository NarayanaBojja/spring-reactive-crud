package com.employee.manage.service;

import com.employee.manage.entity.Employee;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
	public Mono<Employee> saveEmployee(Employee employee);

	public Mono<Employee> getEmployeeById(int id);

	public Flux<Employee> getAllEmployees();

	public Mono<Employee> updateEmployee(Employee employee);

	public Mono<Boolean> deleteEmployeeById(int id);
}
