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

Start the spica server locally:         ```./gradlew :spica-server:bootRun``` 

Create and upload a new docker image:   ```./gradlew :spica-server:dockerPushImage ```
                                                     
InMemory-H2-Console-URL:                ```http://localhost:8765/h2```
   
InMemory-H2-URL:					    ```jdbc:h2:mem:testdb```    






