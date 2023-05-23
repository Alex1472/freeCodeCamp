// ITERABLE<E> AND ITERATOR<E> 
// The base interface Iterable<E>
public interface Iterable<E> {
    Iterator<E> iterator(); // returns iterator
    //Some another methods
}
// The base interface Iterator<E>
public interface Iterator<E> {
    boolean hasNext();
	E next();
	default void remove() {
        throw new UnsupportedOperationException("remove");
    }
}

// Example
static void printAll(Iterable<?> iterable) {
	Iterator<?> iterator = iterable.iterator();
	while(iterator.hasNext()) {
		Object value = iterator.next();
		System.out.println(value);
	}
}
// Or with for, it is converted in above on bitecode level
static void printAll(Iterable<?> iterable) {
	for(Object object: iterable) {
		System.out.println(object);
	}
}

// Iterator has method remove to remove current element from iterable
// It can throw UnsupportedOperationException, if collection is unchangable
public static void main(String[] args) throws IOException {
	ArrayList<String> strings = new ArrayList<>() {{
		add("aa"); add(""); add("bb"); add("");
	}};
	removeEmpty(strings); // remove empty strings
}
static void removeEmpty(Iterable<String> strings) {
	Iterator<String> iterator = strings.iterator();
	while(iterator.hasNext()) {
		String string = iterator.next();
		if(string.isEmpty()) iterator.remove();
	}
}

// Example, create collection with n the same elements
static <T> Iterable<T> nCopies(T value, int count) {
	if(count < 0) {
		throw new IllegalArgumentException("Count < " + 0);
	}
	return new Iterable<T>() { // we use anonymous class
		@Override
		public Iterator<T> iterator() { // we use anonymous class
			return new Iterator<T>() {
				int rest = count;

				@Override
				public boolean hasNext() { return rest > 0; }

				@Override
				public T next() {
					if(rest == 0) {
						throw new UnsupportedOperationException();
					}
					rest--;
					return value;
				}
			};
		}
	};
}



// COLLECTION<E>
public interface Collection<E> extends Iterable<E> {
	int size();
	boolean isEmpty();
	boolean contains(Object o); // accepts element of any type
    boolean containsAll(Collection<?> c); // collection elements of any type
    Object[] toArray();
    <T> T[] toArray(T[] a);

	// Methods that modifies collection, if collection readonly
	// they throw UnsupporteOperationException()
    boolean add(E e);
    boolean remove(Object o); // try to remove element of any type
    boolean addAll(Collection<? extends E> c);
    boolean removeAll(Collection<?> c); // try to remove element from collection of any type
    boolean retainAll(Collection<?> c); // save only elements from collection c
    void clear();	
}



// SET<E>
// Set<E> extends Collection<E>
// There is no new methods.
// Collection of different objects.
// To sets are equal if they contain the same elements.
// It's your problem if you change element that is Set
// Example, rangeSet:
public static Set<Integer> rangeSet(int fromInclusive, int toExclusive) {
	return new AbstractSet<Integer>() {
		@Override
		public Iterator<Integer> iterator() {
			return new Iterator<Integer>() {
				int current = fromInclusive;

				@Override
				public boolean hasNext() {
					return current < toExclusive;
				}
				@Override
				public Integer next() {
					if(current == toExclusive) {
						throw new IllegalArgumentException();
					}
					return current++;
				}
			};
		};

		@Override
		public int size() {
			return toExclusive - fromInclusive;
		}
	};
}

// Standard sets
// HashSet - standard changable unordered set
// LinkedHashSet - standard changable ordered set in order of adding
// TreeSet - changable sorted set(you can specify sorting method)
// EnumSet - changable set of enum. You can store several value of the enum
// Collections.emptySet() - unchangable empty set
// Collections.singleton(x) - unchangable set of one element
// Set.of(...) - unchangable unordered set of passed elements(you can passed
// several elements)
// Set.copyOf(...) - unchangable copy
// Collections.unmodifieableSet(set) - unchangable wrapper under the set
// Collections.synchronizedSet(set) - syncronized wrapper under the set
// Collections.checkedSet(set, type) - checkable wrapper


// HashSet
// Uses hashCode and hashTable(array) (table[obj.hashCode() % table.lenngth])
// Uses linked list in case of collisions
// If there are a lot of collisions, uses RB-tree, so the complexity is O(logN)


// TreeSet<E> implements NavigableSet<E>
// You can get first, last elements
// First element that is greater / lower then specified element
// All elements that greater / less then specified
// ...


// LIST<E>
public interface List<E> extends Collection<E> {
	boolean addAll(int index, Collection<? extends E> c);
	void sort(Comparator<? super E> c);
	E get(int index);
	E set(int index, E element);
	void add(int index, E element);
	E remove(int index); // there is remove(Object o) for remove element
	int indexOf(Object o);
	int lastIndexOf(Objectt o);
	ListIterator<E> listIterator(); // extention of iterator, you can go forward,
	                                // backward. Add and remove element
	ListIterator<E> listIterator(int index);
	List<E> subList(int fromIndex, int toIndex); // returns view, not a copy,
	// if you change element in origin list, sublist also has changed
}

// Example, RangeList
public static List<Integer> rangeList(int fromInclusive, int toExclusive) {
	return new AbstractList<>() {
		@Override
		public Integer get(int index) {
			return fromInclusive + index;
		}
		@Override
		public int size() { return toExclusive - fromInclusive; }
	};
}

// Note, many standard methods have to implementations for lists with random
// access and without. To say, that you list has random access add interface
// RandomAccess. There are not any methods in it.
class RangeList extends AbstractList<Integer> implements RandomAccess {
	...
}

// Standard lists
// ArrayList - standard changable list
// Arrays.asList - changable wrapper under list. You can change elements
//                 but not add / remove elements
// Collections.emptyList() - unchangable empty list
// Collections.singletonList(x) - unchangable list with one element
// Collections.nCopies(n, x) - unchangable list with n identical elements
// List.of(a, b, c) - unchangable list with passed elements
// List.copyOf(list) - unchagable copy on list
// Collections.unmodifiableList(list) - unchangable wrapper under list
// Collections.synchronizedList(list) - synchronized wrapper under list
// Collections.checkedList(list, type)



// QUEUE
// Queue<E> extends Collection<E>
// Methods to add, remove and look at first element. First is safe, returns 
// null/false, if can't execute the operation. Second throws exceptions in this case.
// offer/add - to add an element
// poll/remove - to remove an element
// peek/element - to return first element in collection without removing



// DEQUE
// Deque<E> extends Queue<E>
// With deque you can add / remove elements from both ends
// Also first method is safe, second throws exception in case of failure.
// offerFirst/addFirst
// offerLast/addLast
// pollFirst/removeFirst
// pollLast/removeLast
// peekFirst/getFirst
// peekLast/getLast

// Note, Deque<E> extends Queue<E> therefore it has methods add, remove and element
// They manupulate with elements at the end of the deque.
// But highly recommended to use First/Last methods not to get confused 



// STANDARD QUEUE AND DEQUE
// ArrayDeque - use as deque, queue and stack
// PriorityQueue - priority quese

// There are several old collection, it's better don't use them, use more new once.
// Enumeration -> Iterator
// Vector -> ArrayList
// Stack -> ArrayDeque
// Dictionary -> Map
// Hashtable -> HashMap
// LinkedList -> ArrayList / ArrayDeque - there are memory and time overheads compared 
// to ArrayList. Don't use it.



// MAP
// Map doesn't implement Iterable<E> and Collection<E>. It has its own hierachy.
// There are interfaces SortedMap and NavigableMap.
// We usually use NavigableMap, Sorted is used seldom.
// Map is a collection of Entries - pairs key - value
interface Map<K, V> {
	int size();
	boolean isEmpty();
	// containsKey usually works fast O(1) or O(NlogN)
	boolean containsKey(Object key); //Note, Object not K, it wrong solution
	// containsValue usually works slow O(n)
	boolean containsValue(Object value); //Note, Object not K, it wrong solution
	V get(Object key); // return null, there is no this key
	V put(K key, V value); // add new value
	V remove(Object key);
	void putAll(Map<? extends K, ? extends V> m);
	void clear();
	Set<K> keySet(); // all keys, return views
	Collection<V> values(); // all values, values can be not unique, return view
	Set<Map,Entry<K, V>> entrySet(); // all entries key-value, return view
	V getOrDefault(Object key, V value);
	V putIfAbsent(K key, V value); // add pair, if there is no 
	// the key in the map or it is null
	boolean remove(Ojbect key, Object value); // remove on this key-value pair
	boolean replace(K key, V oldValue, V newValue); // replace only specified value
	V replace(K key, V value); // replace any value
}

// Usually entry is a view, so if you will change value, the value in the 
// collection will also be changed`
Interface Entry<K, V> {
	K getKey();
	V getValue();
	V setValue(V value);
}

// Example, if you want go over the map and get key-value pairs, 
// better to use entrySet rather then keySet and get.
// It's because entrySet usually works faster.
public static void iterateOverMap(Map<String, Integer> map) {
	for(Map.Entry<String, Integer> entry: map.entrySet()) {
		System.out.println(entry.getKey() + " -> " + entry.getValue());
	}
}

// Iterate with forEach and BiConsumer interface
public static void iterateOverMap(Map<String, Integer> map) {
	map.forEach(new BiConsumer<String, Integer>() {
		@Override
		public void accept(String key, Integer value) {
			System.out.println(key + " -> " + value);
		}
	});
}
// Iterate with forEach and lambda
// It's the best way, because we should not create iterator
public static void iterateOverMap(Map<String, Integer> map) {
	map.forEach((key, value) -> System.out.println(key + " -> " + value));
}

// Let's implement map with pairs, number and string representation of number
// We will use anonymous type, and should just implement the method entrySet,
// that returns an entrySet
static Map<Integer, String> numberToString(int count) {
	return new AbstractMap<>() {
		@Override
		public Set<Entry<Integer, String>> entrySet() {
			return new AbstractSet<>() {
				@Override
				public Iterator<Entry<Integer, String>> iterator() {
					return new Iterator<>() {
						int next = 0;
						@Override
						public Entry<Integer, String> next() {
							return new SimpleImmutableEntry<>(next, String.valueOf(next++));
						}

						@Override
						public boolean hasNext() {
							return next < count;
						}
					};
				}

				@Override
				public int size() {
					return count;
				}
			};
		}
	};
}


// MODIFICATION
// With keySet, it's the slowest way
public static void trimAllValues(Map<String, String> map) {
	for(String key: map.keySet()) {
		String value = map.get(key);
		map.put(key, value.trim());
	}
}
// With entries 
public static void trimAllValues(Map<String, String> map) {
	for(Map.Entry<String, String> entry: map.entrySet()) {
		String value = entry.getValue();
		entry.setValue(value.trim());
	}
}
// With replaceAll, it's a faster way
public static void trimAllValues(Map<String, String> map) {
	map.replaceAll((key, value) -> value.trim());
}

// REMOVING
// With keys
public static void removeUnwantedValues(Map<String, String> map) {
	for(String key: map.keySet()) {
		String value = map.get(key);
		if(value.equals("aaa") || value.equals("bbb")) {
			map.remove(key);
		}
	}
}
// With entries
public static void removeUnwantedValues(Map<String, String> map) {
	Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
	while(iterator.hasNext()) {
		Map.Entry<String, String> entry = iterator.next();
		if(entry.getValue() == "aaa" || entry.getValue() == "bbb") {
			iterator.remove();
		}
	}
}
// With REMOVEIF method, every collection has it.
public static void removeUnwantedValues(Map<String, String> map) {
	map.entrySet().removeIf(entry -> entry.getValue().equals("aaa") || entry.getValue().equals("bbb"));
}
// With values, if you remove value => you remove all entry
public static void removeUnwantedValues(Map<String, String> map) {
	map.values().removeIf(value -> value.equals("aaa") || value.equals("bbb"));
}
// With remove all method. If you add value for value view, you remove 
// all entry
public static void removeUnwantedValues(Map<String, String> map) {
	List<String> unwantedValues = Arrays.asList("aaa", "bbb");
	map.values().removeAll(unwantedValues);
}



// MULTIMAP
// Sometimes, you want to store several value that correspont one key
// It is named multimap
static Map<String, List<String>> map = new HashMap<>();
public static void add(String key, String value) {
	List<String> list = map.get(key);
	if(list == null) {
		list = new ArrayList<>();
		map.put(key, list);
	}
	list.add(value);
}
// You can use COMPUTEIFABSENT to add value, if key is not present. It
// returns value(created or not)
public static void add(String key, String value) {
	map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
}



// MULTI-SET/BAG
// We want to count element occurances
static Map<String, Integer> counts = new HashMap<>();
public static void add(String key) {
	Integer count = counts.get(key);
	if(count == null) {
		count = 0;
	}
	counts.put(key, count + 1);
}
// We can use the method MERGE, it accepts key, value and function that
// return new value if there is oldValue. It accepts oldValue and passed value
public static void add(String key) {
	counts.merge(key, 1, (a, b) -> a + b);
}



// STANDARD MAPS
// HashMap - unordered changable standard map
// LinkedHashMap - ordered changable standard map. It stores elements in order
// of thier addition to the map
// TreeMap - sorted changable Map
// EnumMap - a map, which key are elements of the enum
// IdentityHashMap - map, which keys are compared with ==, not equals
// Collections.singletonMap(k, v) - unchangable map with one element
// Collections.emptyMap() - unchangable empty map
// Map.of(k1, v1, k2, v2, ...); Map.ofEntries(Map.entry(k1, v1), ...) -
// unchangable map
// Collections.unmodifiableMap, synchronizedMap, checkedMap - wrappers



// NAVIGABLEMAP
// NavigableMap<K, V> extends SortedMap<K, V>
// For example, you can find greatest key/entry, that lower then specified 
// get view that started from specified key, revert map(descending map)
// - lowerKey, lowerEntry, floorKey, floorEntry
// - higerKey, higherEntry, ceilingKey, ceilingEntry
// - firstKey, firstEntry, lastKey, lastEntry
// - pollFirstEntry, pollLastEntry
// - descendingMap, navigableKeySet, descendingKeySet
// - subMap, headMap, tailMap



// CONCURRENTMODIFICATIONEXCEPTION
// - Is thrown if collection has noticed that it is changed, when you can'table
//   do this
// - Usually while iteration with iterator or with forEach
// - You can modify a collection while iterating only with with iterator. 
 


