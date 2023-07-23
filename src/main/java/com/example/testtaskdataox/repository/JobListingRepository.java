package com.example.testtaskdataox.repository;

import com.example.testtaskdataox.model.JobListing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {
    List<JobListing> findAllByLocationsContaining(String location);
}
