FROM gradle:8.10-jdk21-alpine AS build
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /deployments

# Copy Quarkus application
COPY --from=build /app/build/quarkus-app/lib/ /deployments/lib/
COPY --from=build /app/build/quarkus-app/*.jar /deployments/
COPY --from=build /app/build/quarkus-app/app/ /deployments/app/
COPY --from=build /app/build/quarkus-app/quarkus/ /deployments/quarkus/

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0"

EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]
