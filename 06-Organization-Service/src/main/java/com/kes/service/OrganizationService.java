package com.kes.service;

import java.util.List;

import com.kes.dto.OrganizationRequest;
import com.kes.dto.OrganizationResponse;

public interface OrganizationService {

	OrganizationResponse saveOrganization(OrganizationRequest departmentRequest);

	List<OrganizationResponse> getAllOrganizations();

	OrganizationResponse getOrganizationById(Integer organizationId);

	OrganizationResponse getOrganizationByCode(String organizationCode);

	OrganizationResponse updateOrganization(Integer organizationId, OrganizationRequest organizationRequest);

	String deleteOrganization(Integer organizationId);

}
