// SUSPENCE COMPONENT
// show fallback component instead of loading component in case of lazy loading
// REACT.LAZY
// load component dinamically

// This component is loaded dynamically
const OtherComponent = React.lazy(() => import('./OtherComponent'));

function MyComponent() {
  return (
    // Displays <Spinner> until OtherComponent loads
    <React.Suspense fallback={<Spinner />}>
      <div>
        <OtherComponent />
      </div>
    </React.Suspense>
  );
}



// ERROR BOUNDARIES COMPONENT
// You should implement the getDerivedStateFromError method to update state to indicate that there is an error
// componentDidCatch to log an error 
class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError(error) {
    // Update state so the next render will show the fallback UI.
    return { hasError: true };
  }

  componentDidCatch(error, errorInfo) {
    // You can also log the error to an error reporting service
    logErrorToMyService(error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      // You can render any custom fallback UI
      return <h1>Something went wrong.</h1>;
    }

    return this.props.children; 
  }
}



// REACTDOM.CREATEPORTAL
// The first argument (child) is any renderable React child, such as an element, string, or fragment. The second argument (container) is a DOM element.
render() {
  // React does *not* create a new div. It renders the children into `domNode`.
  // `domNode` is any valid DOM node, regardless of its location in the DOM.
  return ReactDOM.createPortal(
    this.props.children,
    domNode
  );
}



// USELAYOUTEFFECT
// This runs synchronously immediately after React has performed all DOM mutations. This can be useful if you need to make DOM measurements (like getting the scroll 
// position or other styles for an element) and then make DOM mutations or trigger a synchronous re-render by updating state.

// As far as scheduling, this works the same way as componentDidMount and componentDidUpdate. 
// Your code runs immediately after the DOM has been updated, but before the browser has had a chance to "paint" 
// those changes (the user doesn't actually see the updates until after the browser has repainted).

react reconciliation



// USECALLBACK
//It only recreates the function when dependency array changes.
//Pass an inline callback and an array of dependencies. 
//useCallback will return a memoized version of the callback that only changes if one of the dependencies has changed. 
//This is useful when passing callbacks to optimized child components that rely on reference equality to prevent unnecessary renders (e.g. shouldComponentUpdate).
const memoizedCallback = useCallback(
  () => {
    doSomething(a, b);
  },
  [a, b],
);
//Returns a memoized callback.



// USEMEMO
// Returns a memoized value.
// Pass a “create” function and an array of dependencies. useMemo will only recompute the memoized value 
// when one of the dependencies has changed. This optimization helps to avoid expensive calculations on every render.
const memoizedValue = useMemo(() => computeExpensiveValue(a, b), [a, b]);



//USECALLBACK with useMemo
const handleMegaBoost = React.useMemo(() => {
  return function() {
    setCount((currentValue) => currentValue + 1234);
  }  
}, []);