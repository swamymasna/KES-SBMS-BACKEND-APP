package com.kes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationRequest {

	private String organizationName;

	private String organizationCode;

	private String organizationDescription;
}
