# About smartcmo

## What is smartcom
Smartcom is an extensible, **project-oriented** communication platform. 
We want to cover following aspects always in the focus of a project:

* **Messaging** (project or topicrelated chat as well as private chats, 1:1 or group chats)
* **Exchanging and accessing information** (documents, knowledge, surveys)
* **Making support cases as easy as possible** (SOS function...)
* **Making project related work easier** (e.g for developers the setup of their environment)

## Who we target
Smartcom can be used e.g by:

* **Companies** (e.g. insurance brokers) to handle and ease communication with customers
* **Schools** to handle knowledge transfer, communication between school and pupils and their parents
* **Developers** to collaborate in a project and to easily handle project related infos and share the state of the project

## What are the key features
* **Extensibility** You can easily contribute plugins for both server and client
* **Runtime flexibility** You choose were the backend is hosted, in a cloud datacenter or on-premise
* **Multi platform** Smartcom provides a mobile client as well as a browser app and (for developers) a commandline interface to interact with the platform

#Development

Client im Browser starten: 		    ```ionic cordova run browser```                                           
Client in Lab starten:			     ```ionic serve -l```                                                      
Client auf Smartphone deployen:	 	```ionic cordova run android --device --prod```                             
Server starten:                      ```gradle bootRun```                                                       
DockerImage erzeugen:                TODO ```docker build -t maodialog-server . -f Dockerfile.server```   
Docker Container starten:            ```docker run -p 8080:8080 -it maodialog-server```   
InMemory-H2-Console-URL:             ```http://localhost:8080/h2```   
InMemory-H2-URL:					 ```jdbc:h2:mem:testdb```    





