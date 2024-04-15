FROM openjdk:17
COPY target/gift-service-1.0.0.jar /app/gift.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=k8s", "/app/gift.jar"]
