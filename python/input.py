# INPUT
# Read line from the terminal:
s = input() 

# Print message and then read line:
s = input('Write something!')


# READ FROM FILE:
# You should open file, then read lines.

f = open('input.txt', 'r') # file should located in the same folder as a script
s1 = f.readline()
s2 = f.readline()

l1 = list(map(int, s1.strip().split()))
l2 = list(map(int, s2.strip().split()))

f.close()


# You can read all file with the function read():
f = open('input.txt', 'r')
s1 = f.read()
print(s1)

f.close()


# You can read all lines with for loop:
f = open('input.txt', 'r')
l = []
for s in f:
    l.append(s)

f.close()


# Write line
with open("input.txt", "w") as f:
    f.write("text\n")
    f.write("another text")
