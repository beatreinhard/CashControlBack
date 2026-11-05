# Packt das bereits gebaute, ausfuehrbare Spring-Boot-Jar (inkl. Frontend) in ein Image.
# Das Jar wird nicht hier gebaut, darum vorher im Repo-Root ausfuehren:
#
#   sh backend/mvnw clean install
#   docker build -t cashcontrol .

FROM eclipse-temurin:21-jre
RUN useradd --system --no-create-home cashcontrol
WORKDIR /app
# Nur das -exec-Jar ist ausfuehrbar (siehe classifier im spring-boot-maven-plugin).
COPY backend/target/backend-*-exec.jar app.jar
USER cashcontrol
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
