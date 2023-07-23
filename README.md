# ⚡ Job Listing Parser

### 📄 Project description:

Test Task DataOX is a web application that allows users to scrape job listings from the Techstars Jobs website and store them in a database. 
The application is built using Spring Boot and utilizes Selenium WebDriver to interact with the website and fetch job listings. 
Users can use various API endpoints to interact with the data and retrieve job listings based on different criteria.

### 🔨Technologies
- Java 17 
- Spring Boot 
- Hibernate 
- MySQL 
- Selenium WebDriver 
- JSOUP
- Maven


### 📂 Project structure:
#### java/
- [controller](src%2Fmain%2Fjava%2Fcom%2Fexample%2Ftesttaskdataox%2Fcontroller) - http controllers
- [repository](src%2Fmain%2Fjava%2Fcom%2Fexample%2Ftesttaskdataox%2Frepository) - class for CRUD operation with database
- [exception](src%2Fmain%2Fjava%2Fcom%2Fexample%2Ftesttaskdataox%2Fexception) - custom exceptions
- [service](src%2Fmain%2Fjava%2Fcom%2Fexample%2Ftesttaskdataox%2Fservice) - classes that provide business logic
- [util](src%2Fmain%2Fjava%2Fcom%2Fexample%2Ftesttaskdataox%2Futil) - utility sorting class
- [model](src%2Fmain%2Fjava%2Fcom%2Fexample%2Ftesttaskdataox%2Fmodel) - 
  The JobListing class represents a data model for job listings and includes the following attributes:


    id (Long): The unique identifier for each job listing.
    positionName (String): The name of the position for the job listing.
    organizationTitle (String): The title of the organization associated with the job listing.
    logoLink (String): The link to the logo of the organization.
    organizationUrl (String): The URL of the organization's website.
    laborFunction (String): The labor function of the job.
    jobPageUrl (String): The URL of the job listing's page.
    description (String): The description of the job listing with TEXT column definition.
    postedDate (Long): The timestamp representing the date when the job listing was posted.
    locations (List of Strings): A list of locations associated with the job listing.
    tags (List of Strings): A list of tags associated with the job listing.

#### resources/
- [application.properties](src%2Fmain%2Fresources%2Fapplication.properties) - contains database configuration
- [database_dump.sql](src%2Fmain%2Fresources%2Fdatabase_dump.sql) - contains dump of database
- [example-result.png](src%2Fmain%2Fresources%2Fexample-result.png) - contains example result
- [credentials.png](src%2Fmain%2Fresources%2Fcredentials.png) - contains default credentials

#### other/

- [INSTALL.md](INSTALL.md) - instruction to start application
- [pom.xml](pom.xml) - contains maven configuration


