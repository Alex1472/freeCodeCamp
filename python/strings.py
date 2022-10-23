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

# Predicates
print('123'.isdigit()) # number
print('abs'.isalpha()) # letters
print('123abs'.isalnum()) # numbers or letters
print('ABS'.isupper())
print('abs'.islower())
print('Boo Bar'.istitle()) 
print('\n\t  \t'.isspace())



# OBJECT REPRESENTATION
# There is 3 ways to represent an object as a string
# str - representation for men
# repr - the most full representation, with it you can recreate an object
# ascii - reps, but only ascii symbols allowed
print(str('я строка')) # "я строка"
print(repr('я строка')) # "'я строка'"
print(ascii('я строка')) # "'\u044f \u0441\u0442\u0440\u043e\u043a\u0430'"



# FORMAT METHOD
print('Today is {}, {}th'.format('October', 10)) # Today is October, 10th

#You can specify a representation of a string
print('{!s}'.format('я строка')) # str "я строка"
print('{!r}'.format('я строка')) # rept "'я строка'"
print('{!a}'.format('я строка')) # "'\u044f \u0441\u0442\u0440\u043e\u043a\u0430'"

# Format specification
# You can specify format of content after column
print('{:~^16}'.format('foo bar')) # '~~~~foo bar~~~~~'
# You can specify notation (2, 4, 8, 16, ...)
print('int: {0:d}, hex: {0:x}'.format(42)) # 'int: 42, hex: 2a'
print('oct: {0:o}, bin: {0:b}'.format(42)) # 'oct: 52, bin: 101010'
# You can round a number with format:
print('{:.2f}'.format(231.2365)) # 231.24
# You can specify representation and format
print('{!r:~^16}'.format('foo bar')) # "~~~'foo bar'~~~~"

# You can specify number of argument
print('{0} {1} {0}'.format('Hello', 'Kitty')) # 'Hello Kitty Hello'
print('{0} {name} {0}'.format('Hello', name='Kitty')) # 'Hello Kitty Hello'

# You can use lists, tuples and dicts with format
point = 0, 10
print('x={0[0]}, y={0[1]}'.format(point))
point = { 'x': 0, 'y': 20 }
print('x={0[x]}, y={0[y]}'.format(point))

# Module string
# In this module there are many constants (it is strings)
import string
print(string.ascii_letters) # abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
print(string.digits) # 0123456789
print(string.punctuation) # !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
print(string.whitespace) # ' \n\t\r\x0b\x0c'



# Bytes
# Bytes is an immutable class in python
# To specify bytes, add b before quotes, the write \x and two 1-e digits
print(b'\x71\x45\x77')
# Or with symbols
print(b'abc')
print(type(b'\x71\x45\x77')) # <class 'bytes'>

# You can encode a string into bytes and decode bytes into a string
chunk = 'я строка'.encode('utf-8')
print(chunk) # b'\xd1\x8f \xd1\x81\xd1\x82\xd1\x80\xd0\xbe\xd0\xba\xd0\xb0'
print(chunk.decode('utf-8')) # 'я строка'

# If you try to decode bytes of another encoding, you get an error
chunk = 'я строка'.encode('cp1251')b'----Foo Bar-----'
print(chunk.decode('utf-8')) # error

# You can specify what to do, if there is unknow symbol (by default, throw exception)
chunk = 'я строка'.encode('cp1251')
print(chunk.decode('utf-8', 'ignore')) # skip this symbol
print(chunk.decode('utf-8', 'replace')) # replace on \ufffd symbol

# Get default system encoding
print(sys.getdefaultencoding())

#Bytes suppot most methods of strings.
print(b'b' in b'abc') # True
print(b'abc'.find(b'c')) # 2
print(b'foo bar'.title().center(16, b'-')) # b'----Foo Bar-----'



# Files
# You can open text file like this:
file = open('input.txt')

# You can read all text
text = file.read()
# Some characters
text = file.read(10)
# Line
line = file.readline()
# Lines
lines = file.readlines()

# You can write text (create file or clear existing)
with open('input.txt', 'w') as file:
    file.write('test')
# Append to existing
with open('input.txt', 'a') as file:
    file.write('aaaa')
# Only create, if exist - error
with open('input1.txt', 'x') as file:
    file.write('aaaa')


# By default files opens in text mode ('t'), use 'b' to open in binary mode
with open('input.txt', 'rb') as file:
    data = file.read(6) # read 6 bytes, b'testaa'
    print(data)
with open('input.txt', 'wb') as file:
    file.write(b'somedata')














