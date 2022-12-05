// EXPORT BEFORE DECLARATION
export function sayHi(name) {
    console.log(`Hi, ${name}!`);
}

export const user = "Tom";



//EXPORT AFTER DECLARATION
function sayHi(name) {
    console.log(`Hi, ${name}!`);
}

const user = "Tom";

export { sayHi, user };



//IMPORT
import { sayHi, user } from "./sayHi.js";

sayHi("Tom");
console.log(user);



//IMPORT *
//We can import all object from the module as an object.
import * as test from "./sayHi.js"; //we should specify the module object name

test.sayHi("Tom");
console.log(test.user);



//IMPORT AS
//We can rename objects while importing them
import { sayHi as hi, user as usr } from "./sayHi.js";

hi("Tom");
console.log(usr);



//EXPORT AS
//We can rename objects while exporting them
//We we import objects we should use new names
function sayHi(name) {
    console.log(`Hi, ${name}!`);
}

const user = "Tom";

export { sayHi as hi, user as usr };



//EXPORT DEFAULT 
//If we want to export only one object, we can use export default syntax
//Just write export default before this object
//Can be only one export default in one module
export default class User {
    constructor(name) {
        this.name = name;
    }
}

//Then you can import like this, you can specify any name for this object
import User from "./User.js";

let user = new User("Tom");




//DEFAULT name
//You can use default export with {}, add as default after the object you want import with default export
class User {
    constructor(name) {
        this.name = name;
    }
}

function printUser(user) {
    console.log(user.name);
}

export { printUser, User as default };


//You can use default import with {}, add default as and default object name
import { default as User, printUser } from "./User.js"; //User is imported with default import

let user = new User("Tom");
printUser(user);


//When you use * as import the default property has the default export object.
import * as userModule from "./User.js";

let user = new userModule.default("Tom");
userModule.printUser(user);



// REEXPORT
// You can import and export the same objects. This way you can do wrapper that contains all objects
// in your package
// user.js
export { printUser, User as default };

// wrapper.js
// reexport printUser, reexport default export from User as userAgent
export { printUser, default as User } from "./User.js";
// script.js 
import { printUser, User } from "./wrapper.js"; 


//wrapper.js
//reexport printUser, reexport default export as default export
export { printUser, default } from "./User.js";
// script.js
import { printUser, default as User } from "./wrapper.js";


//wrapper.js
//reexport printUser as default, default as User
export { printUser as default, default as User } from "./User.js";
// script.js
import { default as printUser, User } from "./wrapper.js"


export * from './user.js'; // reexport all named objects, default is not included
export {default} from './user.js'; // reexport default



// DYNAMIC IMPORT
// Import considered before is static.
// Path should be a string(not a result of calculations). You can't import something
// depending on a condition.

// You can import module dynamically with import("path") syntax
// It returns a Promise
// Then you can get a module object
// default property contains default import

// User.js
export { printUser, User as default };

// script.js
const module = await import("./User.js");

let user = new module.default("Tom"); //get default import
module.printUser(user); //get printUser function

// or
const { default: User, printUser } = await import("./User.js");

let user = new User("Tom");
printUser(user);




















