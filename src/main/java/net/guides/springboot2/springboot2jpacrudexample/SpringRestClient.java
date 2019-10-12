package net.guides.springboot2.springboot2jpacrudexample;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import net.guides.springboot2.springboot2jpacrudexample.model.Employee;

public class SpringRestClient {

	private static final String GET_EMPLOYEES_ENDPOINT_URL = "http://localhost:8080/api/v1/employees";
	private static final String GET_EMPLOYEE_ENDPOINT_URL = "http://localhost:8080/api/v1/employees/{id}";
	private static final String CREATE_EMPLOYEE_ENDPOINT_URL = "http://localhost:8080/api/v1/employees";
	private static final String UPDATE_EMPLOYEE_ENDPOINT_URL = "http://localhost:8080/api/v1/employees/{id}";
	private static final String DELETE_EMPLOYEE_ENDPOINT_URL = "http://localhost:8080/api/v1/employees/{id}";
	private static RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringRestClient springRestClient = new SpringRestClient();

		// Add a bit of flexibility by bandying around the id
		long id=0;
		
		// Step1: first create a new employee
		id = springRestClient.createEmployee();

		// Step 2: get new created employee from step1
		springRestClient.getEmployeeById(id);
		
		// Step3: get all employees
		springRestClient.getEmployees();
		
		// Step4: Update employee with id = 1
		springRestClient.updateEmployee(id);
		
		// Step5: Delete employee with id = 1
		springRestClient.deleteEmployee(id);

		// see what breaks
//		springRestClient.forceInputException();



	}

	private void getEmployees() {

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

		ResponseEntity<String> result = restTemplate.exchange(GET_EMPLOYEES_ENDPOINT_URL, HttpMethod.GET, entity,
				String.class);

		System.out.println(result);
	}

	private void getEmployeeById(long id) {

		Map<String, String> params = new HashMap<String, String>();
//		params.put("id", "1");
		params.put("id", String.valueOf(id));

		RestTemplate restTemplate = new RestTemplate();
		Employee result = restTemplate.getForObject(GET_EMPLOYEE_ENDPOINT_URL, Employee.class, params);

		System.out.println(result);
	}

	private long createEmployee() {

		Employee newEmployee = new Employee("admin", "admin", "admin@gmail.com");

		RestTemplate restTemplate = new RestTemplate();
		Employee result = restTemplate.postForObject(CREATE_EMPLOYEE_ENDPOINT_URL, newEmployee, Employee.class);

		System.out.println(result);

		return result.getId();

	}

	private void updateEmployee(long id) {
		Map<String, String> params = new HashMap<String, String>();
//		params.put("id", "1");
		params.put("id", String.valueOf(id));
		Employee updatedEmployee = new Employee("admin123", "admin123", "admin123@gmail.com");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.put(UPDATE_EMPLOYEE_ENDPOINT_URL, updatedEmployee, params);
	}

	private void deleteEmployee(long id) {
		Map<String, String> params = new HashMap<String, String>();
//		params.put("id", "1");
		params.put("id", String.valueOf(id));
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.delete(DELETE_EMPLOYEE_ENDPOINT_URL, params);
	}

	// Would like to see how the exception handler copes with malformed request
	private void forceException() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", "one and only");

		RestTemplate restTemplate = new RestTemplate();
		Employee result = restTemplate.getForObject(GET_EMPLOYEE_ENDPOINT_URL, Employee.class, params);

		System.out.println(result);
	}









}
