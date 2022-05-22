#!/bin/sh 
yum -y update
amazon-linux-extras install epel -y
yum install stress -y
stress -c 1 --backoff 300000000 -t 30m