## Dead Code Analyser

Any java project which satisfy the standard java project scaffolding defined by maven could be analysed to identify any dead code available in the project. 


The project uses [Sci Tool Understand](https://scitools.com/) for its code analysis task.


You can can use four APIs published by this application through(http://localhost:8080/swagger-ui.html) assuming the deployied host and port is localhost:8080.

The swagger definition will be available at the following URL:

[http://localhost:8080/api/v1/swagger.json](http://localhost:8080/api/v1/swagger.json)

Starting using the feature at Docker swam using following urls:

- [Swagger UI](http://webserver.devfactory.com:15560/swagger-ui.html)

- [Swagger definition](http://webserver.devfactory.com:15560/api/v1/swagger.json)

or by deploying the package from [github](https://github.com/lahiruk/dead-code-analyzer)

```
> git clone https://github.com/lahiruk/dead-code-analyzer.git
> gradlew clean bootRepackage
> java -Ddistribution='MacOS' -DtmpPath='/Users/<username>/tmp' -jar Development/workspace/dead-code-analyzer/build/libs/dead-code-analyzer-1.0.0.jar
```

At present the software is only certified to work on Linux and MacOS operating system flavors. Specify -Ddistribution='MacOS' for mac and nothing for linux. Specify -DtmpPath if the user.home is not root.

###Build
> gradlew clean bootRepackage

###Test
> gradle build or gradle test

###Docker Local
> gradlew buildDocker

###Docker Manual Push
> gradlew manualDockerPush
> export DOCKER_HOST="tcp://build.swarm.devfactory.com"
> cd Development/workspace/dead-code-analyzer/
> docker build  -t dead-code-image:latest build/docker