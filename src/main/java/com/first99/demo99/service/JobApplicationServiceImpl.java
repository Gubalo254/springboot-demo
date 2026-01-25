package com.first99.demo99.service;

import com.first99.demo99.dto.CreateJobApplicationRequest;
import com.first99.demo99.model.ApplicationStatus;
import com.first99.demo99.model.JobApplication;
import com.first99.demo99.model.User;
import com.first99.demo99.repository.JobApplicationRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;


    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;

    }

    @Override
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public JobApplication createApplication(
            CreateJobApplicationRequest request,
            User currentUser
    ) {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setCompanyName(request.getCompanyName());
        jobApplication.setPosition(request.getPosition());
        jobApplication.setAppliedDate(
                request.getAppliedDate().atStartOfDay()
        );
        jobApplication.setStatus(ApplicationStatus.APPLIED);
        jobApplication.setUser(currentUser);

        return jobApplicationRepository.save(jobApplication);
    }


    @PreAuthorize("hasRole('ADMIN') or #user.id == authentication.principal.user.id")

    @Transactional(readOnly = true)
    public List<JobApplication> getApplicationsForUser(User user) {

        return jobApplicationRepository.findByUser(user);
    }
}