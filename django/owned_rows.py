# Sometimes you want to show, grant access to edit / delete items, only for user that create this items
# First, you need to add into model a foreign key to user table (it's a system table)
from django.conf import settings

class Article(models.Model):
    title = models.CharField(max_length=200, validators=[
        validators.MinLengthValidator(
            2, 'Title should has at least two symbols.'
        )
    ])
    text = models.CharField(max_length=2000)
    author = models.ForeignKey(
        settings.AUTH_USER_MODEL, on_delete=models.CASCADE) # add name table with users

    def __str__(self) -> str:
        return self.title


# Method get_queryset retrieves data from database, let's override it and filter data by current user
# Also inherit this view from LoginRequiredMixin, so that we can always get a user       
class OwnerListView(LoginRequiredMixin, ListView):
    def get_queryset(self):
        queryset = super().get_queryset()
        return queryset.filter(author=self.request.user) # get a user from request field

# Inherit our view from created view
class ArticleList(OwnerListView):
    model = Article
    
    

# Let's create CreateArticle view, we need add author of the article in this view.
# We need to add author, for this override form_valid method
class OwnerCreateView(LoginRequiredMixin, CreateView):
    def form_valid(self, form): 
        object = form.save(commit=False) #create item, but do not save it into a database
        object.author = self.request.user # manually setting an author
        object.save()
        return super().form_valid(form)


class ArticleCreate(OwnerCreateView):
    model = Article
    fields = ['title', 'text'] # we only get access to title and name, not author
    success_url = reverse_lazy('cats:articles')
    

path('', ArticleListView.as_view(), name='all')
path('articles/create', CreateArticle.as_view(success_url=reverse_lazy('myarts:all')), name='article_create'), # template_name took, template_name = None in a view