---
layout: post
title: "Plot Colour Helper - Finally an easy way to pick colours for your R plots!"
tags: [professional, rstats, r, r-bloggers, shiny, packages]
date: 2017-01-09 05:00:00 -0500
share-img: https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/plothelper-demo.png
permalink: /blog/plot-colour-helper/
layout: post
comments: true
show-share: true
show-subscribe: true
gh-repo: daattali/colourpicker
gh-badge: [star, watch, follow]
---

You've just made an amazing plot in R, and the only thing remaining is finding the right colours to use. Arghhh this part is never fun... You're probably familiar with this loop: try some colour values -> plot -> try different colours -> plot -> repeat. Don't you wish there was a better way?

Well, now there is :)

If you've ever had to spend a long time perfecting the colour scheme of a plot, you might find the new Plot Colour Helper handy. It's an RStudio addin that lets you interactively choose combinations of colours for your plot, while updating your plot in real-time so you can see the colour changes immediately.

[![Demo of colour picker addin](https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/plothelper-demo.png)](https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/plothelper-demo.png)

This new tool is available from the [colourpicker package](https://github.com/daattali/colourpicker). 

To use this tool, either call the `plotHelper()` function or highlight code for a plot and select the addin through the RStudio Addins menu. The colours you select in the tool will be available to your code as a variable named `CPCOLS`.

The Plot Colour Helper has many little extra features that are designed to make working with it as efficient as possible. For example, you can initialize the tool with a specified set of colours or you can let the tool guess how many colours are needed based on your plot code. There are also several keyboard shortcuts for your convenience.

Here is a short demo of the tool:

[![Plot Colour Helper demo](https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/plothelper-demo.gif)](https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/plothelper-demo.gif)

Fun fact: this tool was born thanks to my girlfriend's blunt feedback. I had previously made a Colour Picker addin, which lets you select colours in RStudio and get the resulting colour list as a vector. When I saw my girlfriend creating lots of plots for her thesis and spending a lot of time on colour selecting and re-plotting, I told her she can use my Colour Picker to help with the process. She told me that it's pretty much useless since she can't actually see the plot update while choosing colours. So me, being the best boyfriend in the universe (and an R addict), I immediately proceeded to create the Plot Colour Helper for her. And for all of you!

Go ahead and install [`colourpicker`](https://github.com/daattali/colourpicker) to get started. I hope you find this addin useful, and as always - [feedback is welcomed](http://deanattali.com/contact/).
