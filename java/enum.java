// ENUMS
// Enum in java is a special class. 
// With enum you can automatically create fixed instances of the enum.
// All instances are created when you first time use the enum.
// For VM we create static final fields in the class Weekday
// You can create a enum like that:
enum Weekday {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}
// And use like that:
System.out.println(Weekday.FRIDAY);



// You can define methods, fields, constructors for enum.
enum Weekday { // first specify all instances
    MONDAY("MON", false), TUESDAY("TUE", false),
    WEDNESDAY("WED", true), THURSDAY("THU", false),
    FRIDAY("FRI", true), SATURDAY("SAT", true),
    SUNDAY("SUN", true); // use semi-colon

	// After specifing instances, it's a simple class.
	// Usually all fields in your enum are final.
    private final String shortName;
    private final boolean isWeekend;

    Weekday(String shortName, boolean isWeekend) {
        this.shortName = shortName;
        this.isWeekend = isWeekend;
    }

    public String getShortName() { return this.shortName; }
    public boolean IsWeekend() { return this.isWeekend; }
}

// You can use enums like that:
System.out.println(Weekday.SATURDAY.getShortName());
System.out.println(Weekday.SATURDAY.IsWeekend());

// Use the method Enum.values() to go through all values.
for(Weekday day: Weekday.values()) {
	System.out.println(day + " (" + day.getShortName() + ") " +
	(day.IsWeekend() ? "Relax and enjoy" : "Work"));
}

// You can use enums with switch. You shouldn't default default part
// But you should specify all enum values. Otherwise there will be an exception
public static String workingHours(Weekday weekday) {
	return switch(weekday) {
		case MONDAY, FRIDAY -> "9 - 13";
		case TUESDAY, THURSDAY -> "13 - 19";
		case WEDNESDAY, SATURDAY, SUNDAY -> "Day off";
	};
}

// Some useful methods
EnumClass.values() // values of the enum
EnumClass.valueOf(String) // value by string representation
ENUM_VALUE.name() // element name
ENUM_VALUE.ordinal() // serial number






