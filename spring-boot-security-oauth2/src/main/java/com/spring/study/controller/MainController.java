package com.spring.study.controller;

import java.security.Principal;
import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.spring.study.dao.IUserRepository;

@Controller
public class MainController {

	@Autowired
	private IUserRepository userRepo;
	
	@Resource(name = "userService")
    private UserDetailsService userDetailsService;
	
	
	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
//	public String welcomePage(Model model) {
	public ModelAndView welcomePage(Principal principal) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("welcomePage");
		mav.addObject("title", "Welcome");
//		mav.addObject("name", principal.getName());
//		mav.addObject("message", "This is welcome page!");
		
//		model.addAttribute("title", "Welcome");
//		model.addAttribute("message", "This is welcome page!");
		
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (auth != null) {
//			System.out.println("=============> CustomLogoutSucessfullHandler != null: " + auth.getName());
//		} else {
//			System.out.println("=============> CustomLogoutSucessfullHandler is null ");
//		}
//		return "welcomePage";
		return mav;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Model model) {
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
	public ResponseEntity<Iterable<com.spring.study.model.User>> profile() {
		
		
		//Build some dummy data to return for testing

		return ResponseEntity.ok(userRepo.findAll()); //ResponseEntity.ok(profile);
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
