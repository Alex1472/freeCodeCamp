// By default, react rerenders all children components, if state or props of a parent component
// have been changed.
// We want to avoid rerendering of child component if its state and props haven't been changed.


// ShouldComponentUpdate
// You can impolement the ShouldComponentUpdate method to say should component rerender.
// The method receives new props and new state
class GrandParent extends Component {  
    //We will rerender, only if the count property has changed.
    shouldComponentUpdate(nextProps, nextState) {
        if (nextProps.count === this.props.count) {
            return false
        }
        return true
    }
      
    render() {
        console.log("[👴🏼]   [ ]   [ ]   [ ] rendered")
        return (
            <div>
                <p>I'm a GrandParent Component</p>
                <Parent />
                <Parent />
            </div>
        )
    }
}


// PureComponent
// PureComponent did a shallow comporation of props and state is the shouldComponentUpdate method
// so you should not implement it
// You need just inherit it from PureComponent instead of Component

class GrandParent extends PureComponent {    
    render() {
        console.log("[👴🏼]   [ ]   [ ]   [ ] rendered")
        return (
            <div>
                <p>I'm a GrandParent Component</p>
                <Parent />
                <Parent />
            </div>
        )
    }
}


//React.memo
// memo is a high order component that create a component that support a shallow comporation
// - it works like a PureComponent
// - it only compare props
// - you can define custom comparation with a second parameter areEqual function

import React, {memo} from "react"

export default memo(function GrandChild() {
    console.log("[ ]   [ ]   [ ]   [👶🏻] rendered")
    return (
        <div>
            <p>I'm a GrandChild Component</p>
        </div>
    )
})


// Custom comparation
import React from "react"
import Parent from "./Parent"

function GrandParent(props) {    
    console.log("[👴🏼]   [ ]   [ ]   [ ] rendered")
    return (
        <div>
            <p>I'm a GrandParent Component</p>
            <Parent />
            <Parent />
        </div>
    )
}

// Custom comporation, return true is the props are equal. So you should not rerender the component.
function areEqual(prevProps, nextProps) {
  
}

export default React.memo(GrandParent, areEqual)


