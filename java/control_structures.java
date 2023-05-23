// You can use code block as is
// Variables created in this block are visible only there.
public static void main(String[] args) {
	{
		int x = 1;
		System.out.println(x); // 1, correct
	}
	System.out.println(x); //error
}

// You can use ; to specify empty block
int x = 2;
if(x > 1);
else {
	System.out.println("test");
}
// nothing printed



// IF
int x = 1;
if(x > 0) {
	System.out.println("More");
} else {
	System.out.println("Less");
}

// You can leave out brackets for one command
int x = 1;
if(x > 0)
	System.out.println("More");
else
	System.out.println("Less");

// In () must be a boolean type.
// Java doesn't convert types with another type will a compilation error.
if(1) // error
	System.out.println("Test") 
	
// Several if
int x = 1;
if(x < 0) {
	System.out.println("Less");
} else if(x == 0) {
	System.out.println("Equal");
} else {
	System.out.println("More");
}

// If you don't specify it explicitly, else refers to last if
boolean a = true;
boolean b = false;
if(a)
	if(b)
		System.out.println("a and b");
	else
		System.out.println("other"); // other, with a = false, b = true it prints nothing



// TERNARY OPERATOR
int x = -1;
System.out.println(x > 0 ? "More" : "Less"); // More



// SWITCH
// Switch from Java 14. x should be a number(except long), string or enum.
int x = 2;
switch(x) {
	case 1 -> System.out.println("one");
	case 2, 3 -> {
		System.out.println("Two");
		System.out.println("Three");
	}
	default -> System.out.println("Default");
}

// More old school switch
int x = 2;
switch(x) {
	case 1:
		System.out.println("One");
		break; // should use break after case code
	case 2: // should use several case for several values
	case 3:
		System.out.println("Two or three");
		break;
	default:
		System.out.println("Default");
}

// Switch with return values
// From Java 14, x should be a number(except long), string or enum.
int x = 2;
System.out.println(switch(x) {
	case 0 -> "Zero";
	case 1, 2 -> "One or two";
	default -> "Other";
});



// WHILE
// With empty body
// Use ; for empty block
System.out.println("Type 'exit' to exit");
while(!System.console().readLine().contains("exit")); //exit when enter exit

// Ordinal while
int x = 0;
while(x < 5) {
	System.out.println(x);
	x++;
}

// do - while
// body executes at least one time
int x = 0;
do {
	System.out.println(x);
	x++;
} while (x < 5);



// FOR
for(int i = 0; i < 5; i++) {
	System.out.println(i);
}
// While equivalent
int i = 0;
while(i < 5) {
	System.out.println(i);
	i++;
}

// Infinite loop
for(;;) {}

// FOREACH
int[] arr = {1, 2, 3, 4, 5};
for(int x: arr) {
	System.out.println(x);
}



// BREAK, CONTINUE
int[] arr = {1, 2, 3, 4, 5};
for(int x : arr) {
	if(x > 3) break;
	System.out.println(x); //1 2 3
}

int[] arr = {1, 2, 3, 4, 5};
for(int x : arr) {
	if(x % 2 == 1) continue; 
	System.out.println(x); //2 4
}