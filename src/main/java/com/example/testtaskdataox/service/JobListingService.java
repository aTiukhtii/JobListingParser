package com.example.testtaskdataox.service;

import com.example.testtaskdataox.model.JobListing;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface JobListingService {
    List<JobListing> scrapeAndSaveJobListings(String jobFunction);

    List<JobListing> getAllWithSort(Sort sort);
}
