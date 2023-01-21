# DJANGO.FORMS.FORM
# You can use django feature for the forms by inheriting you form from django.forms.Form
# specify input fields in your form
from django import forms
from django.core import validators


class BasicForm(forms.Form): # inherit your form from django.forms.Form
    title = forms.CharField(validators=[
        validators.MinLengthValidator(2, '...')
    ])
    mileage = forms.IntegerField()
    purchase_date = forms.DateTimeField()
    
# Then create your form and render it in a view.
class FormView(View):
    def get(self, request):
        form = BasicForm()
        return HttpResponse(f'<table>{form.as_table()}</table>')
        
# as_table generates some html code like this:
<table>
    <tr>
        <th><label for="id_title">Title:</label></th>
        <td>
          <input type="text" name="title" required id="id_title">
        </td>
    </tr>

    <tr>
        <th><label for="id_mileage">Mileage:</label></th>
        <td>
            <input type="number" name="mileage" required id="id_mileage">
        </td>
    </tr>

    <tr>
        <th><label for="id_purchase_date">Purchase date:</label></th>
        <td>
            <input type="text" name="purchase_date" required id="id_purchase_date">
        </td>
    </tr>
</table>



# But better render the form with templates:
# templates/home/form.html
<p>
    <form action="" method="post">
        {% csrf_token %}
        <table>
            {{ form.as_table }}
        </table>
        <input type="submit" value="Submit">
    </form>
</p>

# views.py
class FormView(View):
    def get(self, request):
        form = BasicForm()
        context = {'form': form}
        return render(request, 'home/form.html', context)


# You can pass data into a form (usually from model):
class FormView(View):
    def get(self, request):
        data = {
            'title': 'SakaiCar',
            'mileage': 42,
            'purchase_date': '2018-08-14'
        }
        form = BasicForm(data)
        context = {'form': form}
        return render(request, 'home/form.html', context)



# DATA VALIDATION
# You should valiedate data that comes from a browser. In django we do it with validators
class BasicForm(forms.Form): 
    title = forms.CharField(validators=[
        validators.MinLengthValidator(2, 'Title should be at least 2 characters long.')
    ])
    mileage = forms.IntegerField()
    purchase_date = forms.DateTimeField()
    
# In a view on post request we can validate data with forms
class FormView(View):
    def get(self, request):
        data = {
            'title': 'SakaiCar',
            'mileage': 42,
            'purchase_date': '2018-08-14'
        }
        form = BasicForm(data)
        context = {'form': form}
        return render(request, 'home/form.html', context)

    def post(self, request):
        form = BasicForm(request.POST) # pass post to form. It contains all entered data as right key-value pairs
        if not form.is_valid(): # we render form with data from post, if it not a valid. The template automatically contains errors messages for incorrect data.
            context = {'form': form}
            return render(request, 'home/form.html', context)
        return redirect(reverse('home:main'))
        
# Note, that we send 200 after post request (if data is not valid). So if we hit refresh we get a yacky message. 
# This how django is suppose to do. Just will do like this.
        


# VALIDATE AND SAVE WITH FORMS
# You can use forms for data validation on server.
# We use model:
class Make(models.Model):
    name = models.CharField(max_length=100, validators=[
        validators.MinLengthValidator(
            2, message='Model name should have at least two characters.')
    ])

    def __str__(self):
        return self.name
        
        
# We can create form for model, like this:
# forms.py
from django.forms import ModelForm
from .models import Make


class MakeForm(ModelForm):
    class Meta:
        model = Make # model name
        fields = '__all__' # use all fields


# Then define template with form for create / update operations
# templates/polls/make_form.html
<form method="post" action=""> # post for the same url
    {% csrf_token %}
    <table>
        {{ form.as_table }}
    </table>
    <input type="submit" value="Submit" />
</form>

# Create views for create / update items
urlpatterns = [
    ...
    path('<int:pk>/make/', views.MakeUpdateView.as_view(), name='make_update'),
    path('make/', views.MakeCreateView.as_view(), name='make_create')
    ...
]

# we can use form to validate and save data
# form has methods is_valid, save for this.
# update item view
class MakeUpdateView(generic.View):
    def get(self, request, pk):
        make = get_object_or_404(Make, pk=pk)
        form = MakeForm(instance=make) # we should pass current item into a form as an instance
        context = {'form': form}
        return render(request, 'polls/make_form.html', context)

    # when a user hits submit
    def post(self, request, pk):
        make = get_object_or_404(Make, pk=pk)  
        form = MakeForm(request.POST, instance=make) # pass post data and database instance
        if not form.is_valid(): # we can validate data with form
            return render(request, 'polls/make_form.html', {'form': form})

        form.save() # we can save data into db with a form
        return redirect(reverse('polls:index'))

# create item view
class MakeCreateView(generic.View):
    def get(self, request):
        form = MakeForm()
        context = {'form': form}
        return render(request, 'polls/make_form.html', context)

    def post(self, request):
        form = MakeForm(request.POST)
        if not form.is_valid():
            context = {'form': form}
            return render(request, 'polls/make_form.html', context)

        form.save()
        return redirect(reverse('polls:index'))

# delete item view
class BreedDelete(LoginRequiredMixin, View):
    model = Breed
    tempalate_name = 'cats/breed_confirm_delete.html'

    def get(self, request, pk):
        breed = get_object_or_404(self.model, pk=pk)
        context = {'breed': breed}
        return render(request, self.tempalate_name, context)

    def post(self, request, pk):
        breed = get_object_or_404(self.model, pk=pk)
        breed.delete()
        return redirect('cats:all')



# There is more succinct way to create CUD forms. 
# You should inherit your view from django.views.generic.edit.UpdateView/CreateView/DeleteView
# This view will look for template as templates/polls/model_name_form.html
# Note, in this case it automatically passes form object into the template context
# So you even don't has to create corresponding ModelForm in forms.py 
# For exampls:
# make_form.html
<form method="post" action="">
    {% csrf_token %}
    <table>
        {{ form.as_table }}
    </table>
    <input type="submit" value="Submit" />
</form>

# urls.py
urlpatterns = [
    ...
    path('<int:pk>/make/', views.MakeUpdateView.as_view(), name='make_update'),
    path('make/', views.MakeCreateView.as_view(), name='make_create')
    ...
]

# views.py
class MakeCreateView(CreateView):
    model = Make
    fields = '__all__'
    success_url = reverse_lazy('polls:makes') # where to redirect after succesful creation


class MakeUpdateView(UpdateView):
    model = Make
    fields = '__all__'
    success_url = reverse_lazy('polls:makes')

# default template name model_name_confirm_delete.html
class CatDelete(LoginRequiredMixin, DeleteView):
    model = Cat
    success_url = reverse_lazy('cats:all')
    # you can specify you template with template_name field
    # template_name = 'cats/my_template.html'