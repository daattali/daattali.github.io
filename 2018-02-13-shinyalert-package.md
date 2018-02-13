---
title: "shinyalert: Easily create pretty popup messages (modals) in Shiny"
tags: [professional, rstats, r-bloggers, packages, shiny]
share-img: "https://daattali.com/shiny/img/shinyalert.png"
permalink: /blog/shinyalert-package/
date: 2018-02-13 12:00:00 -0500
gh-repo: daattali/shinyalert
gh-badge: [star, watch, follow]
---

A brand new shiny package has entered the world yesterday: [`shinyalert`](https://github.com/daattali/shinyalert/). It does only one thing, but does it well: show a message to the user in a modal (aka popup, dialog, or alert box). Actually, it can do one more thing: `shinyalert` can also be used to retrieve an input value from the user using a modal.

You can visit the [GitHub page](https://github.com/daattali/shinyalert/) or check out a demo online for yourself:

<div style="text-align:center;">
<a class="btn btn-lg btn-success" href="https://daattali.com/shiny/shinyalert-demo/">Check out a demo</a>
</div>

I usually release packages at version *0.1*, but with `shinyalert` I decided to be optimistic (read: naive) and release version *1.0*. This package is fairly lightweight and I do not see it growing too much in terms of functionality, and because of its simplicity I don't expect too many bugs. So I decided to start if off at *1.0* and my wishful thinking is that it could remain there without much ongoing maintenance. 

# Table of contents

- [Background](#background)
- [Overview](#overview)
- [Input modals](#input-modals)
- [Modal return value](#return-value)
- [Callbacks](#callbacks)
- [Comparison with Shiny modals](#shiny-comparison)

## Background {#background}

The idea for this package has been on my mind for a very long time, and the basis for this package is a 10-liner that I wrote 2.5 years ago.

Back in mid 2015, when I was a graduate student spending 15 hours/day building Shiny apps and packages for fun, and maybe 15 minutes/day on my actual thesis (I will forever be grateful to [Jenny Bryan](https://twitter.com/JennyBryan) for allowing me to get away with that), [Kent Russell (@timelyportfolio)](https://twitter.com/timelyportfolio) built an htmlwidget called `sweetalertR`. It's a port of the [sweetalert](https://github.com/t4t5/sweetalert) JavaScript library, which is used to create simple pretty modals, to R.

Shortly after, [Eric Nantz (@theRcast)](https://twitter.com/thercast) asked Kent if it's possible to incorporate these modals in Shiny apps, and tagged me in his question. I'd never heard of sweetalert before, but I was intrigued, so in [my response](https://github.com/timelyportfolio/sweetalertR/issues/1#issuecomment-123564079) I came up with a short piece of code to include sweetalert modals in Shiny apps.

Over the next year I noticed that I actually found these modals to be very nice so I started working on this package so that everyone could benefit from them. But it turned out that just a few weeks prior, Shiny released a great new version, which included support for modals! And so my motivation for the package quickly plummeted. I still wanted to release this package because I do believe it's nicer and simpler than Shiny's modals, and includes a few extra features. Last week, after a 16-month hiatus, I resumed its development.

And here we are!

## Overview {#overview}

`shinyalert` uses the [sweetalert](https://github.com/t4t5/sweetalert) JavaScript library to create simple and elegant modals in Shiny. Modals can contain text, images, OK/Cancel buttons, an input to get a response from the user, and many more customizable options. A modal can also have a timer to close automatically.

Simply call `shinyalert()` with the desired arguments, such as a title and text, and a modal will show up. In order to be able to call `shinyalert()` in a Shiny app, you must first call `useShinyalert()` anywhere in the app's UI.

[![basic modal](https://raw.githubusercontent.com/daattali/shinyalert/master/inst/img/shinyalert-basic.gif)](https://raw.githubusercontent.com/daattali/shinyalert/master/inst/img/shinyalert-basic.gif)

Here is some minimal Shiny app code that creates the above modal:

```r
library(shiny)
library(shinyalert)

ui <- fluidPage(
  useShinyalert(),  # Set up shinyalert
  actionButton("preview", "Preview")
)

server <- function(input, output, session) {
  observeEvent(input$preview, {
    # Show a modal when the button is pressed
    shinyalert("Oops!", "Something went wrong.", type = "error")
  })
}

shinyApp(ui, server)
```

## Input modals {#input-modals}

Usually the purpose of a modal is simply informative, to show some information to the user. However, the modal can also be used to retrieve an input from the user by setting the `type = "input"` parameter.

[![input modal](https://raw.githubusercontent.com/daattali/shinyalert/master/inst/img/shinyalert-input.gif)](https://raw.githubusercontent.com/daattali/shinyalert/master/inst/img/shinyalert-input.gif)

Only a single input can be used inside a modal. By default, the input will be a text input, but you can use other HTML input types by specifying the `inputType` parameter. For example, `inputType = "number"` will provide the user with a numeric input in the modal.

See the *[Modal return value](#return-value)* and *[Callbacks](#callbacks)* sections below for information on how to access the value entered by the user.

## Modal return value {#return-value}

Modals created with `shinyalert` have a return value when they exit.

When there is an input field in the modal (`type="input"`), the value of the modal is the value the user entered. When there is no input field in the modal, the value of the modal is `TRUE` if the user clicked the "OK" button, and `FALSE` if the user clicked the "Cancel" button.

When the user exits the modal using the Escape key or by clicking outside of the modal, the return value is `FALSE` (as if the "Cancel" button was clicked). If the `timer` parameter is used and the modal closes automatically as a result of the timer, no value is returned from the modal.

The return value of the modal can be accessed via `input$shinyalert` in the Shiny server's code, as if it were a regular Shiny input. The return value can also be accessed using the *[modal callbacks](#callbacks)*.

## Callbacks {#callbacks}

The return value of the modal is passed as an argument to the `callbackR` and `callbackJS` functions (if a `callbackR` or `callbackJS` arguments are provided). These are functions that get called, either in R or in JavaScript, when the modal exits.

For example, using the following `shinyalert` code will result in a modal with an input field. After the user clicks "OK", a hello message will be printed to both the R console and in a native JavaScript alert box. You don't need to provide both callback functions, but in this example both are used for demonstration.

```r
shinyalert(
  "Enter your name", type = "input",
  callbackR = function(x) { message("Hello ", x) },
  callbackJS = "function(x) { alert('Hello ' + x); }"
)
```

Notice that the `callbackR` function accepts R code, while the `callbackJS` function uses JavaScript code.

Since closing the modal with the Escape key results in a return value of `FALSE`, the callback functions can be modified to not print hello in that case.

```r
shinyalert(
  "Enter your name", type = "input",
  callbackR = function(x) { if(x != FALSE) message("Hello ", x) },
  callbackJS = "function(x) { if (x !== false) { alert('Hello ' + x); } }"
)
```

## Comparison with Shiny modals {#shiny-comparison}

Doesn't Shiny already have support for modals? 

Yes, it does.

And Shiny's modals are more powerful in some ways than `shinyalert` modals: Shiny's native modals (`showModal()`+`modalDialog()`) can contain multiple input fields and even outputs.

I created `shinyalert` for two reasons: first of all, I started working on it well before Shiny had modals (or so I thought). But I decided to keep working on it and release it even afterwards because I find `shinyalert` to be easier to use and to result in much nicer modals. There are also some extra features in `shinyalert`, such as the callback functions and the timer. But ultimately it's a matter of convenience and aesthetics.
