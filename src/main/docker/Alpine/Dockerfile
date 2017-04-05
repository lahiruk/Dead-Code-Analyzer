FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp

RUN apk update
RUN apk add libstdc++
RUN apk add glib
RUN apk add libxext-dev
RUN apk add libxrender
RUN apk add libxtst
RUN apk add libxi-dev
    
RUN mkdir /tmp/linux64
COPY ["linux64","/tmp/linux64"]
ADD dead-code-analyzer-1.0.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENV PATH="$PATH:/tmp/linux64/"
ENV LD_LIBRARY_PATH="./usr/lib/jvm/java-8-oracle/jre/lib/amd64/server:$LD_LIBRARY_PATH"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]