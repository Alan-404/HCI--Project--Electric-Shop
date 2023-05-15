FROM maven:3.9.1-eclipse-temurin-8 AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY DigiCertGlobalRootCA.crt.pem /home/app
RUN mvn -f /home/app/pom.xml clean package


FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/target/*.jar app.jar
COPY DigiCertGlobalRootCA.crt.pem DigiCertGlobalRootCA.crt.pem
COPY jce_policy-8.zip /tmp
RUN unzip /tmp/jce_policy-8.zip -d /tmp && \
    cp /tmp/UnlimitedJCEPolicyJDK8/*.jar /usr/lib/jvm/java-1.8-openjdk/jre/lib/security && \
    rm -rf /tmp/jce_policy-8.zip /tmp/UnlimitedJCEPolicyJDK8
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
