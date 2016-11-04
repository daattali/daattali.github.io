# New shinyjs version: useful tools for shiny developers and new functionality

shinyjs is a package that helps you easily improve the user interaction and user experience in your shiny apps. It has come a long way in the 1.5 years since it was first released, and today I'm happy to announce a new version with a few features that I was 

added runcodeUI() and runcodeServer() functions that you can add to your app in order to run arbitrary R code interactively
added showLog() function which lets you redirect all JavaScript logging statements to the R console, to make it easier and quicker to debug apps without having to open the JS console (#88)
onclick and onevent now support callback functions, and the JavaScript Event object is passed to the callback (#92)
the reset() function now works on file inputs
added alert() as an alias for info()
