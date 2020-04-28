package com.example.demo.controller;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.model.UserProfile;
import com.example.demo.repository.IUserRepository;

@Controller
public class MainController {

	@Autowired
	private IUserRepository userRepo;
	
	
	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");
		model.addAttribute("message", "This is welcome page!");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			System.out.println("=============> CustomLogoutSucessfullHandler != null: " + auth.getName());
		} else {
			System.out.println("=============> CustomLogoutSucessfullHandler is null ");
		}
		return "welcomePage";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Model model) {
		System.out.println("============> main controller login");
		return "loginPage";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			System.out.println("=============> CustomLogoutSucessfullHandler != null: " + auth.getName());
//		    new SecurityContextLogoutHandler().logout(request, response, auth);
		} else {
			System.out.println("=============> CustomLogoutSucessfullHandler is null ");
		}
		
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			User loginedUser = (User) ((Authentication) principal).getPrincipal();

			String userInfo = toString(loginedUser);

			model.addAttribute("userInfo", userInfo);

			String message = "Hi " + principal.getName() //
					+ "<br> You do not have permission to access this page!";
			model.addAttribute("message", message);

		}

		return "403Page";
	}

	@RequestMapping("/api/users/list")
	public ResponseEntity<Iterable<com.example.demo.model.User>> profile() {
		
		
		//Build some dummy data to return for testing
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName() + "@howtodoinjava.com";

		UserProfile profile = new UserProfile();
		profile.setName(auth.getName());
		profile.setEmail(email);
		
		

		return ResponseEntity.ok(userRepo.findAll()); //ResponseEntity.ok(profile);
	}
	
	@RequestMapping("/api/users/me")
	public ResponseEntity<UserProfile> profile1() {
			
			
			//Build some dummy data to return for testing
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String email = auth.getName() + "@howtodoinjava.com";

			UserProfile profile = new UserProfile();
			profile.setName(auth.getName());
			profile.setEmail(email);
			
			

			return ResponseEntity.ok(profile);
		}
	
	public String toString(User user) {
		StringBuilder sb = new StringBuilder();

		sb.append("UserName:").append(user.getUsername());

		Collection<GrantedAuthority> authorities = user.getAuthorities();
		if (authorities != null && !authorities.isEmpty()) {
			sb.append(" (");
			boolean first = true;
			for (GrantedAuthority a : authorities) {
				if (first) {
					sb.append(a.getAuthority());
					first = false;
				} else {
					sb.append(", ").append(a.getAuthority());
				}
			}
			sb.append(")");
		}
		return sb.toString();
	}
}
