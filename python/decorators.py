# FUNCTIONS:
# You can wraps one function into another. The wrapper function is called decorators.
def logger(fn): # Decorator, accepts raw function, return wrapper.
    def wrapper(*args, **kwargs):
        print(fn.__name__)
        return fn(*args, **kwargs)
    return wrapper

# You can use it explicitly.
def say_hello(name):
    """This function says hello."""
    print(f'Hello, my name is {name}.')

wrapper = logger(say_hello)
wrapper('Tom')

# But usually we use syntax sugar.
@logger # apply decorator to the function say_hello
def say_hello(name):
    print(f'Hello, my name is {name}.')

say_hello('Tom') #it's wrapper



# There is problem that we loose name and doc of the original function.
print(wrapper.__name__) # wrapper
print(wrapper.__doc__) # None

# We can fix this with the wraps decorator from the functools module.
def logger(fn):
    @wraps(fn)
    def wrapper(*args, **kwargs):
        print(fn.__name__)
        return fn(*args, **kwargs)
    return wrapper

print(wrapper.__name__) # say_hello
print(wrapper.__doc__) # 'This function says hello.'



# If we want that decorator accepts parameters, we should create functions that return decorator, that returns wrapper.
def star(n):
    def decorate(fn):
        @wraps(fn)
        def wrapper(*args, **kwargs):
            print('*' * n)
            res = fn(*args, **kwargs)
            print('*' * n)
            return res
        return wrapper
    return decorate

@star(5)
def say_hello(name):
    """This function says hello."""
    print(f'Hello, my name is {name}.')

say_hello('Tom')
"""
*****
Hello, my name is Tom.
*****
"""



# You can wrap function with class and make it callable
class Logger:
    def __init__(self, fn):
        self.fn = fn

    def __call__(self, *args, **kwargs):
        print(self.fn.__name__)
        return self.fn(*args, **kwargs)

@Logger # we wraps the function say hello with the Logger class
def say_hello(name):
    """This function says hello."""
    print(f'Hello, my name is {name}.')

print(Logger) # <class '__main__.Logger'>
say_hello('Tom')
"""
say_hello
Hello, my name is Tom.
"""



# You can use class for decoration with paramenters.
# You should create class that accepts in constructor paramenter and implement __call__ method that wraps a function.
# And decorate function with instance of class.
class Star:
    def __init__(self, n):
        self.n = n
    
    def __call__(self, fn):
        @wraps(fn)
        def wrapper(*args, **kwargs):
            print('*' * self.n)
            res = fn(*args, **kwargs)
            print('*' * self.n)
            return res
        return wrapper 

@Star(10) # Will be created an instance of Star, then will be invoked the __call__ method to decorate the say_hello function.
def say_hello(name):
    """This function says hello."""
    print(f'Hello, my name is {name}.')

say_hello('Tom')
"""
**********
Hello, my name is Tom.
**********
"""



# By the way decorator can return any object not always funtion.
def empty(fn):print(identity(1))

    return 0

@empty
def say_hello(name):
    """This function says hello."""
    print(f'Hello, my name is {name}.')

print(say_hello) # 0
print(say_hello == 0) # True



# You can use several decorators with one function. Write them on lines above
def add_one(fn):
    @wraps(fn)
    def wrapper(*args, **kwargs):
        return fn(*args, **kwargs) + 1
    return wrapper

def square(fn):
    @wraps(fn)
    def wrapper(*args, **kwargs):
        return fn(*args, **kwargs) ** 2
    return wrapper

@add_one
@square
def identity(x):
    return x

print(identity(1)) # 2 first will be invoked square, then add_one
#It's equivalent to:
identity = add_one(square(identity))



FUNCTOOLS
You can use LRU_CACHE to cache last several function calls. You can specify this with the maxsize parameter.

from functools import lru_cache

@lru_cache(maxsize=250)
def identity(x):
    return x

for i in range(5):
    for j in range(100):
        identity(j)

print(identity.cache_info()) # CacheInfo(hits=400, misses=100, maxsize=250, currsize=100)
# hits - how many times cached values was used.
# misses - how many times the function was calculated


With function PARTIAL you can fix some function arguments (positional or keyword)
import functools

def multiply(a, b):
    return a * b

times2 = functools.partial(multiply, 2)
times3 = functools.partial(multiply, 3)
times4 = functools.partial(multiply, b=4)

print(times2(3)) # 6
print(times3(3)) # 9 
print(times4(5)) # 20


You can add different behaviour for function depending on its arguments type with singledispatch.
You can decorate base behaviour with the singledispatch decorator.
The this function get the decorator registor and you should use it to add behavior for specify types.
import functools

@functools.singledispatch # specify base behaviour
def say_type(obj):
    print('Unknown type')
    
@say_type.register(int) # use register decorator to specify behaviour for the int type
def a(obj):
    print('int')

@say_type.register(str) # use register decorator to specify behaviour for the int type
def b(obj):
    print('str')

say_type(1) # int
say_type('eee') # str
say_type([]) # Unknown type


In functools there is the reduce function that accumulate a value from a sequence.
res = functools.reduce(lambda acc, x: acc * 10 + int(x), '1234', 0) # 0 - initial value
print(res) # 1234
print(type(res)) # int

res = functools.reduce(lambda acc, x: acc + x, [1, 2, 3, 4])
print(res) # 10



# You can also apply decorators to class. In this a function receives class instance and returns something. And that object will be in class variable
def obsolete(cls): # decorator will return the same class, but changes the constructor so that it print warning.
    old_init = cls.__init__

    @wraps(cls.__init__)
    def new_init(self, *args, **kwargs):
        print('This class is obsolete.')
        old_init(self, *args, **kwargs)
    cls.__init__ = new_init
    return cls

@obsolete
class A:
    pass

a1 = A() # 'This class is obsolete.'
a2 = A() # 'This class is obsolete.'



from functools import wraps
# singleton decorator allow to create just one instance of object.
def singleton(cls):
    instance = None
    
    # we change object in A to function
    # wraps also copy __dict__ from class to function
    @wraps(cls)
    def factory(*args, **kwargs):
        nonlocal instance
        if instance is None:
            instance = cls(*args, **kwargs)
        return instance
    return factory


@singleton
class A:
    pass


a1 = A()
print(id(a1))
a2 = A()
print(id(a2))






