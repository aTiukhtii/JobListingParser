package com.example.testtaskdataox.service;

import com.example.testtaskdataox.model.JobListing;
import java.util.List;

public interface JobListingScraper {
    List<JobListing> scrapeAndSaveJobListings(String jobFunction);
}
