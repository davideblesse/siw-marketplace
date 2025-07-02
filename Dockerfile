# ---------- build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src/ src/
RUN mvn -B clean package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/*-SNAPSHOT.jar app.jar

# *** create the directory Spring expects ***
RUN mkdir -p /app/src/main/resources/static/images

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
