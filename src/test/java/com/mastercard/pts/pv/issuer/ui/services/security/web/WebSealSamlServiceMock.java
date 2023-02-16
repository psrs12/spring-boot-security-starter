package com.mastercard.pts.pv.issuer.ui.services.security.web;

import com.mastercard.middleware.SAML2.validator.SAML2Validator;
import com.mastercard.middleware.SAML2.validator.SAML2ValidatorResult;
import com.mastercard.pts.pv.issuer.ui.services.security.authentication.SamlAutheticationToken;
import com.mastercard.pts.pv.issuer.ui.services.security.model.UserIdentity;
import com.mastercard.pts.pv.issuer.ui.services.security.model.UserIdentityMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WebSealSamlServiceMock {
    private SAML2Validator saml2Validator;
    private AuthenticationProvider autheticationProvider;

    public void prepareSecurityContextFromSaml() throws Exception {
        processSAMLHeader(getSamlString());
    }

    public WebSealSamlServiceMock(SAML2Validator saml2Validator, AuthenticationProvider authenticationProvider) {
        super();
        this.saml2Validator = saml2Validator;
        this.autheticationProvider = authenticationProvider;
    }

    private void processSAMLHeader(String saml) throws Exception {
        List<GrantedAuthority> authorityList;
        if (StringUtils.isNotEmpty(saml)) {
            String b64Encoded = org.apache.commons.codec.binary.Base64.isBase64(saml) ? saml
                    : org.apache.commons.codec.binary.Base64.encodeBase64String(saml.getBytes(StandardCharsets.UTF_8));
            SAML2ValidatorResult result = saml2Validator.validate(b64Encoded);

            String[] memberships = result.getGroups().toArray(new String[0]);
            authorityList = Arrays.stream(memberships).filter(StringUtils::isNotEmpty).map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            UserIdentity userIdentity = UserIdentityMapper.newUserIdentity(result.getPrincipal());

            SamlAutheticationToken samlAuthToken = new SamlAutheticationToken(authorityList, userIdentity);

            Authentication authentication = autheticationProvider.authenticate(samlAuthToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);


        }
    }

    private UserIdentity userIdentity() {
        UserIdentity.UserIdentityBuilder userIdentityBuilder = UserIdentity.builder();
        return userIdentityBuilder.firstName("PSR").lastName("Srinivas").companyId("0").userId("e032967")
                .email("tesat@test.com").build();
    }


    private String getSamlString() throws IOException {
        return  Files.lines(Paths.get("src/test/java/resources/cardholdersaml.xml"))
                .parallel()
                .collect(Collectors.joining());

    }
}
