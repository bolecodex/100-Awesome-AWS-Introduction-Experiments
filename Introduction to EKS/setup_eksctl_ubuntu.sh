#!/bin/bash
# 1 Install requirement
sudo apt-get upgrade -y
sudo apt-get install net-tools git jq -y
sudo snap install yq

# 2 Install Docker
sudo apt-get remove docker docker-engine docker.io containerd runc -y
sudo apt-get install ca-certificates curl gnupg lsb-release -y
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
sudo bash -c 'echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] \
https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null'
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io -y  
sudo bash -c 'cat > /etc/docker/daemon.json << EOF
{
  "exec-opts": ["native.cgroupdriver=systemd"]
}
EOF
'
sudo gpasswd -a $USER docker
sudo systemctl enable docker 
sudo systemctl restart docker
sudo docker info

# 3 Install kubectl
sudo bash -c 'curl -s https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | sudo apt-key add -'
sudo bash -c 'echo "deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main" >>/etc/apt/sources.list.d/kubernetes.list'
sudo apt-get update
sudo apt-get install kubectl=1.22.10-00 -y
sudo bash -c "echo 'source <(kubectl completion bash)' >> /root/.bash_profile"
sudo bash -c "echo 'source <(kubectl completion bash)' >> /home/$USER/.bash_profile"
source <(kubectl completion bash)
sudo bash -c "chown $USER.$USER /home/$USER/.bash_profile"

# 4 Install awscli
os=`grep ^NAME /etc/os-release|awk -F = '{print $2}'|tr -d '"'` && if [ "$os" == "Ubuntu" ];then sudo apt install unzip;fi
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
aws --version

# 5 Install eksctl
curl -Ls "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
echo "source <(eksctl completion bash)" >> ~/.bash_profile
source <(eksctl completion bash)
eksctl version

# 6 Install kubectx
sudo git clone https://github.com/ahmetb/kubectx /opt/kubectx
sudo ln -s /opt/kubectx/kubectx /usr/local/bin/kubectx
sudo ln -s /opt/kubectx/kubens /usr/local/bin/kubens

# 7 Install helm
wget https://get.helm.sh/helm-v3.9.0-linux-amd64.tar.gz
tar xzvf helm-v3.9.0-linux-amd64.tar.gz
sudo cp linux-amd64/helm /usr/local/bin/helm
echo "source <(helm completion bash)" >> ~/.bash_profile
source <(helm completion bash)
helm version
