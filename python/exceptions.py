# EXCEPTIONS:
# CATCHING EXCEPTIONS
# You can handle exceptions with try - except block
# It will be invoked the isinstance function for exception(isinstance(e, TypeError))
try:
    x = [1, 2] + 1
except TypeError:
    print('TypeError')
    
# You can specify several exception types in tuple
try:
    x = 1 / 0
except (TypeError, ArithmeticError):
    print('Error')    

# It can be sevaral except blocks
try:
    x = 1 / 0
except TypeError:
    print('TypeError')
except ArithmeticError:
    print('ArithmeticError') # ArithmeticError
    
# It will be handled first suitable block and only it.
try:
    x = 1 / 0
except ArithmeticError:
    print('ArithmeticError') # ArithmeticError
except Exception:
    print('Exception') # this block do not works
    
    
# You can create a variable for exceptions:
try:
    x = 1 / 0
except ArithmeticError as e:
    print('ArithmeticError')
    
# In an except block you should specify type, you can you any conditions for it.
# Example, we try do something dangerous. If there is exception, we try again, if we get the same Excaption, we handle it somehow.
def try_divide(x):
    return x / 0

def try_again_divide(x):
    return x / 0

x = 0
try:
    res = try_divide(x)
except ArithmeticError as e:
    print('Division Error')
    try:
        try_again_divide(x)
    except type(e): # get type of current exception
        print('Division Error Again') 
        
        
# It will be craeted exception attribute in local namespace, and will be deleted after handeling an exception.
e = 1
try:
    x = 1 / 0
except ArithmeticError as e:
    print(e) # division by zero

print(e) # error
        
        
        
        
# EXCEPTIONS CLASSES
# The most base class for exceptions is BaseException
# It has several subclasses: Exception and other connected to exit from program.
# When you created you own exception, you should inherit it from the Exception class
print(BaseException.__subclasses__())
# [<class 'Exception'>, <class 'GeneratorExit'>, <class 'SystemExit'>, 
# <class 'KeyboardInterrupt'>, <class 'asyncio.exceptions.CancelledError'>, 
# <class 'asyncio.exceptions.CancelledError'>]

#AssertionError
assert 2 + 2 == 5, 'Math should work'

# ImportError - when you try to export not existing module
import a

# NameError - when you try to get unknown variable in namespace
try:
    print(x)
except NameError:
    print('NameError')
    
# AttributeError - when you try to get unknown attribute in an object
object().a

# KeyError, IndexError - when you access with unknown key in dictionary or unknown index in an array. They is inherited from LookupError
{}['a'] # KeyError
[][0] # IndexError

# ValueError - use when you have an incorrect argument, and there isn't more suitable exception
'aaa'.split('')

# TypeError occurs when you invoke function with incorrect argument type
'aaa' + 1



# CREATING EXCEPTIONS
# For your own library you should create your own base exception and inherit your exceptions from it
class CSCException(Exception): # our base exception
    pass

class TestFailureException(CSCException): # TextFailure
    def __str__(self):
        return 'Test failed.'
        
        
# An exception object has 2 handy arguments: args, __traceback__
# args - arguments that were passed into constructor
# __traceback__ - stacktrace of the exception
import traceback

def f():
    try:
        raise TestFailureException('Some data', 'another data')
    except TestFailureException as e:
        print(e.args) # ('Some data', 'another data')
        traceback.print_tb(e.__traceback__) # File "<string>", line 12, in f

f()



# RAISING EXCEPTIONS
# You can raise exception with the raise operator
try:
    raise ArithmeticError('Zero division')
except ArithmeticError as e:
    print(e)
    
# You can reraise catchex exception with raise with no paramenters
try:
    raise ArithmeticError('Zero division')
except ArithmeticError as e:
    raise # the arithmetice error raised again, the same as raise e

# You can reraise new exception but want to save link to an origin exception
# Use raise Exception as origin_exception
# Trackback will be changed according all an origin exception
# An origin exception will be in the __cause__ attribute
try:
    try:
        raise ArithmeticError('Zero division')
    except ArithmeticError as e:
        raise RuntimeError() from e
except RuntimeError as re:
    print(type(re.__cause__))



# FINALLY
# Use the finally block so that code invoked if exception occurs or not
try:
    raise ValueError
finally: # this block will be executed in any case
    print('After handling')
    
# Use the else block to execute code if there is no exception.
try:
    x = 1 + 2
    # x = 1 / 0 - else won't be executed
except:
    print('Exception')
else:
    print('Successfully executed!') # will be executed

# Note, if an exception occurs in except block, else will be executed.



# Exception changed occurs when if there is exception in else, finally block
try:
    {}['a']
finally: # else:
    'f'.split('')
    
    
    
# You should do try as small as possible
# In an except block an exception type should be as specific as possible