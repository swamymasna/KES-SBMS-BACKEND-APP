package com.kes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

	@NotEmpty(message = "employee name must be null or empty")
	@Size(min = 2, message = "employee name should have atleast 2 charecters")
	private String employeeName;

	@NotNull(message = "employee salary must not be null")
	private Double employeeSalary;

	@NotEmpty(message = "employee address must be null or empty")
	@Size(min = 2, message = "employee address should have atleast 2 charecters")
	private String employeeAddress;

	@NotEmpty(message = "employee address must be null or empty")
	@Email
	private String email;
	
	@NotEmpty(message = "department code must be null or empty")
	@Size(min = 2, message = "department code should have atleast 2 charecters")
	private String departmentCode;
	
	@NotEmpty(message = "organization code must be null or empty")
	@Size(min = 2, message = "organization code should have atleast 2 charecters")
	private String organizationCode;

}
