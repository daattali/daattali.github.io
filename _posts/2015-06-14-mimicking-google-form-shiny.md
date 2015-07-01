---
layout: post
title: "Mimicking a Google Form with a Shiny app"
tags: [professional, rstats, r, r-bloggers, shiny, shinyjs]
date: 2015-06-14 5:30:00 -0700
permalink: /2015/06/14/mimicking-google-form-shiny
---

In this post we will walk through the steps required to build a shiny app that mimicks a Google Form. It will allow users to submit responses to some input fields, save their data, and allow admins to view the submitted responses.  Like many of my other posts, it may seem lengthy, but that's only because I like to go into fine details to ensure everything is as foolproof and reproducible as possible. In a [follow-up article](http://deanattali.com/blog/shiny-persistent-data-storage/) you can learn about different methods to store data in Shiny apps.

# Table of contents

- [Motivation](#motivation)
- [Overview](#overview)
  - [Note about persistent storage](#note-storage)
- [Prerequisites](#prereqs)
- [Build the basic UI (inputs)](#build-inputs)
- [Define mandatory fields](#define-mandatory)
  - [Show which fields are mandatory in the UI](#mandatory-ui)
- [Save the response upon submission](#save)
  - [Note regarding file permissions](#note-perms)
- [After submission show a "Thank you" message and let user submit again](#thanks-msg)
- [Better user feedback while submitting and on error](#feedback-error)
- [Add table that shows all previous responses](#add-table)
- [Add ability to download all responses](#download)
- [Restrict access to previous data to admins only](#admins)
- [Updates](#updates)

# Motivation {#motivation}

Last year I was fortunate enough to be a teaching assistant for [STAT545](https://twitter.com/stat545) - a course at the University of British Columbia, taught by [Jenny Bryan](https://twitter.com/JennyBryan), that introduces R into the lives of student scientists. (It was especially special to me because just 12 months prior, that course taught *me* how to write my first line in R.) To facilitate communication with the students, we wanted to gather some basic information from them, such as their prefered name, email, and Twitter and Github handles. We didn't want to simply have them all send us an email with this information, as we were trying to set an example of how to be techy and automated and modern :) Our first thought was to use a Google Form, but university policies didn't quite allow for that because the data must be stored within Canadian borders, and with Google Forms it'd probably end up somewhere in the US. We also decided earlier that day that one of the course modules will be about shiny, so we figured we'd put our money where our mouths were, and attempt to collect student data via a shiny app. I was given the task of developing this shiny app, which was a great learning experience. You can view the original code for that app [on GitHub](https://github.com/daattali/UBC-STAT545/tree/master/shiny-apps/request-basic-info) or [visit the app yourself to see it in action](http://daattali.com/shiny/request-basic-info/).

The idea of recording user-submitted form data can be applied to many differente scenarios. Seeing how successful the previous app was for our course, we decided to also collect all peer reviews of assignments in a similar shiny app. I created an app with a template for a marking sheet, and every week students would use the app to submit reviews for other students' work. This worked great for us - you can see the original code [on GitHub](https://github.com/daattali/UBC-STAT545/tree/master/shiny-apps/marking-sheet) or [try the app out yourself](http://daattali.com/shiny/peer-review/).

Since developing those apps, I've become a better shiny developer also wrote the [shinyjs package](https://github.com/daattali/shinyjs) to help with many user-experience stuff like hiding/disabling/resetting inputs. I've also seen multiple people asking how to do this kind of thing with shiny, so my hope is that this post will be useful for others who are also looking to create user-submitted forms with shiny.

# Overview {#overview}

The app we will build will be a form collecting data on a user's R habits - their name, length of time using R, favourite R package, etc. You can see the result of this tutorial [on my shiny server](http://daattali.com/shiny/mimic-google-form/) and the corresponding code [on GitHub](https://github.com/daattali/shiny-server/blob/master/mimic-google-form/app.R).  It looks like this:

[![Final app]({{ site.url }}/img/blog/mimic-google-form-shiny/mimic-google-form-shiny-final.png)]({{ site.url }}/img/blog/mimic-google-form-shiny/mimic-google-form-shiny-final.png)

The main idea is simple: create a UI with some inputs that users need to fill out, add a submit button, and save the response. Sounds simple, and it is! In this tutorial each response will be saved to a *.csv* file along with the timestamp of submission. To see all submissions that were made, we simply read all *csv* files and join them together. There will also be an "admin panel" that will show admin users all previous responses and allow them to download this data. When using Shiny Server Pro or paid shinyapps.io accounts, you can add authentication/login to your apps, and decide which usernames have admin access. Since my app is hosted on a free shiny server that doesn't support authentication, it'll just assume that everyone is an admin. I also like to focus **a lot** (arguably too much) on user experience, so this post will also discuss many small tips & tricks that are optional but can be nice additions. Many of these use the [`shinyjs`](https://github.com/daattali/shinyjs) package, so instead of loading the package in the beginning, I'll explicitly show when functions from `shinyjs` are used so that you know what functions are not core shiny.

### Note about persistent storage {#note-storage}

One major component of this app is storing the user-submitted data in a way that would allow it to be retrieved later. This is an important topic of its own, and I wrote a [separate article](http://deanattali.com/blog/shiny-persistent-data-storage/) detailing the different storage options and how to use them. That article is also featured on [RStudio's official Shiny articles](http://shiny.rstudio.com/articles/persistent-data-storage.html) because it's such an important skill. In this tutorial I will use the simplest approach for saving the data: every submission will be saved to its own `.csv` file.

**NOTE:** this method should only be used if you have your own shiny server or are running the app on your own machine, and should not be used if your app is hosted on `shinyapps.io`. Using the local filesystem in shinyapps.io is a bad idea because every time your app is launched it will be on a different machine, and it will not have access to files saved by other users who were running the app on a different machine. If using shinyapps.io, you will need to use remote storage, which will be discussed in my next post. You can get a bit more information about why shinyapps.io can't be used for local storage [in the shiny docs](http://shiny.rstudio.com/articles/share-data.html).

# Prerequisites {#prereqs}

The following packages need to be installed in order for all the code to work:

- [`DT`](https://github.com/rstudio/DT) version 0.1.3 (as of today, this version is not yet on CRAN, so install it from GitHub with `devtools::install_github('rstudio/DT')`  
- `shiny`, `shinyjs`, `dplyr`, `digest` - any version will do, you can install from CRAN

# Build the basic UI (inputs) {#build-inputs}

I generally prefer to split shiny apps into a `ui.R` and `server.R` file (with an additional `helpers.R` or `globals.R` if necessary), but for simplicity, I'll place all the app code together in this tutorial. (In case you didn't know: Shiny apps don't have to be broken up into separate `ui.R` and `server.R` files, they can be completely defined in one file [as this Shiny article explains](http://shiny.rstudio.com/articles/app-formats.html)) 

Create a new file named `app.R` and copy the following code into it to build the input elements. 

~~~
shinyApp(
  ui = fluidPage(
    titlePanel("Mimicking a Google Form with a Shiny app"),
    div(
      id = "form",
      
      textInput("name", "Name", ""),
      textInput("favourite_pkg", "Favourite R package"),
      checkboxInput("used_shiny", "I've built a Shiny app in R before", FALSE),
      sliderInput("r_num_years", "Number of years using R", 0, 25, 2, ticks = FALSE),
      selectInput("os_type", "Operating system used most frequently",
                  c("",  "Windows", "Mac", "Linux")),
      actionButton("submit", "Submit", class = "btn-primary")
    )
  ),
  server = function(input, output, session) {
  }
)
~~~

Most of this code is simply setting up a shiny app and adding a few input fields and a button to a div element named `form`.

After saving this file, you should be able to run it either with `shiny::runApp()` or by clicking the "Run App" button in RStudio. The app simply shows the input fields and the submit button, but does nothing yet.

# Define mandatory fields {#define-mandatory}

We want everyone to at least tell us their name and favourite package, so let's ensure the submit button is only enabled if both of those fields are filled out. We need to use `shinyjs` for that, so you need to add a call to `shinyjs::useShinyjs()` anywhere in the UI. In the global scope (above the definition of `shinyApp`, outside the UI and server code), define the mandatory fields:

~~~
fieldsMandatory <- c("name", "favourite_pkg")
~~~

Now we can use the `toggleState` function to enable/disable the submit button based on a condition. The condition is whether or not all mandatory fields have been filled. To calculate that, we can loop through the mandatory fields and check their values.  Add the following code to the server portion of the app:

~~~
observe({
  # check if all mandatory fields have a value
  mandatoryFilled <-
    vapply(fieldsMandatory,
           function(x) {
             !is.null(input[[x]]) && input[[x]] != ""
           },
           logical(1))
  mandatoryFilled <- all(mandatoryFilled)
  
  # enable/disable the submit button
  shinyjs::toggleState(id = "submit", condition = mandatoryFilled)
})
~~~

Now try running the app again, and you'll see the submit button is only enabled when these fields have a value.

### Show which fields are mandatory in the UI {#mandatory-ui}

If you want to be extra fancy, you can add a red asterisk to the mandatory fields. Here's a neat though possibly overcomplicated approach to do this:  define a function that takes an input label and adds an asterisk to it (you can define it in the global scope):

~~~
labelMandatory <- function(label) {
  tagList(
    label,
    span("*", class = "mandatory_star")
  )
}
~~~

To use it, simply wrap the `label` argument of both mandatory input element with `labelMandatory`. For example, `textInput("name", labelMandatory("Name"), "")`.

To make the asterisk red, we need to add some CSS, so define the CSS in the global scope:

~~~
appCSS <- ".mandatory_star { color: red; }"
~~~

And add the CSS to the app by calling `shinyjs::inlineCSS(appCSS)` in the UI.

The complete code so far should look like this (it might be a good idea to just copy and paste this, to make sure you have the right code):

~~~
fieldsMandatory <- c("name", "favourite_pkg")

labelMandatory <- function(label) {
  tagList(
    label,
    span("*", class = "mandatory_star")
  )
}

appCSS <-
  ".mandatory_star { color: red; }"

shinyApp(
  ui = fluidPage(
    shinyjs::useShinyjs(),
    shinyjs::inlineCSS(appCSS),
    titlePanel("Mimicking a Google Form with a Shiny app"),
    
    div(
      id = "form",
      
      textInput("name", labelMandatory("Name"), ""),
      textInput("favourite_pkg", labelMandatory("Favourite R package")),
      checkboxInput("used_shiny", "I've built a Shiny app in R before", FALSE),
      sliderInput("r_num_years", "Number of years using R", 0, 25, 2, ticks = FALSE),
      selectInput("os_type", "Operating system used most frequently",
                  c("",  "Windows", "Mac", "Linux")),
      actionButton("submit", "Submit", class = "btn-primary")
    )
  ),
  server = function(input, output, session) {
    observe({
      mandatoryFilled <-
        vapply(fieldsMandatory,
               function(x) {
                 !is.null(input[[x]]) && input[[x]] != ""
               },
               logical(1))
      mandatoryFilled <- all(mandatoryFilled)
      
      shinyjs::toggleState(id = "submit", condition = mandatoryFilled)
    })    
  }
)
~~~

# Save the response upon submission {#save}

The most important part of the app is to save the user's response.  First we need to define (a) what input fields we want to store and (b) what directory to use to store all the responses. I also like to add the submission timestamp to each submission, so I also want to define (c) a function that returns the current time as an integer. Let's define these three things in the global scope:

~~~
fieldsAll <- c("name", "favourite_pkg", "used_shiny", "r_num_years", "os_type")
responsesDir <- file.path("responses")
epochTime <- function() {
  as.integer(Sys.time())
}
~~~

Make sure you create a `responses` directory so that the saved responses can go there.

Next we need to have a way to gather all the form data (plus the timestamp) into a format that can be saved as a *csv*. We can do this easily by looping over the input fields. Note that we need to transpose the data to get it into the right shape that we want (1 row = 1 observation = 1 user submission). Add the following reactive expression to the server:

~~~
formData <- reactive({
  data <- sapply(fieldsAll, function(x) input[[x]])
  data <- c(data, timestamp = epochTime())
  data <- t(data)
  data
})
~~~

The last part is to actually save the data. As I said earlier, in this post we will save the data to a local file, but in my next post I'll show how to alter the following function in order to save to other sources. When saving the user responses locally to a file, there are two options: either save all responses to one file, or save each response as its own file. The first approach might sound like it makes more sense, but I wanted to avoid it for two reasons: first of all, it's slower because in order to save (add a new row to the file), we'd need to first read the whole file to know where to add the new row. Secondly, this approach is not thread-safe, which means that if two people submit at the same time, one of their responses will get lost. So I opted to use the second solution - each submission is its own file. It might seem weird, but it works.

To ensure that we don't lose any submissions, we need to make sure that no two files have the same name. It's difficult to 100% guarantee that, but it's easy enough to be almost sure that filenames are unique by adding some randomness to them. However, instead of having turly random characters in the filename, I went a slightly different way: I make the filename a concatenation of the current time and the md5 hash of the submission data. This way the only realistic way that two submissions will overwrite each other is if they happen at the same second and have the exact same data.  Here is the function to save the response (add to the server): 

~~~
saveData <- function(data) {
  fileName <- sprintf("%s_%s.csv",
                      humanTime(),
                      digest::digest(data))
  
  write.csv(x = data, file = file.path(responsesDir, fileName),
            row.names = FALSE, quote = TRUE)
}

# action to take when submit button is pressed
observeEvent(input$submit, {
  saveData(formData())
})
~~~

Notice that I used `humanTime()` instead of `epochTime()` because I wanted the filename to have a more human-friendly timestamp. You'll need to define `humanTime()` as

~~~
humanTime <- function() format(Sys.time(), "%Y%m%d-%H%M%OS")
~~~

Now you should be able to run the app, enter input, save, and see a new file created for every submission. If you get an error when saving, make sure the `responses` directory exists and you have write permissions.

### Note regarding file permissions {#note-perms}

If you are running the app on a shiny server, it's very improtant to understand user permissions. By default, all apps are run as the `shiny` user, and that user will probably not have write permission on folders you create.  You should either add write permissions to `shiny`, or change the running user to yourself. See more information on how to do this [in this post](http://deanattali.com/2015/05/09/setup-rstudio-shiny-server-digital-ocean/#shiny-user-perms).

# After submission show a "Thank you" message and let user submit again {#thanks-msg}

Right now, after submitting a response, there is no feedback and the user will think nothing happened. Let's add a "Thank you" message that will get shown, and add a button to allow the user to submit another response (if it makes sense for your app).

Add the "thank you" section to the UI *after* the `form` div (initialize it as hidden because we only want to show it after a submission):

~~~
div(id = "form", ...),
shinyjs::hidden(
  div(
    id = "thankyou_msg",
    h3("Thanks, your response was submitted successfully!"),
    actionLink("submit_another", "Submit another response")
  )
)  
~~~

And in the server, after saving the data we now want to reset the form, hide it, and show the thank you message:

~~~
# action to take when submit button is pressed
observeEvent(input$submit, {
  saveData(formData())
  shinyjs::reset("form")
  shinyjs::hide("form")
  shinyjs::show("thankyou_msg")
})
~~~

Note that the this observer should overwrite the previous one because we added 3 expressions.

We also need to add an observer to clicking on the "Submit another response" button that will do the opposite: hide the thank you message and show the form (add the following to the server):

~~~
observeEvent(input$submit_another, {
  shinyjs::show("form")
  shinyjs::hide("thankyou_msg")
})    
~~~

Now you should be able to submit multiple responses with a clear indication every time that it succeeded.

# Better user feedback while submitting and on error {#feedback-error}

Right now there is no feedback to the user when their response is being saved and if it encounters an error, the app will crash.  Let's fix that! First we need to add a "Submitting..." progress message and an error message container to the UI - add them inside the `form` div, just after the submit button:

~~~
shinyjs::hidden(
  span(id = "submit_msg", "Submitting..."),
  div(id = "error",
      div(br(), tags$b("Error: "), span(id = "error_msg"))
  )
)
~~~

Now let's hook up the logic. When the "submit" button is pressed, we want to: disable the button from being pressed again, show the "Submitting..." message, and hide any previous errors. We want to reverse these actions when saving the data is finished. If an error occurs while saving the data, we want to show the error message.  All these sorts of actions are why `shinyjs` was created, and it will help us here. Change the observer of `input$submit` once again:

~~~
observeEvent(input$submit, {
  shinyjs::disable("submit")
  shinyjs::show("submit_msg")
  shinyjs::hide("error")
  
  tryCatch({
    saveData(formData())
    shinyjs::reset("form")
    shinyjs::hide("form")
    shinyjs::show("thankyou_msg")
  },
  error = function(err) {
    shinyjs::text("error_msg", err$message)
    shinyjs::show(id = "error", anim = TRUE, animType = "fade")
  },
  finally = {
    shinyjs::enable("submit")
    shinyjs::hide("submit_msg")
  })
})
~~~

Just as a small extra bonus, I like to make error messages red, so I added `#error { color: red; }` to the `appCSS` string that we defined in the beginning, so now `appCSS` is:

~~~
appCSS <-
  ".mandatory_star { color: red; }
   #error { color: red; }"
~~~

**Now you have a fully functioning form shiny app!** The only thing that's missing so far is a way to view the responses directly in the app. Remember that all the responses are saved locally, so you can also just open the files manually or use any approach you want to open the files.

# Add table that shows all previous responses {#add-table}

*Note: this section is not visually identical to the app shown [on my shiny server](http://daattali.com/shiny/mimic-google-form/) because in my app I placed the table to the right of the form, and the code given here will place the table above the form.*

Now that we can submit responses smoothly, it'd be nice to also be able to view submitted responses in the app. First we need to add a dataTable placeholder to the UI (add it just before the `form` div, after the `titlePanel`):

~~~
DT::dataTableOutput("responsesTable"),
~~~

The main issue we need to solve in this section is how to retrieve all previous submissions. To do this, we'll look at all the files in the `responses` directory, read each one into a data.frame separately, and then use `dplyr::rbind_all` to concatenate all the responses together. Note that this will only work if all the response files have exactly the same fields, so if you change your app to add new fields, you'll probably need to either remove all previous submissions or make your own script to add a default value to the new field of all previous submissions.

Here's our function that will retrieve all submissions and load them into a data.frame. You can define it in the global scope.

~~~
loadData <- function() {
  files <- list.files(file.path(responsesDir), full.names = TRUE)
  data <- lapply(files, read.csv, stringsAsFactors = FALSE)
  data <- dplyr::rbind_all(data)
  data
}
~~~

Now that we have this function, we just need to tell the dataTable in the UI to display that data. Add the following to the server:

~~~
output$responsesTable <- DT::renderDataTable(
  loadData(),
  rownames = FALSE,
  options = list(searching = FALSE, lengthChange = FALSE)
) 
~~~

Now when you run the app you should be able to see your previous submissions, assuming you followed the instructions without problems.

# Add ability to download all responses {#download}

It would also be very handy to be able to download all the reponses into a single file. Let's add a download button to the UI, either just before or just after the dataTable:

~~~
downloadButton("downloadBtn", "Download responses"),
~~~

We already have a function for retrieving the data, so all we need to do is tell the download hadler to use it. Add the following to the server:

~~~
output$downloadBtn <- downloadHandler(
  filename = function() { 
    sprintf("mimic-google-form_%s.csv", humanTime())
  },
  content = function(file) {
    write.csv(loadData(), file, row.names = FALSE)
  }
)
~~~

Almost done!

# Restrict access to previous data to admins only {#admins} 

The only missing piece is that right now everyone will see all the responses, and you might want to restrict that access to admins only.  This is only possible if you enable authentication, which is available in Shiny Server Pro and in the paid shinyapps.io accounts. Without authentication, everyone who goes to your app will be treated equally, but with authentication you can give different people different usernames and decide which users are considered admins.

The first thing we need to do is remove all the admin-only content from the UI and only generate it if the current user is an admin. Remove the `dataTableOutput` and the `downloadButton` from the UI, and instead add a dynamic UI element:

~~~
uiOutput("adminPanelContainer"),
~~~

We'll re-define the dataTable and download button in the server, but only if the user is an admin. The following code ensures that for non-admins, nothing gets rendered in the admin panel, but admins can see the table and download button (add this to the server):

~~~
output$adminPanelContainer <- renderUI({
  if (!isAdmin()) return()
  
  wellPanel(
    h2("Previous responses (only visible to admins)"),
    downloadButton("downloadBtn", "Download responses"), br(), br(),
    DT::dataTableOutput("responsesTable")
  )
}) 
~~~

All that's left is to decide if the user is an admin or not (note the `isAdmin()` call in the previous code chunk, we need to define that function). If authentication is enabled, then the logged in user's name will be available to us in the `session$user` variable. If there is no authentication, it will be `NULL`. Let's say John and Sally are the app developers so they should be the admins, we can define a list of admin usernames in the global scope:

~~~
adminUsers <- c("john", "sally")
~~~

Now that we know who are the potential admins, we can use this code (in the server) to determine if the current user is an admin:

~~~
isAdmin <- reactive({
  !is.null(session$user) && session$user %in% adminUsers
})  
~~~

This will ensure that only if "john" or "sally" are using the app, the admin panel will show up.  For illustration purposes, since many of you don't have authentication support, you can change the `isAdmin` to

~~~
isAdmin <- reactive({
  is.null(session$user) || session$user %in% adminUsers
})  
~~~

This will assume that when there is no authentication, everyone is an admin, but when authentication is enabled, it will look at the admin users list.

That's it! You are now ready to create forms with shiny apps.  You can see what the final app code looks like [on GitHub](https://github.com/daattali/shiny-server/blob/master/mimic-google-form/app.R) (with a few minor modifications), or test it out [on my shiny server](http://daattali.com/shiny/mimic-google-form/)).

# Updates {#updates}

**[2015-06-15]** As mentioned in the comments below, if you don't have a Pro account but would still like to implement the idea of admins and "authentication", there are other ways to achieve a similar result. I won't go into any of them because I haven't done that, but it's definitely possible to have an input field that accepts a password and if you type in an admin password, the tables will be shown. That's just one example, you can get more creative with the specifics, but essentially you just need a way to return a TRUE/FALSE value from `isAdmin()`.

**[2015-07-01]** Added a link to the [follow-up post](http://deanattali.com/blog/shiny-persistent-data-storage/) on how to store data in Shiny apps.
