http://rpubs.com/daattali/heatmapsGgplotVsLattice

calculating/heatmaps of programs ouput, spinme code chunk, must use runtime:shiny in shiny server, cant use cache chunk option with shiny

---

https://pviefers.wordpress.com/2015/08/16/programming-simple-economic-experiments-in-shiny

R shiny tricks (shinyjs - reset inputs, disable textinput when radio button is selected, loading..., state variables to use in ui - can be useful if want to use conditionalPanel with a variable that's calcualted in the server, global.R, splitting off big ui/server into files,  shiny debugging such as add a `options(warn=2)` at top of UI and server if getting a "ERRORR: canot open the conenction" butyou have no clue where the error's happening or what file it's failing at, ajax loading on image, ajax loading + error on submit button), 

withBusyIndicator

more breathing room in selectizeinput:

```
runApp(shinyApp(
  ui = fluidPage(
    tags$style(type='text/css', ".selectize-input { line-height: 40px; } .selectize-dropdown { line-height: 30px; }"),
    selectInput("test","Test", 1:5)
  ),
  server = function(input, output, session) {
  }
))
```

collapsing (or showing) sidebar in shinydashboard

```
library(shinydashboard)
library(shinyjs)

shinyApp(
  ui = 
    dashboardPage(
      dashboardHeader(),
      dashboardSidebar(),
      dashboardBody(
        shinyjs::useShinyjs(),
        actionButton("showSidebar", "Show sidebar"),
        actionButton("hideSidebar", "Hide sidebar")
      )
    ),
  server = function(input, output, session) {
    observeEvent(input$showSidebar, {
      shinyjs::removeClass(selector = "body", class = "sidebar-collapse")
    })
    observeEvent(input$hideSidebar, {
      shinyjs::addClass(selector = "body", class = "sidebar-collapse")
    })
  }
)
```

fix uploaded file names

```
#' When files get uploaded, their new filenames are gibberish.
#' This function renames all uploaded files to their original names
#' @param x The dataframe returned from a shiny::fileInput
fixUploadedFilesNames <- function(x) {
  if (is.null(x)) {
    return()
  }
  
  oldNames = x$datapath
  newNames = file.path(dirname(x$datapath),
                       x$name)
  file.rename(from = oldNames, to = newNames)
  x$datapath <- newNames
  x
}
```

show custom message when the'res an error in a reactive context

```
runApp(shinyApp(
  ui = fluidPage(
    tags$style(type="text/css",
               ".shiny-output-error { visibility: hidden; }",
               ".shiny-output-error:before { visibility: visible; content: 'An error occurred. Please contact the admin.'; }"
    ),
    textOutput("text")
  ),
  server = function(input, output, session) {
    output$text <- renderText({
      stop("lalala")
    })
  }
))
```

prepopulate input fields when app loads

```
runApp(shinyApp(
  ui = fluidPage(
    textInput("name", "Name"),
    numericInput("age", "Age", 25)
  ),
  server = function(input, output, session) {
    observe({
      query <- parseQueryString(session$clientData$url_search)
      if (!is.null(query[['name']])) {
        updateTextInput(session, "name", value = query[['name']])
      }
      if (!is.null(query[['age']])) {
        updateNumericInput(session, "age", value = query[['age']])
      }
    })
  }
))
```

when developing shiny app , its annoying that when you close the browser window the app is still alive.

```
runApp(shinyApp(
  ui = (),
  server = function(input, output, session) {
    session$onSessionEnded(function()stopApp())
  }
))
```

click button to close the current window

```
library(shinyjs)
jscode <- "shinyjs.closewindow = function() { window.close(); }"

runApp(shinyApp(
  ui = tagList(
    useShinyjs(),
    extendShinyjs(text = jscode),
    navbarPage(
      "test",
      id = "navbar",
      tabPanel(title = "tab1"),
      tabPanel(title = "", value = "Stop", icon = icon("power-off"))
    )
  ),
  server = function(input, output, session) {
    observe({
      if (input$navbar == "Stop") {
        js$closewindow();
        stopApp()
      }
    })
  }
))
```

all my extensions


google analytics - thank god i spent time makin gwebsite nice on mobile - 65% of traffic is from mobile. just like tagged taught me - i am not the average user, cant make assumptions based on what i think, have to test and see how people use. nice to see some of my friends i made while traveling are viewing my site (dont worry i cant see IP or pin it down to specific people). apparnetly i have 2 friends with Windows phones. Losers! I tried ensuring my hotos look good mostly on wide screen but also look ok on phones, but it looks like i should have had the priorities reversed. Glad to see IE is dead :)

media affects what we think/talk about (ebola)

always reach higher -> youll never be satisfied/happy. happiness if wanting what you have rather than having what you want, or is happiness going after what you want?

danger with social media (fb post aug 9)

boycot 3d movies

natural vs unnatural products
