package com.jsp.JobPortal.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.jsp.JobPortal.dto.PortalUser;
import com.jsp.JobPortal.service.PortalUserservice;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSender {

	

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	TemplateEngine engine;

	public void sendOtp(PortalUser portaluser) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("poojapo3800@gmail.com", "Job-Portal");
			helper.setText(portaluser.getEmail());
			helper.setSubject("Otp Verification from Job Portal");
			Context context = new Context();
			context.setVariable("name", portaluser.getName()); // to send name to emailsender.html
			context.setVariable("otp", portaluser.getOtp());

			String text = engine.process("emailSenderTemplate.html", context);

			helper.setText(text, true);

			mailSender.send(message);
		} catch (Exception e) {
			System.out.println("Error-Not able to send Otp");
		}
	}
}
