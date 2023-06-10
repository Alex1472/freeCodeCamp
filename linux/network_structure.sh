# SHOW IPS OF THE HOST ip addr command
ip addr

# SWITCH, CONNECT INTERFACES
# We have 2 computers A and B. How does system A reach B?
# WE CONNECT THEM TO A SWITCH AND SWITCH CREATES A NETWORK containing
# the two systems. To connect them to a switch we need an INTERFACE on each 
# host, physical or virtual, depending on the host. 
# To see the interfaces for the host we use the IP link command.
ip link
# On A:
eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP
mode DEFAULT group default qlen 1000
# On B:
eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc fq_codel state UP
mode DEFAULT group default qlen 1000
# In this case, we look at the interface named eth0, that we will be using
# to connect to the switch.

# CONNECTING TO SWITCH
# Let's assume we have a network with the address 192.168.1.0(switch),
# We then assign the systems with IP addresses on the same network.
# For this, we use the command ip addr(changes are only valid till a system restart).
# To persist changes you must set them in the /etc/netwok/interfaces file.
# On A:
ip addr add 192.168.1.19/24 dev eth0
# On B:
ip addr add 192.168.1.11/24 dev eth0
# Once the links are up and the IP addresses are assigned, the cumputers can
# now communicate with each other through the switch. The switch can only 
# enable communication within a network. It can receive packets from a host
# on the network and deliver it to other systems within the same network.
# Use ip addr to see the IP addresses assigned to those interfaces
ip addr



# SEVERAL NETWORKS, ROUTER
# Say we have another network containing systems C and D
# First network:
# A:192.168.1.10 --- Switch:192.168.1.0 --- B:192.168.1.11
# Second network:
# C:192.168.2.10 --- Switch:192.168.2.0 --- D:192.168.2.11

# How does the system in one network reach a system in the other?
# How can B reach C?
# We need a router. A router helps connect two networks together.
# Think of it as another server with many network ports.
# Since it connects to the two separate networks, IT GETS TWO IPS ASSIGNED,
# one on each network. In first network we assigned it an 
# IP address 192.168.1.1. In second network - 192.168.2.1
# So we have a router connected to the two networks, that can enable communication
# between them.
# A:192.168.1.10 --- Switch:192.168.1.0 --- B:192.168.1.11
#                          |
#                          |192.168.1.1
#                        router
#                          |191.168.2.1
#                          |
# C:192.168.2.10 --- Switch:192.168.2.0 --- D:192.168.2.11

# GATEWAY OR ROUTE
# When system B tries to send a packet to system C, how does it know where
# the router is on the network to send the packet through?
# The router just an another device in the network. There could be many other
# such devices.
# That's where we configure the systems with a GATEWAY or a ROUTE.
# If the network was a room, the gateway is a door to the outside world to
# other networks or to the internet. The systems need to know where the door
# is to go through that.
# To see the existing routing configuration on a system, run 
# the route(or ip route) command.
route # It displays the kernel's routing table.

# We need to configute the gateway on system B to reach the systems on 
# another network. Use the ip route add command
ip route add 192.168.2.0/24 via 192.168.1.1 # get network 192.168.2.0/24 
# with gateway 192.168.1.1
# Now it should be in routing table
route
Kernel IP routing table
Destination     Gateway       Genmask       Flags    Metric  Ref   Use Iface
192.168.2.0     192.169.1.1   255.255.255.0 UG       0       0     0   eth0  
# NOTE, THIS HAS TO BE CONFIGURED ON ALL SYSTEMS.
# If the system C sends a packet to system B then you need to add a route
# on system C's routing table
ip route add 192.168.1.0/24 via 192.168.2.1



# ACCESS TO THE INTERNET
# Say, we need asscess to Google at 172,217.194.0 network on the internet.
# So you connect the router to the internet. Then add a new route in your
# routing table to route all traffic to the network 172.217.194.0
# through your router.
ip route add 172.217.194.0/24 via 192.168.2.1

# There are som many different sites on different networks on the internet
# Instead o adding a routing table entry with the same routers IP address
# for each of those networks, youncan simply say, for any network
# that you don't know a route to(not in local network), 
# use this router as default gateway.
# This way, any request to any netweork outside of your existing network
# goes to this paticular router
ip route add default via 192.168.2.1 # send to 192.168.2.1 if not 
# in local network
# Note, instead of default you can use 0.0.0.0, it means any IP destination.
# A 0.0.0.0 entry in the gateway field indicates that you don't need a gateway
Destination     Gateway       Genmask       Flags    Metric  Ref   Use Iface
# For any unknow destination use 192.168.2.1 gateway
0.0.0.0         192.168.2.1   255.255.255.0 UG       0       0     0   eth0
# To access 192.168.2.0 you don't need a gateway, it is in the same network
192.168.2.0     0.0.0.0       255.255.255.0 UG       0       0     0   eth0



# MULTIPLE ROUTERS
# Say, you have multiple routers in your network, one for the internet
# another for internel networks, then you will need to have two separate
# entries for each network. 
# For internal private network 
192.168.1.0     192.168.2.2       255.255.255.0 UG    0    0    0   eth0
# Default gateway for other networks
default         192.168.2.1       255.255.255.0 UG    0    0    0   eth0
# IF YOU ARE HAVING ISSUES READHING THE INTERNET FROM YOUR SYSTEMS,
# THIS ROUTING TABLE AND THE DEFAULT GATEWAY CONFIGURATION IS A GOOD PLACE TO 
# LOOK AT



# TROUBLESHOOTING
# We can access server with a variety of reasons:
# - could be an issue with your local interface not being connected 
# to the network
# - could be your host not resolving the IP address of the host name
# you were trying to connect to
# - could be an issue with the route to the server
# - could be an issue with the server itself

# Check our host ip connectivity, ensure the primary interface is up.
ip link
# Check if we can resolve the host name to an IP address. Run nslookup 
nslookup caleston-repo-01

# Try to ping remote server to get a response
ping caleston-repo-01 # If we don't get response the issue lies between our
# system and the server.

# Run treceroute destination ip command. This will show us the number of hops
# or devices between the source and the server.
1 <1ms <1ms 192.168.1.1 
2 <2ms <1ms 192.168.2.1
3 *    *    Request timed out
# There is two routers between source and server and they are ok.
# Request timed out between the second router and the server.

# Look at our server
# Check if the HTTP process is running on port 80
# The netstat command can be used to  print the information of network
# connections, routing tables and several other network statistics.
# We look if the port 80 is listening on the server
netstat -n | grep 80 | grep -a LISTEN # if it is listening => server is up

# We can check the interfaces on the server
ip link # If the interface is down, it is a problem
# To up
ip link set dev interface_name up


