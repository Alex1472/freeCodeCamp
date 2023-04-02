# You want to dockerise you application for ease of shiping and deployment (create an image for it)
# To create an image we should crate a Dockerfile
# For example, we have an application on python 2, app.py:
import os
from flask import Flask
app = Flask(__name__)

@app.route("/")
def main():
    return "Welcome!"

@app.route('/how are you')
def hello():
    return 'I am good, how about you?'

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)



# We will look at a simple web server based on flask
# First we should think what we should do to install the app manually
# 1. OS - Ubuntu
# 2. Update apt repo
# 3. Install dependencies using apt
# 4. Install python dependencies using pip
# 5. Copy source code to /opt folder
# 6. Ren the web server using "flask" command



# HINT:
# It's easier to run container with os, install there your app and then transform it into a Dockerfile
# You can see privous command with the command history
history



# Dockerfile contains of pairs of instarction and argument
# For instactions is used uppercase(FROM, RUN, COPY, ENTRYPOINT).
# For exaple RUN - instuction, apt-get update - argument
# Then create Dockerfile
FROM ubuntu # for witch os

RUN apt-get update # install python2 and pip
RUN apt-get install -y python2 curl
RUN curl https://bootstrap.pypa.io/pip/2.7/get-pip.py --output get-pip.py
RUN python2 get-pip.py

RUN pip install flask # install flask
RUN pip install flask-mysql

COPY app.py /opt/app.py # copy app.py from the current folder to /opt/app.py in the container

ENTRYPOINT FLASK_APP=/etc/app.py flask run --host=0.0.0.0 # what we should do to run container



# Then create an image using the Dockerfile, and specify the tag
# Tag should has a structure docker-hub-name/app-name
# You should be in the app directory
docker build . -t docker-hub-name/my-app-name

# Each layer of the dockerfile create a new layer in the docker image
# That just the changes from the previous layer.
# So each layer size based on this changes.
# You can see this information with docker history command
docker history image_name

# Docker cached layers, so if command in dockerfile failed
# on the next try, this layers will be used instead of running all dockerfile from start


# Login to docker hub
docker login
# Then push you image to a docker hub
docker push docker-hub-name/app-name



# ENTRYPOINT AND CMD
# We know that container is alive only some process executes inside it.
# To specify this process entrypoint and cmd command are used.
# First of all, they have two syntaxes, shell form and exec form:
CMD echo "Hello World"
CMD ["echo", "Hello World"]
ENTRYPOINT echo "Hello World"
ENTRYPOINT ["echo", "Hello World"]

# CMD command you can override when running container
# Dockerfile:
FROM ubuntu
CMD ["echo", "Hello World"]

docker run cmd-instructions # Hello World

# If you add parameters to command line, that completly overrides CMD command
docker run cmd-instructions echo "message changed" # message changed 


# ENTRYPOINT adds parameters from command line as parameters to command form entrypoint
# Dockerfile:
FROM ubuntu
ENTRYPOINT ["echo", "Hello World"]

docker run entrypoint-instructions # Hello world

docker run entrypoint-instructions echo Hello # Hello world echo Hello


# If you specify command in entrypoint and then add cmd with parameter
# By default command will be executed with this parameter
# But you can override this parameter from command line
# Dockerfile:
FROM ubuntu
ENTRYPOINT ["echo", "Hello"]
CMD ["Tom"]

docker run entrypoint-cmd # Hello Tom

docker run entrypoint-cmd Jack # Hello Jack


# Also you can override entrypoint with --entrypoint parameter in command line
docker run --entrypoint sleep test-env-vars 5 # will sleep 5 seconds

