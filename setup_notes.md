# Run Microservices Dockerized Application in Java in Ubuntu machine

### Pre-requisites
`Install Kubernetes from kubernetes_setup.md`

### Install git and maven
apt-get update && apt-get install -y git tree maven default-jdk

### Clone the project from git
```bash
git clone https://github.com/shekhar2010us/microservices_kubernetes_docker.git
```

### Build installers for all services
```bash
# user-service
cd user-service
mvn clean install -U

# product-service
cd ../product-service
mvn clean install -U

# api-composer-service
cd ../api-composer-service
mvn clean install -U
```


### Build required docker images
```bash
# go to project root directory
cd ..

# build docker image for user data loader
docker build -f Dockerfile.userdataloader -t java_mvn_redis_userloader:1.0 .

# build docker image for product data loader
docker build -f Dockerfile.productdataloader -t java_mvn_redis_productloader:1.0 .
```

### Deploy Kubernetes entities
```bash
cd k8s_yaml

# deploy redis server
kubectl create -f 1redis-deployment.yml
kubectl create -f 1redis-service.yml

kubectl create -f redis-dep.yml
kubectl create -f redis-svc.yml
kubectl port-forward deployment/redis-master 6379:6379 


# deploy redis data loader
kubectl create -f 2redis_user_loader-pod.yml
kubectl create -f 2redis_product_loader-pod.yml

# deploy persistent claim for user-service
kubectl create -f userservice-pc.yml
kubectl create -f userservice-pv.yml
kubectl create -f 3userservice-deployment.yml
kubectl create -f 3userservice-lb.yml

# deploy persistent claim for product-service
kubectl create -f productservice-pc.yml
kubectl create -f productservice-pv.yml
kubectl create -f 4productservice-deployment.yml
kubectl create -f 4productservice-lb.yml

# deploy persistent claim for api-composer-service
kubectl create -f apicomposerservice-pc.yml
kubectl create -f apicomposerservice-pv.yml
kubectl create -f 3apicomposerservice-deployment.yml
kubectl create -f 3apicomposerservice-lb.yml
```


### Test your application
Different ways to run

If used load balancer
`http://<external-ip>:8080/user-service-1.0/rest/userDesign/getuser?userid=102`

<br>

If used nodeport service
`http://<host-ip>:<target_port>/user-service-1.0/rest/userDesign/getuser?userid=102`




## run these in browser to test, replace IP
* http://<IP>:<port>/user-service-1.0/rest/userDesign/getuser?userid=102
* http://<IP>:<port>/product-service-1.0/rest/productDesign/getproduct?productid=201
* http://<IP>:<port>/api-composer-service-1.0/rest/apicomposerDesign/userfromapic?userid=101
* http://<IP>:<port>/api-composer-service-1.0/rest/apicomposerDesign/userbuy?userid=101&productid=201

