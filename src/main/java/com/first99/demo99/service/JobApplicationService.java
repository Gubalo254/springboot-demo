package com.first99.demo99.service;

import com.first99.demo99.dto.CreateJobApplicationRequest;
import com.first99.demo99.model.JobApplication;
import com.first99.demo99.model.User;

import java.util.List;

public interface JobApplicationService {

    public JobApplication createApplication(CreateJobApplicationRequest request, User currentUser);

    public List<JobApplication> getApplicationsForUser(User user);
}