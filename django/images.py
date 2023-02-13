# If you want to store a file in a db, you should specify two fields in a model
# for binary data and for content-type
picture = models.BinaryField(null=True, blank=True, editable=True)
content_type = models.CharField(max_length=255, null=True, blank=True)

# Also when you send file with a from you should specify the enctype attribute
# It specifies how the form-data should be encoded when submitting it to the server.
<form action="" method="POST" id="upload_form" enctype="multipart/form-data">
...
</form>

# When you create a form with files from client, you should pass files from request
form = CreateForm(request.POST, request.FILES or None)


# You can add image type information to db item before saving by overriding 
# the form method save
def save(self, commit=True):
    instance = super(CreateForm, self).save(commit=False) # get model item
    pic = instance.picture
    
    # Add image information before saving
    if isinstance(pic, InMemoryUploadedFile):
        bytearr = pic.read()
        instance.picture = bytearr
        instance.content_type = pic.content_type
    if commit:
        instance.save()
    return instance
    
    
# HANDLING IMAGE REQUESTS
# You can handle image requeste like this
# urls.py
path('pic_picture/<int:pk>', pic_stream, name='pic_picture')

# views.py
def pic_stream(request, pk):
    pic = get_object_or_404(Pic, pk=pk)

    response = HttpResponse()
    response['Content-Type'] = pic.content_type # specify content type, you should save it into a db
    response['Content-Length'] = len(pic.picture) # content length
    response.write(pic.picture) # picture stores in a db as a binary array
    return response
    
# Then you can add image into template
# detail.html
<img src="{% url 'pics:pic_picture' object.id %}" alt="image">



# MAKE POPUP IMAGE
# You can create additional overlay that shows if you click on image
# and hides if you then click on overlay
<style>
    .overlay {
        width: 100%;
        height: 100%;
        z-index: 10;
        background-color: rgba(0, 0, 0, 0.5);
        position: fixed;
        display: none;
    }
</style>

# overlay
<div id="overlay" class="overlay" onclick="document.getElementById('overlay').style.display = 'none'">
    <img alt="image" src="{% url 'pics:pic_picture' object.id %}" style="width: 90%; margin-top: 100px;">
</div>

# image
<img src="{% url 'pics:pic_picture' object.id %}" alt="image"
                 onclick="document.getElementById('overlay').style.display = 'block';">

