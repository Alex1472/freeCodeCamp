# You should inherit you model from django.db.models.
# When you specify foreign key, you should pass another class / table name
# on_delete - what should we do, if row we referenced will be deleted
# we should delete row with this foreign key (models.CASCADE) 
# or set NULL (models.SET_NULL)
# null = true is null values allowed in a db table
# black = True is allowed not to specify data in a form
class Lang(models.Model):
    name = models.CharField(max_length=200)


class Book(models.Model):
    title = models.CharField(max_length=200)
    isbn = models.CharField(max_length=13)
    lang = models.ForeignKey('Lang', on_delete=models.SET_NULL, null=True) # set null when corresponding language has been deleted


class Instance(models.Model):
    book = models.ForeignKey('Book', on_delete=models.CASCADE) # delete if corresponding book has been deleted
    due_date = models.DateTimeField(null=True, blank=True)
    
    
    
# USING SHELL
# You can use special shell with django
# run python manage.py shell

# Then you can use models from models.py to save and retrieve data from a database
# But first you should import this classes
# from project_name.models import model_name
from polls.models import Lang, Book, Instance

lang = Lang(name='en')
lang.save() # save into a table
lang.id # 1 django automatically adds id after save

book = Book(name='Simple Book', isbn='12345', lang=lang) # we specify object for foreign key, not id
book.save()

instance = Instance(book=book, due_date='2000-01-01')
instance.save()


# You can walk through different tables with items
book = Book.objects.get(pk=1)
book.name # Simple book
book.lang.name # en

instance = Instance.objects.get(pk=1)
instance.book.name # Simple book
