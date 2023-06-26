// JVM OVERVIEW
// JVM in java is fully specificated. It is documented what is it, 
// and its behaviour.
// https://docs.oracle.com/javase/specs/jvms/se7/html/

// You can split JVM into:
// - Class loader system - loads .class files
// - Runtime data area aka runtime(Heap, Method Area - compiled methods
// that will be executed, Stack, PC Register) 
// - Execution engine(Interpreter, JIT Compiler, Garbage Collector)

// Note, JVM uses .class files as input. It doesn't know about .java files
// So if the language can be compiled into class files, you can use JVM.



// CLASS LOADER
// Class loader is a java class. It:
// - Finds class by name and reads as byte array
// - Reads class structure
// - Verifies class
// - Resolves references (finds classes that this class uses)
// - Initializes this class

// So loader can reads .class file, .jar archive with files or we can write
// our own class loader that loads files from where you want(generate, 
// download).

// Let's load MyRunnable class with our own loader. We need to implement the
// loadClass method.
// Note, our loader also loads another classes that connected to 
// ours. For example, interface, superclass, etc... . In the example loader
// also should can load the interface Runnable.
// Because of this we use return super.loadClass(name); 
// as default implementation.
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Hello!");
    }
}

ClassLoader loader = new ClassLoader() {
	// we get class name to load, should return loaded class 
	//(class object)
	// We will use defineMethod that creates a class based on byte[] of 
	// .class file
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if(name.equals("MyRunnable")) {
			// Get binary text of the class
			try(InputStream stream = MyRunnable.class.getResourceAsStream("MyRunnable.class")) {
				byte[] data = stream.readAllBytes();
				return defineClass("MyRunnable", data, 0, data.length);
			} catch (IOException e) {
				throw new ClassNotFoundException();
			}
		}
		return super.loadClass(name);
	}
};

Runnable orig = new MyRunnable();
orig.run();
Class<?> loadedClass = loader.loadClass("MyRunnable");
Runnable loaded = loadedClass.asSubclass(Runnable.class)
	.getConstructor().newInstance();
loaded.run();

// We can get class loader of the loaded class
System.out.println(orig.getClass().getClassLoader());
// jdk.internal.loader.ClassLoaders$AppClassLoader@76ed5528
System.out.println(loaded.getClass().getClassLoader());
// Main$1@7b23ec81
// As classes return different loaders it is two different classes.
// If we try to cast one class to another we will get an exception.
// SO, IF WE LOAD CLASS WITH DIFFERENT CLASS LOADERS WE WILL GET TWO DIFFERENT
// CLASSES.
MyRunnable runnable = (MyRunnable)loaded;



// CLASS VERIFICATION
// Done by class loader
// about verification https://www.youtube.com/watch?v=-OocG7tFIOQ
// Class loader verifies the class that it loads(array of bytes that we
// load on previous step)

// For example:
// - all variable are initialized be use
// - Types of method parameters corresponds it arguments when you invoke 
// a method in your class. 
// (arguments in a moment of invocation)
// - Access rights aren't violated.
// - Local stack isn't overflowed
// - And so on

// These checks are done only on verification step. They won't be done further
// in VM. It speeds up execution.
// In case of broken class we will get a verification error.
// Note, you can disable verification java --noverify, but you shouldn't.
// But it is very dangerous, because we can load a broken class. 
// Then behavour will undefined(we can lose data, write into wrong place,
// virtual machine crash)



// CLASS INITIALIZATION
// - Done by the loader
// - Loader not always initialize class during loading.
// - It is an execution of static fields initializators and static 
// - initialization sections
// - It is guaranteed that initialization will be executed only once.
// Deadlock on initialization can occur.
// - If an error occurs during initialization, you can use the class further.

// Initialization for a class happens, with
// - On virual machine start for start class (main)
// - New instraction for specified class (object, not array of objects)
// - Reading static fields and invoking static methods
// - MethodHandle call under certain conditions
// - Some reflection methods call
// - Subclass initialization
// - Implementator initialization if interface has at least on default method.
// Static fields won't be initialized if there aren't default methods.
// And initialization will be on get a field.
// But because we can invoke default method and use here static fields
// that are not initialized because invocation default method is not presented
// above we should invoke initialization on implementator initialization.

// Example
// Class initalized only when we try to create an instance
class InitTest {
    static {
        System.out.println("Initialized");
    }
}
System.out.println("Class: " + InitTest.class); // getting class 
InitTest[] arr = new InitTest[10];
System.out.println("Array is created");
System.out.println(InitTest.class.getDeclaredConstructor()); // class structure
// is known before initialization
System.out.println(new InitTest()); // initializing

// Example, we can't use uninitialized class.
class InitTest {
    static {
        System.out.println("Initialized");
        if(true) throw new RuntimeException("Upsss!");
    }
}
try {
	new InitTest();
} catch (Throwable e) { e.printStackTrace(); } //ExceptionInInitializerError
try {
	new InitTest();
} catch (Throwable e) { e.printStackTrace(); } //NoClassDefFoundError,
// we tried to initialize the class but failed



// .CLASS-FILE
// TO DECOMPILE VERBOSE(WITH CONSTANT PULL) USE 
// JAVAP -V PATH_TO_CLASS_FILE IN JAVAP FOLDER
// - .class-file structure described in specification
// - disassembler javap -c -p, -c decompile method body, -p decompile private 
// methods
// - Every class is compiled into its own file
// - There are special .class formats: package-info.class, module-inof.class

// Every class-file starts with magic number 0xCAFEBABE
// Further goes class-file version. Version is 44 + java-version
// (52 = Java 8, 53 = Java 9)
// For example, can be an error
Exception in thread "main" java.lang.UnsupportedClassVersionError:
Hello has been compiled by a more recent version of the Java Runtime
(class file version 53.0), this version of the Jaava Runtime only 
recognizes class file versions up to 52.0
// Our runtime can works only with class-files version up to 52.0. But
// our class file has version 53.0.
// Note, for every java version will be created new version of JVM, even 
// though there are no changes in it.

// .class-file structure
// - Megic number (0xCAFEBABE)
// - Version (44 + java-version)
// - Constant pool
// - class flags(abstract, static)
// - class name
// - parent class name
// - interfaces
// - fields
// - methods
// - attribures

// Constant pool can contain:
// 1) String in UTF-8(Utf8) - an abstract string. Usually used as intermediate
// constants. More high-level constants consist of them
#4 = Utf8               TestBase
// 2) Number int/float/long/double
// 3) Class(Class) - reference to class name string constant(1) in 
// internal formats
#8 = Class              #10            // TestClass
#10 = Utf8               TestClass
// 4) String(String) - reference to string constant (1)
#44 = String             #45            // Hello, \u0001!
// 5) Name-type(NameAndType): reference to string name (1) + reference 
// to string type (1). Used for fields and methods
#15 = NameAndType        #17:#18        // out:Ljava/io/PrintStream;
#16 = Utf8               java/lang/System
#17 = Utf8               out
#18 = Utf8               Ljava/io/PrintStream;
// - field(Fieldref) - reference to class + reference to name-type
#7 = Fieldref           #8.#9          // TestClass.name:Ljava/lang/String;
#8 = Class              #10            // TestClass
#9 = NameAndType        #11:#12        // name:Ljava/lang/String;
// - method(Methodref) - reference to class + reference to name-type
// (reference to method of a class)
#23 = Methodref          #24.#25        // java/io/PrintStream.println:(Ljava/lang/String;)V
#24 = Class              #26            // java/io/PrintStream
#25 = NameAndType        #27:#28        // println:(Ljava/lang/String;)V
// - MethodHandle, MethodType, InvokeDynamic
// - Dynamic constant1
// Note, to compile System.out.println("Hello World"); it is nessesary to
// use 14 constants!
// Constant pool has max size (2^15 or 2^16). If your class need more 
// constants there will be an error.

// Primitive types encoding
// - B = byte
// - C = char
// - D = double
// - F = float
// - I = int
// - J = long 
// - S = short
// - Z = boolean
// - L<class_name>; = reference type
// - [ = array

// Fields signature
[[B // byte[][]
[Ljava/lang/String; // = String[]
// String field
#7 = Fieldref           #8.#9          // TestClass.name:Ljava/lang/String;
#8 = Class              #10            // TestClass
#9 = NameAndType        #11:#12        // name:Ljava/lang/String;
#10 = Utf8               TestClass
#11 = Utf8               name
#12 = Utf8               Ljava/lang/String;
// int field
#13 = Fieldref           #8.#14         // TestClass.age:I
#14 = NameAndType        #15:#16        // age:I
#15 = Utf8               age
#16 = Utf8               I

// Method signature
// ([param1[param2[...]]])returnValue
// V - void
(BB)I // int method(byte b1, byte b2)
// System.out.println("Hello, " + name + "!")
#27 = Methodref          #28.#29        // java/io/PrintStream.println:(Ljava/lang/String;)V
#28 = Class              #30            // java/io/PrintStream
#29 = NameAndType        #31:#32        // println:(Ljava/lang/String;)V
#30 = Utf8               java/io/PrintStream
#31 = Utf8               println
#32 = Utf8               (Ljava/lang/String;)V
// For constructor name=<init>, type = (...)V
// For class initializers name=<clinit>, type=()V
// public TestClass(String name, int age) { }
#17 = Methodref         #8.#18       // TestClass."<init>":(Ljava/lang/String;I)V

// CLASS ATTRIBUTES
// Attributes are like key-value pair
// Note, we can create our own attibutes and add them into class file.
// VM will ignore them.
// Some attributes specified in VM standard:
// - SourceFile - file name from which the class was compiled(for example
// Hello.java)
// - InnerClasses - all inner classes
// - EnclosingMethod - method, where class was declared(if class was declared
// in a method)
// - BootstrapMethods - invokedynamic(to support lambdas, etc)
// - Signature - if class is genetic, it has generic signature.
// It is ignored by VM, but it is useful for compilation and IDE
java.util.Map:<K:Ljava/lang/Object;V:Ljava/lang/Object;>Ljava/lang/Object
// There are two type parameter K extends Object, V extends Object
// - Annotations: RuntimeVisibleAnnotations(has retention=runtime), 
// RuntimeInvisibleAnnotations(has retention=class),
// RuntimeVisibleTypeAnnotations, RuntimeInvisibleTypeAnnotations

// FIELDS
// - Flags (public, static, final, volatile, synthetic(create by compiler), 
// transient, enum(enum constant in an enum))
// - Name
// - Signature
// - Attributes
//     - ConstantValue(if final primitive/String). If field is ConstantValue
// the field intialization passes during class loading, not with class 
// initializer
//     - Signature - if field is a generic class
//     - RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations
//     - RuntimeVisibleTypeAnnotations, RuntimeInvisibleTypeAnnotations
static int res;
  descriptor: I
  flags: (0x0008) ACC_STATIC
final java.lang.String name;
  descriptor: Ljava/lang/String;
  flags: (0x0010) ACC_FINAL
final int age;
  descriptor: I
  flags: (0x0010) ACC_FINAL
  
// Example, fields initalization 
class ClassInit {
    static final String a = "String a";
    static final String b = "String b".trim();
    static final String c = init();
    static final String d = "String " + "d";
    static final String e = "String e".trim();

    private static String init() {
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        System.out.println("d = " + d);
        System.out.println("e = " + e);
        return "String e";
    }
}
System.out.println(ClassInit.c);
// a and b in any case initialized before c.
// c is not initialized
// d is initialized during class loading because the value consists of 
// constant fields
// e isn't initialized yet. Because initialization contains method invokation.
// a = String a
// b = String b
// c = null
// d = String d
// e = null
// String e
// Bytecode:
static final java.lang.String a;
  descriptor: Ljava/lang/String;
  flags: (0x0018) ACC_STATIC, ACC_FINAL
  ConstantValue: String String a
  
  
  
// METHODS
// - Flags(public, static, final, synchronized, 
// bridge(special synthetic method), 
// varargs(if method has various number of arguments), 
// native(method which implementation uses native libraries),
// abstract, strict, synthetic)
// - Name
// - Descriptors
// - Attributes
//   - Code - code of the methods
//   - Exceptions - exceptions that method can throw(declared with throws)
//   - MethodParameters - names(ust names without types) of method parameters,
//                        can be leaved. You can take it with reflection.
//   - AnnotationDefault - if method is an annotation method and it has
//     default value
//   - Runtime(In)visible(Type/Parameter)Annotations
// From example below method test
void test(Hello$Y) throws java.lang.Throwable;
  descriptor: (LHello$Y;)V
  flags: (0x0000)
  Code:
    stack=1, locals=3, args_size=2
	   0: aload_1
	   1: invokevirtual #7                  // Method Hello$Y.get:()Ljava/lang/String;
	   4: astore_2
	   5: return
  LineNumberTable:
	line 70: 0
	line 71: 5
  LocalVariableTable:
	Start  Length  Slot  Name   Signature
		0       6     0  this   LHello;
		0       6     1     y   LHello$Y;
		5       1     2     o   Ljava/lang/Object;
Exceptions:
  throws java.lang.Throwable

// Example, we have class Hello with X, Y inner classes.
class Hello {
    static class X {
        Object get() { return null; }
    }
    static class Y extends X {
        String get() { return "foo"; }
    }

    void test(X x) {
        Object o = x.get();
    }
}

// view:< javap.exe -c -p Path\Hello$X.class > Hello$X.txt
// Hello$X.txt:
Compiled from "Main.java"
class Hello$X {
  Hello$X();
    Code:
       0: aload_0 // 
       1: invokespecial #1    // Method java/lang/Object."<init>":()V
       4: return

  java.lang.Object get();
    Code:
       0: aconst_null
       1: areturn
}
// If there is no constructor, it will be generated automatically.
// Generated constructor invokes the constructor of super class.
// 0: aload_0 - load on stack zero parameter. Zero parameter is always
// the reference this.
// 1: invokespecial #1 - load base constructor method, it takes zero parameter
// from stack

// 0: aconst_null - put on stack constant null
// 1: areturn - return an object of reference type
// Note: a means work with reference type

// Hello$Y.txt:
Compiled from "Main.java"
class Hello$Y extends Hello$X {
  Hello$Y();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method Hello$X."<init>":()V
       4: return

  java.lang.String get();
    Code:
       0: ldc           #7                  // String foo
       2: areturn

  java.lang.Object get();
    Code:
       0: aload_0
       1: invokevirtual #9                  // Method get:()Ljava/lang/String;
       4: areturn
}

// 0: ldc           #7  - load constant foo on stack from constant pull
// java.lang.String get(); return String, it is exact what we wrote in
// Y.get method
// Note, X.get returns Object, Y.get returns String
// On java level signature doen't contain return value. So the methods 
// java.lang.String get() and java.lang.Object get() are the same.
// On the bitecode level signature contains return value. Some the methods
// java.lang.String get() and java.lang.Object get() are different.
// When we invoke the method in bytecode we use the signature of specified
// object with return value
void test(Y y) {
	Object o = y.get();
}
Code:
   0: aload_1
   1: invokevirtual #7  // Method Hello$Y.get:()Ljava/lang/String;
   4: astore_2
   5: return
void test(X x) {
	Object o = x.get();
}
Code:
   0: aload_1
   1: invokevirtual #7  // Method Hello$X.get:()Ljava/lang/Object;
   4: astore_2
   5: return
   
// So to use the override method we need in Y class method with the exact 
// the same signature, because of this the method ava.lang.Object get(); 
// was generated
// 1: invokevirtual #7  // Method Hello$X.get:()Ljava/lang/Object;
// Invoke get as virtual method, so we can invoke overrided methods from
// heirs



// CODE ATTRIBUTE
// It has its own attributes
// - Max_stack(stack) - max stack depth. Used an interpreter to reserve 
// memory in advance
// - Max_locals(locals) - max local variables quantity. Used an interpreter
// to reserve memory in advance
// - Bytecode 
// - Exceptions table. In bytecode there is no information about what to docs
// if some instruction throws an exception. For this is used special table.
// Records in it has type: if from x line to y line there is an exception 
// go to target line. If there is no record for an exception the method just
// stops.
// - Attributes
//    - LineNumberTable. Debug attribute, sets match between bytecode 
// instructions and line number in source code. In exception stack trace 
// we see this line numbers
//    - LocalVariableTable. Has records like: from start length commands
// we have a local variable with name Name with Signature stored in slot 
// with a number slot. Here is stored parameters. Used by debugger to 
// get variable names
//    - StackMapTable - need for verificater
//    - Runtime(In)visibleTypeAnnotations - annotation in a method
void test(Y y) {
	try {
		Object o = y.get();
		System.out.println(o);
	} catch (Throwable e) {}
}
void test(Hello$Y);
    descriptor: (LHello$Y;)V
    flags: (0x0000)
    Code:
      stack=2, locals=3, args_size=2
         0: aload_1
         1: invokevirtual #7                  // Method Hello$Y.get:()Ljava/lang/String;
         4: astore_2
         5: getstatic     #13                 // Field java/lang/System.out:Ljava/io/PrintStream;
         8: aload_2
         9: invokevirtual #19                 // Method java/io/PrintStream.println:(Ljava/lang/Object;)V
        12: goto          16
        15: astore_2
        16: return
      Exception table:
         from    to  target type
             0    12    15   Class java/lang/Throwable
      LineNumberTable:
        line 71: 0
        line 72: 5
        line 73: 12
        line 75: 16
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            5       7     2     o   Ljava/lang/Object;
            0      17     0  this   LHello;
            0      17     1     y   LHello$Y;
      StackMapTable: number_of_entries = 2
        frame_type = 79 /* same_locals_1_stack_item */
          stack = [ class java/lang/Throwable ]
        frame_type = 0 /* same */



// Bytecode
// - It's a stack machine, we can push value on stack and get them.
// - There is block for local variables: this, parameters and variables - 
// locals. Slots for local variables can be reused.
// - Then goes bytecode. Instruction consists of one byte. Then goes 
// an instruction parameters. It depents on an instruction. If instruction
// begins with this letter, it is an instruction for this type.
// Note, in bytecode boolean values represented as integer or byte.
// So there is no instruction for boolean.
//   - a = reference
//   - i = integer
//   - l = long
//   - f = float
//   - d = double
//   - s = short
//   - b = byte
//   - c = character



// Instructions:
// Instruction take arguments from stack and put result on stack
// - load constant on stack: iconst_1(for short numbers there are
// special instruction iconst_1 - load 1 on stack), aconst_null,
// ldc <#cpentry> - load constant from constant pool. As parameter specify
// number in constant pool
// - load variable on stack aload_0 - special instructions for 
// small spot number, iload 6 - for big number specify parameter
// - save from stack into variable dstore_1, astore_3 - save into 
// corresponding slot
// - arthmetic instructions ladd(sum), ishr - byte shift`
// - type convertion i2b(integer to byte), d2i
// - stack manipulations: dup - clone element on the top of the stack,
// swap - swap two top elements on the stack
// - reading/writing fields and arrays getfield - read field, 
// getstatic - read static field, putfield, putstatic - writing fields,
// aaload - load from object array, iastore - save values into integer array
// - invoke method - invokestatic, invokevirtual(invoke virtual method),
// invokespecial - invoke exact method, invokeinterface - invoke interface
// method
// - object manipulation - checkcast, new, instanceof
// - manage flow - ifnull - if on stack null, ifeq - if on stack 0,
// if_icmeq - if two value on stakc are equal, goto - go to specified instruction
// tableswitch, lookupswitch, areturn - return reference, athrow - throw
// an exception 

// Example
void test() {
	int a = 3;
	int b = 2;
	System.out.println(a + b);
}
void test();
    descriptor: ()V
    flags: (0x0000)
    Code:
      stack=3, locals=3, args_size=1
         0: iconst_3
         1: istore_1
         2: iconst_2
         3: istore_2
         4: getstatic     #7   // Field java/lang/System.out:Ljava/io/PrintStream;
         7: iload_1
         8: iload_2
         9: iadd
        10: invokevirtual #13  // Method java/io/PrintStream.println:(I)V
        13: return
      LineNumberTable:
        line 70: 0
        line 71: 2
        line 72: 4
        line 73: 13
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      14     0  this   LHello;
            2      12     1     a   I
            4      10     2     b   I
			
// Example
void test() {
	System.out.println(this.a);
	this.a = "b";

	int[] arr = new int[3];
	arr[1] = 5;
	System.out.println(arr);
}
void test();
    descriptor: ()V
    flags: (0x0000)
    Code:
      stack=3, locals=2, args_size=1
         0: getstatic     #13                 // Field java/lang/System.out:Ljava/io/PrintStream;
         3: aload_0
         4: getfield      #7                  // Field a:Ljava/lang/String;
         7: invokevirtual #19                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        10: aload_0
        11: ldc           #25                 // String b
        13: putfield      #7                  // Field a:Ljava/lang/String;
        16: iconst_3
        17: newarray       int
        19: astore_1
        20: aload_1
        21: iconst_1
        22: iconst_5
        23: iastore
        24: getstatic     #13                 // Field java/lang/System.out:Ljava/io/PrintStream;
        27: aload_1
        28: invokevirtual #27                 // Method java/io/PrintStream.println:(Ljava/lang/Object;)V
        31: return
      LineNumberTable:
        line 75: 0
        line 76: 10
        line 78: 16
        line 79: 20
        line 80: 24
        line 81: 31
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      32     0  this   LHello;
           20      12     1   arr   [I