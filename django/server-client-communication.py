# We want to add a star favourite for item.
# When we click on the star it becomes yellow and this updates db.
# When we click on the star next time it becomes white and we delete favorite from db.

# models.py
from django.db import models
from django.core import validators
from django.conf import settings


class Thing(models.Model):
    title = models.CharField(max_length=200, validators=[
                             validators.MinLengthValidator(2, 'Title should has at least two characters.')])
    text = models.TextField()
    favorites = models.ManyToManyField(
        settings.AUTH_USER_MODEL, through='Fav', related_name='fav_things') # get favourite things for user user.fav_things
    owner = models.ForeignKey(settings.AUTH_USER_MODEL,
                              on_delete=models.CASCADE)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.title

# Fav through table, we add record into in when we ad item to favorite
# and delete item, when we delete from favorite
class Fav(models.Model):
    thing = models.ForeignKey('Thing', on_delete=models.CASCADE)
    user = models.ForeignKey(settings.AUTH_USER_MODEL,
                             on_delete=models.CASCADE)

    class Meta:
        unique_together = ('thing', 'user')

    def __str__(self):
        return f"{self.thing.title} is fav for {self.user.username}"



# thing_list.html
{% block content %}

<h2>Favorite Things</h2>
<ul>
    {% for thing in things %}
    <li>
        <a href="{% url 'favs:thing_detail' thing.id %}">{{ thing.title }}</a>
        {% if thing.owner == user %}
        (<a href="{% url 'favs:thing_update' thing.id %}" >Edit</a> | 
         <a href="{% url 'favs:thing_delete' thing.id %}">Delete</a>)
        {% endif %}
        
        {% if user.is_authenticated %} # show yellow or white star, if user is authrised
        # show one star, another has display: none
        <a href="#"
            {% if thing.id in favs %} style="display: none;" {% endif %} 
            id="favourite_star_{{ thing.id }}"
            onclick="sendFavourite('{% url 'favs:thing_favourite' thing.id %}', {{ thing.id }}); return false;"> # invoke sendFavourite with url and thing id
            # favorite
            <span class="fa-stack" style="vertical-align: middle;">
                <i class="fa fa-star fa-stack-1x" style="display: none; color: orange;"></i>
                <i class="fa fa-star-o fa-stack-1x"></i>
            </span>
        </a>
        # unfavourite
        <a href="#"
            {% if thing.id not in favs %} style="display: none;" {% endif %}
            id="unfavourite_star_{{ thing.id }}"
            onclick="sendFavourite('{% url 'favs:thing_unfavourite' thing.id %}', {{ thing.id }}); return false;">
            <span class="fa-stack" style="vertical-align: middle;">
                <i class="fa fa-star fa-stack-1x" style="color: orange;"></i>
                <i class="fa fa-star-o fa-stack-1x"></i>
            </span>
        </a>
        {% endif %}
    </li>
    {% endfor %}
</ul>

<a href="{% url 'favs:thing_create' %}">Create Thing |</a>
{% if user.is_authenticated %}
<a href="{% url 'logout' %}?next={% url 'favs:things' %}">Logout</a>
{% else %}
<a href="{% url 'login' %}?next={% url 'favs:things' %}" >Login</a>
{% endif %}

<script>
    # send post request for favourite or unfavourite item
    function sendFavourite(url, thing_id) {
        console.log(url, thing_id)
        fetch(url, { method: 'POST'}) # thend 
            .then(res => {
                console.log("post")
                $(`#favourite_star_${thing_id}`).toggle()
                $(`#unfavourite_star_${thing_id}`).toggle()
            })
    }
</script>

{% endblock %}



# urls.py 
urlpatterns = [
    path("", ThingListView.as_view(), name='things'),
    path("thing/<int:pk>", ThingDetailView.as_view(), name='thing_detail'),
    path("thing/create", ThingCreateView.as_view(success_url=reverse_lazy('favs:things')),
         name='thing_create'),
    path("thing/update/<int:pk>", ThingUpdateView.as_view(success_url=reverse_lazy('favs:things')),
         name='thing_update'),
    path("thing/delete/<int:pk>", ThingDeleteView.as_view(success_url=reverse_lazy('favs:things')),
         name='thing_delete'),
    path("thing/<int:pk>/favourite", ThingFavoriteView.as_view(),
         name='thing_favourite'), # for post request to favourite item 
    path("thing/<int:pk>/unfavourite", ThingUnfavouriteView.as_view(),
         name='thing_unfavourite') # for post request to unfavourite item
]



# views.py
class ThingListView(OwnerListView):
    model = Thing
    template_name = 'favs/thing_list.html'

    def get(self, request):
        things = Thing.objects.all()
        favs = []
        if request.user.is_authenticated:
            rows = request.user.fav_things.values('id')
            for row in rows:
                favs.append(row['id'])
        ctx = {'favs': favs, 'things': things} # pass things and ids of favourite items for current user
        return render(request, self.template_name, ctx)



from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt

@method_decorator(csrf_exempt, name='dispatch')
class ThingFavoriteView(LoginRequiredMixin, View):
    def post(self, request, pk):
        thing = get_object_or_404(Thing, pk=pk)
        fav = Fav(thing=thing, user=request.user)
        fav.save() # save item to favs 
        return HttpResponse()


@method_decorator(csrf_exempt, name='dispatch')
class ThingUnfavouriteView(LoginRequiredMixin, View):
    def post(self, request, pk):
        thing = get_object_or_404(Thing, pk=pk)
        fav = get_object_or_404(Fav, thing=thing, user=request.user)
        fav.delete() # remove items from favs
        return HttpResponse()