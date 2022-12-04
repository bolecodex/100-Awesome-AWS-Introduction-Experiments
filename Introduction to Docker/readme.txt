docker -v
docker pull nginx
docker run -d -p 8080:80 --name nginx nginx
docker ps
docker logs nginx
docker exec -it nginx /bin/bash
cd /usr/share/nginx/html
cat index.html
exit

docker stop nginx
docker ps -a
docker rm nginx
docker rmi nginx

mkdir demo
cd demo

# Dockerfile
FROM nginx
COPY index.html /usr/share/nginx/html

docker build -t nginx2.0 .
docker history nginx2.0
docker images
docker run -d -p 8080:80 --name nginx nginx2.0
docker inspect nginx
docker ps
docker stop nginx
docker rm nginx

docker run -d -p 8080:80 -v /home/ec2-user/environment/demo/index.html:/usr/share/nginx/html/index.html:ro --name nginx nginx2.0

-----------------------------------------------------------
cd ..
mkdir demo2
cd demo2

echo 'from flask import Flask

app = Flask(__name__)

@app.route("/")
def hello():
    return "hello world!"

if __name__ == "__main__":
    app.run(host="0.0.0.0")' > app.py

python --version
pip install flask
python app.py

touch Dockerfile

FROM python:3.8-alpine
RUN pip install flask
COPY app.py /app.py
CMD ["python", "app.py"]

docker build -t python-hello-world .
docker run -d -p 8080:5000 python-hello-world

export DOCKERHUB_USERNAME=bolecodex
docker login docker.io -u $DOCKERHUB_USERNAME
docker tag python-hello-world $DOCKERHUB_USERNAME/python-hello-world
docker push $DOCKERHUB_USERNAME/python-hello-world

----------------------------------------------------
docker pull alexwhen/docker-2048
docker run -d -p 8080:80 --name game-2048 alexwhen/docker-2048

docker pull bharathshetty4/supermario
docker run -d -p 8080:8080 --name supermario bharathshetty4/supermario

--------------------------------------------------------------


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