# In python there is lambda functions.
# Lambda function can have only one instruction.
f = lambda x, *args, **kwargs: print(x, args, kwargs)
f(1, 2, 3, a=6, b=7) # 1 (2, 3) {'a': 6, 'b': 7}

#MAP
#Map return special object, use list to get values
print(map(lambda x: x ** 2, [1, 2, 3])) # <map object at 0x0000028269223190>
print(list(map(lambda x: x ** 2, [1, 2, 3]))) # [1, 4, 9]

# if function has several paramenter, you should pass them as second, third, ... arguments
print(list(map(lambda x, n: x ** n, [1, 2, 3], [1, 2, 3]))) # [1, 4, 27]



# FILTER
# You can use filter to filter values. It's also returns special object.
print(filter(lambda x: x % 2 == 0, [1, 2, 3])) # <filter object at 0x0000028269222320>
print(list(filter(lambda x: x % 2 == 0, [1, 2, 3]))) # [2]

# if you pass None as a function, filter returns trusy values
print(list(filter(None, [0, 1, 0, 3]))) # [1, 3]



#ZIP
# Zip can union n-th elements on sequences.
print(zip('abc', [1, 2, 3])) # <zip object at 0x0000028269287900>
print(list(zip('abc', [1, 2, 3]))) # [('a', 1), ('b', 2), ('c', 3)]



#LIST GENERATORS
# You can generate list with syntax: expression loop filter condition
l = [i ** 2 for i in range(10) if i % 2 == 0]
print(l) # [0, 4, 16, 36, 64]

# You can use several loops next on variable of previous
data = [[1, 2, 3], [4, 5, 6]]
l = [x ** 2 for l in data for x in l]
print(l) # [1, 4, 9, 16, 25, 36]

# You also can generate dict like this:
d = {
    'a': 0,
    'b': 1,
    'c': 2,
    'd': 0
}

res = { k: v for k, v in d.items() if v } # filter truthy values
print(res) # {'b': 1, 'c': 2}