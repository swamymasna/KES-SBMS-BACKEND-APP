package com.kes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kes.dto.OrganizationResponse;

@FeignClient(name = "ORGANIZATION-SERVICE")
public interface OrganizationClient {

	@GetMapping("/api/organizations/organization-code/{org-code}")
	public OrganizationResponse getOrganizationByCode(@PathVariable("org-code") String organizationCode);

}
