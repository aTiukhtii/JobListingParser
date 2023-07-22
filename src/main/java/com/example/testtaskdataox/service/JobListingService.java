package com.example.testtaskdataox.service;

import com.example.testtaskdataox.model.JobListing;
import java.util.List;

public interface JobListingService {
    List<JobListing> scrapeAndSaveJobListings();
}
