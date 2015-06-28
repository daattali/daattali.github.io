---
layout: post
title: "You can finally select colours in Shiny apps with a colourInput"
tags: [professional, rstats, r, r-bloggers, shiny, packages, shinyjs]
date: 2015-05-09 21:30:00 -0700
---

I don't always think Shiny is missing anything, but when I do - I fill in the gap myself. That was meant to be read as The Most Interesting Man In The World, but now that I think about it - maybe he's not the type of guy who would be building Shiny R packages...

Shiny has many useful input controls, but there was one that was always missing until today - a colour picker.  There have been many times when I wanted to allow users in a Shiny app to select a colour, and I've seen that feature being requested multiple times on different online boards, so I decided to make my own such input control.  My package [`shinyjs`](https://github.com/daattali/shinyjs) now has a `colourInput()` function and, of course, a corresponding `updateColourInput()`. 

[Click here for a demo of `colourInput` in a Shiny app](http://daattali.com/shiny/colourInput/) that shows some of its features. If you don't want to check out the Shiny app, here is a short GIF demonstrating the most basic functionality of `colourInput()`.

[![colourInput demo]({{ site.url }}/img/blog/colourInput/colourInputDemo.gif)]({{ site.url }}/img/blog/colourInput/colourInputDemo.gif)

The colour of course don't look at ugly as in the GIF, here's a screenshot of what a plain `colourInput()` looks like.

[![colourInput demo]({{ site.url }}/img/blog/colourInput/colourInputDemo.png)]({{ site.url }}/img/blog/colourInput/colourInputDemo.png)

`colourInput()` was implemented in a way that very closely mimic all other Shiny inputs, and it's as easy and trivial to use as any other input.  You can add a colour input to your Shiny app with `colourInput("col", "Select colour", value = "red")`. 
