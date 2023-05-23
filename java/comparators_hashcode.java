// HASHCODE
// - Generated randomly, it is NOT connected to object address
// - If you overrided equals, you should override hashCode
// - a.equlas(b) => a.hashCode() == b.hashCode()
// - for primitives Double.hashCode(double)
// - for arrays Arrays.hashCode()
//   By default hashCode for an array isn't connected to hashCodes of its elements
//   It's a random number.
// - Objects.hash(field1, field2, Arrays.asList(arrayField3), ...);
//   Unlike arrays, lists calculate their hashCode by its content
//   So, if you have an array field use Arrays.asList() to convert it to 
//   list and then get hashCode

// Example:
@Override
public int hashCode() {
	int result = field1 != null ? field1.hashCode() : 0;
	result = 31 * result + (field2 != null ? field2.hashCode() : 0);
	result = 31 * result + (field3 != null ? field3.hashCode() : 0);
	result = 31 * result + (field4 != null ? field4.hashCode() : 0);
	return result;
}
// or
public int hashCode() {
	return Objectes.hash(field1, field2, field3, field4);
}



// COMAPRATORS
// You can specify order on objects with the interface Comparable<T>
public interface Comparable<T> {
	public int compareTo(T o);
}
// < 0 => this < o
// == 0 => this == o
// > 0 => this > o

// Example:
class User implements Comparable<User> {
    final String name;
    public User(String name) { this.name = name; }

    @Override
    public int compareTo(User o) {
        return name.compareTo(o.name);
    }
}

// Specification for compareTo
// - sgn(x.compareTo(y)) == -sgn(y.compareTo(x))
// - x.compareTo(y) throws <=> y.compareTo(x) throws
// - x.compareTo(null) throws NullPointerException
// - (x.compareTo(y) > 0 && y.compareTo(z) > 0) => x.compareTo(z) > 0
// - x.compareTo(y) == 0 => for every z: sgn(x.compareTo(z)) == sgn(y.compareTo(z))
// - recommended: x.compareTo(y) == 0 <=> x.equals(y)
// - recommended: x.compareTo(y) != Integer.MIN_VALUE


// External way to specify order in an interface COMPARATOR<T>
public interface Comparator<T> {
	int compare(T o1, T o2);
}
// <0 => o1 < o2
// ==0 => o1 == o2
// >0 => o1 > o2

// Comparator specification
// - sgn(compare(x, y)) == -sgn(compare(y, x))
// - compare(x, y) throws <=> compare(y, x) throws
// - (compare(x, y) > 0 && compare(y, z) > 0) => compare(x, z) > 0
// - compare(x, y) == 0 => for every z: sgn(compare(x, z)) == sgn(compare(y, z))
// - recommended compare(x, y) != Integer.MIN_VALUE
// - recommended x.equals(y) => compare(x, y) == 0

// Example:
class User {
    final int age;
    public User(int age) { this.age = age; }
}
class UserComparator implements Comparator<User> {
    @Override
    public int compare(User a, User b) {
        return Double.compare(a.age, b.age);
    }
}

// IF YOU WANT TO COMPARE BASED ON PRIMITIVE FIELD IT'S BETTER TO USE 
// STANDARD STATIC METHODS Integer.compare(), Double.compare(), ...

// To compare by several fields use this approach:
class User implements Comparable<User> {
    final String name;
    final int age;
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int compareTo(User o) {
        int res = name.compareTo(o.name);
        return res == 0 ? Integer.compare(age, o.age) : res;
    }
}

// It's better to implement comparator as lambda:
static final Comparator<User> USER_COMPARATOR = (a, b) -> {
	int res = a.getName().compareTo(b.getName());
	return res == 0 ? Integer.compare(a.getAge(), b.getAge()) : res;
};

public static void main(String[] args) {
	User user1 = new User("Tom", 20);
	User user2 = new User("Jack", 25);
	System.out.println(USER_COMPARATOR.compare(user1, user2));
}

// You can specify comparators, like this
// we should pass function that returns field to compare
static Comparator<User> USER_COMPARATOR =
	Comparator.comparing((User u) -> u.getName())
	.thenComparingInt(u -> u.getAge());
// As a second parameter we can pass comparator to compare field
// Reverse order
static Comparator<User> USER_COMPARATOR =
	Comparator.comparing((User u) -> u.getName(), Comparator.reverseOrder())
	.thenComparingInt(u -> u.getAge());
// Case insensitive
static Comparator<User> USER_COMPARATOR =
	Comparator.comparing((User u) -> u.getName(), String.CASE_INSENSITIVE_ORDER)
	.thenComparingInt(u -> u.getAge());
// If you want null users go first you should wrap it all into Comparator.nullsFirst
static Comparator<User> USER_COMPARATOR =
	Comparator.nullsFirst(
			Comparator.comparing((User u) -> u.getName())
			.thenComparingInt(u -> u.getAge()));
// If you want nulls in fields goes first, use Comparator.nullsFirst for field
static Comparator<User> USER_COMPARATOR =
		Comparator.comparing((User u) -> u.getName(), Comparator.nullsFirst(Comparator.naturalOrder()))
		.thenComparingInt(u -> u.getAge());
		
// You can pass method reference instead of lambda
static Comparator<User> USER_COMPARATOR =
		Comparator.comparing(User::getName)
		.thenComparingInt(User::getAge);