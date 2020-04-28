package com.example.demo.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter 
{
	private static final String SPARKLR_RESOURCE_ID = "sparklr";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(SPARKLR_RESOURCE_ID).stateless(false);
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {

//		http
//		.authorizeRequests()
//			.antMatchers("/resourceA/**").hasAuthority("#oauth2.hasScope('resourceA:read')")
//			.antMatchers("/resourceB/**").hasAuthority("#oauth2.hasScope('resourceB:read')")
//			.anyRequest().authenticated();
		
		
		http
		// Since we want the protected resources to be accessible in the UI as well we need 
		// session creation to be allowed (it's disabled by default in 2.0.6)
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	.and()
		.requestMatchers().antMatchers("/photos/**", "/oauth/users/**", "/oauth/clients/**","/me")
	.and()
		.authorizeRequests()
			.antMatchers("/me").access("#oauth2.hasScope('read')")					
			.antMatchers("/photos").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")                                        
			.antMatchers("/photos/trusted/**").access("#oauth2.hasScope('trust')")
			.antMatchers("/photos/user/**").access("#oauth2.hasScope('trust')")					
			.antMatchers("/photos/**").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
			.regexMatchers(HttpMethod.DELETE, "/oauth/users/([^/].*?)/tokens/.*")
				.access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('write')")
			.regexMatchers(HttpMethod.GET, "/oauth/clients/([^/].*?)/users/.*")
				.access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('read')")
			.regexMatchers(HttpMethod.GET, "/oauth/clients/.*")
				.access("#oauth2.clientHasRole('ROLE_CLIENT') and #oauth2.isClient() and #oauth2.hasScope('read')");
	}
}
