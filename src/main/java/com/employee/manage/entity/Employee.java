package com.employee.manage.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Table
@Data
public class Employee implements Persistable<Integer> {
	@Id
	private Integer id;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@JsonIgnore
	@Transient
	private boolean newEmployee;

	@JsonIgnore
	@Override
	@Transient
	public boolean isNew() {
		return this.newEmployee || (id == null);
	}

	public Employee setAsNew() {
		this.newEmployee = true;
		return this;
	}
}
