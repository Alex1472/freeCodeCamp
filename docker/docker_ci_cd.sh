# Docker support for majority of build system: Jenkins, Bamboo, Travis, ...
# How it works:
# Project has dockerfile, once new code pushed into the repository
# Jenkins build image with dockerfile (it may tag it with a new build number)
# That it can run tests with this image
# It tests are successful the image can be pushed to the image repository(registry)
# The image repository can be integrated with container hosted platform, like AWS
# to automated deploy your application 



# To deliver you changed to the production server you can use this scheme:
# 1) Create image to push on docker hub with docker image tag command
# 2) Push your image on docker hub
# 3) Make nessassary changes in your code
# 4) Build new image with docker-compose build command
# 5) Push new image on docker hub with the docker-compose push command
# 6) Pull new image on the production server with the docker-compose pull command
# 7) Recreate changed containers on the production server with docker-compose up command
# 8) Repeat steps 3-7



# To do not pull images and rerun containers on the production server
# you can use WATCHTOWER. It's a container that looks for changes in image of the 
# specified container. It pulls new images and reruns container if need.
# To run use command 
# WATCHTOWER_TRACE - should print logs
# WATCHTOWER_POLL_INTERVAL - time interval to check new image
# app-node_app-1 container to check
docker run -d --name watchtower -e WATCHTOWER_TRACE=true -e WATCHTOWER_DEBUG=TRUE 
-e WATCHTOWER_POLL_INTERVAL=50 -v /var/run/docker.sock:/var/run/docker.sock containrrr/watchtower app-node_app-1
# Now just push new image on docker hub and watchtower will pull it and rerun container