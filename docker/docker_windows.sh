# We know, that we can't run linux docker container on windows and windows container
# on linux, because containers use os kernal.
# So we should install virtual machine in windows with linux and run container for linux there

# There are several options to run linux container on windows
# 1) Install virtual box, there install linux virtual machine and on linux install docker
# 2) Docker provides us with set of tools for options 1 named DOCKER TOOLBOX (virtual box, docker engine, docker machine
#    docker compose, kitematix gui)
# 3) DOCKER FOR WINDOWS 
#    It uses Microsoft Hyper-V instead of virtual box to install linux virtual machine
#    As earlier we run linux containers on linux virtual machine
# 4) Also with DOCKER FOR WINDOWS you can create windows bases images and run container on windows
#    There are two types of windows containers:
#        1) windows server containers that share system kernal exactly like linux containers
#        2) For better security there are Hyper-V isolation containers
#           With it each container is run on it's own virtual machine guaranting complete kernal isolation
#           between container and underlined host

# NOTE, docker on windows can work with Hyper-V or WSL 2. You can select it in the setting.


# In linux world we have a number of base images (that we specify in docker compose file): ubuntu, fedora, debian, ...
# For windows we have two options: windows server core, nano server



# INSTALLATION DOCKER FOR WINDOWS
# You need to install hyper-v for windows 11 Home - https://beebom.com/how-enable-hyper-v-windows-11-home/
# For windows 11 Enterprise and Professional it is available by default.

# Then install docker for windows 
https://docs.docker.com/desktop/install/windows-install/
# Then run docker for windows
# Now you can use docker through terminal too:
docker version
# You can run in terminal all commands as before, for example:
docker run -it ubuntu bash

# Note, by defaut docker client - windows and server - linux. Therefore you run your docker on linux vm.
# You windows system has docker client and it connects docker server
# that on your machine or not
# When you open docker for windows you run docker server

# You can switch on windows containers with icom in system tray 
# => Switch to windows containers (run forever on my laptop)