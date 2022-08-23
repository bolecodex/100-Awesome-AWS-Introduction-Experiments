# Check the security group of EFS Mount Target Point, all NFS protocol traffic access from the EC2 security group

# Enter EC2 Linux 2 Instance, create mount folder
sudo mkdir efs

# Install package
sudo yum install amazon-efs-utils

# Use mount command 1
sudo mount -t efs -o tls fs-0b595a9f27a2935ca:/ efs

#Or use mount command 2
sudo mount -t nfs4 -o nfsvers=4.1,rsize=1048576,wsize=1048576,hard,timeo=600,retrans=2,noresvport fs-0b595a9f27a2935ca.efs.us-east-1.amazonaws.com:/ efs

# check the mounted file system
mount -t nfs4

# Unmount file system
sudo umount efs