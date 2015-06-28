---
layout: post
title: "You can finally select colours in Shiny apps with a colourInput"
tags: [professional, rstats, r, r-bloggers, shiny, packages, shinyjs]
date: 2015-06-28 10:00:00 -0700
---

I don't always think Shiny is missing anything, but when I do - I fill in the gap myself. That was meant to be read as The Most Interesting Man In The World, but now that I think about it - maybe he's not the type of guy who would be building Shiny R packages...

Shiny has many useful input controls, but there was one that was always missing until today - a colour picker.  There have been many times when I wanted to allow users in a Shiny app to select a colour, and I've seen that feature being requested multiple times on different online boards, so I decided to make my own such input control.  My package [`shinyjs`](https://github.com/daattali/shinyjs) now has a `colourInput()` function and, of course, a corresponding `updateColourInput()`. 

## Table of contents

- [Demo](#demo)
- [Availability](#availability)
- [Features](#features)
  - [Simple and familiar](#simple)
  - [Allowing "transparent"](#transparent)
  - [How the chosen colour is shown inside the input](#showColour)
  - [Updating a colourInput](#update)
  - [Flexible colour specification](#colour-spec)
  - [Works on any device](#compatibility)
- [Misc](#misc)

## Demo {#demo}

[Click here for a demo of `colourInput` in a Shiny app](http://daattali.com/shiny/colourInput/) that shows some of its features. If you don't want to check out the Shiny app, here is a short GIF demonstrating the most basic functionality of `colourInput`.

[![colourInput demo]({{ site.url }}/img/blog/colourInput/colourInputDemo.gif)]({{ site.url }}/img/blog/colourInput/colourInputDemo.gif)

The colour of course don't look at ugly as in the GIF, here's a screenshot of what a plain `colourInput` looks like.

[![colourInput demo]({{ site.url }}/img/blog/colourInput/colourInputDemo.png)]({{ site.url }}/img/blog/colourInput/colourInputDemo.png)

## Availability {#availability}

`colourInput()` is available in `shinyjs`. You can either install it [from GitHub](https://github.com/daattali/shinyjs) with `devtools::install_github("daattali/shinyjs")` or from CRAN with `install.packages("shinyjs")`.

## Features {#features}

### Simple and familiar {#simple}

Using `colourInput` is extremely trivial if you've used Shiny, and it's as easy to use as any other input control.  It was implemented to very closely mimic all other Shiny inputs so that using it will feel very familiar. You can add a simple colour input to your Shiny app with `colourInput("col", "Select colour", value = "red")`. The return value from a `colourInput` is an uppercase HEX colour, so in the previous example the value of `input$col` would be `#FF0000` (#FF0000 is the HEX value of the colour red). The default value at initialization is white (#FFFFFF).

### Allowing "transparent" {#transparent}

Since most functions in R that accept colours can also accept the value "transparent", `colourInput` has an option to allow selecting the "transparent" colour. By default, only real colours can be selected, so you need to use the `allowTransparent = TRUE` parameter. When this feature is turned on, a checkbox appears inside the input box. If the user checks the checkbox for "transparent", then the colour input is grayed out and the returned value of the input is `transparent`. This is the only case when the value returned from a `colourInput` is not a HEX value. When the checkbox is unchecked, the value of the input will be the last selected colour prior to selecting "transparent". By default, the text of the checkbox reads "Transparent", but you can change that with the `transparentText` parameter. For example, it might be more clear to a user to use the word "None" instead of "Transparent". Note that even if you change the checkbox text, the return value will still be `transparent` since that's the actual colour name in R.

### How the chosen colour is shown inside the input {#showColour}

By default, the colour input's background will match the selected colour and the text inside the input field will be the colour's HEX value. If that's too much for you, you can customize the input with the `showColour` parameter to either only show the text or only show the background colour.

### Updating a colourInput {#update}

As with all other Shiny inputs, `colourInput` can be updated with the `updateColourInput` function.  Any parameter that can be used in `colourInput` can be used in `updateColourInput`. This means that you can start with a basic colour input such as `colourInput("col", "Select colour")` and completely redesign it with

~~~
updateColourInput(session, "col", label = "COLOUR:", value = "orange",
  showColour = "background", allowTransparent = TRUE, transparentText = "None")
~~~

### Flexible colour specification {#colour-spec}

Specifying a colour to the colour input is made very flexible to allow for easier use. When giving a colour as the `value` parameter of either `colourInput` or `updateColourInput`, there are a few ways to specify a colour:

- Using a name of an R colour, such as `red`, `gold`, `blue3`, or any other name that R supports (for a full list of R colours, type `colours()`)
- If transparency is allowed in the `colourInput`, the value `transparent` (lowercase) can be used. This will update the UI to check the checkbox.
- Using a 6-character HEX value, either with or without the leading `#`.  For example, initializing a `colourInput` with any of the following values will all result in the colour red: `ff0000`, `FF0000`, `#ff0000`.
- Using a 3-character HEX value, either with or without the leading `#`, by automatically doubling every character. For example, all the following values would result in the same colour: `1ac`, `#1Ac`, `11aacc`.

### Works on any device {#compatibility}

If you're worried that maybe someone viewing your Shiny app on a phone won't be able to use this input properly - don't you worry. I haven't quite checked every single device out there, but I did spend extra time making sure the colour selection JavaScript works in most devices I could think of. `colourInput` will work fine in Shiny apps that are viewed on Android cell phones, iPhones, iPads, and even Internet Explorer 8+.

## Misc {#misc}

In order to build `colourInput`, I needed to use a JavaScript colour picker library. After experimenting with many different colour pickers, I decided to use [this popular jQuery colour picker](https://github.com/claviska/jquery-minicolors) as a base, and extend it myself to make it geared to work with Shiny. I simplified much of the code and added some features that would make it much more readily integrate with Shiny. You can see the exact changes I've made in the [README for my version of the library](https://github.com/daattali/jquery-colourpicker). The main feature I added was the support for a "transparent" checkbox, and I also changed the colour picker colours to render completely in CSS instead of using images.

It's been pointed out that this function is not exactly in-line with the general `shinyjs` idea, so it might not stay there forever. Ideally, this `colourInput` will soon be part of `shiny`, but until then I'll just keep it here until it finds a more loving home.
