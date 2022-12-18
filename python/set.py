# Set is unordered with no duplicates data collection.
s = { 'a', 'b', 'c'}
s = set(['a', 'b', 'c']) # create from another collection
print(len(s)) # 3


# Check if item in collection:
print('a' in s) # True


# Iterate with for:
for item in s:
    print(item) #'c' 'a' 'b'
	

# Add item into set:
s.add('d')

# Add item from another collection:
s.update(['d', 'e'])


# Remove from set:
s.remove('b') # if there is not the item in collection there will be an error.

# Remove from set:
s.discard('e') # there will not an error if there is no item in collection


# UNION:
a = { 'a', 'b' }
b = { 'c', 'd' }
c = a.union(b) # { 'a', 'b', 'c', 'd' } create new set and union Or use |
b.update(c) # { 'a', 'b', 'c', 'd' } add items to b set 

a = { 'a', 'b', 'c' }
b = { 'c', 'd', 'e' }

c = a.intersection(b) # { 'c' }  create new intersection set or use &
b.intersection_update(a) # { 'c' }  intersect b with a


c = b.symmetric_difference(a) # { 'a', 'b', 'd', 'e' } or use ^
a.symmetric_difference_update(b) # { 'a', 'b', 'd', 'e' } 
 


