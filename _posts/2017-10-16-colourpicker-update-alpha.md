---
layout: post
title: "colourpicker package v1.0: You can now select semi-transparent colours in R (& more!)"
tags: [professional, rstats, r, r-bloggers]
date: 2017-10-16 12:00:00 -0500
share-img: "http://deanattali.com/img/blog/colourpicker-update-alpha/colourinputnew.PNG"
permalink: /blog/colourpicker-update-alpha/
layout: post
comments: true
show-share: true
gh-repo: daattali/colourpicker
gh-badge: [star, watch, follow]
---

For those who aren't familiar with the [`colourpicker`](https://github.com/daattali/colourpicker/) package, it provides a colour picker in R that can be used in Shiny, as well as other related tools. Today it's leaving behind its 0.x days and moving on to version 1.0!

`colourpicker` has gone through a few major milestones since its inception. It began as merely a colour selector input in an unrelated package ([`shinyjs`](https://github.com/daattali/shinyjs/)), simply because I didn't think a colour picker input warrants its own package. After gaining a gadget and an RStudio addin, it graduated to become its own package. Earlier this year, [the Plot Helper tool](http://deanattali.com/blog/plot-colour-helper/) was added. And now `colourpicker` is taking its next big step - an upgrade to version 1.0.

# Table of contents

- [Due credit](#credits)
- [New feature #1: Transparent colours](#feature-alpha)
- [New feature #2: Flexible colour specification](#feature-flexible)
- [New feature #3: Type colour directly into input box](#feature-type)
- [Existing features](#existing-features)

## Due credit {#credits}

Before describing the amazing new features, I have to give credit to [David Griswold](https://twitter.com/DavidGriswoldHH) who made substantial contributions to this update.

## New feature #1: Transparent colours {#feature-alpha}

The most important new feature is the new alpha selector that lets you select a transparency value for any colour. Instead of being able to only select opaque colours (such as "green"), you can now select "green" with 50% transparency for example. To use this feature, use the `allowTransparent = TRUE` argument in the `colourInput()` function. Here is a screenshot of the new colour input with an alpha bar:

[![new colour input with alpha](http://deanattali.com/img/blog/colourpicker-update-alpha/colourinputnew.PNG)](http://deanattali.com/img/blog/colourpicker-update-alpha/colourinputnew.PNG)

While I'm very excited about this transparency feature, it also caused me to introduce my first big **breaking change** unfortunately. The colour input previously already had a `allowTransparent` argument, which when set to `TRUE` resulted in a checkbox inside the input:

[![previous transparent feature](https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/allowTransparent.png)](https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/allowTransparent.png)

There was also a parameter that could control the text inside the checkbox. This was a useful, albeit fairly awkward, feature. After much consideration and discussion with many people, I decided against supporting both the old and new features simultaneously, and instead overriding the old `allowTransparent` by changing its behaviour to the current form. Even though it is technically a breaking change, this change was welcomed by most people I asked who've used that checkbox :)

## New feature #2: Flexible colour specification {#feature-flexible}

Previously, colours supplied to the colour input had to be specified by either using a colour name (such as `blue`), or using a HEX code (such as `#0000FF` or `#00F`).

In version 1.0, you can also specify colours using RGB codes (such as `rgb(0, 0, 255)`) or HSL codes (such as `hsl(240, 100, 50)`).

If your colour input uses the transparency feature described above, then you can also specify the colour to be transparent. All four methods of specifying colours support transparent colours using the alpha channel. For example, `transparent`, `#0000FF80`, `rgba(0, 0, 255, 0.5)` and `hsla(240, 100, 50, 0.5)` are all legal colour values for `colourpicker`.

## New feature #3: Type colour directly into input box {#feature-type}

This one is less impressive, but still a very convenient and useful feature.

Previously, the only way to pick a colour was to click on a colour input box with the mouse and select a colour by clicking on the desired colour in the palette. There is now another way to change the value of a colour input if you know the precise colour that you want to choose: you can type in the value of a colour directly into the input box. For example, you can click on the colour input and literally type the word "blue", and the colour blue will get selected. This will work with any of the four methods for specifying colours that are mentioned in Feature 2.

## Existing features {#existing-features}

If you've never used the colour input in R before, here is a quick rundown of the features it has.

### Simple and familiar

Using `colourInput` is extremely trivial if you've used Shiny, and it's as easy to use as any other input control.  It was implemented to very closely mimic all other Shiny inputs so that using it will feel very familiar. You can add a simple colour input to your Shiny app with `colourInput("col", "Select colour", value = "red")`. The return value from a `colourInput` is an uppercase HEX colour, so in the previous example the value of `input$col` would be `#FF0000`.

### Retrieving the colour names

If you use the `returnName = TRUE` parameter, then the return value will be a colour name instead of a HEX value, when possible. For example, if the chosen colour is red, the return value will be `red` instead of `#FF0000`. For any colour that does not have a standard name, its HEX value will be returned.

### Limited colour selection

If you want to only allow the user to select a colour from a specific list of colours, rather than any possible colour, you can use the `palette = "limited"` parameter.  By default, the limited palette will contain 40 common colours, but you can supply your own list of colours using the `allowedCols` parameter. Here is an image of the default `limited` colour palette.

[![limited palette](https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/limited-palette.png)](https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/limited-palette.png)

### How the chosen colour is shown inside the input box

By default, the colour input's background will match the selected colour and the text inside the input field will be the colour's HEX value. If that's too much for you, you can customize the input with the `showColour` parameter to either only show the text or only show the background colour.

Here is what a colour input with each of the possible values for `showColour` looks like

[![showColour](https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/showColour.png)](https://raw.githubusercontent.com/daattali/colourpicker/master/inst/img/showColour.png)

### Works on any device

If you're worried that maybe someone viewing your Shiny app on a phone won't be able to use this input properly - don't you worry. I haven't quite checked every single device out there, but I did spend extra time making sure the colour selection JavaScript works in most devices I could think of. `colourInput()` will work fine in Shiny apps that are viewed on Android cell phones, iPhones, iPads, and even Internet Explorer 8+.

---

As always, any feedback is more than welcomed!
