# index.js
exports.handler = async (event) => {
        // let's log the incoming event
        let payloadAsString = JSON.stringify(event);
        console.log(payloadAsString);
        try{
            // event contains Records, which is an array with a certain structure
            for(let i=0;i<event.Records.length;i++) {
                //let's get the Record
                let record = event.Records[i];
                // let's decode the base64-encoded payload that was sent
                let data = Buffer.from(record.kinesis.data,'base64').toString('utf-8');
                // let's show the data
                console.log(`Data received: ${data}`);
                // let's show the timestamp in which it was received (approximately)
                let receivedTst = new Date(record.kinesis.approximateArrivalTimestamp*1000);
                console.log(`Received tst: ${receivedTst}`);
            }
        } catch(e) {
            //let's handle the errors, if any
            console.log("Error:",e);
            const response = {
                statusCode : 200,
                body : e
            };
            return response;
        }
        // this is a successful processing
        const response = {
            statusCode: 200
        };
        return response;
    };


# Test
{
    "Records": [
        {
            "kinesis": {
                "kinesisSchemaVersion": "1.0",
                "partitionKey": "1",
                "sequenceNumber": "49590338271490256608559692538361571095921575989136588898",
                "data": "SGVsbG8sIHRoaXMgaXMgYSB0ZXN0Lg==",
                "approximateArrivalTimestamp": 1545084650.987
            },
            "eventSource": "aws:kinesis",
            "eventVersion": "1.0",
            "eventID": "shardId-000000000006:49590338271490256608559692538361571095921575989136588898",
            "eventName": "aws:kinesis:record",
            "invokeIdentityArn": "arn:aws:iam::123456789012:role/lambda-role",
            "awsRegion": "us-east-2",
            "eventSourceARN": "arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream"
        },
        {
            "kinesis": {
                "kinesisSchemaVersion": "1.0",
                "partitionKey": "1",
                "sequenceNumber": "49590338271490256608559692540925702759324208523137515618",
                "data": "VGhpcyBpcyBvbmx5IGEgdGVzdC4=",
                "approximateArrivalTimestamp": 1545084711.166
            },
            "eventSource": "aws:kinesis",
            "eventVersion": "1.0",
            "eventID": "shardId-000000000006:49590338271490256608559692540925702759324208523137515618",
            "eventName": "aws:kinesis:record",
            "invokeIdentityArn": "arn:aws:iam::123456789012:role/lambda-role",
            "awsRegion": "us-east-2",
            "eventSourceARN": "arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream"
        }
    ]
}

# Response Body
Response
{
  "statusCode": 200
}

Function Logs
START RequestId: f48324d4-495f-43a7-813a-6872d4af1114 Version: $LATEST
2018-12-17T22:11:52.227Z    f48324d4-495f-43a7-813a-6872d4af1114    INFO    {"Records":[{"kinesis":{"kinesisSchemaVersion":"1.0","partitionKey":"1","sequenceNumber":"49590338271490256608559692538361571095921575989136588898","data":"SGVsbG8sIHRoaXMgaXMgYSB0ZXN0Lg==","approximateArrivalTimestamp":1545084650.987},"eventSource":"aws:kinesis","eventVersion":"1.0","eventID":"shardId-000000000006:49590338271490256608559692538361571095921575989136588898","eventName":"aws:kinesis:record","invokeIdentityArn":"arn:aws:iam::123456789012:role/lambda-role","awsRegion":"us-east-2","eventSourceARN":"arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream"},{"kinesis":{"kinesisSchemaVersion":"1.0","partitionKey":"1","sequenceNumber":"49590338271490256608559692540925702759324208523137515618","data":"VGhpcyBpcyBvbmx5IGEgdGVzdC4=","approximateArrivalTimestamp":1545084711.166},"eventSource":"aws:kinesis","eventVersion":"1.0","eventID":"shardId-000000000006:49590338271490256608559692540925702759324208523137515618","eventName":"aws:kinesis:record","invokeIdentityArn":"arn:aws:iam::123456789012:role/lambda-role","awsRegion":"us-east-2","eventSourceARN":"arn:aws:kinesis:us-east-2:123456789012:stream/lambda-stream"}]}
2018-12-17T22:11:52.227Z    f48324d4-495f-43a7-813a-6872d4af1114    INFO    Data received: "Hello, this is a test."
2018-12-17T22:11:52.250Z    f48324d4-495f-43a7-813a-6872d4af1114    INFO    Received tst: Mon Dec 17 2018 22:10:50 GMT+0000 (Coordinated Universal Time)
2018-12-17T22:11:52.251Z    f48324d4-495f-43a7-813a-6872d4af1114    INFO    Data received: "This is only a test."
2018-12-17T22:11:52.251Z    f48324d4-495f-43a7-813a-6872d4af1114    INFO    Received tst: Mon Dec 17 2018 22:11:51 GMT+0000 (Coordinated Universal Time)
END RequestId: f48324d4-495f-43a7-813a-6872d4af1114
REPORT RequestId: f48324d4-495f-43a7-813a-6872d4af1114    Duration: 43.67 ms    Billed Duration: 44 ms    Memory Size: 128 MB    Max Memory Used: 67 MB    Init Duration: 176.31 ms

Request ID
f48324d4-495f-43a7-813a-6872d4af1114
