# Export and Import Lambda Cross-Region Functions

aws iam create-role --role-name lambda-execution --assume-role-policy-document '{"Version": "2012-10-17","Statement": [{ "Effect": "Allow", "Principal": {"Service": "lambda.amazonaws.com"}, "Action": "sts:AssumeRole"}]}'

aws iam attach-role-policy --role-name lambda-execution --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole

echo "exports.handler = async (event) => {
    console.log('calling lambda function...', event);
    const response = {
        'statusCode': 200,
        'body': JSON.stringify('Testing the import and export of Lambda functions!'),
    };
    return response;
};" > index.js

zip my-function index.js

export ACC_ID=$(aws sts get-caller-identity --query Account --output text)
aws lambda create-function --function-name my-function --zip-file fileb://my-function.zip --handler index.handler --runtime nodejs14.x --role arn:aws:iam::$ACC_ID:role/lambda-execution

aws lambda publish-version --function-name my-function

aws lambda create-alias --function-name my-function --name Prod --function-version 1 --description " "

aws lambda invoke --function-name my-function --payload $(echo '{ "key_sample": "value_sample" }' | base64) --log-type Tail --query 'LogResult' --output text out | base64 -d

aws lambda update-function-configuration --function-name my-function --memory-size 256

aws lambda list-functions --region us-east-1

aws lambda get-function --function-name my-function --query 'Code.Location' | xargs wget -O my-function-exp.zip

aws lambda create-function --function-name my-function --zip-file fileb://my-function.zip --handler index.handler --runtime nodejs14.x --role arn:aws:iam::$ACC_ID:role/lambda-execution --region us-east-2 

aws lambda update-function-code --function-name my-function --region us-east-2 --zip-file fileb://my-function-exp.zip

aws lambda invoke --function-name my-function --payload $(echo '{ "key_sample": "value_sample" }' | base64) --log-type Tail --query 'LogResult' --region us-east-2 --output text out | base64 -d

aws lambda delete-function --function-name my-function --region us-east-1
aws lambda delete-function --function-name my-function --region us-east-2
