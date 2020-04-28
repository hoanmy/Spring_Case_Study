package com.example.demo.security.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class LogoutHandlerImpl implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("=============> logout handler: ");
		
		if (auth != null) {
			System.out.println("=============> logout handler != null: " + auth.getName());
		    new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
	}

}
