// In java you can use exceptions.
// Exteding exception
class MyException extends Exception {
}

// Throwing
static void test() throws MyException {
	throw new MyException();
}

public static void main(String[] args) throws Exception {
	test();
}

// - stack trace in java is filled when an exception created,
// not when thrown.
// - you can throw only object that inherited from java.lang.Throwable



// THROWS
// If you throw and don't handle an exception in a method you should declare
// that the method can throw that exception with the word throw
static void test() throws MyException {
	throw new MyException();
}
// If you handle an exception in the method, you shouldn't declare it
static void test() {
	try {
		throw new MyException();
	} catch(MyException e) {

	}
}

// The same is true for methods that invoke methods that can throws exception
public static void main(String[] args) throws MyException { // should also
// declare that can throw MyException
    Main.test(); // test declares that throws MyException
}
static void test() throws MyException { 
	throw new MyException();
}
// or we can handle the exception in parent method
public static void main(String[] args) { // shouldn't declare that the method
// can throw MyException
	try {
		Main.test();
	} catch(MyException e) {

	}
}



// CHECKABLE, UNCHECKABLE EXCEPTIONS
// Exception in java can be checkable and uncheckable.
// Ideology is:
// - checkable exception says that the problem is in outer world(in input)
//   IOException, SQLException, TimeoutException
// - uncheckable exception says that the problem is in your program or VMID
//   VirtualMachineError, NullPointerException(why null pointer, there is a bug in your program)

// You may not declare an unckable exception in the method
static void test() {
	new TimeoutException();
}
// Or may declare for documentation purpose
// But in this case you should handle it or declare in the parent method
static void test() throws TimeoutException {
	new TimeoutException();
}
public static void main(String[] args) throws TimeoutException {
	Main.test();
}

// Note, checkable exceptions are doubtful, criticized feature of java.



// ILLEGALARGUMENTEXCEPTION, ILLEGALSTATEEXCEPTION, UNSUPPORTEDOPERATIONEXCEPTION
// There are some exceptions that you can use for your needs:
// IllegalArgumentException, IllegalStateException(you invoke right method,
// but in this object state you shouldn't invoke it), UnsupportedOperationException



// ERROR, EXCEPTIONS
// From Throwable is inherited Error and Exception
// From Error is inherited classes usually for VM errors.
// You shouldn't create exceptions inherited from directly from Throwable
// or Error



// EXCEPTION STRUCTURE
// Exception has several fields, most important:
// - message - string 
// - cause - more low-level exception
// Throwable and Exception have constructors with them, so when you 
// creat your exception you can declare constuctors with them
// Throwable()
// Throwable(message)
// Throwable(cause)
// Throwable(message, cause)
class MyException extends Exception {
    public MyException(String message, Exception cause) {
        super(message, cause);
    }
}
throw new MyException("It's my exception!", new TimeoutException());

// You can use several method to get information from an exception:
// getMessage, getCause, getStackTrace, printStackTrace
System.out.println(e.getMessage());
System.out.println(e.getCause());
System.out.println(e.getStackTrace());
e.printStackTrace();
// You can have several errors, and you want add them all into your exception
// you can do this by adding them into suppressed exceptions
// Example, we have on main cause - OutOfMemoryError, and two suppressed errors
// IllegalArgumentException, TimeoutException
Exception exception = new MyException("It's my exception!", new OutOfMemoryError());
exception.addSuppressed(new IllegalArgumentException());
exception.addSuppressed(new TimeoutException());
// Get suppressed exceptions with the method getSuppressed
System.out.println(Arrays.toString(e.getSuppressed()));



// EXCEPTION HANDLING
// Use try - catch to handle exceptions
try {
	test();
} catch (MyException e) {
	e.printStackTrace();
}

// You can rethrow an exception in the catch block.
try {
	throw new IOException();
} catch (IOException e) {
	throw new MyException("Unable to read a file.", e);
}



// TRY-FINALLY
try {
	throw new IOException();
} finally {
	System.out.println("Do it!");
}

// Note, finally works with return, contitue and break.
static void test() {
	for(int i = 0; i < 10; i++) {
		try {
			if(i == 5) continue;
			if(i == 7) break;
			System.out.println("Do iteration");
		} finally {
			System.out.println("I = " + i);
		}
	}
}

// finally evaluates just before exit
// The example returns 6, because the finally block evaluates just before return
static int test() {
	try {
		return 5;
	} finally {
		return 6;
	}
}

// Note, evaluates just before return, after the value for return is evaluated.
// So, we evaluate value for return, then evaluate the finally block and then return.
// In this example, the function returns 5, because the expression for return
// evaluates before the the finally block. Therefore 5 value is saved in internal
// variable
static int test() {
	int x = 5;
	try {
		return x;
	} finally {
		x = 6;
	}
}














