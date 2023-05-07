package com.employee.manage.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.manage.entity.Employee;
import com.employee.manage.exception.EmployeeNotFoundException;
import com.employee.manage.repository.EmployeeRepository;
import com.employee.manage.service.EmployeeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Mono<Employee> saveEmployee(Employee employee) {
		return employeeRepository.save(employee.setAsNew());
	}

	@Override
	public Mono<Employee> getEmployeeById(int id) {
		return employeeRepository.findById(id).switchIfEmpty(Mono.error(new EmployeeNotFoundException(id)));
	}

	@Override
	public Flux<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public Mono<Employee> updateEmployee(Employee employee) {
		return employeeRepository.save(employee).switchIfEmpty(this.employeeRepository.save(employee.setAsNew()));
	}

	@Override
	public Mono<Boolean> deleteEmployeeById(int id) {
		employeeRepository.deleteById(id).subscribe();
		return Mono.just(true);
	}

}
