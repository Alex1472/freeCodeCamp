// OPTIONAL
// Optional is a "box" with or without a value
// Create, use methods of, ofNullable, empty
Optional<Integer> optional = Optional.of(1); // from not null value
System.out.println(optional.isPresent());
optional = Optional.empty(); // null box
System.out.println(optional.isPresent());
optional = Optional.ofNullable(null); // from nullable value
System.out.println(optional.isPresent());
optional = Optional.ofNullable(3);
System.out.println(optional.isPresent());

// Methods
// Filter returns origin optional if it sutisfied the condition, or empty Optional
boolean isPresent = Optional.of("test").filter("bar"::equals).isPresent();
System.out.println(isPresent);
// Map create new Optional with new value using the map function
String res = Optional.of("   test   ").map(String::trim).get();
System.out.println(res);
// flatMap creates accepts function that create new optional
int result = Optional.of(1).flatMap(x -> Optional.of(x + 1)).get();
System.out.println(result);
// orElse returns values of the optional or if it is empty specified value
result = Optional.<Integer>empty().orElse(5);
System.out.println(result);
// With method orElseGet you can pass supplier, instead of constant
result = Optional.<Integer>empty().orElseGet(() -> 5);
System.out.println(result);
// With orElseThrow you can throw exception, if Optional is empty.
// You should pass a function that creates an exception
result = Optional.<Integer>empty().orElseThrow(Exception::new);
System.out.println(result);
// or return new Optional, if current is empty. It accepts 
// supplier that creates new Optional, 
result = Optional.<Integer>empty().or(() -> Optional.of(5)).get();
System.out.println(result);
result = Optional.of(1).or(() -> Optional.of(5)).get();
System.out.println(result);



// STREAMS
// Note, you can use the button Trace Current Stream Chain to debut 
// stream in intellij idea
// - ALL STREAM (STREAM<T>, INTSTREAM, LONGSTREAM, DOUBLESTREAM) are interfaces.
//   Their implementation are package private, you can't use them directly.
// - Stream is a flow of items of the same type.
// - Usually source creates a lazy stream.
// - Intermediate operations(map, filter) do nothing just add a receipt of handling elements
// - All work execute on terminal operations(forEach)
// - Stream can be infinite
Stream.of(1, 2, 3)
	.map(x -> x + 1)
	.forEach(System.out::println);
Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
	.filter(x -> x % 2 == 0)
	.forEach(System.out::println);
	
// SOURCES
// - Stream.empty()
// - Stream.of(x, y, z) - from elements
// - Stream.ofNullable(x) - empty stream if x is null or stream of one elements
// - Stream.generate(supplier) - generates infinite stream, to get elements
//   calls supplier
	 Stream.generate(() -> 1).forEach(System.out::println); // infinite stream of ones
// - Stream.iterate(seed, f) - calculate next value based on previous
	 Stream.iterate(0, x -> x + 1).forEach(System.out::println);
// - Stream.iterate(seed, condition, f) - calculates new value based on previous
//   while value sifisfied the condition
	 Stream.iterate(0, x -> x < 100, x -> x + 1).forEach(System.out::println); // 0 .. 99
// - collection.stream() - create stream from collection
     ArrayList<Integer> list = new ArrayList<>();
 	 list.add(1);
	 list.add(2);
	 list.add(3);
	 list.stream().forEach(System.out::println);
// - Arrays.stream(array) - stream from array, only for int/long/double/Object
	 int[] arr = new int[] { 1, 2, 3 };
	 Arrays.stream(arr).forEach(System.out::println);
// - Random.ints/longs/doubles() - creates infinite stream of random ints/longs/doubles
	 Random rand = new Random();
	 rand.ints().forEach(System.out::println);
// - String.chars - stream of the string chars as ints
	 "abacaba".chars().forEach(System.out::println);
// ...

// There are specialized streams for performance.
// - java.util.stream.Stream
// - java.util.stream.IntStream
// - java.util.stream.LongStream
// - java.util.stream.DoubleStream
	 IntStream.of(1, 2, 3).map(x -> x + 1).forEach(System.out::println);


// INTERMEDIATE OPERATIONS
// - map (mapToInt/mapToLong/mapToDouble/mapToObj to change type of stream, for performance)
// - filter
// - flatMap(flatMapToInt, flatMapToLong, flatMapToDouble) - get stream from stream of streams
// - mapMulti - type of flatMap
// - distinct - removes repeated elements from stream
	 Stream.of(1, 1, 2, 3, 2).distinct().forEach(System.out::println);
// - sorted - sort stream
	 Stream.of(3, 2, 1, 3, 2).sorted().forEach(System.out::println);
// - limit - limit elements in stream
	 Stream.of(3, 2, 1, 3, 2).limit(3).forEach(System.out::println); // 3 2 1
// - skip - skip several elements
	 Stream.of(3, 2, 1, 3, 2).skip(2).forEach(System.out::println); // 1 3 2
// - peek - peek elements, need for debug, you should pass a function
	 Stream.of(3, 2, 1, 3, 2).peek(System.out::println).forEach(System.out::println);
// - takeWhile - take elements while the next element satisfy the condition
	 Arrays.stream(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
		.takeWhile(x -> x < 5)
		.forEach(System.out::println); // 0 1 2 3 4
// - dropWhile - do not take elements while it satisfy the condition
	 Arrays.stream(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 })
		 .dropWhile(x -> x < 5)
		 .forEach(System.out::println); // 5 6 7 8 9
// - boxed, asLongStream/asDoubleStream - convert to another type of stream
//   (boxed - to object stream)


// TERMINAL OPERATIONS
// It creates terninal(final) object from stream.
// - count - elements in a stream
	 long count = Stream.of(1, 2, 3, 4, 5).count(); // 5
// - findFirst / findAny - returns first / any element of the stream in an Optional
//   If stream is empty, returns empty Optional. findAny has better performans in parallel streams
	 int value = Stream.of(1, 2, 3, 4, 5).findAny().get();
	 int value = Stream.of(1, 2, 3, 4, 5).findFirst().get();
// - anyMatch / noneMatch / allMatch - is any / none / all elements satisfy the
//   condition
	 boolean res1 = Stream.of(1, 2, 3, 4, 5).anyMatch(x -> x == 2);
	 System.out.println(res1); // true
	 boolean res2 = Stream.of(1, 2, 3, 4, 5).noneMatch(x -> x == 2);
	 System.out.println(res2); // false
	 boolean res3 = Stream.of(1, 2, 3, 4, 5).allMatch(x -> x == 2);
	 System.out.println(res3); // false
// - forEach, forEachOrdered - execute passed function on every element
//   for parallel stream forEach executes function in parallel order,
//   forEachOrdered in sequence order
//   Because of optimizations stream work bad with external effect
//   so better don't use forEach that affects external data.
	 Stream.of(1, 2, 3, 4, 5).forEach(System.out::println);
	 Stream.of(1, 2, 3, 4, 5).forEachOrdered(System.out::println);
// - min, max - returns min / max element of the stream, returns an optional
	 int res = Stream.of(1, 2, 3, 4, 5).min(Integer::compare).get(); // 1
	 res = Stream.of(1, 2, 3, 4, 5).max(Integer::compare).get(); // 5
// - reduce - with reduce we can calculate value based on all elements in the stream
//            we should pass start value and function that accepts current result and current value
	 public static BigInteger factorial(int n) {
		 BigInteger res = IntStream.rangeClosed(1, n)
		 		 .mapToObj(BigInteger::valueOf)
		 		 .reduce(BigInteger.ONE, BigInteger::multiply);
		 return res;
	 }
// - collect - WILL LOOK LATER
// - toArray, toList - converts stream to array / list
	 int[] arr = Stream.of(1, 2, 3, 4, 5).mapToInt(x -> x).toArray();
	 List<Integer> list = Stream.of(1, 2, 3, 4, 5).toList();
// - sum, average(for primitive streams) - returns sum / average of the elements
	 long sum = Stream.of(1, 2, 3, 4, 5).mapToInt(x -> x).sum();
	 double average = Stream.of(1, 2, 3, 4, 5).mapToInt(x -> x).average().getAsDouble();
// - summaryStatistics - returns an object with min, max, sum and count
	 IntSummaryStatistics statistics = Stream.of(1, 2, 3, 4, 5).mapToInt(x -> x).summaryStatistics();
	 System.out.println(statistics.getMin());
	 System.out.println(statistics.getMax());
	 System.out.println(statistics.getAverage());
	 System.out.println(statistics.getCount());


// Examples
// Example, how many element start with "--"
long count = Stream.of("--a", "--b", "f", "--c")
	.filter(x -> x.startsWith("--"))
	.count();
	
// peek doesn't change elements of the stream, so sometime it is not invoked
// We know how many elements in the stream, so we do not invoke peek, it's an optimization
long count = Stream.of(1, 2, 3, 4, 5)
	.peek(System.out::println)
	.count();
// The same thing, peek won't be invoked
long count = Stream.of(1, 2, 3, 4, 5)
	.skip(1)
	.peek(System.out::println)
	.count();

// Example, find first element with the specified prefix
Stream.of("--a", "--b", "c", "--d")
	.filter(x -> x.startsWith(PREFIX))
	.findFirst()
	.map(x -> x.substring(PREFIX.length()))
	.ifPresentOrElse(x -> System.out.println("Element: " + x),
					() -> System.out.println("There is no such element"));

// To check if stream has a suitable element better to use anyMatch instead of 
// filter + findFirst + ifPresent
boolean hasElement = Stream.of("--a", "--b", "c", "--d")
								.anyMatch(x -> x.startsWith(PREFIX));
								
// SHORT CIRCUIT
// stream not always goes through all element if it calculates the result
// but we should not rely on it
List<List<String>> list = List.of(
		List.of("foo", "bar", "baz"),
		List.of("Java", "Kotlin", "Groovy"),
		List.of("Hello", "Goodbye")
);
boolean res = list.stream()
				.flatMap(List::stream) // function maps element(inner list) to stream
				.peek(System.out::println) // invokes only
				.anyMatch("Java"::equals);
System.out.println(res); // foo bar baz Java


// COLLECTORS
// - Collector is a receipt for terminal operation(to create a final result from stream)
// - Predefined collectors are in class Collectors
// - You can combine collectors
// - You can write your own collector from scratch.

// Predifined collectors
// - toList, toSet
	 List<Integer> res = Stream.of(1, 2, 3).collect(Collectors.toList());
	 Set<Integer> res = Stream.of(1, 2, 3, 3).collect(Collectors.toSet());
// - toCollection. You should specify what collection create with this collector
//   You should pass supplier - creator of the collection
	 List<Integer> res = Stream.of(1, 2, 3, 3)
		 .collect(Collectors.toCollection(ArrayList<Integer>::new));
// The same
	 List<Integer> res = Stream.of(1, 2, 3, 3)
		.collect(Collectors.toCollection(() -> new ArrayList<>()));
// - toMap - create map based on collection. You should pass function that return key by collection
//   element and function that returns value by collection element
//   You can pass merger to merge value of elements with the same key and supplier to specify map type
	 Map<Integer, Integer> res = Stream.of(1, 2, 3)
		 .collect(Collectors.toMap(x -> x, x -> x));
	 System.out.println(res); // { 1: 1, 2: 2, 3: 3 }
// - joining - collector to join string. You can pass separator, prefix, suffix
	 String res = Stream.of("a", "b", "c").collect(Collectors.joining()); // "abc"
// - groupingBy - group element by feature. You should pass a function that accept element
//   and returns feature
	 Map<Integer, List<String>> res = List.of("a", "b", "c", "aa", "bb", "ccc").stream()
         .collect(Collectors.groupingBy(String::length)); // {1=[a, b, c], 2=[aa, bb], 3=[ccc]}
//   You can merge values of one feature by passing collector to join as a second parameter
	 Map<Integer, String> res = List.of("a", "b", "c", "aa", "bb", "ccc").stream()
         .collect(Collectors.groupingBy(String::length, Collectors.joining("$")));
// {1=a$b$c, 2=aa$bb, 3=ccc}						
// - partitioningBy - it's a special case of groupingBy. It create to groups(true and false) by predicate
//   You should pass a predicate and optinally downstream.
	 Map<Boolean, List<Integer>> res = Stream.of(1, 11, 3, 20)
		.collect(Collectors.partitioningBy(x -> x < 10)); // {false=[11, 20], true=[1, 3]}
// - collectingAndThen - you can specify an action to do with result
//   You should pass downstream and finisher(action)
	 int res = Stream.of(1, 2, 3, 4) // minBy returns an Optional, so we want to unpack it
		.collect(Collectors.collectingAndThen(
			Collectors.minBy(Integer::compare), x -> x.get() // downstream and finisher
	 ));
// - teeing - you can combine results of two collectors togeter.
//   You should pass two collectors to combine and finisher that combines results of collectors
//   See example later   

// DOWNSTREAM COLLECTROS
// We usually use this collectors as downstream collectors in combination with another collectors
// - reducing/counting/minBy/maxBy - the same as terminal operations
// - mapping, flatMapping(look later) - We can use mapping to convert stream items. You should pass
//   second parameter - downstream collector, to specify how to convert new stream into a value.
	 List<String> res = Stream.of("a", "b", "c", "aa", "ab")	
		 .collect(Collectors.mapping(x -> x + "$", Collectors.toList())); // [a$, b$, c$, aa$, ab$]
	 long res = Stream.of("a", "b", "c").collect(Collectors.counting());
// - averagingInt/averagingLong/averagingDouble - the same as terminal operation
//   You should pass mapper to Int / Long / Double value
	 double res = Stream.of(1, 2, 4).collect(Collectors.averagingInt(x -> x));
// - summingInt/summingLong/summingDouble - the same as terminal operation
//   You should pass mapper to Int / Long / Double value
	 double res = Stream.of(1, 2, 4).collect(Collectors.summingInt(x -> x));


// GROUPINGBY
// You can group elements by feature, with groupingBy. You should pass
// classifier as a parameter
Map<Integer, List<String>> res = List.of("a", "b", "c", "aa", "bb", "ccc").stream()
                .collect(Collectors.groupingBy(String::length)); // {1=[a, b, c], 2=[aa, bb], 3=[ccc]}
				
// When we grouped elements, we have a stream that corresponds for every key.
// We should turn it into value with another collector(downstream collector). By default is used
// Collectors.toList() but we can specify it as a second paramenter.
// For example, for string use joining to join strings in a group into one string.
Map<Integer, String> res = Stream.of("a", "b", "c", "aa", "ab")
	.collect(Collectors.groupingBy(String::length,
			Collectors.joining("$"))); // {1=a$b$c, 2=aa$ab}

// You can use Collectors.mapping to convert groupped elements
Map<Integer, List<String>> res = Stream.of("a", "b", "c", "aa", "ab")
	.collect(Collectors.groupingBy(String::length,
			Collectors.mapping(s -> s + "$", Collectors.toList())));

// Or you can group groupped elements one more time
// Group string by length and the by first character
Map<Integer, Map<Character, List<String>>> res = Stream.of("a", "b", "c", "aa", "ab", "b", "bb")
	.collect(Collectors.groupingBy(String::length,
			Collectors.groupingBy(s -> s.charAt(0))));
			

// FLATMAPPING
// If in mapping we provide a stream we can union all streams with flatMapping collector
// We should pass mapping function that provide a stream by item and 
// downstream collector to convert result stream into a value
class Person {
    String name;
    int age;
    List<Integer> items;

    public String name() {
        return this.name;
    }
    public int age() {
        return this.age;
    }
    public Stream items() {
        return this.items.stream();
    }

    public Person(String name, int age, List<Integer> items) {
        this.name = name;
        this.age = age;
        this.items = items;
    }
}
List<Person> data = new ArrayList<>();
data.add(new Person("Alice", 20, Arrays.asList(1, 2)));
data.add(new Person("Alice", 20, Arrays.asList(5, 6)));
data.add(new Person("Bob", 20, Arrays.asList(2, 3)));

Map<String, Integer> res =  (Map<String, Integer>)data.stream()
	.collect(Collectors.groupingBy(Person::name,
			Collectors.flatMapping(Person::items, Collectors.summingInt(x -> (int)x))));
// {Bob=5, Alice=14}
// We have intermediate stream in after flatMapping Alice -{ 1, 2, 5, 6 } 
// and Bob - { 2, 3 }

// COLLECTINGANDTHEN
// you can specify an action to do with result
// You should pass downstream and finisher(action)
int res = Stream.of(1, 2, 3, 4) // minBy returns an Optional, so we want to unpack it
.collect(Collectors.collectingAndThen(
	Collectors.minBy(Integer::compare), x -> x.get() // downstream and finisher
));
// With grouping, unpack Optional
List<Person> data = new ArrayList<>();
data.add(new Person("Alice", 10, Arrays.asList(1, 2)));
data.add(new Person("Alice", 20, Arrays.asList(5, 6)));
data.add(new Person("Bob", 10, Arrays.asList(2, 3)));

var res = data.stream()
	.collect(Collectors.groupingBy(Person::name,
			Collectors.collectingAndThen(
				Collectors.mapping(
					Person::age,
					Collectors.minBy(Comparator.naturalOrder())
				), Optional::get
			)
		)
	);



// CUSTOM COLLECTORS
// The collector interface consists of 4 functions
public interface Collector<T, A, R> {
	// T element type, A accumulator type, R result type
	// Create accumulator
	Supplier<A> supplier();
	// Add to accumulator
	BiConsumer<A, T> accumulator();
	// Merge two acculators, for parallel streams
	BinaryOperator<A> combiner();
	// Convert accumulator into result
	Function<A, R> finisher();
	
	// Flags (for special cases)
	Set<Characteristics> characteristics();
}

// Usually, we don't have to implement this interface. We can use to static methods
// of the Collector interface to create a collector
// Special case, accumulator is a result
public static <T, R> Collector<T, R, R> of(Supplier<R> supplier,
										   BiConsumer<R, T> accumulator
										   BinaryOperator<R> combiner, 
										   Characteristics... characteristics)
public static <T, A, R> Collector<T, A, R> of(Supplier<A> supplier,
                                              BiConsumer<A, T> accumulator,
											  BinaryOperator<A> combiner,
											  Function<A, R> finisher,
											  Characterisitcs... characteristics)									   
// Own toList collector
List<Integer> res = Stream.of(1, 2, 3, 4).collect(toList());
System.out.println(res);

static <T> Collector<T, List<T>, List<T>> toList() {
	return Collector.of(ArrayList::new, List::add,
			(a, b) -> { // we can create an new list or change and return one of the passed
				a.addAll(b);
				return a;
	});
}							 
// Note, it's better don't show the type of accumulator to users
// So, we should write
static <T> Collector<T, ?, List<T>> toList() {
	
// Let's write a collector that calculates min and max value together
// We should pass a comparator and finisher that merges min and max values together
// We will return an optional in case our stream has no elements.
static <T, R> Collector<T, ?, Optional<R>> minMax(Comparator<? super T> cmp,
                                            BiFunction<? super T, ? super T, ? extends R> finisher) {
	class Acc {
		T min;
		T max;
		boolean present;

		public void add(T t) {
			if(present) {
				if(cmp.compare(t, min) < 0) min = t;
				if(cmp.compare(t, max) > 0) max = t;
			} else {
				min = max = t;
				present = true;
			}
		}
		public Acc merge(Acc that) {
			if(!that.present) return this;
			if(!this.present) return that;
			if(cmp.compare(that.min, min) < 0) min = that.min;
			if(cmp.compare(that.max, max) > 0) max = that.max;
			return this;
		}
	}

	return Collector.of(Acc::new, Acc::add, Acc::merge,
			acc -> acc.present ? Optional.of(finisher.apply(acc.min, acc.max)) : Optional.empty());
}

Optional<String> res = Stream.of("a", "b", "cc", "ddd")
                        .collect(minMax(Comparator.comparingInt(String::length), (min, max) -> min + "$" + max));
						
// But you can solve this problem with Collectors.teeing
static <T, R> Collector<T, ?, Optional<R>> minMax(Comparator<? super T> cmp,
	BiFunction<? super T, ? super T, ? extends R> finisher) {
	return Collectors.teeing(Collectors.minBy(cmp), Collectors.maxBy(cmp),
			(a, b) -> a.map(v -> finisher.apply(v, b.get())));
}