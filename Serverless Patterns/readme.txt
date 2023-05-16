# Change the password for the user in the Cognito user pool 
aws cognito-idp admin-initiate-auth --user-pool-id <YOUR_POOL_ID> --client-id <YOUR_CLIENT_ID> --auth-flow ADMIN_NO_SRP_AUTH --auth-parameters 'USERNAME=testUser,PASSWORD="testUser123!"'

# Use the CLI to set the final password for the user. The session value can be retrieved from the previous response
aws cognito-idp admin-respond-to-auth-challenge --user-pool-id <YOUR_POOL_ID>  --client-id <YOUR_CLIENT_ID> --challenge-name NEW_PASSWORD_REQUIRED --challenge-responses "USERNAME=testUser,NEW_PASSWORD=<FINAL_PASSWORD>" --session "<SESSION_VALUE_FROM_PREVIOUS_RESPONSE>"

# If needed, you can get a new token for the user
aws cognito-idp admin-initiate-auth --user-pool-id <YOUR_POOL_ID> --client-id <YOUR_CLIENT_ID> --auth-flow ADMIN_NO_SRP_AUTH --auth-parameters 'USERNAME=testUser,PASSWORD="<FINAL_PASSWORD>"'




Serverless Patterns
https://catalog.workshops.aws/serverless-patterns/en-US/module1
The Amazon API Gateway Workshop
https://catalog.workshops.aws/apigateway/en-US/module-1/step-6