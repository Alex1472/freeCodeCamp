// jQuery add handler for different events:
// click, dblclick, mousenter, mouseleave, mousedown, mouseup, hover

$(function() {
    $("p").click(function() {
        console.log("paragraph is clicked");
    });
    $("p").mouseenter(function() {
        console.log("mouse enter");
    });
    $("p").mouseleave(function() {
        console.log("mouse leave");
    });
});

// hover = mouseenter + mouseleave. It takes two functions.
$(function() {
    $("p").hover(function() {
        console.log("mouse enter");
    }, 
        function() {
            console.log("mouse leave");
    });
});



// Input events: focus, blur(focus leave), change(input text was changed and enter was clicked)
$(function() {
    $("input").focus(function() {
        console.log("focus");
    });
    $("input").blur(function() {
        console.log("unfocus");
    });
    $("input").change(function() {
        console.log("text changed");
    });
});


// ON
// You can subscribe on an event with on method.
$(function() {
   $("p").on("click", function() {
       console.log("click");
   });
});
// You can subscrive for several events using dictionary.
$(function() {
    $("p").on({
        "click": function() { console.log("click"); },
        "mouseenter": function() { console.log("mouseneter"); },
        "mouseleave": function() { console.log("mouseleave");}
    });
});

// OFF
//You can unsubscribe with off method.
$(function() {
    $("p").on("click", function() { console.log("click"); });
    $("button").on("click", function() { $("p").off("click"); });
});

// ONE
// You can subscribe, so that an event works just one time with one method.
$(function() {
    $("p").one("click", function() { console.log("click"); });
});