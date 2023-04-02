# You can show all networks with command 
docker network ls


# By default docker create at least three networks: bridge, none, host

# BRIDGE - default network a container gets attach to.
# Network has ip:
172.17.0.1 
# The containers get usually like this:
172.17.0.2
172.17.0.3
...
# Containers can access each other using this ips, you also can get containers with
# this ip on the host

# To access this container outside, you should port of the container to port on docker host
docker run -d -p 8080:8080 jenkins/jenkins # now you can access it as localhost:8080


# HOST
# To get access to the container you can assosiate it to host network.
docker run --network=host jenkins/jenkins
# Now you can get access to the container outside, to containers can't listen the same port


# NONE
# With none network container doesn't attack to any network.
# And do not have access to any network or container



# To show all networks use 
docker network ls
# To show containers connected to the network use
docker network inspect network_id/name
# You can create your own custom network
docker network create --driver=bridge my-network
# You can specify subnet for the network
docker network create --driver=bridge --subnet=182.18.0.0/16 my-network
# Then you can specify this network to assosiate container to it
docker run -d --network my-network jenkins/jenkins
# You can configure gateway for network
docker network create --driver bridge --subnet=182.18.0.1/24 --gateway 182.18.0.1  wp-mysql-network
# You can show to which network the container attached with command, in networks section
docker inspect container_id/name
# To remove network use docker network rm
docker network rm my-network



# Containers in the same network can reach each other using their name.
# So in your application you can use internal container ip address or container name
# That because docker has built-in DNS server.
# Note, built-in DNS server always runs at address 127.0.0.11



# How docker implement networking?
# Docker create separate namespace for each container.
# It then uses virtual internet pairs to connect containers together.


