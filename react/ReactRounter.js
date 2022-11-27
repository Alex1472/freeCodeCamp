//You can create multipage web app with react router.
//First you need to add react-router-dom package
//And wrap you app with BrowserRouter component
//BrowserRouter component is a context provider

import React from "react"
import ReactDOM from "react-dom"
import { BrowserRouter as Router } from "react-router-dom"
import App from "./App"

ReactDOM.render(
	<Router>
		<App />
	</Router>,
	document.getElementById("root")
)



//To navigate to another page instead of <a> tag you should use the Link component
//In the paramenter "to" you should add path where you want to go

import React from "react"
import { Link } from "react-router-dom"

function App() {
	return (
		<div>
			<Link to="/">Home</Link>
			<Link to="/about">About</Link>
		</div>
	)
}

export default App



// You can specify what you what to render for each path with Switch and Route components.
// You should place Switch in a place, where nested component should be rendered.
import React from "react"
import {Link, Switch, Route} from "react-router-dom"

import Home from "./Home"
import About from "./About"

function App() {    
    return (
        <div>
            <Link to="/">Home</Link>
            <Link to="/about">About</Link>
            
            <Switch> 
				//Specify what should be rendered for each path
			    //Path is matched if start part is matched. Use exact for strick matching
                <Route exact path="/"><Home /></Route>
                <Route path="/about"><About /></Route>
            </Switch>
        </div>
    )
}



export default App

//You can use nested routes
//In profile you can specify nested routes 
function App() {    
    return (
        <div>
            <Header />
            
            <Switch>
                <Route exact path="/">
                    <Home />
                </Route>
                <Route path="/profile">
                    <Profile/>
                </Route>
            </Switch>
            
            <Footer />
        </div>
    )
}

function Profile() {
    return (
        <div>
            <h1>Profile Page</h1>
            <ul>
                <li><Link to="/profile/info">Profile Info</Link></li>
                <li><Link to="/profile/settings">Profile Settings</Link></li>
            </ul>
            <Switch>
                <Route path="/profile/info">
                    <Info />
                </Route>    
                <Route path="/profile/settings">
                    <Settings />
                </Route>        
            </Switch>
        </div>
    )
}



//You can also set up routs like this:
<Switch>
	<Route exact path="/" component={Home} />
	<Route path="/about" component={About} />
</Switch>
//We are passing component function, note a jsx.
//As a props of our component we get history, location, match, staticContext information.
//If we are passing jsx into Route we can get this information with hooks.



// USEPARAMS
// You can use params in your Router path
// Then you can handle this param to display corresponding data
<Switch>
	<Route path="/services/:serviceId" >
		<ServiceDetail />
	</Route>
</Switch>

//Specify parameter in a link
function ServicesList() {
    const services = servicesData.map(service => (
        <h3 key={service._id}>
            <Link to={`/services/${service._id}`}>{service.name}</Link> - ${service.price}
        </h3>
    ))
    return (
        <div>
            <h1>Services List Page</h1>
            {services}
        </div>
    )
}

// Get the parameter with the useParams hooks, the we can use it to render corresponding data.
function ServiceDetail(props) {
    const {serviceId} = useParams()
    const thisService = servicesData.find(service => service._id === serviceId)
    
    return (
        <div>
            <h1>Service Detail Page</h1>
            <h3>{thisService.name} - ${thisService.price}</h3>
            <p>{thisService.description}</p>
        </div>
    )
}



// You can get current match part of route with the useRouteMatch hooks
// We can use it in links and routes. We should use url for link, path for route
// path - The path pattern used to match, url - The matched portion of the URL.
function Profile() {
    const {path, url} = useRouteMatch()

    return (
        <div>
            <h1>Profile Page</h1>
            <ul>
                <li><Link to={`${url}/info`}>Profile Info</Link></li>
                <li><Link to={`${url}/settings`}>Profile Settings</Link></li>
            </ul>
            
            <Switch>
                <Route path={`${path}/info`}>
                    <Info/>
                </Route>
                <Route path={`${path}/settings`}>
                    <Settings/>
                </Route>
            </Switch>
        </div>
    )
}



// We uses Link component to go to another page.
// But we can use the useHistory hook to go to another page programmatically
function ServiceDetail() {
    const {serviceId} = useParams()
    const history = useHistory()
    const thisService = servicesData.find(service => service._id === serviceId)

    
    function handleClick() {
        console.log("Submitting...")
        setTimeout(() => {
            history.push("/services") //go to services page
        }, 2000)
        history.replace("")
    }
    
    return (
        <div>
            <h1>Service Detail Page</h1>
            <h3>{thisService.name} - ${thisService.price}</h3>
            <p>{thisService.description}</p>
            <button onClick={handleClick}>Go back to all services</button>
        </div>
    )
}


// You can use useLocation hook to get current location in the app
const location = useLocation()
console.log(location)

{pathname: "/services/4", search: "", hash: "", state: undefined, key: "qporvo"}
// pathname - current location in your app
// search - query string (begins with ?)	



//REDIRECT
// If the Redirect component renders it sends us to another page
import React, {useState} from "react"
import {Link, Switch, Route, Redirect} from "react-router-dom"

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false)
    return (
        <div>
            <Link to="/">Home</Link>
            <Link to="/private">Private</Link>
            
            <Switch>
                <Route exact path="/">
                    <h1>Home page, anyone is allowed here</h1>
                </Route>
                <Route path="/private">
                    {
                        isLoggedIn ?
                        <h1>Protected page, must be logged in to be here</h1> :
                        <Redirect to="/login" /> //if we are not logged it, go to login page
                    }
                </Route>
                <Route path="/login">
                    <button onClick={() => setIsLoggedIn(true)}>Log in</button>
                </Route>
            </Switch>
        </div>
    )
}

export default App