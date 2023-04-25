aws ecr create-repository --repository-name hello-world --image-scanning-configuration scanOnPush=true --image-tag-mutability MUTABLE

aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 071908484098.dkr.ecr.us-east-1.amazonaws.com

docker build -t hello-world .

docker tag hello-world:latest 071908484098.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest

docker push 071908484098.dkr.ecr.us-east-1.amazonaws.com/hello-world:latest
