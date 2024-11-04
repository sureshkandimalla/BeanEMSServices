# Use an OpenJDK base image
FROM amazoncorretto:22-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled Java application JAR file into the container
COPY target/springboot_mysql_project-0.0.1-SNAPSHOT.jar /app/springboot_mysql_project-0.0.1-SNAPSHOT.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run your application
CMD ["java", "-jar", "springboot_mysql_project-0.0.1-SNAPSHOT.jar"]
