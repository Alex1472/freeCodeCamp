# You can use conditional operator like this:
x = 4
if x > 10:
    print('A')
elif x > 5:
    print('B')
else:
    print('C')

# And / or
x = 5
if x > 0 and x < 10:
    print('YES')
if x < 0 and x > 2:
    print('YES')


# Shorthand if:
x = 4
if x > 1: print('A')

# Shorthand if/else:
x = 4
print('A') if x < 1 else print('B')

# Ternary operator:
x = 10
res = 1 if x > 5 else 0 # 0


# If you have an if statement and do not know what command should be executed you can use pass to avoid an error.
x = 5
if x > 2:
    pass
	
	
    
# Match (switch) statement:
error = 310

match error:
    case 100 | 110: # combine several values
        print("1..")
    case 200:
        print("2..")
    case 300 | 310:
        print("3..")
    case _: # default 
        print("Default")
