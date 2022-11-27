// You can access components that was passed as children in your custom
// component with the props.children property
import React from "react"

function CTA(props) {
    return (
        <div className="border">
            {props.children}
        </div>
    )
}

export default CTA


import React from "react"
import CTA from "./CTA"

function App() {
    return (
        <div>
            <CTA position="right">
                <h1>This is an important CTA</h1>
                <button>Click me now or you'll miss out!</button>
            </CTA>
            
            <CTA>
                <form>
                    <input type="email" placeholder="Enter email address here"/>
                    <br />
                    <button>Submit</button>
                </form>
            </CTA>
        </div>
    )
}

export default App