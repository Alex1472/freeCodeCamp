// Fully-qualified class name
// It consists of three parts
// java.util     .      PrimitiveIterator    .     OfInt
// Package name        Top level class name     Simple name
//                     (for nested classes)    

// Note classes are names from capital letter, packages from lowercase letter

// VM uses another notation:
// We use $ between classes, otherwise /
// java/util/PrimitiveIterator$OfInt



// In java must be only one public class in a .java file. And file
// should have the same name as this class. For example:
// MathUtils.jave
public class MathUtils {
}



// Package has the same name as path from root to it.
// For example
// Spelling package contains SpellingChecker class
// Test package contains MyTest class

// Test                               Test package
//  |----Spelling                     Test.Spelling package
//  |       |-----SpellChecker.java
//  |
//  |----MyTest.java

// - On first line of the file we should specify package name
//   It should correspond the file location
     package Test
     package Test.Spellingx

// - From other packages you can use all public classes from the package.

// - You can just use fully-qualified class name
     Test.Spelling.SpellChecker checker = new Test.Spelling.SpellChecker()
	 
// - You can use simple name for classes from the same package

// - You can import this class
//   Note, import just say: we can use simple name instead of
//   fully-qualified class name. We do not execute the import package
//   It's like an alies
import Test.Spelling.SpellChecker
SpellChecker checker = new SpellChecker();

// - You can import all classes from the package
import Test.Spelling.*
SpellChecker checker = new SpellChecker();

// - Note, there is no relationship between nested packages
//   So, you don't have access to class of nested package from outer package



// Usually a package has name corresponding the domain name of a company,
// product or project
// For example: org.stepik.java, com.google.common
// Corresponding folder structure: org/stepik/jave, com/google/common



// STATIC IMPORT
// You can import static method // field from class, or all static methods/fields
import static java.util.Arrays.sort;

int[] arr = {1, 3, 2, 1, 3};
sort(arr);
for(int x: arr) {
	System.out.println(x);
}

// import all static methods/fields
import static java.util.Arrays.*;

