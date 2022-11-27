// We want to provide data without using props

//First we need to create a context. It is a compound class.
//Its properties Provider and Consumer are components.
const ThemeContext = React.createContext()


//We should wrap element which children can access the context
//with ThemeContext.Provider
//We want all application has access to ThemeContext
//We should specify the value prop - the data we what to provide.
ReactDOM.render(
    <ThemeContext.Provider value={"light"}>
        <App />
    </ThemeContext.Provider>,
    document.getElementById("root")
)



//To get context you can specify a static property contextType.
//Then you can use the context property to get a value frop the provider.
import React, {Component} from "react"
import ThemeContext from "./themeContext"

class Button extends Component {
    render() {
        console.log(this.context) // getting the value from provider
        return (
            <button className={`${this.context}-theme`}>Switch Theme</button>
        )    
    }
}

Button.contextType = ThemeContext //specify context
export default Button



//You can get context value with Context.Consumer. It is a component that required child render prop.
// This function gets a context value.
function Button(props) {
    return (
        <ThemeContext.Consumer> // we return this component
            {theme => ( //theme is a context value 
                <button className={`${theme}-theme`}>Switch Theme</button>
            )}
        </ThemeContext.Consumer>
    )    
}


// In example above button always uses a theme.
// If you don't want this behavior, you can get theme from context earlier 
// And the pass theme into button
function App() {
    return (
        <div>
            <Header />
            <ThemeContext.Consumer>
                {theme => (
                    <Button theme={theme} /> //passing theme from context
                )}
            </ThemeContext.Consumer>
            
            <Button theme="light" /> //hardcoding a light theme
        </div>
    )
}




// We should create our own provider to give ability to change context value
import React, { Component } from "react"
const { Provider, Consumer } = React.createContext()

class ThemeContextProvider extends Component {
    state = {
        theme: "light"
    }
    
    toggle = () => {
        this.setState(prevState => ({
            theme: prevState.theme === "light" ? "dark" : "light"
        }))
    }
    
    render() {
        return (
            <Provider value={{theme: this.state.theme, toggle: this.toggle}}>
                {this.props.children}
            </Provider>
        )
    }
    
}

export { ThemeContextProvider, Consumer as ThemeContextConsumer }


//Now we should use ThemeContextProvider and ThemeContextConsumer
import React from "react"
import ReactDOM from "react-dom"

import App from "./App"
import {ThemeContextProvider} from "./themeContext"

ReactDOM.render(
    <ThemeContextProvider>
        <App />
    </ThemeContextProvider>, 
    document.getElementById("root")
)

// We use consumer to get themeContext
import React from "react"
import PropTypes from "prop-types"
import {ThemeContextConsumer} from "./themeContext"

function Button(props) {
    return (
        <ThemeContextConsumer>
            {context => (
                <button className={`${context.theme}-theme`} onClick={context.toggle}>Switch Theme</button>
            )}
        </ThemeContextConsumer>
    )    
}

export default Button


import React, {Component} from "react"
import {ThemeContextConsumer} from "./themeContext"

function Header(props) {
    return (
        <ThemeContextConsumer>
            {context => (
                <header className={`${context.theme}-theme`}>
                    <h2>{context.theme === "light" ? "Light" : "Dark"} Theme</h2>
                </header>
            )}
        </ThemeContextConsumer>
    )    
}

export default Header