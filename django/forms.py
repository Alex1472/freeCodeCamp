# Let's look an the form
# form.html
<body>
    <form method="get">
        {% csrf_token %}
        <label for="text-input">Some text:</label><br />
        <input id="text-input" type="text" name="text" />
        <button type="submit">Submit</button>
    </form>
</body>

# with get method, when the submit button is clicked
# browser will send get request with text input value in a query string (csrfmiddlewaretoken will be discussed later)
http://127.0.0.1:8000/polls/form?csrfmiddlewaretoken=fzTsBsPJIQDDEgSvuh9lJJbi1ZtwoMaZXQy34G4Hz3EudzId4C1Mpy71gLLLPPVv&text=sss

GET /polls/form?csrfmiddlewaretoken=BlsAfC3nRXcE4dqth01piojObtbdczAXjC7bIQilIadvDwgbRlTQYdfxqftsDClt&text=qq HTTP/1.1
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Accept-Encoding: gzip, deflate, br
# you can get text in a view
request.GET['text']

# with post method, when the submit button is clicked
# browser will send post request text will be send after headers
POST /polls/form HTTP/1.1
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Accept-Encoding: gzip, deflate, br
...

text=ss

# you can get text in a view
request.POST['text']



# CSRF (Cross Site Request Forgery)
# There is a volnurability.
# If you logged in to a site.
# Then bad site can send you a form. If you submit this form, the form get send request to site you logged in. 
# In this case you browser will send cookies for the site, and this way the rogue site can change your data.

# To solve this problem, site usually send in form hidden input.
# And check this value, when server get a post request.
# Rogue site do not now this value, so its post request will be rejected.
<input type="hidden" value='random complex value'>


# By default django always checks scrf token, so if you do nothing for csrf
# when you try to submit form you get 403 error
# to disable csrf checking, you can use decorator csrf_exempt
@csrf_exempt
def detail(request):
    ...
    
# Let's include csrf_token
from django.middleware.csrf import get_token

def detail(request):
    response = """
        ...
        <imput type="hidden" name="csrfmiddlewaretoken" value="__token__">
        ...
    """
    
    token = get_token(request)
    respose.replace('__token__', html.escape(token))
    return HttpRespose(response)
    
# Note, when you are in a request handler method the csrf checking is already done. 
# It is doing before generating response    


# You can handle csrf easier. Just include csrf tag into you form
# And it emits input, type=hidden and so on
{% csrf_token %}



# POST - REFRESH PROBLEM
# When you hit refresh you send last request again.
# But if the last request was a post request, usually you don't want to change data on server again.
# Commonly, it is a bad practice return 200 on post request. Usually you should return 302 (redirect)
# Usually, you need to pass some data from post request to new get request, through redirect
# So, server receives post request, handles it. Then send redirect and add data to session (if it's need).
# The browser, gets redirect response, sends get request. 
# The server receive the get request, gets data from session, delete it from session and send response.
class AwesomeView(View):
    def get(self, request):
        msg = request.session.get('msg', False)
        if ( msg ) : del(request.session['msg'])
        return render(request, 'getpost/quess.html', { 'message': msg })
        
    def post(self, request):
        quess = request.POST.get('guess')
        msg = checkguess(guess)
        reqeust.session['msg'] = msg
        return redirect(request.path)