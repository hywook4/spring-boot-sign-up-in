FROM openjdk:8-alpine

ADD run.sh run.sh
ADD /build/libs/sign-up-in-0.0.1-SNAPSHOT.jar app.jar

RUN apk update && apk add bash
RUN chmod +x run.sh && mkdir -p /data/h2

EXPOSE 10001

CMD ["/run.sh"]
