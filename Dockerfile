FROM adoptopenjdk/openjdk11:ubi

WORKDIR /program
ADD setenv.sh setenv.sh
ADD target/demo-0.0.1-SNAPSHOT.jar app.jar
ADD holidayapi.com.cer holidayapi.com.cer

EXPOSE 50000

VOLUME ["/logs", "/logs/gc"]
RUN keytool -import -alias backbase -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt -file holidayapi.com.cer

CMD ["/bin/bash", "-c", "source setenv.sh && exec java $JAVA_OPTS -jar app.jar"]
