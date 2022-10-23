# CLASSES IN PYTHON
# For class names we use CamelCase format.
# Class is an object in Python. When you declare a class, python create an object with the same name as the class name.
# This object has class named type.
class HtmlDocument:
    pass

print(type(HtmlDocument)) # <class 'type'>

print(isinstance(HtmlDocument, type)) # HtmlDocument is instance of class type
print(isinstance(type, type)) # type is instance of class type



# INSTANCE ATTRIBUTES
# You can initialize INSTANCE ATTIBUTES in a constructor. The first parameter always should be
# a self(it can has another name but usually it is self), that is an object.
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

# You can get counter value using class or object`
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

# Python allows you to get access of CLASS ATTRIBUTES. It looks up variable in instance __dict__, then it class __dict__.
# If it will find variable in instance __dict__, it won't find variable in class __dict__.
class HtmlDocument:
    default_name = 'default document'

    def __init__(self, name):
        self.name = name

document = HtmlDocument('document1')
print(document.default_name) # 'default document'



# GETATTR, SETATTR, DELATTR
# You can get attributes with this method, it's equvalent to dot notation for instanses and classes.
You can get CLASS ATTIBUTE value with gettattr function or dot notation:
class HtmlDocument:
    extention = 'html'
    version = 1

print(getattr(HtmlDocument, 'version')) # 1
print(HtmlDocument.version) # 1

setattr(HtmlDocument, 'test_attr', 3)
print(HtmlDocument.test_attr)
HtmlDocument.test_attr = 4
print(HtmlDocument.test_attr)

delattr(HtmlDocument, 'test_attr')
del HtmlDocument.test_attr # the same as delattr
print(HtmlDocument.test_attr) # error



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

#Note that you can even add method to class in runtime.
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



# __GETATTR__, __SETATTR__, __DELATTR__ MAGIC METHODS
# __getattr__ is invoked when you try to get not existing attribute
# __setattr__, __delattr__ is invoked always
# Note, when you try to get attribute with __dict__ these methods aren't invoked.
class Person:
    def __getattr__(self, name):
        print('get', name)

    def __setattr__(self, name, value):
        print('set', name, value)
        super().__setattr__(name, value) # we should set the base method otherwise the attribute won't be set

person = Person()
person.name = 'Tom' # set name Tom
print(person.name) # __getattr__ isn't invoked as attribute existes
print(person.age) # __getattr__ is invoked get age

#Note, if there is class attribute __getattr__ method isn't invoked.
  


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







