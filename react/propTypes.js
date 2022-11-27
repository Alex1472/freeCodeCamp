// You can specify default value for a prop, and a range of valus

import PropTypes from "prop-types"

Button.propTypes = {
    theme: PropTypes.oneOf(["light", "dark"]) //them can be only light or dark
}

Button.defaultProps = {
    theme: "light"
}