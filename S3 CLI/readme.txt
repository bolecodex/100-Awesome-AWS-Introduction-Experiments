aws configure get region
aws s3 ls
aws s3 mb s3://lab-bucket --region us-east-1
aws s3api list-buckets --query 'Buckets[].Name'
aws s3api get-bucket-location --bucket lab-bucket


