FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /workspace

RUN apk add --no-cache maven

COPY pom.xml .
COPY common/ common/
COPY services/ services/

RUN mvn -pl services/eureka-server -am clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=builder \
    /workspace/services/eureka-server/target/*.jar \
    app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]