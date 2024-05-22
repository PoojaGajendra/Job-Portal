package com.jsp.JobPortal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.JobPortal.dto.PortalUser;

public interface PortalUserRepository extends JpaRepository<PortalUser, Integer> {


	boolean existsByEmailAndVerifiedTrue(String email);

	boolean existsByMobileAndVerifiedTrue(long mobile);

	PortalUser findByEmail(String email);

	PortalUser findByMobile(long mobile);
//	expected single result but reciver many


}
