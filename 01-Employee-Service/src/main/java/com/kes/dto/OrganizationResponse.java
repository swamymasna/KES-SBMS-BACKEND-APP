package com.kes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationResponse {

	private Integer organizationId;

	private String organizationName;

	private String organizationCode;

	private String organizationDescription;

}
