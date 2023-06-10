# We have a script project-mercury.sh located at /user/bin
# - It has to be started in the background as a service
# - Enabled to start during the boot
# - This script starts python Django application. The application depends on 
# the Postgres Database. So the postgres service should be up and running
# before the python application.
# - The service should make use of the project-mercury user/bin
# - If the application fails for any reason, it should restart automatically
# - Restart interval 10 seconds
# - Log service event(start, stop, failure)
# - Load when booting the graphical mode



# START SERVICE
# To run the script in the background, we have to define it as a service.
# We need to create a SERVICE UNIT FILE project-mercury.service needs to be
# created under /etc/systemd/system/project-mercury.service
# First define a command to run it in the background
# We use ExecStart to run a command or an application
# project-mercury.service:
[Service]
ExecStart= /bin/bash /user/bin/project-mercury.sh

# To start service use systemctl start. You would have to use sudo at this
# time as the service is set to run as the root user by default.
systemctl start project-mercury.service

# To check the status of the service user systemctl status command
systemctl status project-mercury.service

# To stop the service use 
systemctl stop project-mercury.service



# RUN WITH BOOT
# We need add another section called install add specify systemd target
# project-mercury.service:
[Service]
ExecStart= /bin/bash /user/bin/project-mercury.sh

[Install]
WantedBy graphical.target



# ADD THE SERVICE ACCOUNT, RESTART BEHAVIOR, LOGGING
# Let's add service account to be used to start the service instead of root
# (which is default)
# We need to add the User direction in the service section
[Service]
ExecStart= /bin/bash /user/bin/project-mercury.sh
User=project-mercury

[Install]
WantedBy graphical.target

# To restart script on failure add the Restart directive
# To set the time in seconds to wait before the system attempts to restart it
# user RestartSec directive
[Service]
ExecStart= /bin/bash /user/bin/project-mercury.sh
User=project-mercury
Restart=on-failure # Restart=always to restart for any reason
RestartSec=10

# All service events are automatically logged.



# DEPENDENCY
# Since the python django application depends on the postgres service
# We can define a dependency that makes sure that our new service is started
# only after the postgres database is ready.
# We need to add another section to the service file called Unit
[Unit]
Description=Some descr # not mandatory
Documentation=Some docs # not mandatory
After=postgressql.service # service depends on

# For the system to detect the changes we made in the file, run the 
# systemctl deamon-reload command
systemctl daemon-reload



# SYSTEMCTL
# systemctl is the main command used to manage services on a systemd-managed 
# server.
systemctl start docker # start service
systemctl stop docker # stop service
systemctl restart docker # restart service
# Use systemctl reload to reload the service without interrupting normal
# functionality
systemctl reload docker
# to enable the service and make it persistent across reboot use 
# systemctl
systemctl enable docker
# Use if you want to disable the service at boot
systemctl disable docker
# Get information about the state of the service
systemctl status docker
# States: active (running), inactive(dead), activating, deactivating, failed

# To reload system manager configuration and makes the systemd awore
# of the changes after changing a service unit file (.service) use
systemctl daemon-reload
# A running service whose unit file has been updated on disk can only be 
# restarted after running the systemctl daemon-reload command.

# You can also make changed to unit file using the systemctl edit command
# Chages will apply immediately without deamon-reload
systemctl edit docker.service --full

# View current target(runlevel)
systemctl get-default
# Change to a different target
systemctl set-default multi-user.target

# List all the units systemd has loaded or attempted to load use
systemctl list-units --all
# To show only active units
systemctl list-units

# Show services and their ports
netstat -ltnp  



# JOURNALCTL
# It checks the journal or logs entries from all parts of the system.
# Print all the log entries from the oldest to the newest.
journalctl
# To see the logs from the current boot run it with the -b option
journalctl -b
# To see the log entries for a specific unit, run service name with 
# the -u flat
journalctl -u docker.service
# The data provided by the log entries for a service file generally provide
# the reason why a service unit is in an inactive or failed state.
# IT'S GREAT FOR DEBUGGING.