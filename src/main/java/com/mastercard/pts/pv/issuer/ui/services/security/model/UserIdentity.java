package com.mastercard.pts.pv.issuer.ui.services.security.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserIdentity implements Serializable {

	// Common attributes
	@Getter
	@JsonIgnore
	private String userId;
	@Getter
	@JsonIgnore
	private String userDn;
	@Getter
	private String firstName;
	@Getter
	private String middleName;
	@Getter
	private String lastName;
	@Getter
	private String email;
	@Getter
	@JsonIgnore
	private String mobileNumber;
	@Getter
	private String issuerId;

	// B2C-only attributes
	@Getter
	private String alias;
	@Getter
	@JsonIgnore
	private String country;
	@Getter
	@JsonIgnore
	private String siteID;
	@Getter
	@JsonIgnore
	private String appPropID;
	@Getter
	@JsonIgnore
	private boolean csrLoggedIn;

	// B2B/B2Comm-only attributes
	@Getter
	private String authLevel;
	@Getter
	private String companyId;
	@Getter
	private String companyGroupId;
	@Getter
	@JsonIgnore
	private String mcmid;
	@Getter
	private String[] membership;
	@Getter
	@JsonIgnore
	private String smartDataAlias;
	@Getter
	@JsonIgnore
	private String b2bAliasInsensitive;
	@Getter
	private String b2bAlias;
	@Getter
	@JsonIgnore
	private String issuerGroupId;
	@Getter
	private String[] roles;

}
