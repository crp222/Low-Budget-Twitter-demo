package com.app.twittercloneapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TextEncryptor encryptor;

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		SecureAuthenticationFilter filter = new SecureAuthenticationFilter(authenticationManager);
		filter.setAuthenticationConverter(new SecureAuthenticationConverter(encryptor));
		http
			.csrf((crfs) -> crfs.disable())
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/","/reg","/login","/public/**").permitAll()
				.anyRequest().authenticated()
			)
			.addFilter(filter);
		return http.build();
	}

}
