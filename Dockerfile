FROM maven3.3.9-jdk-7
COPY . app
WORKDIR app
RUN mvn install -Plinux
EXPOSE 52808
WORKDIR /app/dist
RUN chmod 777 * -R
CMD ["./startupHold.sh"]