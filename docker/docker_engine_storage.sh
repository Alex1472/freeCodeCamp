# NAMESPACING
# Docker engine is just a host with docker installed on it.
# When you install docker you actually install 3 parts
#    1. Docker daemon - background process that manages docker objects such
#       as images, containers, volumes and networks
#    2. REST API Server - API interface that can talk to the daemon and provide instuctions
#    3. Docker CLI - command line interface is used to perform different actions.
#       It uses REST API to enteract with Docker daemon
#       Note, Docker CLI can be on another host.
#       To specify remote server use -H option
#       For example:
        docker -H 10.123.2.1:2375 run nginx # run nginx container on remote docker host 
		

# Docker uses namespaces to to isolate workspace.
# Process IDs, Networks, InterProcess communication, mounts, ... 
# are created in their own namespaces thereby providing isolatioin
# between containers


# In linux every process is created by first process with PID 1.
# When system boots up completly we have a handfull of processes 
# With their own PIDs.
# When we run container its like an child system within current system.
# The child system need to think that it is an indemendent system. 
# And it has their processes including root process with PID 1.
# In fact, processes running in the container are running on the underlying host.
# Processes can have the same PID.
# With process id namespaces each process can have multiple process id assosiated with it.
# So processes in the container just set of processes in the host with another ids.
# So process has one id for the host another id for the container.

# You can look process id in container exec command(execute in container):
docker exec cont_id ps -eaf
# And id on this process on host:
ps -eaf | grep something_from_cmd_of_process # look in information from process in container, grep for filtering


# All container and docker host shared resources(memory, cpu).
# By default there is no restrictions how much of a resource a container
# You can restrict amount of resources a container can use
# You can specify this in docker run command
docker run --cpus=.5 ubuntu # max use 50% of cpu
docker run --memory=100m ubuntu # use max 100mb




# 
# Docker stores data in /var/lib/docker
# There are folders aufs, images, containers, volumes here.
# Here docker stores container and images.

# Let's recall, that docker creates images with layer architecture
# Every line in dockerfile - new layer.
# Docker reuse layer for another image if this layers are the same.

# When docker has created an image, created layers becomes readonly. You can't change them.
# When you run container docker create new writable container layer on top of the container layers.
# Container layer is used to store the data crated by the container(logs, temp files).
# This layer (and all its data) destroyed when the container destroyed.

# If in container create a file it will be created in the container layer.
# If you want to change file in the container in container layer will be created copy of
# this file, and it will be changed. is called COPY-ON-WRITE
# When container is stopped all data will be removed.

# If you want to see how image is created by layers (from Dockerfile) use command:
docker history image_id
# Note, if you specify os (FROM ubuntu), first several layers comes from this instruction


# VOLUME MOUNTING
# If we want to save data from this container, we can use volumes mechanism
# First create volume in /var/lib/docker/volumes folders
docker volume create data_volume
# Then when run container map this volume folder to folder inside continer.
# This folder will be saved in a volume folder
docker run -v data_volume:/var/lib/mysql -e MYSQL_ALLOW_EMPTY_PASSWORD=true mysql
# Note, if you didn't create a volume and just run container with unexisting, volume will be crated automatically.

# BIND MOUNTING
# You can mount another folder instead of volume, in this case you should specify all path to folder
docker run -v /data/mysql:/var/lib/mysql mysql
# Note, by default volumes is a two-way road, so container can change the source file
# To avoid this, you can use read-only volumes with :ro in the end.
# With this container only can read files, not update, create, delete.
docker run -d --name node-app -p 3000:3000 -v D:\DifferentReps\DockerProjects\LearnDockerProject\:/app:ro node-app-image

# Using -v is an old way to mount folder, new way is using --mounts
docker run --mount type=bind,source=/data/mysql,target=/var/lib/mysql -e MYSQL_ALLOW_EMPTY_PASSWORD=true mysql


# Storage driver is responsible for all this operations:
# mainainging layers, moving files across layers, ...
# Docker uses storage driver to enable layers architecture.
# There are several storage drivers AUFS, ZFS, BTRFS, ...
# The choise of storage driver depends on the OP.
# You can look what storage driver used the command
docker info # and see storage driver section
# In folder var/lib/docker/storage_driver_name you can find data

# for aufs storage driver there are three folders: diffs, layers, mnt
# diff - stores actual data of layers
# in diff folder in nested folders layers are stored
# layers - stores information about how layers stacked
# mnt - stores information about mount points



# Note, size of images is size that image takes
# It's not exactly consume space on the disk
# To show total disk usage of docker use command:
docker system df
# If you want to see detailed information use flag -v:



version: "3"

services:
    redis:
        image: redis
        
    db:
        image: postgres:9.4
        
    vote:
        image: dockersamples/examplevotingapp_vote
        ports:
            - 5000:80
            
    worker:
        image: dockersamples/examplevotingapp_worker
        
    result:
        image: dockersamples/examplevotingapp_result
        ports:
            - 5001:80