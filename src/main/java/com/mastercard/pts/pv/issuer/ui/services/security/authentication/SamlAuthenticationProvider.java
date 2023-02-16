package com.mastercard.pts.pv.issuer.ui.services.security.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class SamlAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (supports(authentication.getClass()) && !((String) authentication.getPrincipal()).isEmpty()) {
			authentication.setAuthenticated(true);
			return authentication;
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SamlAutheticationToken.class.isAssignableFrom(authentication);
	}

}
