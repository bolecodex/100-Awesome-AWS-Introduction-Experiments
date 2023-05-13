#!/bin/sh 
yum -y update
amazon-linux-extras install epel -y
yum install stress -y
stress -c 1 --backoff 300000000 -t 30m


Level 100: Monitoring an Amazon Linux EC2 instance with CloudWatch Dashboards :: AWS Well-Architected Labs
https://wellarchitectedlabs.com/performance-efficiency/100_labs/100_monitoring_linux_ec2_cloudwatch/