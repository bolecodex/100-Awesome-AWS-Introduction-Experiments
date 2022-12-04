# Lab steps:
1. Create Aurora database
2. Change Security Group and allow traffic to MYSQL/Aurora port 3306 
3. Modify DB instance and allow public access in Connectivity settings
4. Access the database through MySQL Workbench

mysql -h database-1.cjsff9e3cpj2.us-east-1.rds.amazonaws.com -P 3306 -u admin -p
show databases;
use mysql;
show tables;
