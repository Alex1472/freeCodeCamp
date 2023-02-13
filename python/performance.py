# We will use matrix multiplication example
# matrix.py:
import random

class Matrix(list):
    @classmethod
    def zeros(cls, shape):
        n_rows, n_cols = shape
        return cls([[0] * n_cols for i in range(n_rows)])

    @classmethod
    def random(cls, shape):
        M, (n_rows, n_cols) = cls(), shape
        for i in range(n_rows):
            M.append([random.randint(-255, 255) for j in range(n_cols)])
        return M

    @property
    def shape(self):
        return ((0, 0) if not self else (len(self), len(self[0])))

def matrix_product(X, Y):
    n_xrows, n_xcols = X.shape
    n_yrows, n_ycols = Y.shape

    Z = Matrix.zeros((n_xrows, n_ycols))
    for i in range(n_xrows):
        for j in range(n_xcols):
            for k in range(n_ycols):
                Z[i][k] += X[i][j] * Y[j][k]
    return Z

def print_matrix(m):
    for row in m:
        print(row)
        

# MODULE TIMEIT
# timeit accepts setup - some code to prepare measurements
# It measures code number times and returns sum
setup = """
from matrix import Matrix, matrix_product

shape = (64, 64)
X = Matrix.random(shape)
Y = Matrix.random(shape)
"""

res = timeit.timeit('matrix_product(X, Y)', setup, number=10)
print(res) # 0.983



# JUPYTER NOTEBOOK %TIMEIT
# You can use jupyter notebook to measure performance
import timeit
from matrix import Matrix, matrix_product

shape = (64, 64)
X = Matrix.random(shape)
Y = Matrix.random(shape)

%timeit matrix_product(X, Y)
# 91.1 ms ± 11.7 ms per loop (mean ± std. dev. of 7 runs, 10 loops each)
# It make 7 mesurements. It each runs function 10 time. And divede time on 10.

# You can specify number to run function
%timeit -n15 matrix_product(X, Y)
# 87.6 ms ± 13.2 ms per loop (mean ± std. dev. of 7 runs, 15 loops each)



# PROFILING
# You can profile function with cProfile
# matrix.py
def bench(shape=(64, 64), n_iter=16):
    X = Matrix.random(shape)
    Y = Matrix.random(shape)
    for iter in range(n_iter):
        matrix_product(X, Y)

if __name__ == "__main__":
    bench()
    
    
import cProfile

source = open('matrix.py').read()
cProfile.run(source, sort='tottime')


# We will get a table
# ncalls - number of function call
# tottime - total time without time for subfunctions
# percall - tottime for one function call
# cumtime - total time with time for subfunctions
# percall - tottime cumtime for one function call
         65965 function calls in 1.914 seconds

   Ordered by: internal time

   ncalls  tottime  percall  cumtime  percall filename:lineno(function)
       16    1.869    0.117    1.869    0.117 <string>:22(matrix_product)
     8192    0.016    0.000    0.031    0.000 random.py:292(randrange)
     8192    0.008    0.000    0.012    0.000 random.py:239(_randbelow_with_getrandbits)
     8192    0.005    0.000    0.037    0.000 random.py:366(randint)
      128    0.004    0.000    0.041    0.000 <string>:14(<listcomp>)
    24576    0.004    0.000    0.004    0.000 {built-in method _operator.index}
     8213    0.002    0.000    0.002    0.000 {method 'getrandbits' of '_random.Random' objects}
        1    0.001    0.001    1.913    1.913 <string>:39(bench)
     8192    0.001    0.000    0.001    0.000 {method 'bit_length' of 'int' objects}
        2    0.001    0.001    0.042    0.021 <string>:10(random)
        1    0.001    0.001    1.914    1.914 {built-in method builtins.exec}
       16    0.000    0.000    0.000    0.000 <string>:8(<listcomp>)
       16    0.000    0.000    0.001    0.000 <string>:5(zeros)
        1    0.000    0.000    1.913    1.913 <string>:1(<module>)
       32    0.000    0.000    0.000    0.000 <string>:17(shape)
        1    0.000    0.000    0.000    0.000 {built-in method builtins.__build_class__}
      128    0.000    0.000    0.000    0.000 {method 'append' of 'list' objects}
       64    0.000    0.000    0.000    0.000 {built-in method builtins.len}
        1    0.000    0.000    0.000    0.000 <string>:4(Matrix)
        1    0.000    0.000    0.000    0.000 {method 'disable' of '_lsprof.Profiler' objects}


# LINE_PROFILER
# We can measure performance on every string in a function
pip install line_profiler

# in jupyter notebook use command %lprun
%load_ext line_profiler

%lprun -f matrix_product bench() # matrix_product - function to measure, run bench function
Timer unit: 1e-07 s

Total time: 6.5435 s
File: d:\DifferentReps\PythonProjects\TestProjects\TestPythonProject\matrix.py
Function: matrix_product at line 22

Line #      Hits         Time  Per Hit   % Time  Line Contents
==============================================================
    22                                           def matrix_product(X, Y):
    23        16       1364.0     85.2      0.0      n_xrows, n_xcols = X.shape
    24        16        349.0     21.8      0.0      n_yrows, n_ycols = Y.shape
    25                                           
    26        16       9064.0    566.5      0.0      Z = Matrix.zeros((n_xrows, n_ycols))
    27      1024       5122.0      5.0      0.0      for i in range(n_xrows):
    28     65536     273983.0      4.2      0.4          for j in range(n_xcols):
    29   4194304   18338920.0      4.4     28.0              for k in range(n_ycols):
    30   4194304   46806034.0     11.2     71.5                  Z[i][k] += X[i][j] * Y[j][k]
    31        16        127.0      7.9      0.0      return Z
    
    
    
# Improve our matrix_product
# Let's save time on __getitem__
def matrix_product(X, Y):
    n_xrows, n_xcols = X.shape
    n_yrows, n_ycols = Y.shape

    Z = Matrix.zeros((n_xrows, n_ycols))
    for i in range(n_xrows):
        Xi = X[i]
        for k in range(n_ycols):
            acc = 0
            for j in range(n_xcols):
                acc += Xi[j] * Y[j][k]
            Z[i][k] = acc
    return Z
    
Timer unit: 1e-07 s

Total time: 5.28606 s
File: d:\DifferentReps\PythonProjects\TestProjects\TestPythonProject\matrix.py
Function: matrix_product at line 33

Line #      Hits         Time  Per Hit   % Time  Line Contents
==============================================================
    33                                           def matrix_product(X, Y):
    34        16       1304.0     81.5      0.0      n_xrows, n_xcols = X.shape
    35        16        326.0     20.4      0.0      n_yrows, n_ycols = Y.shape
    36                                           
    37        16       8607.0    537.9      0.0      Z = Matrix.zeros((n_xrows, n_ycols))
    38      1024       4356.0      4.3      0.0      for i in range(n_xrows):
    39      1024       8344.0      8.1      0.0          Xi = X[i]
    40     65536     281798.0      4.3      0.5          for k in range(n_ycols):
    41     65536     264907.0      4.0      0.5              acc = 0
    42   4194304   17529266.0      4.2     33.2              for j in range(n_xcols):
    43   4194304   34330386.0      8.2     64.9                  acc += Xi[j] * Y[j][k]
    44     65536     431160.0      6.6      0.8              Z[i][k] = acc
    45        16        132.0      8.2      0.0      return Z




# But with numpy it works faster
import numpy as np

X = np.random.randint(-255, 255, shape)
Y = np.random.randint(-255, 255, shape)

%timeit -n100 X.dot(Y)
177 µs ± 21.4 µs per loop (mean ± std. dev. of 7 runs, 100 loops each)
