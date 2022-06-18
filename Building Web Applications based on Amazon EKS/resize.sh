#!/bin/bash

# Designate Desired volume size
SIZE=${1:-30}

# Get AWS Cloud9 instance ID
INSTANCEID=$(curl -s http://169.254.169.254/latest/meta-data/instance-id)

# Get Amazon EBS ID associated with the instance 
VOLUMEID=$(aws ec2 describe-volumes \
--filters Name=attachment.instance-id,Values=$INSTANCEID \
--query "Volumes[0].VolumeId" \
--output text)

# Resize the EBS volume
aws ec2 modify-volume --volume-id $VOLUMEID --size $SIZE

while [ \
"$(aws ec2 describe-volumes-modifications \
    --volume-id $VOLUMEID \
    --filters Name=modification-state,Values="optimizing","completed" \
    --query "length(VolumesModifications)"\
    --output text)" != "1" ]; do
sleep 1
done

#succeed to modify volume

#Check Whether NVMe filesystem
if [ $(readlink -f /dev/xvda) = "/dev/xvda" ]; then
  # Rewrite the partition table to take up all the space that it can
  sudo growpart /dev/xvda 1
else 
  sudo growpart /dev/nvme0n1 1
fi

#succeed to rewrite the partition table

# Expand the size of the file system
STR=$(cat /etc/os-release)
SUB="VERSION_ID=\"2\""

if [[ "$STR" == *"$SUB"* ]]; then
  sudo xfs_growfs -d /
elif [ $(readlink -f /dev/xvda) = "/dev/xvda" ]; then
  sudo resize2fs /dev/xvda1
else
  sudo resize2fs /dev/nvme0n1p1
fi

#succeed to expand file system