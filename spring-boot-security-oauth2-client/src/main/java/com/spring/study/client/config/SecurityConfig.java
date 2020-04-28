package com.spring.study.client.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;
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
	}

	@Bean
	@ConfigurationProperties("oursvn.client")
	public AuthorizationCodeResourceDetails oursvnClient() {
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("oursvn.resource")
	public ResourceServerProperties oursvnResource() {
		return new ResourceServerProperties();
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
	
	@Bean
	public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean<OAuth2ClientContextFilter> registration = 
				new FilterRegistrationBean<OAuth2ClientContextFilter>();
		registration.setFilter(filter);
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		return registration;
	}	

	private Filter oauth2ClientFilter() {
		CompositeFilter compositeFilter = new CompositeFilter();
		List<Filter> filters = new ArrayList<>();
//		SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
//		successHandler.setAlwaysUseDefaultTargetUrl(true);
//		successHandler.setDefaultTargetUrl("/user");
		
		OAuth2ClientAuthenticationProcessingFilter githubFilter = 
				new OAuth2ClientAuthenticationProcessingFilter("/login/github");
		OAuth2RestTemplate githubTemplate = new OAuth2RestTemplate(githubClient(), oauth2ClientContext);
		githubFilter.setRestTemplate(githubTemplate);
		UserInfoTokenServices githubTokenServices = 
				new UserInfoTokenServices(githubResource().getUserInfoUri(), githubClient().getClientId());
		githubTokenServices.setRestTemplate(githubTemplate);
		githubFilter.setTokenServices(githubTokenServices);
		
		filters.add(githubFilter);
		
		OAuth2ClientAuthenticationProcessingFilter oursvnFilter = 
				new OAuth2ClientAuthenticationProcessingFilter("/login/myapp");
		OAuth2RestTemplate oursvnTemplate = new OAuth2RestTemplate(oursvnClient(), oauth2ClientContext);
		oursvnFilter.setRestTemplate(oursvnTemplate);
		UserInfoTokenServices oursvnTokenServices = 
				new UserInfoTokenServices(oursvnResource().getUserInfoUri(), oursvnClient().getClientId());
		oursvnTokenServices.setRestTemplate(oursvnTemplate);
		oursvnFilter.setTokenServices(oursvnTokenServices);
		
		filters.add(oursvnFilter);
		
		
		compositeFilter.setFilters(filters);
		
		return compositeFilter;
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