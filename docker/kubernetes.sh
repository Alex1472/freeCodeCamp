# Kubernetes is one of the advanced Docker orchestration technologies available
# today. Kubernetes was built by Google.
# Docker Swarm is one of the container orchestration technology and so is Kubernetes.



# With Docker Swarm we learned that a cluster is formed by a Swarm Manager node
# and multiple worker nodes all running docker on them.
# The same is true with Kubernetes.

# Kubernetes cluster is formed by a master node and multiple worker nodes
# that obey the master node.
# The worker nodes are named MINIONS.

# With Docker Swarm, the master node is responsible for orchestration,
# which is scheduling tasks to run containers on the worker nodes,
# monitoring the status of containers, restarting containers on failure,
# and scaling containers as required. The same is true with Kubernetes.
# The Kubernetes MASTER node is responsible for all orchestration tasks.



# PODS
# Unlike Docker Swarm, Kubernetes does not run containers directly on the worker nodes.
# Kubernetes wraps the containers inside a virtual block, known as PODS.
# A pod is the smalles and simplest unit in Kubernetes that you can create.
# With Kubernetes you can't run a container by itself, or you can't create a 
# service with just containers. All containers have to be encapsulated in a 
# unit of deployment known as pods. Each container encapsulated by a separate pod.
# IN THE SIMPLES CASE we have one-to-one relationship between a pod and a container.

# BUT A SINGLE POD CAN HAVE MULTIPLE CONTAINERS WITHIN IT.
# It's usually not multiple containers of the same type.
# If you intention is to scale your application, you would deploy multiple
# instances of the pod itself.
# The multiple containers in a pod are the different application images that
# depend on each other.

# For example, container one could be every application that gets a request from the user
# and container two could be a backend service that processes that request.
# These containers depend on each other. They share the same storage and share the
# same network space.

# In Kubernetes, an IP address is assigned at a POD LEVEL and not at a container
# level as in Docker Swarm. All containers within the same pod share the same
# network namespace and can communicate with each other using the host name - local host.
# They also share the same storage volumes allowing the different containers to
# access the same shared data.

# A pod is thus another layer of abstraction that Kubernetes provides to group
# together different containers, their network and storage into a virtual container.
# Containers in the same pod are created and destroyed together.

# Also, you application is scaled at the pod level.
# You can add several pods not containers. You can have multiple pods within
# the same Kubernetes worker of have them separated onto different workers.

# >>>>
# So containers in a pod share network, volume, life cycle, and scaled at the pod level.
# >>>>



# DEPLOYMENT
# Services in Docker Swarm are multiple instances of a container run across 
# the swarm cluster.
# In Kubernetes it's known as a DEPLOYMENT.
# A deployment creates a REPLICA SET to create multiple instances of pods.
# Replica set ensure that the specified number of pod instasnces are running at 
# any given time.



# SERVICES
# In Docker Swarm we used links and networks to enable communication between containers
# We had ingress network to enable external access to the services.
# In Kubernetes this is implemented with the help of SERVICES.
# There are different type of services such as internal and external.
# We will create internal-facing services for connectivity between the components
# such as the web service and the database and external facing services using load balancer
# to expose ports to the external world so users can access our application



# KUBECTL
# Kubernetes provides the kubectl command-line interface.
# We can use it to create configurations, get the current status of get detailed
# information about the services.
# Use kubectl create with pod definition file to create pods
kibectl create -f pod-definition.yml
# Use the kubectl get pods command to get a list of pods and their statuses
kubectl get pods
# Use the kubectl describe pods to get detailed information about pods
kubectl describe pods



# DEPLOYMENTS, SERVICES DEFINITION
# In Docker Swarm we created stacks using yml-based docker-compose files
# Similarly, in Kubernetes pods, deployments and services are all created 
# using a yml-based definition file
# The format is a bit different the the docker-compose file
# There are 4 different sections in the file:
# 1) apiVersion - discribes the version of schema we're using. Current version is 1
# 2) kind - The kind describes the type of resource we are defining in the yml file
#    It could be a pod, deployment or service
#    We could use yml files to define all of the different resources we create
#    in Kubernetes, in place of create command with a lot of arguments
#    Note, there are other type of resources.
# 3) metadata - Metadata - this we provide additional information about our configurations
#    such as it's name, custom labels and many other things.
# 4) spec - Specification, here we specify the actial deployment definition
#    such as the type of containers, that form the pod, the images used, the pods exposed ...      

# Example of a simple pod definition file:
# pod-definition.yml
apiVersion: v1

kind: pod # we creating a pod

metadata:
    name: my-nginx # name of the pod
	
spec:
	containers: # we have 1 container in a pod
    - name: nginx
	  image: nginx
	  ports:
	  - containerPort: 80 # expose port 80 from nginx container
        				  # expose port so that other pods can address it
						  # do not specify anything if your pods just send
						  # requests to another pods, not respons to request
			 
# To run the pod:
kubectl create -f pod-definition.yml

# Another example:
apiVersion: v1
kind: Pod
metadata:
    name: voting-app-pod
    labels: # specify labels to search you pod in another pods
        name: voting-app-pod
        app: demo-voting-app
spec:
    containers:
	- name: voting-app
	  image: dockersamples/examplevotingapp_result
	  ports:
		  - containerPort: 80
		  
# Service example:
apiVersion: v1
kind: Service
metadata:
    name: redis # we should name service based on name other pods looking for (used in apps)
    labels:
        name: redis-service
        app: demo-voting-app
spec:
    ports: # specify ports we expose and listen from pod
        - port: 5432
          targetPort: 5432
    selector: # we use filters consists of labels to link pods to service
        name: redis-pod
        app: demo-voting-app
		
# LoadBalancer
# By default we create an internal service 
# It created with the type ClusterIP
# These services are only available within cluster.
# To enable the service to be accessible outside the cluster,
# we must change the type of the cluster to something else
# for example to LoadBalancer
apiVersion: v1
kind: Service
metadata:
    name: voting-app-service
    labels:
        name: voting-app-service
        app: demo-voting-app
spec:
    type: LoadBalancer
    ports:
        - port: 5432
          targetPort: 5432
    selector:
        name: voting-app-pod
        app: demo-voting-app




# Similarly we could create multiple different definition files, one for each
# pod deployment or service we need and execute the kubectl
# Another example, we want to deploy our voting app with Kubernetis
# We have 5 containers(voting-app, result-app, worker, redis, db)
# We assume that Kubernetes cluster is already set up and running.
# So we need:
#  1) Deploy 5 containers
#  2) Establish communication between containers
#  3) External access (publish ports for voting-app and result-app)

# In a Kubernetes terms we should:
#  1) Deploy 5 PODS (1 for each container)
#  2) Create Services (internal connectivity between containers)
#  3) Create Services (LoadBalancer, for external access)




