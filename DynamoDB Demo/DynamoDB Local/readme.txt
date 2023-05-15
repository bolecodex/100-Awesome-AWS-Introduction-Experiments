# Use executable .jar file
mkdir dynamodb-local
cd dynamodb-local/
wget https://s3.eu-central-1.amazonaws.com/dynamodb-local-frankfurt/dynamodb_local_latest.zip
unzip dynamodb_local_latest.zip
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb

# Use Docker
docker-compose up


# add "--color off" at the end of command if  output format broken in Windows 
aws dynamodb list-tables --endpoint-url http://localhost:8000

SELEC FROM Music WHERE Artist=? and SongTitle=?

SELECT * FROM "Music" WHERE "Artist" = 'Acme Band' AND "SongTitle" = 'PartiQL Rocks'



Deploying DynamoDB locally on your computer - Amazon DynamoDB
https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html