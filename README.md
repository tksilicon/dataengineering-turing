# Data Engineering Challenge (Producer)

Expectation - Obtain large amount of data and process based on given constraints. Sample file containing 100,000 github respositories were provided. Solution should include application that can run multiple nano/micro instances.

Solution - Microservices is appropriate for developing applications that can take adavantage of nano/micro instances. Given my proficiency in Spring Boot, it was used to develop the application. 

Architecture - The concept of producer/consumer was utilized. This module is the producer and it takes advantage of many data architect frameworks and libraries written in Java to develop the solution. 

Spring Boot - foremost microservices Java framework which can be used to develop production grade applications

Apache Spark - is a unified analytics engine for large-scale data processing. Reading a file of 100,000 lines is a walk in the park for the framework, so it serves as a means of loading the data, dividing it into batches and forward to the consumer for processing of application logic.

Apache Kafka - is one of the most efficient streaming frameworks which enables messaging and queuing across between microservices. 

This producer module loads the files into memory and has a controller endpoint which will be curl (ed) to trigger analysis. File can be uploaded too but for development purposes, there is a sample list taken from the 100,000 URLs csv file provided.

To run the application and process github respository sample provided Apache Kafta have to be installed on the system.  Alongside Zookeeper. Direct installation or docker. For Mac OS

Direct installation:

$ brew cask install java
$ brew install kafka

Start Zookeeper:
$ zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties

Start Kafka server:
$ kafka-server-start /usr/local/etc/kafka/server.properties

Docker

Johnny Park's docker image was used for testing

-- docker pull johnnypark/kafka-zookeeper

Then start the docker+zookeeper+kafka

docker run -p 2181:2181 -p 9092:9092 -e ADVERTISED_HOST=127.0.0.1  -e NUM_PARTITIONS=10 johnnypark/kafka-zookeeper

Clone the module to your computer

Producer
git clone https://github.com/tksilicon/dataengineering-turing.git
cd  dataengineering-turing

mvn install  
mvn spring-boot:run  

curl -v http://localhost:5000/api/getgithubanalysis

The above command will spurn the producer to send all repositories to proceess to the consumer. The processed file results.json is in root folder of consumer in dataset folder.

For testing running the application on localhost which is the default is preferred

Because this is heavy data processing, taking advantage of aws elastic beanstalk services, the application was deployed on beanstalk which has the capability to scale the application and create multiple instances and load balance them and share the workload.

To deploy on aws, with a user account. Locate beanstalk and upload the jar files or 

use ebs cli

eb init
eb create 

eb scale 5 

The above will deploy the modules. However, Apache Kafka is a paid service on aws so a free 30 day trial from CloudKafka service was used for testing. The CloudKafka service works while the appplication is working on local too. To run on CloudKafka service  comment the local configuration and uncomment CloudKafka. 


The Json outputs provides the deliverables
-Number of lines of code 
-List of external libraries/packages
-The Nesting factor for the repository
-Code duplication(not concluded)
-Average number of parameters 
-Average Number of variables

The solution leverages modern, recent and trending technologies to deliver
- Use of distributed systems, Efficiency, Accuracy, Optimisations, Comments as stipulated., Use of distributed system, Efficiency, Accuracy, Optimisations, Comments:


