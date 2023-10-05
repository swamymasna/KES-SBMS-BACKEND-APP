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

import com.kes.dto.OrganizationRequest;
import com.kes.dto.OrganizationResponse;
import com.kes.service.OrganizationService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/organizations")
@AllArgsConstructor
public class OrganizationRestController {

	private OrganizationService organizationService;

	@PostMapping
	public ResponseEntity<OrganizationResponse> saveOrganization(@RequestBody OrganizationRequest organizationRequest) {
		return new ResponseEntity<>(organizationService.saveOrganization(organizationRequest), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {
		return new ResponseEntity<>(organizationService.getAllOrganizations(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable("id") Integer organizationId) {
		return new ResponseEntity<>(organizationService.getOrganizationById(organizationId), HttpStatus.OK);
	}

	@GetMapping("/organization-code/{org-code}")
	public ResponseEntity<OrganizationResponse> getOrganizationByCode(
			@PathVariable("org-code") String organizationCode) {
		return new ResponseEntity<>(organizationService.getOrganizationByCode(organizationCode), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<OrganizationResponse> updateOrganization(@PathVariable("id") Integer organizationId,
			@RequestBody OrganizationRequest organizationRequest) {
		return new ResponseEntity<>(organizationService.updateOrganization(organizationId, organizationRequest),
				HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrganization(@PathVariable("id") Integer organizationId) {
		return new ResponseEntity<>(organizationService.deleteOrganization(organizationId), HttpStatus.OK);
	}

}
