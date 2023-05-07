package com.employee.manage.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.manage.constants.EmployeeConstants;
import com.employee.manage.entity.Employee;
import com.employee.manage.exception.EmployeeNotFoundException;
import com.employee.manage.modal.EmployeeResponse;
import com.employee.manage.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Controller to handle employee create, get, get all, update , delete requests
 *
 */
@RequestMapping("/employee")
@RestController
@Slf4j
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@PostMapping
	public Mono<ResponseEntity<EmployeeResponse>> saveEmployee(
			@RequestHeader(name = EmployeeConstants.USER_ID) String userId,
			@RequestHeader(name = EmployeeConstants.CORRELATION_ID) String correlationId,
			@RequestBody Employee employee) {
		log.info("EmployeeController::saveEmployee");
		Instant start = Instant.now();
		var response = new EmployeeResponse();
		return employeeService.saveEmployee(employee).flatMap(resp -> {
			response.setStatus(1);
			printAPIProcessTime(start, EmployeeConstants.SAVE_EMP_PROCESS);
			log.info("Employee is created succussfully");
			return Mono.just(ResponseEntity.ok(response));
		}).onErrorResume(error -> {
			response.setStatus(-1);
			response.setErrorMessage(EmployeeConstants.ERROR);
			log.error(EmployeeConstants.ERROR, error);
			printAPIProcessTime(start, EmployeeConstants.SAVE_EMP_PROCESS);
			return Mono.just(ResponseEntity.internalServerError().body(response));
		});
	}

	@GetMapping("/{employeeId}")
	public Mono<ResponseEntity<EmployeeResponse>> getEmployeeById(
			@RequestHeader(name = EmployeeConstants.USER_ID) String userId,
			@RequestHeader(name = EmployeeConstants.CORRELATION_ID) String correlationId,
			@PathVariable int employeeId) {
		log.info("EmployeeController::getEmployeeById");
		Instant start = Instant.now();
		var response = new EmployeeResponse();
		return employeeService.getEmployeeById(employeeId).flatMap(resp -> {
			response.setStatus(1);
			response.setData(List.of(resp));
			printAPIProcessTime(start, EmployeeConstants.GET_EMP_PROCESS);
			log.info("Employee details are found");
			return Mono.just(ResponseEntity.ok(response));
		}).onErrorResume(error -> {
			response.setStatus(-1);
			if(error instanceof EmployeeNotFoundException) {
				response.setErrorMessage(EmployeeConstants.EMP_NOT_FOUND);
			} else {
				response.setErrorMessage(EmployeeConstants.ERROR);				
			}
			printAPIProcessTime(start, EmployeeConstants.GET_EMP_PROCESS);
			log.error(EmployeeConstants.ERROR, error);
			return Mono.just(ResponseEntity.internalServerError().body(response));
		});
	}

	@GetMapping("/all")
	public Mono<ResponseEntity<EmployeeResponse>> getAllEmployees(
			@RequestHeader(name = EmployeeConstants.USER_ID) String userId,
			@RequestHeader(name = EmployeeConstants.CORRELATION_ID) String correlationId) {
		log.info("EmployeeController::getAllEmployees");
		Instant start = Instant.now();
		var response = new EmployeeResponse();
		return employeeService.getAllEmployees().collectList().flatMap(empList -> {
			response.setStatus(1);
			response.setData(empList);
			printAPIProcessTime(start, EmployeeConstants.GET_ALL_EMP_PROCESS);
			log.info("All Employee details are retrieved");
			return Mono.just(ResponseEntity.ok(response));
		}).onErrorResume(error -> {
			response.setStatus(-1);
			response.setErrorMessage(EmployeeConstants.ERROR);
			printAPIProcessTime(start, EmployeeConstants.GET_ALL_EMP_PROCESS);
			log.error(EmployeeConstants.ERROR, error);
			return Mono.just(ResponseEntity.internalServerError().body(response));
		});
	}

	@PutMapping
	public Mono<ResponseEntity<EmployeeResponse>> updateEmployee(
			@RequestHeader(name = EmployeeConstants.USER_ID) String userId,
			@RequestHeader(name = EmployeeConstants.CORRELATION_ID) String correlationId,
			@RequestBody Employee employee) {
		log.info("EmployeeController::updateEmployee");
		Instant start = Instant.now();
		var response = new EmployeeResponse();
		return employeeService.updateEmployee(employee).flatMap(resp -> {
			response.setStatus(1);
			response.setData(List.of(resp));
			printAPIProcessTime(start, EmployeeConstants.UPDATE_EMP_PROCESS);
			log.info("Employee is updated succussfully");
			return Mono.just(ResponseEntity.ok(response));
		}).onErrorResume(error -> {
			response.setStatus(-1);
			response.setErrorMessage(EmployeeConstants.ERROR);				
			log.error(EmployeeConstants.ERROR, error);
			printAPIProcessTime(start, EmployeeConstants.UPDATE_EMP_PROCESS);
			return Mono.just(ResponseEntity.internalServerError().body(response));
		});
	}

	@DeleteMapping("/{employeeId}")
	public Mono<ResponseEntity<EmployeeResponse>> deleteEmployeeById(
			@RequestHeader(name = EmployeeConstants.USER_ID) String userId,
			@RequestHeader(name = EmployeeConstants.CORRELATION_ID) String correlationId,
			@PathVariable int employeeId) {
		log.info("EmployeeController::deleteEmployeeById");
		Instant start = Instant.now();
		var response = new EmployeeResponse();
		return employeeService.deleteEmployeeById(employeeId).flatMap(resp -> {
			response.setStatus(1);
			printAPIProcessTime(start, EmployeeConstants.DELETE_EMP_PROCESS);
			log.info("Employee is delete succussfully");
			return Mono.just(ResponseEntity.ok(response));
		}).onErrorResume(error -> {
			response.setStatus(-1);
			if(error instanceof EmployeeNotFoundException) {
				response.setErrorMessage(EmployeeConstants.EMP_NOT_FOUND);
			} else {
				response.setErrorMessage(EmployeeConstants.ERROR);				
			}
			log.error(EmployeeConstants.ERROR, error);
			printAPIProcessTime(start, EmployeeConstants.DELETE_EMP_PROCESS);
			return Mono.just(ResponseEntity.internalServerError().body(response));
		});
	}

	private void printAPIProcessTime(Instant start, String process) {
		Instant finish = Instant.now();
		long timeElapsed = Duration.between(start, finish).toMillis();
		MDC.put("processTime", (timeElapsed / 1000) + "");
		log.info("Time taken to proccess {} is {}", process, (timeElapsed / 1000));
	}
}
