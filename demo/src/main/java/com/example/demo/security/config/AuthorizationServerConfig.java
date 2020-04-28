package com.example.demo.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer	
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter  {

	@Autowired(required=true)
	private TokenStore tokenStore;

	@Autowired
	private UserApprovalHandler userApprovalHandler;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

//	@Value("${tonr.redirect:http://localhost:8081/tonr2/sparklr/redirect}")
	private String tonrRedirectUri;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	private static final String SPARKLR_RESOURCE_ID = "sparklr";
 
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security
//            .tokenKeyAccess("permitAll()")
//            .checkTokenAccess("isAuthenticated()")
//            .allowFormAuthenticationForClients();
//    }
 
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	
        clients
            .inMemory()
            .withClient("myapp")
            .secret("123456")
            .authorizedGrantTypes("password", "authorization_code", "refresh_token")
//            .authorities("READ_ONLY_CLIENT")
//            .scopes("read_profile_info")
            .authorities("ROLE_CLIENT")
            .scopes("read", "write")
//            .resourceIds("oauth2-resource")
            .redirectUris("http://localhost:8082/login/myapp")
            .accessTokenValiditySeconds(120)
            .refreshTokenValiditySeconds(240000);
    }
   

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
				.authenticationManager(authenticationManager);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.realm("demo/client");
	}
	
	
	protected static class Stuff {

		@Autowired
		private ClientDetailsService clientDetailsService;

		@Autowired
		private TokenStore tokenStore;

		@Bean
		public ApprovalStore approvalStore() throws Exception {
			TokenApprovalStore store = new TokenApprovalStore();
			store.setTokenStore(tokenStore);
			return store;
		}

		@Bean
		@Lazy
		@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
		public UserApprovalHandlerImpl userApprovalHandler() throws Exception {
			UserApprovalHandlerImpl handler = new UserApprovalHandlerImpl();
			handler.setApprovalStore(approvalStore());
			handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
			handler.setClientDetailsService(clientDetailsService);
			handler.setUseApprovalStore(true);
			return handler;
		}
	}
}
