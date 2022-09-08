curl -v -X "PUT" -H "Content-Type: application/json" -d "{\"id\": \"123\", \"price\": 12345, \"name\": \"myitem\"}" https://abcdef123.execute-api.us-west-2.amazonaws.com/items
curl -v https://abcdef123.execute-api.us-west-2.amazonaws.com/items
curl -v https://abcdef123.execute-api.us-west-2.amazonaws.com/items/123
curl -v -X "DELETE" https://abcdef123.execute-api.us-west-2.amazonaws.com/items/123
curl -v https://abcdef123.execute-api.us-west-2.amazonaws.com/items