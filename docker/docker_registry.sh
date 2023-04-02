# Registry is a repository of docker images.
# By default docker pull images from Docker Hub repository

# If you specify one name when pull / run image it stands for name/name
# where first name of the account on docker hub and second name of image 
docker run nginx <=> docker run nginx/nginx

# Docker hub dns name is docker.io
# So you can pull nginx like this:
docker run docker.io/nginx/nginx # address/account/image
# To push you image to you docker hub account you first should tag it username/appname
# It creates separate instance in docker images list
docker image tag worker-app alex1473/worker-app
# Then push it
docker push alex1473/worker


# You can make registry private. In docker hub for example
# In this case to run a container using an image from a private registry you should 
# first login to a private registry
docker login private-registry.io
# And then pull the image
docker run private-registry.io/apps/internal-app



# Docker registry is an applicatation and has an image
# So you can make your own private registry, it uses port 5000
# Run you own registry
docker run -d -p 5000:5000 --name registry registry:2

# To push your image to the registry you should tag it first (create locally image with this name)
# check it my using docker images
# Create a tag TARGET_IMAGE that refers to SOURCE_IMAGE
docker image tag my-image localhost:5000/my-image # registry-address/image-name 

# Then you can push image to registry
docker push localhost:5000/my-image

# Then you can pull this image from registry
docker pull localhost:5000/postgres # registry-address/image-name

# To look what containers is in the registry you can
# connect to it and go to specific folder with images
docker exec -it registry /bin/sh # connect to container terminal
cd /var/lib/registry/docker/registry/v2/repositories # go to folder with images

# You can use UI to interact with your custom registry
# For this install another container and connect it with your registry
docker run \
  -d \
  -e ENV_DOCKER_REGISTRY_HOST=registry \ # how to name you registry
  -e ENV_DOCKER_REGISTRY_PORT=5000 \ # port of your registry
  -p 8080:80 \
  --link registry:registry # link you registry with container with ui
  konradkleine/docker-registry-frontend:v2
# then go to the port 8080




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
# With kubernetes you can several instances of applicatation
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
