// MANUALLY RUN JAVA
// You can just run java program if it consists of one file
// Go to jdk .jdks/openjdk-20/bin
// In prompt run:
java.exe myProgram.java

// To compile java into byte code use javac. 
javac.exe myProgram.java //myProgram.class file will be created

// To run byte code file use java and specify class with the main method
// By default VM tries to find this class in a current directory
java.exe myProgram

// To disassemble bytecode file use javap
javap.exe -c myProgram.class

// You can enter jshell to run java interpreter
jshell
2 + 2 //4
/exit // to exit



// TYPES:
// logical: boolean
// integer: byte, short, int, long
// fractional: float, double
// symbol: char
// reference: null, different objects



// COMMENTS
// You can leave comments with one line and multiline comments:
// It's a one line comment
/*
   It's a multiline comment
*/

// You can use special javadoc comments to document you code.
// Every line of that comment starts with asterisk
// You can add these comments to classes, methods, ...
// These comments are commonly made up of two sections:
// 1) The description of what we're commenting on
// 2) The standalone block tags (marked with the “@” symbol) which describe 
// specific meta-data
// Example:
/*
* A program that prints <code>Hello world</code>
*
* @author Alexander
* @version 1.0
*/
public class Main {
    /**
    * Program entry point
    *
    * @params args command line arguments
    */
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}



// VARIABLES
// Initialization:
int x;
int x = 5;

// final - you can assing value just one time
final int x = 5; // correct
final int y;
y = 10; // correct
final int z;
z = 5;
z = 10; // incorrect

// declare several variables
int x = 5, y = 10;
int x, y;

// type inference
var x = 5; // you should always assing a value. Note, x has static type
//you can't change it
var y; // incorrect

// - local variable exists in a code block (between { })
// - in a nested block you can't define variable with the same name

// Assignment
x = 5; // correct
x = y; // correct 
x = y = 5; // correct 
x += 5; // correct 

// Note, you can't use uninitialized variable
int x;
System.out.println(x); // error
int x;
if(Math.random() > 0.5) {
	x = 1;
}
System.out.println(x); // error



// BOOLEAN OPERATIONS
// There is a wrapper Boolean
// |, &, ||, && - the different that ||, && do not evaluate right part if the result known
System.out.println(true | false); // true
System.out.println(true || false); // true
System.out.println(true & false); // false
System.out.println(true && false); // false

// ^, != - the same
System.out.println(false ^ true); // true
System.out.println(false != true); // true
System.out.println(true ^ true); // false
System.out.println(true != true); // false

// ==, !
System.out.println(!true); // false
System.out.println(true == false); // false

// |=, &=, ^=
boolean res = true;
res ^= true;
System.out.println(res); // false

boolean res = true;
res |= false;
System.out.println(res); // true

boolean res = true;
res &= false;
System.out.println(res); // false



// INTEGER NUMBERS
// name                wrapper               MIN_VALUE             MAX_VALUE
// byte                Byte                  -2^7                  2^7-1
// short               Short                 -2^15                 2^15-1
// char                Character             0                     2^16-1
// int                 Integer               -2^31                 2^31-1
// long                Long                  -2^63                 2^63-1



// LITERALS
// You can specify is literal int or long. By default it is an int.
int a = 1;
// Use L to specify that literal has a long type.
long b = 123L

// You can use _ in any part of you number. It changes nothing.
long a = 1_234_567;

// You can define binary(2), octal(8), hexadecimal(16) number in java.
// Note, it changes nothing just format to define.
// For binary use 0b prefix
int a = 0b1_1011;
System.out.println(a); //27
// For octal use 0(zero) prefix. It's tricky, better do note use octal numbers.
int a = 034;
System.out.println(a); //28
// For hexadecimal use 0x prefix
int a = 0x1234_ABCD;
System.out.println(a); //305441741



// OPERATIONS WITH INTEGER NUMBERS
// +, -, *, /, % 
// / - integer division
System.out.println(5 / 2) // 2
// % - reminder
System.out.println(5 / 2) // 1

// +=, -=, *=, /=, %=
// ++, --

// Arithmetic operation without overflow. The methods throw ArithmeticException
// in this case
// Math.addExact(), Math.subtractExact(), Math.multiplyExact(), Math.incrementExact()
// Math.decrementExact(), Math.negateExact() - returns netagation(-)
System.out.println(Math.addExact(2, 3));
System.out.println(Math.addExact(Integer.MAX_VALUE, 1));
System.out.println(Math.negateExact(5));

// Some useful functions
Math.abs()
Math.min()
Math.max()
Integer.parseInt()
Integer.parseUnsignedInt()
Integer.signum() // 1 if x > 0, 0 if x == 0, -1 if x < 0



// BIT OPERATIONS
// ~, |, &, ^, <<, >>, >>>
// &=, |=, ^=, <<=, >>=, >>>=



// CHARS
// Unicode is used for chars
char c = 'a'; //correct
char c1 = 'a' + 1; //correct 'b'
char c2 = c + 1; //incorrect, compilation error
char c3 = '\u270D' // supports unicode
char c = '\\'; // just \ 
char c = '\n'

// For symbol from unicode that not in first 2^16-1 base symbols
// we should use 2 chars. These symbols are named surrogate pair
// It consists of high surrogate and low surrogate
System.out.println("\ud83c\udf70"); // a cake
// Note, length of the string is length of the string in chars
// If string contains a surrogate pair, the number of symbols and the length 
// of the string are not equal

// Some operations with symbols:
// Character.toUpperCase, Character.toTitleCase, Character.isDigit, 
// Character.isAlphabetic, Character.getNumericValue 
System.out.println(Character.toUpperCase('c')); //C
System.out.println(Character.isDigit('4')); //true
System.out.println(Character.isAlphabetic('d')); //true
System.out.println(Character.getNumericValue('7')); //7



// REAL TYPES
// type      Wrapper      MIN_VALUE     MAX_VALUE    Bits    Mantissa    Order
// float     Float        1.4*10^-45    3.4*10^38    32      23          8
// double    Double       4.9*10^-324   1.798*10^308 64      52          11

// Real literals
// By default real literal is double
double x = 1.0;
float x = 1.0; //error
double f = 1.0F;
double y = 1.0D;
float z = 1.0D; //error
// With exponential format
double x = 1.1e-19;

// There are +0.0 and -0.0. They are equal with ==, but different with toString
System.out.println(Double.toString(0.0)); //0.0
System.out.println(Double.toString(-0.0)); //-0.0

// There are some special numbers
Double.POSITIVE_INFINITY
Double.NEGATIVE_INFINITY
Double.NaN // NaN is not equal, less or more than other number, every operation
// with NaN is NaN

// Operations
// +, -, *, /, %
// +=, -=, *=, /=, %=
// ++, -- left, right

// Some useful methods:
// Double.isNan, Double.isFinite, Double.isInfinite
// Math.sin, Math.pow, Math.floor, Math.ceil, Math.round



// LONG ARITHMETIC
// For long arithmetic use classes java.math.BigInteger and java.math.BigDecimal



// Type conversion
// There are widening primitive conversion (from less accuracy to more) and
// narrowing primitive conversion
// Widening primitive concersion works automatically, that conversion is allowed:
byte -> short -> int -> long -> float -> double
         char ->  |
long x = 1;
float y = x; // works automatically

// Note, you can lose precision with these conversion(because for real numbers
// we should store an order)
int -> float
long -> float
long -> double

// With narrowing primitive conversion you can lose precision or get overflow.
// You should cast type explicitly in this case.
byte <- short <- int <- long <- float <- double
        char
long x = 100_444_666;
int y = (int)x;



// Integer literal are always have types int or long.
// But you can write byte b = 123. How it works?
// There are compilation time constants, they are:
// - literals
// - initialized final variables
// - operations with constants
// - ...

// This operations compiler calculates during compilation
// and substitute the value
// If the value in range of type with lower value range, you shouldn't use cast
byte x = 123
byte y = Integer.MAX_VALUE; // Error, MAX_VALUE is not in range of byte
// But if with overflow calculated value in the range, it works!
byte x = Integer.MAX_VALUE + Integer.MAX_VALUE; // Works, Integer.MAX_VALUE + Integer.MAX_VALUE in the byte range



// Type conversion in expressions
// In expressions these converions will be.
// unary operation, bit shift byte, short, chart -> int 
byte a = 1;
byte b = -a; // error, -a is an int

// Binary operations:
// If one operand is a double, another - double
// Else, if one operand is a float, another - float
// Else, if one operand is a long, another - long
// Else both - int
byte a = 1;
byte b = 2;
byte c = a + b; // error, a + b - int

// Sevaral operations are just operations that calculated in turn
// If to take into account type converion, the results are different
double a = Long.MAX_VALUE;
long b = Long.MAX_VALUE;
int c = 1;
System.out.println(a + b + c); // 1.8446744073709552E19 double -> double
System.out.println(c + b + a); // 0.0 long -> double

// Operators +=, -= and other similar apply type casting implicitly.
byte x = 1;
x = x + 1; //error
x += 1; // works, cast to byte applied x = (byte)(x + 1)



// ARRAYS
// Arrays in Java are objects.
int[] ints
// Array is an object in the heap.
// In memory an array is represented like this:
// Header Length [0] [1] [2] [3]
// Header contains information about array type
// Array in java always has fixed size.
// When array is created you can't change size.

// Initialization:
int[] ints = new int[10];
int[] initialized1 = new int[] {1, 2, 3};
int[] initialized2 = {1, 2, 3, 4};
int[][] table = new int[2][5];
int[][] initializedTable = new int[][] {{1, 2}, {3, 4, 5}, {6}};
// Note, in java 2-dimentional array in array of links to arrays, 
// So sizes of inner array can be different.

// Array methods:
int[] arr = {1, 2, 3, 4, 5};
System.out.println(arr.length);
System.out.println(arr[0]);
arr[1] += 1;
int[] copy = arr.clone(); //shallow copy
System.out.println(copy == arr); // false, reference equal
System.out.println(copy.equals(arr)); // false, reference equal
System.out.println(Arrays.equals(arr, copy)); // true, shallow equal
System.out.println(arr); //[I@15aeb7ab
System.out.println(Arrays.toString(arr)); [1, 3, 3, 4, 5]
Arrays.sort(arr);




