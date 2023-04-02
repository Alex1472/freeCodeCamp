# After image name you can spefify tag. Tag contains version of image
docker run redis:7.0 # 7.0 - tag
# By default tag is 'latest'
# You can find all available tags on docker hub page of the image


# By default docker container doesn't listen standard input. It just skip it.
# If you want to pass input into container you should map input of you host to docker container.
# Use -i parameter for this. (interactive mode)
# To get out of the container terminal use option -tag
# So, to get out from the container and pass input into container use -it
docker run -it kodekloud/simple-prompt-docker


# How can I access the running application?
# Let's run a sample application
docker run jenkins/jenkins
# Using docker ps command you can find out what ports the app is listening 
8080/tcp, 50000/tcp

# But what should be and an ip?
# There are two options: 
#    1) Use ip for this container. Every container get an ip assigned by default.
#       Use docker inspect command to find it out
#       Find ip in NetworkSetting -> IPAdress
#       In this case ip - 172.17.0.2
#       But it is an internal ip and it's accessable only within the docker host.
#       So you can access container with http://172.17.0.2:8080 only on docker host.
#    2) Use port of docker host. In this case you should map port of the container to 
#       free port of the docker host
#       You should specify mapping in the run command
        docker run -p 8080:8080 jenkins/jenkins # map container app port(8080) to docker host port(8080), first goes external port
#       Now you can access container application by going to url http://localhost:8080 or http://127.0.0.1:8080

# You can map several instances of application(or different applications) and map this to a
# different ports. But, of course, all docker host ports must be different
docker run -p 80:5000 kodekloud/webapp
docker run -p 100:5000 kodekloud/webapp
docker run -p 3303:3306 mysql


# Every docker container has it's own isolated file system.
# So every changes happen only within the container.
# If you delete a container all data in the container WILL BE DELETED TOO.
# If you want to persist data, you should map a dicectory outside the container on the docker host
# to a directory inside the container

# For example mysql saves its data in directory /var/lib/mysql inside the docker container
# You want to save this data. You should map this director to a director on you docker host
# Specify it in a run command. 
# Map /home/alex147/jenkins_data directory on docker host to a dicectory /var/jenkins_home inside the container.
docker run -p 8080:8080 -v /home/alex147/jenkins_data:/var/jenkins_home -u root jenkins/jenkins # we add -u root because of permission issue
# Now data persists even we delete a container.


# docker instpect command return all information aboud a container
docker inspect container_name/id
docker inspect er34


# To show logs of the conainer that runs in a detached mode use logs command
docker logs container_name/id
docker logs er34

	
# Recal, that you can get environment variables from bash in python
# Create environment variable in terminal
export ENV=test 	
	
# You can get this variable with os.environ
import os
print(os.environ['NAME'])
print(os.environ['ENV'])

# To pass variable into docker container use flag -e
docker run -e NAME=TOM -e ENV=Rrr test-env-vars

# You can inspect env variables in a container with command inspect
docker inspect container_name/id
# Then look at Config -> Env

