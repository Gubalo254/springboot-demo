package com.first99.demo99.controller;

import com.first99.demo99.dto.ApplicationsResponse;
import com.first99.demo99.dto.CreateJobApplicationRequest;
import com.first99.demo99.dto.JobApplicationResponse;
import com.first99.demo99.model.JobApplication;
import com.first99.demo99.model.User;
import com.first99.demo99.security.CustomUserDetails;
import com.first99.demo99.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {

        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/create")
    public JobApplicationResponse createJobApplication(
           @RequestBody  @Valid  CreateJobApplicationRequest request,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        User user = principal.getUser();

        JobApplication jobApplication =
                jobApplicationService.createApplication(request, user);

        return new JobApplicationResponse(
                "job application done",
                jobApplication.getCompanyName()
        );
    }

    @GetMapping
    public List<ApplicationsResponse> getMyApplications(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        User user = principal.getUser();

        List<JobApplication> applications =
                jobApplicationService.getApplicationsForUser(user);

        return applications.stream()
                .map(j -> new ApplicationsResponse(
                        j.getId(),
                        j.getCompanyName(),
                        j.getPosition(),
                        j.getStatus().name(),
                        j.getAppliedDate().toString()
                ))
                .toList();
    }


}