# Multi-stage build for Kobweb Todo Application

# Stage 1: Build the application
FROM gradle:9.1-jdk17 AS builder

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

# Build the production JS bundle
RUN ./gradlew :site:jsBrowserProductionWebpack --no-daemon

# Build the JVM API jar
RUN ./gradlew :site:jvmJar --no-daemon

# Unpack the Kobweb server and create start scripts
RUN ./gradlew :site:kobwebUnpackServerJar :site:kobwebCreateServerScripts --no-daemon

# Stage 2: Runtime image
FROM eclipse-temurin:25-jre-alpine

# Install bash and curl for running scripts and health checks
RUN apk add --no-cache bash curl

# Set up the site directory structure that matches the build
WORKDIR /app/site

# Copy .kobweb folder with server.jar and start scripts
COPY --from=builder /build/site/.kobweb .kobweb

# Copy build directory with JS bundle, API jar, and resources
COPY --from=builder /build/site/build build

# Set environment variables for database connection
ENV DB_HOST=db
ENV DB_PORT=5432
ENV DB_NAME=todos
ENV DB_USER=malefic
ENV DB_PASSWORD=password

# Expose the port the application runs on
EXPOSE 8080

# Run the Kobweb server start script
CMD ["bash", ".kobweb/server/start.sh"]
