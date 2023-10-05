package com.kes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kes.dto.DepartmentRequest;
import com.kes.dto.DepartmentResponse;
import com.kes.service.DepartmentService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/departments")
@AllArgsConstructor
public class DepartmentRestController {

	private DepartmentService departmentService;

	@PostMapping
	public ResponseEntity<DepartmentResponse> saveDepartment(@RequestBody DepartmentRequest departmentRequest) {
		return new ResponseEntity<>(departmentService.saveDepartment(departmentRequest), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
		return new ResponseEntity<>(departmentService.getAllDepartments(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable("id") Integer departmentId) {
		return new ResponseEntity<>(departmentService.getDepartmentById(departmentId), HttpStatus.OK);
	}

	@GetMapping("/department-code/{dept-code}")
	public ResponseEntity<DepartmentResponse> getDepartmentByCode(@PathVariable("dept-code") String departmentCode) {
		return new ResponseEntity<>(departmentService.getDepartmentByCode(departmentCode), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable("id") Integer departmentId,
			@RequestBody DepartmentRequest departmentRequest) {
		return new ResponseEntity<>(departmentService.updateDepartment(departmentId, departmentRequest), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteDepartment(@PathVariable("id") Integer departmentId) {
		return new ResponseEntity<>(departmentService.deleteDepartment(departmentId), HttpStatus.OK);
	}

}
