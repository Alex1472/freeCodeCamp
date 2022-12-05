//SYMBOL
// Symbol is an unique identifier
let id = Symbol("id")

// You can get description with the property description
console.log(id.description) //"id"

//It's just description symbol with the same description are different.
let id1 = Symbol("id");
let id2 = Symbol("id");
alert(id1 == id2); // false



//You can add symbol as a key to an object
let user = {
    name: "Tom"	
};
let id = Symbol("id")

user[id] = 1
console.log(user[id])



// Symbol a are taken in account by the prop in obj operator and the Object.assing method.
// But not by for .. in loop



// You can you the global register of symbol to get access the the in all program.
// This symbols have unique identifiers.
// Use Symbol.for to get or create symbol by identifier.
let id = Symbol.for("id")
let newId = Symbol.for("id")
	
console.log(id == newId) //true

//You can get description of symbol with the method Symbol.keyFor
console.log(Symbol.keyFor(id)) //"id"
//Or with the discription property
console.log(id.description)



//There are many system symbol to adjust objects behavior. For example:
// Symbol.hasInstance
// Symbol.isConcatSpreadable
// Symbol.iterator
// Symbol.toPrimitive
