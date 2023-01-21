# COOKIES
# cookie is a key - values pair, that you can add to responce
# then browser should keep this pair and send it with every 
# request to this server
# A cookie send to server in a cookie header
Cookie: sessionid=vhefvsg79q2kxhu15mdxx00jbeg7j5fr; dj4e_cookie=bf8d643e

# When server sents cookies they are send as Set-Cookie header
# Note, you ususally set cookie just once, the browser will send it with every request
# Cookie has several parameters (expires, Max-Age, Path, ...)
Set-Cookie:  dj4e_cookie=bf8d643e; expires=Wed, 04 Jan 2023 17:34:38 GMT; Max-Age=1000; Path=/
Set-Cookie:  sessionid=vhefvsg79q2kxhu15mdxx00jbeg7j5fr; expires=Wed, 18 Jan 2023 17:17:58 GMT; HttpOnly; Max-Age=1209600; Path=/; SameSite=Lax

# You can specify expiration date for a cookie. By default a cookie will be removed when you close a browser (session cookies)


# In django you can manage cookies with request / responce object in a view
def hello(request):
    print(request.COOKIES['sessionid']) # get sessionid cookie from the request
    resp = HttpResponse('view count='+str(num_visits))
    resp.set_cookie('dj4e_cookie', 'bf8d643e', max_age=1000) # set dj4e_cookie cookie
    return resp
    
    
    
    
# SESSIONS
# Django support sessions. Session is a storage on server with some key-values pairs
# When a browser make first request on a server, the server(django) sets a cookie
# sessionid with random values vhefvsg79q2kxhu15mdxx00jbeg7j5fr. It is an identifier for the client
# Django creates a storage for this client
# You can look at it in the django_session table in a database
vhefvsg79q2kxhu15mdxx00jbeg7j5fr|eyJ4IjoyfQ:1pD7k7:TpsPDeD304xQBimRhEdXqw8xt7mMdlO6tbtB--LbcdY|2023-01-18 17:39:59.972571  # key, value, expiration date
# value is a dictionary serialization the base64 format.

# With next request, browser sends sessionid cookie, and django (before urls.py), sets session storage to request for this client

# You can manage session data with request.session dictionary
def hello(request):
    num_visits = request.session.get('num_visits', 0) + 1 # get data
    request.session['num_visits'] = num_visits # update data in the session
    if num_visits > 4:
        del request.session['num_visits']
    return HttpResponse('view count='+str(num_visits))