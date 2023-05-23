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

# DOCKER-COMPOSE UP
# To run docker compose use (for lates version):
docker-compose up
# For build images
docker-compose up --build
# You can specify container to rebuild. In this case docker will rebuild only it
# and it dependencies (depend_on)
docker-compose up --build node_app
# You can add the --no-deps flag to rebuild only image for this container without dependencies
docker-compose up --build node_app --no-deps
# Note, you don't need to down you containers when you update your yml file.
# Just execute up, one more time without down.
# You can specify specific container to up / update without its dependencies
docker-compose up --no-deps node_app

# DOCKER-COMPOSE DOWN
# To shut down all containers use:
docker-compose down
# Use the -v flag to remove assosiated volumes
docker-compose down -v
# For rebuild images 

# --FORCE-RECREATE
# You can force to recreate a running container with the flag --force-recreate
docker-compose up --force-recreate node_app
# To recreate just specified container without dependencies use flat --no-deps
docker-compose up --force-recreate --no-deps node_app

# --SCALE
# Note, you can run several instances of the container with the flag --scale
docker-compose up -d --scale node_app=2 # run 2 instances of the node_app container

# DOCKER-COMPOSE BUILD
# You can build all images in you docker compose file
docker-compose build 
# You can build just one image from you file
docker-compose build node_app

# DOCKER-COMPOSE PUSH
# You can push all images from you docker compose file to docker hub
docker-compose push
# Or the specified image
docker-compose push node_app

# DOCKER-COMPOSE PULL
# You can pull all images from docker compose file from docker hub
docker-compose pull
# Or the specified image
docker-compsoe pull node_app



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
# But depends_on just start containers in a specified order, but not ensure that they will initialized 
# in that order. For this implement custom logic by yourself. For example, try to connect 
# to db several times

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
	


# BUILD, VOLUMES, ENVIRONMENTS, ENV_FILE
# You can specify environment variables and volumes for service
# Build is an instuction how to build image, if it is not found. You should specify path to Dockerfile
# IT IS NOT REBUILD IMAGE ON CHANGE AUTOMATICALLY, it rebuild it only if image is not found.
# To force rebuild image use the --build key with docker compose up command. (docker-compose up --build)
version: '3'
services:
  node_app:
    build: . # how to build image, if it is not found
    ports:
      - '3000:3000'
    volumes:
      - .:/app # match Dockerfile directory to /app 
      - /app/node_modules # anonymous volume, save /app/node_modules folder from container into volume, hack
    environment:
      - PORT=3000 # specify environment variable
	# you can specify .env file instead of several environment variables
	# env_file:
    #   - ./.env - .env file in docker-compose.yml file folder
	
# To store db data we usually use name volumes
# To do this, declare it in the volumes section.
# Then use it in the service section
services:
  mongo:
	image: mongo
    volumes:
      - mongo-db:/data/db

volumes:
  mongo-db:

# For remove unnessasary volumes use docker volume prune
# It's better to run it, when you containers are started, so you remove
# only volumes that don't need your containers. 

# You can get value for environment variable of the container from invoronment
# variable of the machine
environment:
  - MONGO_USER=${MONGO_USER} # get value of the MONGO_USER from machine MONGO_USER env variable
  - MONGO_PASSWORD=${MONGO_PASSWORD}
  - SESSION_SECRET=${SESSION_SECRET}
# You can set env variable on the machine like this:
https://mkyong.com/linux/how-to-set-environment-variable-in-ubuntu/



# DEV, PROD MODE, COMMAND
# We want to have to configurations for dev and prod.
# We will have one Dockerfile and several docker-compose files:
# docker-compose.yml(base), docker-compose.dev.yml and docker-compose.prod.yml
# docker will merge setting from base and dev/prod files
# docker-compose.yml
version: '3'
services:
  node_app:
    build: .
    ports:
      - "3000:3000"
    environment:
      - PORT=3000

# docker-compose.dev.yml
# We need to specify different commands to run server for dev and prod
# We can override command to run with the command option in .yml
version: '3'
services:
  node_app:
    volumes:
      - .:/app
      - /app/node_modules
    environment:
      - NODE_ENV=development
    command: npm run dev

# docker-compose.prod.yml
version: '3'
services:
  node_app:
    environment:
      - NODE_ENV=production
    command: node index.js
	
# We also what that dev dependencies won't be installed in production mode.
# For this let's will use build time args https://docs.docker.com/engine/reference/builder/#arg
# First let's rewrite build section to specify not only path to dockerfile but build-time args
# docker-compose.prod.yml
services:
  node_app:
    build:
      context: .
      args:
        - NODE_ENV=production
# docker-compose.dev.yml
services:
  node_app:
    build:
      context: .
      args:
        - NODE_ENV=development
# Then in a dockerfile we need first declare this variable and then 
# use bash script to run corresponding command based on the variable value
# Dockerfile:
ARG NODE_ENV
RUN if [ "${NODE_ENV}" = "development" ]; \
    then npm install; \
    else npm install --only=production; \
    fi
	
# To run docker compose with several file use -f flag for every file
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up
# Use the same file for down
docker-compose -f docker-compose.yml -f docker-compose.dev.yml down



# RUN SPECIFIED CONTAINER
# You can run just specified container, not all containers. Specify this container
# Docker-compose will run the container and its dependencies(depends_on) 
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up node_app
# You can run exactly the specified container, with no dependencies, use the --no-devs flat for this
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up --no-deps node_app