// JAVA.UTIL.CUNCURRENT
// It has:
// - atomic variables
// - non-blocking collections
// - blocking collections
// - synchronization primitives
// - thread pools
// - CompletableFuture



// ATOMIC VARIABLES
// - AtomicBoolean/Integer/Long/Reference - has methods for atomic update and
// compareAndSet
// - AtomicInteger/Long/ReferenceArray - make array element access atomic and
// and volatile
// - AtomicInteger/Long/ReferenceFieldUpdater - used for optimization instead
// of AtomicBoolean/Integer/Long/Reference
// - Long/DoubleAccumulator, Long/DoubleAdder - when you want to modify 
// variable from many threads and get result after all. It has better perfor
// mance then atomic variables. They do not store current result, just in the end.

// We want so that method will be executed only once
// This doesn't work because we can several times check the flag field
// in different threads before we set flag = true in one of the threads. 
class Tester {
    private volatile boolean flag = false;

    public void doOnce(Runnable action) {
        if(!flag) {
            flag = true;
            action.run();
        }
    }
}

// The problem that flag reading and writing isn't atomic. Something can
// happen between them.

// We can use double checked locking. The problem is that the second
// thread will wait until first thread will execute the action. 
// We don't want that the second thread will wait, we want that it will exit
// right away.
public void doOnce(Runnable action) {
	if(!flag) {
		synchronized (this) {
			if(!flag) {
				flag = true;
				action.run();
			}
		}
	}
}

// We can move action out of the synch block.
// We set the variable set to true in synch section only. And then check 
// this variable
// It's better but we are still waiting here. Synch is rather long operation.
// NOTE, TRY TO WRAP IN SYNCH SECTION MINIMUM CODE. IT THIS CASE DON'T ADD
// HERE ACTION EXECUTION.
public void doOnce(Runnable action) {
	if(!flag) {
		boolean set = false;
		synchronized (this) {
			if(!flag) {
				set = true;
				flag = true;
			}
		}
		if(set) {
			action.run();
		}
	}
}

// COMPAREANDSET
// We can use AtomicBoolean here. It has an atomic method compareAndSet
// It checks if the variable has expected value if no set new value.
// Return if the new value was set.
// CompareAndSet operations is supported on processor level, so it is very
// fast
private AtomicBoolean flag = new AtomicBoolean(false);
public void doOnce(Runnable action) {
	if(flag.compareAndSet(false, true)) {
		action.run();
	}
}

// ATOMICINTEGERFIELDUPDATER
// If we don't want to create new object AtomicBoolean, because of performance.
// (we should go to AtomicBoolean first and then to value in this object,
// instead of one dereference with just a boolean variable)
// In this case use AtomicIntegerFieldUpdater
class Tester {
    private static final AtomicIntegerFieldUpdater<Tester> FLAG_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(Tester.class, "flag");
    private volatile int flag = 0; // updater field isn't volatile by default

    public void doOnce(Runnable action) {
        if(FLAG_UPDATER.compareAndSet(this, 0, 1)) {
            action.run();
        }
    }
}



// GETANDINCREMENT
// Based on compareAndSet you can implement atomic increment operation
// We want to change AtomicInteger on 1
static int getAndIncrement(AtomicInteger value) {
	// This loop called compare and set loop.
	int cur;
	do {
		cur = value.get();
	} while (!value.compareAndSet(cur, cur + 1)); // if cur hasn't changed
	// then change it, else read again
	return cur;
}
AtomicInteger value = new AtomicInteger(0);
Runnable action = () -> {
	for (int i = 0; i < 100_000; i++) {
		getAndIncrement(value);
	}
};
Thread thread1 = new Thread(action);
Thread thread2 = new Thread(action);
thread1.start(); thread2.start();
thread1.join(); thread2.join();

// Note, in AtomicInteger there is method for this - getAndIncrement
// It works faster, because it compiled into special processor instruction
Runnable action = () -> {
	for (int i = 0; i < 100_000; i++) {
		value.getAndIncrement();
	}
};


// This approach also work with another value updates.
static int getAndAddTwo(AtomicInteger value) {
	int cur;
	do {
		cur = value.get();
	} while(!value.compareAndSet(cur, cur + 2));
	return cur;
}
// There is a method to update an atomic variable - getAndUpdate
Runnable action = () -> {
	for (int i = 0; i < 100_000; i++) {
		value.getAndUpdate(x -> x + 2);
	}
};



// ATOMIC UPDATE SEVERAL VALUES
// We want to atomic update two values.
// We don't want to use synchronized.
// Naive realization doesn't work. 
class AtomicPoint {
    AtomicInteger x = new AtomicInteger(0);
    AtomicInteger y = new AtomicInteger(1);

    void rotateClockwise() { // on 90 degrees
        int oldX = x.getAndSet(y.get()); // this two operations aren't atomic
        y.set(-oldX);
    }
}
AtomicPoint point = new AtomicPoint();
Runnable action = () -> {
	for (int i = 0; i < 100_000; i++) {
		point.rotateClockwise();
	}
};
Thread thread1 = new Thread(action);
Thread thread2 = new Thread(action);
thread1.start(); thread2.start();
thread1.join(); thread2.join();

// We can use inner object that stores current state.
// Wrap it with AtomicReference
// And create new inner object (new state) on every update with atomic
// updateAndGet method. 
class AtomicPoint {
    private record Point(int x, int y) {
        Point rotateClockwise() {
            return new Point(y, -x);
        }
    }

    private final AtomicReference<Point> point = new AtomicReference<>(new Point(0, 1));

    void rotateClockwise() {
        point.updateAndGet(Point::rotateClockwise);
    }
}
// Note, updateAndGet works the same way. We get old point, create new,
// look if old point changed. If no - set new point. If changed repeat
// the operation (create new and check changes).
// THIS METHOD WE CAN CHANGE ANY OBJECT IN ATOMIC WAY.





// WAITING SEVERAL THREADS
// Kernels count available for your process.
// Note, you can limit available kernels count in operating systen for
// the process(or in docker).
int COUNT = Runtime.getRuntime().availableProcessors();

// COUNTDOWNLATCH
// With countdownlatch will wait until you count down (with the method countDown)
// as many as you specify in constructor
CountDownLatch latch = new CountDownLatch(1);
Runnable action = () -> {
	try {
		latch.await(); // will wait until count down
	} catch (InterruptedException e) { }
	doer.doOnce(() -> System.out.println("In thread " + Thread.currentThread()));
};
// Create and run several threads
latch.countDown(); // should count down one time

// if we specify
CountDownLatch latch = new CountDownLatch(2);
// You should count down 2 time and then thread will awake        
latch.countDown();
latch.countDown();

// Some classes for waiting
// - CountDownLatch - wait until count down to zero
// - CyclicBarrier - wait until all threads come to the waiting place
// - Semaphore - we specify the number. We can give resource in number
// more then zero. When we give resource we count down. If number
// is equal to zero a thread waits when another thread release resource.
// - Exchanger - if we want exchange values from different threads.
// From one thread we get a, from another - b. From first thread we get b,
// from second - a. One thread will wait until another come to the exchange 
// point.
// - Phaser - contains all functional from classes above.



// EXECUTERS, EXECUTORSERVICE, FUTURE
// With Executors we can create ExecutorService(ThreadPool). 
// Then we can create threads with ExecutorService
// To create new task use submit method, it returns Future<?>
// To await the result use future.get()
// In the end run shutdown to release thread resources.
// Example, test doOnce several times(JUMPS)
int JUMPS = 10;
AtomicReference<Doer> doer = new AtomicReference<>();
AtomicInteger count = new AtomicInteger(0);
Runnable action = () -> {
	doer.get().doOnce(count::incrementAndGet);
};

int COUNT = Runtime.getRuntime().availableProcessors();
// Create a thread pool 
ExecutorService executor = Executors.newFixedThreadPool(COUNT);
for (int i = 0; i < JUMPS; i++) {
	count.set(0);
	doer.set(new Doer());
	// use submit to create new task (thread)
	List<Future<?>> futures = Stream.generate(() -> executor.submit(action))
			.limit(COUNT)
			.collect(Collectors.toList());

	for(Future<?> future : futures) {
		future.get(); // wait until task will be completed
	}

	System.out.println(count.get());
	if(count.get() != 1) {
		System.out.println("Ooops!!!");
	}
}
executor.shutdown();

// You can create thread pool several ways
// - Executors.newFixedThreadPool - thread pool with fixed number of threads
// - Executors.newCachedThreadPool - unlimited number on threads
// - new ThreadPoolExecutor()
// - newSingleThreadScheduledExecutor() - you can specify when run tasks
// (not now, in the future)
// - newWorkStealingPool()



// COMPLETABLEFUTURE
// You can create futures without thread pool, with the runAsync method.
// It creates CompletableFuture.
// Note, it gets thread for futures from Common Thread Pool. You shouldn't
// release any resources
List<CompletableFuture<?>> futures = Stream.generate(() -> CompletableFuture.runAsync(action))
	.limit(COUNT)
	.collect(Collectors.toList());
for(CompletableFuture<?> future : futures) {
	future.get();
}

// You can union several futures and wait until all of them will be completed.
// Use the method CompletableFuture.allOf
CompletableFuture<?> future = CompletableFuture.allOf(
	Stream.generate(() -> CompletableFuture.runAsync(action))
	.limit(COUNT)
	.toArray(CompletableFuture[]::new));
future.join();

// SUPPLYASYNC, RUNASYNC, THENCOMBINE
// Example, let's use Futures to calculate combination
// To create a Future<Void>, that returns nothing we use 
// CompletableFuture.runAsync
// To create a Future<T> we use CompletableFuture.supplyAsync
// The method thenCombine (factN.thenCombine(factK, BigInteger::divide)) 
// waits until factN and factK will be calculated and then divide results
// and return Future

static BigInteger factorial(int n) {
	BigInteger res = BigInteger.ONE;
	while(n > 0) {
		res = res.multiply(BigInteger.valueOf(n));
		n--;
	}
	return res;
}
static BigInteger combination(int n, int k) {
	CompletableFuture<BigInteger> factN = CompletableFuture.supplyAsync(() -> factorial(n));
	CompletableFuture<BigInteger> factK = CompletableFuture.supplyAsync(() -> factorial(k));
	CompletableFuture<BigInteger> factNminusK = CompletableFuture.supplyAsync(() -> factorial(n - k));
	CompletableFuture<BigInteger> result = factN.thenCombine(factK, BigInteger::divide)
			.thenCombine(factNminusK, BigInteger::divide);
	return result.join();
}



// REENTRANTLOCK
// More advanced lock than synchronized.
// It has several methods to inspect the state of lock.
// It has methods lock and unlock.
// We can pass true into constructor so that threads will unlock in the 
// order that became locked.
// tryLock() - try to lock or don't wait just return false
// tryLock(5, TimeUnit.SECONDS) - try to lock and wait some time if can't
// get lock, then throw exception
class ThreadSafeList {
    private List<Integer> list = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock(true);

    public int get(int i) {
        lock.lock();
        try { return list.get(i); }
        finally { lock.unlock(); }
    }
    public void add(int value) {
        lock.lock();
        try { list.add(value); }
        finally { lock.unlock(); }
    }
}



// REENTRANTREADWRITELOCK
// We can read from diffrent threads the resource with ReentrantReadWriteLock
// simoltaniously
// We can't write from diffrent thread to the resource simoltaniously.
// If we want to read we will wait if there is a writing now. Then read.
// If we want to write we will wait until all readings will ended and 
// writing will ended and then write.
// Use ReadLock to lock reading operation, use WriteLock to lock writing
// operation
class ThreadSafeList {
    private List<Integer> list = new ArrayList<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public int get(int i) {
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        readLock.lock();
        try { return list.get(i); }
        finally { readLock.unlock(); }
    }
    public void add(int value) {
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        try { list.add(value); }
        finally { writeLock.unlock(); }
    }
}













