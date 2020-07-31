[![Build Status](https://travis-ci.org/moley/spica.svg?branch=master)](https://travis-ci.org/moley/spica)
[![CodeCoverage](https://codecov.io/gh/moley/spica/branch/master/graph/badge.svg)](https://codecov.io/gh/moley/spica)

# About spica


## What is spica
Spica is an extensible, **project-oriented** development and communication platform. 
We set our focus on the efficency of the development process. The **use cases** are:

* Handle todos and tasks  
* Improve communication
* Improve navigation to external systems (jenkins, stash, bitbucket...)
* Improve booking of tasks 
* Ease working with multi module projects
* Automate routine tasks 


# Development

##Build everything
Calling `./gradlew build` creates: 
- runnable fxclient (see below), 
- runnable cli-client (see below), 
- startable server (see below)
- documentation in `spica-docs/build/docs

##Publish everything
First you have to add releasenodes in releasenotes.md. 
Afterwards call `./gradlew publish` to release the following artifacts: 
- platform dependend fx client
- platform dependend cli client
- server as docker container on https://hub.docker.com/repository/docker/markusoley/spica

##Start the spica server locally:         
`./gradlew :spica-server:bootRun`

##Create and upload a new docker image:   
`./gradlew :spica-server:dockerPushImage `
                                                     
##Working with the h2 database
- InMemory-H2-Console-URL: `http://localhost:8765/h2` 
- InMemory-H2-URL:					    `jdbc:h2:mem:testdb`

##Start the cli client locally           
- `./gradlew :spica-cli:installDist`
- start from *spica/spica-cli/build/install/spica-cli/bin/spica-cli* (you can define an alias to this program)

##Start the fx client locally           
- `./gradlew :spica-fxclient:installDist`
- start from *spica/spica-cli/build/install/spica-fxclient/bin/spica-fxclient* (you can define an alias to this program)

##Create demo data 
- `./gradlew :spica-testutils:demoData`
                                   






