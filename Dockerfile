FROM maven:3.6.3-jdk-11-slim AS build
WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline

COPY ./src /build/src

RUN mvn clean package --batch-mode

#
# RELEASE image
#
FROM adoptopenjdk/openjdk11:jre-11.0.4_11-alpine AS release

COPY --from=build /build/target/*.jar /service.jar

COPY docker-entrypoint.sh /
RUN apk add --no-cache curl && \
    chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]