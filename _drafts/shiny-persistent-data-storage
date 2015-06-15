---
layout: post
title: "Persistent data storage in shiny apps"
---






~~~
fields <- c("name", "used_shiny", "r_num_years")

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

shinyApp(
  ui = fluidPage(
    DT::dataTableOutput("responses", width = 300), 
    textInput("name", "Name", ""),
    checkboxInput("used_shiny", "I've built a Shiny app in R before", FALSE),
    sliderInput("r_num_years", "Number of years using R", 0, 25, 2, ticks = FALSE),
    actionButton("submit", "Submit")
  ),
  server = function(input, output, session) {
    formData <- reactive({
      data <- sapply(fields, function(x) input[[x]])
      data <- t(data)
      data
    })
    
    observeEvent(input$submit, {
      saveData(formData())
    })
    
    output$responses <- DT::renderDataTable({
      input$submit
      loadData()
    })     
  }
)
~~~



NOTES
====
- shiny app available at http://daattali.com/shiny/persistent-data-storage/
- mention that sql packages don't support input sanitization and kinda weird to hack together sql statements with sprintf rather than using prepared statements  
- one more approach im not demonstrating or talking about: storing all responses in ONE file (either locally or remotely). I don't want to deal with concurrency issues that arise from a few users submitting at the same time, even though it's very unlikely to happen unless we're dealing with a huge volume of submissions. This only means that when I want to write, the only operation I have to do is a write, rather than read the existing file and append to it.
- after publishing post: add comment on http://shiny.rstudio.com/articles/share-data.html
