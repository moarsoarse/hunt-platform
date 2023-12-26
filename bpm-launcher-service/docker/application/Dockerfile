FROM openjdk:11-jdk-slim

#Configure Timezone
ENV TZ=Europe/Warsaw
RUN cp /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#Check java version
RUN java -version

VOLUME /tmp

ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/app.jar", "--spring.config.location=classpath:/application.properties,file:/configuration/application.properties"]

EXPOSE 8080