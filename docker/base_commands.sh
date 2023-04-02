# COMMANDS
# docker version - to get current docker version
docker version


# docker run - run docker container from an image. If image is not present, it tries to dowload it from docker hub.
# You should specify IMAGE name
# Note, when we run a container from image, on EVERY command new container will be created
docker run ngnix # run ngnix on you docker host, 

# If you want to stop container use another terminal for this and command docker stop name/id

# Run container and name it as you want
docker run --name container_name image_name
docker run --name webapp ngnix

# Note, container is meant to host a process as server or computation.
# If container ended computation or has no process to run it stops.
# Thats why docker run ubuntu stops immediatly. It has no process to run, so it stops immediatly.
docker run centos # returns to terminal immediatly, because centos is an image of os. We don't run anything there.

# Let's run bash there, and attach to this bash
docker run -it centos bash # it - interactive, now we inside centos container

# Now you can run command in this os, for example
cat /etc/*release*

# Use exit to exit from this terminal
exit

# Also you can append command to run command
docker run ubuntu cat /etc/*release*
# run centos and sleep 20 second. Container will be running this time.
docker run centos sleep 20


# docker ps - list all running containers
docker ps
# docker ps -a - list all containers
# Note, on every docker run command will be created new container.
# With option -a all this containers (and stoped) will be listed
docker ps -a


# docker stop - stop container
# You should specify container name, id or some first symbols of the id
docker stop gifted-noither
docker stop 1hejre2jfd3
docker stop 1he


# docker rm - remove container. Specify name, id, or part of the id
# You can specify several containers
docker rm gifted-noither
docker rm 1hejre2jfd3
docker rm 1he d3i kso # remove several containers
# docker container prune - remove all stoped containers
docker container prune


# docker images - show all downloaded images
docker images


# docker rmi - remove image
# Note, you must remove all container for this image first
docker rmi nginx


# docker pull - download image
docker pull ngnix

# If we what to execute command in docker container (running), use exec command
docker exec container_name/id command
docker exec 32o cat /etc/hosts 


# By default containers run on a foreground, in attach mode
# So, if you run container with a server, in your terminal you will so output from this server
# But can't do anything else. 
# If you want to stop container use another terminal for this and command docker stop container_name/id
docker run image_name

# If you what to run container on a background use command docker run -d
# This runs command in a detach mode, so you can enter another command in a terminal
docker run -d image_name

# To attach to a running container use docker attach command
docker attach container_name