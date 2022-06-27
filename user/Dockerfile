# Maven image koji se koristi za build
FROM docker.io/maven:3.8.5-openjdk-11 AS builder

# Verzija aplikacije
ARG VERSION=0.0.1-SNAPSHOT

# Aplikaciju cemo da buildujemo u /usr/app
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME

ADD . $HOME
RUN --mount=type=cache,target=/root/.m2 mvn -Drevision=$VERSION -Dmaven.test.skip -f $HOME/pom.xml clean package -P prod
# Finalni stage koji ce zapravno da pokrene aplikaciju

FROM adoptopenjdk:11-jre-hotspot as extractor

ARG VERSION=0.0.1-SNAPSHOT
ARG SERVICE_NAME="user" # zameniti sa vasim nazivom app
# Kopiranje izbuildanog JAR-a iz prethodnog stage-a
COPY --from=builder /usr/app/target/$SERVICE_NAME-$VERSION.jar application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:11-jre-hotspot

LABEL org.opencontainers.image.source https://github.com/RAF-SI-2021/Racunovodstvo-Back

COPY --from=extractor dependencies/ ./
COPY --from=extractor snapshot-dependencies/ ./
COPY --from=extractor spring-boot-loader/ ./
COPY --from=extractor application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
