# DESCRIPTORS
# __GET__, __SET__, __DELETE__, __SET_NAME__
# You can incapsulate property logic in a class. This class has name descriptor.
# It should implement __get__, __set__ or __delete__ method.

class StringProperty:
#when property is creating the method __set_name__ will be invoked. It receives type of class and attribute name in class instance.
#It is optional.
    def __set_name__(self, owner, name):
        self.prop_name = name

# __get__ receives instance and type class of class where descriptor is determined
# Note that owner is type of instance. (in case of inherinance)
    def __get__(self, instance, owner):
        return instance.__dict__[self.prop_name]

# __set__ receives instance and value
    def __set__(self, instance, value):
        instance.__dict__[self.prop_name] = value

# __delete__ is invoked when you delete property
	def __delete__(self, instance):
        print('deleting')
        del instance.__dict__[self.prop_name]

# You should create descriptor as CLASS ATTRIBUTE.
class Person:
    first_name = StringProperty()
	
# Methods __get__, __set__ will be invoked when you try to access attribute through instance.
person = Person()
person.first_name = 'Tom' # __set__ works
print(person.first_name) # __get__ works
del person.first_name # __delete__ works
	
	

# GET DESCRIPTORS FROM CLASS
# If you try to get first_name with class instance __get__ will be invoked with instance = None, owner = Person
l = Person.first_name 

# If you try to set first_name with class instance descriptor will be changed to value
Person.first_name = 1
print(Person.first_name) # 1

# If you try to delete first_name with class instance descriptor will be removed.
del Person.first_name
print(Person.first_name) # Error, there is no first_name attribute



# DATA, NON-DATA DESCRIPTORS
# If descriptor implements the __set__ method it is data descriptor else it is non-data descriptor.

# 1. Data descriptors.
# Python always at first tries to use data descriptors no matter is there instance attribute with the same name.
class StringProperty:
    def __get__(self, instance, owner): # if data descriptor implements __get__, it will be used. Else python tries to find attribute in __dict__, in class and so on.
        print('get')

    def __set__(self, instance, value):
        print('set')

class Person:
    first_name = StringProperty()

person.__dict__['first_name'] = 'Alex'
person.first_name = 'Tom' # use __set__
name = person.first_name # use __get__


# 2. Non-data descriptors.
# Python at first tries to get instance attribute and then tries to use non-data descriptor.
class StringProperty:
    def __get__(self, instance, owner):
        print('get')

class Person:
    first_name = StringProperty()

person = Person()
person.__dict__['first_name'] = 'Alex'
name = person.first_name # name = 'Alex'
print(name)

del person.__dict__['first_name']
name = person.first_name # use descriptor - 'get'

# Note that with set you will always just set instance attribute.
person.first_name = 'Jack'
print(person.__dict__['first_name']) # 'Jack'



# EXAMPLE: CACHED PROPERTY
# You can use non-data descriptors to calculate and property value. When you first try to get property value, the value will be calculated 
# and added to instance __dict__. Then you will get the property value from the instance __dict__
class cached_property:
    def __init__(self, method):
        self._method = method

    def __get__(self, instance, owner):
        if instance is None:
            return self # return self, if we getting property from class object
        value = self._method(instance)
        setattr(instance, self._method.__name__, value) # setting instance.__dict__[self._method.__name__] = value
        return value


class Nope:
    @cached_property
    def calc(self):
        print('Calculating...')
        return 42

nope = Nope()
print(nope.calc)
print(nope.calc)
print(nope.calc)



# THE PROPERTY IMPLEMENTATION.
# The implementation of properties uses descriptors
class property:
    def __init__(self, fget=None, fset=None, fdel=None): # init to create properties like that: name = property(fget, fset, fdel)
        self._fget = fget
        self._fset = fset
        self._fdel = fdel

    def __get__(self, instance, owner):
        if self._fget is None:
            print('Error')
            return
        return self._fget(instance)

    def __set__(self, instance, value):
        if self._fset is None:
            print('Error')
            return
        return self._fset(instance, value)

    def __delete__(self, instance):
        if self._fdel is None:
            print('Error')
            return
        return self._fdel(instance)
    # decorator to add setter
    def setter(self, fset):
        self._fset = fset
        return self
    # decorator to add deleter
    def deleter(self, fdel):
        self._fdel = fdel
        return self


class Person:
    def __init__(self, name):
        self._name = name

    @property # create property descriptor in Person.name
    def name(self):
        return self._name

    @name.setter # setter - decorator, that addes setter method to descriptor instance in Person.name, 
                 # in setter we should return descriptor to Person.name will be the descriptor
    def name(self, value):
        self._name = value

    @name.deleter # deleler - decorator, that addes deleter method to descriptor instance in Person.name
                  # in setter we should return descriptor to Person.name will be the descriptor
    def name(self):
        del self._name


person = Person('Tom')
print(person.name) # Tom
person.name = 'Jack'
print(person.name) # Jack

print(person.__dict__) # {'_name': 'Jack'}
del person.name
print(person.__dict__) # {}



# METHODS
# We know that there are function and bound methods
class Person:
    def __init__(self, name):
        self._name = name

    def say_hi(self):
        print(f'Hi, my name is {self._name}')

person = Person('Tom')
# We get function when we get ClassName.method_name
print(Person.say_hi) # <function Person.say_hi at 0x00000282691479A0>
# We get bound method when we try to get with instance
print(person.say_hi) # <bound method Person.say_hi of <__main__.Person object at 0x000002826936DE70>>

# How does it work?
# The function is descriptor, and when you try to get method from instance it creates special wrapper MethodType and passes instance into it.
class Funtion:
    def __get__(self, instance, owner):
        if instance is None:
            return self
        else:
            return MethodType(self, instance)
            
# We can get bound method manually
person = Person('Tom')
method = Person.say_hi.__get__(person, Person)
print(method) # <bound method Person.say_hi of <__main__.Person object at 0x00000282691EDE70>>
method() # Hi, my name is Tom

# We can create a bound method manually
sample_method = MethodType(Person.say_hi, person)
print(sample_method) # <bound method Person.say_hi of <__main__.Person object at 0x000002826936EFB0>>
sample_method() # Hi, my name is Tom



# CLASSMETHOD
# You can create class method with descriptors
class Person:
    def __init__(self, name):
        self._name = name

    @classmethod
    def do_something(cls, text): # class method recieves class not instance
        print(text)

# You can access to it with class or instance, but usually you should use class.
Person.do_something('test') # test
person.do_something('test') # test

# Let create the descriptor classmethod
class classmethod:
    # We should return a function without the class paramenter and invoke original function with instance class
    def __get__(self, instance, owner):
        return lambda *args, **kwargs: self._method(owner, *args, **kwargs)
        
        

# STATICMETHOD
# We can define the staticmethod that do not accepts neither instance nor class
class Person:
    @staticmethod
    def say_something(text):
        print(text)
        
# You can access to it with class or instance, but usually you should use class.
person = Person('Tom')
person.say_something('test')
Person.say_something('test')

# We can create the static method descriptor like this:
class staticmethod:
    def __init__(self, method):
        self._method = method
    # We should always return an original function
    def __get__(self, instance, owner):
        return self._method
