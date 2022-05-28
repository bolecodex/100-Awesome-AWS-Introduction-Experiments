arn:aws:dynamodb:us-east-1:071908484098:table/GameTopX


https://pq1a9agfra.execute-api.us-east-1.amazonaws.com/prod/topxstatistics?sessionId=TheTestSession

API 密钥
bXMYw0Ja8n8jymx89RtNQ60KNGYPrtkH2OKVtjQd

curl --verbose --header "x-api-key: bXMYw0Ja8n8jymx89RtNQ60KNGYPrtkH2OKVtjQd" https://pq1a9agfra.execute-api.us-east-1.amazonaws.com/prod/topxstatistics?sessionId=TheTestSession


#set($inputRoot = $input.path('$'))
## The next line changes the HTTP response code with the one provided by the Lambda Function
#set($context.responseOverride.status = $inputRoot.statusCode)
## Decoding base64 (this could have been left to the application)
#if( $inputRoot.isBase64Encoded == true )
$util.base64Decode($inputRoot.body)
#else
$inputRoot.body
#end