package com.kes.service.impl;

import static com.kes.utils.AppConstants.DELETE_ORGANIZATION_BUSINESS_EXCEPTION;
import static com.kes.utils.AppConstants.LIST_ORGANIZATIONS_BUSINESS_EXCEPTION;
import static com.kes.utils.AppConstants.ORGANIZATION_DELETION_FAILED;
import static com.kes.utils.AppConstants.ORGANIZATION_DELETION_SUCCEEDED;
import static com.kes.utils.AppConstants.ORGANIZATION_NOT_FOUND_BY_CODE_EXCEPTION;
import static com.kes.utils.AppConstants.ORGANIZATION_NOT_FOUND_EXCEPTION;
import static com.kes.utils.AppConstants.SAVE_ORGANIZATION_BUSINESS_EXCEPTION;
import static com.kes.utils.AppConstants.UPDATE_ORGANIZATION_BUSINESS_EXCEPTION;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.kes.dto.OrganizationRequest;
import com.kes.dto.OrganizationResponse;
import com.kes.entity.Organization;
import com.kes.exception.OrganizationServiceBusinessException;
import com.kes.exception.ResourceNotFoundException;
import com.kes.props.AppProperties;
import com.kes.repository.OrganizationRepository;
import com.kes.service.OrganizationService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

	private OrganizationRepository organizationRepository;

	private ModelMapper modelMapper;

	private AppProperties appProperties;

	@Override
	public OrganizationResponse saveOrganization(OrganizationRequest organizationRequest) {

		OrganizationResponse organizationResponse = null;
		Organization organization = null;

		try {

			organization = modelMapper.map(organizationRequest, Organization.class);

			organization = organizationRepository.save(organization);

			organizationResponse = modelMapper.map(organization, OrganizationResponse.class);

		} catch (Exception e) {
			throw new OrganizationServiceBusinessException(
					appProperties.getMessages().get(SAVE_ORGANIZATION_BUSINESS_EXCEPTION));
		}

		return organizationResponse;
	}

	@Override
	public List<OrganizationResponse> getAllOrganizations() {

		List<OrganizationResponse> organizationResponse = null;
		List<Organization> organizations = null;
		try {

			organizations = organizationRepository.findAll();
			if (!organizations.isEmpty()) {

				organizationResponse = organizations.stream()
						.map(organization -> modelMapper.map(organization, OrganizationResponse.class))
						.collect(Collectors.toList());

			} else {
				organizationResponse = Collections.emptyList();
			}

		} catch (Exception e) {
			throw new OrganizationServiceBusinessException(
					appProperties.getMessages().get(LIST_ORGANIZATIONS_BUSINESS_EXCEPTION));
		}

		return organizationResponse;
	}

	@Cacheable(key = "#organizationId",value = "organizations")
	@Override
	public OrganizationResponse getOrganizationById(Integer organizationId) {

		Organization organization = organizationRepository.findById(organizationId)
				.orElseThrow(() -> new ResourceNotFoundException((String
						.format(appProperties.getMessages().get(ORGANIZATION_NOT_FOUND_EXCEPTION), organizationId))));

		return modelMapper.map(organization, OrganizationResponse.class);
	}

	@Cacheable(key = "#organizationCode",value = "organizations")
	@Override
	public OrganizationResponse getOrganizationByCode(String organizationCode) {

		Organization organization = organizationRepository.findByOrganizationCode(organizationCode)
				.orElseThrow(() -> new ResourceNotFoundException((String.format(
						appProperties.getMessages().get(ORGANIZATION_NOT_FOUND_BY_CODE_EXCEPTION), organizationCode))));

		return modelMapper.map(organization, OrganizationResponse.class);
	}

	@CachePut(key = "#organizationId",value = "organizations")
	@Override
	public OrganizationResponse updateOrganization(Integer organizationId, OrganizationRequest organizationRequest) {

		OrganizationResponse organizationResponse = null;
		Organization organization = null;

		organization = organizationRepository.findById(organizationId).orElseThrow(() -> new ResourceNotFoundException(
				(String.format(appProperties.getMessages().get(ORGANIZATION_NOT_FOUND_EXCEPTION), organizationId))));

		try {

			organization.setOrganizationName(organizationRequest.getOrganizationName());
			organization.setOrganizationCode(organizationRequest.getOrganizationCode());
			organization.setOrganizationDescription(organizationRequest.getOrganizationDescription());

			organization = organizationRepository.save(organization);

			organizationResponse = modelMapper.map(organization, OrganizationResponse.class);

		} catch (Exception e) {
			throw new OrganizationServiceBusinessException(
					appProperties.getMessages().get(UPDATE_ORGANIZATION_BUSINESS_EXCEPTION));
		}

		return organizationResponse;
	}

	@CacheEvict(key = "#organizationId",value = "organizations")
	@Override
	public String deleteOrganization(Integer organizationId) {
		String message = null;
		Organization organization = organizationRepository.findById(organizationId)
				.orElseThrow(() -> new ResourceNotFoundException((String
						.format(appProperties.getMessages().get(ORGANIZATION_NOT_FOUND_EXCEPTION), organizationId))));

		try {
			if (organization != null) {
				organizationRepository.deleteById(organizationId);
				message = String.format(appProperties.getMessages().get(ORGANIZATION_DELETION_SUCCEEDED), organizationId);
			} else {
				message = String.format(appProperties.getMessages().get(ORGANIZATION_DELETION_FAILED), organizationId);
			}
		} catch (Exception e) {
			throw new OrganizationServiceBusinessException(
					appProperties.getMessages().get(DELETE_ORGANIZATION_BUSINESS_EXCEPTION));
		}

		return message;

	}

}
