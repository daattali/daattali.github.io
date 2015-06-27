# Persistent data storage with Shiny

Shiny apps can be used for a wide variety of applications, ranging from simply running R code interactively to building complete multi-page web apps. Sometimes Shiny apps need to be able to save data, either to load it back in a different session or to simply log some information. The most trivial method of storing data - saving a file to the local machine (for example, using `write.csv()` or `saveRDS()`) - should not be used when hosting on *[shinyapps.io](http://www.shinyapps.io/)* because it only allows for *temporary* data storage rather than *persistent*. In short, *shinyapps.io* is designed to distribute your Shiny app across different servers, which means that if a file is saved during one session on some server, then loading the app again later will probably direct you to a different server where the previously saved file doesn't exist. More information on this issue can be found in Jeff Allen's [article regarding sharing data across sessions](http://shiny.rstudio.com/articles/share-data.html). In his article, Jeff also mentions that there are several ways to save (and load) data from Shiny, which can be grouped into three categories.

The main purpose of this guide is to not only explain in theory how to store data in Shiny apps, but also show full working examples, in order to make it clear and easy for anyone to use these concepts in their own apps. In this guide we'll learn about each of the three main ways to store data, and see how to implement each one in a real-world Shiny app. This post will introduce seven specific storage methods and will include complete working code for each that shows how to integrate each one into a Shiny app.  As a complement to this article, you can see a [live demo of a Shiny app](http://daattali.com/shiny/persistent-data-storage/) that uses each of the seven storage methods to save and load data.

## Basic Shiny app without data storage

To demonstrate how to store data using each storage types, we'll start with a simple form-submission Shiny app that collects some information from the user, stores their response, and shows all previous responses. Initially the app will only save responses within its R session, and we will learn how to modify the app to use each different storage type. Here is the code for the basic app that we will be using as our starting point. 

~~~
library(shiny)

# Define the fields we want to save from the form
fields <- c("name", "used_shiny", "r_num_years")

# Shiny app with 3 fields that the user can submit data for
shinyApp(
  ui = fluidPage(
    DT::dataTableOutput("responses", width = 300), tags$hr(),
    textInput("name", "Name", ""),
    checkboxInput("used_shiny", "I've built a Shiny app in R before", FALSE),
    sliderInput("r_num_years", "Number of years using R", 0, 25, 2, ticks = FALSE),
    actionButton("submit", "Submit")
  ),
  server = function(input, output, session) {
    
    # Whenever a field is filled, aggregate all form data
    formData <- reactive({
      data <- sapply(fields, function(x) input[[x]])
      data <- t(data)
      data
    })
    
    # When the Submit button is clicked, save the form data
    observeEvent(input$submit, {
      saveData(formData())
    })
    
    # Show the previous responses
    # (update with current response when Submit is clicked)
    output$responses <- DT::renderDataTable({
      input$submit
      loadData()
    })     
  }
)
~~~

The above code is taken from a [guide on how to mimic a Google form with Shiny](http://deanattali.com/2015/06/14/mimicking-google-form-shiny/). 

The above app is very simple - there is a table that shows all responses, three input fields, and a **Submit** button that will take the data in the input fields and save it. You might notice that there are two functions that are not defined but are used in the app: `saveData(data)` and `loadData()`. These two functions are the only code that affects how the data is stored/retrieved, and we will redefine them for each data storage type. In order to make the app work for now, here's a trivial implementation of the save and load functions that simply stores responses in the current R session. 

~~~
saveData <- function(data) {
  data <- as.data.frame(data)
  if (exists("responses")) {
    responses <<- rbind(responses, data)
  } else {
    responses <<- data
  }
}

loadData <- function() {
  if (exists("responses")) {
    responses
  }
}
~~~

Before continuing further, make sure this basic app works for you. The code for this app is also available as a [gist](https://gist.github.com/daattali/c4db11d81f3c46a7c4a5) and you can run it either by copying all the code to your RStudio or by running `runGist("c4db11d81f3c46a7c4a5")`. 




local vs remote

3 main types

