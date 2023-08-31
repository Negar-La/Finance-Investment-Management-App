# Use a Maven image with OpenJDK 11 as the base image for building
FROM maven:3.8.3-openjdk-11 AS build

# Copy the entire project into the image
COPY . /app
WORKDIR /app

# Build the application with Maven
RUN mvn clean package -DskipTests

# Use a lightweight OpenJDK 11 image as the base image for running the application
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory in the image
WORKDIR /app

# Copy the JAR file from the build stage to the image
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar ./demo.jar

# Expose the port your application is running on
EXPOSE 8080

# Command to run your application when the container starts
CMD ["java", "-jar", "demo.jar"]
