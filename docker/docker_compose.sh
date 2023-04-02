# To run application with multiple services you should run several container 
# each with one service. 
# It's better to use docker compose, and store run configuration of all containers in .yml file

# RUN APP WITH DOCKER RUN COMMAND
# Let's look at example
# We have application with this architecture and stack
# We voting with voting app. Save data into redis
# With worker get data from redis and save it to postgres db
# And result-app access db to get results

# voting-app       result-app
#  python            node.js
#     |                |
#     |                |
#in-memory DB          db
#   redis           postgres
#     \                /
#      \              /
#       \            /
#        \          /
#         \        /
#           worker
#            .net

# Let's run this containers
docker run -d --name=redis redis
docker run -d --name=db -e POSTGRES_HOST_AUTH_METHOD=trust postgres # to run without authentification
docker run -d --name=vote -p 5000:80 voting-app
docker run -d --name=result -p 5001:80 result-app
docker run -d --name=worker worker

# That doesn't work because we don't connect this containers together
# We didn't say voting-app use paticular redis container instance
# We didn't tell worker and result-app to use paticular db container

# Voting app looking for redis service running on host redis
# But it app container can't resolve the host by the name redis
g.redis = Redis(host="redis", db=0, socket_timeout=5)
# We should app link when running redis container
# --link target_container_name:host_name_that_our_container_looling_for
docker run -d --name=vote -p 5000:80 --link redis:redis voting-app
# It's create an entry into the /etc/hosts file on the voting app container
# adding an entry with the hostname redis with an internal ip of the redis container

# Also we need to add link to db from result-app
# And links to db and redis from worker
# Result:
docker run -d --name=redis redis
docker run -d --name=db -e POSTGRES_HOST_AUTH_METHOD=trust postgres # to run without authentification
# or specify default user and password POSTGRES_USER=postgres, POSTGRES_PASSWORD=postgres
docker run -d --name=vote -p 5000:80 --link redis:redis voting-app
docker run -d --name=result -p 5001:80 --link db:db result-app
docker run -d --name=worker --link db:db --link redis:redis worker

# But now links way to connect container is depricated


# DOCKER COMPOSE VERSION 1
# Now we will use docker compose to deploy our application
# We need to install docker compose separate from docker
# -- - https://docs.docker.com/compose/install/linux/#install-the-plugin-manually
# To run docker compose use (for lates version):
docker compose up

# Let's create a docker-compose.yml file
# We create a dictionary with key - container name(that we specify with --name)
# value - information about the container
# It's a dictionary 
# image - which image to use 
# ports - which ports to publish
# links - for links. Note you can use shortcut db instead of db:db

# docker-compose.yml:
redis:
  image: redis
db:
  image: postgres:9.4
vote:
  image: voting-app
  ports: 
	- 5000:80
  links:
    - redis
result:
  image: result-app
  ports:
    - 5001:80
  links:
    - db
worker:
  image: worker
  links:
    - redis
	- db
	
# We can say build image instead of try to find it
# Use build key for this. In this case docker build image with 
# internal name and create container bases on it.
# You should specify directory where app with dockerfile is located
redis:
  image: redis
db:
  image: postgres:9.4
vote:
  build: ./vote # specify directory with Dockerfile
  ports: 
	- 5000:80
  links:
    - redis
result:
  build: ./result
  ports:
    - 5001:80
  links:
    - db
worker:
  build: ./worker
  links:
    - redis
	- db
	
	
# DOCKER COMPOSE VERSION 2		
# We looked at version 1 of docker compose file
# But now there is versions 2 and 3

# In version 2 you specify information about services in a special services section

# For version 2 and up you should specify version up in the file

# Another diffrence in networking. In version 1 docker-compose attaches
# all the containers it runs to the default bridges network and then use
# links to enable communication between containers as we did before
# With version 2 docker compose automatically creates a dedicated bridges network
# for this application and then attaches all containers to that new network
# all containers. All containers are then able to communicate to each other 
# using each other's service name. So you don't need to use links in version 2

# Version 2 introduces depends on feature. If you wish to specify a start up order.
# For instance voting applicatin is dependent on the redis service
# So you need to ensure that the redis container start first and only then the voting app.
# You can add depend_on property and indicate that app depend on redis

# Example - version 1:
redis: 
	image: redis
db:
	image: postgres:9.4
vote:
	image: voting-app
	ports: 
		- 5000:80
	links:
		- redis

# Example - version 2:
version: "2"
services:
	redis:
		image: redis
	db:
		image: postgres:9.4
	vote:
		image: voting-app
		ports:
			- 5000:80
		depends_on:
			- redis
			
			
# Full application .yml file
version: "2"
services:
  redis:	
    image: redis

  db:
    image: postgres:15-alpine
    environment:
      - POSTGRES_HOST_AUTH_METHOD=trust   

  voting-app:
    image: voting-app
    ports:
      - 5000:80
    
  worker-app:
    image: worker-app
    
  result-app:
    image: result-app
    ports:
      - 5001:80



# DOCKER COMPOSE VERSION 3
# Version 3 has the same structure as version 2
# Version 3 comes with support for Docker Swarm
# There are some options that were remove or added.
# We discuss version 3 later.
version: "3"
services:
	redis:
		image: redis
	db:
		image: postgres:9.4
	vote:
		image: voting-app
		ports:
			- 5000:80
		depends_on:
			- redis



# DIFFERENT NETWORKS
# So far we deploying all containers on the default bridged network.
# We want to modify our architecture to contain traffic from the different sources.
# To separate user generated traffic from the application traffic.
# We create frontend network for user traffic and backend network for internal traffic
# We connect voting-app and result-app to frontend network
# And all components to backend network (voting-app, result-app, redis, db, worker)
# We should specify network we are going to use in network section
# Example (we leave out ports section ):
version: "2"
services:
	redis:
		image: redis
		networks:
			- back-end
	db:
		image: postgres:9.4
		networks:
			- back-end
	vote:
		image: voting-app
		ports: 
			- 5000:80
		networks:
		    - front-end
			- back-end
	result:
		image: result-app
		ports:
			- 5001:80
		networks:
		    - front-end
			- back-end
	worker:
		image: worker
		networks:
		    - back-end

networks:
	front-end:
	back-end:
