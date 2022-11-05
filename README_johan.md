# Steps to start the project
- You downloaded this project. 

# Creating a startup icon for Docker MQ
- Copy a short-link on your desktop. 
  - Tab: snelkoppeling
  - Path: K:\A__IntelliJ_projects\messaging-microservices-rabbitmq\docker\DockerRabbitMq.bat
  - Execute in:  K:\A__IntelliJ_projects\messaging-microservices-rabbitmq\docker
- All startup stuff is in the 'docker' subfolder
- Double click on the desktop quick link. After a while the RabbitMQ is running 
as a Docker container. 

# Startup the demo
- Set JDK via 'project settings' on JDK 11. 
- Start the applications
  - Consumer: Maven clean install ... and start the Application (right click)
  - Producer: Maven clean install ... and start the Application (right click)
- Start the console: browse to localhost:15672 and login with guest/guest
- Send a message via Postman: 
  - Url: http://localhost:8081/api/produce
  - Type: POST
  - Body: raw > json > { "userId":"ID1", "userName":"johan" }
- Je ziet in RabbitMQ console dat er een bericht gestuurd is

# Dynamic queues
- Start only the Producer (message sub project)
- Postman: http://localhost:8081/api/addqueue
  - Type: POST
  - Body: raw > json > { "queueName":"dynaqueue1", "bindingKey":"abo1" }
- Postman: http://localhost:8081/api/produceforqueue
  - Type: POST
  - Body: raw > json > { "routingKey":"abo1", "message": { "userId":"ID1", "userName":"johan" } }
