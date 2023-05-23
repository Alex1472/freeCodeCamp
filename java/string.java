// STRINGS
// String aren't modified
// CREATION
String s1 = "Hello world!";
String s2 = new String("Hello World!");
String s3 = new String(new char[] {
		'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'
});



// METHODS
"Hello World!".startWith("Hello");
"Hello World!".endsWith("World");
"Hello World!".contains(" ");
"Hello World!".equalsIgnoreCase("HeLLo WoRLd!");
"Hello World!".indexOf('e');
"Hello World!".indexOf("oo");
"Hello World!".lastIndexOf("o", 10); // search from end, you can specify
                                     // index from which to search
"Hello World!".replace('o', 'a'); // replace all occurances
"Hello World!".replace("Hello", "Goodbye");
"Hello World!".replaceFirst("^Hello", "Goodbye"); // replace one occurances
                                                  // use regex to specify string
"Hello World!".replaceAll("\\w", "x"); // replace all occurances, use regex to specify string
"   Hello World!   ".trim();
"Hello World!".substring(6);
"Hello World!".substring(6, 11);
"Hello World!".split(" ");



// STRING FORMATTING
// - Concatination: +
// - String.concat() - do not use it
// - String.format()
String make = "make";
String java = "java";
String great = "great";
String again = "again";
System.out.println(String.format("%s %s %s %s", make, java, great, again));
// You can specify locale
System.out.println(String.format(Locale.ENGLISH,"%s %s %s %s", make, java, great, again));

// - MessageFormat.format()
//   MessageFormat has placeholders that are different from String.format
String make = "make";
        String java = "java";
        String great = "great";
        String again = "again";
        System.out.println(MessageFormat.format("%s %s %s %s", make, java, great, again));

// - StringBuilder(StringBuffer)
StringBuilder builder = new StringBuilder();
for(Weekday weekday: Weekday.values()) {
	builder.append(weekday).append("\n");
}
System.out.println(builder.toString());
// - String.join()
// - StringJoiner - similar to StringBuilder
// - "*".repeat(100)



// Note, the methods toLowerCase, toUpperCase uses current computer locale.
// With english locale this works:
String string = "INT";
String lower = string.toLowerCase();
System.out.println(lower.equals("int")); //true
// But in turkish there are i with dot and without. So it is not works.
System.out.println(lower.equals("int")); // false
// To avoid this you can use the root locale
String string = "INT";
String lower = string.toLowerCase(Locale.ROOT);



// By default, java uses system encoding
// When you convert string to bytes and viseversa, better to specify encoding
System.out.println(new String(new byte[] {
                72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33
        }, StandardCharsets.UTF_8));
System.out.println(Arrays.toString("Hello World!".getBytes(StandardCharsets.UTF_8)));

// The same is for reading string from files, we should specify how to convert
// file bytes to symbols
new FileReader("/etc/passwd", StandardCharsets.UTF_8);
