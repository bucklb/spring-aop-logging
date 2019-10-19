package net.guides.springboot2.springboot2jpacrudexample.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.guides.springboot2.springboot2jpacrudexample.exception.ResourceNotFoundException;
import net.guides.springboot2.springboot2jpacrudexample.model.Employee;
import net.guides.springboot2.springboot2jpacrudexample.repository.EmployeeRepository;

/**
 * Employee Service
 * @author Ramesh
 *
 */
@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	// Check whether "internal" calls will be picked up by AOP
	private String uselessPrivateCall() {
		return "Called private";
	}
	private String uselessPublicCall() {
		return "Called public";
	}


	public Optional<Employee> getEmployeeById(Long employeeId)
			throws ResourceNotFoundException {

		String pv=uselessPrivateCall();
		String pc=uselessPublicCall();

		return employeeRepository.findById(employeeId);
	}

	public Employee addEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	public Employee updateEmployee(Long employeeId,
			Employee employeeDetails) throws ResourceNotFoundException {

		// Allow possibility of throwing an exception within the service AND NOT being a not found exception ...
		if(employeeId > 69) {
			throw new RuntimeException("It's too BIG!");
		}


		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employee.setEmailId(employeeDetails.getEmailId());
		employee.setLastName(employeeDetails.getLastName());
		employee.setFirstName(employeeDetails.getFirstName());




		// See if call to employee can be caught by AOP too
		employee.updateEmployeeEmailsId("forbiddden@access.com");

		final Employee updatedEmployee = employeeRepository.save(employee);
		return updatedEmployee;
	}

	public Map<String, Boolean> deleteEmployee(Long employeeId)
			throws ResourceNotFoundException {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
