FROM maven:3.3.9-jdk-7
RUN mkdir -p /usr/local/app
WORKDIR /usr/local/app
ADD . /usr/local/app

RUN mvn install -Plinux \
    && chmod 777 dist -R

EXPOSE 52808
WORKDIR dist
CMD ["pwd"]
