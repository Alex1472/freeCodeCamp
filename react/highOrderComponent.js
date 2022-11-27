// High Order Component is a function that accepts a component and returns another
// component (functional component(function)), that adds some behavior to the origin
// component. To apply this behavior you should wraps your component.



import React from "react"

export function withExtraPropAdded(component) { //High order component, usually name begins from "with"
    const Component = component
    return function(props) {
        return (
            <Component {...props} anotherProp="Bla blah blah" /> // {...props} - passes wrapper props into origin component
        )
    }
}


import React from "react"
import {withExtraPropAdded} from "./withExtraPropAdded"

function App(props) {
    return (
        <div>Hello world!</div>
    )
}

const ExtraPropComponent = withExtraPropAdded(App) //wrap origin component
export default ExtraPropComponent