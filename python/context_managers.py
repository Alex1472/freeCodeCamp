# You can magage resources with context managers
# Example
with acquire_resource() as r:
    do_something(r)
    
# acquire_resource() creates context manager
# It should implement __enter__ and __exit__ methods.
# __enter__ return resource saved in r
# __exit__ is invoked after with block
# It acceptes exception type, exception and tracebace (None if there is no exception)
# __exit__ should release resources, it can return true to suppress an exception
# It works like this:
manager = acquire_resource()
r = manager.__enter__()
try:
    do_something(r)
finally:
    exc_type, exc_value, tb = sys.exc_info()
    suppress = manager.__exit__(exc_type, exc_value, tb)
    if exc_value in not None and not suppress:
        raise exc_value
        
# Example
class test_context_manager:
    def __enter__(self):
        print('Context manager enter.')
        return 1

    def __exit__(self, exc_type, exc_value, traceback):
        print('Context manager exit.')

with test_context_manager() as num:
    print(f'With body. Num = {num}')
# Context manager enter.
# With body. Num = 1
# Context manager exit.


# You can use several context managers:
with acquire_resource() as r, acquire_another_resource() as other:
    do_something(r, other)
# If context managers on other lines, you should write \
with acquire_resource() as r, \ 
    acquire_another_resource() as other:
    do_something(r, other)

# You can use manager context without a resoure variable
with acquire_resource():
    do_something()
    
    
    
    
# EXAMPLES    
# Example, automatically close file
from functools import partial

class opened:
    def __init__(self, path, *args, **kwargs):
        self.opener = partial(open, path, *args, *kwargs)

    def __enter__(self):
        self.handle = self.opener()
        return self.handle

    def __exit__(self, exc_type, exc_value, traceback):
        self.handle.close()
        del self.handle

with opened('input.txt', 'r') as f:
    for l in f:
        print(l)
# Note, files in python have already implemented context manager interface

# There is module for working with tempfile. When with block will end, a file will be deleted
import tempfile

with tempfile.TemporaryFile() as tf:
    path = tf.name
    print(path)
    
    
    
# MODULE CONTEXTLIB
# closing
# if object has a method close but do not support context manager interface, you can use closing
from contextlib import closing
from urllib.request import urlopen

url = 'http://eurosport.com'
with closing(urlopen(url)) as page:
    print(page)


# redirect_stdout
# you can temperary redirect stdout during a block with with redirec_stdout 
from contextlib import redirect_stdout
import io

handle = io.StringIO()
with redirect_stdout(handle):
    print('Hello')

print(handle.getvalue())

