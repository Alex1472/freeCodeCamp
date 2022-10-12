d = {
    'key1': 'data1',
    'key2': 'data2'
}


#get value
print(d['key1']) # 'data1'
print(d.get('key2')) # 'data2'
print('key1' in d) # True  is key in dictionary


print(list(d.keys())) # ['key1', 'key2']
print(list(d.values())) # ['data1', 'data2']
print(list(d.items())) # [('key1', 'data1'), ('key2', 'data2')]


#Add or update data1
d['key3'] = 'new data' # add new data
d.update([('a', 1), ('b', 2)]) # add items, it should be a dictionary or a key-value pair

#remove item
d.pop('key1') # remove item with key 'key1'
del d['key2'] # remove item with key 'key2'
d.clear()

#Looping through keys:
for key in d:
    print(key)

#Copy dictionary:
d1 = d.copy()
d1 = dict(d)

	
	



