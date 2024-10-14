# Base image olarak OpenJDK kullanıyoruz
FROM openjdk:17-jdk-slim

# Çalışma dizinini ayarlıyoruz
WORKDIR /app

# Maven bağımlılıklarını yüklemek için pom.xml ve src klasörlerini kopyalayın
COPY pom.xml .
COPY src ./src

# Maven ile uygulamayı derleyin
RUN ./mvnw clean package -DskipTests

# Jar dosyasını çalıştırın
COPY target/your-application-name.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
