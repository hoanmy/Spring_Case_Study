package com.spring.study.client.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AppController {
	@GetMapping("hello")
	public ModelAndView welcome(Principal principal) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("welcome");
		mav.addObject("name", principal.getName());
		return mav;
	}
}
