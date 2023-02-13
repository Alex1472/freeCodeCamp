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
