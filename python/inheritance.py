# CLASS, __INIT__, SUPER
# You can inherite one class from another:
# You try to find attributes in instance __dict__, class __dict__, parent class __dict__ and so on.
class Person:
    def __init__(self, name):
        self.name = name

    def greeting(self):
        print(f'Hi, my name is {self.name}')

class Employee(Person):
    def __init__(self, name, job_title):
        
        super().__init__(name)
        self.job_title = job_title
		
# name and job_title attibutes contains in instance __dict__
employee = Employee('Tom', 'Python Developer')
print(employee.__dict__) # {'name': 'Tom', 'job_title': 'Python Developer'}

# Note, python tries to find constructor with the same number of paramenter
# in class object, then in parent etc
# Also, object has constructor without paramenters
# So, you always can create object without parameters



# SUPER
# super is a build-in function, that return special object with parent methods and attributes. 
# It's a bounded method so you shouldn't pass instance.
# super takes first argument of the function (it's instance for methods), takes current class,
# create special object and returns it
# you can use the super function in any method
def test(self):
    super().test() 
    
# If we don't call super, base method (and constructor too) won't be invoked.

# super also can takes 2 parameters: 
# 1) class from which to find method (find in its parents)
# 2) instance of object
class A:
    def test(self):
        print("A")

class B(A):
    def test(self):
        print("B")

class C(B):
    def test(self):
        print("C")
        
c = C()
super(C, c).test() # B
super(B, c).test() # A 



# ISINSTACE, ISSUBCLASS
# To check if object is instance of a class, use the function isinstance(object, class)
print(isinstance(person, Person)) # True
print(isinstance(person, Employee)) # False
print(isinstance(employee, Person)) # True
print(isinstance(employee, Employee)) # True

# To check if one class is subclass of another use the function issubclass
print(issubclass(Employee, Person)) # True

# Any class is subclass of object.
print(issubclass(Person, object)) # True



# MULTIPLE INHERITANCE, MRO
# In python you can use multiple inheritance, but you should not.
class A:
    def say_a(self):
        print('a')

class B:
    def say_b(self):
        print('b')

class C(A, B):
    def say_c(self):
        print('c')

c = C()
c.say_a()
c.say_b()
c.say_c()

# The mro(method resolution order) method return list that describes 
# in witch order python will find attribute name in class instances.
print(C.mro()) # [<class '__main__.C'>, <class '__main__.A'>, <class '__main__.B'>, <class 'object'>]



# MIXINS
# You can use multiple inheritance to add some behavior to class
# This class called mixin. 
# It's not make sence to create it on his own. Only as a parent of another class.
# Mixin just add / change some functionality to class
class Tester:
    def test(self, x):
        return

class LoggedMixin: # adds loggin behavior for test
    def test(self, x):
        print(x)
        res = super().test(x) # we do not inherit mixin from any class, but call super.
        print(res)
        return res

class LoggedTester(LoggedMixin, Tester): # first should goes mixin, because first class to search a method goes first in the brackets
    pass

loggedTester = LoggedTester()
loggedTester.test(42)



# CLASS DECORATORS
# You can decorate class. Decorator, in this case, function from class to class
# This way, you also can change, add behavior for class

# Example, add logging for tester
def logger(cls):
    test_org = cls.test # as we search attribute dynamically, we should save original function

    def loggedTest(self, *args, **kwargs):
        print('Input :', *args, **kwargs)
        res = test_org(self, *args, **kwargs)
        print(f'Result: {res}')
        return res

    cls.test = loggedTest
    return cls

@logger # appling decorator
class Tester:
    def test(self, x):
        return x
        
        
# Example, singleton
def singleton(cls):
    instance = None

    @functools.wraps(cls)
    def inner(*args, **kwargs):
        nonlocal instance
        if instance is None:
            instance = cls(*args, **kwargs)
        return instance
    return inner

@singleton
class Tester:
    def test(self, x):
        return x


# Another example, deprecated
def deprecated(cls):
    org_init = cls.__init__

    functools.wraps(org_init)
    def new_init(self, *args, **kwargs):
        print('Deprecated!')
        org_init(self, *args, **kwargs)

    cls.__init__ = new_init
    return cls

@deprecated
class Counter:
    def __init__(self, initial):
        self.value = initial        
        
        

