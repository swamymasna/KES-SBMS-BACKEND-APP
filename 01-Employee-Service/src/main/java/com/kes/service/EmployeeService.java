package com.kes.service;

import java.util.List;

import com.kes.dto.ApiResponse;
import com.kes.dto.EmployeeRequest;
import com.kes.dto.EmployeeResponse;

public interface EmployeeService {

	EmployeeResponse saveEmployee(EmployeeRequest employeeRequest);

	List<EmployeeResponse> getAllEmployees();

	EmployeeResponse getEmployeeById(Integer employeeId);

	ApiResponse getEmployeeInfoById(Integer employeeId);

	String deleteEmployee(Integer employeeId);

	EmployeeResponse updateEmployee(Integer employeeId, EmployeeRequest employeeRequest);
}
