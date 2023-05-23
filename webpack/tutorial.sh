# install webpack and webpack-cli as dev dependency
npm i -D webpack webpack-cli

# Our code in src folder
# index.js, generateJoke.js
# dist
#     index.html
# src
#     index.js
#     generateJoke.js



# To run webpack add into package.json into scripts build configuration
"scripts": {
	"build": "webpack --mode production"
}
# To run it use
npm run build
# It creates in dist folder main.js with bundle code from index.js and generateJoke.js
# dist
#     index.html
#     main.js
# src
#     index.js
#     generateJoke.js
	
# Note, webpack bandled code from external modules, just use them.



# WEBPACK CONFIG
# Let's create a config for webpack
# We can specify mode there
# By default webpack looks for index.js in src folder
# By default it creates a bundle in dest folder with name main.js
# project_folder/webpack.config.js
const path = require("path");
module.exports = {
    mode: "development",
    entry: path.resolve(__dirname, "src/index1.js"), # specify entry file
    output: { # specify bundle location project_folder/dist/bundle.js
        path: path.resolve(__dirname, "dist"),
        filename: "bundle.js", 
    },
};

# You can specify multiply entries, in this case entry is a dictionary
# To name output bundle as specified an entry use [name] 
module.exports = {
    mode: "development",
    entry: {
        bundle: path.resolve(__dirname, "src/index1.js"), # we specified here bundle name
    },
    output: {
        path: path.resolve(__dirname, "dist"),
        filename: "[name].js", # result bundle.js
    },
};



# LOADERS
# We want webpack handle our sass files
# For this webpack use loaders. Let's install sass, loaders
npm i -D sass style-loader css-loader sass-loader

# Make a main.scss file with address src/styles/main.scss
# Add loaders to use for specified file types
# webpack.config.js
module.exports = {
    module: {
        rules: [
            {
                test: /\.scss$/, # specify type of file for loaders to apply
                use: ["style-loader", "css-loader", "sass-loader"], # loaders
            },
        ],
    },
};

# Then we should import our .sass file in index1.js file
import "./styles/main.scss";

# All style will be applied in our bundle(bundle.js), e. g. with js. 



# HTML WEBPACK PLUGIN
# For now, we have index.js html with script tag for out bundle in a body
# We want that it will be created automatically
# Install plugin
npm i -D html-webpack-plugin

# Add it into webpack-config file
const HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = {
    plugins: [
        new HtmlWebpackPlugin({
            title: "Webpack", # title of the page
            filename: "index.html", # name of the page to generate
        }),
    ],
};
# NOTE, WITH HTML WEBPACK PLUGIN WE CAN DELETE DIST FOLDER
# IT WILL BE GENERATED AUTOMATICALLY

# The problem that it will generate the same template automatically
# You can specify template in webpack-config
plugins: [
	new HtmlWebpackPlugin({
		title: "Webpack",
		filename: "index.html",
		template: "src/template.html", # template file
	}),
]

# You can get access of parameter from config file in template
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title><%= htmlWebpackPlugin.options.title %></title> # get title from webpack.config.js
    </head>
    <body>        <div class="container">
            <h3>Don\'t Laugh Challenge</h3>
            <div id="joke" class="joke"></div>
            <button id="jokeBtn" class="btn">Get Another Joke</button>
        </div>
    </body>
</html>



# HASH
# You can generate bundle with a hash in a name instead of just a bundle.js 
# Add [contenthash] into filename
output: {
	path: path.resolve(__dirname, "dist"),
	filename: "[name][contenthash].js",
},
# As a result: bundle8a344a6f09f09902693c.js

# So if you change file in a bundle and run npm run build
# new bundle will be created
# To automatically remove old bundles add into webpack.config.js
output: {
	path: path.resolve(__dirname, "dist"),
	filename: "[name][contenthash].js",
	clean: true, # automatically clean from old files
},



# SERVER
# We can run server to automatically reload you project on change
# In package.json add script
"scripts": {
	"dev": "webpack serve"
}

# To run use: 
# It can ask to install webpack-dev-server, agree.
npm run dev
# Add some configs to webpack.config.js
devServer: {
	static: {
		directory: path.resolve(__dirname, "dist"), # folder to serve
	},
	port: 3000, # port to run
	open: true, # open browser on webpack serve
	hot: true, # hot reload
}
# Now on every change save project will be rebundled and rerun.
# Now you can turn off live server.

# NOTE, server not runs from dist folder, it runs from files in memory
# So with server dist folder won't be updates
# Run bundling manually (npm run build) to update dist folder



# SOURCE MAP
# By default in browser you get the line with error from a bundle file
# To get also name of origin file and line from it 
# add into webpack.config
module.exports = {
    devtool: "source-map",
};



# BABEL LOADER
# Let's install babel loader to use babel
npm i -D babel-loader @babel/core @babel/preset-env
# In webpack-config.js
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



# IMAGES
# We want to add images in our bundle
# We have image in src/assets/laughing.svg
# Add this file into our root bundle file
# index1.js
import laughing from "./assets/laughing.svg";

# Next we need to add loader for images, it comes we webpack, some
# We just need to specify it in webpack.config.js
module: {
	rules: [
		{
			test: /\.(png|svg|jpg|jpeg:giff)$/i,
			type: "asset/resource",
		},
	],
},

# And specify in output, to save image name in dest
output: {
	assetModuleFilename: "[name][ext]",
},

# To use it, just assign it to img from dom
import laughing from "./assets/laughing.svg";

const laughImg = document.getElementById("laughImg");
laughImg.src = laughing;



# WEBPACK-BUNDLE-ANALYZER
# It shows treemap of element that your bundle contains
npm i -D webpack-bundle-analyzer

#webpack.config.js
const BundleAnalyzerPlugin =
    require("webpack-bundle-analyzer").BundleAnalyzerPlugin;
	
plugins: [
	new BundleAnalyzerPlugin(),
],