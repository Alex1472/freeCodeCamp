# You can send json response with django with JsonResponse

# urls.py
urlpatterns = [
    path('jsonfun', jsonfun, name='jsonfun')
]

# views.py
from django.http import JsonResponse

def jsonfun(request):
    data = {
        'one': 'one thing',
        'two': 'another thing'
    }
    return JsonResponse(data)