# 1. Java 21 tabanlı küçük boyutlu imaj
FROM eclipse-temurin:21-jdk-alpine

# 2. Uygulama çalışma klasörü
WORKDIR /app

# 3. JAR dosyasını kopyala (JAR ismini build sırasında override edebilirsin)
COPY build/libs/smart_home-0.0.1-SNAPSHOT.jar app.jar

# 4. config klasörü dışarıdan bind edilebilir olacak şekilde bırakılır
VOLUME ["/app/config"]

# 5. YML dosyasının yolunu Spring'e bildir
ENV SPRING_CONFIG_LOCATION=file:/app/config/application.yml

# 6. Uygulama portu
EXPOSE 8080

# 7. Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "app.jar"]
