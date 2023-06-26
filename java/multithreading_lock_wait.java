// NOTE, YOU CAN USE DUMP THREAD BUTTON TO DEBUG DEADLOCK IN IDEA
// OR USE JSTACK
// YOU CAN USE THE DL-CHECK TO FIND POTENTIAL DEADLOCKS IN A PROGRAM.
// Example log:
"Thread-0" #22 [18264] prio=5 os_prio=0 cpu=171.88ms elapsed=226.55s tid=0x0000027f292ba3d0 nid=18264 waiting for monitor entry  [0x000000d618eff000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at Main.transfer(Main.java:31)
	- waiting to lock <0x00000007196ea8b8> (a java.util.ArrayDeque) // is waiting another locking on another ArrayDeque
	- locked <0x00000007196e7268> (a java.util.ArrayDeque) // this thread take locking on ArrayDeque
	at Main.lambda$main$0(Main.java:15)
	at Main$$Lambda$15/0x00000008010031f8.run(Unknown Source)
	at java.lang.Thread.runWith(java.base@20/Thread.java:1636)
	at java.lang.Thread.run(java.base@20/Thread.java:1623)
// Another thread	
"Thread-1" #23 [19492] prio=5 os_prio=0 cpu=156.25ms elapsed=226.55s tid=0x0000027f292b7400 nid=19492 waiting for monitor entry  [0x000000d618ffe000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at Main.transfer(Main.java:31)
	- waiting to lock <0x00000007196e7268> (a java.util.ArrayDeque)
	- locked <0x00000007196ea8b8> (a java.util.ArrayDeque)
	at Main.lambda$main$1(Main.java:21)
	at Main$$Lambda$16/0x0000000801003410.run(Unknown Source)
	at java.lang.Thread.runWith(java.base@20/Thread.java:1636)
	at java.lang.Thread.run(java.base@20/Thread.java:1623)


// DEADLOCK
// It can occur the situation when two threads wait events, but this events
// can't happen. It's called DEADLOCK
// For example one thread take two blockings and another thread takes these
// blockings in reverse order. First thread takes the first blocking, second
// thread takes the second blocking. Then the thread try to take another 
// blocking, but can't because another blocking was already taken by another
// thread.

// There are four conditions that should be fulfilled so that a deadlock can
// happen
// - there is an unshared resource
// - at least two unshared resources(one taken, one are trying to take)
// - resource can be released only by those who keep it
// - in waiting graph there is a cycle. Vertex - an object on which we lock
// edge(from A to B) - trying to take locking on object b, when we took 
// blocking on A 

// Example we tring to move element from one queue to another with lock
static void transfer(Queue<String> in, Queue<String> out) {
	synchronized (in) {
		synchronized (out) {
			String elem = in.poll();
			if(elem != null)
				out.add(elem);
		}
	}
}
Queue<String> queue1 = new ArrayDeque<>(Arrays.asList("foo", "bar", "buz"));
Queue<String> queue2 = new ArrayDeque<>(Arrays.asList("foo", "bar", "buz"));
Thread thread1 = new Thread(() -> {
	for (int i = 0; i < 100_000; i++) {
		System.out.println("Thread1: " + i);
		transfer(queue1, queue2);
	}
});
Thread thread2 = new Thread(() -> {
	for (int i = 0; i < 100_000; i++) {
		System.out.println("Thread2: " + i);
		transfer(queue2, queue1);
	}
});
System.out.println("Started");
thread1.start(); thread2.start();
thread1.join(); thread2.join();
System.out.println("Finished");



// DEADLOCK ON CLASS LOADING
// Deadlock can occurs on class loading. JVM takes internal lock on a class
// loading.
// Example:
public class Main {
    static {
        int res = IntStream.range(0, 1000)
                .parallel()
                .map(x -> x * 2)
                .sum();
        System.out.println(res);
    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println("main");
    }
}
// JVM takes internal lock on the Main class loading. Then we use parallel 
// streams and lambdas in the map method.
// Lambdas are converted into class methods. But we can't use this method
// from another thread as we have lock on class because it is not initialized.
// And we can't end initialization because to calculate res must use lambda.



// LIVE LOCK 
// Live lock - situation when threads are working but there is no any progress.
// Example:
// When we try to take second lock we check it it is already taken and
// if it is unlock first lock, sleep some time and take first lock again.
// We try to wait until second lock will be free.
void getLocks12(Lock lock1, Lock lock2) {
	lock1.lock();
	while(lock2.locked()) {
		lock1.unlock();
		sleep();
		lock1.lock();
	}
	lock2.lock();
}
void getLocks21(Lock lock1, Lock lock2) {
	lock2.lock();
	while(lock1.locked()) {
		lock2.unlock();
		sleep();
		lock2.lock();
	}
	lock1.lock();
}
// But if we have second method that does the same things in the reverse order
// we can simoltaniously take first/second lock, wait, etc
// In this case both threads will work but wait in the while cycle.




// CONDITION WAITING, SPIN LOOP, ONSPINWAIT
// In some cases we don't want to use synchronized and object to wait,
// because we don't have any suitable object for this. For example in 
// on thread we want to wait until in another thread data will be loaded
// We can use spin loop (while for this)
static volatile boolean isDataReady = false;
Thread thread = new Thread(() -> {
	try {
		System.out.println("Start Sleeping...");
		Thread.sleep(5000);
		System.out.println("End Sleeping...");
		isDataReady = true;
	} catch (Exception e) { }
});
thread.start();
while(!isDataReady) {} // wait until data will be ready in another thread
System.out.println("End main");
// The problem is that spin look consumes processor resources because the
// while loop will be executed.

// If we what to wait in a loop we can use the onSpinWait method there.
// It optimizes processor resouces consuming.
while(!isDataReady) {
	Thread.onSpinWait();
}
// USUALLY SPIN LOOP IS USED WHEN WE KNOW THAT WAITING WILL BE SHORT
// (LESS THEN 10MICROSECONDS). IF WE WILL WAIT SEVEREL MS OR LONGER IT'S
// TO BETTER USE ANOTHER METHODS.

// WAIT, NOTIFY, NOTIFYALL METHODS
// You can use the mathods wait, notify and notifyall to implement waiting.
// Let's look at example:
// We will wait with method wait, and notify that data is ready with the 
// method notifyAll.
// We should wrap the wait and notifyAll methods with synchronized block
// on the current object

// It works like this:
// 1) We enter the waitForData method and take the blocking. 
// 2) On method wait we will wait the until someone will notify. 
// Note we wait but leave the blocking.
// 3) Then we take the blocking in the deliver method.
// 4) Wait until we will get the data.
// 5) Call notify all. 
// 6) Note another waitForData will continue working ONLY WHEN the deliver 
// method leave the blocking. So first the deliver method will be executed
// until the end and leave blocking
// 7) Then another thread (waitForDeliver) will take blocking after wait
// and continue execute the code

// Note, wait can interrupt waiting without notification, it's called spurious wakeup.
// So we should use loop with checking isDataReady
// We use notifyAll to notify all waiters, notify - to notify just one waiter.
// Also note, when we try to awake several threads, they will be awaken in turn,
// because the awaken thread takes locking, so other awaken threads will wait
// this locking.
class Tester {
    boolean isDataReady = false;

    synchronized void waitForData() {
		System.out.println("Sleeping before waiting");
		Thread.sleep(5000);
		System.out.println("End sleeping before waiting");
        try {
            while (!isDataReady) {
                System.out.println("Start waiting");
                wait();
                System.out.println("End waiting");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    synchronized void deliver() {
        try {
            System.out.println("Start sleeping");
            Thread.sleep(5000);
            System.out.println("End sleeping");
            isDataReady = true;
            System.out.println("Notifying...");
            notifyAll();
            System.out.println("Sleeping again");
            Thread.sleep(5000);
            System.out.println("End sleeping");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
Tester tester = new Tester();
Thread thread = new Thread(tester::deliver);
thread.start();
tester.waitForData();
System.out.println("Data was get");
// Sleeping before waiting
// End sleeping before waiting
// Start waiting
// Start sleeping
// End sleeping
// Notifying...
// Sleeping again
// End sleeping
// End waiting
// Data was get


