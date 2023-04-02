# Docker support for majority of build system: Jenkins, Bamboo, Travis, ...
# How it works:
# Project has dockerfile, once new code pushed into the repository
# Jenkins build image with dockerfile (it may tag it with a new build number)
# That it can run tests with this image
# It tests are successful the image can be pushed to the image repository(registry)
# The image repository can be integrated with container hosted platform, like AWS
# to automated deploy your application 