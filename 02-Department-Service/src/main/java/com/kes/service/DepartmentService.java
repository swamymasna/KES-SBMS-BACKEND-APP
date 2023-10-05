package com.kes.service;

import java.util.List;

import com.kes.dto.DepartmentRequest;
import com.kes.dto.DepartmentResponse;

public interface DepartmentService {

	DepartmentResponse saveDepartment(DepartmentRequest departmentRequest);

	List<DepartmentResponse> getAllDepartments();

	DepartmentResponse getDepartmentById(Integer departmentId);

	DepartmentResponse getDepartmentByCode(String departmentCode);

	DepartmentResponse updateDepartment(Integer departmentId, DepartmentRequest departmentRequest);

	String deleteDepartment(Integer departmentId);

}
