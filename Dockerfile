# Multi-stage build for Kobweb Todo Application

# Stage 1: Build the application
FROM gradle:8.5-jdk17 AS builder

WORKDIR /build

# Copy gradle files first for better caching
COPY gradle gradle
COPY gradlew gradlew.bat settings.gradle.kts gradle.properties ./
COPY site/build.gradle.kts site/

# Download dependencies
RUN ./gradlew dependencies --no-daemon || true

# Copy the source code
COPY site/src site/src
COPY site/.kobweb site/.kobweb

# Build the application (production bundle and jar)
RUN ./gradlew :site:kobwebExport -PkobwebReuseServer=false -PkobwebEnv=PROD --no-daemon

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install script dependencies
RUN apk add --no-cache bash

# Copy the exported site from builder
COPY --from=builder /build/site/.kobweb /app/.kobweb

# Copy the server scripts
COPY --from=builder /build/site/build/kobweb/server /app/server

# Set environment variables for database connection
ENV DB_HOST=db
ENV DB_PORT=5432
ENV DB_NAME=todos
ENV DB_USER=malefic
ENV DB_PASSWORD=password

# Expose the port the application runs on
EXPOSE 8080

# Run the Kobweb server
CMD ["bash", "/app/server/start.sh"]
