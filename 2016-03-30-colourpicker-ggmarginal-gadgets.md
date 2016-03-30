---
title: "An awesome RStudio addin for selecting colours, and another for adding marginal density plots to ggplot2"
tags: [professional, rstats, r, r-bloggers, shiny, shinyjs]
date: 2016-03-30 09:00:00 -0700
fb-img: http://deanattali.com/img/blog/colourpicker-ggmarginal-gadgets/colourpickerscrnshot.png
---

TL;DR: There's a [**colour picker addin** in shinyjs](https://github.com/daattali/shinyjs#colourpicker) and a [**ggplot2 marginal plots addin** in ggExtra](https://github.com/daattali/ggExtra#marginal-plots-rstudio-addingadget).

Any R user who hasn't been spending the past 2 months under a rock should know by now about RStudio's new exciting features: addins and gadgets.

(In case you don't know, here's a summary: Gadgets are simply Shiny apps that return a value, and are therefore meant to be used by the programmer to assign a value to a variable rather than by an end-user.  Addins are a way to add your own clickable menus in RStudio that call R functions upon being clicked.)

From the moment I saw the announcement about gadgets and addins, I was excited to try them out. Unfortunately, I've been pretty tied up with wrapping up my thesis, so I didn't get too much play time.  I'm happy to finally announce the two addin+gadget pairs I've been working on: a [colour picker](#colour-picker) and a [tool to add marginal density plots to ggplot2](#add-marginal-densityhistogram-plots-to-ggplot2).

## Colour picker

A colour picker gadget and addin are available in the [`shinyjs`](https://github.com/daattali/shinyjs) package.

Screenshot:

[![Colour picker screenshot]({{ site.url }}/img/blog/colourpicker-ggmarginal-gadgets/colourpickerscrnshot.png)]({{ site.url }}/img/blog/colourpicker-ggmarginal-gadgets/colourpickerscrnshot.png)

Some of you may already know that `shinyjs` provides a `colourInput()` function, which is a Shiny input widget that lets you select a colour ([demo](http://daattali.com/shiny/colourInput/)).  The idea of the colour picker gadget is to extend this idea of a colour input and provide R developers with an easy way to select colours. It's perfect if you want to choose a colour/a vector of colours to use for a plot or for anything else that requires a careful selection of colours.

You can either run the colour picker as an **addin** or as a **gadget**.  To access it as an addin, click on the RStudio *Addins* menu and select *Colour Picker*. To access the colour picker gadget, run the `colourPicker()` function. When running the gadget, you can provide a positive integer as an argument (e.g. `colourPicker(3)`), which will cause the colour picker to initialize with placeholders for 3 colours.

### Features

- By default, the colour picker lets you select one colour. You can add/remove colours by using the buttons (plus sign to add another colour placeholder, garbage icon to remove the selected colour).

- The colours returned can either be HEX values (e.g. "#FF0000") or R colour names (e.g. "red"). If you opt to get the R colour names, you will still get the HEX value for any colour that does not have a corresponding R colour. 

- Each colour can be selected in one of three ways: 1. picking any arbitrary colour from a "colour wheel" (it's actually a square though); 2. providing an arbitrary colour and selecting an R colour that's very similar to the colour you provided; 3. picking from the complete list of all available R colours.

- When accessed as an addin (through the *Addins* menu), the result will be injected into the current script in RStudio as valid code that produces a vector of colours.

- When ran as a gadget, the result will return a vector of colours, so you can use it to assign a vector of colours to a variable, e.g. `mycols <- colourPicker(5)`.


Here's a GIF demo of the colour picker in action:

[![Colour picker GIF]({{ site.url }}/img/blog/colourpicker-ggmarginal-gadgets/colourPickerGadget.gif)]({{ site.url }}/img/blog/colourpicker-ggmarginal-gadgets/colourPickerGadget.gif)

## Add marginal density/histogram plots to ggplot2

This gadget and addin are available in the [`ggExtra`](https://github.com/daattali/ggExtra) package.

Screenshot:

[![ggMarginalGadget screenshot]({{ site.url }}/img/blog/colourpicker-ggmarginal-gadgets/ggmarginal-gadget.png)]({{ site.url }}/img/blog/colourpicker-ggmarginal-gadgets/ggmarginal-gadget.png)

The flagship function of the `ggExtra` package is `ggMarginal()`, which is used to add marginal density/histograms to ggplot2 plots ([demo](http://daattali.com/shiny/ggExtra-ggMarginal-demo/)). Now with the help of this addin, you can do this interactively by setting all the different parameters and seeing how the plot is affected in real-time.

You can either run the marginal plot builder as an **addin** or as a **gadget**.  To access it as an addin, highlight the code for a plot and then select *ggplot2 Marginal Plots* from the RStudio *Addins* menu.  This will embed the resulting code for the marginal plots right into your script. Alternatively, you can call `ggMarginalGadget()` with a ggplot2 plot, and the return value will be a plot object.  For example, you can call `myplot <- ggMarginalGadget(ggplot(mtcars, aes(wt, mpg)) + geom_point())`.

Disclaimer: The UI that is available for building Shiny gadgets is very limited, and there are very few resources and examples out there for building gadgets, so some of the UI code is a little bit hacky. If you're having issues with the UI of one of these gadgets, please do [let me know]({{ site.url }}/aboutme#contact).  If you have any other feedback, I'd also love to [hear about it]({{ site.url }}/aboutme#contact)!
