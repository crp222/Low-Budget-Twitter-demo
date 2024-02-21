package com.app.twittercloneapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.app.twittercloneapi.User.UserInfoService;

@Configuration
@EnableWebMvc
public class MyConfig implements WebMvcConfigurer{

	@Value("${secure.key}")
    private String SECRET;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**"); // TODO
    }

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserInfoService();
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder("secter",0,1,SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA1);
    }

	@Bean
	public TextEncryptor textEncryptor() {
		String salt = KeyGenerators.string().generateKey(); 
		TextEncryptor textEncryptor = Encryptors.text(SECRET,salt);
		return textEncryptor;
	}
}
