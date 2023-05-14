sudo stress --cpu 8 --vm-bytes $(awk '/MemAvailable/{printf "%d\n", $2 * 0.9;}' < /proc/meminfo)k --vm-keep -m 1


Level 100: Monitoring an Amazon Linux EC2 instance with CloudWatch Dashboards :: AWS Well-Architected Labs
https://wellarchitectedlabs.com/performance-efficiency/100_labs/100_monitoring_linux_ec2_cloudwatch/