# Python converts into bite code
# Then an interpeter is used.
# There are many interperters: cpython (most popular), jython, python stackless, pypy

# Code of the function contains in the attribute __code__
# You even can assign code of one function to another.
# Number of arguments will also changes
def increment(x):
    return x + 1

def add(x, y):
    return x + y

print(add(3, 4)) # 7

add.__code__ = increment.__code__
print(add(2)) # 3



#DIR FUNCTION
# dir enumerates which attribute there is in object(with class instance).
class Person:
    def __init__(self, name):
        self._name = name

    def do_something(self):
        print('something')


person = Person('Tom')
print(dir(person))
# ['__class__', '__delattr__', '__dict__', '__dir__', '__doc__', '__eq__', '__format__', '__ge__', '__getattribute__', 
# '__gt__', '__hash__', '__init__', '__init_subclass__', '__le__', '__lt__', '__module__', '__ne__', '__new__', '__reduce__', 
# '__reduce_ex__', '__repr__', '__setattr__', '__sizeof__', '__str__', '__subclasshook__', '__weakref__', '_name', 'do_something']
print(dir(Person))
# ['__class__', '__delattr__', '__dict__', '__dir__', '__doc__', '__eq__', '__format__', '__ge__', '__getattribute__', '__gt__', 
# '__hash__', '__init__', '__init_subclass__', '__le__', '__lt__', '__module__', '__ne__', '__new__', '__reduce__', 
# '__reduce_ex__', '__repr__', '__setattr__', '__sizeof__', '__str__', '__subclasshook__', '__weakref__', 'do_something']



# DATA TYPE

# NONE
# There is a class NoneType in python. It's a singleton (there is the only one instance of this class).
x = None
print(type(None)) # <class 'NoneType'>
print(None == None) # True
print(None is None) # True reference equility

#LOGICAL
False, True

#NUMBERS
int
float
complex



# You can concatenate lists:
a = [1, 2]
b = [3, 4]
c = a + b # [1, 2, 3, 4]

#copy list
a = [1, 2]
b = a[:]



# You can use operators instead of method for sets
xs = {1, 2, 3, 4}
ys = {4, 5}
print(xs & ys) # {1} instead of xs.intersection(ys)
print(xs | ys) # {1, 2, 3, 4, 5} instead of xs.union(ys)
print(xs - ys) # {1, 2, 3} instead of xs.difference(ys)



#RANGE
# range is a function returns a special object range. To give values you can use list
# three arguments: start value, end value (not contained), step
print(type(range(5))) # <class 'range'>
print(list(range(5))) # [0, 1, 2, 3, 4]
print(list(range(5, -1, -1))) # [5, 4, 3, 2, 1, 0] step can be negative



# REVERSED
# You can use the reversed function to reverse collection. Reverse return special object.
# Use list to to get values from it.
print(type(reversed([1, 2, 3]))) # <class 'list_reverseiterator'>
print(list(reversed([1, 2, 3]))) # [3, 2, 1]