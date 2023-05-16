import boto3

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table('serverless_workshop_intro')

def lambda_handler(event, context):
  data = table.scan()
  return data['Items']
