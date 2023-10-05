package com.kes.service.impl;

import java.util.Collections;

import static com.kes.utils.AppConstants.*;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.kes.dto.DepartmentRequest;
import com.kes.dto.DepartmentResponse;
import com.kes.entity.Department;
import com.kes.exception.DepartmentServiceBusinessException;
import com.kes.exception.ResourceNotFoundException;
import com.kes.props.AppProperties;
import com.kes.repository.DepartmentRepository;
import com.kes.service.DepartmentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

	private DepartmentRepository departmentRepository;

	private ModelMapper modelMapper;

	private AppProperties appProperties;

	@Override
	public DepartmentResponse saveDepartment(DepartmentRequest departmentRequest) {

		DepartmentResponse departmentResponse = null;
		Department department = null;

		try {

			department = modelMapper.map(departmentRequest, Department.class);

			department = departmentRepository.save(department);

			departmentResponse = modelMapper.map(department, DepartmentResponse.class);

		} catch (Exception e) {
			throw new DepartmentServiceBusinessException(
					appProperties.getMessages().get(SAVE_DEPARTMENT_BUSINESS_EXCEPTION));
		}

		return departmentResponse;
	}

	@Override
	public List<DepartmentResponse> getAllDepartments() {

		List<DepartmentResponse> departmentResponse = null;
		List<Department> departments = null;
		try {

			departments = departmentRepository.findAll();
			if (!departments.isEmpty()) {

				departmentResponse = departments.stream()
						.map(department -> modelMapper.map(department, DepartmentResponse.class))
						.collect(Collectors.toList());

			} else {
				departments = Collections.emptyList();
			}

		} catch (Exception e) {
			throw new DepartmentServiceBusinessException(
					appProperties.getMessages().get(LIST_DEPARTMENTS_BUSINESS_EXCEPTION));
		}

		return departmentResponse;
	}

	@Cacheable(key = "#departmentId",value = "departments")
	@Override
	public DepartmentResponse getDepartmentById(Integer departmentId) {

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException((String
						.format(appProperties.getMessages().get(DEPARTMENT_NOT_FOUND_EXCEPTION), departmentId))));

		return modelMapper.map(department, DepartmentResponse.class);
	}

	@Cacheable(key = "#departmentCode",value = "departments")
	@Override
	public DepartmentResponse getDepartmentByCode(String departmentCode) {

		Department department = departmentRepository.findByDepartmentCode(departmentCode)
				.orElseThrow(() -> new ResourceNotFoundException((String.format(
						appProperties.getMessages().get(DEPARTMENT_NOT_FOUND_BY_CODE_EXCEPTION), departmentCode))));

		return modelMapper.map(department, DepartmentResponse.class);
	}

	@CachePut(key = "#departmentId",value = "departments")
	@Override
	public DepartmentResponse updateDepartment(Integer departmentId, DepartmentRequest departmentRequest) {

		DepartmentResponse departmentResponse = null;
		Department department = null;

		department = departmentRepository.findById(departmentId).orElseThrow(() -> new ResourceNotFoundException(
				(String.format(appProperties.getMessages().get(DEPARTMENT_NOT_FOUND_EXCEPTION), departmentId))));

		try {

			department.setDepartmentName(departmentRequest.getDepartmentName());
			department.setDepartmentCode(departmentRequest.getDepartmentCode());
			department.setDepartmentDescription(departmentRequest.getDepartmentDescription());

			department = departmentRepository.save(department);

			departmentResponse = modelMapper.map(department, DepartmentResponse.class);

		} catch (Exception e) {
			throw new DepartmentServiceBusinessException(
					appProperties.getMessages().get(UPDATE_DEPARTMENT_BUSINESS_EXCEPTION));
		}

		return departmentResponse;
	}

	@CacheEvict(key = "#departmentId",value = "departments")
	@Override
	public String deleteDepartment(Integer departmentId) {
		String message = null;
		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException((String
						.format(appProperties.getMessages().get(DEPARTMENT_NOT_FOUND_EXCEPTION), departmentId))));

		try {
			if (department != null) {
				departmentRepository.deleteById(departmentId);
				message = String.format(appProperties.getMessages().get(DEPARTMENT_DELETION_SUCCEEDED), departmentId);
			} else {
				message = String.format(appProperties.getMessages().get(DEPARTMENT_DELETION_FAILED), departmentId);
			}
		} catch (Exception e) {
			throw new DepartmentServiceBusinessException(
					appProperties.getMessages().get(DELETE_DEPARTMENT_BUSINESS_EXCEPTION));
		}

		return message;

	}

}
