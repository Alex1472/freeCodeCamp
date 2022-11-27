//To create class component, you should enherit it from React.components
//You can access prop as a member with this.props
export default class App extends React.Component {
    render() {
        return (
            <h1>{this.props.type} component</h1>
        )
    }
}


// To support state, you should declare it as a field with name state and its members
// You can update state with the setState method, you can specify only members to change, not all state
// You should use arrow functions to declare method that use this.setState method, otherway you can 
// lost this, when you pass this function in another function (as an event handler, for example)
import React from "react"

export default class App extends React.Component {
    state = {
        goOut: "Yes"
    }
    
    toggleGoOut = () => {
        this.setState(prevState => {
            return {
                goOut: prevState.goOut === "Yes" ? "No" : "Yes"
            }
        })
    }
    
    render() {
        return (
            <div className="state">
                <h1 className="state--title">Should I go out tonight?</h1>
                <div className="state--value" onClick={this.toggleGoOut}>
                    <h1>{this.state.goOut}</h1>
                </div>
            </div>
        )
    }
}



// In old fashion code you should declare state in a constructor
// Also you should bind your method to the object with the bind function
export default class App extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            goOut: "Yes"
        }
        this.toggleGoOut = this.toggleGoOut.bind(this)
    }
    
    toggleGoOut() {
        this.setState(prevState => {
            return {
                goOut: prevState.goOut === "Yes" ? "No" : "Yes"
            }
        })
    }
    
    render() {
        return (
            <div className="state">
                <h1 className="state--title">Should I go out tonight?</h1>
                <div className="state--value" onClick={this.toggleGoOut}>
                    <h1>{this.state.goOut}</h1>
                </div>
            </div>
        )
    }
}



// You shouldn't create new state object with properties from previous. 
// You can just specify properties that have changed.
state = {
	firstName: "John",
	lastName: "Doe",
	phone: "+1 (719) 555-1212",
	email: "itsmyrealname@example.com",
	isFavorite: false
}

toggleFavorite = () => {
	this.setState(prevContact => ({
		isFavorite: !prevContact.isFavorite
	}))
}

componentDidUpdate(prevProps, prevState) {
	console.log("Updated")
	if(prevState.count != this.state.count)
		this.getStarWarsCharacter(this.state.count)
}