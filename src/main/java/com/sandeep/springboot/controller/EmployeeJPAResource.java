package com.sandeep.springboot.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sandeep.springboot.exception.EmployeeNotFoundException;
import com.sandeep.springboot.model.Employee;
import com.sandeep.springboot.repository.EmployeeRepository;

@RestController
public class EmployeeJPAResource {

	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("/jpa/employees")
	public List<Employee> retriveAllEmployees() {
		return employeeRepository.findAll();
	}

	@GetMapping("/jpa/employees/{id}")
	public EntityModel<Employee> retrieveEmployee(@PathVariable int id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if (!employee.isPresent())
			throw new EmployeeNotFoundException("Employee with ID:" + id + " doesn't exist");
		EntityModel<Employee> model = new EntityModel<Employee>(employee.get());
		WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retriveAllEmployees());
		model.add(linkTo.withRel("all-users"));
		return model;
	}

	@DeleteMapping("/jpa/employees/{id}")
	public void deleteUser(@PathVariable int id) {
		employeeRepository.deleteById(id);
	}

	@PostMapping("/jpa/employees")
	public ResponseEntity<Object> createUser(@Valid @RequestBody Employee employee) {
		Employee savedEmployee = employeeRepository.save(employee);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedEmployee.getId()).toUri();

		return ResponseEntity.created(location).build();

	}

}
