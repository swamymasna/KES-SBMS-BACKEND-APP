package com.kes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kes.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	Optional<Department> findByDepartmentCode(String departmentCode);
}
