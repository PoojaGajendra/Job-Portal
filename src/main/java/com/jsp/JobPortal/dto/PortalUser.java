package com.jsp.JobPortal.dto;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Component
public class PortalUser{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Size(min = 3, max = 20, message = "*Enter characters between 3~15")
	
	private String name;
	
	@NotNull(message = "*Required")
	@Email(message = "*Enter valid email format")
	private String email;
	
//	@Pattern(regexp = "[a-z]")
	@Size(min = 8, max = 80, message = "*Enter characters between 8~15")
	private String password;
	
	@Size(min = 8, max = 80, message = "*Enter characters between 8~15")
	private String confirm_password;
	@DecimalMin(value = "6000000000", message = "*Enter valid mobile number")
	@DecimalMax(value = "9999999999", message = "*Enter valid mobile number")
	private long mobile;
	
	@NotNull(message = "*Required")
	private LocalDate dob;
	private String role;
	private int otp;
	private boolean verified;
	private boolean profileComplete;
	
//	public String getPassword() {
//		// to decrypt the password and get form DB
//		return AES.decrypt(password, "123");
//	}
//
//	public void setPassword(String password) {
//		// to encrypt the password and save in DB
//		this.password = AES.encrypt(password, "123");
//	}
//	
}
