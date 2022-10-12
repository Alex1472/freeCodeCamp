# You can inherite one class from another:
# You try to find attributes in instance __dict__, class __dict__, parent class __dict__ and so on.
class Person:
    def __init__(self, name):
        self.name = name

    def greeting(self):
        print(f'Hi, my name is {self.name}')

class Employee(Person):
    def __init__(self, name, job_title):
        # super is a build-in function, that return special object with parent methods and attributes. It's bounded method so you shouldn't pass instance.
        # you can use the super function in any method
        super().__init__(name)
        self.job_title = job_title
		
# name and job_title attibutes contains in instance __dict__
employee = Employee('Tom', 'Python Developer')
print(employee.__dict__) # {'name': 'Tom', 'job_title': 'Python Developer'}



# To check if object is instance of a class, use the function isinstance(object, class)
print(isinstance(person, Person)) # True
print(isinstance(person, Employee)) # False
print(isinstance(employee, Person)) # True
print(isinstance(employee, Employee)) # True

# To check if one class is subclass of another use the function issubclass
print(issubclass(Employee, Person)) # True

# Any class is subclass of object.
print(issubclass(Person, object)) # True



# In python you can use multiple inheritance, but you should not.
class A:
    def say_a(self):
        print('a')

class B:
    def say_b(self):
        print('b')

class C(A, B):
    def say_c(self):
        print('c')

c = C()
c.say_a()
c.say_b()
c.say_c()

# The mro method return list that describes in witch order python will find attribute name in class instances.
print(C.mro()) # [<class '__main__.C'>, <class '__main__.A'>, <class '__main__.B'>, <class 'object'>]