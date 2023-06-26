// Shared reource - object / variable in memory that you want to read and update from
// different threads
// We have no problems with reading, we can use it in any number of threads
// There are problems when we modify resource, even though we one threads
// modify the resouce and other only read.

// 	WAYS TO ACCESS A SHARED RESOURCE:
// - Blocking access(lock, mutex, mutual exclusion)
//   MUTUAL EXCLUSION(ВЗАИМНОЕ ИЗКЛЮЧЕНИЕ) - means we have a segment of program
//   (that for example updates values of a shared variable), and only one thread
//   can execute this segment at the same time. 
//   If 2 threads want to execute it, one will execute and other will wait.
//   And only when first will executed it, another will start to execute the segment 
//   a second time
// - Non-blocking access - all threads works, nobody waits
//   - Lock-free - overall progress of the program is guaranteed. 
//     But isn't guarantedd progress in all threads. This means, real access to
//     a shared resource can have only one thread. Another threads in a loop
//     constantly trying to get access to resource but are rejected, while
//     one thread has access to the resource.
//   - Wait-free - progress in any thread is guaranteed.



// BLOCKING ACCESS
// - For blocking is used monitor or lock.
// - To block the same resource you should use the same monitor.
// - We should block reading and writing



// SYNCHRONIZED
// We wrap some code with synchronized and pass some object, if other
// thread tries to go into synchronized section with the same object it will wait.
// Usually is used more high level primitives then synchronized, but sometimes it
// is useful.
// - synchronized(obj) {  } - monitor - obj
// - synchronized void method(): - monitor - objject this. You can use synchronized
//   as method modifier. In this case it is the same as, all method body was wrapped
//   with synchronized(this) { ... }
// - synchronized static void method(): monitor - object .class. You can synchronized
//   as static method modifier. In this case it is the same as, all method body was wrapped
//   with synchronized(.class) { ... }

// Example:
class Container {
    private final List<String> list = new ArrayList<>();

    public synchronized void add(String x) { // we use synchronized
	                                         // keyword to add 1 item in the same time
        this.list.add(x);
    }
}
// 100000 in 10 threads
Container container = new Container(); 
Runnable runnable = () -> {
	for(int i = 0; i < 100000; i++) {
		container.add("foo");
	}
};
List<Thread> threads = new ArrayList<>();
for(int i = 0; i < 10; i++) {
	// add 100000 in different threads
	Thread thread = new Thread(runnable);
	threads.add(thread);
	thread.start();
}
for(Thread thread : threads) {
	thread.join();
}
System.out.println(container.size());

// Example, add non-sinchronized method size
public int size() {
	return list.size();
}
// And will wait with while instead of join
System.out.println("Elements count: " + container.size());
while (container.size() < 1_000_000) { }
System.out.println("Finished");
// In this case we can wait infinitely.
// The thing is that compiler can optimize this code like this.
// It inlines the size method and it sees that we don't change the container
// So the size should be the same, so it just check the condition in if on start
// And makes infinite loop
int size$0 = container.list.size
if(size$0 < 1000000) {
	while(true) {}
}
System.out.println("Finished!");
// But if we declare the count method as synchronized compiler doesn't do this optimization.
// SO WE SHOULD AND GET METHOD SYNCHRONIZED!!!



// CODE CAN BE EXECUTED NOT LIKE IT WRITTEN.
// Because of:
// - Compiler optimizations (previous example)
// - Microoptimizations in processor
// - Changes visibility in memory(MESI, NUMA, cache coherence, ...) - if you write
//   something into memory, it doesn't means that another kernel will see this change
//   at once


// Microoptimizations in processor(out of order execution)
// We have code
x = x * 2
y = y * 2
// For can see in as:
r1 := [ADDR1] // read from memory to register r1
r1 := r1 * 2 // multiplication
[ADDR1] := r1 // write into memory
r2 := [ADDR2] // same for y
r2 := r2 * 2
[ADDR2] := r2

// How it is executed
r1 := [ADDR1] // there is no value in processor cache(L1 level), 
              // let's go to the main memory
			  // It is a long operation. Memory can get it after 100 tacks.
			  // Processor will do another operations this time
r1 := r1 * 2  // skipped as depends on previous operation
[ADDR1] := r1 // skipped as depends on previous operation
r2 := [ADDR2] // There is ADDR2 in the processor cache on L1 level
r2 := r2 * 2  // multiplication
[ADDR2] := r2 // write result into cache on L1 level
// Memory still is loading ADDR1
// So the value in ADDR2 appeared in memory faster then ADDR1.
// It is no matter if we have one thread, but it can be seen from another thread.


// Changes visibility in memory
// Also notice that in previous example we wrote result into processor cache.
// Cache can be for kernal or shared between several kernals.
// So that other kernals can see value that stored in the kernal cache
// used cache coherency protocol(MESI). It is made for performance.
// So different threads(on different kernals) can see ifferent values of one valiable.


// JAVA MEMORY MODEL
// It describes interaction between an application and memory.
// It isn't depend on JVM, OS and hardware.
// It gives some guarantes about when and how record in memory will be seen.
// So all JVM must implement memory model so, that guarantees have been fulfilled

// NOTE, JAVA MEMORY MODEL IS NOT ABOUT LOCAL VARIABLES AND METHOD PARAMETERS
// THEY ARE ACCESSED ONLY IN ONE, FUNCTION EXECUTED THREAD.
// JAVE MEMORY MODEL IS ABOUT ARRAYS AND FIELDS.

// Some terms from memory model.
// ATOMICITY
// Operations is atomic if nobody can observe partial result of the operation.
// An observer can see a system state before or after the operation.
// In java:
// - Writing to a boolean, byte, short, chart, int, float, Object fields is atomic
// - Writing high and low 32 bits to a long/double field is an atomic operation.
// - Writing to a long/double with modifier VOLATILE is atomic.

// Example: if writing to an object field isn't atomic.
// Processor read data by cache lines(their length usually is 64 bytes)
// Let our object reference lays on joint of two cache lines.
// In first thread we tries to do this.obj = new Foo()
// In second this.obj.bar = 5;
// We write new address to two cache lines in first thread.
// In the second thread we read first cache line from memory(it is not cached)
// and it is right, but the second line from cache(it is not updated)
// So the result address is incorrect.
// But VM guaranteed that pointer is aligned, and it can't be in two cache lines.

// Arithmetic operations aren't atomic.
// For example x++, x *= 2
// We increamt count 10000000 times in 10 threads but result is wrong.
// We can simulteniously read count (the same), increament and two time 
// write the same value. So the value increased on 1 not 2.
class Counter {
    int count = 0;
}
Counter counter = new Counter();
Runnable runnable = () -> {
	for (int i = 0; i < 1_000_000; i++) {
		counter.count++;
	}
};
List<Thread> threads = Stream.generate(() -> new Thread(runnable))
		.limit(10)
		.peek(Thread::start)
		.toList();

for(Thread thread : threads) {
	thread.join();
}
System.out.println(counter.count);


// VISIBILITY
// Note, it is incorrect to ask when next operation start to execute? Because,
// we know compiler or processor can change order of operations.
// Also it is incorrect to ask when the value will be in main memory. Maybe it won'tacks
// ever be in main memory just in core cache.

// Instead of it, we should say about VISIBILITY.
// - If result of the operations write X, executed in thread A, visible for operation
//   read X in thread b, then we have visibility. 
// - It is a property for 2 threads and cell in memory.
// - There is no global visibility, it is only for pair of threads.

// So the queastion is: is this reading of the X variable can see the resulr of
// that writing to variable X. We say here about the writing visibility in another
// threads.


// ORDERING(HAPPENS BEFORE)
// Note, there are several orders in memory model but the most important is happens before.
// It is a predicate on two records(lines of code)
// - A HAPPENS BEFORE B if all writings executed before point A inclusivly
// are visible in any reading after point B inclusively
// - A hb B, B hb C -> A hb C - transitivity

// We have some guarantees for happens before
// - For two operations A and B in the same thread A hb B if A before B in 
// the program text(program order). 
// - Call of thread.start() hb first action in the new thread (first action
// in a new thread can see all writing that happened before thread.start())
// - Last action in the thread hb thread.join() (after thread.join() we can see
// all writings of the thread)
// - Default object initialization (fields initialization) hb any other action.
// This means if we save object reference into a variable and other thread uses
// this variable, it never can see fields before initialization. Note, it can see
// fields before constructor invokation but not before initialization.
// - For two synchronized sections on one object we can say synchronization
// end of first section hb synchronization start of the second section (second section)
// always see the writings of the first section



// VOLATILE MODIFIER
// By default when we read or write field we use PLAIN write / read.
// Plain write / read doesn't give any guarantees.
// If field declared as volatile we use volatile read / write
// And for volatile write and volatile read we have happens before.
volalite int[] x;
x = new int[10]; // volatile write
x[0] = 1; 

// - volatile write hb volatile read for the same value
// This means that we will see all change before write after read.
// Example
class Foo {
    int x;
    volatile int y; // declared as volatile
}
// In first thread we assin 1 to x and y.
// In second thread we wait y assignation and print x.
// Without volatile we don't know when second thread will see the changing
// of y field. Can be infinite loop.
// Also we have no guarantees that second thread will see x field change(prints 1 not 0)
// With volatile we are sure that on read y we will see all changed from first thread
// (for fields x and y)
for(int i = 0; i < 100_000; i++) {
	Foo foo = new Foo();
	Thread thread1 = new Thread(() -> {
		foo.x = 1;
		foo.y = 1;
	});
	Thread thread2 = new Thread(() -> {
		while(foo.y != 1) {}
		System.out.println(foo.x);
	});
	thread1.start();
	thread2.start();
	thread1.join();
	thread2.join();
}



// EXAMPLE THREAD-SAFETY SINGLETON WITH LASY INITIALIZATION
// Default implementation isn't thread safety, we can create two containers simoltaniously.
class Container {
    private static Container INSTANCE;

    private Container() {}

    public static Container getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Container();
        }
        return INSTANCE;
    }
}

// We can use synchronized, it works
public static synchronized Container getInstance() {
	if(INSTANCE == null) {
		INSTANCE = new Container();
	}
	return INSTANCE;
}

// But we need synchronized only on initialization. Then synchronized 
// will just reduce performance.
// We can try DOUBLE CHECKED LOCKING
public static Container getInstance() {
	if(INSTANCE == null) {
		synchronized (Container.class) {
			if(INSTANCE == null) {
				INSTANCE = new Container();
			}
		}
	}
	return INSTANCE;
}
// Note, we need second null check, in case if two thread try to enter the synchronized
// section. The the first thread will create an instance and the second thread will wait.
// The the second thread will enter the synchronized section and we mustn't create second instance.
// But this code have a problem:
// We will pring x in two threads:
System.out.println(Container.getInstance().x);
// Execution can be like that:
// We execute first thread until assigning object reference to a variable
// But before exit of the synchronization section. Then we execute 
// thread 2 and see that the instance variable not null and read INSTANCE.x
// But we don't have happens before on INSTANCE.x variable, so we may not see
// that x = 1 in the second thread, instead we can see that x = 0

// Thread 1                     Thread 2
// getInstance()             ---getInstance()
//   read INSTANCE           |    read INSTANCE
//   monitorenter            |  read INSTANCE.x  
//   read INSTANCE           |
//   $newObject = [allocate] |
//   $newObject.x = 1        |
//   INSTANCE = $newObject----
//   monitorexit
// read INSTANCE.x

// We can fix it by adding volatile to INSTANCE
private static volatile Container INSTANCE;
// If we will see in the second thread that INSTANCE != null =>
// We will see all writings before INSTANCE = $newObject including $newObject.x = 1 


// INITIALIZATION ON DEMAND HOLDER IDIOM.
// We can use another solution.
// We should create inner class Holder with a just static field of Container.
// And returns this instance`
class Container {
    class Holder {
        private static final Container INSTANCE = new Container();
    }
    public static Container getInstance() {
        return Holder.INSTANCE;
    }
}
// Note, it works lazy too. Because initialization of the class (static fields)
// happens just before first use of the class. So, the initialization happens-before-guarantee
// on first getInstance().
// But what if we simoltaniously call getInstance?
// We shouldn't think about it! The java specification guarantee that
// every class will be initialized just one time not more. So we will get
// the same instance.



// FINAL
// In the previous example the problem was that inizilization of the field x 
// we can't see in another thread. Actually, this default initialization
// happens in the default constructor. So the problem is that the initialization
// in the constuctor couldn't be seen for another thread.
// But if the field is FINAL, and we don't assign reference to the constructed
// object anywhere in constructor then all threads will see INITIALIZED field value.
// Note, if we assign to final field an object with non-final fields the 
// we can't guarantee that the object's non-final fields will be visible on as initialized
// in other threads.
 







// FROM jenkov.com 
// https://jenkov.com/tutorials/java-concurrency/java-happens-before-guarantee.html
// SYNCHRONIZED GUARANTEE
// - Synchronized Entry Visibility Guarantee - When a thread enters 
// a synchronized block, all variables visible to the thread are refreshed 
// from main memory.
// - Synchronized Exit Visibility Guarantee - When a thread exits a synchronized block, 
// all variables visible to the thread are written back to main memory.
// - Synchronized Block Beginning Happens Before Guarantee. Let's look on example
public void get(Values v) {
	synchronized(this) {
		v.valC = this.valC;
	}
	v.valB = this.valB;
	v.valA = this.valA;
}
// As you can see, the synchronized block at the beginning of the method 
// will guarantee that all of the variables this.valC, this.valB and 
// this.valA are refreshed (read in) from main memory. The following reads 
// of these variables will then use the latest value.
// For this to work, none of the reads of the variables can be reordered 
// to appear before the beginning of the synchronized block. 
// If a read of a variable was reordered to appear before the beginning 
// of the synchronized block, you would lose the guarantee of the variable 
// values being refreshed from main memory. That would be the case with 
// the following, unpermitted reordering of the instructions:
public void get(Values v) {
	v.valB = this.valB;
	v.valA = this.valA;
	synchronized(this) {
		v.valC = this.valC;
	}
}
// - Java Synchronized Block End Happens Before Guarantee. Let's look on example:
public void set(Values v) {
	this.valA = v.valA;
	this.valB = v.valB;

	synchronized(this) {
		this.valC = v.valC;
	}
}
// As you can see, the synchronized block at the end of the method will 
// guarantee that all of the changed variables this.valA, this.valB and 
// this.valC will be written back to (flushed) to main memory when the 
// thread calling set() exits the synchronized blocks.
// For this to work, none of the writes to the variables can be reordered 
// to appear after the end of the synchronized block. If the writes to 
// the variables were reordered to to appear after the end of the 
// synchronized block, you would lose the guarantee of the variable values 
// being written back to main memory. That would be the case in the following, 
// unpermitted reordering of the instructions:
public void set(Values v) {
	synchronized(this) {
		this.valC = v.valC;
	}
	this.valA = v.valA;
	this.valB = v.valB;
}

// VOLATILE GUARANTEES
// - Volatile Write Visibility Guarantee - When you write to a Java 
// volatile variable the value is guaranteed to be written directly 
// to main memory. Additionally, all variables visible to the thread 
// writing to the volatile variable will also get synchronized to main memory.
// - Volatile Read Visibility Guarantee - When you read the value of a 
// Java volatile the value is guaranteed to be read directly from memory. 
// Furthermore, all the variables visible to the thread reading the volatile 
// variable will also have their values refreshed from main memory.
// - Happens Before Guarantee for Writes to volatile Variables
// The reads / writes before a write to a volatile variable are guaranteed to 
// "happen before" the write to the volatile variable. Notice that it is still
// possible for e.g. reads / writes of other variables located after a write 
// to a volatile to be reordered to occur before that write to the volatile.
// Just not the other way around. From after to before is allowed, but from 
// before to after is not allowed.
// Example:
this.frame = frame;
this.framesStoredCount++;
this.hasNewFrame = true;  // hasNewFrame is volatile
// This code can't be reordered like this:
this.hasNewFrame = true;
this.frame = frame;
this.framesStoredCount++;
// As in this case we do not refresh frame and framesStoredCount in main memory
// - Happens Before Guarantee for Reads of volatile Variables
// Reads from and writes to other variables cannot be reordered to occur 
// before a read of a volatile variable, if the reads / writes originally 
// occurred after the read of the volatile variable. Notice that it is 
// possible for reads of other variables that occur before the read of a 
// volatile variable can be reordered to occur after the read of the volatile. 
// Just not the other way around. From before to after is allowed, but from 
// after to before is not allowed.
// Example:
int a = this.volatileVarA;
int b = this.nonVolatileVarB;
int c = this.nonVolatileVarC;
// This code can't be reordered like this:
int c = this.nonVolatileVarC;
int b = this.nonVolatileVarB;
int a = this.volatileVarA;
// As in this case, we do not refresh nonVolatileVarC, nonVolatileVarB before its reading


// IN A NUTSHELL, we read from main memory all thread variables on read 
// volatile field or on enter synchronized section. We write all thread 
// variables to main memory on write volatile field or on exit of synchronized
// section.


