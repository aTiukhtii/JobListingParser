FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY chromedriver_linux64 /usr/local/bin/chromedriver
ENV CHROMEDRIVER_PATH /usr/local/bin/chromedriver
ENTRYPOINT ["java", "-Dwebdriver.chrome.driver=${CHROMEDRIVER_PATH}", "-jar", "app.jar"]
EXPOSE 5005
