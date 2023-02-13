// You should wrap you code in a script in a ready function
// so that it runs only when the document is loaded (dom created), css, js, images loaded
$(document).ready(function () {
	// some code
    $("#chl").click(function () {
        $("#item").append("<div>Last</div>");
        $("#item").prepend("<div>First</div>");
    });

    $("#sbl").click(function () {
        $("#item").before("<div>Before</div>");
        $("#item").after("<div>After</div>");
    });
});

// Get text and html-code inside the element with text and html methods.
$(function() {
    $("#btn").click(function() {
        console.log($("#test").text());
        console.log($("#test").html());
    })
});

// Get attribute value with attr method.
$(function() {
    $("#btn").click(function() {
        console.log($("#fcc").attr("href"));
    })
});

// Set text and html-code with text and html methods.
$(function() {
    $("#btn").click(function() {
        $("#test").text("FreeCodeCamp is awesome!");
    })
});
$(function() {
    $("#btn").click(function() {
        $("#test").html("FreeCodeCamp is <strong>awesome!</strong>");
    })
});

// Get and set value with val method.
$(function() {
    $("#btn").click(function() {
        $("#name").val("another text");
    });
});

// text, html, val function come with callback overlloads.
// First argument is index in list of suitable elements, second - old value. It return new value.
$(function() {
    $("#btn").click(function() {
        $("p").text(function(i, oldText) {
            return "Old text: " + oldText + "index: " + i;
        });
    });
});

