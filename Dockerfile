FROM maven:3.2-jdk-7-onbuild
COPY . app
WORKDIR app
RUN mvn install -Plinux
EXPOSE 52808
WORKDIR /app/dist
RUN chmod -R 777 * 
CMD ["./startupHold.sh"]
