// Object has a header with useful information, for example about type
// Objects are compared by reference.
// Every class is inherited from jave.lang.Object
class Point {
	int x, y; // fields, public by default
	void print() {
		System.out.println("X:" + x + " Y:" + y);
	}
}

Point point = new Point();
point.x = 5;
point.y = 6;
point.print();


// Method is just a function with set of parameters. Instance is passed
// as implicit parameter this.
// You can get access to it with this.
class Point {
	int x, y;
	void print(String test) {
		System.out.println(this.x + " " + this.y + " " + test);
	}
}

// Also you can specify this parameter explicitly
class Point {
	int x, y;
	void print(Point this, String test) { // specifing this
		System.out.println(this.x + " " + this.y + " " + test);
	}
}
Point point = new Point();
point.x = 1;
point.y = 2;
point.print("test"); // You shouldn't pass instance as a parameter.



// You can return value from a method
class Point {
	int x, y;
	double norm() {
		return Math.sqrt(x * x + y * y);
	}
}

Point point = new Point();
point.x = 1;
point.y = 2;
System.out.println(point.norm());



// You can overload methods
class Point {
	int x, y;
	void shift(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}
	void shift(int dx) {
		shift(dx, 0);
	}
	void print() {
		System.out.println(this.x + " " + this.y);
	}
}

Point point = new Point();
point.x = 1;
point.y = 2;
point.shift(2, 1);
point.print();



// INITIALIZATION
// You can specify default values
class Point {
	int x = 1, y = 2;
	void print() {
		System.out.println(this.x + " " + this.y);
	}
}

Point point = new Point();
point.print();

// You can use constructor
class Point {
	int x, y;

	Point(int x, int y) { // constructor
		this.x = x;
		this.y = y;
	}
	void print() {
		System.out.println(this.x + " " + this.y);
	}
}

Point point = new Point(3, 4);
point.print();

// You can call one constructor from another
class Point {
	int x, y;

	Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	Point(int x) {
		this(x, 0);
	}
}



// IMMUTABLE OBJECTS
// You can make fields immutable with word final.
// You only can assign this fields in constructor.
class Point {
	final int x, y; // immutable fields

	Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	Point() {} // Error, you should assign final fields in constructor
}

// For immutable class, you can create a record
record Point(int x, int y) { //specify fields, they are immutable
                             //constructor, accessors will be created automatically
	void print() {
		System.out.println(this.x + " " + this.y);
	}
}
Point point1 = new Point(3, 4);
point1.print
System.out.println(point1.x());
System.out.println(point1.y());
System.out.println(point1.x); // Note, fields are public
System.out.println(point1.y);

// You can create another constructor, but it always should delegate to 
// anothor constructor
record Point(int x, int y) {
	Point() {
		this(1, 2);
	}
}
Point point2 = new Point();



// STATIC 
// You can create static fields and method in a class
// To forbid creating instances, add private constructor. (it doesn't work for me)
class Utils {
	static int res = 1;
	static void test(String message) {
		System.out.println(message);
	}

	private Utils() {
	}
}

System.out.println(Utils.res);
Utils.res += 1;
System.out.println(Utils.res);
Utils.test("fff");



// Object methods
// From Object any class inherit several methods
// - toString()
// - equals()
// - hashCode()
// - getClass()
// - wait()
// - notify()
// - notifyAll()
// - clone()
// - finalize() - deprecated

// toString
// You can override default string representation of object with 
// the method toString
class Point {
	int x, y;
	Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "Point<" + this.x + ", " + this.y + ">";
	}
}
Point point = new Point(1, 2);
System.out.println(point); // Point<1, 2>

// equals 
// specify equals 
class ArrayVector implements Vector {
    final double[] arr;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayVector that = (ArrayVector) o;
        return Arrays.equals(arr, that.arr);
    }
}
Vector vector1 = new ArrayVector(1, 1, 1);
Vector vector2 = new ArrayVector(1, 1, 1);
System.out.println(vector1.equals(vector2)); // true
// Note, operator == doesn't use the equals method, it compares by reference



// INSTANCEOF 
// You can check is object has any type is instanceof
record Point(int x, int y) {
	void print() {
		System.out.println("Point<" + x + ", " + y + ">");
	}
}
public static void main(String[] args) {
	Point point = new Point(3, 4);
	process(point);
}

static void process(Object object) {
	if(object instanceof Point) {
		Point point = (Point)object;
		point.print();
	}
}

// You can use shortcut for check and cast:
static void process(Object object) {
	if(object instanceof Point point) { // if object is Point, create
	                                    // point variable in if scope
		point.print();
	}
}

// There is a subtle difference between instanceof and cast
// It is a behavior with null. Instance of for null return false
// Cast works with null;
System.out.println(null instanceof Point); // false
Point point = (Point)null; // Works
System.out.println(point); // null




// ACCESS MODIFIERS
// Modifier        Class       Package       Subclass       Another Package
// public          yes         yes           yes            yes
// protected       yes         yes           yes            no
// package-private yes         yes           no             no
// private         yes         no            no             no

// - procected methods are accessed the the same package(not 
//   necessarily in a heir)
// - default modifier is package-private - access in the same class and 
//   in the same package
// - Note, you can create a private method in an interface.



// SUPER
// Use the word super to invoke a method / constructor from parent class
public class WorldGreeter extends AbstractGreeter {
	public WorldGreeter() {
		super("World"); // invoke the parent constructor
	}
	
	void test(AbstractGreeter other) {
		super.hello(); // invoke the parent method
	}  
}

