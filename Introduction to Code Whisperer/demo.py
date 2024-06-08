# 1.) Function to get a file from url

import json
import boto3
import requests


def get_file(url):
    """
    Function to get a file from url
    """
    response = requests.get(url)
    if response.status_code == 200:
        return response.content
    else:
        return None


# 2.) #Function to upload a file to S3 using server side encryption


def upload_to_s3(file, bucket_name, key):
    """
    Function to upload to s3
    """
    s3 = boto3.resource('s3')
    s3.Bucket(bucket_name).put_object(Key=key, Body=file)
    return "File uploaded successfully"

# lamda function to call above functions


def lambda_handler(event, context):
    """
    Function to call above functions
    """
    url = event['url']
    bucket_name = event['bucket_name']
    key = event['key']
    file = get_file(url)
    if file:
        return upload_to_s3(file, bucket_name, key)
    else:
        return "File not found"


# Create an EC2 instance, with a tag of 'Name' set to 'MyInstance'


ec2 = boto3.resource('ec2')

instance = ec2.create_instances(
    ImageId='ami-0d7c6b6b8f3c8b5c0',
    MinCount=1,
    MaxCount=1,
    InstanceType='t2.micro',
    KeyName='ec2-keypair',
    SecurityGroups=['launch-wizard-1'],

    UserData=open('userdata.sh').read(),

    TagSpecifications=[
        UserData=open('user_data.sh', 'r').read(),
        {
            'ResourceType': 'instance',
            'Tags': [
                {

                    'Key': 'Name',
                    'Value': 'MyInstance'
                }
            ]
        }
    ]
)
# print(instance)


# Create a S3 bucket, with a tag of 'Name' set to 'MyBucket-bole'

s3 = boto3.resource('s3')

bucket = s3.create_bucket(
    Bucket='MyBucket-bole',
    CreateBucketConfiguration={

        'LocationConstraint': 'us-east-2'
    },

    Tagging={
        'TagSet': [
            {
                'Key': 'Name',
                'Value': 'MyBucket-bole'
            }
        ]
    }
)
# print(bucket)

