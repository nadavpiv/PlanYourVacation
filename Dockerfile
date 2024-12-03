FROM openjdk:17
COPY vacation/target/vacation*.jar /usr/src/vacation.jar
COPY vacation/src/main/resources/application.properties /opt/conf/application.properties
CMD ["java", "-jar", "/usr/src/vacation.jar", "--spring.config.location=file:/opt/conf/application.properties"]
