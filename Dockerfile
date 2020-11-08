FROM gradle:6.6.1-jdk11-openj9 AS build
WORKDIR /cache

COPY build.gradle .
COPY settings.gradle .

RUN gradle --no-daemon dependencies --refresh-dependencies

COPY ./src /cache/src

RUN gradle --no-daemon clean build

#
# RELEASE image
#
FROM adoptopenjdk/openjdk11:jre-11.0.4_11-alpine AS release

COPY --from=build /cache/build/libs/*.jar /service.jar

COPY docker-entrypoint.sh /
RUN apk add --no-cache curl && \
    chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]