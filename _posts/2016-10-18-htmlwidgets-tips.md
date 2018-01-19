---
layout: post
title: "How to write a useful htmlwidgets in R: tips and walk-through a real example"
tags: [professional, rstats, r-bloggers, shiny, tutorial]
date: 2016-10-18 11:05:00 -0700
permalink: /blog/htmlwidgets-tips/
layout: post
comments: true
show-share: true
gh-repo: daattali/timevisBasic
gh-badge: [star, watch, follow]
---

I'd like to share some tips and recommendations on building htmlwidgets, based on my own learning experience while creating [`timevis`](https://github.com/daattali/timevis). These tips are mostly concerned with making your htmlwidget more useful and user friendly. In this post, every tip I provide will be followed by an actual code walk-through so that you can see it applied in a real example. 

Keep in mind that two weeks ago<sup><a href="#footnote">*</a></sup> I was still an htmlwidgets virgin, so I should in no way be considered an authority figure on the subject. But I did spend a long time trying to solve all sorts of problems I ran into and I ended up making an htmlwidget that has a lot of useful functionality that isn't documented anywhere, so I'm hoping that some of the things I learned can be useful for others as well.

The last two tips (7 and 8) are by far the most useful, but I kept them at the end because they're also the most complex.

## Table of contents

- [Background](#background)
- [Starting point for this "tutorial"](#starting-point)
- [Tip 1: Use sensible parameter name in `renderValue(x)` instead of `x`](#rendervalue-x-name)
- [Tip 2: Add custom HTML to the widget](#custom-html)
- [Tip 3: Convert a data frame to a D3-compatible data structure in R](#dataframeToD3)
- [Tip 4: The `renderValue()` function gets called every time the R binding function gets called](#rendervalue-multiple-times)
- [Tip 5: Make sure any one-time initialization code gets run only once](#init-once)
- [Tip 6: Find out if the widget is in a shiny app or not with `HTMLWidgets.shinyMode`](#shinyMode)
- [Tip 7a: Pass data from the widget to R](#widget-to-r-data)
  - [Tip 7b: Use a custom function to convert the JavaScript data into an R object](#javascript-to-r-handler)
- [Tip 8: Add API functions to lets users programmatically interact with the widget](#api)
  - [Tip 8a: Basic implementation](#api-basic)
  - [Tip 8b: Abstracting the code to make it easier to add new API functions](#api-abstract)
  - [Tip 8c: Support chaining API functions with pipes (`%>%`)](#api-chain)
  - [Tip 8d: Make API functions work outside of Shiny too](#api-not-just-shiny)

## Background {#background}

For those who don't know, [`htmlwidgets`](http://www.htmlwidgets.org/) is a handy package that lets you bring functionality from 3rd-party JavaScript libraries into R. This can be especially useful when wanting to use new types of visualizations in Shiny apps. 

Last year, when asked a question about htmlwidgets, [my response](https://github.com/timelyportfolio/sweetalertR/issues/1#issuecomment-123564079) was that *"I'm a bit embarrassed to admit that I haven't actually looked into htmlwidgets yet"*. Almost a year later, in useR 2016 conference, I had a chat with [Ramnath Vaidyanathan](https://github.com/ramnathv). And, again, I had to embarrassingly admit that I still don't really know how htmlwidgets works, but this time I had the added impact of saying it directly to one of the package's co-authors. In that moment I decided that it's time for me to learn htmlwigets.

So a few weeks later I set out to write my first htmlwidget. Even though it was meant to be purely a learning exercise, I wanted it to at least be minimally useful so that it's not a waste of time. I started looking at a ton of JavaScript visualization libraries and checked each one to see if it already exists in R. This was a harder task than I imagined, but I eventually deicded to turn the [vis.js Timeline module](http://visjs.org/docs/timeline/) into an R htmlwidgets, and hence my newest package [timevis](https://github.com/daattali/timevis) was born.

I'm not going to cover the basics of how to write htmlwidgets (if you just want to learn how to write htmlwidgets, the [official documentation](http://www.htmlwidgets.org/develop_intro.html) is great). What this post will do is describe some things that I implemented in timevis that go beyond the basics and that I think can be applied to many htmlwidgets in order to make them much more useful. To help make these ideas more concrete, I created a minimal barebones htmlwidget that we will improve upon throughout this post.

I would like to repeat and stress that I'm very new to htmlwidgets myself so it's entirely possible that people smarter than myself would disagree with some of my ideas. But I still think it's useful to go through this list I made and see if any of my lessons make sense in your case.

## Starting point for this "tutorial" {#starting-point}

I created a GitHub repository with a minimal functioning `timevis` htmlwidget called [`timevisBasic`](https://github.com/daattali/timevisBasic). Throughout this tutorial, I will give you tips on how to improve htmlwidgets, and we will see them in action by incrementally adding those ideas to `timevisBasic`.

I recommend you take a quick look at `timevisBasic` before continuing to see what our starting point is. At the very least you should read the [README](https://github.com/daattali/timevisBasic#readme) and just take a glance at the very simple [R source file](https://github.com/daattali/timevisBasic/blob/master/R/timevisBasic.R) and [JavaScript source file](https://github.com/daattali/timevisBasic/blob/master/inst/htmlwidgets/timevisBasic.js).

Just to be clear: `timevis` is my proper package that you can install and use, `timevisBasic` is a stripped down version of `timevis` that's only on GitHub is used as a learning tool.

## Tip 1: Use a sensible parameter name in `renderValue(x)` instead of `x` {#rendervalue-x-name}

I'll start with a very simple tip. The htmlwidgets docs tell you to define the `renderValue` JavaScript function as

```javascript
renderValue: function(x) { ... }
```

Now, this `x` variable is actually very useful. It contains all the values that are passed in from R to the widget itself. So you often end up referring to `x` a lot in your code.  I have [a commit](https://github.com/daattali/timevis/commit/7bd3e7390256a7617e3642078a94d301062ab628) that has the sole purpose of renaming `x` to `opts`, and I think you should do that too, for two main reasons. First, `opts` is a much more descriptive name than `x` and it tells you that it holds all the different widget options. Second, I'm always against using any variable name that is only one or two characters long, because it makes it very difficuly to search for it.

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip1-rendervalue-x-name#diff).

## Tip 2: Add custom HTML elements to your widget {#custom-html}

The official htmlwidgets documentation [tells us](http://www.htmlwidgets.org/develop_advanced.html#custom-widget-html) that the widget will be "housed" inside a `<div>` tag by default. You can define a special function if you want the widget to be enclosed by a different tag, for example

```r
timevis_html <- function(id, style, class, ...){
  tags$span(id = id, class = class)
}
```

This will change the enclosing tag of the widget to be a `<span>` instead.

But what's more interesting is that you can also use this idea to add HTML elements that are not part of the JavaScript library into the widget. For example, I wanted to add zoom in and zoom out buttons into my timevis widget (the 3rd-party JavaScript library I use does not have zooming buttons) so I added this R function:

```r
timevis_html <- function(id, style, class, ...) {
  tags$div(
    id = id, style = style, class = class,
    htmltools::div(
      style = "position: absolute;",
      htmltools::tags$button("+"),
      htmltools::tags$button("-")
    )
  )
}
```

You can see the actual final code I used in `timevis` [here](https://github.com/daattali/timevis/blob/v0.2/R/timevis.R#L439-L459).

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip1-rendervalue-x-name...tip2-custom-html#diff).

## Tip 3: Convert a data frame to a D3-compatible data structure in R {#dataframeToD3}

As the htmlwidgets docs [point out](http://www.htmlwidgets.org/develop_advanced.html#htmlwidgets.dataframetod3), R data frames are represented in "long" form (an array of named vectors) whereas D3 often requires data to be in "wide" form (an array of objects each of which includes all names and values). In order to transform an R data frame to d3 format, htmlwidgets provides us with the `HTMLWidgets.dataframeToD3()` JavaScript function. This should usually be good enough, but in my particular case it didn't work quite right because it converts any `NA` values in the data frame into `null` values in JavaScript, which presented errors with the specific library I'm using. My solution was to implement my own `dataframeToD3()` function in R:

```r
dataframeToD3 <- function(df) {
  if (missing(df) || is.null(df)) {
    return(list())
  }
  if (!is.data.frame(df)) {
    stop("timevis: the input must be a dataframe", call. = FALSE)
  }
  
  row.names(df) <- NULL
  apply(df, 1, function(row) as.list(row[!is.na(row)]))
}
```

This function takes in a regular R data frame and converts it to a format that is compatible with D3, and it specifically drops any `NA` values. You can see it in use in timevis [here](https://github.com/daattali/timevis/blob/v0.2/R/timevis.R#L314); I simply convert any input data to wide format using this function, and pass that data to JavaScript.

Since the d3 representation of a data frame is much more verbose, this results in bigger data to transfer over the network, so I don't generally recommend doing this transformation in R unless you really need to (as in my case, where using the JavaScript method provided by HTMLWidgets was not sufficient).

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip2-custom-html...tip3-dataframeToD3#diff).

## Tip 4:  The `renderValue()` function gets called every time the R binding function gets called {#rendervalue-multiple-times}

The `renderValue()` function, which is responsible for creating the JavaScript widget based on some data that was passed in from R, gets called every time the R binding function is called. For example, in my package whenever `timevis()` is called in R, the `renderValue()` function is called in JavaScript. This may sound trivial, but I didn't see it explicitly spelled out in the documentation and I think it's important to understand.

What this means is that if you create your widget based on a reactive value in a shiny app, then every time the reactive value updates, the widget's `renderValue()` will be called again. It might not be a problem for you, but it's good to be aware of that because you should make a conscious decision on what happens when the widget's "initialization" code (`renderValue()`) is run multiple times.

For example, suppose we want to render a timevis widget and use a dynamic height based on an input control in Shiny. You might use the following snippet in the shiny server code:

```r
data <- data.frame(...)
output$vis <- renderTimevis({
  timevis(data, height = input$height)
})
```

There are two things that can happen every time `timevis()` is called: either the given data is *added* to the current widget, or it *replaces* the current widget.

#### 1. Add new data to the widget

Using the following JavaScript code, every time the height input changes, more data will be added to the widget.

```javascript
factory : function(el, width, height) {
  var timeline = new vis.Timeline(document.getElementById(el.id), [], {});
  
  return {
    renderValue: function(opts) {
      timeline.itemsData.add(opts.items);
    }
  }
}
```

#### 2. Replace existing widget data with new data

When I first made `timevis`, I didn't consciously think about what happens when the render function is called multiple times, and I implemented the previous code. This resulted in [a bug](https://github.com/daattali/timevis/issues/3) where every time the widget got re-rendered, the old data was not removed. Of course the fix was simple, in my case all I had to do was clear the current data from the widget before adding the new data. I simply added `timeline.itemsData.clear();` to the beginning of the `renderValue` function.  Of course the exact code you'll have to run will depend on the JavaScript library you're bringing into R, but hopefully you get the idea.

In some visualizations it might make more sense to keep the old data and just add new data, so you should do whatever makes the most sense for your library.

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip3-dataframeToD3...tip4-rendervalue-multiple-times#diff).

## Tip 5: Make sure any one-time initialization code gets run only once {#init-once}

The widget initialization code should live in the `renderValue()` JavaScript function. Since that function can get called multiple times (as explained above), it's a good idea to ensure that as many parts as possible are only initialized once instead of getting defined every single time the function gets called.

For example, in `timevis` I have some buttons (zoom buttons as mentioned [above](#custom-html)) that need to register click listeners, but that should only happen once. In order to do that, I define a boolean flag `initialized`, set it to `true` the first time the widget code runs, and only run initialization code if it's `false`. Code example:

```javascript
factory : function(el, width, height) {
  var initialized = false;
  
  return {
    renderValue: function(opts) {
      if (!initialized) {
        initialized = true;
        // Code to set up event listeners and anything else that needs to run just once
      }
    }
  }
}
```

You can see this code pattern in action [in timevis](https://github.com/daattali/timevis/blob/v0.2/inst/htmlwidgets/timevis.js#L26-L38).

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip4-rendervalue-multiple-times...tip5-init-once#diff).

## Tip 6: Find out if the widget is in a shiny app or not with `HTMLWidgets.shinyMode` {#shinyMode}

htmlwidgets can be rendered in a few different contexts: they can be embedded in Rmarkdown documents, viewed as a standalone in RStudio or in a browser, or they can be included in Shiny apps. It may be beneficial to know whether the widget is currently inside a shiny app or not (for example, if you want the widget to interact with Shiny). You can access the variable `HTMLWidgets.shinyMode` in JavaScript code to find out if the current widget is in a Shiny context or not.

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip5-init-once...tip6-shinyMode#diff).

## Tip 7a: Pass data from the widget to R {#widget-to-r-data}

The native flow of information in an htmlwidget is from R to the widget. The widget cannot natively pass information back to R. But when you include an htmlwidget inside a Shiny app, you might want to be able to access some data from the widget in your Shiny app. This can be done using the typical method of passing information from JavaScript to R using `Shiny.onInputChange()`. If you're not familiar with this method, you can learn about it [in my previous post](https://github.com/daattali/advanced-shiny/tree/master/message-javascript-to-r).

This usually involves setting up some event listener in JavaScript for an event that you may be interested in, calling `Shiny.onInputChange()` in the listener callback, and listening for that data in R through the `input` variable. For example, in `timevis` I wanted the user to know what items have been selected in the widget, so I added this simple code:

```javascript
timeline.on('select', function (properties) {
  Shiny.onInputChange(elementId + "_selected", properties.items);
});
```

What that code does is: every time an item gets selected (`timeline.on('select', ...)`), we pass the data (`properties.items`) to R, and it will be accessible as `input$<elementId>_selected`. For completeness, I also pass this data to R when the app first starts, so that the user will be able to access that variable even before any selection is being made:

```javascript
Shiny.onInputChange(elementId + "_selected", timeline.getSelection());
```

You can see this code in the `timevis` source code [here](https://github.com/daattali/timevis/blob/v0.2/inst/htmlwidgets/timevis.js#L43-L53). You'll also notice that I wrapped this code inside a check for `if (HTMLWidgets.shinyMode) {...}` since it only makes sense to pass data back to R when you're in a Shiny app.

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip6-shinyMode...tip7a-widget-to-r-data#diff).

### Tip 7b: Use a custom function to convert the JavaScript data into an R object {#javascript-to-r-handler}

This is actually purely a Shiny issue rather than an htmlwidgets issue (you can learn more about it in my [previous post](https://github.com/daattali/advanced-shiny/tree/master/javascript-to-r-handler)), but it can come up when developing a widget so I will mention it here.

When using `Shiny.onInputChange(name, data)`, you are passing in a JavaScript object (`data`) and expect it to get converted to an R object (`input$name`). This conversion happens by serializing and deserializing the data to and from JSON. Usually `input$name` will look exactly like you'd expect it to, but it is possible for the conversion process to not do exactly what you want. Alternatively, you may just want to alter the data slightly in R before presenting it to Shiny.

This is where the `shiny::registerInputHandler()` function comes in: it allows you to transform the data passed in from JavaScript before it gets used as `input$`. For example, suppose you use `Shiny.onInputChange("myobj", value)` to send a value from JavaScript to R, but you want `input$myobj` to be automatically converted into a list. There are two simple steps you'd need to follow:

1. In JavaScript, change `Shiny.onInputChange("myobj", value)` to `Shiny.onInputChange("myobj:mylist", value)`. Notice that we append `:<type>` to the name of the object. This specifices the type of object that is being passed to Shiny, so that Shiny will know what handler to call when deserializing its value.

2. In R, define the following function:

  ```r
  shiny::registerInputHandler("mylist", function(data, ...) {
    list(data)
  }, force = TRUE)
  ```

Now if you access `input$myobj` in shiny, the value will be wrapped in a list. Of course this particular example isn't terribly useful, but this principle can be applied in real apps. For example, you can see how [I used it](https://github.com/daattali/timevis/blob/v0.2/R/utils.R#L2-L7) in `timevis` to fix an issue where the timeline data I wanted to pass to Shiny was getting flattened to a vector by default and I wanted it to be a data.frame instead. You'll also see that I put that code inside `.onLoad`, which I'm not completely sure is the correct place for that code, so let me know if you have any thoughts on that.

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip7a-widget-to-r-data...tip7b-javascript-to-r-handler#diff).

## Tip 8: Add API functions to lets users programmatically interact with the widget {#api} 

I saved this one for last because it's the most involved suggestion, but it's also the one that will give your htmlwidget the greatest benefit.

You can make a decent htmlwidget by only implementing the JavaScript `renderValue()` function, but you can make a much more useful widget by also allowing the user to interact with the widget programmatically after its creation. Most JavaScript libraries provide some sort of a constructor function that builds a widget (this is what you'd call inside `renderValue()`), as well as other functions that let you interact with the widget (aka an API). I think it's always a good idea to give the user an interface to this API, as it will make your widget more powerful.

The exact way you'd implement an API to your widget depends on the API available by the JavaScript library you're using, but I believe I came up with a good generic framework for adding API functions that can be used by other widgets.

### Tip 8a: Basic implementation {#api-basic}

Let's walk through implementing a real API function in a real htmlwidget (we'll use `timevisBasic` of course, which closely mimics the real `timevis` package). If you look at the [documentation for vis.js](http://visjs.org/docs/timeline/#Methods) (which is the JavaScript library I used to port the timeline widget), you'll see there are many API functions available to manipulate an existing timeline widget. Let's pick one of them at random, say `setWindow()`, and see how we can add a `setWindow()` function to our R htmlwidget package.

Notice that `setWindow()` has two required arguments `start` and `end`, and one optional argument `options`. Essentially what we want to do is be able to call a function in R, supply it with those arguments as well as an ID (the widget ID), and have it call the library's `setWindow()` method. We will rely on the system that Shiny provides for passing data from R to JavaScript using `sendCustomMessage()`; if you aren't familiar with that function, you can learn about it in [my previous post about it](https://github.com/daattali/advanced-shiny/tree/master/message-r-to-javascript).

#### R code

The first thing we need to do is define an R function that accepts those four parameters and passes them to JavaScript. I'll use `timevis:setWindow` as the name of the message - you don't have to namespace the messages like that, but I prefer to do that in order to minimize the chance of having name collisions.

```r
setWindow <- function(id, start, end, options) {
  message <- list(id = id, start = start, end = end)
  if (!missing(options)) {
    message['options'] <- options
  }
  session <- shiny::getDefaultReactiveDomain()
  session$sendCustomMessage("timevis:setWindow", message)
}
```

#### JavaScript code

Now we move on to the JavaScript code. First we need to have a way to access the widget JavaScript object from anywhere arbitrary in the JavaScript, so that we can refer to it after its creation. Recall that earlier we defined a `timeline` variable as the widget:

```javascript
var timeline = new vis.Timeline(document.getElementById(el.id), [], {});
```

This `timeline` object is the actual widget, and is the object we call the API methods (such as `setWindow()`) on. Therefore, we need to have a way to access this widget from anywhere arbitrarily in the JavaScript code. In order to make it easily available, we can attach it to the HTML element housing the widget by placing the following line inside `renderValue()`:  

```javascript
document.getElementById(elementId).timeline = timeline;
```

Since that attachment only needs to happen once per widget, you can place it inside the initialization code (as described [above](#init-once)). What this line of code achieves is that now we can access the timeline widget associated with a specific HTML ID using `document.getElementById(id).timeline`.

Now that we have an R function that sends a JavaScript message with the arguments, and we have a reference to the timeline widget, all that is left to do is implement the JavaScript function that listens for the message coming from Shiny and make the API call: 

```javascript
if (HTMLWidgets.shinyMode) {
  Shiny.addCustomMessageHandler("timevis:setWindow", function(message) {
    var el = document.getElementById(message.id);
    if (el) {
      el.timeline.setWindow(message.start, message.end, message.options);
    }
  });
}
```

Now you have a working basic implementation of an API function. You can call `setWindow()` in Shiny, and it will find the appropriate htmlwidget and call its `setWindow()` method.

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip7b-javascript-to-r-handler...tip8a-api-basic#diff).

### Tip 8b: Abstracting the code to make it easier to add new API functions {#api-abstract}

The code above works just fine, but if you want to start adding support for more API methods, there is going to be a lot of code duplication. So let's see how we can generalize this code so that we have minimal work to do when we want to add new API functions.

#### R code

On the R side, we can rewrite the `setWindow` function as follows to be more generic:

```r
setWindow <- function(id, start, end, options) {
  # Define the API method name (specific to this function)
  method <- "setWindow"
  
  # Get the parameters and pass all the info to JavaScript (non-specific code)
  message <- Filter(function(x) !is.symbol(x), as.list(environment()))
  session <- shiny::getDefaultReactiveDomain()
  method <- paste0("timevis:", message$method)
  session$sendCustomMessage(method, message)
}
```

You can compare this version to the previous version of the function, and hopefully you'll see that the result is identical. The one line there that might be confusing is the one with `Filter()`; basically, it looks at all the objects in the current environment and adds them all to a list, but it filters out any argument that was not given a value. As you can see, only the first line of code `method <- "setWindow"` is specific to this function, while the rest of the code is completely generalized. This means we can now pull out all this code into a separate function, let's call it `callJS()`:

```r
callJS <- function() {
  message <- Filter(function(x) !is.symbol(x), as.list(parent.frame(1)))
  session <- shiny::getDefaultReactiveDomain()
  method <- paste0("timevis:", message$method)
  session$sendCustomMessage(method, message)
}
```

And now our API function simply becomes

```r
setWindow <- function(id, start, end, options) {
  method <- "setWindow"
  callJS()
}
```

Now we have a very simple way to add new API functions since most of the heavy lifting is done by `callJS()`. For example, if we wanted to add support for the `addCustomTime()` method of the JavaScript library, the R code would only need the following simple function added:

```r
addCustomTime <- function(id, time, itemId) {
  method <- "addCustomTime"
  callJS()
}
```

#### JavaScript code

So we have a nice generic system for adding API functions on the R side, but we should also try to abstract the process as much as possible on the JavaScript side.

The idea here is that instead of manually defining all the message handlers, we can have some generic code that takes care of defining all the message handlers. Each message handler should call an appropriate method on the htmlwidget that will know what to do for its message type (message type = API method). This will solve another problem as well: remember that in the basic implementation we attached the `timeline` widget to the HTML element of the htmlwidget? It can be considered bad programming practice to expose the widget directly because then anyone can manipulate it freely. Instead, it's safer to only expose a pre-defined set of functions that we want to allow the user to call.

So what we'll do is instead of making the raw `timeline` object available through the HTML element, we'll only make a reference to the htmlwidget available, and we'll add API functions to the htmlwidget.

To add a reference to the widget from the HTML element, add the following line of code to the initialization code inside `renderValue()`:

```javascript
document.getElementById(elementId).widget = this;
```

Next we add the API function (`setWindow()` and `addCustomTime()` in this case) to the return value of the htmlwidget:

```javascript
factory : function(el, width, height) {
  return {
    renderValue: function(opts) {
      ...
    },
    setWindow : function(params) {
      timeline.setWindow(params.start, params.end, params.options);
    },   
    addCustomTime : function(params) {
      timeline.addCustomTime(params.time, params.itemId);
    }
  }
}
```

And lastly we generalize the code to add message handlers:

```javascript
if (HTMLWidgets.shinyMode) {
  var fxns = ['setWindow', 'addCustomTime'];

  var addShinyHandler = function(fxn) {
    return function() {
      Shiny.addCustomMessageHandler(
        "timevis:" + fxn, function(message) {
          var el = document.getElementById(message.id);
          if (el) {
            el.widget[fxn](message);
          }
        }
      );
    }
  };

  for (var i = 0; i < fxns.length; i++) {
    addShinyHandler(fxns[i])();
  }
}
```

Whenever we want to add a new API function, we now just add the function to the htmlwidget return value, and add its name in the `fxns` variable in the above code chunk.

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip8a-api-basic...tip8b-api-abstract#diff).

### Tip 8c: Support chaining API functions with pipes (`%>%`) {#api-chain}

Now that we made it easy to add many API functions, we also wan to make it easy to *call* multiple API functions together, one after the other. The natural way many people will want to do that is by chaining functions together using the magrittr `%>%` pipe. For example, instead of running

```r
addCustomTime("timeline", time = Sys.Date)
setWindow("timeline", start = Sys.Date() - 1, end = Sys.Date() + 1)
```

It can be easier to type

```r
"timeline" %>%
  addCustomTime(time = Sys.Date()) %>%
  setWindow(start = Sys.Date() - 1, end = Sys.Date() + 1)
```

Right now, the first version will work, but the second version will not. To support it is actually pretty easy though, all we have to do is make sure each API function accepts the ID as its first argument (we're already doing that), and make sure the API functions also *return* that same ID as their value.

#### R code

Simply add the following return call at the end of the `callJS()` function:

```r
return(message$id)
```

And that's it! No JavaScript changes. Now you can chain your API functions (assuming, of course, that you have the magrittr pipe loaded). 

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip8b-api-abstract...tip8c-api-chain#diff).

### Tip 8d: Make API functions work outside of Shiny too {#api-not-just-shiny}

Right now, calling API functions is only possible within Shiny apps, using the widget's ID after the widget has been created. It would be great if we could call an API function on the widget during initialization, such as `addCustomTime(timevis(), time = Sys.Date)`. There are two benefits to this syntax: first of all, it would allow us to call API functions on the widget immediately during initialization. Secondly, it would allow us to use API functions on the widget even when we're not using Shiny (for example, in an R-markdown document or in the R console).

The problem is that we can't simply try to call the API function on an htmlwidget before it's created, because the function would simply not return anything since the widget takes time to initialize. We need to somehow delay the API functions to only run after the widget initializes. And here's how we can do that: when an API function gets called on an htmlwidget, instead of trying to run it immediately, we can simply save it as a property of the widget. Then when the widget gets created, it can look at what API functions were called on it and are in queue, and run them one by one. Simple!

#### R code

We need to have some sort of list attached to the htmlwidget that will keep track of all API requests. In the `timevis()` function, where we bundle together all the data and send it to JavaScript, add the following simple line:

```r
x$api <- list()
```

Now we have a place to store any future API function requests. Remember that the goal is to be able to call an API function directly using an htmlwidget, rather than using an ID. So the next thing we need to do is allow the API functions to be called with an htmlwidget as the first parameter instead of an ID (which is what is currently expected). Recall that all API function calls go through the `callJS()` function, so add the following few lines to that function, after the `session` variable gets defined:

```r
if (methods::is(message$id, "timevisBasic")) {
  widget <- message$id
  message$id <- NULL
  widget$x$api <- c(widget$x$api, list(message))
  return(widget)
}
```

Hopefully you can understand what that code does: it looks to see if the first parameter (`id`) is actually an object of type `timevisBasic`, which indicates it's an htmlwidget. If it is, then instead of calling JavaScript immediately, simply save all the request info (the API function's name and the arguments) into the special list we defined earlier. Notice that we also return the widget, which is important if we want to be able to chain API calls (eg. `timeline() %>% setWindow(...) %>% addCustomTime(...)` vs `addCustomTime(setWindow(timeline(), ...))`).

The one thing I don't love about this solution is that we're overloading the `id` parameter so that it actually accepts two very different types of inputs. In an ideal world this would not happen, but in practice this allows us the great flexibility of calling an API function in any way we want.

#### JavaScript code

The last piece of the puzzle is actually calling the API functions once the widget gets created. If we go back to the `renderValue()` function, that's where we get all the information from R about what the widget should contain, and that's where we initialize the widget. So all we need to do is check if there are any outstanding API function calls, loop through them, and call them one by one. This can actually be done in a very simple way, just add the following code to the end of the `renderValue()` function:

```javascript
// Now that the timeline is initialized, call any outstanding API
// functions that the user wantd to run on the timeline before it was
// ready
var numApiCalls = opts['api'].length;
for (var i = 0; i < numApiCalls; i++) {
  var call = opts['api'][i];
  var method = call.method;
  try {
    this[method](call);
  } catch(err) {}
}
```

Now you can call API functions using an ID or an htmlwidget object, and you can even chain calls easily. I think this solution is failry simple and elegant, and can easily be implemented in other htmlwidget packages. 

[See the implementation of this tip in `timevisBasic`](https://github.com/daattali/timevisBasic/compare/tip8c-api-chain...tip8d-api-not-just-shiny#diff).

---

That's all the advice I can give based on what I learned making my first htmlwidget. Hopefully now if you look at [the JavaScript code](https://github.com/daattali/timevis/blob/v0.2/inst/htmlwidgets/timevis.js) for the `timevis` widget, you'll understand everything that I did there.

As always, feel free to [contact me](/contact). Any comments and feedback are appreciated, especially if any of my code or ideas seem strange.

<span id="footnote"></span>_*It was actually a couple months ago. I started writing this tutorial one week after releasing `timevis` but only wrote the accompanying `timevisBasic` package and the rest of the article several weeks later._
