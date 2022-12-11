# Boosting RDS database performance with Amazon ElastiCache for Redis

sudo yum install git -y
sudo yum install mysql -y
sudo yum install python3 -y
pip3 install --user virtualenv
git clone https://github.com/aws-samples/amazon-elasticache-samples/
cd amazon-elasticache-samples/database-caching
virtualenv venv
source ./venv/bin/activate
pip3 install -r requirements.txt


Master username
admin
Master password
Password!

mysql -h database-1.cjsff9e3cpj2.us-east-1.rds.amazonaws.com -P 3306 -u admin -p < seed.sql


import redis
client = redis.Redis.from_url('redis://redis-cluster-001.ovj7jw.0001.use1.cache.amazonaws.com:6379')
client.ping()

export REDIS_URL=redis://redis-cluster-001.ovj7jw.0001.use1.cache.amazonaws.com:6379/
export DB_HOST=database-1.cjsff9e3cpj2.us-east-1.rds.amazonaws.com
export DB_USER=admin
export DB_PASS=Password!
export DB_NAME=tutorial



# If the database is PostgreSQL, use the following commands
sudo amazon-linux-extras install postgresql10
psql --username=postgre -h database-1-instance-1.cjsff9e3cpj2.us-east-1.rds.amazonaws.com postgres