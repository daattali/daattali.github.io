---
layout: post
title: "test"
tags: [test, test2]
date: 2015-05-17 21:30:00 -0700
fb-img: TODO choose an image
---

# New shinyjs version: Improve and simplify your shiny apps + easiy call JavaScript functions as R code

About a month ago I made [an announcement](http://deanattali.com/2015/04/23/shinyjs-r-package/) about the initial release of `shinyjs`. After some feedback, a few feature requests, and numerous hours of work, I'm excited to say that a new version of `shinyjs` v0.0.6.2 was made available on CRAN this week. The package's main objective is to make shiny app development better and easier by allowing you to perform many useful functions with simple R code that would normally require JavaScript coding. Some of the features included in the earlier release include hiding/showing elements and enabling/disabling inputs, among many others. 

## Table of contents 

- Availability
- Quick overview of new features
- Major new features
  - 

## Availability {availability

`shinyjs` is available through both [CRAN](http://cran.r-project.org/web/packages/shinyjs/)
(`install.packages("shinyjs")`) and [GitHub](https://github.com/daattali/shinyjs)
(`devtools::install_github("daattali/shinyjs")`). Use the GitHub version to get the latest version with the newest features.

## Quick overview of new features

This post will only discuss new features in `shinyjs`. You can find out more about the package [on the initial post](http://deanattali.com/2015/04/23/shinyjs-r-package/) or [in the package README on GitHub](https://github.com/daattali/shinyjs#readme). Remember that in order to use any function, you need to add a call to `useShinyjs()` in the shiny app's UI.

**Two major new features:**

- `reset` function allows inputs to be reset to their original value

- `extendShinyjs` allows you to add your own JavaScript functions and easily call them from R as regular R code

**Two more useful improvements:**

- Enabling and disabling of input widgets now works on **all** types of shiny inputs (many people asked how to disable a slider/select input/date range/etc, and `shinyjs` now handles all of them)

- The `toggle` functions gained an additional `condition` argument, which can be used to show/hide or enable/disable an element based on a condition. For example, instead of writing code such as `if (test) enable(id) else disable(id)`, you can simply write `toggleState(id, test)`

**Three new features available on the [GitHub version](https://github.com/daattali/shinyjs) but not yet on CRAN:**

- `hidden` (used to initialize a shiny tag as hidden) can now accept any number of tags or a tagList rather than just a single tag

- `hide`/`show`/`toggle` can be run on any JQuery selector, not only on a single ID, so that you can hide multiple elements simultaneously 

- `hide`/`show`/`toggle` have a new arugment `delay` which can be used to perform the action later rather than immediately. This can be useful if you want to show a message and have it disappear after a few seconds

## Two major new features

### `reset` -  allows inputs to be reset to their original value

Being able to reset the value of an input has been a frequently asked question on StackOverflow and the shiny Google Group, but a general solution was never available.  Now with `shinyjs` it's possible and very easy: if you have an input with id `name` and you want to reset it to its original value, simply call `reset("name")`. It doesn't matter what type of input it is - `reset` works with all shiny inputs.  

The `reset` function only takes one arugment, an HTML id, and resets all inputs inside of that element. This makes `reset` very flexible because you can either give it a single input widget to reset, or a form that contains many inputs and reset them all. Note that `reset` can only work on inputs that are generated from the app's ui and it will not work for inputs generated dynamically using `uiOutput`/`renderUI`.

Here is a simple demo of reset in action
[![Reset demo]({{ site.url }}/img/blog/shinyjs-improvements/reset-demo.gif)]({{ site.url }}/img/blog/shinyjs-improvements/reset-demo.gif)

### `extendShinyjs` - allows you to easily call your own JavaScript functions from R

The main idea behind `shinyjs` when I started working on it was to make it extremely easy to call JavaScript functions that I used commonly from R. Now whenever I want to add a new function to `shinyjs` (such as the `reset` function), all I have to do is write the JavaScript function, and the integration between shiny and JavaScript happens seamlessly thanks to `shinyjs`.  My main goal after the initial release was to also allow anyone else to use the same smooth R --> JS workflow, so that anyone can add a JavaScript function and call it from R easily. With the `extendShinyjs` function, that is now possible.

#### Very simple example

Using `extendShinys` is very simple and makes defining and calling JavaScript functions painless. Here is a very basic example of using `extendShinyjs` to define a (fairly useless) function that changes the colour of the page.

```
library(shiny)
library(shinyjs)
runApp(shinyApp(
  ui = fluidPage(
    useShinyjs(),
    extendShinyjs(text = 
      "shinyjs.pageCol = function(params){$('body').css('background', params);}"),
    selectInput("col", "Colour:",
                c("white", "yellow", "red", "blue", "purple"))
  ),
  server = function(input,output,session) {
    observeEvent(input$col, {
      js$pageCol(input$col)
    })
  }
))
```

Running the code above produces this shiny app:
[![Extendshinyjs demo]({{ site.url }}/img/blog/shinyjs-improvements/extendshinyjs-demo.gif)]({{ site.url }}/img/blog/shinyjs-improvements/extendshinyjs-demo.gif)

See how easy that was? All I had to do was make the JavaScript function `shinyjs.pageCol`, pass the JavaScript code as an argument to `extendShinyjs`, and then I can call `js$pageCol()`.  That's the basic idea: any JavaScript function named `shinyjs.foo` will be available to call as `js$foo()`. You can either pass the JS code as a string to the `text` argument, or place the JS code in a separate JavaScript file and use the `script` argument to specify where the code can be found.

#### 
