# You can exclude sevaral fields from admin interface:
class PicAdmin(admin.ModelAdmin):
    exclude = ['picture', 'content_type']


admin.site.register(Pic, PicAdmin)