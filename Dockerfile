# Этап 1: сборка проекта с помощью Maven
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

# Кэшируем зависимости отдельно
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем остальное и собираем проект
COPY . .
RUN mvn clean package -DskipTests

# Этап 2: запуск приложения
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Копируем собранный JAR из предыдущего этапа
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт (необязательно, но удобно)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
