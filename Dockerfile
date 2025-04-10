# Stage 1: Build Stage
FROM bellsoft/liberica-runtime-container:jdk-17-stream-musl AS builder

WORKDIR /home/app
COPY . /home/app/spring-boot-aws-sns-sqs-ses
WORKDIR /home/app/spring-boot-aws-sns-sqs-ses
RUN  chmod +x mvnw && ./mvnw -Dmaven.test.skip=true clean package

# Stage 2: Layer Tool Stage
FROM bellsoft/liberica-runtime-container:jdk-17-cds-slim-musl AS optimizer

WORKDIR /home/app
COPY --from=builder /home/app/spring-boot-aws-sns-sqs-ses/target/*.jar spring-boot-aws-sns-sqs-ses.jar
RUN java -Djarmode=tools -jar spring-boot-aws-sns-sqs-ses.jar extract --layers --launcher

# Stage 3: Final Stage
FROM bellsoft/liberica-runtime-container:jre-17-stream-musl

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
COPY --from=optimizer /home/app/spring-boot-aws-sns-sqs-ses/dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-aws-sns-sqs-ses/spring-boot-loader/ ./
COPY --from=optimizer /home/app/spring-boot-aws-sns-sqs-ses/snapshot-dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-aws-sns-sqs-ses/application/ ./