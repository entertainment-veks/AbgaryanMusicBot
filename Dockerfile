# Run stage
FROM eclipse-temurin:17.0.1_12-jre-alpine AS run
WORKDIR /app
# Copy files to main working dir
COPY target/dependency/BOOT-INF/lib     /app/lib
COPY target/dependency/META-INF         /app/META-INF
COPY target/dependency/BOOT-INF/classes /app

ARG SPRING_GROUP=spring
ARG SPRING_USER=spring
RUN addgroup -S ${SPRING_GROUP} && adduser -S ${SPRING_USER} -G ${SPRING_GROUP}
EXPOSE 8080
ENTRYPOINT ["java", "-cp", ".:./lib/*", "ru.antiborov.abgaryanmusicbot.AbgaryanMusicBotApplication"]
