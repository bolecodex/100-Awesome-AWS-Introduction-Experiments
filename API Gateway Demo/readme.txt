# Lambda Proxy
curl -v -X POST \
  'https://wf1nkzexj9.execute-api.eu-north-1.amazonaws.com/test/helloworld?name=John&city=Seattle' \
  -H 'content-type: application/json' \
  -H 'day: Thursday' \
  -d '{ "time": "evening" }'

# No Proxy
curl -v -X POST \
  'https://18a0wvc556.execute-api.us-east-1.amazonaws.com/test/city?time=evening' \
  -H 'content-type: application/json' \
  -H 'day: Thursday' \
  -H 'x-amz-docs-region: us-east-1' \
  -d '{
    "callerName": "John"
}'