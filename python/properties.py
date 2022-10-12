# You can specify get and set property for attribute. By convetion they have names get_attribute, set_attribute.

class Person:
    def __init__(self, name, age):
        self.name = name
        self.set_age(age)
    
    def get_age(self):
        return self._age
    
    def set_age(self, age):
        if age <= 0:
            raise ValueError("Age should be more then zero.")
        self._age = age

person1 = Person('Tom', 5)
person2 = Person('Alex', -1)



# You can define property for attribute with property class. You should create property as class attribute
# and pass to it get and set functions.

class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def get_age(self):
        return self._age

    def set_age(self, age):
        if age <= 0:
            raise ValueError("Age should be more then zero.")
        self._age = age

    age = property(fget=get_age, fset=set_age) # it is class attribute, we are passing get_age and set_age.


person1 = Person('Tom', 5)
print(person1.age)
person1.age = 10
print(person1.age)



# You can define properties in this format:
class MyClass:
    def __init__(self, attr):
        self.prop = attr

    @property
    def prop(self):
        return self._attr

    @prop.setter
    def prop(self, value):
        self._attr = value
		
For example:
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
    
    @property
    def age(self):
        return self._age

    @age.setter
    def age(self, age):
        if age <= 0:
            raise ValueError('Age should be more then zero.')
        self._age = age
    
    @age.deleter # works when you write del person.age
    def age(self):
        del self.age

person = Person('Tom', 20)
person.age = 30		



