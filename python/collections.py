[x] = (1, ) # get element from one element tuple, we use list here
(x, ) = (1, ) # the same



# SLICES
# You can create slice namually 
# And pass them into collection
t = ('Tom', 'Hardy', 1980, 2, 3)

NAME = slice(2) # [:2]
YEAR_MONTH = slice(2, 4) # [2:4]
DATE = slice(2, None)

print(t[NAME]) # ('Tom', 'Hardy')
print(t[YEAR_MONTH]) # (1980, 2)
print(t[DATE]) # (1980, 2, 3)


# REVERSED 
t1 = tuple(reversed((1, 2, 3))) # (3, 2, 1)
t2 = (1, 2, 3)[::-1] # the same
l = list(reversed(range(1, 4))) # [3, 2, 1]



# Concatenate tuples
t1, t2 = (1, 2), (3, 4)
print(t1 + t2) # (1, 2, 3, 4)
# Compare tuples lexicographically
print((1, 2) < (1, 3)) # True
print((3, 1) < (2, 3)) # False




# NAMEDTUPLE
# Namedtuple is like a class factory.
from collections import namedtuple

Person = namedtuple('Person', ['name', 'age'])
person = Person(name='Tom', age=25)
print(person.name)
print(person.age)

# Note we use underscope here, to have an ability to create attributes fiels, asdict
print(person._fields) # ('name', 'age')
print(person._asdict()) # {'name': 'Tom', 'age': 25}

another_person = person._replace(name='Jack') # create new namedtuple and change some fields
print(another_person.name) # 'Jack' 




# LISTS
# Multiplication just copies links to the inner elements n times
l1 = [[0], [1]]
l2 = l1 * 2 # [[0], [1], [0], [1]]
l2[0][0] = 3 # [[3], [1], [3], [1]]

# It's better to use generators to create 2-dimentional arrays
arr = [[0] * 5 for i in range(5)] # array 5 * 5


# CHANGING
x = [1, 2, 3]
x.append(4)
print(x) # [1, 2, 3, 4]
x.extend([7, 8]) # pass something iterable
print(x) # [1, 2, 3, 4, 7, 8]

x[1:3] = [9, 9, 9] # change several elements
print(x) # [1, 9, 9, 9, 4, 7, 8] 

x = [1, 2]
x.insert(1, 3) 
print(x) # [1, 3, 2]
x.insert(-1, 4)
print(x) # [1, 3, 4, 2]

l1 = [1, 2]
l2 = [3, 4]
l3 = l1 + l2
print(l3) # [1, 2, 3, 4]
l2 += l1 # inline appending, approximately equal l2.extend(l1)
print(l2) # [3, 4, 1, 2] 

# Delete elements
x = [1, 2, 3, 4]
del x[1]
print(x) # [1, 3, 4]
del x[1:] # delete several elements
print(x) # [1] 

# remove element and return it
x = [1, 2, 3, 4]
y = x.pop(2)
print(x) # [1, 2, 4]
print(y) # 3

# remove by value
x = [1, 2, 3, 4]
x.remove(3) 
print(x) # [1, 2, 4]

#reverse
x = [1, 2, 3]
x.reverse()
print(x) # [3, 2, 1]

# sort
x = [3, 2, 1]
res = sorted(x) # return sorted list
print(res) # [1, 2, 3]
x.sort() # inplace sorting
print(x) # [1, 2, 3]

x = [3, 2, 1]
x.sort(key=lambda x: x % 2, reverse=True) # specify key, and by ascending or descending

# cmp_to_key
# you can create comparator and then pass it into cmp_to_key to create key for sorting
from functools import cmp_to_key

x = [(1, 1), (2, 1), (2, 2), (1, 2)]
# sort by first ascending, second descending
def cmp(a, b):
    if(a[0] < b[0]):
        return -1
    if(a[0] > b[0]):
        return 1
    if(a[1] > b[1]):
        return -1
    if(a[1] < b[1]):
        return 1
    return 0

x.sort(key=cmp_to_key(cmp)) 
print(x) # [(1, 2), (1, 1), (2, 2), (2, 1)]




# DEQUE
# For list l.pop(0) works for linear times
# Use deque for constant times
# Access by index for linear time in this case
from collections import deque

d = deque([1, 2, 3])
print(d)

d.appendleft(1)
d.append(3)
print(d) # deque([1, 1, 2, 3, 3])

d.pop()
d.popleft()
print(d) # deque([1, 2, 3])

# You can specify max deque length, in this case excess elements will be automatically deleted.
d = deque([1, 2], maxlen=2)
d.append(3) 
print(d) # deque([2, 3], maxlen=2)

d.appendleft(4)
print(d) # deque([4, 2], maxlen=2)




# SET
s1, s2, s3 = {1, 2}, {2, 3}, {3, 4}
print(set.intersection(s1, s2, s3)) # {}
print(s1 & s2 & s3) # {}

print(set.union(s1, s2, s3)) # {1, 2, 3, 4}
print(s1 | s2 | s3) # {1, 2, 3, 4}

print(set.difference(s1, s2, s3)) # {1}
print(s1 - s2 - s3) # {1}

s1, s2 = {1, 2}, {2, 3}
print({1, 2}.isdisjoint({3, 4})) # is intersection empty
print({1, 2} <= {1, 2}) # is subset
print({1, 2} < {1, 2}) # is strict subset

# Add elements
s = set()
s.add(1) # add one element
s.update([2, 3]) # add several elements

s = set()
s.update([1, 2], [4, 5]) # {1, 2, 4, 5} you can pass several iterable objects 

# Remove elements
s = set([1, 2, 3])
s.remove(5) # Error! throws the KeyError if the set has no specified element
s.discard(5) # ignore, if the set has no specified element
s.clear() # clear all elements

# Set only can contains hashable elements, for collections only unchangeble
s = {[], []} # error, as list is changable
s = {}

# Unchangable set - frozenset
s = { frozenset([1, 2]), frozenset([3, 4]) } # correct




# DICTIONARY
# Create
d1 = dict(a=1, b=2) # with keyword arguments { 'a': 1, 'b':2 }
d2 = dict(d1) # with another dict { 'a': 1, 'b':2 }
d3 = dict(d1, c=3) # mixed { 'a': 1, 'b': 2, 'c': 3 }

# fromkeys, it takes enumerable collection of keys
d1 = dict.fromkeys(['a', 'b']) # { 'a': None, 'b': None }
d2 = dict.fromkeys(['a', 'b'], 0) # specify values for keys { 'a': 0, 'b': 0 }

# fromkeys uses default value for every key
d1 = dict.fromkeys([1, 2], [])
d1[1].append(2)
print(d1[2]) # [2]

#keys, values, items - projections of a dictionary
d = { 'a': 1, 'b': 2 }
print(d.keys()) # dict_keys(['a', 'b'])
print(d.values()) # dict_values([1, 2])
print(d.items()) # dict_items([('a', 1), ('b', 2)])

print(len(d.keys()))
print('a' in d.keys())

# Iteration
# You can iterate over key
d = { 'a': 1, 'b': 2 }
for k in d:
    print(d[k])
    
# You can't change collection while iteration
d = { 'a': 1, 'b': 2 }
for k in d:
    del d[k] # Error

# You should create another collection instead:
for k in set(d):
    del d[k] # Correct
    
# if you try to get a value for unknown key, there will be an error.
d = { 'a': 1, 'b': 2 }
print(d['c']) # error
# You can specify default value for this case with the method get
d = { 'a': 1, 'b': 2 }
print(d.get('c', 3)) # 3

# You can create element in dict, if there is no this key
d = { 'a': 1 }
print(d.setdefault('a', 2)) # 1  d = { 'a': 1 }
print(d.setdefault('b', 2)) # 2  d = { 'a': 1, 'b': 2 }

# You can add or update values in a dict with the update method
# Pass key-value as collection of tuples or a keyword argument
d = { 'a': 1, 'b': 2 }
d.update([('a', 4), ('b', 5)], c=6) # { 'a': 4, 'b': 5, 'c': 6 }

# You can delete elements with del and pop. Pop returns the deleted value.
d = { 'a': 1, 'b': 2 }
del d['a']
print(d.pop('b')) # 2
print(d) # {}



# DEFAULTDICT
# If we try to get element, that is not in dict there will be an errer.
# defaultdict create element in this case
# you should pass function that return the default value into constructor
from collections import defaultdict

d = defaultdict(lambda: 0, [('a', 1), ('b', 2)]) # default value is 0 
print(d['c']) # d = { 'a': 1, 'b': 2, 'c': 0 }



# ORDEREDDICT
# ordereddict enumerate elements in order of them adding into collection
# updating elements in collection doesn't change the order
from collections import OrderedDict

d = OrderedDict([('a', 1), ('b', 2)])
for k in d:
    print(k) # a b

d['c'] = 3
for k in d:
    print(k) # a b c
    
    
    
# COUNTER
# Counter count items occurance
from collections import Counter

counter = Counter({'a': 1, 'b': 2}) # {'a': 1, 'b': 2} 
counter = Counter(['a', 'b', 'a']) # {'a': 1, 'b': 2}
print(counter.most_common(1)) # [('b': 2)] return several most common elements
print(list(counter.elements())) # ['a', 'a', 'b'] get element 


counter['b'] += 4 # update value
counter.pop('a') # delete and return 

# It's also a default dict. Counter create element with 0 value
print(counter['c']) # 0

# Add values
counter.update(['a', 'b'])
counter.update({'a': 2, 'b': 3})

# Subtract values
counter.subtract(['a', 'b'])
counter.subtract({'a': 2, 'b': 3})

# Union operations
# Result counter contains only positive values
from collections import Counter

counter1 = Counter({'a': 1, 'b': 2})
counter2 = Counter({'a': 3, 'b': 1})

print(counter1 + counter2) # sum Counter({'a': 4, 'b': 3})
print(counter1 - counter2) # subtract Counter({'b': 1})
print(counter1 | counter2) # max Counter({'a': 3, 'b': 2})
print(counter1 & counter2) # min Counter({'a': 1, 'b': 1})
