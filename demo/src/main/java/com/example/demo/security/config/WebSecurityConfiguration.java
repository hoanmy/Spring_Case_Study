package com.example.demo.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.repository.IUserRepository;
import com.example.demo.services.CustomUserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@Order(1)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final Logger log = LoggerFactory.getLogger(WebSecurityConfiguration.class);

	private IUserRepository repo;

	public WebSecurityConfiguration(IUserRepository userRepository) {
		this.repo = userRepository;
	}
	
	@Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("marissa").password("koala").roles("USER").and().withUser("paul")
                .password("emu").roles("USER");
    }

	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**", "/images/**", "/oauth/uncache_approvals", "/oauth/cache_approvals");
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests().antMatchers("/", "/login", "/logout", "/oauth/authorize").permitAll()
		
		.and().authorizeRequests().antMatchers("/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")

		.and().exceptionHandling().accessDeniedPage("/login.jsp?authorization_error=true")
		
				// .and().authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
		.and().authorizeRequests().and().exceptionHandling().accessDeniedPage("/403")
		
		.and().formLogin().loginProcessingUrl("/login") //.loginPage("/login")
			.defaultSuccessUrl("/user/list").failureUrl("/login?error=true")
			.usernameParameter("username").passwordParameter("password")
		
		.and()
			.csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable()
			.logout().logoutUrl("/logout")
			
			;
		
//	        http.authorizeRequests().and() //
//	                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
//	                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h
			
			
//			http
//            .authorizeRequests()
//                .antMatchers("/login.jsp").permitAll()
//                .anyRequest().hasRole("USER")
//                .and()
//            .exceptionHandling()
//                .accessDeniedPage("/login.html?authorization_error=true")
//                .and()
//            // TODO: put CSRF protection back into this endpoint
//            .csrf()
//                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
//                .disable()
//            .logout()
//            	.logoutUrl("/logout")
//                .logoutSuccessUrl("/login.jsp")
//                .and()
//            .formLogin()
//            	.loginProcessingUrl("/login")
//                .failureUrl("/login.jsp?authentication_error=true")
//                .loginPage("/login.jsp");

	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("mynguyen")
//				.password(passwordEncoder().encode("abcd1234")).roles("ADMIN", "USER");

		auth.userDetailsService(new CustomUserDetailsServiceImpl(this.repo));
	}


	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public LogoutHandlerImpl customLogoutHandler() {
		return new LogoutHandlerImpl();
	}

	@Bean
	public LogoutSuccessHandlerImpl customLogoutSucessfullHandler() {
		return new LogoutSuccessHandlerImpl();
	}

}
