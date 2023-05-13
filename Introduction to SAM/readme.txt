sam init
# Select AWS Quick Start Templates, Hello World Example, Python
# Change template Runtime to python3.7
cd sam-app
sam build
sam local invoke
sam local start-api
curl http://127.0.0.1:3000/hello
sam deploy --guided
sam delete



# https://serverless.kpingfan.com/03.sam/02.hello-world/
