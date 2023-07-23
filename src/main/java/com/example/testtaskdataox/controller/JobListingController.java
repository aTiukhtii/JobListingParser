package com.example.testtaskdataox.controller;

import static com.example.testtaskdataox.util.ParsingJobListing.filterByLocation;
import static com.example.testtaskdataox.util.ParsingJobListing.parsing;

import com.example.testtaskdataox.model.JobListing;
import com.example.testtaskdataox.repository.JobListingRepository;
import com.example.testtaskdataox.service.JobListingScraper;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrapeJobListings")
public class JobListingController {

    private final JobListingScraper jobListingScraper;
    private final JobListingRepository jobListingRepository;

    public JobListingController(JobListingScraper jobListingScraper,
                                JobListingRepository jobListingRepository) {
        this.jobListingScraper = jobListingScraper;
        this.jobListingRepository = jobListingRepository;
    }

    @GetMapping
    public List<JobListing> scrapeJobListings(@RequestParam String jobFunction,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(required = false) String location) {
        Sort sort = Sort.by(parsing(sortBy));
        jobListingScraper.scrapeAndSaveJobListings(jobFunction);
        List<JobListing> jobListings = jobListingScraper.getAllWithSort(sort);
        if (location != null && !location.isEmpty()) {
            jobListings = filterByLocation(jobListings, location);
        }
        return jobListings;
    }
}
