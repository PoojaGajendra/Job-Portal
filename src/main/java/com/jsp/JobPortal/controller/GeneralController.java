package com.jsp.JobPortal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jsp.JobPortal.dto.PortalUser;
import com.jsp.JobPortal.service.PortalUserservice;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class GeneralController {

	@Autowired
	PortalUser portalUser;

	@Autowired
	PortalUserservice userservice;

	@GetMapping("/")
	public String loadLogin() {
		System.out.println("Control- /home");
		return "home";
	}

	@GetMapping("/login")
	public String loadHome() {
		System.out.println("/login");
		return "login";
	}

	@GetMapping("/signup")
	public String loadSignup(ModelMap map) {
		// empty object is sent
		System.out.println("Control- /signup - Get , Empty Object is Sent to Signup Page");
		map.put("portalUser", portalUser);
		return "signup";
	}

	@PostMapping("/signup")
	public String signup(@Valid PortalUser portalUser, BindingResult result, ModelMap map) {
		System.out.println("PostMapping - /signup, recived  post request");
		return userservice.signup(portalUser, result, map);
	}

	@PostMapping("/submit_otp")
	public String submitOtp(@RequestParam int otp, @RequestParam int id, ModelMap map) {
		System.out.println("postMapping - /submit otp");
		// int otp1=Integer.parseInt(otp);
		return userservice.submitOtp(otp, id, map);
	}

	@GetMapping("/resend-otp/{id}")
	public String resendOtp(@PathVariable int id, ModelMap map) {
		return userservice.resendOtp(id, map);
	}

	@PostMapping("/login")
	public String login(@RequestParam String emph, @RequestParam String password, ModelMap map, HttpSession session) {
		System.out.println("/login");
		return userservice.login(emph, password, map, session);
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, ModelMap map) {
		session.removeAttribute("portalUser");
		map.put("msg", "Logged out Sucessfully");
		System.out.println("/logout");
		return "login";
	}

}
