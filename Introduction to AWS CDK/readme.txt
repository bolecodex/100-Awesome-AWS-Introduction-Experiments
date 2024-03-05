Your first AWS CDK app - AWS Cloud Development Kit (AWS CDK) v2
https://docs.aws.amazon.com/cdk/v2/guide/hello_world.html

mkdir hello-cdk
cd hello-cdk
cdk init app --language python
source .venv/bin/activate
python -m pip install -r requirements.txt

cdk ls
cdk synth
cdk bootstrap
cdk deploy
cdk destroy
