package com.kes.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.kes.client.DepartmentClient;
import com.kes.client.OrganizationClient;
import com.kes.dto.ApiResponse;
import com.kes.dto.DepartmentResponse;
import com.kes.dto.EmployeeRequest;
import com.kes.dto.EmployeeResponse;
import com.kes.dto.OrganizationResponse;
import com.kes.entity.Employee;
import com.kes.exception.EmployeeServiceBusinessException;
import com.kes.exception.ResourceNotFoundException;
import com.kes.props.AppProperties;
import com.kes.repository.EmployeeRepository;
import com.kes.service.EmployeeService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import static com.kes.util.AppConstants.*;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeRepository employeeRepository;

	private ModelMapper modelMapper;

	private AppProperties appProperties;

	private DepartmentClient departmentClient;

	private OrganizationClient organizationClient;

	@Override
	public EmployeeResponse saveEmployee(EmployeeRequest employeeRequest) {

		EmployeeResponse employeeResponse = null;
		Employee employee = null;

		try {
			employee = modelMapper.map(employeeRequest, Employee.class);

			employee = employeeRepository.save(employee);

			employeeResponse = modelMapper.map(employee, EmployeeResponse.class);

		} catch (Exception e) {
			throw new EmployeeServiceBusinessException(
					appProperties.getMessages().get(SAVE_EMPLOYEE_BUSINESS_EXCEPTION));
		}

		return employeeResponse;
	}

	@Override
	public List<EmployeeResponse> getAllEmployees() {

		List<EmployeeResponse> employeeResponse = null;

		try {
			List<Employee> employees = employeeRepository.findAll();

			employeeResponse = employees.stream().map(employee -> modelMapper.map(employee, EmployeeResponse.class))
					.collect(Collectors.toList());

		} catch (Exception e) {
			throw new EmployeeServiceBusinessException(
					appProperties.getMessages().get(LIST_EMPLOYEES_BUSINESS_EXCEPTION));
		}

		return employeeResponse;
	}

	@Cacheable(key = "#employeeId",value = "employees")
	@Override
	public EmployeeResponse getEmployeeById(Integer employeeId) {

		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(appProperties.getMessages().get(EMPLOYEE_NOT_FOUND_EXCEPTION), employeeId)));

		return modelMapper.map(employee, EmployeeResponse.class);
	}

	@CacheEvict(key = "#employeeId",value = "employees")
	@Override
	public String deleteEmployee(Integer employeeId) {

		String message = null;
		Employee employee = null;

		employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(appProperties.getMessages().get(EMPLOYEE_NOT_FOUND_EXCEPTION), employeeId)));

		try {
			employeeRepository.deleteById(employeeId);
			message = appProperties.getMessages().get(EMPLOYEE_DELETION_SUCCEEDED) + employeeId;
		} catch (Exception e) {
			throw new EmployeeServiceBusinessException(appProperties.getMessages().get(EMPLOYEE_DELETION_FAILED));
		}

		return message;
	}

	@CachePut(key = "#employeeId",value = "employees")
	@Override
	public EmployeeResponse updateEmployee(Integer employeeId, EmployeeRequest employeeRequest) {
		EmployeeResponse employeeResponse = null;
		Employee employee = null;

		employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(appProperties.getMessages().get(EMPLOYEE_NOT_FOUND_EXCEPTION), employeeId)));

		try {

			employee.setEmployeeName(employeeRequest.getEmployeeName());
			employee.setEmployeeSalary(employeeRequest.getEmployeeSalary());
			employee.setEmployeeAddress(employeeRequest.getEmployeeAddress());
			employee.setEmail(employeeRequest.getEmail());

			employee = employeeRepository.save(employee);

			employeeResponse = modelMapper.map(employee, EmployeeResponse.class);

		} catch (Exception e) {
			throw new EmployeeServiceBusinessException(
					appProperties.getMessages().get(UPDATE_EMPLOYEE_BUSINESS_EXCEPTION));
		}

		return employeeResponse;
	}

	@Cacheable(key = "#employeeId",value = "employees")
	@CircuitBreaker(name = "EMPLOYEE-SERVICE", fallbackMethod = "defaultGetEmployeeInfoById")
	@Override
	public ApiResponse getEmployeeInfoById(Integer employeeId) {

		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(appProperties.getMessages().get(EMPLOYEE_NOT_FOUND_EXCEPTION), employeeId)));

		EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);

		DepartmentResponse departmentResponse = departmentClient.getDepartmentByCode(employee.getDepartmentCode());

		OrganizationResponse organizationResponse = organizationClient
				.getOrganizationByCode(employee.getOrganizationCode());

		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setEmployeeResponse(employeeResponse);
		apiResponse.setDepartmentResponse(departmentResponse);
		apiResponse.setOrganizationResponse(organizationResponse);

		return apiResponse;
	}

	public ApiResponse defaultGetEmployeeInfoById(Integer employeeId, Exception exception) {

		Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
				String.format(appProperties.getMessages().get(EMPLOYEE_NOT_FOUND_EXCEPTION), employeeId)));

		EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);

		DepartmentResponse departmentResponse = new DepartmentResponse();
		departmentResponse.setDepartmentId(0);
		departmentResponse.setDepartmentName("DEFAULT-DEPARTMENT-NAME");
		departmentResponse.setDepartmentCode("DEFAULT-DEPARTMENT-CODE");
		departmentResponse.setDepartmentDescription("DEFAULT-DEPARTMENT-DESCRIPTION");

		OrganizationResponse organizationResponse = new OrganizationResponse();
		organizationResponse.setOrganizationId(0);
		organizationResponse.setOrganizationName("DEFAULT-ORGANIZATION-NAME");
		organizationResponse.setOrganizationCode("DEFAULT-ORGANIZATION-CODE");
		organizationResponse.setOrganizationDescription("DEFAULT-ORGANIZATION-DESCRIPTION");

		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setEmployeeResponse(employeeResponse);
		apiResponse.setDepartmentResponse(departmentResponse);
		apiResponse.setOrganizationResponse(organizationResponse);

		return apiResponse;
	}
}
