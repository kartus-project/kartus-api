# ---- build stage ----
FROM gradle:9.3.0-jdk21-alpine AS builder
WORKDIR /workspace

COPY settings.gradle build.gradle ./
RUN gradle dependencies --configuration runtimeClasspath --no-daemon

COPY src src
RUN gradle bootJar --no-daemon

# ---- runtime stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -g 10001 kartus-app && adduser -u 10001 -G kartus-app -S -D kartus-app

COPY --from=builder --chown=10001:10001 /workspace/build/libs/*.jar app.jar

USER 10001:10001

# 힙 한도 == 컨테이너 메모리 한도의 75%
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
