[![Build Status](https://travis-ci.org/moley/spica.svg?branch=master)](https://travis-ci.org/moley/spica)
[![CodeCoverage](https://codecov.io/gh/moley/spica/branch/master/graph/badge.svg)](https://codecov.io/gh/moley/spica)

# About spica


## What is spica
Spica is an extensible, **project-oriented** development and communication platform. 
We set our focus on the efficency of the development process. The **use cases** are:

- TODO 

# Development

Client im Browser starten: 		    ```ionic cordova run browser```                                           
Client in Lab starten:			     ```ionic serve -l```                                                      
Client auf Smartphone deployen:	 	```ionic cordova run android --device --prod```                             
Server starten:                      ```gradle bootRun```                                                       
DockerImage erzeugen:                TODO ```docker build -t spica-server . -f Dockerfile.server```   
Docker Container starten:            ```docker run -p 8765:8765 -it spica-server```   
InMemory-H2-Console-URL:             ```http://localhost:8765/h2```   
InMemory-H2-URL:					 ```jdbc:h2:mem:testdb```    

Supported JDK: 12




