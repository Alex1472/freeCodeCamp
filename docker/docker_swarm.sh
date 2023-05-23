# With docker swarm you can combine multiple machines into a single cluster.
# Docker swarm with take care of distributing you services into separated hosts.
# It also helps up in balacing load.

# To setup a cluster you should
# - have several hosts with installed docker
# - designate one of the hosts to be a master(Swarm manager)
# - designate others to be slaves(workers)



# MANAGER NODES
# Manager node is responsible for maintaining cluster state, managing the workers,
# adding and removing workers and creating, distributing and ensuring the state of containers
# and services across all workers.

# It's better to have several manager nodes, because manager node can also go down.
# But when you have several manager nodes there arises a conflict of interest.
# To prevent that only one manager node is allowed to make management decisions.
# That node is called as a LEADER.
# The leader cannot always make a decisions on its own
# All decisions have to be mutually agreed upon by all managers or the majority of the managers in the cluster.
# This is important because if the leader was to make a decision and then fail before informing
# the other managers about the decision the cluster would be in an inconsistent state.
# For example, you leader added new worker and failed, other managers don't know about this worker.
# This is know as the problem of DISTRIBUTED CONSENSUS.



# Docker solves this problem by implementing RAFT CONSENSUS ALGORITHM.
# Let's look at it.
# Algorithm uses random timers to initiating requests

# For example, random timer is kicked off on the three managers
# The first on to finish the timer sends out a request to the other managers requesting
# permission to be the leader.
# The other managers on receiving the request responce with their vote
# and the node assumes the leader role.

# Now, as a leader, it sends out notifications at regular intervals to other masters
# informing that it is continuing to assume the role of the leader.
# In case the other nodes do not receive a notification from the leader at some point in timer
# (leader lose connectivity or has gone down), the nodes initiate a reelection process among themselves
# and the new leader is identified

# Every manager has its own copy of Raft database that stores information about entire cluster.
# And it's important that they're all in sync.
# If the leader make a decision(for example add a worker), it has to notify the other two managers,
# get responce from at least one of them to meet QUORUM. And then commit the changes to the database 
# on all master nodes. This ensures that any changes in the environment is made with consent
# (the data stored in multiple databases in consistent fashion) from majority
# of the managers in the cluster.

# So with Raft consencsus algorithm, every decision has to be agreed upon
# by majority of the manager nodes. For example with 3 managers, 2 should approve it.
# It is made so, so that if one manager failed, other managers can make decision (they have 2 votes)
# This is known as a quorum. QUORUM is defined as the minimum number of memebers
# in as assembly that must be present at any of its meetings to make the proceedings
# of that meeting valid. (for 5 - quorum 3, for 7 - 4)
# Note, docker recommends no more seven manager nodes for a cluster.
# Adding more managers does not increase scalability or performance of the cluster.
# But no limit for managers

# The reverse of that, the maximum number of nodes that can fail will give
# us the FAULT TOLERANCE (for 7 managers - 3, for 5 - 2)

# It is recommended to select an odd number of managers(7, 5, 3)
# The reason for that is in case the network was to get segmented
# and the master nodes were to get placed onto two separate networks
# always there will be a group of managers that can make decision 
# in consence of quorum.

# What happens when you don't have enought managers up and the cluster fails?
# The Swarm will no longer be able to perform any more management tasks.
# However, the existing worker nodes, their configuration, and the services
# running on those worker nodes will continue to operate as they were supposed to
# But you can't perform any modifications on them like adding a new worker
# The best way to recover from losing the quorum is to bring the failed nodes back online
# Another way to recover is to force create a new cluster
# We can run init command with --force to create new cluster with only one manager
# and all worker. The we can and another managers or promote worker to manager

# Note, manager also can do work as worker such as hosting a service
# By default, all manager nodes are also worker nodes
# You can disable that and dedicate a node for management purpose alone.
# Docker recommends dedicating manager nodes for management tasks only 
# in the production environment.



# MANAGERS COMMANDS
# To initialize docker swarm cluster run docker swarm init on a future manager 
# node. This command initialize current node as master node
docker swarm init 
# It will show command to use worker to join the cluster, enter this command
# on workers to join this cluster
docker swarm join --token some_token 192.168.56.101:2377
# You can get this command to join the cluster as worker with command:
docker swarm join-token worker
# if you host has multiple api addresses you will get error
choose an IP address to advertise since system has multiple addresses
on different interfaces
# in this case you should select one with --advertise-addr option
docker swarm init --advertise-addr 192.168.56.101 

# to leave a cluster use command, you should run it on worker node:
docker swarm leave
# But this command saves node in nodes list (docker node ls), to remove a node
# from it use:
# You should run it on master node 
docker node rm node_name #

# To show all nodes in the cluster
docker node ls
# You can see status on nodes there, see MANAGER STATUS
# Leader - leader
# Reachable - manager, not leader
# empty - worker
# Unreachable - node is down
# Note, cluster is down (we loose quorum), ls command shows an error

# we want to join cluster as a manager
# first we need get a command for it, to get it use:
docker swarm join-token manager
docker swarm join-token worker # get token for worker
# it looks like, run it on corresponding node:
docker swarm join --token some-token 192.168.56.101:2377

# To promote node to manager, use command(on manager):
docker node promote node-name

# To recreate cluster, use command:
# It recreats cluster with one master node, other - workers
docker swarm init --force-new-cluster # you may need add option --advertise-addr ip



# DOCKER SERVICES
# Docker services are one or more instances of a single application or a service
# that runs across the Swarm cluster.
# We could create a docker service running multiple instances of a web application
# across worker nodes in my Swarm cluster. Note docker service command can be run
# only on manager node
docker service create --replicas=3 my-app-name # create 3 instance of app across workers
# docker service create command is similar to docker run command in terms of
# its arguments
docker service create --replicas=3 -p 8080:80 --network frontend my-web-server

# When we want to create three instances, Orchestrator on the manager node
# decides how many tasks to create, and the scheduler scheduler that tasks on
# worker nodes
# The task is a process that kicks off tha actual container instances
# Task has a one-to-one relationship with each container.
# The task is responsible for updating the status of the container to the manager node,
# so the manager node can keep track of the workers and instances running on them.
# If the container failed, the task failed as well. The manager node becomes
# aware of it and it automatically reschedules a new task to run a new container instance

# Let's say we have only two worker nodes and we want to run three replicas of an instance.
# Running docker service create command will create three containers spread across two worker nodes.

# Let's say we have four worker nodes and we create a service to run three instances of the app
# This will create three containers on three worker nodes and we leave the fourth worker empty.
# If one of the worker node was to fail, it will redeploy a new instance
# of that container on the fourth worker to ensure three replicas
# of the service are available.


# There are two types of services, replicated and global.
# We seen replicated services. It is a services that created with the --replica option
# with predefined number of replicas
# In this case number of instances is irrespective of the number of underlying worker nodes

# In another case you don't want to specify the number of replicas of a service
# Instead you want one instance of a service placed on all nodes in the cluster
# This is called GLOBAL SERVICES.
# To place app on all nodes use option --mode global
docker service create --mode global app-name
# For name docker swarm automatically append number to name for each container
# to avoid the same names
docker service create --replicas=3 --name web-server my-web-server # web-server.1 web-server.2 web-server.3

# Let's look how to update a service
# For example we create service with 3 replicas, and want add fourth
# use docker update command for this
docker service create --replicas=3 --name web-server my-web-server
docker service update --replicas=4 web-server

# Use ls command to list all services
docker service ls
# Use ps command to list all tasks related to the service (task has one-to-one
# relationship to container)
docker service ps service_id

# You can update service configuration, for example publish a port, with docker service update:
docker service update service_id --publish-add 5000:80

# To remove service use rm command
docker service rm service_id

# You can deny manager to run containers(be worker), for example with update command
docker node update --availability drain docker-master-id/name
# It shuts down all tasks on master node and run them on available worker nodes.

# remove stop service and remove all running containers
docker service rm service_id



# NETWORKING
# Say we have multiple Docker hosts running containers in internal 
# private bridge network
# Therefore, containers can communicate with each other, however
# containters across the host has no way to communicate with each other
# unless you publish ports and set up some kind of routing

# This is where overlay networks comes into play
# With docker swarm you can create a new network of type overlay
# which will create an internal private network 
# that spans across all the nodes participating in the Swarm cluster
docker network create --driver overlay --subnet 10.0.9.0/24 my-overlay-network
# We could then attach the containers or services to this network
# using the network option while creating a service
# Therefore, we can get them communicate with each other through the overlay network.
docker service create --replicas 2 --network my-overlay-network nginx

# Note, several instances of app can be run on the same host
# But what if the app publich a port, then both instance will listen the
# same port, what is incorrect
# This is where INGRESS NETWORKING comes into the picture.
# When you create a Docker Swarm, it automatically creates an Ingress network
# The Ingress network has a built-in load balancer that redirects traffic
# from the published port, to all mapped ports of app instances
# Since the ingress network is created automatically there is no configuration
# you have to do. You simply have to create the service you need (with docker
# service create command), and publish ports and Ingress network will simply
# work out of the box

# Let's say we have three nodes and two running instances of an app
# Since we requested only 2 replicas the third Docker host is free
# and has no instances.
# Without Ingress networking user could access the app on nodes one and two
# but not on node three because there is no web service instance running
# on node three
# Ingress networking is, in fact, a type of overlay network, so it spans across
# all the nodes in the cluster
# The way the load balancer works is it receives requests from any node in the
# cluster and forwards that request to the respective insteances on any other node,
# essentially creating a ROUTING MESH.
# The routing mesh helps in routing the user traffic that is received on a node
# that isn't even running an instance of the web service to other nodes
# where the instances are actually running.
# Note, all these is a default beahavior of Docker Swarm, you shouldn't do anything additionally



# DOCKER STACKS
# Before we run individual containers. Instead of running several containers 
# we used docker compose and docker-compose file
# With docker swarm we run docker services. What if we want run several 
# services together
# We can use docker stack in this case and docker-compose file
# That run docker stack deploy command to deploy the entire application stack all at once
# All configuration in one docker-compose file

# Not in you app you can have several services with different count of replicas
# For example, you can have 3 node.js containers, 2 postgres containers and 
# one redis container. Constainers of different (and the same) services can 
# run on the same node.

# You can use docker-compose file with docker swarm. For this you need to add several
# docker swarm related properties to the file
# Let's look on docker compose file
# You should have docker compose file at least version 3 for docker stack configuration
# We trimmed out ports and networks options for simplicity.
# You need to add the DEPLOY property for every container. It can contains several properties
# docker-compose.yml:
version: 3
services:
    redis:
	    image: redis
		deploy:
		    replicas: 1 # how many containers to deploy
			resources: # specify limitation on resources to consume
			    limits:
				    cpus: 0.01
					memory: 50M
	
	db:
	    image: postgres:9.4
		deploy:
		    replicas: 1
			placement: # specify constraints where to plase containers
			    constraints: 
				    - node.host == node1 # on host with name node1
					- node.role == manager # or on a manager host
	
	vote:
	    image: voting-app
		deploy:
		    replicas: 2
			
	result:
	    image: result
		deploy:
		    replicas: 1
			
	worker:
	    image: worker
        deploy:
		    replicas: 1

# You can specify another options for deploy
deploy:
    replicas: 8
    update_config: # config about updating containers if the image has changed
        parallelism: 2 # The number of containers to update at a time.
        delay: 20s # The time to wait between updating a group of containers.
    restart_policy: # Configures if and how to restart containers when they exit.
        condition: any # restart with any reason of failure
			
# To run stack use command, use the --compose-file flag to specify .yml file
docker stack deploy app-name --compose-file docker-stack.yml
# Or use flag -c (shortcut)
docker stack deploy -c docker-compose.yml -c docker-compose.prod.yml myapp
# You can update your stack in case of changing .yml file or changing image by running stack deploy command again
# This will not stop the services just update them 
docker stack deploy app-name --compose-file docker-stack.yml

# Use docker stack rm to stop stack
docker stack rm app-name

# Use docker stack ls to show all stacks (sets of services)
docker stack ls

# Use docker stack services stack_name to show all services of the stack
docker stack services stack_name

# Use docker service ls to list all services across all stacks
docker service ls

# Use docker stack ps command to show all tasks(containers) of the service
docker stack ps service_id



# DOCKER VISUALIZER
# You can see how containers are distributed on the nodes
# Use docker visualizer for this
# Just run its container and go to the corrensponding port
docker run -it -d -p 8080:8080 -v /var/run/docker.sock:/var/run/docker.sock dockersamples/visualizer
