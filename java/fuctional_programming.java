// FUNCTIONAL INTERFACE
// Note, there are many functional interfaces guide: 
// http://blog.orfjackal.net/2014/07/java-8-functional-interface-naming-guide.html
// Note, you can use generic interface Function or, if you need performance(because of boxing),
// special interfaces 
// To store lambdas in java is used function interfaces
// It is a usual interface with just ONE abstract method
// You can use annotation @FunctionalInterface so that compiler check
// if the interface is functional
// Example, functional interface
@FunctionalInterface
interface Runnable {
    public void run();
}
// You can store lambdas in variable with functional interface type
Runnable test = () -> System.out.println("do!");
test.run();

// Functional interfaces can contain DEFAULT methods and STATIC methods
// Example, function that returns a value
@FunctionalInterface
interface Function<T, R> {
    R apply(T t);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        return (V v) -> apply(before.apply(v));
    }
    default <V> Function<T, V> addAfter(Function<? super R, ? extends V> after) {
        return (T t) -> after.apply(apply(t));
    }
    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
Function<Integer, Integer> f = x -> x + 1;
System.out.println(f.apply(4));
Function<Integer, Integer> f2 = f.compose(x -> x + 4);
System.out.println(f2.apply(1));
Function<Integer, Integer> identity = Function.identity();
System.out.println(identity.apply(3));

// Also we don't take into account methods that defined in the object class
// Example:
@FunctionalInterface
interface Comparator<T> {
    int compare(T o1, T o2);
 
    boolean equals(Object obj); // equals defined in Object
}

// You can inherit one interface from another and implement method as default
// And it will be functional interface
@FunctionalInterface
interface LongSupplier {
    long getLong();
}
@FunctionalInterface
interface IntSupplier extends LongSupplier {
    int getInt();
	@Override
    default long getLong() {
        return getInt();
    }
}



// FUNCTIONAL EXPRESSION
// We can create instance of functional interfaces with functional expressions.
// - we only can assing function expression to variable of functional interface
// - functional expression has no state (between calls)
// - we find out type of expression by context (type of variable)
// - functional expession is a lambda expession ((a, b) -> a + b) or method reference(Integer::sum)
// - we can say nothing about type and equality of functional expession. It is not specified by standard
//   so it can be changed. 
// - usually the only thing you should do with an object of functional interface is to run it

// Example, you can pass functional interface in another method
static void run(Runnable obj) {
	obj.run();
}
Runnable obj = () -> System.out.println("Hello world!");
obj.run();
run(obj);

// Sometimes you can specify functional expression type with cast (but it is a weird code)
Object obj = (Runnable)() -> System.out.println("Hello world!");

// Lambda expession consists of:
// Argument:
// - (Type1 name1, Type2 name2) - with types
// - (name1, name2) - you can leave out types if they can be found out by context
// - Name - you can specify one paramenter without brackets
// Arrow: ->
// Body:
// - expression System.out.println()
// - block { System.out.println(); }



// VALUE CAPTURE
// Lambda can capture values from context
// We call CLOSURE - lambda expression with captured values
// Lambda has no state, but with caputured values (closure) it has.

// We can caputure:
// - LOCAL VARIABLE
//   but it should be effectively final (final or you can
//   and final to declaration)
//   TO BE PRECISE, WE DON'T CAPTURE VARIABLE, WE CAPTURE JUST VARIABLE VALUE
// 	 So that won't be misunderstanding, you can change this value in context or in lambda
int x = 1;
Runnable runnable = () -> System.out.println(x); // correct
// You can't change this variable before or after lambda
int x = 1;
x++;
Runnable runnable = () -> System.out.println(x); // Incorrect
// You can't change the captured value in lambda
int x = 1;
Runnable runnable = () -> {
	x++; // incorrect!
	System.out.println(x);
}
// - OBJECT FIELD
//   in this case we capture this, not field value, so
//   we can change this field. You can change the value of captured field 
//   in lambda
class Tester {
    int x;
    public Tester(int x) {
        this.x = x;
    }
    public void test() {
        x++; // Correct
        Runnable runnable = () -> {
            x++;
            System.out.println(x);
        };
        runnable.run();
    }
}
Tester tester1 = new Tester(1);
tester1.test(); // 3
tester1.test(); // 5
// - STATIC FIELDS
//   in this case we capture nothing, we get the value during execution
static int x = 1;
public static void main(String[] args) {
	Runnable runnable = () -> {
		x++;
		System.out.println(x);
	};
	runnable.run(); // 2
	runnable.run(); // 3
	runnable.run(); // 4
}



// METHOR REGERENCE
// There are several types of method reference
// - Static method, simple reference to a static method
IntBinaryOperator operator = Integer::sum;
System.out.println(operator.applyAsInt(1, 2));

// - Non-static method of class. In this case as first parameter
//   you should pass instance
System.out.println("   ssss    ".trim()); // "ssss"
Function<String, String> f = String::trim;
System.out.println(f.apply("   ssss    ")); // "ssss"

// - Instance-bound method (method of an instance). In this case you
//   should pass just parameters of the method
System.out.println("Java".equals("Java"));
System.out.println("Java".equals("Kotlin"));
Predicate<String> isJava = "Java"::equals;
System.out.println(isJava.test("Java"));
System.out.println(isJava.test("Kotlin"));

// - Class constructor, reference to the constructor method
//   Functional interface should have such signature
//   And return instance of and object
@FunctionalInterface
public interface Supplier<T> {
    T get();
}
Supplier<List<String>> supplier = ArrayList::new;
List<String> list = supplier.get();

// - Array constructor, functional interface should accept constructor 
//   parameters and return array
IntFunction<String[]> ctor = String[]::new; // constructor with one parameter - length
String[] arr = ctor.apply(4);



// Difference between lambda expression and method reference
// When we you method reference we fix out instance, with lambda
// we will get it when invoke method.
// So if we set out to null, methodPrinter will work, and lambda 
// printer will get an exception
Consumer<Object> lambdaPrinter = obj -> System.out.println(obj);
Consumer<Object> methodPrinter = System.out::println;
System.setOut(null);
lambdaPrinter.accept("lambda");
methodPrinter.accept("method");



// FUNCTIONAL PROGRAMMING PRIMITIVES
// FUNCTION - just a reference to a method that gets an object and returns 
// an object
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);	
}

// Method compose - return a composition: execute another function before
// and pass result to our function as argument
default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
	Objects.requireNonNull(before);
	return (V v) -> apply(before.apply(v));
}
Function<Integer, Integer> f1 = x -> x * 5;
Function<Integer, Integer> f2 = x -> x + 5;
Function<Integer, Integer> f3 = f1.compose(f2);
System.out.println(f3.apply(1)); // 30
// Method addThen - return a composition: execute our function and pass the
// result to another function 
default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
	Objects.requireNonNull(after);
	return (T t) -> after.apply(apply(t));
}
Function<Integer, Integer> f1 = x -> x * 5;
Function<Integer, Integer> f2 = x -> x + 5;
Function<Integer, Integer> f3 = f1.addThen(f2);
System.out.println(f3.apply(1)); // 10



// Example, bind function fixes first argument of the function
static <A, B, C> Function<B, C> bind(BiFunction<A, B, C> func, A a) {
	Objects.requireNonNull(func);
	return b -> func.apply(a, b);
}
BiFunction<Integer, Integer, Integer> sum = (a, b) -> a + b;
Function<Integer, Integer> addTwo = bind(sum, 2);
System.out.println(addTwo.apply(3)); // 5
// Another example, curring
static <A, B, C> Function<A, Function<B, C>> curry(BiFunction<A, B, C> f) {
	return a -> b -> f.apply(a, b);
}
BiFunction<Integer, Integer, Integer> f = Integer::sum;
System.out.println(curry(f).apply(1).apply(2)); // 3

