package com.kes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

	private Integer employeeId;

	private String employeeName;

	private Double employeeSalary;

	private String employeeAddress;

	private String email;
	
	private String departmentCode;
	
	private String organizationCode;

}
