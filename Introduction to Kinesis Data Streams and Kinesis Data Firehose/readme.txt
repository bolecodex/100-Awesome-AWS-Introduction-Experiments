sudo wget 'https://github.com/aws-samples/aws-analytics-immersion-day/archive/refs/heads/main.zip'

sudo unzip -u main.zip

sudo yum -y install python-pip

sudo pip install csvkit

sudo chmod +x ./aws-analytics-immersion-day-main/set-up-hands-on-lab.sh

sudo ./aws-analytics-immersion-day-main/set-up-hands-on-lab.sh


wget 'https://archive.ics.uci.edu/ml/machine-learning-databases/00502/online_retail_II.xlsx'
in2csv online_retail_II.xlsx > online_retail.csv
head online_retail.csv > sample_online_retail.csv


# S3 Prefix
json-data/year=!{timestamp:yyyy}/month=!{timestamp:MM}/day=!{timestamp:dd}/hour=!{timestamp:HH}/

# S3 error prefix
error-json/year=!{timestamp:yyyy}/month=!{timestamp:MM}/day=!{timestamp:dd}/hour=!{timestamp:HH}/!{firehose:error-output-type}


python3 gen_kinesis_data.py -I resources/online_retail.csv \
 --region-name us-west-2 \
 --service-name kinesis \
 --out-format json \
 --stream-name retail-trans
