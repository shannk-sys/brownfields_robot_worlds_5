# Use Eclipse Temurin OpenJDK 21 as the base image
FROM eclipse-temurin:21-jdk

# Set working directory in the container
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better layer caching)
COPY pom.xml ./
COPY scripts scripts/
# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Create a new stage for the runtime image (multi-stage build)
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=0 /app/target/robot-world-0.0.2.jar app.jar

# Expose port (if your app will have a web interface later)
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]