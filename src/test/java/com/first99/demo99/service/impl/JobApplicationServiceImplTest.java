package com.first99.demo99.service.impl;

import com.first99.demo99.dto.CreateJobApplicationRequest;
import com.first99.demo99.model.JobApplication;
import com.first99.demo99.model.User;
import com.first99.demo99.repository.JobApplicationRepository;
import com.first99.demo99.service.JobApplicationServiceImpl;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class JobApplicationServiceImplTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private JobApplicationServiceImpl service;

    @Test
    void createApplication_shouldSaveAndReturnJobApplication() {
        // given
        User user = new User();
        user.setId(1L);

        CreateJobApplicationRequest request = new CreateJobApplicationRequest();
        request.setCompanyName("Acme");
        request.setPosition("Developer");
        request.setAppliedDate(LocalDate.now());

        JobApplication saved = new JobApplication();
        saved.setCompanyName("Acme");
        when(jobApplicationRepository.save(Mockito.<JobApplication>any()))
                .thenReturn(saved);


        // when
        JobApplication result = service.createApplication(request, user);

        // then
        assertEquals("Acme", result.getCompanyName());
        verify(jobApplicationRepository)
                .save(Mockito.<JobApplication>any());

    }

    @Test
    void getApplicationsForUser_shouldReturnApplications() {
        User user = new User();
        user.setId(1L);

        List<JobApplication> apps = List.of(new JobApplication());

        when(jobApplicationRepository.findByUser(user))
                .thenReturn(apps);

        List<JobApplication> result =
                service.getApplicationsForUser(user);

        assertEquals(1, result.size());
        verify(jobApplicationRepository).findByUser(user);
    }
}
