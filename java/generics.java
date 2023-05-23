// https://www.youtube.com/watch?v=_0c9Fd9FacU - interesting deep lecture

// In java you can parametrize types and methods
// So you can parametrize classes, interfaces and methods.


// TYPE PARAMETRIZATION
// First, let's look on type parametrization
// You can declare class or interfaces as generic. For this just specify 
// type parameters in <> brackets.
class List<T>
class Map<K, V>
class NumberList<T extends Number>
class X<T extends Y & Z>

// Parameter can be type of field, method parameter, variable, 
// return value, parameter of another type
// To use parametrized type, you need to substitute all parameters with
// explicit non-primitive types, another parameters or mask.
// Example, container for a value:
class Shmoption<T> {
    T value;
    public Shmoption(T value) {
        this.value = value;
    }

    public T get() {
        if(value == null) throw new NoSuchElementException();
        return value;
    }
    public void set(T value) { this.value = value; }
    public T orElse(T other) { return value != null ? value : other; }
    public boolean isPresent() { return value != null; }
}
// Example of usage:
Shmoption<String> present = new Shmoption<String>("yes"); // you should specify 
// type parameter in constructor
Shmoption<String> absent = new Shmoption<String>(null);
System.out.println(present.isPresent());
System.out.println(present.get());
System.out.println(absent.isPresent());
System.out.println(absent.orElse("no"));



// OPERATOR DIAMOND
// You can leave out type in generic constructor, if compiler can resolve it.
// Just write <> - diamond
Shmoption<String> present = new Shmoption<>("yes");
Shmoption<String> absent = new Shmoption<>(null);

// Sometimes when it's hard to compiler to resolve type, the result can be
// unexpected
var present = new Shmoption<>("yes"); // parameter is a string as expected
// it resolved from generic parameter
var absent = new Shmoption<>(null); // parameter is object as there is no other information
System.out.println(present.orElse("no").length()); // works
System.out.println(absent.orElse("no").length()); // compilation error



// WILDCARD
// WE CAN USE WILDCARDS ONLY IN TYPE REFERENCES, NO WHEN INHERIT ON CLASS FROM ANOTHER
// In reference we can specify ?, that says it is a some type
// ? means ? extends Object, so it is a type inherited from object
// By get you will recieve an object in this case
// You can't pass object of this type, because we don't now what is it,
// it's just some type that inherited from object
Shmoption<?> present = new Shmoption<String>("yes");
System.out.println(present.get()); // returns object
present.set("no"); //compilation error
// You can pass only null
present.set(null);

// You can specify bound for this wildcard.
// It this case will be returned object of this type
// As earlier, we can't pass any object
Shmoption<? extends String> present = new Shmoption<String>("yes");
System.out.println(present.get().length()); // works, as type is inherited from String
present.set("no"); // compilation error, as we aren't specified paticular type

// Note, with erasure ? replaced by extends 



// INHERITANCE
// Let's inherite classes from our Shmoption
// It seems, we get all methods from base generic where N is N extends Number
class NumberShmoption<N extends Number> extends Shmoption<N> {
    NumberShmoption(N value) { super(value); }
}
//usage, we can pass only Number, and we get Number
NumberShmoption<Number> numberShmoption = new NumberShmoption<>(15);
Number value = numberShmoption.get();
numberShmoption.set(5);

class IntegerShmoption extends NumberShmoption<Integer> {
    IntegerShmoption(Integer value) { super(value); }
}
// usage, there is no generics here
IntegerShmoption integerShmoption = new IntegerShmoption(34);
Integer integer = integerShmoption.get();
integerShmoption.set(6);



// WILDCARD WITH INHERITANCE
NumberShmoption<?> numberShmoption = new NumberShmoption<>(15); // we create
                                                                // NumberShmoption<Integer>
Number value = numberShmoption.get(); // We can use Number, because parameter extends Number
									  
NumberShmoption<? extends Number> numberShmoption = new NumberShmoption<>(15);
Number value = numberShmoption.get();	

NumberShmoption<? extends Integer> numberShmoption = new NumberShmoption<>(15);
Integer value = numberShmoption.get();	

NumberShmoption<?> integerShmoption = new IntegerShmoption(34); // we can use
// Number because parameter in declaration of the NumberShmoption extends Number
Number value = integerShmoption.get();

NumberShmoption<? extends Integer> integerShmoption = new IntegerShmoption(34);
Integer value = integerShmoption.get();



// SUBTYPES AND SUPERTYPES
// Let's say: This means B is a subtype of A if anywhere 
// you can use an A, you could also use a B.
// Otherwords, we can save reference to object B with an A reference variable
// If B implements A => B is subtype of ACTIVE
// If B is inherited from A => B is subtype from A
// NOTE, IF B IS SUBTYPE OF A THAT NOT MEANS THAT X<B> IS SUBTYPE X<A>
// For example, List<Integer> is not subtype of List<Object>
// But, its true for wildcards
// NumberShmoption<Integer> is subtype of NumberShmoption<?>
// NumberShmoption<Integer> is subtype of NumberShmoption<? extends Number>
// NumberShmoption<Integer> is subtype of NumberShmoption<? extends Integer>
// IntegerShmoption is subtype of NumberShmoption<?>
// IntegerShmoption is subtype of NumberShmoption<? extends Number>
// IntegerShmoption is subtype of NumberShmoption<? extends Integer>
// Note, IntegerShmoption is subtype of Shmoption<? super Integer> and
// Shmoption<? extends Integer>
Shmoption<? super Integer> superShmoption = new IntegerShmoption(15);
Shmoption<? extends Integer> extendsShmoption = new IntegerShmoption(20);

// To find out, in on type is subtype of other, see at class hierarchy,
// and parameter hierarchy separatly. If in both one type is subtype of another
// Origin types have subtype relationship.



// SUPER 
// You can use super keyword to specify that type is supertype of specified class
Shmoption<? super Integer> superShmoption = new IntegerShmoption(15);
// Shmoption<? super Integer> - should be subclass of Integer



// READING(PRODUCER - EXTENDS, GET - EXTENDS)
// If you want to get value from generic use the word extends
// Convert value to double, should work for any Number, so we use ? extends Number
static double getDoubleValue(Shmoption<? extends Number> shmoption) {
	// shmoption.set(1); you can set value in this case
	return shmoption.get().doubleValue(); //get() returns Number
}
Shmoption<? extends Number> shmoption = new Shmoption<Integer>(15);
IntegerShmoption integerShmoption = new IntegerShmoption(20);
getDoubleValue(shmoption); // works
getDoubleValue(integerShmoption); // works

// WRITING(CONSUMER - SUPER, SET - SUPER)
// If you want to pass value to generic use the word super
// That means generic type should be super class of the specified
// shmoption should has super class of integer, so it supports setting Integer
static void setInteger(Shmoption<? super Integer> shmoption) { 
	Object value = shmoption.get(); // with get, it always returns Object
	shmoption.set(42); // we can set Integer
}
IntegerShmoption integerShmoption = new IntegerShmoption(1); // can use with IntegerShmoption
setInteger(integerShmoption);
Shmoption<Number> numberShmoption = new Shmoption<>(12.3); // and with Number shmoption
setInteger(numberShmoption);							 
									 
									 

// GENERIC METHODS
// You can type paramenters in methods, ususally in static
// It's handy, because you can connect several paramenters in sitnature.
// You should specify type before return value									 
static <T> void setNotNull(Shmoption<? super T> shmoption, T value) {
	if(value == null) throw new IllegalArgumentException();
	shmoption.set(value);
}
// Usually compiler can determine type paramenter.
Shmoption<Integer> shmoption = new Shmoption<>(13);
Main.setNotNull(shmoption, 42);
// You can specify it explicitly
Main.<Integer>setNotNull(shmoption, 42);

// NOTE, USUSALLY IN METHOD SIGNATURE TYPE PARAMETER IS USED SEVERAL TIMES
// (MORE THEN ONE). IF IT USED ONLY ONE TIME OFTEN IT MEAN THERE IS AN ERROR.



//ERASURE
// VM DON'T NOTE ABOUT TYPE PARAMETERS, THEY ARE ERASED BY COMPILER.
// Type Erasure rules
// - Replace type parameters in generic type with their bound if bounded 
//   type parameters are used.
// - Replace type parameters in generic type with Object 
//   if unbounded type parameters are used.
// - Insert type casts to preserve type safety.
// - Generate bridge methods to keep polymorphism in extended generic types.

// Erasure of generic types
// During the type erasure process, the Java compiler erases all type 
// parameters and replaces each with its first bound if the type parameter 
// is bounded, or Object if the type parameter is unbounded.
// Consider the following generic class that represents a node in a singly 
// linked list:
public class Node<T> {

    private T data;
    private Node<T> next;

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() { return data; }
    // ...
}

// Because the type parameter T is unbounded, the Java compiler replaces it 
// with Object:
public class Node {
    private Object data;
    private Node next;

    public Node(Object data, Node next) {
        this.data = data;
        this.next = next;
    }

    public Object getData() { return data; }
    // ...
}

// In the following example, the generic Node class uses a bounded type parameter:
public class Node<T extends Comparable<T>> {
    private T data;
    private Node<T> next;

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = next;
    }

    public T getData() { return data; }
    // ...
}

// The Java compiler replaces the bounded type parameter T with the first 
// bound class, Comparable:
public class Node {
    private Comparable data;
    private Node next;

    public Node(Comparable data, Node next) {
        this.data = data;
        this.next = next;
    }

    public Comparable getData() { return data; }
    // ...
}


// Erasure of generic methods
// The Java compiler also erases type parameters in generic method arguments. 
// Consider the following generic method:
// Counts the number of occurrences of elem in anArray.
public static <T> int count(T[] anArray, T elem) {
    int cnt = 0;
    for (T e : anArray)
        if (e.equals(elem))
            ++cnt;
        return cnt;
}

// Because T is unbounded, the Java compiler replaces it with Object:
public static int count(Object[] anArray, Object elem) {
    int cnt = 0;
    for (Object e : anArray)
        if (e.equals(elem))
            ++cnt;
        return cnt;
}

// Suppose the following classes are defined:
class Shape { /* ... */ }
class Circle extends Shape { /* ... */ }
class Rectangle extends Shape { /* ... */ }

//You can write a generic method to draw different shapes:
public static <T extends Shape> void draw(T shape) { /* ... */ }

//The Java compiler replaces T with Shape:
public static void draw(Shape shape) { /* ... */ }


// As we can see, after erasure in class there is no type pramenter, and
// most common suitable class is used.
// BECAUSE OF ERASURE YOU CAN'T CREATE AND INSTEANCE OF PARAMETER TYPE,
// USE INSTANCE OF THIS PARAMETER TYPE, CASTING TO PARAMETER TYPE
// PERMITS, BUT NO MAKE SENCEj(DO NOTHING).
// COMPILER ADD ADDITIONAL TYPE CHECKING, FOR EXAMPLE
// we have generic Optional, after erasure type paramenter was converted to Object
Optional<String> optional = Optional.of("foo")
String value1 = /* (String) cast adding */optional.orElse("bar" /* type checking that passing a String */)
String value2 = /*(String) cast adding */optional.get();

// Example:
// As erasure removes all type parameters => cast to this parameter in 
// a template method no make sense, it casts to object by default
// And real cast occurs in invoking code.
// In this example we return object, and then will be cast to string and exception
String s = get(); //implicit cast

static Object obj = 1;
static <T> T get() {
	return (T)obj; //fake cast
}



// Type convertion can be checked and unchecked
// Checked convertion means that if it is successful, types in the result object
// are right, you can't pass incorrect type to it
List<Integer> integers = new ArrayList<>();
List<? extends Number> objects = integers; // checked
// Unchecked means that you can pass incorrect type. In this case usually warning
// occurs. That means that later you program can blow up!
List<Integer> integers = new ArrayList<>();
List list = integers;
List<Number> numbers = list; // Unchecked, you can pass Long into integer list
// Note, as List generic, after erasure, it stores items as objects
// so if you add Long there will no exception. It occurs we you will 
// try to get Integer.

// Program is safe, if types in runtime corresponds types declaration.
// ClassCastException only can occurs during explicit casting in this case.
// Usually means, that if there is not warnings the program is safe.
// But with generics there are some problems because of erasure, and vm can't check
// types in runtime.
Shmoption<Integer> integerShmoption = new Shmoption<>(12);
// Compilation error, obvious we can't cast like that
// Shmoption<String> stringShmoption = (Shmoption<String>)integerShmoption;
Shmoption<?> shmoption = integerShmoption;
// Problem it can be true, we can't forbid cast from Shmoption<?> to Shmoption<String>
// during compilation, but in runtime there is not type parameters, so this cast
// do nothing in runtime => there will no checking in runtime here
Shmoption<String> stringShmoption = (Shmoption<String>)shmoption; // warning
System.out.println(stringShmoption.get()); // Exception

// Note, during cast changes only type, not its parameter, this cast is checked
NumberShmoption<Integer> numberShmoption = 
							(NumberShmoption<Integer>) integerShmoption; // checked



// RAW-TYPES
// We can use generics without specifing type parameters(but we shouldn't!!!)
// It is named raw-types
// In this case intance accpets Objects and returns Objects.
// It is similar to generic with Object type paramenter
// There are many tricky things with raw-types so, it's better don't use them.
// They were added for back compatibility, when changed some types to generics.
// So that existing code continue working.
Shmoption shmoption = new Shmoption(34);
shmoption.set(89);
Object value = shmoption.get();

NumberShmoption numberShmoption = new NumberShmoption(12);
numberShmoption.set(89);
value = numberShmoption.get();							
							
							
							
// GENERICS AND ARRAYS
// You can set array to supertype variable
// If you then pass another element, exception occurs on set
Integer[] integers = new Integer[1];
Number[] numbers = integers;
numbers[0] = 9.7; // ArrayStoreException

// As checking is in runtime, you can't check it for generics, as type parameters are erased.
// So you just can't create array of generics
Shmoption<Integer>[] array = new Shmoption<Integer>[10]; // compilation error, generic array creation

// You just can create generic array with ?. And you can assign any subclasses to it.
Shmoption<?>[] array = new Shmoption<?>[10];
array[0] = new NumberShmoption<Integer>(23);
array[1] = new Shmoption<String>("test");
Shmoption<?> numberShmoption = array[0];
Shmoption<?> stringShmoption = array[1];

// You can workaround it with raw-types(but shouldn't!)
Shmoption<Integer>[] integers = new Shmoption[1];
Object[] objects = integers; // correct operation
objects[0] = new Shmoption<>("test"); // correct, for compiler correct type, 
// can't check types in runtime, bucause type parameters are erased.
Shmoption<Integer> integerShmoption = integers[0];
Integer value = integerShmoption.get(); // cast exception



// VARARGS
// You can pass arguments to a method with ...
// Specify type then ... and variable name. Inside method the variable
// is an array
static void printAll(int x, Object... objects) {
	System.out.println(x);
	for(Object object: objects) {
		System.out.println(object);
	}
}

// But you can pass generics to methods with varargs
// In this case on every call there will be a warning.
// To suppress it use @SafeVarargs
@SafeVarargs
static <T> boolean isOneOf(T value, T... options) {
	for(T option: options) {
		if(Objects.equals(value, option)) {
			return true;
		}
	}
	return false;
}
isOneOf(shmoption, new Shmoption<>("one"), new Shmoption<>("two"));














