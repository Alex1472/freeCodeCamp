# SETTINGS.PY DATABASES
# You can specify type of a database you use in the project_folder/setting.py file
# Find DATABASES variable - ENGINE - type of database, NAME - default name for sqlite database.
# For sqlite database will be created automatically
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': BASE_DIR / 'db.sqlite3',
    }
}



# SETTINGS.PY - INSTALLED_APPS
# You should add you created app into project_folder/project_folder/settings.py file into INSTALLED_APPS
# For example for project_folder/polls - app you should add 'polls.apps.PollsConfig' - path to a -Config class in your app
INSTALLED_APPS = [
    'polls.apps.PollsConfig',
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
]
# Note INSTALLED_APPS contains some default utility apps



# To use databases in you project, you should 
# 1) Create model in your app (in project_folder/app_folder/models.py)
from django.db import models

# Create your models here.
class Question(models.Model):
    question_text = models.CharField(max_length=200)
    pub_date = models.DateTimeField('date published')

class Choice(models.Model):
    question = models.ForeignKey(Question, on_delete=models.CASCADE)
    choice_text = models.CharField(max_length=200)
    votes = models.IntegerField(default=0)


# 2) In you project folder create migrations.
#    Run shell in you project folder, enter python manage.py makemigrations
#    It create file project_folder/app_folder/migrations/0001_initial.py - python translation of your projects models
# Note, if you change you mode and run python manage.py makemigrations again, new migrations will be created, to apply this changes
# And when you run python migrate, both this files (old and new) will be run for change database
# You can look at the sql generated from migrations with command
python manage.py sqlmigrate apps_nama migrations_number
python manage.py sqlmigrate polls 0001


# 3) Run python manage.py migrate
#    It applies you migrations to the database (changes it scheme)
# The database is stored in the project_folder


# INTERACTIVE SHELL
# You can run python manage.py shell and open interactive shell for database
# You should import table classes
from app_name.models import class_name
from polls.models import Choice, Question

Question.objects.all()

from django.utils import timezone
q = Question(question_text="What's new?", pub_date=timezone.now())
q.save()

q.question_text
q.pub_date

q.question_text = "What's up?"
q.save()
Question.objects.all()



# ADMIN SITE / ADMIN.PY
# Django ships with admin site
# First, we should create superuser
python manage.py createsureruser # and enter creditians

# Admin interface is enabled by default, so you can add /admin to you site and open it.
https://alex147.pythonanywhere.com/admin/

# You can manage Groups and users there. 
# To change tables in databases, you need to add some code into the project_folder/app_folder/admin.py file
# admin.py:
from django.contrib import admin

# Register your models here.
from .models import Question # model for table to edit

admin.site.register(Question)