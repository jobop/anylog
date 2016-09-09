FROM maven:3.2-jdk-7-onbuild
RUN apk add --no-cache wget bash
COPY . app
WORKDIR app
RUN mvn install -Plinux
EXPOSE 52808
WORKDIR dist
RUN chmod 777 startupHold.sh
CMD ["pwd"]
