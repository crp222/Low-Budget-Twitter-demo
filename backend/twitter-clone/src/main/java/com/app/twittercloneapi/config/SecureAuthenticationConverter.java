package com.app.twittercloneapi.config;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;

public class SecureAuthenticationConverter extends BasicAuthenticationConverter {

    private TextEncryptor encryptor;

    public SecureAuthenticationConverter(TextEncryptor encryptor){
        this.encryptor = encryptor;
    }
    
    @Override
	public UsernamePasswordAuthenticationToken convert(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null) {
			return null;
		}
		header = header.trim();
        try {
            String token = encryptor.decrypt(header);
            int delim = token.indexOf(":");
            if (delim == -1) {
                throw new Exception();
            }
            String username = token.substring(0, delim);
            String password = token.substring(delim + 1);
            UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken
                .unauthenticated(username,password);
            result.setDetails(super.getAuthenticationDetailsSource().buildDetails(request));
            return result;
        }catch(Exception e){
            throw new BadCredentialsException("Invalid basic authentication token");
        }
	}
}
