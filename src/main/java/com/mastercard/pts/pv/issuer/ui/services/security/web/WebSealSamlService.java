package com.mastercard.pts.pv.issuer.ui.services.security.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mastercard.pts.pv.issuer.ui.services.security.authentication.SamlAutheticationToken;
import com.mastercard.pts.pv.issuer.ui.services.security.model.UserIdentity;
import com.mastercard.pts.pv.issuer.ui.services.security.model.UserIdentityMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mastercard.middleware.SAML2.validator.SAML2Validator;
import com.mastercard.middleware.SAML2.validator.SAML2ValidatorResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSealSamlService {

	private SAML2Validator saml2Validator;
	private AuthenticationProvider autheticationProvider;


	public void prepareSecurityContextFromSaml(String samlToken) throws Exception {
		log.info("the saml header is " + samlToken);
		//processSAMLHeader(samlToken);
		processSAMLHeader(getSamlString());
	}

	public WebSealSamlService(SAML2Validator saml2Validator, AuthenticationProvider autheticationProvider) {
		super();
		this.saml2Validator = saml2Validator;
		this.autheticationProvider = autheticationProvider;

	}

	private void processSAMLHeader(String saml) throws Exception {
		List<GrantedAuthority> authorityList;
		if (StringUtils.isNotEmpty(saml)) {
			String b64Encoded = org.apache.commons.codec.binary.Base64.isBase64(saml) ? saml
					: org.apache.commons.codec.binary.Base64.encodeBase64String(saml.getBytes(StandardCharsets.UTF_8));
			SAML2ValidatorResult result = saml2Validator.validate(b64Encoded);
			log.info("SAML TOKEN VALIDATED SUCCESSFULLY");
			String[] memberships = result.getGroups().toArray(new String[0]);
			authorityList = Arrays.stream(memberships).filter(StringUtils::isNotEmpty).map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
			UserIdentity userIdentity = UserIdentityMapper.newUserIdentity(result.getPrincipal());
			SamlAutheticationToken samlAuthToken = new SamlAutheticationToken(authorityList, userIdentity);
			Authentication authentication = autheticationProvider.authenticate(samlAuthToken);
			log.info("SAML TOKEN AUTHENTICATED SUCCESSFULLY");
			SecurityContextHolder.getContext().setAuthentication(authentication);


		}
	}

	private String getSamlString() throws IOException {
		return  Files.lines(Paths.get("C:\\temp\\cardholdersaml.xml"))
				.parallel()
				.collect(Collectors.joining());

	}

}
