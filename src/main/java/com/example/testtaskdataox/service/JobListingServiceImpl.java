package com.example.testtaskdataox.service;

import com.example.testtaskdataox.model.JobListing;
import com.example.testtaskdataox.repository.JobListingRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class JobListingServiceImpl implements JobListingService {
    private final JobListingRepository jobListingRepository;

    public JobListingServiceImpl(JobListingRepository jobListingRepository) {
        this.jobListingRepository = jobListingRepository;
    }

    @Override
    public JobListing save(JobListing jobListing) {
        return jobListingRepository.save(jobListing);
    }

    @Override
    public List<JobListing> getAllWithSort(Sort sort) {
        return jobListingRepository.findAll(sort);
    }

    @Override
    public List<JobListing> findAllByLocationsContaining(String location) {
        return jobListingRepository.findAllByLocationsContaining(location);
    }
}
