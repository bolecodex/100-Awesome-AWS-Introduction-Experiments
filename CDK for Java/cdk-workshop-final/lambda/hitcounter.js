const { DynamoDB, Lambda } = require('aws-sdk');

//Using async/await - AWS SDK for JavaScript
// https://docs.aws.amazon.com/sdk-for-javascript/v2/developer-guide/using-async-await.html
exports.handler = async function(event) {
  console.log("request:", JSON.stringify(event, undefined, 2));

  // create AWS SDK clients
  const dynamo = new DynamoDB();
  const lambda = new Lambda();

  // update dynamo entry for "path" with hits++
  await dynamo.updateItem({
    TableName: process.env.HITS_TABLE_NAME,
    Key: { path: { S: event.path } },
    UpdateExpression: 'ADD hits :incr',
    ExpressionAttributeValues: { ':incr': { N: '1' } }
  }).promise();
  // The AWS.Request.promise method provides a way to call a service operation and manage asynchronous flow instead of using callbacks
  // https://docs.aws.amazon.com/sdk-for-javascript/v2/developer-guide/using-promises.html

  // call downstream function and capture response
  const resp = await lambda.invoke({
    FunctionName: process.env.DOWNSTREAM_FUNCTION_NAME,
    Payload: JSON.stringify(event)
  }).promise();

  console.log('downstream response:', JSON.stringify(resp, undefined, 2));

  // return response back to upstream caller
  return JSON.parse(resp.Payload);
};
