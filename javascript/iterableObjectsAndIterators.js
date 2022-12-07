// ITERATOR
// If we want to make an object interable, we should implementation
// the method [Symbol.iterator]()
// It should return iterator.

// Iterator should has the method next.
// It return { done: false, value: nextValue }
// if it has next value, else { done: true }
class Range {
    constructor(start, end) {
        this.start = start;
        this.end = end;
    }

    [Symbol.iterator]() {
        return {
            current: this.start,
            end: this.end,
            next() {
                if (this.current <= this.end) {
                    return { done: false, value: this.current++ };
                } else {
                    return { done: true };
                }
            },
        };
    }
}

let range = new Range(2, 10);
for (let x of range) {
    console.log(x);
}

// Note, loop for .. of works with an iterable objects.



// You can get iterator explicitly
let range = new Range(2, 10);

let iter = range[Symbol.iterator]();
while (true) {
    let item = iter.next();
    if (item.done) break;
    console.log(item.value);
}



// String is an iterable object
let s = "abacaba";
for (let c of s) {
    console.log(c);
}



// INTERABLE OBJECTS AND PSUDO-ARRAY
// Itarable object is an object that has the Symbol.iterator method.
// Psudo-array is an object that has indexies and the property length`
// Note object can be interable and psudo-array simultaniously(Array, string).

// Object, that is a psudo array, but not iterable
let obj = {
    0: "a",
    1: "b",
    length: 2
}



// ARRAY.FROM
// You can create array from psudo-array or iterable with the Array.from method.
let range = new Range(2, 10);
console.log(Array.from(range));

let obj = {
    0: "a",
    1: "b",
    length: 2,
};
console.log(Array.from(obj));




// GENERATORS
// Generator is a function that generates several values.
// It return special object - generator
// Generator is an iterator.
// Then we can iterator throuth it.
function* generateRange(a, b) {
    for (let i = a; i <= b; i++) {
        yield i;
    }
    return 0;
}

let generator = generateRange(1, 3);
for (let x of generator) {
    console.log(item);
} //1, 2, 3

// More explicit way
while (true) {
    let item = generator.next();
    if (item.done) break;
    console.log(item);
}
// {value: 1, done: false}
// {value: 2, done: false}
// {value: 3, done: false}
// {value: 0, done: true} - note this is a value from return



// COMBINE GENERATORS
// You can embed on generator into another
// Value from one generator will be include into another generator`
// Use for this yield*
function* rangeGenerator(start, end) {
    for (let i = start; i <= end; i++) yield i;
}

function* complexRange() {
    yield* rangeGenerator(1, 3); //values from rangeGenerator will be embed into complex range generator
    yield* rangeGenerator(17, 19);
    yield* rangeGenerator(60, 62);
}

for (let x of complexRange()) {
    console.log(x);
}
//1, 2, 3, 17, 18, 19, 60, 61, 62



// PASSING VALUES INTO GENERATORS
// You can pass into generator as an answer of returned value`
// You pass values into the next method
// First invokation should be without value
// The you can pass value, and get it in a generator as result of previous yield
function* askGenerator() {
    let res = yield "1 + 1"; //2
    console.log(res);
    res = yield "3 * 4"; //12
    console.log(res);
    res = yield "3 + 2 * 2"; //7
    console.log(res);
}

let generator = askGenerator();
let question1 = generator.next(); // "1 + 1"
let question2 = generator.next(2); // "3 * 4"
let question3 = generator.next(12); // "3 + 2 * 2"
let res = generator.next(7); // { done: true}



// GENERATOR THROW
// You can pass excetion into a generator
// You need invoke generator.throw(error)
// In generator exception occurs at last yield
function* testGenerator() {
    let res = yield "1 + 1";
    try {
        res = yield "2 * 3";
    } catch (err) {
        console.log(err.message);
    }
    res = yield "2 + 4 * 2";
}

let generator = testGenerator();

let question1 = generator.next();
let question2 = generator.next(2);
generator.throw(new Error("Don't know answer."));













