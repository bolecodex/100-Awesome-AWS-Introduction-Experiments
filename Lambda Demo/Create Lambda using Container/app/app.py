import json

def handler(event, context):
    body = {
        "message": "You reached to the container runtime"
    }
       
    response = {
        "statusCode": 200,
        "body": json.dumps(body)
    }
    return response