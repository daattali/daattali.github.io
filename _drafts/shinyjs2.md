# New shinyjs version: Improve and simplify your shiny apps + easiy call JavaScript functions as R code

About a month ago I made [an announcement](http://deanattali.com/2015/04/23/shinyjs-r-package/) about the initial release of `shinyjs`. After some feedback, a few feature requests, and numerous hours of work, I'm excited to say that a new version of `shinyjs` v0.0.6.2 was made available on CRAN this week. The package's main objective is to make shiny app development better and easier by allowing you to perform many useful functions with simple R code that would normally require JavaScript coding. Some of the features included in the earlier release include hiding/showing elements and enabling/disabling inputs, among many others. 

## Quick overview of new features

More information on any of these features is available in the following sections. Detailed information is also available [in the package README on GitHub](https://github.com/daattali/shinyjs#readme).

#### Version 0.0.6.2 on CRAN includes the following new useful features:

- `reset` function which allows inputs to be reset to their original value

- `extendShinyjs` allows you to add your own JavaScript functions and easily call them from R as regular R code

- enabling and disabling of input widgets now works on **all** types of shiny inputs (many people ask how to disable a slider/select input/date range/etc, and `shinyjs` handles all of them)

- The `toggle` functions gained an additional `condition` argument, which can be used to show/hide or enable/disable an element based on a conditoin. For example, instead of writing code such as `if (test) enable(id) else disable(id)`, you can simply write `toggleState(id, test)`

#### The dev version 0.0.6.4 on GitHub includes a few extra features:

- `hidden` (used to initialize a shiny tag as hidden) can now accept any number of tags or a tagList rather than a single tag

- `hide`/`show`/`toggle` can be run on any JQuery selector, not only on a single ID, so that you can hide multiple elements simultaneously 

- `hide`/`show`/`toggle` have a new arugment `delay` which can be used to perform the action later rather than immediately. This can be useful if you want to show a message and have it disappear after a few seconds

## Availability

`shinyjs` is available through both [CRAN](http://cran.r-project.org/web/packages/shinyjs/)
(`install.packages("shinyjs")`) and [GitHub](https://github.com/daattali/shinyjs)
(`devtools::install_github("daattali/shinyjs")`). Use the GitHub version to get the latest version with the newest features.
