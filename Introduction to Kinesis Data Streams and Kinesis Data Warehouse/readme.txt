sudo wget 'https://github.com/aws-samples/aws-analytics-immersion-day/archive/refs/heads/main.zip'

sudo unzip -u main.zip

sudo chmod +x ./aws-analytics-immersion-day-main/set-up-hands-on-lab.sh

sudo ./aws-analytics-immersion-day-main/set-up-hands-on-lab.sh

# S3 Prefix
json-data/year=!{timestamp:yyyy}/month=!{timestamp:MM}/day=!{timestamp:dd}/hour=!{timestamp:HH}/

# S3 error prefix
error-json/year=!{timestamp:yyyy}/month=!{timestamp:MM}/day=!{timestamp:dd}/hour=!{timestamp:HH}/!{firehose:error-output-type}


python3 gen_kinesis_data.py -I online_retail.csv \
 --region-name us-west-2 \
 --service-name kinesis \
 --out-format json \
 --stream-name retail-trans
