# You can add search in your items
# Add a form for search
<div style="float: right;">
  <form> # form sends get request with paramter search from text input
    <input type="text" name="search" placeholder="Search..."
    {% if search %} value="{{ search }}" {% endif %} > # search - from context, previous value to search
    <button type="submit"><i class="fa fa-search"></i></button> # send request
    <a href="{% url 'ads:all' %}"><i class="fa fa-undo"></i></a> # reset search (show all items)
  </form>
</div>


# You can handle search in a get method of view. You should handle 2 cases with search paramenter
# and without
from django.db.models import Q

def get(self, request):
    search = request.GET.get('search', False) # get search parameter
    if search: # if there is a parameter
        query = Q(title__icontains=search) # paramenter has format field__icontains, i - ignore case
        query.add(Q(text__icontains=search), Q.OR)
        ads = Ad.objects.filter(query).select_related() \  # should call select_related
            .order_by('-updated_at').all()
    else:
        ads = Ad.objects.all()
    favs = []
    if request.user.is_authenticated:
        favs = request.user.fav_ads.values('id')
        favs = list(map(lambda fav: fav['id'], favs))
    ctx = {'ads': ads, 'favs': favs, 'search': search}
    return render(request, self.template_name, ctx)



# To support tags in your project use taggit
# https://django-taggit.readthedocs.io/en/latest/getting_started.html
# Install extention
pip install django-taggit

# Add to INSTALLED_APPS
INSTALLED_APPS = [
    'taggit'
]



# To add tag list to you model, add field with TaggableManager
from taggit.managers import TaggableManager

class Ad(models.Model):
    tags = TaggableManager()   
# Then in a form, you can enter some tags as comma-separated values



# You can render tags in a template
<p>
    {% for tag in ad.tags.all %}
        <span style="border: 1px solid black; background-color: LightGreen;" >{{ tag }}</span>
    {% endfor %}
</p>



# You can filter items by tags with a search

class AdListView(View):
    def get(self, request):
        search = request.GET.get('search', False)
        if search:
            query = Q(title__icontains=search)
            query.add(Q(text__icontains=search), Q.OR)
            query.add(Q(tags__name__in=[search]), Q.OR) # add search in tags
            ads = Ad.objects.filter(query).select_related() \
                .distinct().order_by('-updated_at').all()
        else:
            ads = Ad.objects.all()
        favs = []
        ctx = {'ads': ads, 'search': search}
        return render(request, self.template_name, ctx)