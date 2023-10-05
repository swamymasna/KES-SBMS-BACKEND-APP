package com.kes.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kes.dto.ApiResponse;
import com.kes.dto.DepartmentResponse;
import com.kes.dto.EmployeeRequest;
import com.kes.dto.EmployeeResponse;
import com.kes.dto.OrganizationResponse;
import com.kes.service.EmployeeService;

@WebMvcTest
public class EmployeeRestControllerTest {

	@MockBean
	private EmployeeService employeeService;

	@Autowired
	private MockMvc mockMvc;

	private EmployeeRequest employeeRequest;

	private EmployeeResponse employeeResponse;

	@Autowired
	private ObjectMapper objectMapper;

	private String employeeJson;

	@BeforeEach
	public void setup() throws Exception {

		employeeRequest = EmployeeRequest.builder().employeeName("swamy").employeeSalary(56000.00)
				.employeeAddress("hyd").email("swamy@gmail.com").departmentCode("dev001").organizationCode("tcs001")
				.build();

		employeeResponse = EmployeeResponse.builder().employeeId(1).employeeName("swamy").employeeSalary(56000.00)
				.employeeAddress("hyd").email("swamy@gmail.com").departmentCode("dev001").organizationCode("tcs001")
				.build();

		employeeJson = objectMapper.writeValueAsString(employeeRequest);
	}

	@Test
	public void whenEmployee_thenSavedEmployee() throws Exception {
		
		when(employeeService.saveEmployee(employeeRequest)).thenReturn(employeeResponse);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/employees")
		.contentType(MediaType.APPLICATION_JSON)
		.content(employeeJson);
		
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isCreated());
		
	}

	@Test
	public void whenListEmployees_thenEmployeesList() throws Exception {
		
		when(employeeService.getAllEmployees()).thenReturn(List.of(employeeResponse));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/employees");
		
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());
		
	}

	@Test
	public void whenEmployeeId_thenEmployee() throws Exception {

		Integer empId = 1;

		when(employeeService.getEmployeeById(empId)).thenReturn(employeeResponse);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/employees/{id}", empId);

		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());

	}

	@Test
	public void whenEmployeeId_thenUpdatedEmployee() throws Exception {

		Integer empId = 1;

		when(employeeService.getEmployeeById(empId)).thenReturn(employeeResponse);

		when(employeeService.updateEmployee(empId, employeeRequest)).thenReturn(employeeResponse);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/employees/{id}", empId)
				.contentType(MediaType.APPLICATION_JSON).content(employeeJson);

		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());

	}

	@Test
	public void whenEmployeeId_thenDeletedEmployee() throws Exception {

		Integer empId = 1;

		when(employeeService.getEmployeeById(empId)).thenReturn(employeeResponse);

		when(employeeService.deleteEmployee(empId)).thenReturn("SUCCESS");

		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/employees/{id}", empId);

		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());

	}

//	@Test
	public void whenEmployeeId_thenEmployeesInfo() throws Exception {

		Integer empId = 1;

		EmployeeResponse employeeResponse = EmployeeResponse.builder().employeeId(1).employeeName("swamy")
				.employeeSalary(56000.00).employeeAddress("hyd").email("swamy@gmail.com").departmentCode("dev001")
				.organizationCode("tcs001").build();

		DepartmentResponse departmentResponse = DepartmentResponse.builder().departmentId(1).departmentName("dev")
				.departmentCode("dev001").departmentDescription("dev-dept").build();

		OrganizationResponse organizationResponse = OrganizationResponse.builder().organizationId(1)
				.organizationName("tcs").organizationCode("tcs001").organizationDescription("tcs-desc").build();

		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setEmployeeResponse(employeeResponse);
		apiResponse.setDepartmentResponse(departmentResponse);
		apiResponse.setOrganizationResponse(organizationResponse);

		when(employeeService.getEmployeeInfoById(empId)).thenReturn(apiResponse);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/employees/employee-id/{id}", empId);

		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk());

	}

}
