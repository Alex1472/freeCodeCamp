# In a real world production environment that has many different clients
# in many different servers, all connected through a large network
# with many different switches and routers, it is important that we implement
# network security to allow or restrict access to the various sevices.
# That allows us to control network connectivity, such as allowing SSH access
# from a specific IP or a network range.
# We can apply such security NETWORK-WIDE using external firewalls or 
# appliances such as Cisco ASA. Using these appliences you can define rules that
# will control any traffic flowing through the network.
# Or an alternate option is to apply these rules at an INDIVIDUAL SERVER level
# using tools such as IPTables and FirewallD in Linux-based systems and 
# Firewalls in windows servers.

# IPTABLES
# We will implement local IPtable rules. These roles will allow us to filter
# network traffic within the Linux operation system.
# Assume we have:
# 1) Client laptop with IP 172.15.238.187
# 2) Application server DEVAPP01 with IP 172.16.238.10
# 3) Database server DEVDB01 with IP 172.16.238.11
# 4) Software Repo SErver caleston-repo-01 with IP 172.16.238.15
# - The application server runs a web application on port 80
# - The database server runs db on port 5432
# We want to improve security.
# - from client laptop you should be allowed to ssh to the app server, hence
# - port 80 on the server should be accessable from the client laptop but 
# blocked from all other servers.
# - the app server should be able to connect to the 5432 port on the DB
# - db servers should accepts connections only from the app server
# - the application server should also have HTTP access to the software repo
# - we want to block outgoing internet access on the app server.



# To filter data traffic on the server, we will make use of IPtables
# ON UBUNTU YOU SHOULD INSTALL IPTABLES

# To list the default rules configured in the system run iptables -L
# There is 3 types of rules or chains configured by default: INPUT, FORWARD 
# AND OUTPUT.
# The input chain is applicable to network traffic comming into the system
# The output chain is responsible for connections initiated by this server 
# to other systems.
# The forward chain is typically used in network routers where the data is
# forwarded to toher devices in the network. (It is not commonly used).
# Since we didn't add any rules, the default policy is set to accept.
# This means all traffic is allowed in and out of the system.
iptables -L # Output
Chaing INPUT (policy ACCEPT)
target       prot opt source              destination
Chain FORWARD (policy ACCEPT)
target       prot opt source              destination
Chain OUTPUT (policy ACCEPT)
target       prot opt source              destination

# Why chain?
# That's because each chain can have multiple rules within it. It's a chain
# of rules. Each rule performs a check and accepts or drop the packet.
# The rule can accept or drop the packet if it is satisfied the role condition.
# Or forward packet to the next rule. If the packet is accepted or dropped
# by rule other rule won't be checked.
# If the packet isn't satisfied any rule it, by default policy, will be accepted.
# Example:
# Accept if src is client 01
# Drop if src is client 02
# Accept if src is client 01
# Accept if src is client 01
# Drop if src is any
# Note, also we can confige port and protocol in rule condition.

# There are some flag for the command iptables
# -A - Add Rule
# -p - protocol
# -s - source, IP address of the client or HOSTNAME
# -d - description
# --dport - destination port
# -j - action to take

# Let's add rules for server
# Now, let's add an incoming or an input rule on the dev server that will
# allow SSH connection from the client laptop
iptables -A INPUT -p tcp -s 172.16.238.187 --dport 22 -j ACCEPT
# Let's add rule, to reject all other ips.
iptables -A INPUT -p tcp --dport 22 -j DROP
# NOTE, RULES ARE APPLIED IN ORDER TO ADDITION, OR AS THEY LISTED IN COMMAND
# IPTABLES -L. So if we will add new role after the last rule, it won't be 
# applied anywhen.
# Let's add output rules on the server
iptables -A OUTPUT -p tcp -d 172.16.238.11 --dport 5432 -j ACCEPT # connection
# to db 
iptables -A OUTPUT -p tcp -d 172.16.238.15 --dport 80 -j ACCEPT # connection
# to repo
# Block other outgoing connectiong, port 80/443 (HTTP/HTTPS)
iptables -A OUTPUT -p tcp --dport 433 -j DROP
iptables -A OUTPUT -p tcp --dport 80 -j DROP
# Allow incoming on port 80, from client
iptables -A INPUT -p tcp -s 172.16.238.187 --dport 80 -j ACCEPT
# To add another rule at the top of the list use -I option instead of -A
iptables -I OUTPUT -p tcp -d 172.16.238.100 --dport 443 -j ACCEPT
# To delete a rule use -D flag and specify rule position to delete
iptables -D OUTPUT 5 # Delete fifth r543ule

# Let's setup db server
iptables -A INPUT -p tcp -s 172.16.238.10 --dport 5432 -j ACCEPT # connection
# from the app
iptables -A INPUT -p tcp --dport 5432 -j DROP # reject any other connection


# What happen, if the session is establish between the app and db.
# DO WE NEED INPUT RULE TO ACCEPT RETURNING TRAFFIC ON APP FROM DB?
# In this case - NO.
# Data returned from db to app on a RANDOM tcp port. But we accept all 
# traffic on the app server except ssh port.



# CRON JOB
# With a Cron Job, a user can specify any date, time or frequency to schedule
# a task. The system will execute the task at the set time.
# This functionality is enabled by the CROND SERVICE, that runs in the 
# background.
# To schedule the job, run the command crontab -e. It will open up the
# the crontab in the VI editor. Add a configuration for our job to be
# scheduled
crontab -e

# First 5 fields are used to specify the exact schedurel to run the task.
# The sixth field is the command to run.
# Note, do not use sudo with crontab command or you will find that the job
# will be scheduled for the root user.
# To list all tasks for current user:
crontab -l
# m - minutes, h - hours, dom - day, mon - month, dow - weedday, * - any value
# m h dom mon dow    command
  0 21 *   *   *     uptime >> /tmp/system-report.txt
# To schedule the job to run on February 19, at 10 minutes past 8:00, use
10 8 19 2 * uptime
# To be scheduled only if the 19th of February is a Monday - add weekday
10 8 19 2 1 uptime
# To run task every day use:
10 8 * * * uptime
# To run task every minute use:
* * * * * uptime
# To run command every 2 minutes use step values with a / like this.
*/2 * * * * uptime # run every 2 minutes

# To inspect was the task run successfully, you can see 
# the /var/log/syslog file


