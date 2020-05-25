package com.sandeep.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sandeep.springboot.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
