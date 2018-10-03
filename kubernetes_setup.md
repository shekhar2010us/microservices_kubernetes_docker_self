# Setting up Multi-node Kubernetes on virtual machine

## * Setting up the machine

#### SSH to the Ubuntu machine
```bash
ssh username@FQDN
sudo su -
```

#### package install - java, git, maven
```bash
sudo apt-get -y update
sudo apt-get -y install default-jdk git maven redis-tools selinux-utils
```

#### Install docker
```bash
curl -sSL https://get.docker.com/ | sh
sudo groupadd docker
sudo usermod -aG docker $USER
# exit and login again
```

#### Install docker-compose
```bash
sudo curl -L https://github.com/docker/compose/releases/download/1.21.0/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

#### Check docker and docker-compose and image pull
```bash
docker version
docker-compose version
service docker status
docker pull alpine
```

## * Install kubeadm (This step is for all nodes in the cluster)
[Kubeadm Official Installation](https://kubernetes.io/docs/setup/independent/install-kubeadm/)

```bash
sudo apt-get -y update
sudo apt-get install -y apt-transport-https curl
sudo curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
```
<br>

Add below content to "/etc/apt/sources.list.d/kubernetes.list"
`deb http://apt.kubernetes.io/ kubernetes-xenial main`

<br>

```bash
sudo apt-get -y update
sudo apt-get install -y kubelet kubeadm kubectl
```

#### Check cgroup for docker and use the same for kubelet
```bash
docker info | grep -i cgroup
sed -i "s/cgroup-driver=systemd/cgroup-driver=cgroupfs/g" /etc/systemd/system/kubelet.service.d/10-kubeadm.conf
```
<br>

#### System reload
```bash
systemctl daemon-reload
systemctl restart kubelet
setenforce 0
systemctl enable kubelet && systemctl start kubelet
```

Add below content to the file '/etc/sysctl.d/k8s.conf'
`net.bridge.bridge-nf-call-ip6tables=1`<br>
`net.bridge.bridge-nf-call-iptables=1`

#### Check the above
```bash
sysctl --system
```

<br>

#### If kubelet service is not working, there are two options:
* make swap off
```bash
swapoff -a
```
* use swap off property while initializing kube-init
```bash
sed -i '9s/^/Environment="KUBELET_EXTRA_ARGS=--fail-swap-on=false"\n/' /etc/systemd/system/kubelet.service.d/10-kubeadm.conf
```
<br>

#### kubeadm reset after changing the swap
```bash
sysctl net.bridge.bridge-nf-call-iptables=1
systemctl daemon-reload && systemctl restart kubelet
kubeadm reset
```
<br>

## * Initialize master (This step is only for the master node in the cluster)

```bash
kubeadm init --pod-network-cidr=10.244.0.0/16 --apiserver-advertise-address=<IP> --ignore-preflight-errors=Swap
systemctl status kubelet
kubectl get node -o wide
```

<br>

#### Check for the below message.

> Your Kubernetes master has initialized successfully!
>
> To start using your cluster, you need to run the following as a regular user:
> mkdir -p $HOME/.kube
> sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
> sudo chown $(id -u):$(id -g) $HOME/.kube/config
>
> You should now deploy a pod network to the cluster.
> Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
> https://kubernetes.io/docs/concepts/cluster-administration/addons/
>
> You can now join any number of machines by running the following on each node as root:
>
> kubeadm join 34.205.24.3:6443 --token 4gsgnm.lhfyxzzjgi3b79xg --discovery-token-ca-cert-hash sha256:92bf9381ac80c933bf97cdeda5f09777fbabcb637909fc13164ddf66d45aec3d
>


## * Install pod network (This step is for all the nodes in the cluster)

So that pods can communicate with each other. We are using flannel (flannel works on amd64, arm, arm64 and ppc64le). Other options are calico, canal, kube-router. Below is the list of pod networking.

[Pod Networking](https://kubernetes.io/docs/setup/independent/create-cluster-kubeadm/)

[Coreos Flannel](https://kubernetes.io/docs/setup/independent/create-cluster-kubeadm/)


```bash
sysctl net.bridge.bridge-nf-call-iptables=1
```

#### Set KUBECONFIG
```bash
export KUBECONFIG=/etc/kubernetes/admin.conf
```

#### providing pod networking and network policy - flannel
```bash
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/v0.9.1/Documentation/kube-flannel.yml
```

<br>

## * Create Cluster using kubeadm (This step is for all the nodes in the cluster)
[Create-cluster-kubeadm](https://kubernetes.io/docs/setup/independent/create-cluster-kubeadm/)


> kubeadm is initialized in the last step already


#### for root user
```bash
export KUBECONFIG=/etc/kubernetes/admin.conf
```

#### for non-root user
```bash
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```
<br>

#### tokens are used to join the kube cluster, it can be retrieved using "kubeadm token" commands
```bash
kubeadm token list
```
<br>


## * Test if pod network installed properly
```bash
kubectl get nodes -o wide --all-namespaces
kubectl get pods -o wide --all-namespaces
```

#### Use Master node for deploying containers - Usually done for 1 node cluster
```bash
kubectl taint nodes --all node-role.kubernetes.io/master-
```
<br>


#### Join Node to master (change the token created by your Kubernetes master)
```bash
kubeadm join --token 511446.6121e5a77c23e9eb 162.44.170.117:6443 --discovery-token-ca-cert-hash sha256:586789540ff1fa8dbb8cf8c9762424b96604e5eaf40cb203b79e5c587b23630b
```
<br>

#### Check in master node
```bash
kubectl get nodes --all-namespaces -o wide
kubectl get pods --all-namespaces -o wide
kubectl get services --all-namespaces -o wide
kubectl get secrets --all-namespaces -o wide
kubectl get serviceaccounts --all-namespaces -o wide
```

<br>

## * Setup docker private registry

#### Run the registry container
Note:- IP of machine running registry = ip1
It is always preferable to use a shared directory for storing data
```bash
docker run -d -p 5000:5000 --restart always --name registry registry:2
```
<br>

#### To all machines in the cluster, add the below
To the file '/usr/lib/systemd/system/docker.service'<br>
`ExecStart=/usr/bin/dockerd --insecure-registry ip1:5000`

<br>

To the file '/etc/docker/daemon.json'<br>
` { "insecure-registries":["ip1:5000"] }`

<br>

#### Reload
```bash
systemctl daemon-reload
systemctl restart docker
```
<br>

The kubernetes cluster has been installed successfully.
