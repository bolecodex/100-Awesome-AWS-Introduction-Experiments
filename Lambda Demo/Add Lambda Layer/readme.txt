# Package the latest boto3 library and upload it as a Lambda layer.
# In the Cloud9 terminal and enter the following command in the terminal window
cd ~/environment/
mkdir -p layers/python

pip install boto3 -t layers/python

cd layers
zip -r my_boto3.zip python 

aws lambda publish-layer-version --layer-name my_boto3 --description "boto3 layer" --zip-file fileb://my_boto3.zip --compatible-runtimes python3.10

# Navigate to your Lambda function code and add the following line item to the code inside your Lambda handler.
print("current boto3 version: ", boto3.__version__)
