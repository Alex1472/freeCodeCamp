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