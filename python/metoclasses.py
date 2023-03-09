# METACLASS
# Note, that the class object is an instance of type
# As st is an instance of Something, as Something class is an instance of type
# Let's call type METACLASS - class that create classes
class Something:
    attr = 1

st = Something()
print(type(st)) # <class '__main__.Something'>
print(type(Something)) # <class 'type'>



# CLASS CREATING
# You can create a class with type constructor
# You should pass into it class name, base classes, class attributes

name, bases, attrs = 'Something', (), {'attr': 1}
Something = type(name, bases, attrs)
# It's the same as
# class Something:
#     attr = 1

st = Something()
print(st.attr) # 1
print(Something.attr) # 1



# CREATE METACLASS
# You can create your metaclass by inheriting it from type
# This metaclass can have method, they bound to class as an instance of metaclass
# You can use them with classes, not with class instances
# So we try to find attributes in instance, in creator and in it parents, not in creator of creator
class Meta(type):
    def do_something(cls): # we get an instance of metaclass - class`
        print(cls) 

class Something(metaclass=Meta): # specify metaclass
    pass


st = Something()
Something.do_something() # works, Something
st.do_something() # error

object ->  type  ->  Meta
                      |
                     \ /
object -> parent -> class
                      |
                     \ /
                   object
                     
                     
                     
# PROCESS OF CREATING CLASS
# We have this example:
class Meta(type):
    pass

class Base:
    pass

class Something(Base, metaclass=Meta):
    def __init__(self, attr):
        self.attr = attr

    def do_something(self):
        pass

# It contains of 3 steps
# 1) Determine metaclass - looks at class and parents declaration
#    If you specify more then 1 metaclass there - error

# 2) Create __dict__ for class
#      a) create dict
#         we use MetaClass.__prepare__ method
clsdict = Meta.__prepare__('Something', (Base, ))

#      b) Fill the clsdict.
#         For this execute class body and save it into clsdict
body = """
def __init__(self, attr):
    self.attr = attr

def do_something(self):
    pass
"""
exec(body, globals(), clsdict)

# 3) Create a class
Something = Meta("Something", (Base,), clsdict)



# __NEW__ METHOD
# __new__ method create an object, that we initialize in the __init__ method
# base implementation is in object
# Note, new is a staticmethod, so we should pass cls object into it explicitly
class Something:
    def __new__(cls, *args, **kwargs): 
        print('__new__', *args, **kwargs) # __new__ 4
        return super().__new__(cls)

    def __init__(self, n):
        self._n = n

st = Something(4)



# METACLASS EXAMPLE
class UselessMetaclass(type):
    def __new__(metacls, name, bases, clsdict):
        print(type(clsdict))
        print(list(clsdict))
        return super().__new__(metacls, name, bases, clsdict)

    @classmethod
    def __prepare__(metacls, name, bases):
        return OrderedDict()
        
        
class Something(metaclass=UselessMetaclass):
    attr1 = 1
    attr2 = 2

st = Something()



# ABCMETA
# With abc.ABCMeta you can create abstract class
# And specify some abstract methods with abc.abstractmethod
# It will blows up if you do not specify abstract method in the heir
# But it's only blows up when you creating instance of the heir
import abc

class Iterable(metaclass=abc.ABCMeta):
    @abc.abstractmethod
    def __iter__(self):
        pass

class Something(Iterable):
    def __iter__(self): # specify abstract method
        pass

something = Something() # blows up only here without the __iter__ method
                     
                     