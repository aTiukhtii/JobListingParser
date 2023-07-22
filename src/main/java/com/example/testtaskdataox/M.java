package com.example.testtaskdataox;

import com.example.testtaskdataox.model.JobListing;
import com.example.testtaskdataox.service.JobListingScraper;
import com.example.testtaskdataox.service.JobListingService;

public class M {
    public static void main(String[] args) {
        JobListingService jobListingService = new JobListingScraper();
        jobListingService.scrapeAndSaveJobListings();
    }
}
