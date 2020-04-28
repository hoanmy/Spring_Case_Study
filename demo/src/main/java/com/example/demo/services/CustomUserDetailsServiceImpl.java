package com.example.demo.services;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.properties.config.UserConfigProperties;
import com.example.demo.repository.IUserRepository;
import com.example.demo.security.config.WebSecurityConfiguration;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@EnableConfigurationProperties(UserConfigProperties.class)
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(CustomUserDetailsServiceImpl.class);
	
	private RestTemplate restTemplate;
	private UriComponentsBuilder builder;
	private UserConfigProperties userProperties;
	

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
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			final com.example.demo.model.User person = this.repo.findByEmailIgnoreCase(email);
			
			
//			  builder = UriComponentsBuilder.fromUriString(userProperties.getFindByEmailUri()).queryParam("email", email);
//	          log.info("Querying: " + builder.toUriString());
//	                    
//	          ResponseEntity<EntityModel<com.example.demo.model.User>> responseEntity =  
//	        		  restTemplate.exchange(RequestEntity.get(URI.create(builder.toUriString())).accept(MediaTypes.HAL_JSON).build(), 
//	        				  new ParameterizedTypeReference<EntityModel<com.example.demo.model.User>>() {});
//	          
//	          if (responseEntity.getStatusCode() == HttpStatus.OK) {
//	        	  EntityModel<com.example.demo.model.User> resource = responseEntity.getBody();
//                  com.example.demo.model.User person1 = resource.getContent();
//                  log.info("==============> resource content " + person1.getEmail());
//                  
//                  PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//  				  String password = encoder.encode(person.getPassword());
//  				  return User.withUsername(person.getEmail()).accountLocked(!person.isEnabled()).password(password)
//  						.roles(person.getRole()).build();
//	          }     
                  
	      
			if (person != null) {
//				PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); $2a$10$OOy8HCZZR/X.5TdnZLm5TOI3Gye8Y/nChLdgsXF2m4Bvgp/1nwL7C
				System.out.println("+=========> loadUserByUsername: " + person.getPassword());
				
				String password = passwordEncoder.encode(person.getPassword());
				
				return User.withUsername(person.getEmail()).accountLocked(!person.isEnabled()).password(password)
						.roles(person.getRole()).build();
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		throw new UsernameNotFoundException(email);
	}

}
