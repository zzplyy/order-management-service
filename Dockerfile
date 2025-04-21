FROM eclipse-temurin:21-jdk as build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:21-jdk
VOLUME /tmp
COPY --from=build /app/target/order-management-service-1.0-SNAPSHOT.jar app.jar

# Проверяем содержимое JAR внутри контейнера
RUN jar tf /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
