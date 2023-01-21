# You can make you form prittier with crispy forms.
# First, you need to install library
pip install django-crispy-forms

# Then add crispy forms to INSTALLED_APPS
INSTALLED_APPS = [
    ...
    'crispy_forms',
    ...
]
# Add variable into settings.py
CRISPY_TEMPLATE_PACK = 'bootstrap4'

# Add links to bootstrap to head in your html
<link rel="stylesheet"
    href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"
    crossorigin="anonymous">

<link rel="stylesheet"
    href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css"
    crossorigin="anonymous">


# In your forms you need to load crispy tags and add |crispy to  form.
<form action="" method="post">
    {% load crispy_forms_tags  %} # loading crispy tags
    {% csrf_token %}
    <table>
        {{ form|crispy }} # make form awesome
    </table>
    <input type="submit" value="Submit">
    <input type="submit" onclick="window.location='{% url 'cats:all' %}'; return false" value="Cancel">
</form>
