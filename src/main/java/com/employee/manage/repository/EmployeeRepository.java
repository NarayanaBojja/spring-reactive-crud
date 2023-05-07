package com.employee.manage.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.employee.manage.entity.Employee;
@Repository
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Integer> {

}
