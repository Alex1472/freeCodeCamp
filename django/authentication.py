# Authentication based on session.
# We you login, information about current user will be added into a session.
# When you log out, information will be deleted.

# PLUGGING IN AUTHENTICATION
# To support authentication you should add 'django.contrib.auth' into a project.
INSTALLED_APPS = [ 
    ...
    'django.contrib.auth', # included by default
    ...
]

# Then you need to add path to this project
# proejct_folder/project_folder/urls.py
urlpatterns = [
    ...
    path('account/', include('django.contrib.auth.urls')),
    ...
]


# REVERSE METHOD
# In the auth app there is login, logout views.
# You can get their paths by the reverse function
reverse('login')
reverse('logout')


# USER CONTEXT OBJECT
# If you are logged in, in the context of any page you have a user object
# main.html
{% if user.is_authenticated %} 
{{ user.email }} 
{% endif %}


# We can say login / logout view where you want to redirect, when login / logout is sussessful.
# Use next paramenter in url
# main.html
<p><a href="{% url 'login' %}?next={{ request.path }}">Login</a></p> # go to this page, request is in the context
<p><a href="{% url 'login' %}?next={% url 'polls:index' %}">Login</a></p> # go to the index view in a polls app



# registaration/login.html
# You should specify registragion/login.html template for login page. It can be in any application.
# you can check error with context variable form.errors
# you can access user object if you are logged in
{% form.errors %}
    <p>There is some errors.</p>
{% endif %}

<form method="post" action="{% url 'login' %}"> # send it to login as post
    {% csrf_token %} 
    {{ form.as_p }}  # this variable generates username-password form

    <input type="submit" value="Login" /> #button to submit form, there is not it in the form.as_p
    <input type="hidden" name="next" value="{{ next }}" /> # you get next variable from query string for redirection after logging in
    # you should sent it to view as next    
</form>

# you can access user object in code as
request.user



# PROTECTED VIEWS
# Sometimes you want to show a page only for users that are logged in.
# In this case you should redirect users to login page and then back to origin page.
# Manually you can do this like that
from django.shortcuts import redirect
from django.views.generic import View
from django.http import HttpResponse
from django.utils.http import urlencode
from django.urls import reverse


class ProtectedView(View):
    def get(self, request):
        if request.user.is_authenticated: # If user is authenticated show page
            return HttpResponse("Protected Page")
        url = reverse('login') + '?' + urlencode({'next': request.path})
        return redirect(url) # redirect to login page
        
# But you can add this functionality just inherit your view from the LoginRequiredMixin class
# Something do not work here
from django.contrib.auth.mixins import LoginRequiredMixin

class ProtectedView(LoginRequiredMixin, View):
    def get(self, request):
        return HttpResponse("Protected page")