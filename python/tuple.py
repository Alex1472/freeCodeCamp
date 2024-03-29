# Tuples are unchangable.
# You can use indexes in tuples like as in list.

# You can concatenate to tuples:
t1 = ('a', 'b')
t2 = ('c', 'd')
t3 = t1 + t2
print(t3) # ('a', 'b', 'c', 'd')

#Also you can repeat tuple with multiplication by number.

# You can unpack tuple:
t = ('a', 'b', 'c')
a, b, c = t
print(a, b, c) # a, b, c

# If number of items in a tuple and variable are not matched you can use astarisk.
# In this case last items will be in a list.
t = ('a', 'b', 'c', 'd')
(a, b, *c) = t # you should use paranthess
print(a, b, c) #'a' 'b' ['c', 'd']

t = ('a', 'b', 'c', 'd')
(a, *b, c) = t
print(a, b, c) # 'a', ['b', 'c'], 'd'


