// JS do not support arithmetic operations for objects
// It converts objects to primitives in this case.
// For example, we can't create a vector object and add them.



// Object is always true, when we try to convert it to boolean.
// Let's look at convertion to string or number.



// First, js should select a "hint":
// There is 3 hints:
//     1) string  When we need a string
//        For example, alert(obj)
//     2) number  When we need a number
//        For example with arithmetic operations, unary plus, comparation operators
//        let num = Number(obj);
//        let n = +obj; 
//        let delta = date1 - date2;
//        let greater = user1 > user2;
//     3) default  When js is not sure, to what type it should convert the object
//        For example sum, ==
//        let total = obj1 + obj2;
//        if (user == 1) { ... };
//        Note, <, >, ... always use number hint.



// When hint is selected, js tries to invoke the obj[Symbol.toPrimitive](hint) method
// if it is defined
// It should returns corresponding priminive value based on hint.
class User {
    constructor(name) {
        this.name = name;
    }

    [Symbol.toPrimitive](hint) {
        console.log(hint);

        return hint === "string" ? "test" : 1;
    }
}

let user1 = new User("Tom");
let user2 = new User("John");
let res = user2 - user1; // number hint, res = 0
alert(user1); // string hint, returns "test"
let sum = user1 + 1; //default hint, sum = 2



// If Symbol.toPrimitive is not defined, js try to use toString() and valudOf() methods.
// Object has default implementation of this methods.
// toString() returns "[object Object]" string
// valueOf() returns the object
// JS tries to use this methods if they returns object it will be ignored.
// So, by default, the valueOf() method will be ignored.
// Both method return an object there will be an error.

let user1 = new User("Tom");
console.log(user1.toString()); // [object Object]
console.log(user1.valueOf() == user1); // true



// If hint is a number or default js tries to use the valueOf method.
// If it returns an object, it uses toString method`
// If hint is a string js tries to use the toString method then the valueOf method



// As by default valueOf is ignore, it's always used toString method
class User {
    constructor(name, salary) {
        this.name = name;
        this.salary = salary;
    }
}

let user1 = new User("Tom", 3000);
let user2 = new User("John", 5000);

console.log(user1 - user2); //number hint. NaN, as toString returns "[object Object]"
// js can't convert it to number
alert(user1) //string hint. "[object Object]"
console.log(user1 + 1); //default hint. "[object Object]1"



// with toString() and valueOf() implementation
class User {
    constructor(name, salary) {
        this.name = name;
        this.salary = salary;
    }

    toString() {
        return `[${this.name}]`;
    }

    valueOf() {
        return this.salary;
    }
}

let user1 = new User("Tom", 3000);
let user2 = new User("John", 5000);

console.log(user1 - user2); // number hint, valueOf() used, -2000
alert(user1); // string hint, toString() used, "[Tom]"
console.log(user1 + 1); // default hing, valueOf() used, 3001
















