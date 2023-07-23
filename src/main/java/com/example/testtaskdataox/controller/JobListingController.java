package com.example.testtaskdataox.controller;

import static com.example.testtaskdataox.util.ParsingJobListing.parsing;

import com.example.testtaskdataox.model.JobListing;
import com.example.testtaskdataox.service.JobListingScraper;
import com.example.testtaskdataox.service.JobListingScraperImpl;
import com.example.testtaskdataox.service.JobListingService;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrapeJobListings")
public class JobListingController {
    private final JobListingService jobListingService;
    private final JobListingScraper jobListingScraperImpl;

    public JobListingController(JobListingService jobListingService,
                                JobListingScraperImpl jobListingScraperImpl) {
        this.jobListingService = jobListingService;
        this.jobListingScraperImpl = jobListingScraperImpl;
    }

    @GetMapping
    public List<JobListing> scrapeJobListings(@RequestParam String jobFunction,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(required = false) String location) {
        Sort sort = Sort.by(parsing(sortBy));
        jobListingScraperImpl.scrapeAndSaveJobListings(jobFunction);
        List<JobListing> jobListings = jobListingService.getAllWithSort(sort);
        if (location != null && !location.isEmpty()) {
            jobListings = jobListingService.findAllByLocationsContaining(location);
        }
        return jobListings;
    }

    @GetMapping("/all")
    public List<JobListing> getAllJobListings(@RequestParam(defaultValue = "id") String sortBy) {
        Sort sort = Sort.by(parsing(sortBy));
        return jobListingService.getAllWithSort(sort);
    }
}
