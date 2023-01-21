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








