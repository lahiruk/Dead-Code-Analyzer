## Dead code analyser

Any java project which satisfy the standard java project scaffolding defined by maven could be analysed to identify any dead code available in the project. 


The project uses [Sci Tool Understand](https://scitools.com/) for its code analysis task:


You can can use four APIs published by this application through(http://localhost:8080/swagger-ui.html) assuming the deployied host and port is localhost:8080.

The swagger definition will be available at the following URI:

[http://localhost:8080/api/v1/swagger.json](http://localhost:8080/api/v1/swagger.json)

Starting using the feature at Docker swam sing following urls:

- [Swagger UI](http://webserver.devfactory.com:15560/swagger-ui.html)

- [Swagger definition](http://webserver.devfactory.com:15560/api/v1/swagger.json)

or by deploying the package from [github](https://github.com/lahiruk/dead-code-analyzer)

> 'git https://github.com/lahiruk/dead-code-analyzer.git'
> gradlew clean bootRepackage
> java -Ddistribution='MacOS' -DtmpPath='/Users/lahiru/tmp' -jar Development/workspace/dead-code-analyzer/build/libs/dead-code-analyzer-1.0.0.jar

At present the software is only certified to work on Linux and MacOS operating system flavors. Specify -Ddistribution='MacOS' for mac and nothing for linux. Specify -DtmpPath if the user.home is not root.