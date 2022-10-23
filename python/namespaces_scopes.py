# It seems that namespace and scopes works in python as it javascript. Function contains reference on its parent namespace(namespace where it was created).
# That on get python try to find attribute in function namespace, parent, parent parent and so on
# see learn.javascript.ru

# If there is no attribute in local namespace it tryies to find it in parent.
x = 'global'

def f1():
    x = 'nonlocal'
    def f2():
        print(x)
    return f2

f = f1()
f() # 'nonlocal'


# And so on
x = 'global'

def f1():
    def f2():
        print(x)
    return f2

f = f1()
f()



# If you set value to attribute, it will be created in local namespace
def f1():
    x = 'f1'

    def f2():
        x = 'f2'
        print(f'f2: {x}')
    f2()
    print(f'f1: {x}')

x = 'global'
f1() 
# f2: f2
# f1: f1
# global: global


# If you want to change variable in parent scope use nonlocal or global words.
# nonlocal says that this variable in nearest namespace in enclosing scope
def f1():
    x = 'f1'

    def f2():
        nonlocal x
        x = 'f2'
        print(f'f2: {x}')
    f2()
    print(f'f1: {x}')

x = 'global'
f1()
print(f'global: {x}')
# f2: f2
# f1: f2
# global: global


#global says that this variable in global scope (namespace)
def f1():
    x = 'f1'

    def f2():
        global x
        x = 'f2'
        print(f'f2: {x}')
    f2()
    print(f'f1: {x}')

x = 'global'
f1()
print(f'global: {x}')
# f2: f2
# f1: f1
# global: f2

#Note, with operators +=, -= and so on there is a problem.
def f():
    x += 1 # we try to get enclosing attribute and then create a local attrubute.
           # It's a very weird behavior, it's forbidden.

x = 1
f() # error 


# You can get object for local and global scopes. Use functions locals and globals
def f(x):
    y = 2
    print(locals())

f(1) # {'x': 1, 'y': 2}


print(globals()) # there are a lot of names in global scope




