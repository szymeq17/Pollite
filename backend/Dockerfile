# Alpine Linux with OpenJDK JRE
FROM openjdk:17-oracle

VOLUME /tmp

# Copy war file
COPY target/*.jar pollite.jar

# run the app
ENTRYPOINT ["java","-jar","/pollite.jar"]