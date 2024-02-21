package com.app.twittercloneapi.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class SecureAuthenticationFilter extends BasicAuthenticationFilter {

	public SecureAuthenticationFilter(AuthenticationManager authenticationManager) {
		    super(authenticationManager);
	}

}