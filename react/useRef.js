// The useRef hook creates an object that will be returned during the all lifecicle of the component.
// It has a property current that is mutable. The property is initialized with passed value.
// Note, we don't get a notification when the current changes.
const refContainer = useRef(initialValue)

//Often we use useRef to store dom element.
//For this we should add ref attribute with the container.
function TextInputWithFocusButton() {
  const inputEl = useRef(null);
  
  const onButtonClick = () => {
    inputEl.current.focus();
  };
  return (
    <>
      <input ref={inputEl} type="text" /> // input container the dom input
      <button onClick={onButtonClick}>Focus the input</button>
    </>
  );
}


