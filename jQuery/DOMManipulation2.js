// append, prepend methods and elements as last and first child of the selected element.
// before, after add element as before and after the selected element.

<div class="container">
	<p>Some text</p>
	<button id="btn">Test</button>
	<div class="item">Item</div>
	<p>Another text</p>
</div>

$(function() {
    $("#btn").click(function() {
        $(".item").append('<div class="child">Child element</div>');
        $(".item").prepend('<div class="child">Child element</div>');
        $(".item").before('<div class="sibling">Sibling element</div>');
        $(".item").after('<div class="sibling">Sibling element</div>');
    });
});

// You can use chane rule:
$(function() {
    $("#btn").click(function() {
        $(".item")
        .append('<div class="child">Child element</div>')
        .prepend('<div class="child">Child element</div>')
        .before('<div class="sibling">Sibling element</div>')
        .after('<div class="sibling">Sibling element</div>');
    });
});

// Remove selected elements with remove method:
$(".item").remove();

// You can remove only the elements that have a particular class:
$("p").remove(".filter"); //removes only p with filter class

// Remove all children with empty method.
$(".container").empty();