#!/bin/bash
sudo yum update -y
sudo yum -y remove httpd
sudo yum -y remove httpd-tools
sudo yum install -y httpd24 php72 mysql57-server php72-mysqlnd
service httpd start
chkconfig httpd on

sudo usermod -a -G apache ec2-user
sudo chown -R ec2-user:apache /var/www
sudo chmod 2775 /var/www
sudo find /var/www -type d -exec chmod 2775 {} \;
sudo find /var/www -type f -exec chmod 0664 {} \;
cd /var/www/html
curl https://raw.githubusercontent.com/hashicorp/learn-terramino/master/index.php -O





# amzn-ami-hvm-2018.03.0.20210721.0-x86_64-ebs
# ami-0a3335277de246f64