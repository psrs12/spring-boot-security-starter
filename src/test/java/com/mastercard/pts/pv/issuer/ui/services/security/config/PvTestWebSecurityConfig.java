package com.mastercard.pts.pv.issuer.ui.services.security.config;

import com.mastercard.middleware.SAML2.validator.SAML2Validator;
import com.mastercard.middleware.SAML2.validator.SAML2ValidatorBuilder;
import com.mastercard.pts.pv.issuer.ui.services.security.authentication.SamlAuthenticationProvider;
import com.mastercard.pts.pv.issuer.ui.services.security.web.WebSealSamlServiceMock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class PvTestWebSecurityConfig {
    @Bean
    public SAML2Validator saml2Validator() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("debug", "false");
        options.put("logToConsole", "true");
        options.put("disableSignatureValidation", "true");
        options.put("disableCertificateValidation", "true");
        options.put("trustStoreLocation", "c:\\psr\test.jks");
        return SAML2ValidatorBuilder.getInstance().build(options);
    }

    @Bean
    public AuthenticationProvider samlAuthenticationProvider() {
        return new SamlAuthenticationProvider();
    }

    @Bean
    public WebSealSamlServiceMock samlServiceMock() {
        return new WebSealSamlServiceMock(saml2Validator(), samlAuthenticationProvider());
    }

}
