# Module just a python file:
# First string in this file usually is a doc
# You should put the module file near the main py file.

# m1.py:
"""It's m1 module!"""
def f1():
    print('From f1')

def f2():
    print('From f2')

a = 1



#BASE SYNTAX
# You can import module like this:
# main.py
# For import you should type in the name of the module
# When you import module it will be executed.
import m1 # m1 is a special object

print(m1.__doc__) # It's m1 module!
print(m1.__name__) # m1
print(m1.__file__) # Absolute path to file
print(m1.__cached__) # compiled into byte code file

m1.f1() # From f1
m1.f2() # From f2


# You can import only some objects from module:
from m1 import f1

f1()


# You can give alie to module
# The __name__ of the module will not change. 
import m1 as m

m.f1()


# You can give alies to objects you import
# The __name__ of the functions will not changes
from m1 import f1 as f, f2 as g

f()
g()


# If you run module __name__ is always main. You can use it so that some code in your module invoked only 
# when you run module (not import)

# m1.py:
# if m1 runs - __name__ == __main__, if it is imported - __name__ == m1
"""It's m1 module!"""

a = 1

print('Always calls.')

# This if should be at the end of the file
if __name__ == '__main__':
    print('Calls only if the module runs in interpreter.')


# If path to your module not in sys.path, you can't import this module
import sys
print(sys.path)
['d:\\DifferentReps\\PythonProjects\\TestProjects\\TestPythonProject', 
 'C:\\Users\\tula1\\AppData\\Local\\Programs\\Python\\Python310\\python310.zip', 
 'C:\\Users\\tula1\\AppData\\Local\\Programs\\Python\\Python310\\DLLs', 
 'C:\\Users\\tula1\\AppData\\Local\\Programs\\Python\\Python310\\lib', 
 'C:\\Users\\tula1\\AppData\\Local\\Programs\\Python\\Python310', 
 'd:\\DifferentReps\\PythonProjects\\TestProjects\\TestPythonProject\\venv', 
 'd:\\DifferentReps\\PythonProjects\\TestProjects\\TestPythonProject\\venv\\lib\\site-packages', 
 'c:\\Users\\tula1\\.vscode\\extensions\\almenon.arepl-2.0.3\\node_modules\\arepl-backend\\python']
 
 
# You can use constraction:
from m1 import *
# to import all objects from module
# You can specify what objects will be imported define __all__ variable in a module. __all__ works only with star import.
def f1():
    print('From f1')

def f2():
    print('From f2')

a = 1

__all__ = ['f1'] # only the f1 function will be imported with from m1 import *


# Note, if there is some import with the same name of imported object, last erase previous


# Resume:
# Sort imports lexicographically
# first define import module then, from module import something
# Example:
import os
import sys
from collections import OrderedList
from itertools import islice




# PACKAGES
# We can structure files into modules.
# Package is a directory with __init__.py file
# To import package, you should import directory name
# By directory name you get objects from __init__ file (and only from it)

# useful
#     __init__.py

# __init__.py
def init_f():
    print('from init')
    
# main.py
import useful
useful.init_f()


# You can add modules into package
# useful
#     __init__.py
#     foo.py

# foo.py
def foo_f():
    print('from foo')
    
    
# To import the foo module use 
# useful will be imported and foo is attribute of useful.
import useful.foo
useful.init_f()
useful.foo.foo_f()

# You can import just one module
from useful import foo
foo.foo_f()
useful.init_f() # error

# You can also import just one object from module of package
from useful.foo import foo_f
foo_f()
# NOTE, IF YOU IMPORT JUST FOO_F OR JUST MODULE FOO, __INIT__.PY AND FOO.PY WILL BE EXEXUTED. 


# If you import function in module and then import this module, imported function will be visible
# bar.py:
from .foo import foo_f

def bar_f():
    foo_f()
    print('bar function')

# main.py
import useful.bar

useful.bar.foo_f() # works



# from package you can reference current package path with .
# useful
#     __init__.py
#     foo.py
#     bar.py

# bar.py:
from . import foo # . = useful

def bar_f():
    foo.foo_f()
    print('from bar')
    
# or
from .foo import foo_f # . = useful

def bar_f():
    foo_f()
    print('from bar')
    

# You can use .. to refer to parent directory
# useful
#     __init__.py
#     bar
#         __init__.py
#         boo.py
#     foo.py

# boo.py
from ..foo import foo_f # .. - useful

def boo_f():
    foo_f()
    print('bar function')


# Note, when you import some module / package, all parent packages will be created.
# Modules will be added to parent package(begin from useful)
import useful

print(useful.bar) # error
print(useful.bar.boo) # error

from useful.bar.boo import *

print(useful.bar) # created
print(useful.bar.boo) # created

    

# FACADE
# You can import all object from package modules with facade pattern
# useful
#     __init__.py
#     bar
#         __init__.py
#         boo.py
#     foo.py

# useful/bar/__init__.py
from .boo import * # import all functions into useful/bar module

__all__ = boo.__all__ # add functions from boo in case of * import. (boo should define __all__ )

# useful/__init__.py
from .bar import * # import object from bar package and foo module
from .foo import *

__all__ = bar.__all__ + foo.__all__ # for * import, __all__ should be defined in bar and foo



# Modules are cached in sys.modules
# Submodule of a package accessed with full name.
import useful.bar
import sys

print(f'Useful: {"useful" in sys.modules}') # Useful: True
print(f'Useful.bar: {"useful.bar" in sys.modules}') # Useful.bar: True

# All places from where a module can be imported containe in the sys.path attribute
# Note if you load module, and than have changed it, sys.modules won't be updated with next import.
# Old version of the module will be get.



# RUN MODULES
# useful
#     __init__.py
#     bar
#         __init__.py
#         boo.py
#     foo.py

# You can run module as usual file
py ./useful/foo.py
# But can as module
py useful.foo # error
# But you can run it like this:
py -m useful.foo # __name__ == __main__ in this case

# If you want to run a package, you should add __main__.py in it.
# useful
#     __main__.py
#     __init__.py
#     bar
#         __init__.py
#         boo.py
#     foo.py

py -m useful 
# First will be executed __init__.py, then __main__.py









