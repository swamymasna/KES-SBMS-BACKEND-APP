package com.kes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kes.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
