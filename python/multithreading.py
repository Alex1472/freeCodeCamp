# Процесс - запущенная программа
# У каждого процесса есть ___изолированное___ от других процессов состоние:
# - виртуальное адресное пространство
# - указатель на искомую иструкцию
# - стек вызовов
# - системные ресурсы, например, открытые файловые дескрипторы

# Альтернатива - использовать поток
# - поток используется внутри процесса и ___разделяет___ с ним адресное пространство
#   и системные ресурсы
# - потоки удобны для одновременного выполнения нескольких задач,
#   которые требуют доступ к разделяемому состоянию
# - совместным выполнением нескольких процессов и потоков управляет оперионная
#   система, поочередно разрешая каждому процессу или потоку использовать 
#   сколько-то циклов процессора.



# MODULE THREADING
# THREAD
# You can create and start thread with Thread class from the model threading
from threading import Thread
import time

def countdown(n):
    for i in range(n):
        print(i)
        time.sleep(1)

thread1 = Thread(target=countdown, args=(20,))
thread1.start()


# You can inherit your thread from the Thread class
# override the method run and do work there
# When we use thread we can think, that run calls target with args
class CountdownThread(Thread):
    def __init__(self, n):
        super().__init__()
        self.n = n

    def run(self):
        for i in range(self.n):
            print(i)
            time.sleep(1)

thread1 = CountdownThread(10)
thread1.start()

# BUT IT'S BETTER TO USE TARGET BECAUSE OF THE REUSABILITY REASONS 
# (YOU CAN USE FUNCTION IN ANY THREAD, BUT CLASS ALWAYS IN ANOTHER THREAD)

# To use state with thread, you can make object callable (implement __call__), 
# and then pass the object as a target
class Countdown:
    def __init__(self, n):
        self.n = n

    def __call__(self):
        for i in range(self.n):
            print(i)
            time.sleep(1)

countdown = Countdown(10)
thread = Thread(target=countdown)
thread.start()


# A thread has a name and an id.
# It's handy for debug.
print(thread.name) # Thread-1
print(thread.ident) # 10144
# You can specify thread name
thread = Thread(target=countdown, name='My Thread')
print(thread.name) # My Thread

# You can get all threads
print(threading.enumerate()) # [<_MainThread(MainThread, started 17884)>]


# JOIN, IS_ALIVE
# You can use method join to wait until the thread will end his work.
# We can use method is_alive to determine is the thread is working.
thread = Thread(target=time.sleep, args=(5, ))
print(thread.is_alive()) # False

thread.start()
print('Thread started')
print(thread.is_alive()) # True

thread.join()
print('After thread') # after 5 seconds
print(thread.is_alive()) # False`



# Threads can be background and foreground
# Background thread will be killed main thread will end his work.
# Foreground thread will continue working after main thread will end his work.
# By default created threads are foreground.
# You can specify it as background with flag daemon
def sleep_and_print(t):
    time.sleep(t)
    print('Hello')

# Foreground, hello will be printed
thread = Thread(target=sleep_and_print, args=(5, ))
thread.start()

# Background, hello won't be printed
thread = Thread(target=sleep_and_print, args=(5, ), daemon=True)
thread.start()



# You can't cancel thread. You can implement this logic by hands.
# Add terminate method to state passed into thread and check it in the call method.
from threading import Thread
import time


class Task:
    def __init__(self):
        self._running = True

    def terminate(self): # method to terminate the thread
        self._running = False

    def __call__(self):
        while self._running:
            time.sleep(1)
            print("Hello")


task = Task()
thread = Thread(target=task)
thread.start()

time.sleep(7)
task.terminate() # terminating



# SYNCRONIZATION PRIMITIVES
# LOCK, RLOCK
# You can acquire lock, then any other attempt to acquire the lock will block any thread (this or other)
# For example, if you try to acquire the same lock second time in the same thread there will be dead lock.
# Another thread will wait until thread acquired the lock release it.
# If you try to release the lock more time the you locked it there will be an exception.

# Example, add 1 ten time in two threads.
from threading import Thread, Lock, RLock, current_thread
import time

class Counter:
    def __init__(self, n):
        self.n = n
        self.count = 0
        self.lock = Lock()

    def __call__(self):
        for i in range(self.n):
            self.lock.acquire()
            time.sleep(1)
            self.count += 1
            print(current_thread().name)
            print(self.count)
            self.lock.release()


counter = Counter(10)
thread1 = Thread(target=counter)
thread1.start()

thread2 = Thread(target=counter)
thread2.start()

# RLock (recursive lock) allows you to acquire lock several times in the same thread. 
# But you should release the lock the same times, otherwise other thread continue waiting.
# If you try to release the lock more time the you locked it there will be an exception.

# Note, lock has no owner. You can realease lock in an another thread that you acquire it.
# Example, release lock in another thread, and then acquire it in a main thread again.
lock = Lock()
lock.acquire()
print('Acquired from main thread.')


def lock_release():
    time.sleep(5)
    lock.release()
    print("Released from another thread.")


thread = Thread(target=lock_release)
thread.start()

lock.acquire()
print("Second time acquired from main thread.")



# SEMAPHORE, BOUNDEDSEMAPHORE
# You can acquire and not release semaphore several times(specified value), if you try to acquire it more time it will be locked.
# It's like counter the allows you to increment it to specified value not more.
# If you release semophore, it can go under zero, it's ok. The more thread can acquire lock.
from threading import Thread, Lock, RLock, Semaphore, BoundedSemaphore, current_thread
import time

class Counter:
    def __init__(self, n):
        self.n = n
        self.count = 0
        self.semaphore = Semaphore(2)

    def __call__(self):
        for i in range(self.n):
            self.semaphore.acquire()
            time.sleep(1)
            self.count += 1
            print(current_thread().name)
            print(self.count)
            self.semaphore.release()
            self.semaphore.release()


counter = Counter(10)
thread1 = Thread(target=counter)
thread1.start()

thread2 = Thread(target=counter)
thread2.start()

thread3 = Thread(target=counter)
thread3.start()

# BoundedSemophore doesn't allow you to decrement it under zero, there will be an exception.




# CONDITION
# You can lock threads until something happens with Condition.

# A condition variable is always associated with some kind of lock; this can be passed 
# in or one will be created by default. Passing one in is useful when several condition 
# variables must share the same lock. The lock is part of the condition object: you don’t have to track it separately.

# A condition variable obeys the context management protocol: using the with statement
# acquires the associated lock for the duration of the enclosed block. The acquire() 
# and release() methods also call the corresponding methods of the associated lock.

# Other methods must be called with the associated lock held. The wait() method releases the lock, 
# and then blocks until another thread awakens it by calling notify() or notify_all(). 
# Once awakened, wait() re-acquires the lock and returns. It is also possible to specify a timeout.

# The notify() method wakes up one of the threads waiting for the condition variable, if any are waiting. 
# The notify_all() method wakes up all threads waiting for the condition variable.

# Note: the notify() and notify_all() methods don’t release the lock; this means that the 
# thread or threads awakened will not return from their wait() call immediately, but only when 
# the thread that called notify() or notify_all() finally relinquishes ownership of the lock.

# The typical programming style using condition variables uses the lock to synchronize access 
# to some shared state; threads that are interested in a particular change of state call wait() 
# repeatedly until they see the desired state, while threads that modify the state call notify() or notify_all() 
# when they change the state in such a way that it could possibly be a desired state for one of the waiters.


# We nodify one or all consumers when new number has been added into list.
# Sometimes wait work without notify this called spurious wakeup.
# Because of this better, use wait in a while loop, that checks changing of data.
# while self.current_len == len(data):
#     self.cond.wait()

class Creator:
    def __init__(self, cond, data):
        self.cond = cond
        self.data = data

    def __call__(self):
        i = 0
        while True:
            time.sleep(1)
            self.cond.acquire()
            print(f'Adding {i}')
            self.data.append(i)
            i += 1
            self.cond.notify()
            # self.cond.notify_all()
            self.cond.release()


class Consumer:
    def __init__(self, cond, data):
        self.cond = cond
        self.data = data
        self.current_len = len(data)

    def __call__(self):
        while True:
            self.cond.acquire()
            # while self.current_len == len(data):
            self.cond.wait()
            print(f'From thread {current_thread().name}')
            print(self.data[-1])
            self.current_len = len(data)
            self.cond.release()


data = []
is_empty = Condition()

creator = Creator(is_empty, data)
consumer1 = Consumer(is_empty, data)
consumer2 = Consumer(is_empty, data)

thread1 = Thread(target=creator)
thread2 = Thread(target=consumer1)
thread3 = Thread(target=consumer2)

thread1.start()
thread2.start()
thread3.start()




# Instead of invoking acquire, release you can use with for Lock, Semophore and Condition.
# If between acquire and release exception has occur lock won't be released. 
# So, it's better always use with statement with lock.
with self.cond:
    print(f'Adding {i}')
    self.data.append(i)
    i += 1
    # self.cond.notify()
    self.cond.notify_all()



# MODULE QUEUE
# In the model queue there is thread safity Queue, LifoQueue, PriorityQueue
# The method put add item in the queue.
# The method join stop the thread until the queue in empty
# The method get gets element from the queue. Get blocks the thread if the queue is empty. Until new element will be added.
# You should call the method task_done after getting an element from the queue.
# Join works only after invoking task_done for each element.
class Creator:
    def __init__(self, queue):
        self.queue = queue

    def __call__(self):
        i = 0
        while True:
            time.sleep(1)
            self.queue.put(i)
            print(f'Data added: {i}')
            i += 1
            self.queue.join() # add element and wait until consumer gets element from queue


class Consumer:
    def __init__(self, queue):
        self.queue = queue

    def __call__(self):
        while True:            
            data = queue.get() # block thread if the queue is empty
            # handling element
            print(f'Data from queue: {data}.')
            queue.task_done() # submit getting element


queue = Queue()
creator = Creator(queue)
consumer1 = Consumer(queue)

thread1 = Thread(target=creator)
thread2 = Thread(target=consumer1)

thread1.start()
thread2.start()



# THREADPOOLEXECUTOR
# You can give threads from thread pool and execute there tasks.
# You can run new task with method submit.
# It returns special future object.
# To get result invoke method result, the thread will wait until result will be calculated in another thread.
from concurrent.futures import ThreadPoolExecutor
from threading import current_thread
import time

def wait_and_return(x):
    time.sleep(5)
    print(current_thread().name)
    return x


executer = ThreadPoolExecutor()
future = executer.submit(wait_and_return, 3) # pass function and args
print(future.result()) # waiting result will be calculated


# You can use method map to run several tasks in several threads
# map returns special generator
# If you try to iterator through it it will wait until tasks will be calculated and returns results.
def wait_and_return(n):
    time.sleep(n)
    print(current_thread().name)
    return 42


executer = ThreadPoolExecutor()
gen = executer.map(wait_and_return, [5, 3])
for x in gen: # wait until first / second task ends.
    print(x)



# To free executer's resouces use method shutdown or with constuction
executer = ThreadPoolExecutor(max_workers=4) # threads to create
gen = executer.map(wait_and_return, [5, 3])
for x in gen:
    print(x)
executer.shutdown()

with ThreadPoolExecutor() as executer:
    gen = executer.map(wait_and_return, [5, 3])
    for x in gen:
        print(x)


# You can get current state of the future with methods running, done, cancelled
# Also you can get an exception, if it occurs
future = executer.submit(wait_and_return, 3)
print(f'Running: {future.running()}') # True
print(f'Done: {future.done()}') # False
print(f'Cancelled: {future.cancelled()}') # False
print(future.result()) # 42
print(future.exception()) # None



# You can execute function when future will be done, use method add_done_callback
# It will be invoked in another thread
def wait_and_return(n):
    time.sleep(n)
    print(current_thread().name) # ThreadPoolExecutor-0_0
    return 42

def print_result(f):
    print(current_thread().name) # ThreadPoolExecutor-0_0
    print(f'Result: {f.result()}')


executer = ThreadPoolExecutor()
future = executer.submit(wait_and_return, 3)
future.add_done_callback(print_result)
print('End main.') # print first, not block main thread


# You can use the function as completed to create generator from several futures
# The generator returns completed futures 
executer = ThreadPoolExecutor()
future1 = executer.submit(wait_and_return, 3)
future2 = executer.submit(wait_and_return, 5)

g = as_completed([future1, future2])
for f in g:
    print(f.result())
    
    
    
# GIL - GLOBAL INTERPRETER LOCK
# In an interpreter there is a gil. It's a lock, and you can use several threads 
# simoltaniously. So you can't improve performance using several threads.
# The problem comes down to garbage collecting. Garbage collector in python
# base on refs counting. With working multiple threads simoultaniously
# it's hard to count refs properly.
# Note, using CPython you can disable gil.



# MODULE MULTIPROCESSING
# You can use processes instead of threads to evaluate code simoltaniously.
# You can use process the same way as a thread.
# You can use .name, .pid, .join, .daemon
def sleep_and_log():
    time.sleep(5)
    process = mp.current_process()
    print(process.name)
    print(process.pid)
    print(process.daemon)

if __name__ == '__main__':
    process1 = mp.Process(target=sleep_and_log, name="My Process", daemon=True)
    process1.start()
    process1.join()
    print("After process")
    
# There are syncronization primitives for processes: Lock, Semophore, Condition.
# You can send data between processes with Pipe
# It's deserialize data, and send it with socket.
def send_message(con):
    time.sleep(5)
    con.send("Hello from another proceess.") # send data
    time.sleep(5)
    print('Process ended.')

if __name__ == '__main__':
    parent_conn, child_conn = mp.Pipe() # create connections

    process = mp.Process(target=send_message, args=(child_conn, ))
    process.start()

    message = parent_conn.recv() # wait until receive data
    print(f'Message: {message}')

    process.join()
    print("After process")



# Also you can use ProcessPoolExecuter
from concurrent.futures import ProcessPoolExecutor
from multiprocessing import current_process
import time

def wait_and_log():
    time.sleep(5)
    process = current_process()
    print(process.name)
    print(process.pid)

if __name__ == '__main__':
    executer = ProcessPoolExecutor()
    executer.submit(wait_and_log)
    executer.submit(wait_and_log)








