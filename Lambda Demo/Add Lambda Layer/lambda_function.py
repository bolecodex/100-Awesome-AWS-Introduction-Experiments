import boto3
import json

client = boto3.resource('dynamodb')
    
def lambda_handler(event, context):
    
    book_item = event["body"]
    error = None
    try:
        table = client.Table('Books')
        table.put_item(Item = book_item)
    except Exception as e:
        error = e
        
    if error is None:
        response = {
            'statusCode': 200,
            'body': 'writing to dynamoDB successfully!',
            'headers': {
                'Content-Type': 'application/json'
            },
        }
    else:
        response = {
            'statusCode': 400,
            'body': 'writing to dynamoDB fail!',
            'headers': {
                'Content-Type': 'application/json'
            },
        }
  
    return response