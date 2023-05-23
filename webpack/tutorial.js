// We has shared scope for all js-files, but if one file depends on other 
// we should load them in right order. It's tedious
<script src="./src/app/alert.service.js"></script>
<script src="./src/app/component.service.js"></script>
<script src="./src/app/utils/inputs-are-valid.js"></script>
<script src="./src/app/utils/parse-inputs.js"></script>
<script src="./src/app/app.js"></script>
// Note, without modules type="module" we can't use import in js files

// We can use import if js file is a module (type="module")
// Modules has their own scopes for every module, so to use something from 
// another module you should import it. And those script should export functionality
<script src="./src/app/app.js" type="module"></script>



// install webpack and webpack-cli as dev dependency
npm i -D webpack webpack-cli

// Our code in src folder
// index.js, generateJoke.js
// dist
//     index.html
// src
//     index.js
//     generateJoke.js



// To run webpack add into package.json into scripts build configuration
"scripts": {
	"build": "webpack --mode production"
}
// To run it use
npm run build
// It creates in dist folder main.js with bundle code from index.js and generateJoke.js
// dist
//     index.html
//     main.js
// src
//     index.js
//     generateJoke.js
	
// Note, webpack bandled code from external modules, just use them.



// WEBPACK CONFIG
// By default webpack searches for config file in root directory with name webpack.config.js
// You can specify config file name with --config flag
"scripts": {
    "start": "webpack --config webpack.config.js"
},

// Let's create a config for webpack
// We can specify mode there
// By default webpack looks for index.js in src folder
// By default it creates a bundle in dest folder with name main.js
// project_folder/webpack.config.js
const path = require("path");
module.exports = {
    mode: "development",
    entry: path.resolve(__dirname, "src/index1.js"), // specify entry file
    output: { // specify bundle location project_folder/dist/bundle.js
        path: path.resolve(__dirname, "dist"),
        filename: "bundle.js", 
    },
};

// You can specify multiply entries, in this case entry is a dictionary
// To name output bundle as specified an entry use [name] 
module.exports = {
    mode: "development",
    entry: {
        bundle: path.resolve(__dirname, "src/index1.js"), // we specified here bundle name
    },
    output: {
        path: path.resolve(__dirname, "dist"),
        filename: "[name].js", // result bundle.js
    },
};

// You can specify base directory for entries
context: __dirname + "/frontend",
entry: {
	home: "./home", //entry in folder /frontend/home.js
	about: "./about",
},

// https://webpack.js.org/configuration/output/#outputlibrary
// Suppose we want to get exports from our entry point in another script
// By default we can't do this
// Entrypoint home.js:
"use strict";
let welcome = require("./welcome");
exports.welcome = welcome;
//We need to get library for the output in our config
output: {
	filename: "build.js",
	library: "home",
},
//It creates a global variable in our bundle named home with export object from our entry
//Now, you can use exports in another scripts in html
//index.html
<body>
	<script src="./dist/build.js"></script>
	<script>
		console.log(home);
		home.welcome("John");
	</script>
</body>



// WEBPACK-MERGE
// You can want have several configs, for development and production.
// You can create common config that is the same for both modes and configes for dev and product
// The you need to merge common with dev and prod.
// Use webpack-merge for this
npm i --save-dev webpack-merge

// We have 3 configs: webpack.common.js, webpack.dev.js and webpack.prod.js
// In dev and prod we should import common config and merge function and
// merge confings
// webpack.dev.js
const common = require("./webpack.common.js");
const { merge } = require("webpack-merge");

module.exports = merge(common, {
    mode: "development",
    output: {
        path: path.resolve(__dirname, "dist"),
        filename: "main.js",
    },
});

// Then in package.json you can specify config in scripts
"scripts": {
	"start": "webpack --config webpack.dev.js",
	"prod": "webpack --config webpack.prod.js"
},


// In old-style way you can pass environment variable into webpack-config 
// to turn between development and production modes
//webpack.config.js
const NODE_ENV = process.env.NODE_ENV || "development";
...
watch: NODE_ENV == "development",
devtool: NODE_ENV == "development" ? "eval" : "source-map",
//In console
NODE_ENV=production webpack

// You can also need to use this paramenter no only in webpack.config.js 
// but also in modules
// Since webpack 4, specifying mode automatically configures process.env.NODE_ENV for you through DefinePlugin
// So in your modules you can use it
console.log(process.env.NODE_ENV)

// You can also add values into process.env with EnvironmentPlugin
// And as global constants with DefinePlugin
// webpack.config.js
const webpack = require("webpack");
plugins: [
	new webpack.DefinePlugin({
		TEST: JSON.stringify("DDD"), // webpack use value, so value should be "DDD", therefore we use stringify or '"DDD"'
	}),
	new webpack.EnvironmentPlugin({
		DATA: "AAA",
	}),
]
// Source code
console.log(process.env.DATA);
console.log(TEST);
// Bundled code
console.log("AAA");
console.log("DDD");






// LOADERS
// We want webpack handle our sass files
// For this webpack use loaders. Let's install sass, loaders
npm i -D sass style-loader css-loader sass-loader

// Make a main.scss file with address src/styles/main.scss
// Add loaders to use for specified file 
// sass-loader - converts sass into css
// css-loader - add css into bundle as .js
// style-loader - applies js(after coverting from css) to dom
// so first should be applied sass-loader
// but loaders applied in reverse order, so we should specify first style-loader, then css-loader, then sass-loader
// webpack.config.js
module.exports = {
    module: {
        rules: [
            {
                test: /\.scss$/, // specify type of file for loaders to apply
                use: ["style-loader", "css-loader", "sass-loader"], // loaders
            },
        ],
    },
};
// Then we should import our .sass file in index1.js file
import "./styles/main.scss";

// All style will be applied in our bundle(bundle.js), e. g. with js. 
// But this means that it will flicking because css applies in js
// We can exptract css into separated file
// Install mini-css-extract-plugin
// Add into config, add as plugin and when loading css as loader instead of style-loader,
// to exteract it in separated file, not inject into .js file
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
module: {
	rules: [
		{
			test: /\.scss$/i,
			use: [MiniCssExtractPlugin.loader, "css-loader", "sass-loader"],
		},
	],
},
plugins: [
	new MiniCssExtractPlugin({
		filename: "[name].[contenthash].css",
	}),
],


// BABEL
// To use babel in webpack, first install it
npm install -D babel-loader @babel/core @babel/preset-env
// Then add loader into webpack.config.js
module: {
	rules: [
		{
			test: /\.m?js$/,
			exclude: /node_modules/,
			use: {
				loader: "babel-loader",
				options: {
					presets: [["@babel/preset-env"]],
				},
			},
		},
	],
},

// With resolve object, you can specify where to search modules
// and with resolveLoader you can specify where to search loaders
// https://webpack.js.org/configuration/resolve/#resolveloader



// HTML WEBPACK PLUGIN
// For now, we have index.js html with script tag for out bundle in a body
// We want that it will be created automatically
// Install plugin
npm i -D html-webpack-plugin

// Add it into webpack-config file
const HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = {
    plugins: [
        new HtmlWebpackPlugin({
            title: "Webpack", // title of the page
            filename: "index.html", // name of the page to generate
        }),
    ],
};
// NOTE, WITH HTML WEBPACK PLUGIN WE CAN DELETE DIST FOLDER
// IT WILL BE GENERATED AUTOMATICALLY

// The problem that it will generate the same template automatically
// You can specify template in webpack-config
plugins: [
	new HtmlWebpackPlugin({
		title: "Webpack",
		filename: "index.html",
		template: "src/template.html", // template file
	}),
]

// You can get access of parameter from config file in template
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title><%= htmlWebpackPlugin.options.title %></title> // get title from webpack.config.js
    </head>
    <body>        <div class="container">
            <h3>Don\'t Laugh Challenge</h3>
            <div id="joke" class="joke"></div>
            <button id="jokeBtn" class="btn">Get Another Joke</button>
        </div>
    </body>
</html>



// HASH
// You can generate bundle with a hash in a name instead of just a bundle.js 
// This need as browser can cache you bundle, so if it changes it won't look these changes
// To prevent this, we should changed bundle name
// Add [contenthash] into filename
// Note, to include this script into html use HtmlTemplatePlugin, to generate html file with right script name
output: {
	path: path.resolve(__dirname, "dist"),
	filename: "[name][contenthash].js",
},
// As a result: bundle8a344a6f09f09902693c.js

// So if you change file in a bundle and run npm run build
// new bundle will be created
// To automatically remove old bundles add into webpack.config.js
output: {
	path: path.resolve(__dirname, "dist"),
	filename: "[name][contenthash].js",
	clean: true, // automatically clean from old files
},
// Note, earlier you should use clean-webpack-plugin



// AUTOMATIC RELOADING
// You can say webpack watch for changes and automatically rebundle project in changes save
// In webpack.config add watch
module.exports = {
    watch: true, //watch for changes
    watchOptions: {
        aggregateTimeout: 100, //rebundle after 100ms after changes
    },
};

// We can run server to automatically reload you project on change
// In package.json add script
"scripts": {
	"dev": "webpack serve"
}
// To run use: 
// It can ask to install webpack-dev-server, agree.
npm run dev
// Add some configs to webpack.config.js
devServer: {
	static: {
		directory: path.resolve(__dirname, "dist"), // folder to serve
	},
	port: 3000, // port to run
	open: true, // open browser on webpack serve
	hot: true, // hot reload
}
// Now on every change save project will be rebundled and rerun.
// Now you can turn off live server.

// Or you can specify it in run configuration
"scripts": {
    "start": "webpack-dev-server --config webpack.dev.js --open",
}

// NOTE, server not runs from dist folder, it runs from files in memory
// So with server dist folder won't be updates
// Run bundling manually (npm run build) to update dist folder



// SOURCE MAP
// By default in browser you get the line with error from a bundle file
// To get also name of origin file and line from it
// Use devtool: "source-map" for this
// Will be created special file for bundle, for example: build.js.map
// And in you bundle there will be reference for this file
# sourceMappingURL=build.js.map 
// add into webpack.config
module.exports = {
    devtool: "source-map",
};

// You can embed this source map in a bundle with parameter devtool: "source-map-inline"
// In this case source-map will be in the bundle.
// As it rather big we should not use this in production



// BABEL LOADER
// Let's install babel loader to use babel
npm i -D babel-loader @babel/core @babel/preset-env
// In webpack-config.js
module: {
	rules: [
		{
			test: /.js$/,
			exclude: /node_modules/,
			use: {
				loader: "babel-loader",
				options: {
					presets: ["@babel/preset-env"],
				},
			},
		},
	],
}



// IMAGES
// We want to add images in our bundle
// We have image in src/assets/laughing.svg
// Add this file into our root bundle file
// index1.js
import laughing from "./assets/laughing.svg";

// Next we need to add loader for images, it comes we webpack, some
// We just need to specify it in webpack.config.js
module: {
	rules: [
		{
			test: /\.(png|svg|jpg|jpeg:giff)$/i,
			type: "asset/resource",
		},
	],
},

// And specify in output, to save image name in dest
output: {
	assetModuleFilename: "imgs/[name][ext]", //in the imgs folder
},

// To use it, just assign it to img from dom
import laughing from "./assets/laughing.svg";

const laughImg = document.getElementById("laughImg");
laughImg.src = laughing;


// To not import all images manually you can use html-loader
// It automatically imports all images in js
npm i --save-dev html-loader
module: {
	rules: [
		{
			test: /\.html/i,
			use: ["html-loader"],
		},
	],
}




// WEBPACK-BUNDLE-ANALYZER
// It shows treemap of element that your bundle contains
npm i -D webpack-bundle-analyzer

//webpack.config.js
const BundleAnalyzerPlugin =
    require("webpack-bundle-analyzer").BundleAnalyzerPlugin;
	
plugins: [
	new BundleAnalyzerPlugin(),
],