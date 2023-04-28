# Lambda Proxy
curl -v -X POST \
  'https://9x78843vy7.execute-api.us-east-1.amazonaws.com/test/helloworld?name=John&city=Seattle' \
  -H 'content-type: application/json' \
  -H 'day: Thursday' \
  -d '{ "time": "evening" }'

# No Proxy
curl -v -X POST \
  'https://h5cuty8bld.execute-api.us-east-1.amazonaws.com/test/Berlin?time=evening' \
  -H 'content-type: application/json' \
  -H 'day: Thursday' \
  -H 'x-amz-docs-region: us-east-1' \
  -d '{
    "callerName": "John"
}'