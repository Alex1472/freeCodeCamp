# PING
# Let's look at an example:
# We have two computers, A and B, both are part of the same network.
# The have ip addresses 192.168.1.10 and 192.168.1.11
# You can use ping command to check the connectivity between the two servers.
# Run ping command on one computer with ip of the other. By default ping
# will send requests contineously. Use -c flag to specify number of requests.
ping -c 1 192.169.1.11
ping -c 1 google.com



# /ETC/HOSTS
# We want to give another server a name, and use it when send a requests
# We want to tell system A that the system B had IP address 192.169.1.11
# has a name db. When I say db I mean the IP 192.168.1.11
# You can do that by adding an entry into the /etc/hosts file on system A.
# /etc/hosts:
8.8.8.8 db # The ip 8.8.8.8 is a host named db.
# Note, hostname of B isn't need to be db, we just say to A that db has 
# ip 8.8.8.8
# Example, we can even fool system A into believing that System B is google.
# Add into /etc/hosts 
192.168.1.11 www.google.com
# Note, you can have several name pointing to the same host (db and google).



# NAME RESOLUTION
# Every time we reference another host by its name from Host A through a 
# ping command of the SSH command or through any of the applications or tools
# within the system, it looks into the /etc/hosts file to find out the IP
# address of that host. Translating hostname to IP address this way is known
# as NAME RESOLUTION.
ping db
ssh db
curl http://www.google.com



# DNS SERVER
# Within a small network of few systems, you can easily get away with
# the entries in the /etc/hosts file. On each system we should specify 
# which are the other systems.
# In big networks it's better to move all entries into a single server that
# will manage it centrally. We call that our DNS server. Then, we point all
# hosts to look up that sever if they need to resolve a hostname to an
# IP address instead of its own /etc/hosts file.
# How to do this?
# Every host has a DNS resolution configuration file at /etc/resolv.conf
# You add an entry into it specifying the address of the DNS server.
# /etc/resolv.conf
nameserver 191.168.1.100 # 191.168.1.100 - ip of the dns server
# Every time a host comes across a hostname it doesn't know about, 
# it lloks it up from the DNS server.



# DNS SERVER AND /ETC/HOSTS. /ETC/NSSWITCH.CONF
# Yoy can use a dbs server and /etc/host file.
# What if you have an entry in both dns server and hosts file?
# The host first looks in the local /etc/hosts file ant then looks at the
# nameserver. If it finds the entry in the local hosts file, it uses that.
# If not, in loolks for that host in the DNS server. 
# That order can be changed. It defined by an entry in the file 
# /etc/nsswitch.conf
# nsswitch.conf
# files refers to /etc/hosts file, DNS to DNS server
hosts:         files dns


# PUBLIC DNS SERVER
# What if you try to ping a server that is not on either list(in hosts file 
# and local dns server)?
ping www.facebook.com
# You can add another entry into your hosts resolv.conf to point to
# a nameserver that knows Facebook
nameserver 192.168.1.100 
nameserver 8.8.8.8 # public nameserver available on the internet hosted by
# Google that knows about all websites on the internet.
# Note, you should add all nameservers on all hosts in your network with
# this approach.

# You can configure the DNS server itself to forward any unknown host names
# to the public nameserver on the internet. The you be able to ping external 
# sites such as facebook.com 



# DOMAIN NAMES
# We pinged Facebook as www.facebook.com. How does that work?
# What is this name with a www and .com at the end?
# It is called domain name.
# It is how IPs translate to names that we can remember on the public internet.
# Just like how we did for out hosts.
# The last portion of the domain name, the .com, .net, .edu, .org, etc/host
# are the top level domains. They represent the intent of the websites
# .com - for commercial or general perpose
# .net - for network or general purpose
# .edu - for education organizations
# .org - for nonprofit organizations 

# Let's look at www.google.com
# .com - top-level domain
# google - domain name assigned to Google
# www - sub domain. The sub domains help in further grouping things together
# under Google
# Several google subdomains:
# mail - mail.google.com, 
# drive - drive.google.com, 
# www - www.google.com, 
# maps - maps.google.com, 
# apps - apps.google.com
# You can further divide each of these into as many sub domains based on
# your needs. So we have a tree structure format.

# RESOLVING DOMAIN NAME
# When you try to reach any of these domain names, for example apps.google.com
# from within your organization your request first hits Your oraganization's
# internal DNS server. It doesn't know who apps or Google is, so it forwards
# your request to the internet. On the internet, the IP address of the server
# serving apps.google.com may be resolved with the help of multiple DNS 
# servers. 
# A root DNS server looks at your request and point you to a DNS server
# serving .com . A .com DNS server looks at your request and forwards you
# to Google and Google's DNS server provides you the IP of the server
# serving applications.
# apps.google.com -> Org DNS -> Root DNS -> .com DNS -> Google DNS ->
# -> 216.58.221.78
# In order to speed up all future results, your organization's DNS server
# may choose to cache this IP for a period of time typically a few seconds
# up to a few minutes. That way, it doesn't have to go through the whole
# process again.

# Your organization can have a similar structure too. It can have several
# subdomains for each purpose. For example 
# www.mycompany.com - for an external-facing website
# mail.mycompany.com - for accessing your organization's mail
# drive.mycompany.com - for accessing storage
# pay.mycompany.com - for accessing the payroll application
# hr.mycompany.com - for accessing HR application
# All of these are configured in your organization's internal DNS server.


# /ETC/RESOLV.CONF 
# In /etc/resolv.conf we configured the DNS server to be used for our host.
# We were able to resolve servers in our organization with just their names,
# like the web server
# On our dns server
192.168.1.10 web
192.168.1.11 db
192.168.1.12 nfs
...
# But with more standard domain you can't use just web, because on your dns
# server. You should ping web.mycompany.com for example
192.168.1.10 web.mycompany.com
192.168.1.11 db.mycompany.com
192.168.1.12 nfs.mycompany.com
# If someone outside our company wants to access our web sever, he would
# have to use web.mycompany.com. But within your company, you want to simply 
# address the web server by its first name, web.
# What do you do to configure web to resolve web.mycompany.com?
# You should make an entry into your hosts /etc/resolv.conf file and 
# specify the doamin name you want to append
# /etc/resolve.conf
nameserver        192.168.1.100
search            mycompany.com
# Now, if you try to ping web it will actually tries, web.mycompany.com
# Host is intellegent enough to exclude the search domain if you specify 
# the domain in your query like this.
ping web.mycompany.com
# You can provide several search domains, it will search both:
search     mycompany.com prod.mycompany.com 



# RECORD TYPES 
# How are the records stored in the DNS server?
# It stores IP to hostname, it known as A record:
A web-server 192.168.1.1
# Stores IPv6 to hostnames in known as quad A records:
AAAA web-server 2001:0db8:...
# Mapping one name to another name is called CNAME records:
# You can have multiple aliases for the same application, like a food delivery
# service. It can read as food, eat and hungry
CNAME food.web-server    eat.web-server, hungry.web-server
# There are other mappings



# OTHER TOOLS TO PING
# NSLOOKUP
# You can use nslookup to query a hostname from a DNS server
# Note, it is not considered the entries in the local /etc/hosts
nslookup google.com

Server:		127.0.0.53
Address:	127.0.0.53#53

Non-authoritative answer:
Name:	google.com
Address: 172.217.169.174
Name:	google.com
Address: 2a00:1450:4017:80e::200e

# DIG
# It is another tool to test DNS name resolution
# It returns more detauls in a similar form as it stored on the server.



# SWITCHING
# We have to computers A and B, laptops, desktops, VM on the cloud, wherever
# How does system A reach B?
# We connect them to a switch and the switch creates a network containing
# the two systems. To connect them to a switch, we need an interface
# on each host, physical or virtual, depending on the host.
# To see the inteface for the host we use the IP link command.









