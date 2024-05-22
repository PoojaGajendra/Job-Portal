package com.jsp.JobPortal.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jsp.JobPortal.dto.PortalUser;
import com.jsp.JobPortal.repository.PortalUserRepository;

@Repository
public class PortalUserDao {

	@Autowired
	PortalUserRepository userRepository;

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmailAndVerifiedTrue(email);
	}

//save the user details to db
	public void saveUser(PortalUser portalUser) {
		userRepository.save(portalUser);
		System.out.println("data saved in db");
	}

	public PortalUser findUserById(int id) {
		return userRepository.findById(id).orElse(null);
	}

	public void deleteIfExists(String email) {
		PortalUser user = userRepository.findByEmail(email);
		if (user != null)
			userRepository.delete(user);
	}

	public PortalUser findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public PortalUser findUserByMobile(long mobile) {
		return userRepository.findByMobile(mobile);
	}

	public boolean existsByMobile(long mobile) {
		return userRepository.existsByMobileAndVerifiedTrue(mobile);

	}

	
}
