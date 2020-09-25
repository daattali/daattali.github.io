---
title: "Exciting updates to my top 4 Shiny packages"
tags: [rstats, packages, shiny, shinyjs]
thumbnail-img: /assets/img/blog/4shinyupdates/4packages.png
permalink: /blog/4shinyupdates/
share-description: "In this post I'm not announcing any new packages; instead I want to let you know about how I spent much of my COVID lockdown time by releasing major updates to 4 of my most popular Shiny packages: {shinyalert}, {shinyjs}, {timevis}, and {colourpicker}."
date: 2020-09-29 11:00:00 -0400
---

Building new packages in R is a lot of fun for me, and if I could then I would spend all day just doing that. But after the initial relese comes the endless maintenance, which mostly involves adding new features requested by users and fixing bugs. Releasing an update to a package is much less glamorous than releasing a brand new one, but it's just as important! 

In this post I'm not announcing any new packages; instead I want to let you know about how I spent much of my COVID lockdown time by releasing major updates to 4 of my most popular Shiny packages: {shinyalert}, {shinyjs}, {timevis}, and {colourpicker}.

# Table of contents

- [shinyalert v2.0](#shinyalert)
- [shinyjs v2.0](#shinyjs)
- [timevis v1.0](#timevis)
- [colourpicker v1.1](#colourpicker)

# shinyalert v2.0 {#shinyalert}

[{shinyalert}](https://github.com/daattali/shinyalert) was first released over 2.5 years ago, and it's been the preferred way to create modals (popups/alert boxes) in Shiny for many people ever since. It had a new version available on GitHub for a long time, but was never updated on CRAN until recently. [Check out an interactive demo here](https://daattali.com/shiny/shinyalert-demo/).

There was one key feature that {shinyalert} always lacked: Shiny inputs/outputs were not supported. Starting with version 2.0, you can now include **any** Shiny UI code in a {shinyalert} modal, including Shiny inputs and outputs, or any other Shiny tags.

The new version also brings many other features, including the ability to "chain" modals one after the other, specify a size, support in Rmd documents, a way to dismiss existing modals, a custom Shiny input ID to use for the return value, and more. Click below to see all the new features and fixes!

<div style="text-align:center;">
<a class="btn btn-lg btn-success" href="https://github.com/daattali/shinyalert/blob/master/NEWS.md">See all {shinyalert} News</a>
</div>

# shinyjs v2.0 {#shinyjs}

[{shinyjs}](https://github.com/daattali/shinyjs) is one of the oldest and most popular packages that extend Shiny, by providing many functions that enhance the user experience. It also hasn't been updated in over two years, since the stable version 1.0 was released.

The latest release does unfortunately introduce two breaking changes to anyone who uses `extendShinyjs()`, in order to make it more flexible and easier to use. First, the `functions` argument, which was previously optional, is now mandatory. The good news is that this means the `V8` package, which was sometimes a pain to install, is no longer a dependency. Second, the `script` path parameter now behaves like any other Shiny web resource, which means it cannot be loaded from the local file system.

Another major change is the license. Over the first few years, several big companies asked me for a commercial license for {shinyjs}, so I made it available under a dual license. But over time, I noticed that the dual license is causing more pain than gain, as some users were confused or offput by it, so I finally removed it and brought {shinyjs} back into the 100% open source territory!

To see a full list of new features, check out the complete changelog below.

<div style="text-align:center;">
<a class="btn btn-lg btn-success" href="https://github.com/daattali/shinyjs/blob/master/NEWS.md">See all {shinyjs} News</a>
</div>

# timevis v1.0 {#timevis}

[{timevis}](https://github.com/daattali/timevis) is an interactive timeline visualization widget that I created just as a learning exercise and wasn't meant to be used by the public, but it's become quite widely used. It recently graduated to version 1.0 after more than four years. [Check out an interactive demo here](https://daattali.com/shiny/timevis-demo/).

The new version, which has been available on GitHub for almost two years, has built-in support for Shiny modules. This means you no longer need to explicitly use namespaces when calling API functions inside a module. Several requested features have been implemented, such as adding timezone support, providing a way to retrieve the visible items, and allowing to modify any vertical time bars.

There are two outstanding issues that have not been resolved. First, I would like to [upgrade to the latest JavaScript library version](https://github.com/daattali/timevis/issues/50), but unfortunately that task is blocked by the fact that the newer version has some regression bugs that I'm still waiting to be fixed. Second, I would love to [add {crosstalk} support](https://github.com/daattali/timevis/issues/95), and any help from the community would be greatly appreciated!

<div style="text-align:center;">
<a class="btn btn-lg btn-success" href="https://github.com/daattali/timevis/blob/master/NEWS.md">See all {timevis} News</a>
</div>

# colourpicker v1.1 {#colourpicker}

[{colourpicker}](https://github.com/daattali/colourpicker) gives you a colour selector input for Shiny, and it's been around since before some of you even used Shiny! It's very stable and the latest version has been on GitHub for three years until it was recently released on CRAN. [Check out an interactive demo here](https://daattali.com/shiny/colourInput/).

The new version adds support for bookmarking (meaning that it will restore its state automatically when using Shiny bookmarks), has a new `closeOnClick` parameter that automatically closes the colour selection box after selecting a colour, and fixes several bugs. This update took so long simply because I was waiting to see if there are any more bugs that people report or features I could add, but it seems that the package is almost at full maturity by now!

<div style="text-align:center;">
<a class="btn btn-lg btn-success" href="https://github.com/daattali/colourpicker/blob/master/NEWS.md">See all {colourpicker} News</a>
</div>
