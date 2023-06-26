// ANNOTATIONS
// Annotation is a metadata about an element in your program.
// It used by
// - Compiler (@Override, @SuppressWarnings, @FunctionalInterface)
// - IDE (@Nullable, @NotNull, #Contract). For example for static analisis.
// IDE can get warnings while analyzing annotations.
// - Annotation processor (Lombook - @Getter, @EqualAndHashCode). It is 
// compiler plugin, they can transform(generate) code for example.
// - Bytecode analyzer (FindBugs - @SuppressFBWarnings). If annotation there
// is in bytecode bytecode analyzer can do something based on the annotation.
// For example with @SuppressFBWarnings it can suppress warnings.
// - During execution (Bloody enterprice = @OneToMany). We can change runtime
// behavior with annotations

// Annotations can be with or without parameters



// OWN ANNOTATION 
// You can create your own annotation. For that declare it with @interface
// Annotation can be annotated.
// @Documented - if the annotation should be in the generated javadoc.
// "This class has this annotation"
// @Retention 
// - SOURCE - only in source code
// - CLASS - in source code and bytecode
// - RUNTIME - in source code, bytecode and in runtime. You can check
// it with reflection
// @Target - for which element this annotation is class, method, field, ...
// TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE, ...

// New annotation:
@Documented // should be in javadoc
@Retention(RetentionPolicy.SOURCE // shouldn't be in bytecode, only in source 
                                  // code
@Target({ElementType.TYPE}) // for class
@interface MyAnnotation {
}

@MyAnnotation
class Tester {
} 



// ANNOTATION TARGET
// Annotated example, with different targets:
@TypeAnno
public class AnnoDemo {
	@AnnoAnno @interface Anno {}
	
	@FieldAnno String s;
	
	@ConstructorAnno AnnoDemo(@ParamAnno int x) {
		@LocalAnno int y = x
	}
	
	@MethodAnno <@TypeParamAnno T> T method() {
		return null;
	}
	
	@TypeUseAnno List<@TypeUseAnno String @TypeUseAnno []> get() {
		return null;
	}
}

// With target TYPW_USE you can use annotation before any type entry
@Target(ElementType.TYPE_USE)
@interface NotNull {} // value can't be null
@Target(ElementType.TYPE_USE)
@interface Nullable {} // value can be null
// can return null list, but elements can't be null
// param array can be null, but elements can't
@Nullable List<@NotNull String> test(@NotNull String @Nullable [] param
                                     @Nullable Object obj)
                                     throws @NotNull RuntimeException {
    if(obj instanceof @Nullable String) { // obj can't be null, but it doesn't
	                                      // have sence
		@Nullable String s = (@Nullable String)obj;
		return Collection.singletonList(String.valueOf(s));
	} 
	return null;
}

// Sometimes one annotation entry can be applied to several targets
// For example
@Target({ElementType.METHOD, ElementType.TYPE_USE})
@interface MyAnno {
}
class Tester {
    class Inner {}
    // MyAnno will be applied to method and return value
	// This is because java syntax restrictions
    @MyAnno String test(Integer a, Integer b) { return "a"; }
	// MyAnno will be applied to method
    @MyAnno Tester.Inner getInner() { return new Inner(); }
	// MyAnno will be applied to return value
    Tester.@MyAnno Inner getInner2() { return new Inner(); }
}	
// Because of this, it's better don't use any other targets with TYPE_USE.	



// ANNOTATIONS PARAMETERS
// Annotations can have parameters.
// They have the form parameter name equals value.
// Value should be known on compilation stage.
// Annotation parameters can have type:
// - primitive types
// - String
// - Class, Class<? extends Xyz>
// - Enum
// - Annotation (without cyrcular dependencies)
// - One dimentional array of another allowed types
// - Value - compilation time constant	

// To create a parameter you should declare a method. It can have a defualt
// value. If parameter doesn't have defualt value we must specify it 
// when add this annotation.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface MyAnno {
    TimeUnit unit() default TimeUnit.DAYS;
    int data();
}

@MyAnno(data=1, unit=TimeUnit.HOURS)
class Tester {
}
// With default unit value
@MyAnno(data=1)
class Tester {
}	 

// If value is array and it has only one element we can don't specify an
// array just element
@interface MyAnno {
    String[] data();
}
@MyAnno(data="aa") // It will be an array with one element.
class Tester {
}

// If the name of the paramenter is "value" and the annotation has just
// that attribute(or other attributes has default values and you
// don't specify them) we can leave out the name when add this annotation
@interface MyAnno {
    int value();
}
@MyAnno(1)
class Tester {
}

@interface MyAnno {
    int value();
    int data() default 0;
}
@MyAnno(1)
class Tester {
}



// REFLECTION
// Reflection allows you to get information about program structure 
// in runtime. Also with reflection we can invoke method, read a field, etc
// We can get information about classes, methods, fields, types, annotations,
// packages, modules, ...

// The entry point is the method get class for instance or .class syntax for 
// class. They return the Class object for current class.
class Hello {}
Hello hello = new Hello();
Class instanceClass = hello.getClass();
Class myClass = Hello.class;

// The you can get constructor, method, ... objects of the class. And create
// instances, invoke method, ...
class Hello {
    private final String name;
    public Hello(String name) {
        this.name = name;
    }
    public void print() {
        System.out.println("Hello " + name + "!");
    }
    public void test(String data) {
        System.out.println(data);
    }
}
// To get constructor we should pass parameter classes to getConstructor
Constructor<Hello> constructor = Hello.class.getConstructor(String.class);
// To create instance we should pass constructor parameters
Hello hello = constructor.newInstance("Jack");
// To get method, we should pass method name and parameter classes
Method print = Hello.class.getMethod("print");
// To invoke the method we should pass method parameters
print.invoke(hello);
// Method with parameters
Method test = Hello.class.getMethod("test", String.class);
test.invoke(hello, "my test");

// Note, the getMethod method searched PUBLIC methods in current class
// parents. To search non-public method you can use getDeclaredMethod
// but it searches only in current class, not in parents
class Hello {
    // ...
    void test(String data) {
        System.out.println(data);
    }
}
Method test = Hello.class.getDeclaredMethod("test", String.class);
test.invoke(hello, "my test");

// SETACCESSIBLE
// By default you can't get/set value or invoke PRIVATE field and method
// even with reflection.
// You can get access with setAccessible method but only in the same module
// where the class was declared
Hello hello = new Hello("test");
// field
Field field = hello.getClass().getDeclaredField("name");
field.setAccessible(true); // you can't get value from private field 
                           // without setAccessible(true)
String value = (String)field.get(hello);
System.out.println(value);
field.set(hello, "data"); 
// method
Method method = hello.getClass().getDeclaredMethod("print");
// You can invoke private method without setAccessible(true)
method.setAccessible(true);
method.invoke(hello);

// If you try use setAccessible with class from another module you will
// get an exception
// We can get access but for this we should run VM with special options.
Set<String> set = Collections.singleton("foo");
Field field = set.getClass().getDeclaredField("element");
field.setAccessible(true); // exception!
field.set(set, "bar");

// REFLECTION USAGE
// We can use reflection for:
// - Dynamic loading - load java classes in runtime and create instances
// of this class
// - Support different versions of dependencies in runtime. Some method
// or class can be renamed in different versions of dependant library.
// But you want that your program will work with both virsions of this 
// library
// - Dependency injection
// - Serialization - create object based on serialized data
// - Dirty hacks

// REFLECTION AND GENERICS
// - You can get any information that there is in the source code with
// reflection
// - You can't get information about type because the type is erased.




// METHODHANDLES
// - More lowlevel and optimized API then reflection
// - You can only run methods with it, not get information
// - You can generate methods and they will work fast
// - Access checked during method search

// For method search is used special method - lookup. It can be different.
// It has context.
// - MethodHandles.Lookup() creates lookup that depends on context. If 
// in the method where we created lookup we can invoke the method, therefore
// we can invoke the method with lookup
// - MethodHandles.publicLookup() creates lookup with witch you can call any
// public method

// Example:
class Hello {
    private String name;
    public Hello(String name) {
        this.name = name;
    }
    private void print() {
        System.out.println(name);
    }
    public static MethodHandles.Lookup getLookup() {
        return MethodHandles.lookup();
    }
}
MethodHandles.Lookup lookup = Hello.getLookup();
// We can't use here 
// MethodHandles.Lookup lookup = MethodHandles.lookup();
// Because with it we don't have access to private methods
MethodHandle ctor = lookup.findConstructor(Hello.class, 
	MethodType.methodType(void.class, String.class)); // we should specify
// return type and parameter types
Hello hello = (Hello)ctor.invoke("test");
MethodHandle print = lookup.findSpecial(Hello.class, "print", 
	MethodType.methodType(void.class), Hello.class);
print.invoke(hello);

// You can get private lookup from external code like this:
MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(Hello.class,
	MethodHandles.lookup()); // now you can use this lookup to invoke private
// methods of the class Hello

// You can invoke several methods in a chain, it passes result of the
// constructor to the second function
MethodHandle compound = MethodHandles.collectArguments(print, 0, ctor);
compound.invoke("foo");

// NOTE, METHODHANDLE HAS A VERY BIG API, WITH IT YOU CAN DO WHAT YOU WANT.






