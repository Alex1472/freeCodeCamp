// There are for base entities(abstract classes) for input out in java:
// Reader and writer more highlevel classes, they convert character into bytes 
// with charset 
// - InputStream - read bytes
// - OutputStream - write bytes
// - Reader - read symbols
// - Writer - write symbols

// INPUTSTREAM METHODS
// All this method block the thread, so they wait until can get bytes.
// Note, the second and the third methods wait only one time.
// If they get any number of bytes, they will return them, and do not
// wait even though they don't get required number of bytes.
byte[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }; // this array is used as input stream
ByteArrayInputStream input = new ByteArrayInputStream(data);
// - int read() -> -1 or 0..255, return -1 if can't read next bytes
	 input.read(); // 1
// - int read(byte[] b) -> read byte array. Returns quantity of bytes that were read
     byte[] res = new byte[5];
	 input.read(res); // res = { 1, 2, 3, 4, 5 }
// - int read(byte[] b, int offset, int length) -> read into b, starts with
//   offset, length byte. Returns quantity of bytes that were read
     byte[] res = new byte[5];
     input.read(res, 2, 3); // [0, 0, 1, 2, 3]
// - int available() - min available bytes you can read without blocking
//   (it is not nessasery method, it can return always 0)
     System.out.println(input.available()); // 10
// - long skip(long n) - skip n bytes in a stream. Returns how many bytes was skipped.
	 byte[] res = new byte[5];
     input.skip(2);
     input.read(res); // res = { 3, 4, 5, 6, 7 }
     System.out.println(Arrays.toString(res));	 
// - void mark(int readlimit) - mark place in a stream, pass max bytes 
//   you will read before reset
// - void reset() - return to place that was marked with the mark method
// - boolean markSupported() - is mark functionality available
     System.out.println(input.markSupported()); // true
 	 input.read(); // 1
	 input.read(); // 2
	 input.mark(10);
	 byte[] res = new byte[5];
	 input.read(res); // { 3, 4, 5, 6, 7 }
	 input.reset();
	 System.out.println(input.read()); // 3

// Example, to read exactly n bytes, you can with this method
static void readAll(InputStream stream, byte[] data) throws IOException {
	int offset = 0;
	while(offset < data.length) {
		int count = stream.read(data, offset, data.length - offset);
		if(count == -1) {
			throw new IOException("Stream has less then " + data.length + " bytes");
		}
		offset += count;
	}
}
byte[] data = new byte[10];
readAll(System.in, data);

// MORE ADVANCED INPUT STREAM METHODS
// This method will wait in case of blocking
byte[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }; // this array is used as input stream
ByteArrayInputStream input = new ByteArrayInputStream(data);
// - byte[] readAllBytes() - read all bytes from stream
     byte[] res = input.readAllBytes(); // res = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }
// - byte[] readNBytes(int len) - read len bytes from stream
     byte[] res = input.readNBytes(3); // res = { 1, 2, 3 }
// - int readNBytes(byte[] b, int offset, int length) - read into b, 
//   start with offset, length bytes. Returns how many bytes it has read.
     byte[] res = new byte[5];
     input.readNBytes(res, 2, 3); // res = { 0, 0, 1, 2, 3 }
// - void skipNBytes(long n) - skip n bytes in the stream
     input.skipNBytes(5);
     System.out.println(input.read()); // 6
// - long transferTo(OutputStream out) - write all current stream into output stream
     ByteArrayOutputStream output = new ByteArrayOutputStream(10);
     input.transferTo(output);
     System.out.println(Arrays.toString(output.toByteArray())); // { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }
// - static InputStream nullInputStream() - returns empty stream
 	 
	 
	 
// INPUT STREAM IMPLEMENTATIONS
// - FileInputStram - from file
// - ByteArrayInputStream - from array
// - BufferedInputStream - create a buffer on java level when reads data.
//   It reads data in chuncks. You should pass another stream into it.
// - DataInputStream - has additional method: readShort(), readInt(), readChart() ...
//   in additional to InputStream methods
// - PipedInputStream - you can move byte from one stream to another with PipedInputStream
//   when the streams are in different threads
//   In has bad implementation, don't use it.
// - ZipFile.getInputStream(ZipEntry) - read from zip-file
// - Process.getInputStream() - get standard out of the process
// - URL.openStream() - read by URL(http/https/file/ftp/...)

// Http client for poor people. Get google main page
InputStream input = new URL("https://google.com").openStream();
System.out.println(new String(input.readAllBytes(), StandardCharsets.UTF_8));

// With HttpClient
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
	.uri(URI.create("https://www.google.com/"))
	.build();
client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
	.thenApply(HttpResponse::body)
	.thenAccept(System.out::println)
	.join();
// Note, better always use URI instead of URL. Url has several 
// nasty peculiarities



// EXTERNAL RESOURCE HANDLING
// When external resource(file for example) open, OS has some data structures
// to handle it(no where we in a file). So you should release resources
// Another reason to do it, is that java can write file when you close it, not earlier.
// Note, garbage collector release reasources(closes file). So if you filestream
// will be collected, it will be closed.

// You can close the stream with method CLOSE()
// Example, read all data from file and close with method
String path = System.getProperty("user.dir") + "\\src\\test.txt";
InputStream input = new FileInputStream(path);
System.out.println(Arrays.toString(input.readAllBytes()));
input.close();

// But better to use TRY WITH RESOURCES to automatically calls the close method.
// We can create here objects that implement the closable interface. 
// The close method will be called automatically.
try(InputStream input = new FileInputStream(path)) {
	System.out.println(Arrays.toString(input.readAllBytes()));
} 


// In case we want to invoke some method when garbage collector what to collect
// our object there are two ways:
// Old way - method finalize, but it is deprecated for removal. We won't discuss it.

// New way - use Cleaner. 
// - Usually you create cleaner one on your program.
// One reason to create more cleaner is that cleaner frees objects in a one
// separated thread in a queue consequently. So if a free operation is hard
// free operations for another objects will ways. In this case you can create
// another cleaners.
// - We should register an object in cleaner with the method register and
//   pass the object and a method to free resources. This method will
//   be invoke when gc will has collected the object
// - Note the free method should be static, in other case we will capture the
//   object and in won't be collected. Also we shouldn't pass any field this the method
// - We should write a wrapper on resouce identifier, with witch we can say
//   what resource to release.
class MyFile {
    static final Cleaner CLEANER = Cleaner.create(); // Create cleaner
    private final int fd;

    public MyFile(int _fd) {
        fd = _fd;
        CLEANER.register(this, () -> free(_fd)); // registering to cleaner
		                                         // we shouldn't use this.fd
												 // in this case gc can't collect the object
    }
    public void read() {
        System.out.println("Reading...");
    }
    static void free(int fd) { // should be static to collect the object
        System.out.println("Freeing " + fd);
    }
} 
static void read() {
	MyFile file = new MyFile(1);
	file.read();
}
read();
System.out.println("GC");
System.gc(); 
System.out.println("Sleeping...");
Thread.sleep(2000);
System.out.println("Done");
// Reading...
// GC
// Sleeping...
// Freeing 1
// Done


// Also with cleaner we can release resources manyally or with try with resources
// The cleaner register method returns special cleanable object.
// You can call its method clean to invoke you registered method to 
// release resources. In this case this method won't be invoked second time
// when gc will collect the object.
class MyFile implements AutoCloseable {
    static final Cleaner CLEANER = Cleaner.create();
    private final int fd;
    private final Cleaner.Cleanable cleanable;

    public MyFile(int _fd) {
        fd = _fd;
        cleanable = CLEANER.register(this, () -> free(_fd)); // returns cleanable
         CLEANER.register(this, () -> free(_fd));

    }

    public void read() {
        System.out.println("Reading...");
    }
    static void free(int fd) {
        System.out.println("Freeing " + fd);
    }
    @Override
    public void close() {
        cleanable.clean(); // call the clean method to release the resources
    }
}
try(MyFile file = new MyFile(1)) {
	file.read();
}
// Reading...
// Freeing 1



// OUTPUTSTREAM
// Methods:
ByteArrayOutputStream output = new ByteArrayOutputStream();
// - void write(int b) - writes low 8 bytes
output.write(1); // [1]
// - void write(byte[] b) - writes all array, if can't throws exception 
byte[] data = {1, 2, 3};
output.write(data); // [1, 2, 3]
// - void write(byte[] b, int offset, int length) - write part of the array
byte[] data = {1, 2, 3, 4, 5};
output.write(data, 2, 2); // [3, 4]
// - flush() - reset all caches and write into destination
// - close()
// static OutputStream nullOutputStream() - you always can write into it. It ignores it all

// IMPLEMENTATIONS
// - FileOutputStream - write into file
// - ByteArrayOutputStream - write into array. Often used to write in memory
// - BufferedOutputStream - write by chunks into destination 
// - PrintStream - for printing convinience has many additional method,
//   for example println. System.out has this type
// - DataOutputStream - pair for DataInputStream
// - PipedOutputStream - pair for PipedInputStream, all bad (to write from
//   one stream to another in different threads)
// - Process.getOutputStream() - standard input for process
// - URL.openConnection().getOutputStream() - write into URL, (HTTP POST)

// Example:
ByteArrayOutputStream output = new ByteArrayOutputStream();
output.write(1);
output.write(new byte[] { 3, 4 });
System.out.println(Arrays.toString(output.toByteArray())); // [1, 3, 4]

// Note, there is a problem with write(byte[] b) for ByteArrayOutputStream
// It is declared that OutputStream.write(byte[] b) can throw IOException
// But in ByteArrayOutputStream the method wasn't override to say that it
// doesn't throw the exception (as was done for write(int b) and write(byte[] b, int offset, int length))
// And we can't fix it because it will be a breaking change, in case someone
// catches this exception. If we remove the declaration that method can throw
// it, this code won't compile
// So it is declared that write(byte[] b) can throw IOException, but tecnically it couldn't
// Because of this the method writeBytes(byte[] b) was added. It equivalent
// write(byte[] b) only without exception
output.writeBytes(new byte[] { 3, 4 }); // [3, 4]

// Also ByteArrayOutputStream has methods
// - toByteArray() - return result byte array
// - size() - size of bytes was written
// - toString(Charset) - converts to string



// READER
// Reader has similar methods as InputStreams, only works with charactors 
// instead of bytes
// Methods:
// - int read() -> -1 .. or 0..0xFFFF, 16 low bytes in a char
// - int read(chart[] cb) -> int - reads data in char[]. Returns characters
//   quantity was read
// - int read(char[] cb, int offset, int length) -> Reads length characters
//   into cb starts with offset. Returns characters quantity was read
// - long skip(long n) - skips n characters. Returns charcters quantity was skipped.
//   It works rather slow, because we should calculate shift, but characters
//   need different bytes count to code.
// - boolean ready() - returns how many characters you can read without blocking
// - void mark(int readlimit) - you can mark place in a stream and then return to it with reset
// - void reset() - return to marked place
// - markSupported() - is mark functionality supported
// - long transferTo(Writer out) - read all stream into writer
// - static Reader nullReader() - returns empty reader

// Example:
StringReader reader = new StringReader("test");
char[] res = new char[3];
reader.mark(10);
System.out.println(reader.read()); // "t"
System.out.println(reader.read()); // "e"
reader.reset();
reader.read(res); 
System.out.println(Arrays.toString(res)); // "tes"


// READERS IMPLEMENTATIONS
// - InputStreamReader - wrapper on InputStream. You can pass charset
	 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {1, 2, 3});
     InputStreamReader reader = new InputStreamReader(inputStream);
     System.out.println(reader.read()); // 1
     System.out.println(reader.read()); // 2
     System.out.println(reader.read()); // 3
// - FileReader - to read file
     FileReader fileReader = new FileReader(System.getProperty("user.dir") + "\\src\\test.txt");
	 char[] res = new char[4];
	 fileReader.read(res);
// - StringReader - to read from string
     StringReader reader = new StringReader("ajjjvv");
     System.out.println(reader.read());
// - BufferedReader - add buffered reading from another reader
     StringReader reader = new StringReader("ajjjvv");
     BufferedReader bufferedReader = new BufferedReader(reader);
     System.out.println(bufferedReader.read());
//   It has method read readLine and readLines to read until next \n
     StringReader reader = new StringReader("ajjjvv\ndddd");
	 BufferedReader bufferedReader = new BufferedReader(reader);
	 System.out.println(bufferedReader.readLine()); // ajjjvv
     System.out.println(bufferedReader.readLine()); // dddd
	 // or
	 System.out.println(Arrays.toString(bufferedReader.lines().toArray()));	// [ ajjjvv, dddd ]
	 
	 
	 
// WRITER
// It works on a char level
// It has similar method to OutputStream
// - void write(int b) - writes 16 low bytes
// - void write(char[] cb)
// - void write(char[] cb, int offset, int length)
// - void write(String str)
// - void write(String str, int offset, int length)
// - void flush()
// - void close()
// - static Write nullWriter()

// Example:
StringWriter writer = new StringWriter();
writer.write("aaa");
writer.write(new char[] { 's', 't' });
System.out.println(writer.toString());


// WRITER IMPLEMENTATIONS
// - OutputStreamWriter (OutputStream + charset) - writes characters to OutputStream
// - FileWriter - write to file
// - StringWriter - write to string
// - BufferedWriter - adds buffer to writing



// WORKING WITH FILES
// JAVA.IO.FILE API
// - The class java.io.File represents path to file and file itself. It can be folder
// - Can be absolute or relative, relative to current catalog of java-process
//   Catalog where the java process was run

// Constructors (better use second constructor, that automatically specify separator)
File file1 = new File("C:\\folder\\text.txt");
File file2 = new File("C:\\folder", "text.txt");

// Methods:
// - getName()/getPath()/getParent() returns parent path/getParentFile() - returns File
//   these methods do not work with file system, we don't if this file exists
// - getAbsoluteFile() - returns file / getAbsolutePath() - turns relative path to absolute
//   these methods do not work with file system, we don't if this file exists 
// - getCanonicalFile() / getCanonicalPath()
// - exists()/canRead()/canWrite()/canExecute()/length() - works with access modifiers
// - isDirectory()/isFile()/isHidden()
// - createNewFile() / mkdir() - creates new dir if parant dir exists / 
//   mkdirs()- can create subfolders
// - renameTo() / delete()
// - list([filter]) - returns catalog contents as list of string / 
//   listFiles([filter]) - returns catalog contents as list of Files
//   You can filter contents with filter

// The problem that this method bad handle errors, they just return true(all right) 
// or false(there was an error)



// JAVA.NIO.FILE
// java.nio.file.Path - repsents just path. It is not work with file system.
// You can work with virtual paths (in zip-file) as in is usual paths.
// You can create path with static methods of classes Path and Paths
Path path1 = Paths.get("/ab/cd"); // /ab/cd
Path path2 = Paths.get("ab", "cd"); // ab/cd

Path path3 = Path.of("/ab/cd"); // /ab/cd
Path path4 = Path.of("ab", "cd"); // ab/cd`
// Methods:
Path path1 = Paths.get("C://ab/cd"); // cd
// - getFileName() - returns file name. Returns relative path
System.out.println(path1.getFileName());
// - getParent() - get parent path
System.out.println(path1.getParent()); // C:\ab, returns path
// - getRoot() - get first element in path. Returns path 
System.out.println(path1.getRoot()); // C:\
// - resolve(Path, String) - create new path, where passed path(relateve) goes after object path
Path path1 = Paths.get("C://ab/cd");
Path path2 = Paths.get("test.txt");
Path res = path1.resolve(path2); // C:\ab\cd\test.txt
// - resolveSibling(Path, string) - goes on one directory up
Path res = path1.resolveSibling(path2); // C:\ab\text.txt
// - isAbsolute() - is path absolute
Path path1 = Paths.get("C://ab/cd");
System.out.println(path1.isAbsolute()); // true
Path path2 = Paths.get("test.txt");
System.out.println(path2.isAbsolute()); // false
// - toAbsolutePath() - converts to absolute path, relative to the current folder
Path path2 = Paths.get("test.txt");
System.out.println(path2.toAbsolutePath()); // D:\DifferentReps\JavaScriptProjects\TestProjects\TestJavaProject\test.txt
// - startsWith(Path/String), endsWith(Path/String)
Path path1 = Path.of("C:\\ab\\cd");
Path path2 = Path.of("C:\\ab");
System.out.println(path1.startsWith(path2)); // true
Path path3 = Path.of("cd");
System.out.println(path1.endsWith(path3)); // true
// - getName(int) - returns n-th path part
Path path1 = Path.of("C:\\ab\\cd");
System.out.println(path1.getName(0)); // ab, returns Path
// - getNameCount() - returns count of name parts
System.out.println(path1.getNameCount());
// - iterator() - returns iterator through path's parts
// - toString() / toFile() / toUri() - converts to string, File, Uri
// - register(WatchService, WatchEvent.Kind) - you can track any actions in 
//   a directory, by registering a watcher

// You can go through path parts
Path path1 = Path.of("C:\\ab\\cd");
for(Path path : path1)
	System.out.println(path); // ab cd



// FILES
// To work with file system use the class Files and its static methods
// - copy, move, delete, deleteIfExists
// - createFile, createDrectory, createDirectories
// - createLink, createSymbolicLink
// - createTimpFile, createTimpDirectory - create a temperary file / directory
// - readAllBytes, readAllLines - read file into byte array or String array
// - write(Path, byte[]/Iterable<String>) - write file
// - lines(Path) -> Stream<String> - get file lines as Stream
// - list(Path) -> Stream<Path> - get directory contents as Stream
// - walk(Path) -> Stream<Path> - get directory contents recursively
// - walkFileTree - more flexible go around directory
// - exists/size/getAttribute/isDirectory/isRegularFile/isSymbolicLink
// - newBufferedReader/newInputStream/newOutputStram/newByteChannel - open file with ...



// SOME OTHER API
// - BiteBuffer/DirectBuffer/MappedByteBuffer - you can map contents of the source 
//   to buffer. You can navigate in buffer, read, write
// - Channel, FileChanner - more low-lever way to work with files/socket/threads and another resouces
// - RandomAccessFile - class that allows you to read file in random order(not only sequentially)
// - Socket/ServerSocket - to low-level work with tcp/ip






