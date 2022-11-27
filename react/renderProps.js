// You can pass a function into a component as a prop.
// This function can take paramenters and base on them reterns a value or JSX.
// That component can use for rendering.
// This way you can create a component (Toggler) with some logic and rendering UI for with a prop.

// Toggler.js
import React, {Component} from "react"

export default class Toggler extends Component {
    state = {
        on: this.props.defaultOnValue
    }
    
    static defaultProps = {
        defaultOnValue: true
    }
    
    toggle = () => {
        this.setState(prevState => {
            return {
                on: !prevState.on
            }
        })
    }
    
    render() {
        return (
            this.props.children(this.state.on, this.toggle)
        )
    }
}



//Menu.js
import React from "react"
import Toggler from "./Toggler"

export default function Menu(props) {
    return (
        <Toggler>{
            (on, toggle) => {
                return (
                    <div>
                        <button onClick={toggle}>{on ? "Hide" : "Show"} Menu </button>
                        <nav style={{display: on ? "block" : "none"}}>
                            <h6>Signed in as Coder123</h6>
                            <p><a>Your Profile</a></p>
                            <p><a>Your Repositories</a></p>
                            <p><a>Your Stars</a></p>
                            <p><a>Your Gists</a></p>
                        </nav>
                    </div>
                )
            }
        }</Toggler>
    ) 
}


//Favorite.js
import React, {Component} from "react"
import Toggler from "./Toggler"

export default function Favorite(props) {
    return (
        <Toggler defaultOnValue={false}>{
            (on, toggle) => {
                return (
                    <div>
                        <h3>Click heart to favorite</h3>
                        <h1>
                            <span 
                                onClick={toggle}
                            >
                                {on ? "❤️" : "♡"}
                            </span>
                        </h1>
                    </div>
                )
            }  
        }</Toggler>
    ) 
}




// Default props
// You can specify default props values, for properties that is used in a component.
// In a component:
static defaultProps = {
	defaultOnValue: false
}

// Or
Toggler.defaultProps = {
	defaultOnValue: false
}
