# FOR
# For iterates through collection list, tuple, range.
# range has 3 attributes start, end, step. End is not included.
for i in range(1, 6):
    print(i)
# 1, 2, 3, 4, 5

for i in range(1, 6, 2):
    print(i)
# 1, 3, 5


# You can use else to execute code exact after loop:
for i in range(1, 6, 2):
    print(i)
else:
    print('The loop has ended!')
	
# You can use pass in loop:
for x in [0, 1, 2]:
  pass
  
  
# You can get index with an enumerate function in for loop. 
data = ["a", "b", "c"]

for idx, item in enumerate(data):
     print(f"{item}, {idx}")




# You can add else statement to run code once after while loop.
x = 1
while x < 5:
    print(x)
    x += 1
else:
    print('end')