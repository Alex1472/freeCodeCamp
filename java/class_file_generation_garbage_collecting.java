// We want to generatate bytecode on our own, without compiler.
// We can use ASM, ByteBuddy, BCEL, cglib, javassist library.
// ASM and ByteBuddy are more modern.
// ASM is more lowlevel than ByteBuddy.
// We can generate and read bytecode with ASM.

// We will use class loader to load a class. If we want to load the MyRunnable
// class, we will generate it with ASM and load.
ClassLoader loader = new ClassLoader() {
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if(name.equals("MyRunnable")) {
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            // some code to generate the MyRunnable class
			byte[] data = classWriter.toByteArray();
			return defineClass("MyRunnable", data, 0, data.length);
		}
		return super.loadClass(name);
	}
};
Class<?> loadedClass = loader.loadClass("MyRunnable");
Runnable loaded = loadedClass.asSubclass(Runnable.class)
		.getConstructor().newInstance();
loaded.run();
// We will generate this class:
public class MyRunnable implements Runnable {
    public void run() {
        System.out.println(3 + 10);
    }
}



// To create a class file, we need to use ClassWrite
// COMPUTE_MAXS - calculate locals and stack depth(in the code section)
// COMPUTE_FRAMES - generate stack frame table(need for verification)
ClassWriter classWriter = 
	new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
// Opcodes.V1_8 - class file version, for java 1.8
// Then class flags goes. 
// Opcodes.ACC_PUBLIC - public class,
// Opcodes.ACC_SUPER - from old times
// "MyRunnable" - class name,
// null - generic signature, 
// "java/lang/Object" - parent class
// new String[] { "java/lang/Runnable" } - interfaces to implement
classWriter.visit(Opcodes.V1_8, Opcodes.ACC_SUPER | Opcodes.ACC_PUBLIC, 
"MyRunnable", null, "java/lang/Object", new String[] { "java/lang/Runnable" });

// Create default constructor, we must create it.
MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V",
	/* generic signature */ null, /* Exceptions */ null);
// Load this on stack
mv.visitVarInsn(Opcodes.ALOAD, 0);
// Call base constructor(from Object)
mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", 
	false);
mv.visitInsn(Opcodes.RETURN);
mv.visitMaxs(0, 0); // local and stack depth, as we say to calculate them
// automatically can pass any values
mv.visitEnd();

// Run method
mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "run", "()V", null, null);
mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
mv.visitInsn(Opcodes.ICONST_3);      // stack: System.out, 3
mv.visitIntInsn(Opcodes.BIPUSH, 10); // stack: System.out, 3, 10
mv.visitInsn(Opcodes.IADD);          // stack: System.out, 13
mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V",
		false);
mv.visitInsn(Opcodes.RETURN);
mv.visitMaxs(0, 0);
mv.visitEnd();

byte[] data = classWriter.toByteArray();



// GARBAGE COLLECTING
// There are 4 main approaches to garbage collecting.
// - Copying(scavenge)
// - Mark-and-sweep
// - Mark-and-compact
// - Reference counting
// In java(hostspot) first three method are used.
// First three methods can be combined.

// Infant mortality or the generational hypothesis is the observation that,
// in most cases, young objects are much more likely to die that old objects.
// So let's divide heat into two regions: young gen and old gen.
// In time object from young gen can be moved to old gen(if it lives through
// n(15) garbage collectings).
// So we can carry out MINOR GC - collecting only in young gen
// And MAJOR GC - collecting in young and old gen.
// Usually minor gc works fast, major gc - slow.

// COPYING GARBAGE COLLECTING
// We have two regions in memory: fromspace and tospace.
// When fromspace is full, garbage collection happens.
// At first, all object are in fromspace. Garbage collector finds all alive
// object with algorithm similar to Djikstra algorithm. All alive objects
// are copied to tospace. Then we swap from and tospaces
// - If many object died fast, it doesn't slow garbage collecting because
// algorthms collect alive objects but not dead.
// - Main drawback we need twice as much memory.
// - Usually young gen uses this algorithm.

// REGIONALIZED COPYING
// We can use several regions(1000 and more).
// Garbage collector select the most filled regions, searches alive objects
// and copies them to a new empty(one) region.

// The problem that usually during searching alive object and coping 
// program works. New objects can be created and the application can use 
// the links of object that we are moving. These complicate garbage collecting
// algorithm.

// MARK-AND-SWEEP, MARK-AND-COMPACT
// They don't need additional memory.
// Mark-and-sweep mark released memory blocks as empty. So they can be used
// further. It doesn't move objects. Because of this we don't need to 
// refresh links, but there is segmentation.
// Mark-and-compact moves objects to the begining of the heap. So we don't
// have segmentation. Mark-and-compact usually stops the application and
// moves objects. In java was used mark-and-compact.



// PARALLEL/CONCURRENT GC
// Parallel: GC or some steps are parallel, app(user threads) are stopped.
// STW can lasts long (several seconds)
// SO WORKS PARALLEL GC THAT WAS USED IN JAVA BEFORE JAVA 8.
// Concurrent: some step are parallel with app(app works during gc).
// In this case garbage collecting is continous.
// STW lasts several ms. But when we use link we should do additional step
// to ensure that the object isn't moving now.
// CONCURRENT GC NOW IS USED IN JAVA.
// STW(Stop-the-world) - stop of all user threads in the app for service 
// functions(gc)



// JAVA GARBAGE COLLECTORS
// - Serial GC: works in one thread, stops the app. Need for debugging vm
// as the most simple.
//   In young gen - copying
//   In old gen - mark-compact
// - Parallel GC(default before Java 8) - as Serial GC but several steps are
// parallel
// - CMS (concurrent mark sweep) - deleted in new java version. 
//   In young gen - copying
//   In old gen - concurrent mark sweep constantly.
// - G1(used in java since java 9) - regional collector
//   - Heap is separated into several regions with the same size
//   - Some regions are marked as young gen, another - old gen. all new
//   objects creates in young gen regions.
//   - Marks objects concurrently, copyies with app stop. When it has 
//   enough memory to free, it stops the app and copies objects.
// - Shenandoah(RedHat), ZGC(Oracle) - regional cuncurrent collectors.
// Copies objects in parallel. But to sync state we still need STW.



// TLAB-ALLOCATION
// TLAB = thread local allocation buffer
// Usually we have large memory segment where there is no garbage
// (with mark-and-compact). But the problem when we try to allocate the 
// memory from different thread simoltaniously. We can use CAS 
// but it is slow. Instead TLAB is used. For every thread its own memory 
// buffer is allocated by heap. Only this thread can write into this memory.
// Memory allocation in this thread happens like this:
long current_pos;
byte* tlb_start;
byte* allocateObject(long object_size) {
	if(current_pos + object_size > tlab_size) {
		tlab_start = request_new_tlab();
		current_pos = 0;
	}
	byte* ptr = tlab_start + current_pos;
	current_pos += object_size;
	return ptr;
}
// So to allocate memory we just need two additions and one comparation. 
// It is very fast!