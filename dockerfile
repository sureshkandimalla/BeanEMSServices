
# Use an official Java runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/springboot_mysql_project-0.0.1-SNAPSHOT.jar /app/springboot_mysql_project-0.0.1-SNAPSHOT.jar


# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "springboot_mysql_project-0.0.1-SNAPSHOT.jar"]

