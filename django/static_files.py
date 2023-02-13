# Use static files(images, csses, scripts) in you app you should:

# 1) Add in settings.py app django.contrib.staticfiles
INSTALLED_APPS = [
    'django.contrib.staticfiles'
]

# 2) Add in settings.py STATIC_URL
STATIC_URL = '/static/'

# 3) Add your file into a directory static (or create it and add) in your app (near the directory templates)
# Create directory with name of the app in it and then add a file. (as it works for templates)

# 4) In your template use static 
{% load static %}
<script src="{% static 'script.js' %}">