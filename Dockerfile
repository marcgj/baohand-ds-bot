FROM ubuntu:latest
RUN \
# Update
apt-get update -y && \
# Install Java
apt-get install openjdk-17-jre -y

ADD ./baohand-ds-bot.jar bot.jar
ADD ./.env .env

CMD java -jar bot.jar
