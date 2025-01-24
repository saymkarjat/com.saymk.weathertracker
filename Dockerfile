FROM maven:3.9.5-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM tomcat:10.1.34-jre21-temurin-jammy

ENV JAVA_OPTS="-Dspring.profiles.active=dev"

COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]
