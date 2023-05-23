// use REPL to evaluate javascript in console
// enter "node"
// enter javascript
// Ctrl + C - to exit

//node index.js (node index) - run js-file



// GLOBALS
// In any node module you can access do these variable anywhere
__dirname
__filename
require // function to use modules
module // info about current module
process // info about environment where the program being executed 

// In nodejs there is an object global.
// If you set attribute to it, the value will be available accross all project.
// It's like a global variable.
// test.js
function sayHi(name) {
    console.log(`Hi, ${name}!`);
}
global.sayHi = sayHi;

// index.js
require("./test");
sayHi("Tom");



// MODULES
//In node.js every .js file is a module. To export it to another file you should use require function.
//You can set different properties to exports object and than use it in another module.
//Node, cache modules, so on the second require module won't be executed.
//Node, just get it from cache.

// test.js
// Note, exports just a reference on module.exports
exports.hello = function(name) { console.log(`Hello ${name}`);} ;

// index.js
const test_module = require("./test"); //get exports object
test_module.hello("Alex");

// Also you can set exports object.
// test.js
module.exports = { name: "Alex", age: 25, hello: function() { console.log("Hello!"); };
// Note, if you want to export just one object, it's better just assign it to exports
// module.exports = object_to_export

// index.js
const test_module = require("./test");
console.log(test_module.name);
console.log(test_module.age);
test_module.hello();


// You can make directory a module
// Node tryes to find required module as a file with specified name,
// If it not found, like a directory with specified name, and there execute file index.js
// Some to make directory module user, you should add into folder user file index.js with all data
src/user/index.js

user = require('./user');


// When node tring to load a module it uses this algorithm
https://nodejs.org/dist/latest-v18.x/docs/api/modules.html#all-together
// 1)If it's not a path and it's an embedded module (fs for example) just use it 
// 2)If it's a path, node search there module as file or directory
// 3)It it's not a path and it's not an embedded module
//   search node_modules directory in a current directory and there module
//   search node_modules directory one directory update and there module
//   ...
//   YOU CAN FIND THESE PATHS IN MODULE.PATHS
//   So we can install external dependencies in root node_modeles directory
//   And then require this dependencies in nested directories
//  4)Searching in folders from paths from environment variable NODE_PATH (you can set in terminal) 

// MODULE OBJECT
// In every module there is a global variable with meta information
// about module  -  module
// It's attributes:
// id - usually path to the module
// exports - object for export from the current module
// filename - full name of the file
// loaded - is module already loaded
// children - modules that current module pluged in (with require)
Module {
  id: 'D:\\DifferentReps\\JavaScriptProjects\\TestProjects\\TestNodeJSProject\\user.js',
  path: 'D:\\DifferentReps\\JavaScriptProjects\\TestProjects\\TestNodeJSProject',
  exports: { User: [Function: User] },
  filename: 'D:\\DifferentReps\\JavaScriptProjects\\TestProjects\\TestNodeJSProject\\user.js',
  loaded: false,
  children: [],
  paths: [
    'D:\\DifferentReps\\JavaScriptProjects\\TestProjects\\TestNodeJSProject\\node_modules',
    'D:\\DifferentReps\\JavaScriptProjects\\TestProjects\\node_modules',
    'D:\\DifferentReps\\JavaScriptProjects\\node_modules',
    'D:\\DifferentReps\\node_modules',
    'D:\\node_modules'
  ]
}



// NPM
// Npm - package manager for node
// Npm comes with node, so if you have installed node, you have installed npm

// To create a module for publishing you should create in a directory index.js file
// and package.json file 
// This file contains metainformations about module
// You can create it automatically with npm init command
npm init 
// To publish package you first should log in or create account
// Use command npm adduser
npm adduser
// To publish use command npm publish
npm publish

//To search a module by description use npm search
npm search some text to search

//To install a module use npm install / npm id
//We install it into node_modules directory
npm install module_name
npm i module_name

// To update modules from node_modules use npm update
npm up

// To remove module use npm r
npm r module_name

// To get help use npm help 
npm help


PATH MODULE

const path = require("path");
console.log(__filename);

//name of the file
console.log(path.basename(__filename));
//directory name == __dirname
console.log(path.dirname(__filename));
//extention
console.log(path.extname(__filename));
//join path segments
console.log(path.join(__dirname, "test", "hello.txt"));
//get object with basename, dirname, ext, root and name.
console.log(path.parse(__filename));



FILESYSTEM MODULE

const path = require('path');
const fs = require('fs');

All this methods by default are asyncronous. You can use callback function.

//Make directory, 
fs.mkdir(path.join(__dirname, "test"), {}, err => {
    if(err) throw err;
    console.log("Folder created");
});

//Create file
fs.writeFile(path.join(__dirname, "test", "hello.txt"), "Hello world!" , err => {
    if(err) throw err;
    console.log("File written...");
});

//Append data to file.
fs.appendFile(path.join(__dirname, "test", "hello.txt"), " I love node.js!", err => {
    if(err) throw err;
    console.log("Data appended");
});

//Read file
fs.readFile(path.join(__dirname, "test", "hello.txt"), "utf8", (err, data) => {
    if(err) throw err;
    console.log(data);
});

//Rename file
fs.rename(path.join(__dirname, "test", "hello.txt"), path.join(__dirname, "test", "helloworld.txt"), err => {
    if(err) throw err;
    console.log("File renamed!");
});




OS MODULE

const os = require("os");

console.log(os.platform());

console.log(os.arch());

console.log(os.totalmem());

console.log(os.freemem());

console.log(os.homedir());

console.log(os.cpus());




URL MODULE

const url = require("url");

let myUrl = new URL("http://myWebsite.com/hello.html?active=true&id=100");

console.log(myUrl.href);
console.log(myUrl.toString());
console.log(myUrl.host);
console.log(myUrl.hostname);
console.log(myUrl.pathname);
console.log(myUrl.search);
console.log(myUrl.searchParams);

myUrl.searchParams.append("param", "data");
console.log(myUrl.search);

myUrl.searchParams.forEach((name, value) => console.log(`Name: ${name} Value: ${value}`));




EVENTS MODULE

Much of the Node.js core API is built around an idiomatic asynchronous event-driven architecture in which certain kinds of objects (called "emitters") emit named events that 
cause Function objects ("listeners") to be called.

const EventEmitter = require("events");

class MyEventEmitter extends EventEmitter { }

let eventEmitter = new MyEventEmitter();

eventEmitter.on("event", () => console.log("Event fired!"));
eventEmitter.emit("event");
eventEmitter.emit("event");
eventEmitter.emit("event");
eventEmitter.emit("event");