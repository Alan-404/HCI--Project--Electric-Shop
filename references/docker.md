```
# Use openjdk:8-jdk-alpine as base image
FROM openjdk:8-jdk-alpine

# Copy JCE files into base image
COPY jce_policy-8.zip /tmp
RUN unzip /tmp/jce_policy-8.zip -d /tmp && \
    cp /tmp/UnlimitedJCEPolicyJDK8/*.jar /usr/lib/jvm/java-1.8-openjdk/jre/lib/security && \
    rm -rf /tmp/jce_policy-8.zip /tmp/UnlimitedJCEPolicyJDK8

# Copy Spring Boot application JAR file into base image
COPY my-app.jar /app

# Specify entrypoint for application
ENTRYPOINT ["java", "-jar", "/app/my-app.jar"]
```