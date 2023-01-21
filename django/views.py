# In a url in django project after domain name goes app name and then view and paramenters
https://sampales.dj4e.com/views/funky # view - app, funky - view
https://sampales.dj4e.com/views/danger?guess=42 # guess - parameter
https://sampales.dj4e.com/views/rest/42 # 42 - parameter too

# We use polls app here.


# ROUTING
# To resolve the view we should use for request, django do this things:
# 1) Goes to the project_folder/setting.py and finds ROOT_URLCONF variable
#    It specifies what URLCONF-file for routing we should use, for example:
ROOT_URLCONF = 'mysite.urls' # /project_folder/project_folder/urls.py

# 2) It goes to file from step 1, and finds variable urlpatterns
#    Here we usually specify another URLCONF-file (for an application), for example:
from django.urls import include, path
urlpatterns = [
    path('polls/', include('polls/urls'))
    # when we find url starts with polls, we should go to project_folder/polls/urls.py and find view there
    # for example for url http://127.0.0.1:8000/polls/main/
    # go to project_folder/polls/urls.py with path main/
]

# 3) In app URLCONF-file you should specify view, which handles this request.
# project_folder/polls/urls.py:
from . import views

urlpatters = [
    path('main/', v`iews.main)
]

# 4) In the file project_folder/polls/views.py we should define a function that we specify in urlpatters
# views.py
from django.http import HttpResponce

def main(responce):
    return HttpResponce("It's a main page.")




# HANDLING REQUESTS:
# You can get an html page with 3 ways:
# 1) If you want to send always the same page for the route you can use TemplateView in urls.py file
#    You should add the file to send in /project_folder/polls/templates/polls/file_name.html
#    Note, you should add polls folder in a path, bacause django searches files in the templates folders of all apps
#    and if we add file directly into templates folder (/project_folder/polls/templates/file_name.html)
#    it can find a file with the same name in another app, so we can search polls/file_name.html not file_name.html
# project_folder/polls/urls.py
from django.urls import path
from django.views.generic import TemplateView

urlpatterns = [
    path('main/', TemplateView.as_view(template_name='polls/main.html')) # give main.html from project_folder/polls/templates/polls/main.html
]


# 2) If you want to dinamically generate page on every request, you should define a function
# that will handle every request in views.py . And specify this function in urls.py .
# project_folder/polls/urls.py
from django.urls import path
from . import views

urlpatterns = [
    path('main/', views.main)
]

# project_folder/polls/views.py
from django.http import HttpResponse

def main(request):
    return HttpResponse("It's a main page.")


# 3) You can also use classes to handle requests.
# You can create a class which is inherited from django.views.View and define methods for get, post and another requests.
# project_folder/polls/views.py
from django.views import View

class Main(View):
    def get(self, request):
        return HttpResponse("It's a main page.")
        
# project_folder/polls/urls.py
from . import views

urlpatterns = [
    path('main/', views.Main.as_view(), name='main')
]



# REDIRECT
# You can send redirect response. In this case a browser will request specified page in redirect responce
# project_folder/polls/urls.py
from . import views

urlpatterns = [
    path('old_main/', views.old_main, name='old main')
]

# project_folder/polls/views.py
def old_main(request):
    return HttpResponseRedirect('http://localhost:8000/polls/main') # redirect to main page




# PARAMETERS
# You can handle query paramenter with django:
# For query http://127.0.0.1:8000/polls/queryString/?a=1&b=2 
# You can get parameters like this:
def queryString(request):
    return HttpResponse(f"Parameter A: {request.GET['a']}, parameter B: {request.GET['b']}")

# You can handle route paramenter
# Specify route paramenter in project_folder/polls/urls.py
# urls.py
urlpatterns = [
    path('<int:a>/<int:b>/query/', views.query, name='query')
]

# You can get route paramenter as keyword parameters in a handle function:
# views.py
def query(request, a, b):
    return HttpResponse(f'Paramenter A: {a}, paramenter B: {b}.')

# Note, you need to use the escape function for data comes from user to avoid security issues.
from django.utils.html import escape

def query(request, a, b):
    return HttpResponse(f'Paramenter A: {escape(a)}, paramenter B: {escape(b)}.')    
    


# TEMPLATES
# You can specify some data for html with templates
# First you should specify html-template in project_folder/polls/templates/polls/your_template.html
# game.html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Game</title>
    </head>
    <body>
        You number is {{ number }} # number from context
    </body>
</html>


# views.py
from django.template import loader

class Game(View):
    def get(self, request, number):
        template = loader.get_template('polls/game.html')
        context = {'number': number}
        return HttpResponse(template.render(context, request))

# or with shortcut
from django.shortcut import render

class Game(View):
    def get(self, request, number):
        context = {'number': number}
        return render(request, 'polls/game.html', context)
        
# urls.py
urlpatterns = [
    path('game/<int:number>/', views.Game.as_view(), name='game')
]



# TEMPLATE LANGUAGE
# Note, all values in templates are escaped.
# If you don't want to escape the value you can add |safe
{{ number|safe }}

# Specify a variable value
<h2>Number is {{ number }}</h2>

# if statment
{% if number > 10 %}
<p>Number is more then 10.</p>
{% else %}
<p>Number is less or equal then 10.</p>
{% endif %}




# for loop:
class Game(View):
    def get(self, request):
        context = {'numbers': [1, 2, 3, 4, 5]}
        return render(request, 'polls/game.html', context)

<p>We have {{ numbers|length }} numbers</p> # use attribute
<ul>
    {% for x in numbers %}
    <li>{{ x }}</li>
    {% endfor %}
</ul>

# Hierarchical objects
class Game(View):
    def get(self, request):
        context = {'outer': {'inner': 4}}
        return render(request, 'polls/game.html', context)
        
<p>{{ outer.inner }}</p>



# Templates inheritance (composition)
# You can insert on template into another:
# project_folder/polls/templates/polls/base.html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Game</title>
    </head>
    <body>
        {% block content %} {% endblock %} # insert markup here
    </body>
</html>

# project_folder/polls/templates/polls/game.html
{% extends "polls/base.html" %} # insert this markup into base.html 

{% block content %} 
{% if x < 10 %}
<p>Less then 10</p>
{% elif x > 10 %}
<p>More then 10</p>
{% else %}
<p>It's 10</p>
{% endif %}
{% endblock %}

# views.py 
class Game(View):
    def get(self, request, number): # number is a url parameter
        context = {'x': int(number)}
        return render(request, 'polls/game.html', context)



# REMOVE HARDCODED URLS
# In html you can specify links like this
<a href="/polls/main">Main page</a> # for project_folder/polls app

# You can specify url with view name, not path
# You can do this with url utility
# Specify view name in urls.py for view
urlpatterns = [
    path('main/', views.Main.as_view(), name='main-view'),
]

# html-file
<a href="{% url 'main-view' %}">Main view</a>


# To reference view in another app, you should specify app name
# First define app name, in polls/urls.py
app_name = 'polls'

urlpatterns = [
    ...
]

# html-file
<a href="{% url 'polls:main-view' %}">Main view</a>

# Url utility generate path
<p>{% url 'polls:main-view' %}</p> # /polls/main/


# Specify paramenters for view
# polls/urls.py
urlpatterns = [
    path('game/<int:number>', views.Game.as_view(), name='game-view')
]

# html
<a href="{% url 'polls:game-view' 10 %}">Game 10 view</a> # path /polls/game/10


# You can specify get url in code with the reverse, reverse_lazy functins
from django.urls import reverse

url = reverse('polls:main-view')
url = reverse('polls:game-view', args=[5]) # specify url argument with args param



# HANDLING BAD REQUESTS
# We should raise Http404 exception if request was incorrect.
from django.http import Http404
from .models import Question

def detail(request, question_id):
    try:
        question = Question.objects.get(pk=question_id)
    except Question.DoesNotExist:
        raise Http404("Question does not exist")
    return render(request, 'polls/detail.html', {'question': question})

# or with a shortcut
from django.shortcuts import get_object_or_404, render

def detail(request, question_id):
    question = get_object_or_404(Question, pk=question_id)
    return render(request, 'polls/detail.html', { 'question': question })



# GENERIC VIEWS
# You can create view for detail and list cases
# For list case, you can inherit you view from django.views.generic.ListView
# For list view, you should specify model attribute with model from models.py
# One get request you gets all items from the model.
# Passes all items into context with name object_list
# Render template with name project_folder/polls/templates/polls/modelName_list.html
# views.py
from django.views import generic

class QuestionsList(generic.ListView):
    model = Question


# urls.py
urlpatterns = [
    path('questions/', views.QuestionsList.as_view(), name='questions_list'),
]


# /polls/templates/polls/question_list.html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Question List</title>
    </head>
    <body>
        <ul>
            {% for question in object_list %}
            <li>{{ question.question_text }}</li>
            {% endfor %}
        </ul>
    </body>
</html>


# DETAILVIEW
# You should inherit you view from django.views.generic.DetailView
# and specify model attirute
# Then you should id pattern into a path for this view
# DetailView retrieves items from the model,
# Gets item with id from path
# Add passes it to context as object, and renders template project_folder/polls/templates/polls/model_name_detail.html
# views.py
class QuestionDetail(generic.DetailView):
    model = Question
    
# urls.py
urlpatterns = [
    path('questions/<pk>/', views.QuestionDetail.as_view(), name='question_detail')
]

# question_detail.html
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Details</title>
    </head>
    <body>
        <h2>{{ object.question_text }}</h2>
        <a href="{% url 'polls:questions_list' %}">Questions List</a>
    </body>
</html>

