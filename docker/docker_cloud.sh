# Docker cloud is Docker's own cloud based container hosting platform
# To use cluster we should:
# 1) Build images
# 2) Have several machines
# 3) Configure docker on them
# 4) Configure Swarm 
# 5) We should manage nodes
# 6) We should manage services

# We cloud platforms we should just develop our application, craete docker 
# images and create a docker stack file
# Docker cloud provisions the necassary number of hosts, sets up docker
# and docker swarm on those hosts, sets up networking, creates services
# and runs containers as specified in the stack file.
# Docker cloud however doesn't host the infrastructure on its own. So you 
# must link one of the cloud providers to its backend such as AWS
# And docker cloud provisions resources on those cloud providers to host 
# our application
# Similary, you can also link source providers such as Github or BitBucket to integrate
# docker cloud with a CICD pipeline
# Once you link your Github account to docker cloud, every time a developer
# pushes the code, the docker cloud internal system reads the code and builds the
# docker image
# Once the image is built successfully, it is then pushed to docker hub
# where it is made available for the public