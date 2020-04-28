package com.spring.study.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.spring.study.dao.IUserRepository;
import com.spring.study.service.impl.CustomUserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "userService")
    private UserDetailsService userDetailsService;
    
    private IUserRepository userRepo; 
    
    public SecurityConfig(IUserRepository userRepository) {
		this.userRepo = userRepository;
	}

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
    
    @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new CustomUserDetailsServiceImpl(this.userRepo));
	}


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//                .anonymous().disable()
//                .authorizeRequests()
//                .antMatchers("/api-docs/**").permitAll();
        
        http
		.authorizeRequests().antMatchers("/", "/login", "/logout", "/oauth/authorize").permitAll()
		
		.and().authorizeRequests().antMatchers("/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")

		.and().exceptionHandling().accessDeniedPage("/login.jsp?authorization_error=true")
		
				// .and().authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')")
		.and().authorizeRequests().and().exceptionHandling().accessDeniedPage("/403")
		
		.and().formLogin().loginProcessingUrl("/j_spring_security_check").loginPage("/login")
			.defaultSuccessUrl("/user/list").failureUrl("/login?error=true")
			.usernameParameter("username").passwordParameter("password")
		
		.and()
			.csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize")).disable()
			.logout().logoutUrl("/logout")
			
			;
		
//	        http.authorizeRequests().and() //
//	                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
//	                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
