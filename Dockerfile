# ---------- build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src/ src/
RUN mvn -B clean package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy your fat-jar in
COPY --from=build /workspace/target/*-SNAPSHOT.jar app.jar

# Create the folder Spring expects for uploads
RUN mkdir -p /app/src/main/resources/static/images

# Wrap DATABASE_URL → SPRING_DATASOURCE_URL and launch
RUN printf '%s\n' \
  '#!/usr/bin/env sh' \
  'if [ -n "$DATABASE_URL" ] && [ -z "$SPRING_DATASOURCE_URL" ]; then' \
  '  export SPRING_DATASOURCE_URL=$(echo "$DATABASE_URL" \
       | sed -E "s|^postgres(ql)?://|jdbc:postgresql://|")' \
  'fi' \
  'exec java -jar /app/app.jar' \
  > /entrypoint.sh && chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
EXPOSE 8080

