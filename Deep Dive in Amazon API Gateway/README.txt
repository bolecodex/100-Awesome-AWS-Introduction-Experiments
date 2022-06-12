# Deploy cloudformation template
aws cloudformation deploy --template-file path-to-template-file/packaged-template.yaml --stack-name serverless-immersion-day-stack --capabilities CAPABILITY_IAM

# JSON Sample in the Request Body section
{
  "price": "400000",
  "size": "1600",
  "unit": "sqFt",
  "downPayment": "20"
} 

# Remove quoted Request Body
{
  "price": 400000,
  "size": 1600,
  "unit": "sqFt",
  "downPayment": 20
}

# Message Transformation Model
Model name: costCalculatorRequest
ContentType: application/json
Model Description: CostCalculator incoming request schema
Model Schema:
{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "title": "costCalculatorRequestModel",
  "type": "object",
  "properties": {
                  "price": { "type": "number" },
                  "size": { "type": "number" },
                  "unit": { "type": "string"}, 
                  "downPayment": { "type": "number"} 
  }
}

# Transform Request Payload VTL template
#set($inputRoot = $input.path("$"))
{
  "price": "$inputRoot.price",
  "size": "$inputRoot.size",
  "unit": "$inputRoot.unit",
  "downPaymentAmount": $inputRoot.downPayment
}


aws cognito-idp admin-initiate-auth --user-pool-id <YOUR_POOL_ID> --client-id <YOUR_CLIENT_ID> --auth-flow ADMIN_NO_SRP_AUTH --auth-parameters 'USERNAME=testUser,PASSWORD="testUser123!"'

aws cognito-idp admin-respond-to-auth-challenge --user-pool-id <YOUR_POOL_ID>  --client-id <YOUR_CLIENT_ID> --challenge-name NEW_PASSWORD_REQUIRED --challenge-response "USERNAME=testUser,NEW_PASSWORD=<FINAL_PASSWORD>" --session "<SESSION_VALUE_FROM_PREVIOUS_RESPONSE>"

# Update API Gateway Authentication
Name: CostCalculatorUserPool
Type: Cognito
Cognito Region: us-east-1
Cognito user Pool: CostCalculatorUserPool
Token source: method.request.header.Authorization

aws cognito-idp admin-initiate-auth --user-pool-id <YOUR_POOL_ID> --client-id <YOUR_CLIENT_ID> --auth-flow ADMIN_NO_SRP_AUTH --auth-parameters 'USERNAME=testUser,PASSWORD="<FINAL_PASSWORD>"'


# Message Caching "application/json" template
{
    "region": "$input.params("region")"
}
