package com.spring.study.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.spring.study.config.UserConfigProperties;
import com.spring.study.dao.IUserRepository;

@EnableConfigurationProperties(UserConfigProperties.class)
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);
	

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	private IUserRepository repo;

	public CustomUserDetailsServiceImpl(IUserRepository userRepository) {
		this.repo = userRepository;
	}
	
//	public CustomUserDetailsService(RestTemplateBuilder restTemplateBuilder, UserConfigProperties userProperties) {
//		  this.userProperties = userProperties;
//	      this.restTemplate = restTemplateBuilder.basicAuthentication(userProperties.getUsername(),userProperties.getPassword()).build();
//	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			System.out.println("+=========> CustomUserDetailsServiceImpl loadUserByUsername: ");
			com.spring.study.model.User person = this.repo.findByEmailIgnoreCase(username);
			
			if (person != null) {
//				PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); $2a$10$OOy8HCZZR/X.5TdnZLm5TOI3Gye8Y/nChLdgsXF2m4Bvgp/1nwL7C
				System.out.println("+=========> loadUserByUsername: " + person.getPassword());
				
				String password = passwordEncoder.encode(person.getPassword());
				
				return User.withUsername(person.getUsername()).accountLocked(false).password(password)
						.roles("ADMIN").build();
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		throw new UsernameNotFoundException(username);
	}

}
