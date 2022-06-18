# Building Web Applications based on Amazon EKS

# Cloud9 remove temporary credentials
rm -vf ${HOME}/.aws/credentials
aws sts get-caller-identity --query Arn

# Install cli
sudo pip install --upgrade awscli
aws --version

# Install kubectl
sudo curl -o /usr/local/bin/kubectl  \
   https://amazon-eks.s3.us-west-2.amazonaws.com/1.21.2/2021-07-05/bin/linux/amd64/kubectl
sudo chmod +x /usr/local/bin/kubectl
kubectl version --client=true --short=true

# Install jqHeader
sudo yum install -y jq

# Install bash-completion
sudo yum install -y bash-completion

# Install eksctl
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv -v /tmp/eksctl /usr/local/bin
eksctl version

# AWS Cloud9 Additional Settings
## AWS region
export AWS_REGION=$(curl -s 169.254.169.254/latest/dynamic/instance-identity/document | jq -r '.region')
echo "export AWS_REGION=${AWS_REGION}" | tee -a ~/.bash_profile
aws configure set default.region ${AWS_REGION}
aws configure get default.region

## ACCOUNT_ID
export ACCOUNT_ID=$(curl -s 169.254.169.254/latest/dynamic/instance-identity/document | jq -r '.accountId')
echo "export ACCOUNT_ID=${ACCOUNT_ID}" | tee -a ~/.bash_profile

## Resize the cloud9 disk
sh resize.sh
df -h

# Create EKS Cluster 
cd ~/environment
eksctl create cluster -f eks-demo-cluster.yaml
kubectl get nodes

# Attach Console CredentialHeader
rolearn=$(aws cloud9 describe-environment-memberships --environment-id=$C9_PID | jq -r '.memberships[].userArn')
echo ${rolearn}
eksctl create iamidentitymapping --cluster eks-demo --arn ${rolearn} --group system:masters --username admin
kubectl describe configmap -n kube-system aws-auth


# Build backend
git clone https://github.com/bolecodex/amazon-eks-flask.git
aws ecr create-repository \
--repository-name demo-flask-backend \
--image-scanning-configuration scanOnPush=true \
--region ${AWS_REGION}

# Create AWS Load Balancer Controller
cd ~/environment
mkdir -p manifests/alb-ingress-controller && cd manifests/alb-ingress-controller

## Create IAM OpenID Connect (OIDC) identity provider for the cluster
eksctl utils associate-iam-oidc-provider \
    --region ${AWS_REGION} \
    --cluster eks-demo \
    --approve
aws eks describe-cluster --name eks-demo --query "cluster.identity.oidc.issuer" --output text
aws iam list-open-id-connect-providers | grep <enter the id>
aws iam create-policy \
    --policy-name AWSLoadBalancerControllerIAMPolicy \
    --policy-document https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/v2.4.1/docs/install/iam_policy.json
eksctl create iamserviceaccount \
    --cluster eks-demo \
    --namespace kube-system \
    --name aws-load-balancer-controller \
    --attach-policy-arn arn:aws:iam::$ACCOUNT_ID:policy/AWSLoadBalancerControllerIAMPolicy \
    --override-existing-serviceaccounts \
    --approve

## Add AWS Load Balancer controller to the cluster
### cert-manager
kubectl apply --validate=false -f https://github.com/jetstack/cert-manager/releases/download/v1.5.3/cert-manager.yaml
### Download Load balancer controller
wget https://github.com/kubernetes-sigs/aws-load-balancer-controller/releases/download/v2.4.1/v2_4_1_full.yaml
### Apply after modification
kubectl apply -f v2_4_1_full.yaml
kubectl get deployment -n kube-system aws-load-balancer-controller
kubectl get sa aws-load-balancer-controller -n kube-system -o yaml
kubectl logs -n kube-system $(kubectl get po -n kube-system | egrep -o "aws-load-balancer[a-zA-Z0-9-]+")
ALBPOD=$(kubectl get pod -n kube-system | egrep -o "aws-load-balancer[a-zA-Z0-9-]+")
kubectl describe pod -n kube-system ${ALBPOD}

# Deploy First Backend Service
cd ~/environment/manifests/
kubectl apply -f flask-deployment.yaml
kubectl apply -f flask-service.yaml

# Deploy Second Backend Service
cd ~/environment/manifests/
kubectl apply -f nodejs-deployment.yaml
kubectl apply -f nodejs-service.yaml
kubectl apply -f ingress.yaml
echo http://$(kubectl get ingress/backend-ingress -o jsonpath='{.status.loadBalancer.ingress[*].hostname}')/contents/aws
echo http://$(kubectl get ingress/backend-ingress -o jsonpath='{.status.loadBalancer.ingress[*].hostname}')/services/all

# Deploy Frontend Service
cd /home/ec2-user/environment
git clone https://github.com/bolecodex/amazon-eks-frontend.git
aws ecr create-repository \
--repository-name demo-frontend \
--image-scanning-configuration scanOnPush=true \
--region ${AWS_REGION}
echo http://$(kubectl get ingress/backend-ingress -o jsonpath='{.status.loadBalancer.ingress[*].hostname}')/contents/'${search}'
echo http://$(kubectl get ingress/backend-ingress -o jsonpath='{.status.loadBalancer.ingress[*].hostname}')/services/all
cd /home/ec2-user/environment/amazon-eks-frontend
npm install
npm run build
docker build -t demo-frontend .
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com
docker tag demo-frontend:latest $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/demo-frontend:latest
docker push $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/demo-frontend:latest
kubectl apply -f frontend-deployment.yaml
kubectl apply -f frontend-service.yaml
kubectl apply -f ingress.yaml
echo http://$(kubectl get ingress/backend-ingress -o jsonpath='{.status.loadBalancer.ingress[*].hostname}')

# Deploy service with AWS Fargate
cd /home/ec2-user/environment/manifests
eksctl create fargateprofile -f eks-demo-fargate-profile.yaml
eksctl get fargateprofile --cluster eks-demo -o json
kubectl delete -f frontend-deployment.yaml
kubectl apply -f frontend-deployment.yaml # after change
kubectl apply -f frontend-service.yaml
kubectl get pod -o wide
echo http://$(kubectl get ingress/backend-ingress -o jsonpath='{.status.loadBalancer.ingress[*].hostname}')

# EKS CloudWatch Container Insights
cd ~/environment
mkdir -p manifests/cloudwatch-insight && cd manifests/cloudwatch-insight
kubectl create ns amazon-cloudwatch
kubectl get ns
ClusterName=eks-demo
RegionName=ap-northeast-2
FluentBitHttpPort='2020'
FluentBitReadFromHead='Off'
[[ ${FluentBitReadFromHead} = 'On' ]] && FluentBitReadFromTail='Off'|| FluentBitReadFromTail='On'
[[ -z ${FluentBitHttpPort} ]] && FluentBitHttpServer='Off' || FluentBitHttpServer='On'
wget https://raw.githubusercontent.com/aws-samples/amazon-cloudwatch-container-insights/latest/k8s-deployment-manifest-templates/deployment-mode/daemonset/container-insights-monitoring/quickstart/cwagent-fluent-bit-quickstart.yaml
sed -i 's/{{cluster_name}}/'${ClusterName}'/;s/{{region_name}}/'${RegionName}'/;s/{{http_server_toggle}}/"'${FluentBitHttpServer}'"/;s/{{http_server_port}}/"'${FluentBitHttpPort}'"/;s/{{read_from_head}}/"'${FluentBitReadFromHead}'"/;s/{{read_from_tail}}/"'${FluentBitReadFromTail}'"/' cwagent-fluent-bit-quickstart.yaml 
kubectl apply -f cwagent-fluent-bit-quickstart.yaml 
kubectl get po -n amazon-cloudwatch

# Applying Pod Scaling with HPA
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
kubectl get deployment metrics-server -n kube-system
kubectl apply -f flask-deployment.yaml # After change
kubectl apply -f flask-hpa.yaml
kubectl autoscale deployment demo-flask-backend --cpu-percent=30 --min=1 --max=5
kubectl get hpa -w
ab -c 200 -n 200 -t 30 http://$(kubectl get ingress/backend-ingress -o jsonpath='{.status.loadBalancer.ingress[*].hostname}')/contents/aws


# Applying Cluster Scaling with Cluster Autoscaler
aws autoscaling \
    describe-auto-scaling-groups \
    --query "AutoScalingGroups[? Tags[? (Key=='eks:cluster-name') && Value=='eks-demo']].[AutoScalingGroupName, MinSize, MaxSize,DesiredCapacity]" \
    --output table
wget https://raw.githubusercontent.com/kubernetes/autoscaler/master/cluster-autoscaler/cloudprovider/aws/examples/cluster-autoscaler-autodiscover.yaml
kubectl apply -f cluster-autoscaler-autodiscover.yaml
kubectl get nodes -w
kubectl create deployment autoscaler-demo --image=nginx
kubectl scale deployment autoscaler-demo --replicas=100
kubectl get deployment autoscaler-demo --watch

# Install Kubernetes Operational View
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/download/metrics-server-helm-chart-3.8.2/components.yaml

#curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash
curl -L https://git.io/get_helm.sh | bash -s -- --version v3.8.2
helm version --short
helm repo add k8s-at-home https://k8s-at-home.com/charts/
helm install my-kube-ops-view k8s-at-home/kube-ops-view --version 1.1.4
helm install my-kube-ops-view \
k8s-at-home/kube-ops-view \
--set service.type=LoadBalancer \
--set rbac.create=True
helm list





