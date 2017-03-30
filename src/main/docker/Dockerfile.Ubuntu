FROM ubuntu:14.04

# Install Java 8
RUN apt-get update
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get update
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN apt-get install oracle-java8-installer -y --force-yes
RUN apt-get install oracle-java8-set-default

#Install aws cli
RUN apt-get install awscli -y

VOLUME /tmp
RUN mkdir /tmp/linux64
COPY ["linux64","/tmp/linux64"]
ADD dead-code-analyzer-1.0.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENV PATH="$PATH:/tmp/linux64/"
EXPOSE 80 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]