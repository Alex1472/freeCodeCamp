# LOADING DATA FROM CSV, RUNNING SCRIPT
# To run scripts you should install django-extentions and add it to apps
pip3 install django-extentions

# project_name/project_name/setting.py
INSTALLED_APPS = [
    ...,
    'django_extensions',
    ...,
]

# Then we should create corresponding models in models.py file in a project
# project_folder/polls/models.py 
class Cat:
...

class Breed:
...

# Then create folder for scripts in a project_folder/polls/models
# Add __init__.py
# Add script file

# In a project_folder:
mkdir scripts
touch scripts/__init__.py
touch scripts/load_cats.py

# load_cats.py
import csv
from polls.models import Cat, Breed # you can import model classes

def run():
    Cat.objects.all().delete()
    Breed.objects.all().delete()

    file = open('polls/meow.csv')
    reader = csv.reader(file)

    next(reader)

    for row in reader:
        breed, created = Breed.objects.get_or_create(name=row[1]) # we use the get_or_create method
        cat = Cat(name=row[0], breed=breed, weight=row[2])
        cat.save() 


# Finally, run script
python3 manage.py runscript cats_load



# Many-to-many relationship
import csv
from cats.models import Person, Course, Membership


def run():
    file = open('cats/load.csv')
    reader = csv.reader(file)

    Person.objects.all().delete()
    Course.objects.all().delete()
    Membership.objects.all().delete()

    next(reader)
    for row in reader:
        person, created = Person.objects.get_or_create(name=row[0])
        course, created = Course.objects.get_or_create(name=row[2])

        role = Membership.LEARNER if row[1] == 'L' else Membership.INSTRUCTOR
        membership = Membership(person=person, role=role, course=course)
        membership.save()