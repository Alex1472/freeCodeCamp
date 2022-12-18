# LIST:
# It's a built-in type.

l = [1, 2, 3]
print(len(l))

# You can use constructor to create list from other sequences:
l = list((1, 2, 3))


# You can use range indexes and negative indexes.


# You can change range of items:
thislist = ["apple", "banana", "cherry", "orange", "kiwi", "mango"]
thislist[1:3] = ["blackcurrant", "watermelon"]
print(thislist) # ['apple', 'blackcurrant', 'watermelon', 'orange', 'kiwi', 'mango']

# In this way, you can insert several items instead of another quantity.
thislist = ["apple", "banana", "cherry", "orange", "kiwi", "mango"]
thislist[1:4] = ["blackcurrant", "watermelon"]
print(thislist) # ['apple', 'blackcurrant', 'watermelon', 'mango']

# And instead of one item:
thislist = ["apple", "banana", "cherry", "orange", "kiwi", "mango"]
thislist[1:2] = ["blackcurrant", "watermelon"]
print(thislist) # ['apple', 'blackcurrant', 'watermelon', 'cherry', 'orange', 'kiwi', 'mango']




l = ['a', 'b', 'c']
l.insert(2, 'd') # ['a', 'b', 'd', 'c']
l.append('d') # ['a', 'b', 'c', 'd']
l.extend(['e', 'f']) # ['a', 'b', 'c', 'e', 'f']
l1 = [1, 2]
l2 = [3, 4]
l3 = l1 + l2 # [1, 2, 3, 4]

l.remove('b') # ['a', 'c']
del l[0] # ['b', 'c']
l.pop(2) # ['a', 'c'] also removes item
l.clear() # []



# LIST COMPREHENTION
# With comprehintion you can create a new list based on an existing list.
# Syntax:
# newlist = [expression for item in iterable if condition == True] # condition needed for filter items
# Expression:
x if condition else y # you can return value based on another condition`

l = [i for i in range(10)]
l = [0 if x < 8 else 1 for x in l if x > 5]
print(l) # [0, 0, 1, 1]



# SORTING
l = [2, 4, 3, 1]

l.reverse() # [1, 3, 4, 2]
l.sort() # [1, 2, 3, 4]
l.sort(reverse=True) # [4, 3, 2, 1]

# You can specify key for sorting:
def f(x):
    return abs(2 - x)
l.sort(key=f) # [2, 3, 1, 4]



# COPY:
l = [2, 4, 3, 1]

l1 = l.copy() # [2, 4, 3, 1]
l1 = list(l) # [2, 4, 3, 1]