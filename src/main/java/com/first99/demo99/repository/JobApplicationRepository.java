package com.first99.demo99.repository;


import com.first99.demo99.model.JobApplication;
import com.first99.demo99.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUser(User user);
}
