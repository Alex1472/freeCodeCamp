// You can get a context value, not with consumer but with useContext hook
// You should just pass the context object into the hook and you will get value.
import {ThemeContext} from "./themeContext"

function Button(props) {
    const context = useContext(ThemeContext) // getting the context valued
    return (
        <button onClick={context.toggleTheme} className={`${context.theme}-theme`}>Switch Theme</button>
    )    
}
