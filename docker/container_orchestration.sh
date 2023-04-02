# Container orchestration is a set of tools and scripts that can help 
# host containers in a production environment
# Typically a container orchestration solution consist of multiple 
# docker hosts that can host containers

# Container orchestration solutions easily allows you to deploy
# hundreds of thousands of instances of your application with
# a single command
# Some orchestration solutions can help you automatically 
# scale up the number of instances when users increase 
# and scale down the number of instances when the demand decraeses
# Some solutions can help you in automatically adding additional hosts 
# to support the user load
# It also provide networking between all of this containers across 
# different hosts as well as load balancing user requests across 
# different hosts.
# They also provide support for sharing storage beteween the hosts.

# There are some different container orchestration solutions:
# docker swarm, kubernetes, MESOS 



# DOCKER SWARM
# With Docker Swarm you could now combine multiple Docker machines together into a single cluster.
# Docker Swarm takes care of distributing your services or your application instances 
# into separate hosts for high availability and for load balancing across different systems and hardware.
# To set up docker swarm, first you should make one of your hosts to be the manager (master)
# and others as slaves (workers, node)
# Run to make machine a manager. The output provides command to be a run on workers:
docker swarm init --advertise-addr 192.168.1.12
# Command for workers:
docker swarm join --token <token>

# The key component of the orthestration is the docker service
# Docker service is one or more instances of the application that
# runs across the nodes in the swarm cluster.
# To run several instances of the application use command docker service create
# You should run this command on the manager node
docker service create --replicas=3 my-web-server
# Docker service create command similar to run command in terms of the paramenters you can pass
docker service create --replicas=3 -p 8080:80 my-web-server
docker service create --replicas=3 --network frontend my-web-server



# KUBERNETES
# With kubernetes you can have several instances of applicatation
kubectl run --replicas=1000 my-web-server
# You can scale it to another count
kubectl run --replicas=2000 my-web-server
# You can scale it up even automatically based on user load.

# With kubernetes you can upgrade instance of the application
kubect1 rolling-update my-web-server --image=web-server:2
# You can rollback this images
kubect1 rolling-update my-web-server --rollback

# Note, kubernetes uses docker host to host applications in the form of containers
# But its not be a docker all the time, kubernetes supports alternatives, for example rocket

# Kubernetes cluster supports a set of nodes.
# Node is a machine where kubernetes tools are installed.
# Node is a worker machine where containers will be launch by kubernetes.
# Special node master manages worker nodes.


# When you install kubernetes you install following components:
# API Server, etcd, kubelet, container runtime, controller, scheduler

# API Server - "frontend" for kubernetes. Users, managment defices, CLI all 
# talk to API server to interact with cluster

# etcd is a distributed reliable key-value store. Kubernetes use it to store
# all data used to manage a cluster

# Scheduler is responsible for distributing work / containers across multible nodes.
# It looks for a newly created container and assign it to a node.

# Controller is responsible for noticing and responding when nodes / container
# close down. Controllers make desicion create new containers in such cases.

# Container runtime is an underline software used to run containers.

# Kubelet is an agent that runs on every node in the cluster.
# It responsible for making sure that the containers are running on the node as expected.


# kubectl (kube control tool) is a command line utility (CLI).
# It used to deploy and managed applications on kubernetes clusters,
# get cluster related information, get status of the nodes and so on.

# Run app on the cluster
kubectl run my-app
# View information about the cluster
kubectl cluster-info
# List all nodes on the cluster
kubectl get nodes
# Run 100 instances of app
kubectl run my-app --image-my-app --replicas=100
