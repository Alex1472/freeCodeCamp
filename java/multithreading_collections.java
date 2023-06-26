// Obviously, we can't use usual collections in several threads. They can
// break with simoltanious access.
// We can use lock with any access. But lock takes a lot of time and don't 
// solve problem with modifing collection while iteration. We should use lock
// for the collection during all iteration long.
// Note, it is a serious science work to create cuncurrent collection.
// It's not just a code of usual developer.

// Competative collections can be:
// - Blocking
// - Nonblocking

// Nonblocking collections don't wait events from other threads.
// Except case we can update the same collection part simoltaniously.

// Blocking collections allows you to get element when it will be added there.
// Or collections that has fixed max size, so we will wait until another 
// element will be removed.



// NONBLOCKING COLLECTIONS
// MAIN PRINCIPALS
// - Simple operations are atomic (add, remove). We can't see any intermediate
// state 
// - Package operations can be non-atomic (addAll, removeAll).
// - Length isn't stored. To get length we usually should iterate over
// the collection (use isEmpty()). It is rather wierd operation for concurrent
// collections, because we can revome / add element from collection
// while iterating to count the size.
// - Don't throw ConcurrentModificationException
// - Usually weakly-consistent (after creating iterator changes can be 
// visible or not. It isn't specified)

// MAIN CLASSES
// - ConcurrentLinkedQueue(LinkedList)
// - ConcurrentLinkedDeque(LinkedList/ArrayDeque). If you need a queue, it's
// better to use ConcurrentLinkedQueue. Because it is faster.
// - CopyOnWriteArrayList(ArrayList)
// - CopyOnWriteArraySet
// - ConcurrentHashMap(+ newKeySet(), use this static method
// to create HashSet) (HashMap/HashSet)
// - ConcurrentSkipListMap(TreeMap)
// - ConcurrentSkipListSet(TreeSet)

// COPYONWRITEARRAYLIST(SET)
// - Contents is stored in array
// - All modification operations(add, remove) are synchronized
// (use synchronized).
// - Reading operations and interator aren't synchronized.
// - Any change COPIES the array.
// - Iterator, forEach goes through a snapshot on the moment of beginning 
// iteration
// - You can sort collection begin with Java 8.
// - CopyOnWriteArraySet - delegate to CopyOnWriteArrayList => contains
// works for O(n). As any write is coping an array => long contains is not 
// very bad.
// - Package operations work for O(n). So if you want to add several element
// use addAll instead of several add.

// CONCURRENTHASHMAP
// - Usually is used to store some data and aggregate statistics from it.
// - Until java 8, it is a union of 16 hashmaps
// Lock was used to add into a paticular hashmap.
// - In java 8 was rewritten, now it is more like usual hashmap
// - Implementation contains more then 6000 lines
// - Maximum will be locked one basket (in case of collision)
// - reduce*/search*/forEach* - creates parallel tasks
// - putIfAbsent, computeIfAbsent, merge - atomic, but can use synchronization.

// CONCURRENTSKIPLISTMAP
// - equivalent for TreeMap - sorted map
// - This map uses the skip-list data structure. It is randomized 
// (uses random in realization). 
// - There is ConcurrentSkipListSet
// - computerIfAbsent can invoke function 
// - merge can invoke function several times
// Update methods (for example merge), don't lock backet when trying to 
// update. If two threads try to update the backet simoltaniously they
// will compute the value and then set the value with compareAndSet. 
// One will set the value, and the second will look the change and calculate
// the value for the second time. And the will set the new value. So the 
// function calculated value can be called several times.

// ConcurrentSkipListMap stores several LinkedLists
// First contains all entries, second - half, third - quater, and so on.
// So map contains logN lists(N - elements count)
// When we add new element, we go to the list with smallest number of elements
// If this list contains the element we return it, if not we give the neareast
// element on this level and go to the down level and repeat the procedure
// If we don't have the element in list we add it into the down list (with 
// all elements). Then with probability .5 add the element into the upper list
// If success with with probability .5 add element into the next upper list
// and so on.
// This algorithm is used because there is thread-safe LinkedList. 
// But there is no thread-safe balanced trees.



// BLOCKING COLLECTIONS
// It is usually a queue. Supposed, there is a producer and consumer for 
// the queue. Producer adds elements, consumer gets elements.
// We will wait uptil an elements will be added.

// - SynchronousQueue(without memory) - when we try to add element, we will
// wait until someont gets this element
// - ArrayBlockingQueue(has fixed size) - we will wait if the queue is empty
// and we want to get an element. We will wait if the queue is full and we 
// try to add element.
// - LinkedBlockingDeque - we can don't specify the size. Also can wait on
// getting and addition(with fixed size).
// - PriorityBlockingQueue(PriorityQueue + ArrayBlockingQueue)

// Example:
SynchronousQueue<String> queue = new SynchronousQueue<>();
Runnable producer = () -> {
	int i = 0;
	while(true) {
		try {
			Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));
			String value = String.valueOf(i++);
			queue.put(value);
			System.out.println("Added: " + value);
		} catch (InterruptedException e) { }

	}
};
Runnable consumer = () -> {
	while(true) {
		try {
			Thread.sleep(ThreadLocalRandom.current().nextInt(500, 2000));
			String value = queue.take();
			System.err.println("Read: " + value);
		} catch (InterruptedException e) { }
	}
};
new Thread(producer).start();
new Thread(consumer).start();
// We use SynchronousQueue, and elements produced faster than consumed.
// But with SynchronousQueue, addition will wait until we try to read 
// an element.

// If we will use ArrayBlockingQueue with capacity 10, 10 element will be 
// added gradually. Then to add new element some element must be read.
ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);



// PARRALLEL STREAMS
// To create a parallel stream use .stream() to create from stream or
// .parallelStream() to create from collection.
List<Integer> res = List.of(1, 2, 3, 4).stream()
	.parallel()
	.map(x -> x * x)
	.filter(x -> x < 10)
	.toList();
List<Integer> res = List.of(1, 2, 3, 4).parallelStream()
	.map(x -> x * x)
	.filter(x -> x < 10)
	.toList();

// Order of execution and result in some method changes in parallel stream.
// findAny in returns any element not first as with one thread
int res = List.of(1, 2, 3, 4).parallelStream()
	.findAny()
	.get(); // 3, random element, not 1 as with one thread
// forEach also doesn't save order
List.of(1, 2, 3, 4).parallelStream()
	.forEach(x -> System.out.println(x)); // 3 4 2 1, not 1 2 3 4
// findFirst() and forEachOrdered() save order but have poor performance.

// toList(), toArray(), max(), sum() save order and have good performance
// filter(), map() have good performance

// Example, in specification written that reduce should be associative
// We try to calculate hashcode with method from default implementation.
// But it doesn't work because the function in reduce isn't associative.
// But it is used with parallel streams. With .stream() all work fine.
List<String> list = List.of("a", "b", "c", "d", "e");
int hashCode = list.parallelStream()
		.map(String::hashCode)
		.reduce(1, (a, b) -> a * 31 + b);
System.out.println(hashCode); // 135230
System.out.println(list.hashCode()); // 121228546


