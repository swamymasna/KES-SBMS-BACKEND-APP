package com.kes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kes.dto.DepartmentResponse;

@FeignClient(name = "DEPARTMENT-SERVICE")
public interface DepartmentClient {

	@GetMapping("/api/departments/department-code/{dept-code}")
	public DepartmentResponse getDepartmentByCode(@PathVariable("dept-code") String departmentCode);
}
