# For testing, we will use a function:
def rle(iterable):
    for item, g in itertools.groupby(iterable):
        yield item, sum(1 for _ in g)
        
        

# DOCTEST
# You can write iterprepter lines as test in a function doc
# We use module doc test for this
# rle.py
import itertools
import doctest # import doctest


def rle(iterable):
    # some testing code from interpreter
    """ 
    >>> list(rle("")) 
    []
    >>> list(rle("mississippi")) 
    [('m', 1), ('i', 1), ('s', 2), ('i', 1), ('s', 2), ('i', 1), ('p', 2), ('i', 1)]
    """
    for item, g in itertools.groupby(iterable):
        yield item, sum(1 for _ in g)


if __name__ == '__main__':
    doctest.testmod() # run tests only if we run module
    
# run tests
# if there is no error output will be empty
py rle.py
# To enable log add -v key
py rle.py -v
# To run force doctest in a module, you shouldn't specify doctest.testmod() in a mode for this:
py -m doctest "module path"

# Strings should be the same
# If we add line break character there will be an error.
""" 
>>> list(rle("")) 
[]
>>> list(rle("mississippi")) 
[('m', 1), ('i', 1), ('s', 2), ('i', 1), 
('s', 2), ('i', 1), ('p', 2), ('i', 1)]
"""


# NORMALIZE_WHITESPACE, ELLIPSIS
# To ignore whitespaces in the expected result use NORMALIZE_WHITESPACE on the line of the test
"""
>>> list(rle("mississippi")) # doctest: +NORMALIZE_WHITESPACE
[('m', 1), ('i', 1), ('s', 2), ('i', 1), 
('s', 2), ('i', 1), ('p', 2), ('i', 1)]
"""
# To ignore some part of the output use ELLIPSIS
"""
>>> list(rle("mississippi")) # doctest: +ELLIPSIS
[('m', 1), ('i', 1), ('s', 2), ('i', 1), ...]
"""



# ASSERT
# You can test any condition with assert
# It take condition and optionally assertion message
# It's better always add message
def assert_equal(expected, actual):
    assert expected == actual, f"{expected} != {actual}"

def test_rle():
    expected = [
        ('m', 1), ('i', 1), ('s', 2), ('i', 1),
        ('s', 2), ('i', 1), ('p', 2), ('i', 2)
    ]
    actual = list(rle("mississippi"))
    assert_equal(expected, actual)

def test_rle_empty():
    expected = []
    actual = list(rle(""))
    assert_equal(expected, actual)


if __name__ == '__main__':
    test_rle()
    test_rle_empty()
    
    
    
# TESTUNIT
# You can write tests with the testunit module
# Test method in a class inherited from TestCase and starts with "test"
# rle.py:
import testcase

class TestHomework(unittest.TestCase): # inherit class from TestCase
    def test_rle(self):
        self.assertEqual([
            ('m', 1), ('i', 1), ('s', 2), ('i', 1),
            ('s', 2), ('i', 1), ('p', 2), ('i', 1)
        ], list(rle("mississippi")))

    def test_rle_empty(self):
        self.assertEqual([], list(rle("")))


if __name__ == '__main__':
    unittest.main() # run tests
    
# to run, just run the module
py rle.py

# You can run all test in a directory (all file started from test_)
py -m unittest .

# TestSuit has method to assert
assertEqual
assertNotEqual
assertTrue
assertIn
assertRaise
...

# You can prepare context with method setUp and tearDown
class TestHomework(unittest.TestCase):
    def setUp(self):
        print("Setup")

    def tearDown(self):
        print("Tear down")
        
        
        
#PYTEST
# You need to install pytest first
pip install pytest
# With pytest you should not write any additional code for testing
# just write assert
# test_rle.py
def test_rle():
    assert list(rle("mississippi")) == [
        ('m', 1), ('i', 1), ('s', 2), ('i', 1),
        ('s', 2), ('i', 1), ('p', 2), ('i', 2)
    ]

def test_rle_empty():
    assert [] == list(rle(""))
    

# just run this module with py.test, and it runs tests
py -m pytest -q test_rle.py

# You can run pytest several methods
python -m pytest # run all test in a dicrectory
python -m pytest test_pytest.py # run module
python -m pytest tess_pytest.py::test_rle # method in a module

# Test for pytest is:
# 1) function test_
# 2) method test_ in a class Test* or in a class inherited from unittest.TestCase
# 3) doctest, if pytest was launched with a key --doctest-modules


# You can check raising exceptions with pytest.raises context manager
import pytest

def test_exc():
    with pytest.raises(RuntimeError):
        print('test') # Error
        
def test_exc():
    with pytest.raises(RuntimeError):
        raise RuntimeError() # OK
        
        
# You can parametrize test with pytest.mark.parametrize decorator
def mysum(a, b):
    return a + b

@pytest.mark.parametrize('a,b,expected', [ # specify argument names and tuples of arguments
    (1, 1, 2),
    (2, 3, 5),
    (-1, 1, 0)
])
def test_mysum(a, b, expected):
    assert mysum(a, b) == expected
 
 
# You can prepare context with py.test
# You should create a context variable create a fuction with the
# name that the same as context parameter in a test function
# Your function should return context object with yield
# After that goes teardown (allocation resources)
# For function that creates context use decorator pytest.fixture
@pytest.fixture
def context():
    print('Setup')
    yield {'a': 1, 'b': 2}
    print('Teardown')

def test_context(context):
    print(f'Context: {context}')
    
    
    
# MODULE HYPOSESIS
# You can generate data for tests with module hyposesis
# With decorator given you can specify data to generate
# You should pass into it description for data to generate
import hypothesis.strategies as st
from hypothesis import given

@given(st.lists(st.integers())) # generate lists of integers
def test_sorted(l):
    result = sorted(l)
    assert all(xi <= xj for xi, xj in zip(result, result[1:]))
    
    
# You can generate
# Types:
st.just(x) # x, x, x
st.none() # None, None, None
st.one_of(a, b, b) # a, a, b, c, a
st.booleans() # True, False, False
st.integers() # 1, -10, 2, 42
st.floats() # math.py, 42.42
st.text() # 'abra', 'cadabra'
st.binary() # b"\xff\xef", b"ascii"

# Collections:
st.sampled_from(iterable)
st.tuples(st.foo(), st.bar(), st.boo())
st.lists(st.foo())
st.sets(st.foo())
st.dictionaries(st.foo(), st.bar())
 