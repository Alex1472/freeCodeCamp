#Base syntax:
def print_name(fname, lname):
    print(f'{fname} {lname}')

print_name('Tom', 'Soyer')
#By default a function returns None 



# You can add documentation right after a function signature
# Use __doc__ attribute or function help to get it.
def say_hi(text):
    """This function says hi."""
    print(text)

say_hi('test')
print(say_hi.__doc__) # 'This function says hi.'
print(help(say_hi)) # 'This function says hi.'



# ARGUMENTS
# By default, number of parameters in a function should match number of arguments you pass.

# POSITIONAL ARGUMENTS - arguments that go without argument name.
print_name('Tom', 'Soyer') # the function has 2 positional arguments
# KEYWORD ARGUMENTS - arguments that go with parameter names
print_name(fname='Tom', lname='Soyer') # function has two keyword arguments

# POSITIONAL ARGUMENTS SHOULD GO BEFORE KEYWORD ARGUMENTS
print_name(1, 2, fname='Tom', lname='Soyer') #correct
print_name(1, fname='Tom', 2, lname='Soyer') #incorrect

# You should always pass all paraments a function define.
print_name('Tom', 'Soyer') #correct
print_name('Tom', lname='Soyer') #correct
print_name(fname='Tom', lname='Soyer') #correct
print_name('Tom') #incorrect

# You can also pass additional positional arguments. In this case you should add *args in function declaration.
# It is a tuple of arguments
def print_name(fname, lname, *args)
print_name('Tom', 'Soyer', 15, 1900) #args = (15, 1900), args - tuple

# You can also pass additional keyword arguments. In this case you should add **kwargs in function declaration.
# It is a dictionary.
def print_name(fname, lname, **kwargs)
print_name('Tom', 'Soyer', age=15, year=1900) # kwargs = { age: 15, year: 1900 }

# You can also pass additional positional and keyword paramenters. In this case positional paramenters 
# should go before keyword.
def print_name(fname, lname, *args, **kwargs)
print_name('Tom', 'Soyer', 'test', age=15, year=1900) # args=('test',) kwargs={ age: 15, year: 1900 }

# You can specify that parameter should be always keyword, defining it after *args. Usually they are with default values.
def f(a, b, *args, c, **kwargs):
f(1, 3, c=4) # correct
f(1, 3, 4) # error
# Or with *, but in this case you do not have additional positional arguments
def f(x, *, y):
f(1, y=3) # correct 
f(1, 2, y=3) # incorrect


# As a result you can specify argument without default values, some of them you can specify only with name.
def f(a, b, *args, c, **kwargs):
f(1, 2, 3) # incorrect - c should be keyword
f(1, 2, c=3) # correct
f(c=3, 2, 3) # incorrect - positional arguments should go first
f(1, b=2, c=3) # correct
f(a=1, b=2, c=3) # correct
f(c=3, b=2, a=3) # correct - keyword arguments can go in any order



# UNWRAPPING
# You can unwrap positional argument
def f(x, *args):
    print(x)
    print(args)
    
args = [1, 2]
f(*args) # 1 (2,)

# And keyword arguments
def f(x, y, *, z, **kwargs):
    print(x, y, z)
    print(kwargs)

args = {
    'a': 3,
    'z': 4
}
f(1, 2, **args) # 1 2 4 {'a': 3}
# Note, you can unwrap several objects


# DEFAULT PARAMETERS
# Argument can have a default value. 
def f(a, b=2)

# In positional arguments first should go argement without default value.
def f(a, b, e=5, *args): # correct
def f(a, e=5, b, *args): # incorrect

#For positional arguments it's not nessasary
def f(a, b, *args, c=5, d, **kwargs): # correct
def f(a, b, *args, c, d=5, **kwargs): # correct

# Values for default paramenters is initialized only one time when function created.
def test(iterable, seen=[]):
    for x in iterable:
        seen.append(x)
    print(f'seen: {seen}')

l1 = [1, 2, 3]
test(l1) # seen: [1, 2, 3]
test(l1) # seen: [1, 2, 3, 1, 2, 3]
# You can get default values of paramenters with the __defaults__ attribute
print(test.__defaults__) # ([1, 2, 3, 1, 2, 3],)
# You can fix it like this:
def test(iterable, seen=None):
    seen = set(seen or [])



# UNWRAPPING WITH ASSIGNING.
# You can unwrap when assing value
x, y, z = [1, 2, 3]
x, y, z, a = [1, 2, 3] # elements number left and right should be same

# Structure can be more complex. Use paranthess left in this case.
rect = (0, 0), (1, 1)
(x1, y1), (x2, y2) = rect

# You can use * for all other values, this variable will be an array.`
start, *rest = [1, 2, 3, 4, 5]
print(rest) # [2, 3, 4, 5]
# *variable can be on any position
start, *rest, end = [1, 2, 3, 4, 5]
print(rest) # [2, 3, 4]

# This works in for loop:
for x, *y in [[1, 2, 3], [1, 2]]:
    print(x, y)
# 1 [2, 3]
# 1 [2]

# You can update dict with unwrapping
default_settings = { 'a': 1, 'b': 2}
setting = { **default_settings, 'b': 3 } # b will be updated
print(setting) # {'a': 1, 'b': 3}



