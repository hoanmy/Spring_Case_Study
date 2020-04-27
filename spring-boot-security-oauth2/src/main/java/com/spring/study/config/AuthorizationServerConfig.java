package com.spring.study.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Value("${app.oauth.clientId}")
    private String CLIEN_ID;
	
	@Value("${app.oauth.clientSecret}")
    private String CLIENT_SECRET;
	
	@Value("${app.oauth.grantTypePassword}")
    private String GRANT_TYPE_PASSWORD;
	
	@Value("${app.oauth.authorizationCode}")
    private String AUTHORIZATION_CODE;
	
	@Value("${app.oauth.refreshToken}")
    private String REFRESH_TOKEN;
	
	@Value("${app.oauth.implicit}")
    private String IMPLICIT;
	
	@Value("${app.oauth.scopeRead}")
    private String SCOPE_READ;
	
	@Value("${app.oauth.scopeWrite}")
    private String SCOPE_WRITE;
	
	@Value("${app.oauth.trust}")
    private String TRUST;
	
	@Value("${app.oauth.accessTokenValiditySeconds}")
    private String ACCESS_TOKEN_VALIDITY_SECONDS;
	
	@Value("${app.oauth.refreshTokeValiditySeconds}")
    private String FREFRESH_TOKEN_VALIDITY_SECONDS;
    
	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {

		configurer
				.inMemory()
				.withClient(CLIEN_ID)
				.secret(CLIENT_SECRET)
				.authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT )
				.scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
				.accessTokenValiditySeconds(Integer.parseInt(ACCESS_TOKEN_VALIDITY_SECONDS)).
				refreshTokenValiditySeconds(Integer.parseInt(FREFRESH_TOKEN_VALIDITY_SECONDS));
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore)
				.authenticationManager(authenticationManager);
	}
}