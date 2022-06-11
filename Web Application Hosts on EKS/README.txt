# Configure the AWS Access Key and Security Key
export AWS_DEFAULT_REGION=ap-southeast-1
export AWS_ACCESS_KEY_ID=<placeholder for key id>
export AWS_SECRET_ACCESS_KEY=<placeholder for your access key>
export ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)


# Install eksctl for creating and managing your EKS cluster
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
eksctl version

# Install Terraform
# https://www.terraform.io/downloads
sudo yum install -y yum-utils
sudo yum-config-manager --add-repo https://rpm.releases.hashicorp.com/AmazonLinux/hashicorp.repo
sudo yum install terraform -y
terraform --version

# Install the JSON praser jq command on your Cloud9 environment.
sudo yum install -y jq
jq

# Install kubectl
sudo curl --silent --location -o /usr/local/bin/kubectl \
   https://amazon-eks.s3.us-west-2.amazonaws.com/1.19.6/2021-01-05/bin/linux/amd64/kubectl
sudo chmod +x /usr/local/bin/kubectl

# Install docker
sudo amazon-linux-extras install docker
sudo service docker start
sudo usermod -a -G docker ec2-user

# Install Helm
curl -sSL https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash
curl -L https://git.io/get_helm.sh | bash -s -- --version v3.8.2

# Install NodeJS Runtime
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.34.0/install.sh | bash
.~/.nvm/nvm.sh
nvm install --lts
node -v

# Terraform Networking Architecture
# export VPC
export VPC_ID=$(terraform output -json | jq -r .vpc_id.value)
# export private subnets
export PRIVATE_SUBNETS_ID_A=$(terraform output -json | jq -r .private_subnets_id.value[0])
export PRIVATE_SUBNETS_ID_B=$(terraform output -json | jq -r .private_subnets_id.value[1])
export PRIVATE_SUBNETS_ID_C=$(terraform output -json | jq -r .private_subnets_id.value[2])
# export public subnets
export PUBLIC_SUBNETS_ID_A=$(terraform output -json | jq -r .public_subnets_id.value[0])
export PUBLIC_SUBNETS_ID_B=$(terraform output -json | jq -r .public_subnets_id.value[1])
export PUBLIC_SUBNETS_ID_C=$(terraform output -json | jq -r .public_subnets_id.value[2])

echo "VPC_ID=$VPC_ID, \
PRIVATE_SUBNETS_ID_A=$PRIVATE_SUBNETS_ID_A, \
PRIVATE_SUBNETS_ID_B=$PRIVATE_SUBNETS_ID_B, \
PRIVATE_SUBNETS_ID_C=$PRIVATE_SUBNETS_ID_C, \
PUBLIC_SUBNETS_ID_A=$PUBLIC_SUBNETS_ID_A, \
PUBLIC_SUBNETS_ID_B=$PUBLIC_SUBNETS_ID_B, \
PUBLIC_SUBNETS_ID_C=$PUBLIC_SUBNETS_ID_C"


# Create an EKS cluster configuration file
ssh-keygen
eksctl create cluster -f /home/ec2-user/environment/eks-cluster.yaml

# Grab the kube config file
mkdir ~/.kube
cat > ~/.kube/config << EOF
  <PASTE YOUR KUBE CONFIG FILE HERE>
EOF
chmod 600 ~/.kube/config 


# Deploying Prometheus
kubectl create namespace prometheus
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm upgrade -i prometheus prometheus-community/prometheus \
    --namespace prometheus \
    --set alertmanager.persistentVolume.storageClass="gp2",server.persistentVolume.storageClass="gp2"
kubectl get pods -n prometheus
kubectl --namespace=prometheus port-forward deploy/prometheus-server 8080:9090  

# Install AWS Distro for OpenTelemetry
curl https://raw.githubusercontent.com/aws-observability/aws-otel-collector/main/deployment-template/eks/otel-container-insights-infra.yaml |
kubectl apply -f -
kubectl get pods -l name=aws-otel-eks-ci -n aws-otel-eks


# Expose application to internet
helm repo add eks https://aws.github.io/eks-charts
kubectl apply -k "github.com/aws/eks-charts/stable/aws-load-balancer-controller//crds?ref=master"
helm install aws-load-balancer-controller eks/aws-load-balancer-controller -n kube-system --set clusterName=web-host-on-eks --set serviceAccount.create=false --set serviceAccount.name=aws-load-balancer-controller

# export public subnets
export PUBLIC_SUBNETS_ID_A=$(aws ec2 describe-subnets --filters "Name=tag:Name,Values=k8s-ap-southeast-1a-public-subnet" | jq -r .Subnets[].SubnetId)
export PUBLIC_SUBNETS_ID_B=$(aws ec2 describe-subnets --filters "Name=tag:Name,Values=k8s-ap-southeast-1b-public-subnet" | jq -r .Subnets[].SubnetId)
export PUBLIC_SUBNETS_ID_C=$(aws ec2 describe-subnets --filters "Name=tag:Name,Values=k8s-ap-southeast-1c-public-subnet" | jq -r .Subnets[].SubnetId)

# Get application URL
kubectl get ing -n front-end
echo "http://$(kubectl get ing -n front-end --output=json | jq -r .items[].status.loadBalancer.ingress[].hostname)"


ab -c 500 -n 30000 http://k8s-frontend-ingressf-64d27aad1b-1167206074.ap-southeast-1.elb.amazonaws.com/
eksctl delete cluster -f /home/ec2-user/environment/eks-cluster.yaml
