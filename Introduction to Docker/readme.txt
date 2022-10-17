git clone https://github.com/bemer/containers-on-aws-workshop.git

cd containers-on-aws-workshop/00-Application/

docker build -t containers-workshop-app .

docker run -d -p 8080:80 containers-workshop-app

docker ps


# Docker provides a single command that will clean up any resources — images, containers, volumes, and networks — that are dangling (not tagged or associated with a container):

docker system prune

# To additionally remove any stopped containers and all unused images (not just dangling images), add the -a flag to the command:

docker system prune -a

How To Remove Docker Images, Containers, and Volumes | DigitalOcean
https://www.digitalocean.com/community/tutorials/how-to-remove-docker-images-containers-and-volumes


#To list any process listening to the port 8080:
lsof -i:8080

#To kill any process listening to the port 8080:
kill $(lsof -t -i:8080)

#or more violently:
kill -9 $(lsof -t -i:8080)



bemer/containers-on-aws-workshop
https://github.com/bemer/containers-on-aws-workshop
如何使用 Amazon Elastic Container Service、Docker 和 Amazon EC2 在 120 分钟内将整体式应用程序拆分为微服务 | AWS
https://aws.amazon.com/cn/getting-started/hands-on/break-monolith-app-microservices-ecs-docker-ec2/
AWS Modernization with Docker :: AWS Modernization with Docker
https://docker.awsworkshop.io/