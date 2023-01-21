# We implement many-to-many relationship with through / join table
# In django we use ManyToManyField and spicify result table and through table
# It's a virtual field, it isn't stored in a database table
class Book(models.Model):
    name = models.CharField(max_length=200, validators=[
        validators.MinLengthValidator(
            2, 'Name should has at least 2 characters.')
    ])
    authors = models.ManyToManyField('Author', through='Authored') # virtual many-to-many field


class Author(models.Model):
    name = models.CharField(max_length=200, validators=[
        validators.MinLengthValidator(
            2, 'Name should has at least 2 characters.')
    ])
    books = models.ManyToManyField('Book', through='Authored') # virtual many-to-many field


# we also should create thought table with two foriegn key fields that point to origin tables.
class Authored(models.Model):
    book = models.ForeignKey(Book, on_delete=models.CASCADE, null=False)
    author = models.ForeignKey(Author, on_delete=models.CASCADE, null=False)
    
    

# Managing tables in python manage.py shell
from cats.models import Book, Author, Authored

book1 = Book(name='book1') 
book2 = Book(name='book2')
book1.save()                        
book2.save()

author1 = Author(name='author1') 
author2 = Author(name='author2') 
author3 = Author(name='author3') 
author1.save()
author2.save()
author3.save()

Authored(book=book1, author=author1).save()
Authored(book=book2, author=author2).save()
Authored(book=book2, author=author3).save()

# You can get values from the other side of the relationship using virtual field 
book1.authors.values()
<QuerySet [{'id': 1, 'name': 'author1'}]>
book2.authors.values()
<QuerySet [{'id': 2, 'name': 'author2'}, {'id': 3, 'name': 'author3'}]