# Step 1: Build the project using Maven
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests  # This will build the JAR file

# Step 2: Use OpenJDK to run the app
FROM openjdk:17
WORKDIR /app
COPY --from=build /app/target/vacation*.jar /usr/src/vacation.jar
COPY vacation/src/main/resources/application.properties /opt/conf/application.properties

# Step 3: Run the application with the specified properties file
CMD ["java", "-jar", "/usr/src/vacation.jar", "--spring.config.location=file:/opt/conf/application.properties"]
