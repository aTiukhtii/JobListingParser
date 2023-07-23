# 📃 Installation Instructions

To run the project, you need to have the following software installed on your machine:

1. Java Development Kit (JDK) 17 or later
2. Google Chrome browser

### 🪜 Steps to Run the Project

1. Clone the repository to your local machine.

2. Open IntelliJ IDEA and import the project.
## 🔑 Setting up
1. First of all, you need to prepare the database. (For developing this web application, MySQL was used.) Create a schema and copy everything from the `database_dump.sql` file, also you can find this file on my google drive https://drive.google.com/file/d/10DYqNMYaeiT3sa6KO_ELC3PBwfufCNCm/view?usp=sharing then execute it in your DBMS.
2. To set up the project input correct credentials. In the `resourses/application.properties`, substitute the highlighted variables.


![img.png](src/main/resources/credentials.png)

3. Build and run the app

4. The application will be accessible at `http://localhost:8080`. You can interact with the API endpoints using Postman or any other REST client.

### 🌐 API Endpoints

1. To scrape job listings and save them to the database, send a GET request to:

   http://localhost:8080/scrapeJobListings?jobFunction=<JOB_FUNCTION>&sortBy=postedDate&location=New%20York

2. To retrieve all job listings from the database, send a GET request to:

   http://localhost:8080/scrapeJobListings/all

3. To retrieve all job listings from the database, sorted by a specific field (ASC by default), send a GET request to:

   http://localhost:8080/scrapeJobListings?sortBy=<SORT_BY>:DESC

Replace `<SORT_BY>` with the desired field to sort the results.

Replace `<JOB_FUNCTION>` with the desired field to sort the results.

- For example for the query http://localhost:8080/scrapeJobListings?jobFunction=<JOB_FUNCTION>&sortBy=postedDate&location=New%20York you will get
![example-result.png](src%2Fmain%2Fresources%2Fexample-result.png)
### Available Job Functions
1. Data Science
2. Design
3. IT
4. Legal
5. Marketing & Communications
6. Operations
7. Other Engineering
8. People & HR
9. Product
10. Quality Assurance
11. Sales & Business Development
12. Software Engineering

Note: When using job functions with spaces in the URL, replace each space with %20 and each ampersand with %26. For example, Marketing & Communications should be represented as Marketing%20%26%20Communications.

Note: This job listing parser works exclusively with job vacancies from the Techstars Jobs website.   The application uses Selenium WebDriver, which interacts with the Chrome browser. By default, it uses a headless mode, so you don't need to download or configure ChromeDriver separately. The project already includes the necessary dependencies to handle WebDriver interactions.

