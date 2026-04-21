FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
COPY target/marine-biodiversity-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]
