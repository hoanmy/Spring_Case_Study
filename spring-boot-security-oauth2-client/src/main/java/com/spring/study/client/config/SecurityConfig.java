package com.spring.study.client.config;

import java.util.Arrays;
import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;

@Configuration
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	OAuth2ClientContext oauth2ClientContext;
//
//	@Value("${app.accessTokenUri}")
//	private String accessTokenUri;
//
//	@Value("${app.userAuthorizationUri}")
//	private String userAuthorizationUri;

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("marissa").password("wombat").roles("USER").and().withUser("sam")
//				.password("kangaroo").roles("USER");
//	}

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/resources/**");
//	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/login**", "/error**").permitAll().anyRequest().authenticated()
        .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
        .and().addFilterBefore(oauth2ClientFilter(), BasicAuthenticationFilter.class);
		
		/*
		 * http .authorizeRequests().antMatchers("/", "/error**",
		 * "/login**").permitAll() .anyRequest().authenticated() .and()
		 * .logout().logoutUrl("/logout").logoutSuccessUrl("/login.jsp").permitAll()
		 * .and() .formLogin() .loginProcessingUrl("/login") .loginPage("/login.jsp")
		 * .failureUrl("/login.jsp?authentication_error=true") .permitAll();
		 */
			
//		        .and().addFilterBefore(oauth2ClientFilter(), BasicAuthenticationFilter.class)

		;

	}

	@Bean
	@ConfigurationProperties("github.client")
	public AuthorizationCodeResourceDetails githubClient() {
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("github.resource")
	public ResourceServerProperties githubResource() {
		return new ResourceServerProperties();
	}
	
	
//	public AuthorizationCodeResourceDetails githubClient() {
//		AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
//		details.setId("github-hoanmy");
//		details.setClientId("40c506f2741391fedc80");
//		details.setClientSecret("ac396d4300625e23cc7b3bf6b4510ce79e20d322");
//		details.setAccessTokenUri("https://github.com/login/oauth/access_token");
//		details.setUserAuthorizationUri("https://github.com/login/oauth/authorize"); 
//		details.setClientAuthenticationScheme(AuthenticationScheme.form);
//		
//		return details;
//	}
//
//	public ResourceServerProperties githubResource() {
//		ResourceServerProperties rs = new ResourceServerProperties(); 
//		rs.setUserInfoUri("https://api.github.com/user");
//		return rs;
//	}

	@Bean
	public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(
			OAuth2ClientContextFilter filter) {
		FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
		registration.setFilter(filter);
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		return registration;
	}	

	private Filter oauth2ClientFilter() {
		OAuth2ClientAuthenticationProcessingFilter oauth2ClientFilter = 
				new OAuth2ClientAuthenticationProcessingFilter("/login/github");
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(githubClient(), oauth2ClientContext);
		oauth2ClientFilter.setRestTemplate(restTemplate);
		UserInfoTokenServices tokenServices = new UserInfoTokenServices(
				githubResource().getUserInfoUri(),
				githubClient().getClientId());
		tokenServices.setRestTemplate(restTemplate);
		oauth2ClientFilter.setTokenServices(tokenServices);
		return oauth2ClientFilter;
	}
//
//	@Bean
//	public OAuth2RestTemplate springApppClientRestTemplate(OAuth2ClientContext clientContext) {
//		return new OAuth2RestTemplate(springAppClient(), clientContext);
//	}
//
//	@Bean
//	public OAuth2ProtectedResourceDetails springAppClient() {
//		AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
//		details.setId("spring-app-client");
//		details.setClientId("spring-app-client");
//		details.setClientSecret("secret-code");
//		details.setAccessTokenUri(accessTokenUri);
//		details.setUserAuthorizationUri(userAuthorizationUri);
//		details.setScope(Arrays.asList("read", "write"));
//		return details;
//	}

}