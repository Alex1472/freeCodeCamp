# In python a variable is created the moment you assign a value to it.
# A variable is not need to be declared with any paticular type.
x = 5
x = 'a'

# You can cast variable to any particalar type.
x = 5
s = str(x)
b = bool(x)
i = int(s)

# You can get variable type with type function:
x = 5
print(type(x))

# You can change variable type at runtime:
x = 1
x = 'a'


# VARIABLE NAMING:
# Variables should contain only A-z, 0-9, and _ characters and start with A-z and _.

#correct
myvar = "John"
my_var = "John"
_my_var = "John"
myVar = "John"
MYVAR = "John"
myvar2 = "John"

#incorrect
2myvar = "John"
my-var = "John"
my var = "John"



# ASSIGNING VALUES:
# You can assign multiple values to multiple variables:
a, b, c = 1, 2, 3

# One value to multiple variables:
a = b = c = 1

# You can unpack collection:
data = [1, 2, 3]
a, b, c = data



# OUTPUT:
# You can pass several values in print. You can't concatenate number and string.

x, y, z = 1, 2, 3
print(x, y, z) #correct
print(x + 'aaa') #incorrect



# GLOBAL VARIABLE:
# If function you have access to global variables:
x = 5
def f():
    print(x) #correct
	
f() 


# If you create variable with same name in function you only have access to it.
x = 5
def f():
    x = 3
    print(x)
	
f() #3
print(x) #5


# You can create global variable with keyword global
def f():
    global x
    x = 3
    print(x)
	
f() #3
print(x) #3


#You also can change global variable with work global:
x = 5
def f():
    global x
	x = 3
	print(x)
	
f() #3
print(x) #3



