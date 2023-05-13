aws dynamodb create-table \
    --table-name BarkTable \
    --attribute-definitions AttributeName=Username,AttributeType=S AttributeName=Timestamp,AttributeType=S \
    --key-schema AttributeName=Username,KeyType=HASH  AttributeName=Timestamp,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --stream-specification StreamEnabled=true,StreamViewType=NEW_AND_OLD_IMAGES

export accountID=$(aws sts get-caller-identity --query Account --output text)
export region=us-east-1

aws iam create-role --role-name WooferLambdaRole \
    --path "/service-role/" \
    --assume-role-policy-document file://trust-relationship.json

aws iam put-role-policy --role-name WooferLambdaRole \
    --policy-name WooferLambdaRolePolicy \
    --policy-document file://role-policy.json

aws sns create-topic --name wooferTopic

aws sns subscribe \
    --topic-arn arn:aws:sns:$region:$accountID:wooferTopic \
    --protocol email \
    --notification-endpoint example@example.com

aws lambda create-function \
    --region $region \
    --function-name publishNewBark \
    --zip-file fileb://publishNewBark.zip \
    --role arn:aws:iam::$accountID:role/service-role/WooferLambdaRole \
    --handler publishNewBark.handler \
    --timeout 5 \
    --runtime nodejs16.x

aws lambda invoke --function-name publishNewBark --payload file://payload.json --cli-binary-format raw-in-base64-out output.txt

aws dynamodb describe-table --table-name BarkTable

aws lambda create-event-source-mapping \
    --region $region \
    --function-name publishNewBark \
    --event-source arn:aws:dynamodb:$region:$accountID:table/BarkTable/stream/2023-04-28T22:59:50.320  \
    --batch-size 1 \
    --starting-position TRIM_HORIZON

aws dynamodb put-item \
    --table-name BarkTable \
    --item Username={S="Jane Doe"},Timestamp={S="2016-11-18:14:32:17"},Message={S="Testing...1...2...3"}