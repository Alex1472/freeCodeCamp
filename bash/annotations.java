// ANNOTATIONS
// Annotation is a metadata about an element in your program.
// It used by
// - Compiler (@Override, @SuppressWarnings, @FunctionalInterface)
// - IDE (@Nullable, @NotNull, #Contract). For example for static analisis.
// IDE can get warnings while analyzing annotations.
// - Annotation processor (Lombook - @Getter, @EqualAndHashCode). It is 
// compiler plugin, they can transform(generate) code for example.
// - Bytecode analyzer (FindBugs - @SuppressFBWarnings). If annotation there
// is in bytecode bytecode analyzer can do something based on the annotation.
// For example with @SuppressFBWarnings it can suppress warnings.
// - During execution (Bloody enterprice = @OneToMany). We can change runtime
// behavior with annotations

// Annotations can be with or without parameters



// OWN ANNOTATION 
// You can create your own annotation. For that declare it with @interface
// Annotation can be annotated.
// @Documented - if the annotation should be in the generated javadoc.
// "This class has this annotation"
// @Retention 
// - SOURCE - only in source code
// - CLASS - in source code and bytecode
// - RUNTIME - in source code, bytecode and in runtime. You can check
// it with reflection
// @Target - for which element this annotation is class, method, field, ...
// TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE, ...

// New annotation:
@Documented // should be in javadoc
@Retention(RetentionPolicy.SOURCE // shouldn't be in bytecode, only in source 
                                  // code
@Target({ElementType.TYPE}) // for class
@interface MyAnnotation {
}

@MyAnnotation
class Tester {
} 



// ANNOTATION TARGET
// Annotated example, with different targets:
@TypeAnno
public class AnnoDemo {
	@AnnoAnno @interface Anno {}
	
	@FieldAnno String s;
	
	@ConstructorAnno AnnoDemo(@ParamAnno int x) {
		@LocalAnno int y = x
	}
	
	@MethodAnno <@TypeParamAnno T> T method() {
		return null;
	}
	
	@TypeUseAnno List<@TypeUseAnno String @TypeUseAnno []> get() {
		return null;
	}
}

// With target TYPW_USE you can use annotation before any type entry
@Target(ElementType.TYPE_USE)
@interface NotNull {} // value can't be null
@Target(ElementType.TYPE_USE)
@interface Nullable {} // value can be null
// can return null list, but elements can't be null
// param array can be null, but elements can't
@Nullable List<@NotNull String> test(@NotNull String @Nullable [] param
                                     @Nullable Object obj)
                                     throws @NotNull RuntimeException {
    if(obj instanceof @Nullable String) { // obj can't be null, but it doesn't
	                                      // have sence
		@Nullable String s = (@Nullable String)obj;
		return Collection.singletonList(String.valueOf(s));
	} 
	return null;
}

// Sometimes one annotation entry can be applied to several targets
// For example
@Target({ElementType.METHOD, ElementType.TYPE_USE})
@interface MyAnno {
}
class Tester {
    class Inner {}
    // MyAnno will be applied to method and return value
	// This is because java syntax restrictions
    @MyAnno String test(Integer a, Integer b) { return "a"; }
	// MyAnno will be applied to method
    @MyAnno Tester.Inner getInner() { return new Inner(); }
	// MyAnno will be applied to return value
    Tester.@MyAnno Inner getInner2() { return new Inner(); }
}	
// Because of this, it's better don't use any other targets with TYPE_USE.	



// ANNOTATIONS PARAMETERS
// Annotations can have parameters.
// They have the form parameter name equals value.
// Value should be known on compilation stage.
// Annotation parameters can have type:
// - primitive types
// - String
// - Class, Class<? extends Xyz>
// - Enum
// - Annotation (without cyrcular dependencies)
// - One dimentional array of another allowed types
// - Value - compilation time constant	

// To create a parameter you should declare a method. It can have a defualt
// value. If parameter doesn't have defualt value we must specify it 
// when add this annotation.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface MyAnno {
    TimeUnit unit() default TimeUnit.DAYS;
    int data();
}

@MyAnno(data=1, unit=TimeUnit.HOURS)
class Tester {
}
// With default unit value
@MyAnno(data=1)
class Tester {
}	 

// If value is array and it has only one element we can don't specify an
// array just element
@interface MyAnno {
    String[] data();
}
@MyAnno(data="aa") // It will be an array with one element.
class Tester {
}

// If the name of the paramenter is "value" and the annotation has just
// that attribute(or other attributes has default values and you
// don't specify them) we can leave out the name when add this annotation
@interface MyAnno {
    int value();
}
@MyAnno(1)
class Tester {
}

@interface MyAnno {
    int value();
    int data() default 0;
}
@MyAnno(1)
class Tester {
}