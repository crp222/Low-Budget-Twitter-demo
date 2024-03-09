package com.app.twittercloneapi.config;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SecureAuthenticationFilter extends BasicAuthenticationFilter {

	public SecureAuthenticationFilter(AuthenticationManager authenticationManager) {
		    super(authenticationManager);
	}

	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) throws IOException {
		super.onSuccessfulAuthentication(request, response, authResult);
		
	}
}