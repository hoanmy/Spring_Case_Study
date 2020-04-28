package com.example.demo.properties.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "user.authentication")
public class UserConfigProperties {

	private String findByEmailUri;
    private String username;
	private String password;
	
	
    public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFindByEmailUri() {
		return findByEmailUri;
	}
	public void setFindByEmailUri(String findByEmailUri) {
		this.findByEmailUri = findByEmailUri;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
}
