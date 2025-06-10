# Use an official Kotlin runtime as a parent image
FROM openjdk:26-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the build files
COPY /site/build/libs/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
