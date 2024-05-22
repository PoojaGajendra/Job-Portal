package com.jsp.JobPortal.service;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.jsp.JobPortal.dao.PortalUserDao;
import com.jsp.JobPortal.dto.PortalUser;
import com.jsp.JobPortal.helper.AES;
import com.jsp.JobPortal.helper.EmailSender;

import jakarta.servlet.http.HttpSession;

@Service
public class PortalUserservice {

	@Autowired
	PortalUserDao userDao;

	@Autowired
	EmailSender emailHelper;

	public String signup(PortalUser portalUser, BindingResult result, ModelMap map) {

		if (portalUser.getDob() == null) {
			result.rejectValue("dob", "error.dob", "* Select a Date");
			System.out.println("Error - Age is Not Selected");
		} else if (LocalDate.now().getYear() - portalUser.getDob().getYear() < 18) {
			result.rejectValue("dob", "error.dob", "*Age should be greater than 18");
			System.out.println("age is less than 18");
		}
		if (!portalUser.getPassword().equals(portalUser.getConfirm_password())) {
			result.rejectValue("confirm_password", "error.confirm_password",
					"*Password and confirm password should be same");
			System.out.println("Error - Password is Not Matching");
		}

//		email Validation
		if (userDao.existsByEmail(portalUser.getEmail())) {
			result.rejectValue("email", "error.email", "* Email Should be unique");
			System.out.println("Error - Email is Repeated");
		}
//		mobile no validation
		if (userDao.existsByMobile(portalUser.getMobile())) {
			result.rejectValue("mobile", "error.mobile", "* Mobile No Should be unique");
			System.out.println("Error - Mobile is Repeated");
		}

		if (result.hasErrors()) {
			System.out.println("Error - There is Some Error");
			return "signup";
		} else {
			System.out.println("No Error");
			userDao.deleteIfExists(portalUser.getEmail());
			int otp = new Random().nextInt(100000, 999999);

			portalUser.setOtp(otp);
			System.out.println(otp);

			portalUser.setPassword(AES.encrypt(portalUser.getPassword(), "123"));
			portalUser.setConfirm_password(AES.encrypt(portalUser.getConfirm_password(), "123"));
			System.out.println(portalUser.getPassword());
			userDao.saveUser(portalUser);

			emailHelper.sendOtp(portalUser); // To send OTP to email
			System.out.println("Otp is Sent to Email " + portalUser.getEmail());

			map.put("msg", "otp sent Sucessfully");
			map.put("id", portalUser.getId());
			return "enter_otp";
		}
	}
//write unique mobile no condition
	public String submitOtp(int otp, int id, ModelMap map) {
		PortalUser portalUser = userDao.findUserById(id);
		if (otp == portalUser.getOtp()) {
			System.out.println("Success- OTP Matched");
			portalUser.setVerified(true);
			userDao.saveUser(portalUser);
			map.put("msg", "Account Created Success");
			return "login";
		} else {
			System.out.println("Failure- OTP MissMatch");
			map.put("msg", "Incorrect Otp! Try Again");
			map.put("id", portalUser.getId());
			return "enter_otp";
		}
	}



	public String login(String emph, String password, ModelMap map, HttpSession session) {
		PortalUser portalUser = null;
		try {
			long mobile = Long.parseLong(emph);
			portalUser = userDao.findUserByMobile(mobile);
		} catch (NumberFormatException e) {
			String email = emph;
			portalUser = userDao.findUserByEmail(email);
		}
		if (portalUser == null) {
			map.put("msg", "Invalid Email or Phone Number");
			return "login.html";
		} else {
			if (password.equals(AES.decrypt(portalUser.getPassword(), "123"))) {
				if (portalUser.isVerified()) {
					map.put("msg", "Login Success");
					session.setAttribute("portalUser", portalUser);
					if (portalUser.getRole().equals("applicant")) {
						return "applicantHome.html";
					} else {
						return "recruiterHome.html";
					}
				} else {
					map.put("msg", "First Verify Your Email");
					return "login.html";
				}
			} else {
				map.put("msg", "Invalid Password");
				return "login.html";
			}
		}
	}
	public String resendOtp(int id, ModelMap map) {
		PortalUser portalUser = userDao.findUserById(id);
		int otp = new Random().nextInt(100000, 999999);
		System.out.println("Otp ReGenerated - " + otp);
		portalUser.setOtp(otp);
		userDao.saveUser(portalUser);
		System.out.println("Data is Updated in db");
		emailHelper.sendOtp(portalUser);
		System.out.println("Otp is Sent to Email " + portalUser.getEmail());
		map.put("msg", "Otp Sent Again, Check");
		map.put("id", portalUser.getId());
		System.out.println("Control- enter-otp.html");
		return "enter_otp";	
	}

}
