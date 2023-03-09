# We have a problem that different components in out stack (node.js, express, mondo.db, redis, ...) can depend 
# on different versions of libraries, os, hardware.
# Also it's hard to install all components for a new developer.

# In docker we run every component in a SEPARATE	container with it's own dependencies.
# Container is a completly isolated environment. They can have their own processes or services, their own network interface
# But the share the same os kernal.

# OS consists of os kernal and software. OS kernal is the same for Ubuntu, Fedora, CentOS ...
# It's a linux kernal. Software makes the difference. Software consists of user interface, drivers, compilers, file managers and so on.
# So we have a linux kernal shared across all OSs and some custom software differentiate.
# So we can run container bases on another os, if they have the same kernal.

# But windows and linux has different kernals. We can't run windows based container on a Docker host with linux on it.
# But you can run linux container on linux virtual machine on windows. It's linux container on Linux virtual machine on Windows.

# THE MAIN PURPOSE OF DOCKER IS TO CONTAINERIZE APPLICATIONS AND TO SHIP THEM AND TO RUN THEM ANYWHERE ANY TIME 

# Docker use one os with different container that is with it's own dependencies.
# In case of vm, hyperviser runs virtual machine with it's own os that with it's own dependencies.

# There are lots of containerized versions of applications. So most applications available as docker container
# in a public docker repository called Docker Hub or Docker store. For example you can find an images for node.js,
# express.js and so on.
# With docker on you os, you can run application just by running a docker run command.

# DIFFERENCE BETWEEN IMAGES AND CONTAINERS.
# IMAGE is a package or a template. It is used to create one or more containers.
# CONTAINER is a running instance of an image with it's own dependencies and processes.

# So for you application, you can provide docker file for setting up the environment for app.

#Get docker images: https://hub.docker.com/