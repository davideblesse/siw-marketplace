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

# --- tiny entrypoint that rewrites DATABASE_URL → SPRING_DATASOURCE_URL ---
RUN printf '%s\n' \
  '#!/usr/bin/env sh' \
  'if [ -n "$DATABASE_URL" ] && [ -z "$SPRING_DATASOURCE_URL" ]; then' \
  '  export SPRING_DATASOURCE_URL="$(echo "$DATABASE_URL" | sed "s|^postgres://|jdbc:postgresql://|")"' \
  'fi' \
  'exec java -jar /app/app.jar' \
  > /entrypoint.sh && chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
EXPOSE 8080

