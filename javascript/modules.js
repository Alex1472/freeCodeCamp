//HOW TO USE MODULES
// Module just a js-file that which can export some code and in which you 
// can import another module

// As modele can use special keywords and has some features, in html you should specify that
// a js file is a module. That it can import another modules


//index.html
<body>
	<script src="./scripts/script.js"type="module"></script> //should specify that file is a module
</body>


//script.js
import { sayHi } from "./sayHi.js"; //path to file from where we want to export relative to a current file

sayHi("Tom");


//sayHi.js
function sayHi(name) {
    console.log(`Hi, ${name}!`);
}

export { sayHi };



//SOME FEATURES:
// 1) Modules always use 'use strict'
a = 5; // error
// 2) Each module has its own scope. You can use something from another module 
//    except you import it.
// 3) Module executes only one time. The created objected can be imported to another files.
//    Therefore every imported gets the same objects from importing module.
// admin.js
export let admin = {
  name: "John"
};
// 1.js and 2.js gets the same admin object
// 1.js
import {admin} from './admin.js';
admin.name = "Pete";
// 2.js
import {admin} from './admin.js';
console.log(admin.name); // Pete

// 4) The import.meta object contains imformation about the current script
// script.js
console.log(import.meta);
// {url: 'http://localhost:5500/scripts/script.js', resolve: ƒ}

// 5) this is not defined in a module
// script.js
console.log(this) //undefined
// If script is not a module, this is a window (global object)




// BROWSER FEATURES:
// 1) Modules are executed only when html is loaded.
<body>
	<script type="module"> // executed only when html is loaded
		console.log(typeof button); // object
	</script>
	<script> // executed before html is loaded 
		console.log(typeof button); // undefined
	</script>

	<button id="button">test</button>
</body>

// 2) You can add attiribute async so that the script will be executed as loaded. 
//    It won't wait html and another scripts.
<body>
	<script type="module" async> // executed async
		console.log(typeof button); //undefined
	</script>
	<button id="button">test</button>
</body>

// 3) Naked modules are not supported.
// You should specify absolute or relative path.
import {sayHi} from 'sayHi'; //error, use "./sayHi.js"

// 3) Old browser do not support type="module". They ignore this script
//    You can add nonmodule script. This script won't be executed in a new browser
//    but will in old
<script type="module">
  alert("Works in a modern browser.");
  alert("Old browsers skip this script.");
</script>

<script nomodule>
  alert("Modern browsers skip this script.")
  alert("Old browsers excecute this script.");
</script>


