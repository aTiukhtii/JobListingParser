package com.example.testtaskdataox.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class JobListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String positionName;
    private String organizationTitle;
    private String logoLink;
    private String organizationUrl;
    private String laborFunction;
    private String JobPageURL;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private Long postedDate;

    @ElementCollection
    @CollectionTable(name = "job_listing_locations", joinColumns = @JoinColumn(name = "job_listing_id"))
    @Column(name = "location")
    private List<String> locations;

    @ElementCollection
    @CollectionTable(name = "job_listing_tags", joinColumns = @JoinColumn(name = "job_listing_id"))
    @Column(name = "tag")
    private List<String> tags;
}
