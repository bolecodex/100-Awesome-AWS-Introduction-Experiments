# Create EFS Security Group with allow all NFS traffic and then replace EFS Security Group in EFS Network tab
Mounting on Amazon EC2 Linux instances using the EFS mount helper - Amazon Elastic File System
https://docs.aws.amazon.com/efs/latest/ug/mounting-fs-mount-helper-ec2-linux.html
Create and mount an Amazon EFS file system to an Amazon EC2 instance
https://aws.amazon.com/getting-started/hands-on/create-mount-amazon-efs-file-system-on-amazon-ec2-using-launch-wizard/?nc1=h_ls

# Enter EC2 Linux 2 Instance, create mount folder
sudo mkdir efs

# Install package
sudo yum install amazon-efs-utils

# Use mount command 1
sudo mount -t efs -o tls fs-0b595a9f27a2935ca:/ efs

# Unmount file system
sudo umount efs
