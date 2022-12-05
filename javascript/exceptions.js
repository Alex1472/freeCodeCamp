// You can handle exception with try - catch block
try {
	aaa

} catch(err) {
	
}

// Note that catch only works with syncronas code:
// This code do not catched exception
setTimeout(function() {
  try {
    noSuchVariable; 
  } catch {
    console.log("catched");
  }
}, 1000);



// ERROR Object
// Error object contains name, message and often stack - string information
// where the error occurs
try {
    aaaa;
} catch (err) {
    console.log(err.name); // ReferenceError
    console.log(err.message); // aaaa is not defined
    console.log(err.stack);
	// ReferenceError: aaaa is not defined
    // at http://localhost:5500/scripts/script.js:2:5
}

// If you don't want to get error object, you can omit it
try {
    aaaa;
} catch {
    console.log("catched");
}



// THROW
// You can throw your own errors with the operator throw
// Technically you can throw any object as an error
let str = '{ "age": 15 }';

try {
    let obj = JSON.parse(str);
    if (!obj.name) {
        throw 1;
    }
} catch (err) {
    console.log(err); // 1
}

// But its better to throw any built-in error
// name is the name of error
// you can pass a message into constructor
let str = '{ "age": 15 }';
try {
    let obj = JSON.parse(str);
    if (!obj.name) {
        throw new SyntaxError("Property name expected.");
    }
} catch (err) {
    console.log(err.name); // "Syntax Error"
    console.log(err.message); // "Property name expected."
}

// You can handle only expected exceptions
// Other errors you can throw up in the catch block
let str = '{ "name": "Tom", "age": 15 }';
try {
    let obj = JSON.parse(str);
    if (!obj.name) {
        throw new SyntaxError("Property name expected.");
    }

    unknownVariable;
} catch (err) {
    if (err.name == "SyntaxError") {
        console.log("Required name property");
    } else {
        throw err;
    }
}



// FINALLY
// You can add finally block, it works always if there was error or not
try {
    console.log("Do something");
} catch (err) {
    console.log(err);
} finally {
    console.log("Releasing resources"); //works
}

try {
    console.log("Do something");
	error
} catch (err) {
    console.log(err);
} finally {
    console.log("Releasing resources"); //works
}

//Note, it also works if in try we return some value



//CUSTOM ERROR OBJECT
// You can create your own error objecte.
// It's better to inherit in from the Error class
class ValidationError extends Error {
    constructor(message) {
        super(message);
        this.name = "ValidationError"; 
    }
}

class PropertyRequiredError extends ValidationError {
    constructor(property) {
        super(`Property ${property} required.`);
        this.name = "PropertyRequiredError";
        this.property = property; // add some date
    }
}

function parseString(str) {
    let obj = JSON.parse(str);
    if (!obj.name) {
        throw new PropertyRequiredError("name");
    }
    if (!obj.age) {
        throw new PropertyRequiredError("age");
    }
}

let str = '{ "age": 15 }';
let obj;
try {
    obj = parseString(str);
} catch (err) {
    if (err instanceof SyntaxError) {
        console.log("Incorrect JSON");
    } else if (err instanceof PropertyRequiredError) {
        console.log(err.message);
    } else {
        throw err;
    }
}







