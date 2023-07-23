package com.example.testtaskdataox.service;

import com.example.testtaskdataox.model.JobListing;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface JobListingService {
    List<JobListing> getAllWithSort(Sort sort);

    List<JobListing> findAllByLocationsContaining(String location);
}
