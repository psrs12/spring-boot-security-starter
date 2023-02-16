package com.mastercard.pts.pv.issuer.ui.services.security.model;

import com.mastercard.middleware.SAML2.SAML2User;
import com.mastercard.pts.pv.issuer.ui.services.security.model.UserIdentity.UserIdentityBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserIdentityMapper {

	public static UserIdentity newUserIdentity(SAML2User saml2User) {
		UserIdentityBuilder userIdentityBuilder = UserIdentity.builder();
		userIdentityBuilder.userId(saml2User.getName());
		if (saml2User ==null || saml2User.getName()==null) {
			return null;
		}
		userIdentityBuilder.issuerId(saml2User.getIssuer());
		List<String> membersList = saml2User.getRoles();
		if (membersList != null) {
			userIdentityBuilder.membership(membersList.toArray(new String[membersList.size()]));
		}

		Map<String, String[]> attributes = saml2User.getAttributes();
		Iterator<String> i = attributes.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next().toString();
			String singleValue = attributes.get(key) != null && attributes.get(key).length > 0 ? attributes.get(key)[0]
					: null;
			switch (key.toLowerCase()) {
			case "givenname":
				userIdentityBuilder.firstName(singleValue);
				break;
			case "middlename":
				userIdentityBuilder.middleName(singleValue);
				break;
			case "sn":
				userIdentityBuilder.lastName(singleValue);
				break;
			case "mail":
				userIdentityBuilder.email(singleValue);
				break;
			case "mobile":
				userIdentityBuilder.mobileNumber(singleValue);
				break;
			case "c":
				userIdentityBuilder.country(singleValue);
				break;
			case "authlevel":
				userIdentityBuilder.authLevel(singleValue);
				break;
			case "dn":
				userIdentityBuilder.userDn(singleValue);
				break;
			case "companyid":
				userIdentityBuilder.companyId(singleValue);
				break;
			case "companygroupid":
				userIdentityBuilder.companyGroupId(singleValue);
				break;
			case "mcmid":
				userIdentityBuilder.mcmid(singleValue);
				break;
			case "alias":
				userIdentityBuilder.alias(singleValue);
				break;
			case "b2bAliasInsensitive":
				userIdentityBuilder.b2bAliasInsensitive(singleValue);
				break;
			case "smartDataAlias":
				userIdentityBuilder.b2bAliasInsensitive(singleValue);
				break;
			case "b2balias":
				userIdentityBuilder.b2bAlias(singleValue);
				break;
			case "issuerGroupId":
				userIdentityBuilder.issuerGroupId(singleValue);
				break;
			case "AppProprietaryID":
				userIdentityBuilder.appPropID(singleValue);
				break;
			case "SiteID":
				userIdentityBuilder.siteID(singleValue);
				break;
			case "CSRLoggedIn":
				userIdentityBuilder.csrLoggedIn(Boolean.valueOf(singleValue));
				break;
			case "roles":
				userIdentityBuilder.roles(attributes.get(key));
				break;
			case "userdn":
				userIdentityBuilder.userDn(singleValue);
				break;
			}
		}
		return userIdentityBuilder.build();
	}
  
}
