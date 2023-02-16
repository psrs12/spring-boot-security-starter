package com.mastercard.pts.pv.issuer.ui.services.security.authentication;

import com.mastercard.pts.pv.issuer.ui.services.security.SecurityConstants;
import com.mastercard.pts.pv.issuer.ui.services.security.web.WebSealSamlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Order(1)
public class SamlAuthFilter extends OncePerRequestFilter {

	private WebSealSamlService samlService;

	public SamlAuthFilter(WebSealSamlService samlService) {
		this.samlService = samlService;
 	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String samlToken = request.getHeader(SecurityConstants.WEB_SEAL_SAML_HEADER_NAME);
		log.info("request.getServletPath() " + request.getServletPath());
		try {
				samlService.prepareSecurityContextFromSaml(samlToken);
		
		} catch (Exception e) {
			log.error("Saml error ", e);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Saml not valid");
		}
		filterChain.doFilter(request, response);
	}

}
