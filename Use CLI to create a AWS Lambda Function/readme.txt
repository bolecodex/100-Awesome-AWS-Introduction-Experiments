aws iam create-role --role-name lambda-role --assume-role-policy-document file://role.json

aws lambda create-function --function-name hello-world --zip-file fileb://function.zip --runtime nodejs8.10 --role arn:aws:iam::<account-id>:role/lambda-role --handler index.handler

cat outputfile.txt
aws lambda invoke --function-name hello-world outputfile.txt