package com.example.testtaskdataox.service;

import com.example.testtaskdataox.exception.InvalidUrlException;
import com.example.testtaskdataox.model.JobListing;
import com.example.testtaskdataox.repository.JobListingRepository;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class JobListingScraper implements JobListingService {
    public static final String BASE_URL = "https://jobs.techstars.com/";
    private static final int LABOR_POSITION = 1;
    private static final int LOCATION_POSITION = 2;
    private static final int NUMBER_VACANCY_PER_PAGE = 20;
    private final JobListingRepository jobListingRepository;

    public JobListingScraper(JobListingRepository jobListingRepository) {
        this.jobListingRepository = jobListingRepository;
    }

    @Override
    public List<JobListing> scrapeAndSaveJobListings(String jobFunction) {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        List<JobListing> scrapedJobListings = new ArrayList<>();
        try {
            driver.get(BASE_URL);

            scrollPage(driver, 1500);

            WebElement jobFunctionDropdown =
                    driver.findElement(By.cssSelector("button[data-testid='dropdown-trigger']"));
            jobFunctionDropdown.click();

            WebElement jobFunctionOption = getJobFunctionDropdown(driver, jobFunction);
            jobFunctionOption.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(1000));
            wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.cssSelector("button[data-testid='load-more']")));

            WebElement loadMoreButton =
                    driver.findElement(By.cssSelector("button[data-testid='load-more']"));
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", loadMoreButton);
            loadMoreButton.click();

            Document document = getPage(driver.getCurrentUrl());
            int numberPages = numberPages(document);

            for (int i = 0; i < numberPages; i++) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, "
                        + "document.body.scrollHeight);");
                Thread.sleep(200);
            }
            List<Element> jobListings = getListingElements(document);
            for (Element jobListingElement : jobListings) {
                String fullUrl = getFullUrl(jobListingElement);
                Document jobListingDocument = getPage(fullUrl);
                JobListing jobListing = getJobListingDetails(jobListingElement,
                        jobListingDocument, fullUrl);
                scrapedJobListings.add(save(jobListing));
                printJobDetails(jobListing);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            driver.quit();
        }
        return scrapedJobListings;
    }

    @Override
    public List<JobListing> getAllWithSort(Sort sort) {
        return jobListingRepository.findAll(sort);
    }

    @Override
    public List<JobListing> findAll() {
        return jobListingRepository.findAll();
    }

    @Override
    public List<JobListing> findAllByLocationsContaining(String location) {
        return jobListingRepository.findAllByLocationsContaining(location);
    }

    private WebElement getJobFunctionDropdown(WebDriver driver, String jobFunction) {
        WebElement element;
        try {
            element = driver.findElement(By.xpath("//button[text()='" + jobFunction + "']"));
        } catch (Exception e) {
            scrollElementIntoView(driver, 300);
            element = driver.findElement(By.xpath("//button[text()='" + jobFunction + "']"));
        }
        return element;
    }

    private void scrollPage(WebDriver driver, int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, arguments[0]);", pixels);
    }

    private void scrollElementIntoView(WebDriver driver, int pixels) {
        WebElement dropdownContent =
                driver.findElement(By.cssSelector("div[data-testid='dropdown-content']"));
        WebElement innerScroller = dropdownContent
                .findElement(By.cssSelector("div[data-test-id='virtuoso-scroller']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop += arguments[1];",
                innerScroller, pixels);
    }

    private Integer numberPages(Document document) {
        return Integer.parseInt(document.select("#content > div.sc-beqWaB.eLTiFX "
                        + "> div.sc-beqWaB.sc-gueYoa.krgmev.MYFxR "
                        + "> div.sc-beqWaB.lfHtNp > div > div > div > div > b").text()
                .replace(",", "")) / NUMBER_VACANCY_PER_PAGE;
    }

    private List<Element> getListingElements(Document document) {
        return document.select("div[class=sc-beqWaB gupdsY job-card]")
                .stream()
                .filter(this::validateUrl)
                .collect(Collectors.toList());
    }

    private boolean validateUrl(Element element) {
        String relativeUrl = element.select("a[data-testid='job-title-link']")
                .attr("href");
        return !relativeUrl.startsWith("https:");
    }

    private JobListing save(JobListing jobListing) {
        return jobListingRepository.save(jobListing);
    }

    private Document getPage(String url) {
        try {
            return Jsoup.parse(new URL(url), 300000);
        } catch (IOException e) {
            throw new InvalidUrlException("Unfortunately, this url is invalid: " + url);
        }
    }

    private String getFullUrl(Element element) {
        String relativeUrl = element.select("a[data-testid='job-title-link']")
                .attr("href");
        return JobListingScraper.BASE_URL + relativeUrl;
    }

    private JobListing getJobListingDetails(Element jobListingElement,
                                            Document jobListingDocument, String fullUrl) {
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
        jobListing.setJobPageUrl(fullUrl);
        return jobListing;
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
        return baseUrl + relativeUrl;
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
        String[] locationsArray = locationsText.split(",");
        for (int i = 0; i < locationsArray.length; i++) {
            locationsArray[i] = locationsArray[i].trim();
        }
        return Arrays.asList(locationsArray);
    }

    private List<String> getTagList(Element element) {
        Elements tagElements = element.select("div[data-testid='tag'] div[class=sc-dmqHEX dncTlc]");
        return tagElements.stream()
                .map(Element::text)
                .collect(Collectors.toList());
    }

    private void printJobDetails(JobListing jobListing) {
        System.out.println("Position Name: " + jobListing.getPositionName());
        System.out.println("Job Page URL: " + jobListing.getJobPageUrl());
        System.out.println("Organization Title: " + jobListing.getOrganizationTitle());
        System.out.println("Organization URL: " + jobListing.getOrganizationUrl());
        System.out.println("Logo Link: " + jobListing.getLogoLink());
        System.out.println("Labor Function: " + jobListing.getLaborFunction());
        System.out.println("Location: " + jobListing.getLocations());
        System.out.println("Posted Date (Unix Timestamp): " + jobListing.getPostedDate());
        System.out.println("Description (with HTML formatting): " + jobListing.getDescription());
        System.out.println("Tag Names: " + jobListing.getTags());
        System.out.println("---------------------------------------");
    }
}
