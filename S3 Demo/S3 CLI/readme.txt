aws configure get region
aws s3 ls
aws s3 mb s3://lab-bucket --region us-east-1
aws s3api list-buckets --query 'Buckets[].Name'
aws s3api head-bucket --bucket lab-bucket
aws s3api get-bucket-location --bucket lab-bucket
aws s3api list-objects --bucket lab-bucket
aws s3api head-object --bucket lab-bucket --key index.html



aws s3 presign s3://lab-bucket/readme.txt --expires-in 3600 
aws s3 cp ./aFile.txt s3://lab-bucket/docs/
aws s3 sync s3://lab-bucket s3://other-bucket --exclude "*another/*"
aws s3 website s3://notes-bucket/ --index-document index.html --error-document error.html 
