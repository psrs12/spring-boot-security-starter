package com.mastercard.pts.pv.issuer.ui.services.security.config;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.mastercard.pts.pv.issuer.ui.services.security.authentication.SamlAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.mastercard.middleware.SAML2.validator.SAML2Validator;
import com.mastercard.middleware.SAML2.validator.SAML2ValidatorBuilder;
import com.mastercard.pts.pv.issuer.ui.services.security.authentication.SamlAuthenticationProvider;
import com.mastercard.pts.pv.issuer.ui.services.security.web.WebSealSamlService;

@Configuration
@ConditionalOnProperty(prefix = "pv",name = "config.security.enabled",matchIfMissing = true)
@ConditionalOnClass(name="com.mastercard.pts.pv.issuer.ui.services.security.model.UserIdentity")
public class PvWebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Value("${KEYSTORE}")
	private String keyStore;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionFixation().migrateSession();
        http.authorizeRequests().antMatchers("/auth").permitAll();//need to get the entry point url
        http.authorizeRequests().antMatchers("/health").permitAll();
        http.csrf().disable();
        http.cors();
        http.addFilterBefore(samlAuthFilter(), BasicAuthenticationFilter.class);
        http.authorizeRequests().anyRequest().authenticated().and().exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint());

    }
    @Bean
    public SAML2Validator saml2Validator() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("debug", "false");
        options.put("logToConsole", "true");
        options.put("disableSignatureValidation", "true");
        options.put("disableCertificateValidation", "true");
        options.put("trustStore", Base64.getDecoder().decode(keyStore));
      //options.put("trustStoreLocation", "c:\\psr\test.jks");
        return SAML2ValidatorBuilder.getInstance().build(options);
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // update allowed origins
        config.addExposedHeader(
                "Authorization, x-xsrf-token,X-Auth-Token, x-auth-token,Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, "
                        + "Content-Type, Access-Control-Request-Method, Custom-Filter-Header");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config); //configure required oe
        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationProvider samlAuthenticationProvider() {
        return new SamlAuthenticationProvider();
    }

    @Bean
    public WebSealSamlService samlService() {
        return new WebSealSamlService(saml2Validator(), samlAuthenticationProvider());
    }

    @Bean
    public SamlAuthFilter samlAuthFilter() {
        return new SamlAuthFilter(samlService());

    }


    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }


    @Bean
    public FilterRegistrationBean<SamlAuthFilter> filterRegistrationBean() {
        FilterRegistrationBean<SamlAuthFilter> filterRegistrationBean = new FilterRegistrationBean<SamlAuthFilter>();
        filterRegistrationBean.setFilter(samlAuthFilter());
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;

    }

}
