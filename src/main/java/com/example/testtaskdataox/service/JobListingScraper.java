package com.example.testtaskdataox.service;

import com.example.testtaskdataox.exception.InvalidUrlException;
import com.example.testtaskdataox.model.JobListing;
import com.example.testtaskdataox.repository.JobListingRepository;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobListingScraper implements JobListingService {
    public static final String BASE_URL = "https://jobs.techstars.com/";
    private static final int LABOR_POSITION = 1;
    private static final int LOCATION_POSITION = 2;
    @Autowired
    private  JobListingRepository jobListingRepository;

//    public JobListingScraper(JobListingRepository jobListingRepository) {
//        this.jobListingRepository = jobListingRepository;
//    }

    @Override
    public List<JobListing> scrapeAndSaveJobListings() {
        List<JobListing> scrapedJobListings = new ArrayList<>();
        Document document = getPage(BASE_URL);
        Elements jobListingElements = document.select("div[class=sc-beqWaB gupdsY job-card]");
        for (Element jobListingElement : jobListingElements) {
            String fullUrl = getFullUrl(jobListingElement, BASE_URL);
            Document jobListingDocument = getPage(fullUrl);
            String positionName = getPositionName(jobListingElement);
            String organizationTitle = getOrganizationTitle(jobListingDocument);
            String logoLink = getLogoLink(jobListingDocument);
            String organizationUrl = getOrganizationUrl(jobListingDocument, BASE_URL);
            String laborFunction = getLaborFunction(jobListingDocument);
            String description = getDescription(jobListingDocument);
            long postedDateText = parseDateToTimestamp(getPostedDateText(jobListingDocument));
            List<String> locationsList = getLocationsList(jobListingDocument);
            List<String> tagsList = getTagList(jobListingElement);
            JobListing jobListing = new JobListing();
            jobListing.setPositionName(positionName);
            jobListing.setOrganizationTitle(organizationTitle);
            jobListing.setLogoLink(logoLink);
            jobListing.setOrganizationUrl(organizationUrl);
            jobListing.setLaborFunction(laborFunction);
            jobListing.setDescription(description);
            jobListing.setPostedDate(postedDateText);
            jobListing.setLocations(locationsList);
            jobListing.setTags(tagsList);
            jobListing.setJobPageURL(fullUrl);
            scrapedJobListings.add(jobListing);
            //save(jobListing);
            // Print or process the extracted data as per your requirements
            printJobDetails(positionName, fullUrl, organizationTitle, organizationUrl, logoLink,
                    laborFunction, description, postedDateText, locationsList, tagsList);
        }
        return scrapedJobListings;
    }

    private JobListing save(JobListing jobListing) {
        return jobListingRepository.save(jobListing);
    }

    private Document getPage(String url) {
        try {
            return Jsoup.parse(new URL(url), 300000);
        } catch (IOException e) {
            throw new InvalidUrlException("Unfortunately, "
                    + "this vacancy is posted on a third-party website: " + url);
        }
    }

    private String getFullUrl(Element element, String baseUrl) {
        String relativeUrl = element.select("a[data-testid='job-title-link']")
                .attr("href");
        return baseUrl + relativeUrl;
    }

    private String getFullUrl(String relativeUrl, String baseUrl) {
        return baseUrl + relativeUrl;
    }

    private String getPositionName(Element element) {
        return element.select("div[class=sc-beqWaB sc-gueYoa iUlpOy MYFxR]").text();
    }

    private String getOrganizationTitle(Document document) {
        return document.select("p[class=sc-beqWaB bpXRKw]").text();
    }

    private String getLogoLink(Document document) {
        return document.select("img[data-testid='image']").attr("src");
    }

    private String getOrganizationUrl(Document document, String baseUrl) {
        String relativeUrl = document.select("a[data-testid='button']").attr("href");
        return getFullUrl(relativeUrl, baseUrl);
    }

    private String getLaborFunction(Document document) {
        return document.select("div[class=sc-beqWaB bpXRKw]").get(LABOR_POSITION).text();
    }

    private String getDescription(Document document) {
        return document.select("div[class=sc-beqWaB fmCCHr]").html();
    }

    private String getPostedDateText(Document document) {
        return document.select("div[class=sc-beqWaB gRXpLa]")
                .text().replace("Posted on ", "");
    }

    private long parseDateToTimestamp(String dateText) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
            Date date = dateFormat.parse(dateText);
            return date.getTime();
        } catch (ParseException e) {
            throw new RuntimeException("invalid data: " + dateText);
        }
    }

    private List<String> getLocationsList(Document document) {
        String locationsText = document.select("div[class=sc-beqWaB bpXRKw]")
                .get(LOCATION_POSITION).text();
        return Arrays.asList(locationsText.split(","));
    }

    private List<String> getTagList(Element element) {
        Elements tagElements = element.select("div[data-testid='tag'] div[class=sc-dmqHEX dncTlc]");
        List<String> tagsList = new ArrayList<>();
        for (Element tagElement : tagElements) {
            String tagName = tagElement.text();
            tagsList.add(tagName);
        }
        return tagsList;
    }

    private void printJobDetails(String positionName, String fullUrl, String organizationTitle,
                                 String organizationUrl, String logoLink, String laborFunction,
                                 String description, long postedDateText, List<String> locationsList,
                                 List<String> tagsList) {
        System.out.println("Position Name: " + positionName);
        System.out.println("Job Page URL: " + fullUrl);
        System.out.println("Organization Title: " + organizationTitle);
        System.out.println("Organization URL: " + organizationUrl);
        System.out.println("Logo Link: " + logoLink);
        System.out.println("Labor Function: " + laborFunction);
        System.out.println("Location: " + locationsList);
        System.out.println("Posted Date (Unix Timestamp): " + postedDateText);
        System.out.println("Description (with HTML formatting): " + description);
        System.out.println("Tag Names: " + tagsList);
        System.out.println("---------------------------------------");
    }
}