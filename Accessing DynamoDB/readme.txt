mkdir dynamodb-local
cd dynamodb-local/
wget https://s3.eu-central-1.amazonaws.com/dynamodb-local-frankfurt/dynamodb_local_latest.zip
unzip dynamodb_local_latest.zip
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb

# add "--color off" at the end of command if  output format broken in Windows 
aws dynamodb list-tables --endpoint-url http://localhost:8000

SELECT *                                         
FROM Music  
WHERE Artist=? and SongTitle=?

aws dynamodb put-item --table-name Songs --item '{"Id":{"S":"3"},"Metadata":{"S":"Test"},"Title":{"S":"HelloWorld!"}}’