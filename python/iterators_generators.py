# Use dis.dis to see how python translates to low level instructions
import dis

dis.dis("for x in data: do_something()")
0 LOAD_NAME                0 (data)
2 GET_ITER                                   # call the method __iter__ to get iterator
4 FOR_ITER                 5 (to 16)         # call function next for iterator to get next element
6 STORE_NAME               1 (x)
8 LOAD_NAME                2 (do_something)
10 CALL_FUNCTION            0
12 POP_TOP
14 JUMP_ABSOLUTE            2 (to 4)
16 LOAD_CONST               0 (None)
18 RETURN_VALUE


# ITERATORS PROTOCOL
# Iterable (the object we iterate) should has method __iter__ that return iterator.
# Iterator should implement method __next__ and method __iter__ too. __iter__ usually returns self.
# When iteration ended StopIteration exception should be througno. Then iterator should through this exception.
class RangeIterator:
    def __init__(self, start, end):
        self._current = start
        self._end = end

    def __iter__(self):
        return self

    def __next__(self):
        if self._current < self._end:
            current = self._current
            self._current += 1
            return current
        raise StopIteration()

class Range:
    def __init__(self, start, end):
        self._start = start
        self._end = end

    def __iter__(self):
        return RangeIterator(self._start, self._end)


r = Range(3, 9)
for i in r:
    print(i)
    
    
# ITER, NEXT    
# function iter return iterator for iterable
r = Range(3, 9)
iter = iter(r)
# Also iter can call function, until it gets stop value.
class Generator:
    def __init__(self):
        self._i = -1

    def generate(self):
        self._i += 1
        return self._i

gen = Generator()
for i in iter(gen.generate, 5):
    print(i) # 0 1 2 3 4

# next return next value for iterator
# you can also specify value next should return, if iteration ended
print(next(iter([]), 5)) # 5

# So, for loop works like this:
iter = iter(l)
while True:
    try:
        x = next(iter)
    except StopIteration:
        break
    print(x)



# __CONTAINS__, __GETITEM__
# You can use in, not in with iterable objects.
# in, not in call method __contains__, that by default uses iterator:
# you can override __contains__ method
def __contains__(self, target):
    for x in self:
        if x == target:
            return True
    return False
    
r = Range(1, 6)
print(3 in r) # True


# Method __getitem__ allows to use indexer for a class
class Identity:
    def __init__(self, n):
        self._n = n

    def __getitem__(self, idx):
        if idx < self._n:
            return idx
        raise IndexError()

idenity = Identity(4)
print(idenity[2]) # using __getitem__

# There is another way to iterate through collection.
# If a collection has no method __iter__, for loop uses method __getitem__.
# It passes 0, 1, 2, ... until IndexError will be throuwn and return values from __getitem__.
# It's weird old behavior we should not lay on it.
idenity = Identity(4)
for i in idenity:
    print(i) # 0 1 2 3
    
# It works like this iterator:
class seq_iter:
    def __init__(self, instance):
        self.instance = instance
        self.idx = 0
        
    def __iter__(self):
        return self
        
    def __next__(self):
        try:
            res = self.instance[self.idx]
        except IndexError:
            raise StopIteration
            
        self.idx += 1
        return res
        
        

# GENERATORS
# Generator is a function with yield.
# When you call that function it return special object - generator.
# When we call next function executes until next yeild and return a value after yield
# After last yield it executes until the end of the function and throws StopIteration exception
def test_gen():
    print('a')
    yield 1
    print('b')
    yield 2
    print('c')
    yield 3

gen = test_gen()
print(gen)

print(next(gen)) # 1
print(next(gen)) # 2
print(next(gen)) # 2
print(next(gen)) # when iterations ended throws StopIteration exception
# a
# 1
# b
# 2
# c
# 3


# YOU SHOULDN'T REUSE GENERATORS (AS ITERATORS).
# Generators store its state. So you just get empty list, or with next StopIteration exception
def f():
    yield 42
    yield 43

gen = f()
print(list(gen))
print(list(gen))


# GENERATOR EXPRESSION
gen = (i ** 2 for i in range(10) if i % 2 == 1)
print(list(gen))
# Use braces for generators in arguments 
print(list(filter(lambda x: x % 2 == 1, (i ** 2 for i in range(10)))))
# Except function with one arguments
print(sum(i for i in range(10))) # 45



# SEND DATA INTO GENERATOR
# You can send data into generator with the method send
# This data return from yield
# send return data that yield returns
# first send data should be None
# we can say send FIRST SEND data and then GET DATA
def f():
    print("Start")
    x = yield 1
    print(x)
    y = yield 2
    print(y)
    z = yield 3
    print(z)
    print('End')

g = f()
print(g)

d = g.send(None) # go until first return yield 1
print(d)
d = g.send("Something") # send data to yield 1, returns from yield 2
print(d)
d = g.send("Another") # send data to yield 2, returns from yield 3
print(d)
d = g.send("Something") # send data to yield 3, goes through end, raises StopIteration
print(d)

# You can write just x = yield, in this case it returns None
# next(gen) calls send with None


# THROW
# We can throw exception into generator
# Use method throw for this
def f():
    try:
        yield 1
    except Exception:
        print('Catched')
        yield 2
    yield 3

g = f()
# we need first go to the first yield
# If you first throw exception it hoist into main
next(g) 
 
res = g.throw(StopIteration, 'test') # got to except block until yield 2
print(res) # 2
print(next(g)) # go to yield 3

# If genetator doesn't handle exception, in hoist into main.


# CLOSE
# You can close generator
# In this case generator stops, there won't exception
# Close throws GeneratorExit exception
def f():
    yield 1
    yield 2

g = f()
print(next(g)) # 1
g.close()
print(next(g)) # StopIteration exception

# You can handle GeneratorExit exception in generator there will be RuntimeException
def f():
    try:
        yield 1
    except BaseException as e:
        print(e)
        yield 2

g = f()
print(next(g))
g.close() # RuntimeError: generator ignored GeneratorExit



# You can emulate program with several input - corutine with generators
def f(pattern):
    while True:
        s = yield # get data 
        print("Contains" if pattern in s else "Not contains")


g = f('test')
next(g) # go to first yield
g.send('My test') # "Contains" - first input
g.send('some text') # "Not contains" - second input


# You need to call next, we can fix it with decorators
from functools import wraps

def coroutine(f):
    @wraps(f)
    def wrapper(*args, **kwargs):
        g = f(*args, **kwargs)
        next(g)
        return g
    return wrapper

# @coroutine
def f(pattern):
    while True:
        s = yield
        print("Contains" if pattern in s else "Not contains")

f = coroutine(f)
g = f('test')
g.send('My test')
g.send('some text')


# YIELD FROM 
# You can pass execution into another generator with yield from 
# You can split generator into some parts
# yield from property handles send and throw (passes them into right generator)
def a():
    yield 1
    yield 2

def b():
    yield 3
    yield 4

def c():
    yield from a() # go to a generator
    yield from b() # go to b generator
        
print(list(c())) # [1, 2, 3, 4, 5]


# RETURN`
# Also you can use return in generators
# It throws StopIteration with return value
# Note, you can't handle return as StopIteration in the generator
def f():
    yield 1
    return 2

gen = f()
print(next(gen)) # 1
try:
    print(next(gen))
except StopIteration as e:
    print(e.args) # (2, )


# Usually you use return to return value from another generator
def a():
    yield 1
    yield 2
    return 'A has ended'

def b():
    yield 3
    yield 4
    return 'B has ended'

def c():
    x = yield from a() # A has ended
    print(x)
    y = yield from b() # B has ended
    print(y)

for x in c():
    print(x)
# 1
# 2
# A has ended
# 3
# 4
# B has ended



# CONTEXTMANAGER
# You can use generators to create context manager
# generator function should has structure:
@contextmanager
def f(value):
    # __enter__ method code
    try:
        yield value
    finally:
        # __exit__ method code
        
with f(5) as value:
    print(f'In body with value {value}')
        
# The contextmanager decorator looks like
class contextmanager:
    def __init__(self, f): # when creating decorator
        self.f = f

    def __call__(self, *args, **kwargs): # f(5) accepts values, returns context manager
        self.gen = self.f(*args, **kwargs)
        del self.f
        return self

    def __enter__(self): # in with start, go to yield
        return next(self.gen)

    def __exit__(self, exc_type, exc_value, traceback): # go to finally 
        next(self.gen, None)
        
# same structure has contextmanager decorator from contextlib
from contextlib import contextmanager

@contextmanager
def f(value):
    print(f'Enter with value {value}')
    try:
        yield value # returns in with: with f(4) as value:
    finally:
        print(f'Exit with value {value}')



# MODULE ITERTOOLS
# ISLICE - iterator slice
# function islice returns iterator through iterable
# you can specify start, end, step
from itertools import islice

r = range(10)
print(list(islice(r, 3))) # [0, 1, 2]
print(list(islice(r, 3, None))) # [3, 4, 5, 6, 7, 8, 9]
print(list(islice(r, None, None, 2))) # [0, 2, 4, 6, 8]


# COUNT, CYCLE, REPEAT
from itertools import islice, count, cycle, repeat

# take first n elements from the iterable
def take(n, iterable):
    return list(islice(iterable, n))
    
# infinitly count from with step
print(take(5, count(1, 3))) # [1, 4, 7, 10, 13]
# infinitly repeat iterable
print(take(5, cycle([1, 2]))) # [1, 2, 1, 2, 1]
# infinitly repeat element 
print(take(5, repeat(3))) # [3, 3, 3, 3, 3]
# repeat element n times
print(take(5, repeat(3, 4))) # [3, 3, 3, 3]


# DROPWHILE, TAKEWHILE
from itertools import dropwhile, takewhile

l = [1, 2, 3, 4, 5, 6, 7, 8, 9]
# drops element until predicate returns true
print(list(dropwhile(lambda x: x < 5, l))) # [5, 6, 7, 8, 9]
# takes elements until predicate returns true
print(list(takewhile(lambda x: x < 5, l))) # [1, 2, 3, 4]


# CHAIN
# You can union several iterables with chain
from itertools import chain

l1 = [1, 2]
l2 = [3, 4]
print(list(chain(l1, l2))) # [1, 2, 3, 4]

iterables = [range(i) for i in range(4)]
print(list(chain.from_iterable(iterables))) # [0, 0, 1, 0, 1, 2]



# TEE
# You can create several independent copies of iterator with tee
# We can pass iterable and iterator into tee.
# Note, that we go only one time through iterator
# We remember values returned by iterator
from itertools import tee


def f():
    print('Start') # it executes only one time
    yield 1
    print('tick')
    yield 2
    print('tick')
    yield 3
    print('End')


g = f()
a, b, c = tee(g, 3)

print(list(a))
print(list(b))
print(list(c))

# Note, tee uses iterator, so you should not go through it in another place
from itertools import tee

l = [1, 2, 3]

iterator = iter(l)
print(list(iterator)) # [1, 2, 3]
a, b, c = tee(iterator, 3)

print(list(a)) # []
print(list(b)) # []
print(list(c)) # []


# COMBINATORICS
from itertools import product, permutations

print(list(product('ABC', repeat=2)))
# [('A', 'A'), ('A', 'B'), ('A', 'C'), ('B', 'A'), ('B', 'B'), ('B', 'C'), ('C', 'A'), ('C', 'B'), ('C', 'C')]
print(list(permutations('ABC')))
# [('A', 'B', 'C'), ('A', 'C', 'B'), ('B', 'A', 'C'), ('B', 'C', 'A'), ('C', 'A', 'B'), ('C', 'B', 'A')]
print(list(combinations('ABC', 2)))
# [('A', 'B'), ('A', 'C'), ('B', 'C')]
print(list(combinations_with_replacement('ABC', 2)))
# [('A', 'A'), ('A', 'B'), ('A', 'C'), ('B', 'B'), ('B', 'C'), ('C', 'C')]