// Custom hooks is a js function that uses another custom hooks or built-in hooks.
// It invokation of a hook has its own state and it saves its state between rerenders.

//useCounter.js
import {useState} from "react"

function useCounter() {
    const [count, setCount] = useState(0)
    
    function increment() {
        setCount(prevCount => prevCount + 1)
    }
    
    return [count, increment]
}

export default useCounter


//App.js
import React, {useState} from "react"
import useCounter from "./useCounter"

function App() {
    const [number1, add1] = useCounter() //This is two different states
    const [number2, add2] = useCounter()
    
    return (
        <>
            <div>
                <h1>The count is {number1}</h1>
                <button onClick={add1}>Add 1</button>
            </div>
            <div>
                <h1>The count is {number2}</h1>
                <button onClick={add2}>Add 1</button>
            </div>
        </>
    )
}

export default App