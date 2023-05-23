// Java supports interfaces
// Usually we document our interfaces
// Example:
/**
 * Represents an immutable 3D-vector
 */
interface Vector {
    /*
     * Returns a vector component
     * @param n component number, must be 0 (X), 1 (Y), 2 (Z)
     * return a value of corresponding vector component
     */
    double component(int n);
    /*
     * @return a vector length
     */
    double length();
    /*
     * Sums two vectors
     * @param other a vector to add to this vector
     * @return a result of addition
     */
    Vector plus(Vector vector);
}

// Let's add implementation
// We use implements keyword to specify interfaces we implement.
class ArrayVector implements Vector { 
    final double[] arr;
    ArrayVector(double x, double y, double z) {
        arr = new double[] {x, y, z};
    }

    @Override
    public double component(int n) {
        return arr[n];
    }
    @Override
    public double length() {
        return Math.sqrt(arr[0] * arr[0] + arr[1] * arr[1] + arr[2] * arr[2]);
    }
    @Override
    public Vector plus(Vector vector) {
        return new ArrayVector(
			arr[0] + vector.component(0),
			arr[1] + vector.component(1),
			arr[2] + vector.component(2)
        );
    }
}
// Note, we use @Override annotation. With this annotation
// compiler check is there method to override in a base class 
// or is there method to implement in an interfaces we implement
// To override or implement method you don't have to specify @Override
// it's just to avoid spelling mistakes.

// You can create in interface DEFAULT implementation
interface Vector {
    double component(int n);
    String toString();
	
    default double length() {
        double x = component(0);
        double y = component(1);
        double z = component(2);
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    default Vector plus(Vector vector) {
        return new ArrayVector(
                component(0) + vector.component(0),
                component(1) + vector.component(1),
                component(2) + vector.component(2)
        );
    }
}
// Note, you can override this implementation
class ZeroVector implements Vector {
    public static Vector INSTANCE = new ZeroVector();

    ZeroVector() {}

    @Override
    public double component(int n) { return 0; };
    @Override
    public double length() { return 0; }
    @Override
    public Vector plus(Vector other) { return other; }
}

// Also, you can add static method into an interface.
interface Vector {
	...
	static Vector of(double x, double y, double z) {
		if(x == 0 && y == 0 && z == 0) {
			return ZeroVector.INSTANCE;
		}
		return new ArrayVector(x, y, z);
	}
}
Vector vector1 = Vector.of(0, 0, 0);
Vector vector2 = Vector.of(1, 1, 1);

// Also, interface can have fields, they are always final static
interface Vector {
    int count = 0;
}
System.out.println(Vector.count);
Vector.count++; // Compilation error, interface variable is always static

// You can clarify return value for inherited interface
interface Supplier {
    Object get();
}
interface StringSuplier extends Supplier {
    @Override
    String get();
}
class MySupplier implements StringSuplier {
    public String get() {
        return "test";
    }
}
// It implements both interfaces.
Supplier supplier1 = new MySupplier();
Object value1 = supplier1.get();
StringSupplier supplier2 = new MySupplier();
String value2 = supplier2.get();



// ABSTRACT CLASS
// You can create an abstract class
// Note, it can not to implement an interface, thus declared, that it 
// implement. Its heirs should implement this interface.
abstract class AbstractVector implements Vector { // it not implements 
                                                  // the Vector interface
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;
        Vector that = (Vector) o;
        return component(0) == that.component(0) &&
                component(1) == that.component(1) &&
                component(2) == that.component(2);
    }
    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[] { component(0), component(1), component(2)});
    }
    @Override
    public String toString() {
        return "(" + component(0) + ", " + component(1) + ", " + component(2) + ")";
    }
}

// Heir should implement Vector interface
class ZeroVector extends AbstractVector { // use extends to inherit from another class
    public static Vector INSTANCE = new ZeroVector();

    ZeroVector() {}

    @Override
    public double component(int n) { return 0; };
    @Override
    public double length() { return 0; }
    @Override
    public Vector plus(Vector other) { return other; }
}

// In abstract class you can create abstract method. Non abstract heirs
// should implement it.
abstract class AbstractVector {
	public abstract void log(String message);
}

class ArrayVector extends AbstractVector {
	@Override
    public void log(String message) { // implementation of abstract method
        System.out.println(message);
    }
}



// FINAL CLASS AND METHOD
// You can make class final, so no class can be inherit from this class
final class ArrayVector extends AbstractVector {
	...
}

class ExtendedArrayVector extends ArrayVector { // compilation error
	...
}

// You can make a method final. So no class can override it.
class ArrayVector extends AbstractVector {
    public final void log(String message) {
        System.out.println(message);
    }
}

class ExtendedArrayVector extends ArrayVector {
    @Override
    public void log(String message) { // compilation error
        System.out.println(message);
    }
}



// SEALED INTERFACES AND CLASSES
// With sealed and permits keywords you can specify classes that implement / 
// inherited from you interface / class. Any another classes aren't allowed
// to implement / inherit interface / class
sealed interface Vector permits AbstractVector { // only AbstractVector 
                                                 // can implement Vector
   ...
}

sealed abstract class AbstractVector implements Vector permits ArrayVector, 
	FieldVector, ZeroVector { // only specified three classes can be inherited from AbstractVector

}

// Note, with sealed and final we can completly seal the hierarhy
// So nobody can add a class to it.



// ANONYMOUS CLASS
// You can create anonymous class, just write 
new AbstractClass/Interface {
	//implementation
}
Vector zero = new Vector() { // Vector is an interface
	@Override // implementation of interface method
	public double component(int n) {
		return 0;
	}
	@Override // implementation of interface method
	public double length() {
		return 0;
	}
};





