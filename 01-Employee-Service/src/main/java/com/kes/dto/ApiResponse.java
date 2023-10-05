package com.kes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {

	private EmployeeResponse employeeResponse;

	private DepartmentResponse departmentResponse;
	
	private OrganizationResponse organizationResponse;
}
