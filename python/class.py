# CLASSES IN PYTHON
# For class names we use CamelCase format.
# Class is an object in Python. When you declare a class, python create an object with the same name as the class name.
# This object has class named type.
class HtmlDocument:
    pass

print(type(HtmlDocument)) # <class 'type'>

print(isinstance(HtmlDocument, type)) # HtmlDocument is instance of class type
print(isinstance(type, type)) # type is instance of class type



# CLASS AND INSTANCE BUILD-IN ATTRIBUTES
class Noop:
    "It's a Noop class."

print(Noop.__name__) # Noop
print(Noop.__doc__) # It's a Noop class.
print(Noop.__module__) # __main__ - module, where class declared
print(Noop.__bases__) # (<class 'object'>,) - base classes

noop = Noop()
print(noop.__class__) # <class '__main__.Noop'> - object of class (Noop)



# PRIVATE ATTRIBUTES
# use _ to specify that an attribute is private
# But it's only a convention, you have access to it
self._initial = initial

# you can also start an attribute with __
# In this case you only can get it as _ClassName__attName
self.__initial = initial

counter1 = Counter()
print(counter1._Counter__initial)



# INSTANCE ATTRIBUTES
# You can initialize INSTANCE ATTIBUTES in a constructor. The first parameter always should be
# a self, that is an object.
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
		
person = Person('Tom', '20')
print(person.name)
print(person.age)

# You can add instance attibute to the object in any moment.
person.test = 7
print(person.test)
# You can set new value to an instance attibute.
person.name = 'Ted'
# You can delete instance attibute:
del person1.name
print(person1.name) #error



# CLASS ATTRIBUTES
# You can define CLASS ATTIBUTES. It will be shared by all instances of the class. You should define it in a class:
class Person:
    counter = 0 # class attribute
    
    def __init__(self, name, age):
        self.name = name
        self.age = age
        Person.counter += 1 # You should use class here to get counter`
    
    def sayHi(self):
        print(f'Hi, my name is {self.name}')


person1 = Person('Tom', 20)
person2 = Person('Jhon', 21)

# You can get counter value using class or object.
print(Person.counter)
print(person1.counter)



# BOUNDING ATTRIBUTES
# CLASS ATTRIBUTES are bounded to class, INSTANCE ATTRIBUTES are bounded to a specific instance of a class.
# Instance of a class also has __dict__ attribute that stores the instance attribute. 
# But it is a dict, so you can use it to change instance attributes.
# When you try to get instance variable it looks up it in __dict__ of instance. 
class HtmlDocument:
    def __init__(self, name):
        self.name = name

document = HtmlDocument('document1')
print(type(document.__dict__)) # dict
print(document.__dict__) # {'name': 'document1'}

# Python allows you to get access of CLASS ATTRIBUTES. It looks up variable in instance __dict__, then in a class __dict__.
# If it will find variable in instance __dict__, it won't search variable in class __dict__.
class HtmlDocument:
    default_name = 'default document'

    def __init__(self, name):
        self.name = name

document = HtmlDocument('document1')
print(document.default_name) # 'default document'

# You can also get a __dic__ from the object with the method VARS
print(vars(document)) # return dict with object attributes



# __SLOTS__
# By default, python looks up an attribute in instance __dict__. But dictionary has memory overhead.
# You can use another data structure with less memory consumption. But you will be unable to add attrubutes in runtime
# In __slots__ attrubute of a class you should define which attributes has an instance.
class Point:
    __slots__ = ('x', 'y') # we have to attibutes x, y.
    def __init__(self, x, y):
        self.x = x
        self.y = y
		
# In this case we will not have __dict__.
point = Point(1, 2)
print(point.__dict__) # error
point.z = 3 # error

# Note, slots works with descriptors
print(vars(Point))
# {
#     '__module__': '__main__', 
#     '__slots__': ['x', 'y'], 
#     '__init__': <function Point.__init__ at 0x000001CB3A334550>, 
#     'x': <member 'x' of 'Point' objects>, 
#     'y': <member 'y' of 'Point' objects>, 
#     '__doc__': None
# }
print(type(Point.x))
# <class 'member_descriptor'>



# INSTANCE AND CLASS METHODS
# You can define INSTANCE METHODS in a class. First parameter should be self. You invoke this method without this parameter.
class Person:
    def __init__(self, name):
        self.name = name
    
    def sayHello(self):
        print(f'Hi! My name is {self.name}')

# Note that the method contained in class object is a function
# When you try to get the method with instance you get a bound method
# It works with descriptors and will be discussed later.
person = Person('Tom') 
print(Person.sayHello) # <function Person.sayHello at 0x00000282690F5EA0>
print(person.sayHello) # <bound method Person.sayHello of <__main__.Person object at 0x00000282690DDC60>>
print(Person.sayHello == Person.__dict__['sayHello']) # True

# If you call method on instance, you shouldn't pass object. But for class - should.
person.sayHello()
Person.sayHello(person)


# Note that you can even add method to class in runtime.
# And it will work as if it was defined in class.
def say_something(self):
    print(self)
    print('something')

class Person:
    def __init__(self, name):
        self.name = name
    
    def sayHello(self):
        print(f'Hi! My name is {self.name}')

person = Person('Tom')
Person.say_something = say_something
Person.say_something(None)
# None
# something
person.say_something()
# <__main__.Person object at 0x0000028269168B20>
# something

#Note, that no matter what you try to find attribute or method, for founding will be used the name.
class Person:
    def __init__(self, name):
        self.name = name
    
    def sayHello(self):
        print(f'Hi! My name is {self.name}')

person = Person('Tom')
person.sayHello()
person.__dict__['sayHello'] = 1 # create attribute in instance dict. Python will not look in class __dict__
print(person.sayHello) # 1
print(person.__dict__) {'name': 'Tom', 'sayHello': 1}



# MAGIC METHODS
# Magis method is a method that specify some behavior for python
# They have view as __methodname__

# __GETATTR__, __SETATTR__, __DELATTR__
# __getattr__ is called when there is no attribute we try to get
# __setattr__ is called on EVERY set
# __delattr__ is called on every del
# note, this magic method only works with attribute
# if you try to get, set, delete values with __dict__ they aren't work

class Counter:
    def __init__(self, initial):
        self.value = initial

    def __getattr__(self, name):
        print(f"Unknown attribute {name}")
        return 42 # what return
        
    def __setattr__(self, name, value):
        print(f'Setting {name} value = {value}')
        super().__setattr__(name, value)

    def __delattr__(self, name):
        print(f'Deleting {name}')
        super().__delattr__(name)

counter = Counter(1)
print(counter.value) # 1
print(counter.test) # "Unknown attribute test" "42"
counter.test = 3 # Setting test value = 3
del counter.test # Deleting test
counter.__dict__['data'] = 1 # do not work

# Example
# Specify attribute use can't set
class Guarded:
    guarded = []

    def __setattr__(self, name, value):
        assert name not in self.guarded 
        super().__setattr__(name, value)

class Noop(Guarded):
    guarded = ['test']
    
noop = Noop()
noop.date = 1 # OK
noop.test = 3 # AssertionError


# Example, use dict via attributes
# Create a dict, but setting, getting and deleting value with attributes
class Bunch:
    def __init__(self, d={}):
        self.__dict__['d'] = d

    def __getattr__(self, name):
        return self.d[name]

    def __setattr__(self, name, value):
        self.d[name] = value

    def __delattr__(self, name):
        del self.d[name]


bunch = Bunch({'a': 1, 'b': 2})
print(bunch.a) # 1
print(bunch.b) # 2
bunch.c = 'test'
print(bunch.c) # 'test'

# also there are corresponding functions GETATTR, SETATTR, DELATTR
# for getattr you can specify value to return if there is no this attibute
# You can get attributes with this method, it's equvalent to dot notation for instanses and classes.
class Noop:
    def __init__(self, a):
        self.a = a

noop = Noop(1)
print(getattr(noop, 'a')) # 1
print(getattr(noop, 'b', 42)) # 42
setattr(noop, 'b', 12) 
print(getattr(noop, 'b')) # 12
delattr(noop, 'b')
getattr(noop, 'b') # error, there is no such attribute



# COMPARATION OPERATORS
# You can specify method __eq__, __ne__, __lt__, __le__, __gt__, __ge__
# By default, for each operator you should specify corresponding method
import functools

# You can only specify __eq__ and __lt__ and use the total_ordering decorator
# to use all operators
@functools.total_ordering
class Counter:
    def __init__(self, initial=0):
        self._value = initial

    def __eq__(self, other):
        return self._value == other._value

    def __lt__(self, other):
        return self._value < other._value


counter1 = Counter(5)
counter2 = Counter(7)

print(counter1 == counter2) # False
print(counter1 < counter2) # True



# __CALL__ method
# You can call an object, if you have specified the method __call__
class Nope:
    def __call__(self, x):
        print("You are calling Nope.")
        print(f'x = {x}')

nope = Nope()
nope(14)

# You can implement decorator as class with the __call__ method
class star:
    def __init__(self, n):
        self._n = n

    def __call__(self, f):
        @functools.wraps(f)
        def inner(*args, **kwargs):
            print('*' * self._n)
            res = f(*args, **kwargs)
            print(res)
            print('*' * self._n)
            return res
        return inner

@star(10) # Create object and then call it with the function
def identity(x):
    return x

identity(2)



# __STR__, __REPR__
# __str__ returns human string representation of the object
# __repr__ returns unique presentation of object. With it you can recreate object
class Counter:
    def __init__(self, initial=0):
        self._value = initial

    def __str__(self):
        return f'Counted to {self._value}'

    def __repr__(self):
        return f'Counter({self._value})'

counter = Counter(5)

print(str(counter)) # Counted to 5
print(repr(counter)) # Counter(5)



# __FORMAT__
# You can specify object format in format, f strings
class Counter:
    def __init__(self, initial=0):
        self._value = initial

    def __format__(self, format_spec):
        return self._value.__format__(format_spec)

counter = Counter(5)
print(f'{counter:b}') # binary representation



# __hash__
# By default hashes are equal if objects are same
# Implement __eq__ with the __hash__ method.
# Do not implement __hash__ for mutable objects.



# __bool__
# To cast object to bool implement the method __bool__
# By default, object is always true.
def __bool__(self):
    return bool(self._value)


 


You can define CLASS METHOD. It will be shared by all instances of the class. You should use decorator @classmethod for this. You 
can use this method with class or object. Python passes object of class into class method.

class Person:
    counter = 0
    
    def __init__(self, name, age):
        self.name = name
        self.age = age
        Person.counter += 1
    
    def say_hi(self):
        print(f'Hi, my name is {self.name}')

    @classmethod
    def create_anonymous(cls): # cls - class object
        return Person('anonymous', 22)


person1 = Person('Tom', 20)

anonymous1 = Person.create_anonymous() # use with class
anonynous2 = person1.create_anonymous() # use with object






You also can define STATIC METHOD. It is not bounded to class or object. It is usually used to group logically related function in class.
Note: but you can get it from class or object.

class TemperatureConverter:
    @staticmethod
    def celsius_to_fahrenheit(c):
        return 9 * c / 5 + 32

    @staticmethod
    def fahrenheit_to_celsius(f):
        return 5 * (f - 32) / 9    

temperature_converter = TemperatureConverter()
t1 = temperature_converter.celsius_to_fahrenheit(1)
t2 = TemperatureConverter.celsius_to_fahrenheit(1)



You can define string representation of object with a __str__ method.

class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
    
    def __str__(self):
        return f'My name is {self.name}' 

person = Person('Tom', 20)
print(person) # 'My name is Tom'







