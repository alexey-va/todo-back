FROM eclipse-temurin:latest
COPY target/*.jar /app/app.jar
WORKDIR /app
EXPOSE 9090
ENV JAVA_OPTS="-Xmx256m -Xms128m"
CMD java $JAVA_OPTS -jar app.jar