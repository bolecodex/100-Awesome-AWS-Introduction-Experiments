import boto3
import uuid

def lambda_handler(event, context):
    table_name = 'serverless_workshop_intro'
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table(table_name)

    result = None
    people = [
            { 'userid' : 'marivera', 'name' : 'Martha Rivera'},
            { 'userid' : 'nikkwolf', 'name' : 'Nikki Wolf'},
            { 'userid' : 'pasantos', 'name' : 'Paulo Santos'},
        ]

    with table.batch_writer() as batch_writer:
        for person in people:
            item = {
                '_id'     : uuid.uuid4().hex,
                'Userid'  : person['userid'],
                'FullName': person['name']
            }
            print("> batch writing: {}".format(person['userid']) )
            batch_writer.put_item(Item=item)
            
        result = f"Success. Added {len(people)} people to {table_name}."

    return {'message': result}
