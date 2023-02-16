package com.mastercard.pts.pv.issuer.ui.services.security.authentication;

import com.mastercard.pts.pv.issuer.ui.services.security.model.UserIdentity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SamlAutheticationToken extends AbstractAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SamlAutheticationToken(Collection<? extends GrantedAuthority> authorities, UserIdentity userIdentity) {
		super(authorities);
		this.setAuthenticated(true);
		this.setDetails(userIdentity);
	}

	@Override
	public Object getCredentials() {
		return "N/A";
	}

	@Override
	public Object getPrincipal() {
		return ((UserIdentity) getDetails()).getUserId();
	}

	@Override
	public String getName() {return ((UserIdentity) getDetails()).getFirstName();	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return super.getAuthorities();
	}


}
