# STRINGS
# For string you can use ' or "
'a'
"b"
# For multiline strings use ''' or """
'''aaa
aaa''' # 'aaa\naaa'
"""bbb
bbb""" # "bbb\nbbb"

a = 'aaa' 'bbb' # literals go together is joined. (as there is +)
print(a) # 'aaabbb'
# You can write long line like this
f('foo '
  'bar '
  'boo') # 'foo bar boo'
# For defining variable
x = ('foo '
     'bar '
     'boo')
     
# Special symbols
# \' - '
# \" - "
# \t - tab
# \n - new line
# \xhh - hex symbol hh
# \\ - \

# You can use raw string to python understand strings as is:
print('\tHello') # '	Hello'
print(r'\tHello') # '\tHello'



# We should code symbols somehow
# At first lets match symbol to number
# ASCII - table that matches 127 symbols
# Unicode - table that matches 120000 symbols
# Now we should convert Unicode number to bytes
# There is several ways
# UTF-8 - uses 1-4 8 bit code units
# UTF-16 - uses 1 or 2 16 bit code units
# UTF-32 - uses 1 32bit code unit
# USC-1, USC-2, USC-4



# It uses unicode in python. We can use any characters from it. (English, Japaniese, Russian)
# Python uses USC-1, USC-2, USC-4 depending on the character range in a string
ord('a') # get unicode code the symbol 'a'
chr(85) # get character by unicode code

# You can add unicode symbol with its codes. Use \n and then code
print('\u0072') # \u for characters that can encode with 2 bytes  
print('\U0001F921') # \U for 4 byte characters
print('sometext\U0001F921\u0072anothertext') # you can use them in a string
print('\N{Japanese Goblin}') # You can use name for character



# STRING METHODS
# Change case
print(s1.upper()) # 'HELLO WORLD!'
print(s1.lower()) # 'hello world!'
print(s1.capitalize()) # 'Hello world!'
print(s1.title()) # 'Hello World!'
print(s1.title().swapcase()) # 'hELLO wORLD!'

# Alignment
print('foo bar'.ljust(20, '-')) # foo bar-------------
print('foo bar'.rjust(20, '-')) # -------------foo bar
print('foo bar'.center(20, '-')) # ------foo bar-------
print('foo bar'.center(20)) # '      foo bar       '

# Strips
# Strips remove characters in the left, right or both side which in an argument string
print(']>>foo bar<<['.lstrip(']>')) # 'foo bar<<['
print(']>>foo bar<<['.rstrip('<[')) # ']>>foo bar'
print(']>>foo bar<<['.strip('><[]')) # 'foo bar'
print('\n\t some text \t \n'.strip()) # 'some text'

# Split / rsplit
print('some    text'.split(' ')) # ['some', '', '', '', 'text'] - if to separators near - add empty string
print(' \t  some  \n   text   '.split()) # ['some', 'text'] - if there is no arguments, it joins all near space characters and then split
# It works only without argument
# You can specify number of splittings
print('foo.bar.bar.boo'.split('.', 2)) # ['foo', 'bar', 'bar.boo']
print('foo.bar.baz.boo'.rsplit('.', 2)) # ['foo.bar', 'baz', 'boo']

# Partition
# Always makes one split, and returns tuple with 3 values
print('foo.bar.bar.boo'.partition('.')) # ('foo', '.', 'bar.bar.boo')
print('foo.bar.bar.boo'.rpartition('.')) # ('foo.bar.bar', '.', 'boo')
print('foobar'.partition('.')) # ('foobar', '', '')

# Join
print(', '.join(['a', 'b', 'c'])) # 'a, b, c'
# Use join instead of constance string concatination in a for loop
# But cpython optimize this scenario, time will be almost the same

# Contanins, startswith, endswith
print('aca' in 'abacaba') # True
print('ada' not in 'abacaba') # True
print('abacaba'.startswith('aba')) # True
print('abacaba'.endswith('caba')) # True

# Find, index
# Index throws exceptions, if can't find substring, find return -1
print('abaracadabra'.find('ara')) # 2
print('abaracadabra'.find('ara', 2, 10)) # -1 - not this substring
print('abaracadabra'.index('ara')) # 2
print('abaracadabra'.index('ara', 2, 10)) # error
print('abacaba'.rindex('aba')) # 4 find begining the end
print('abacaba'.rfind('aba')) # 4

# Replace, translate
print('abracadabra'.replace('br', '**')) # 'a**acada**a'
print('abracadabra'.replace('br', '**', 1)) # 'a**acadabra'
print('abracadabra'.translate({ ord('a'): '*', ord('b'): '*' })) # replace sevaral symbols. Key should be a key of a symbol








