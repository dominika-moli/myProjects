FROM gradle:7.0.0-jdk11 AS builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar -i --no-daemon --parallel

FROM registry.access.redhat.com/ubi8/openjdk-11
COPY --from=builder /home/gradle/src/build /opt/build
WORKDIR /opt/build
EXPOSE 8080
USER 1001
CMD ["/bin/bash", "-c", "find -type f -name '*.jar' | xargs java -jar"]
