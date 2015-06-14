---
layout: post
url: "2015-06-14-mimicking-google-form-shiny"
title: "Mimicking a Google Form with a Shiny app"
tags: [professional, rstats, r, r-bloggers, shiny]
date: 2015-06-14 11:30:00 -0700
---

In this post we will walk through the steps required to build a shiny app that mimicks a Google Form. It will allow users to submit responses to some input fields, save their data, and allow admins to view the submitted responses.  Like many of my other posts, it may seem lengthy, but that's only because I like to go into fine details to ensure everything is as foolproof and reproducible as possible.

# Table of contents

- [Motivation](#motivation)
  - [Note about persistent storage](#note-storage)


# Motivation {#motivation}

Last year I was fortunate enough to be a teaching assistant for [STAT545](https://twitter.com/stat545) - a course at the University of British Columbia, taught by [Jenny Bryan](https://twitter.com/JennyBryan), that introduces R into the lives of student scientists. (It was especially special to me because just 12 months prior, that course taught *me* how to write my first line in R.) To facilitate communication with the students, we wanted to gather some basic information from them, such as their prefered name, email, and Twitter and Github handles so that. We didn't want to simply have them all send us an email with this information, as we were trying to set an example of how to be techy and automated and modern :) Our first thought was to use a Google Form, but university policies didn't quite allow for that because the data must be stored within Canadian borders, and with Google Forms it'd probably end up somewhere in the US. We also decided earlier that day that one of the course modules will be about shiny, so we figured we'd put our money where our mouths were, and attempt to collect student data via a shiny app. I was given the task of developing this shiny app, which was a great learning experience. You can view the original code for that app [on GitHub](https://github.com/daattali/UBC-STAT545/tree/master/shiny-apps/request-basic-info) or [visit the app yourself to see it in action](http://daattali.com/shiny/request-basic-info/).

The idea of recording user-submitted form data can be applied to many differente scenarios. Seeing how successful the previous app was for our course, we decided to also collect all peer reviews of assignments in a similar shiny app. I created an app with a template for a marking sheet, and every week students would use the app to submit reviews for other students' work. This worked great for us - you can see the original code [on GitHub](https://github.com/daattali/UBC-STAT545/tree/master/shiny-apps/marking-sheet) or [try the app out yourself](http://daattali.com/shiny/peer-review/).

Since developing those apps, I've become a better shiny developer also wrote the [shinyjs package](https://github.com/daattali/shinyjs) to help with many user-experience stuff like hiding/disabling/resetting inputs. I've also seen multiple people asking how to do this kind of thing with shiny, so my hope is that this post will be useful for others who are also looking to create user-submitted forms with shiny.

#### Note about persistent storage {#note-storage}

One major component of this app is storing the user-submitted data in a way that would allow it to be retrieved later. This is an important topic of its own, and in a few days I will write a detailed post about all the different storage options and how to use them. In this tutorial I will use the simplest approach for saving the data: every submission will be saved to its own `.csv` file. **Warning:** this method should only be used if you have your own shiny server, and should not be used if your app is hosted on `shinyapps.io`. Using the local filesystem in shinyapps.io is a bad idea because every time your app is launched it will be on a different machine, and it will not have access to files saved by other users who were running the app on a different machine. If using shinyapps.io, you will need to use remote storage, which will be discussed in my next post. You can get a bit more information about why shinyapps.io can't be used for local storage [in the shiny docs](http://shiny.rstudio.com/articles/share-data.html).

# Overview {#overview}

The app we will build will be a form collecting data on a user's R usage - their name, length of time using R, favourite R package, etc. You can see the result of this tutorial [on my shiny server](http://daattali.com/shiny/mimic-google-form/) and the corresponding code [on GitHub](https://github.com/daattali/shiny-server/tree/master/mimic-google-form).  It looks like this:

[![Final app]({{ site.url }}/img/blog/mimic-google-form-shiny/mimic-google-form-shiny-final.png)]({{ site.url }}/img/blog/mimic-google-form-shiny/mimic-google-form-shiny-final.png)

The main idea is simple: create a UI with some inputs that users need to fill out, add a submit button, and save the response. Sounds simple, and it is! In this tutorial each response will be saved to a *.csv* file along with the timestamp of submission. To see all submissions that were made, we simply read all *csv* files and join them together. There will also be an "admin panel" that will show admin users all previous responses and allow them to download this data. When using Shiny Server Pro or paid shinyapps.io accounts, you can add authentication/login to your apps, and decide which usernames have admin access. Since my app is hosted on a free shiny server that doesn't support authentication, it'll just assume that everyone is an admin. I also like to focus **a lot** (arguably too much) on user experience, so this post will also discuss many small tips & tricks that are optional but can be nice additions. Many of these use the `shinyjs` package, so instead of loading the package in the beginning, I'll explicitly show when functions from `shinyjs` are used so that you know what functions are not core shiny.

# Build the basic UI (inputs) {#build-inputs}

I generally prefer to split shiny apps into a `ui.R` and `server.R` file (with an additional `helpers.R` or `globals.R` if necessary), but for simplicity, I'll place all the app code together in this tutorial.  

Create a new file named `app.R` and copy the following code into it to build input elements.

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

After saving this file, you should be able to run it either with `shiny::runApp()` or by clicking the "Run App" button in RStudio. The app simply shows the input fields and the submit button, but does nothing yet.

# Define mandatory fields

We want everyone to at least tell us their name and favourite package, so let's ensure the submit button is only enabled if both of those fields are filled out. We need to use `shinyjs` for that, so add a call to `shinyjs::useShinyjs()` anywhere in the UI. In the global scope (above the definition of `shinyApp`, outside the UI and server code), define the mandatory fields:

~~~
fieldsMandatory <- c("name", "favourite_pkg")
~~~

And now we can use the `toggleState` function to enable/disable the submit button based on a condition. The condition is whether or not all mandatory fields have been filled. To calculate that, we can loop through the mandator fields and check their values.  Add the following code to the server portion of the app:

~~~
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
~~~

Now try running the app again, and you'll see the submit button is only enabled when these fields have a value.

### Show which fields are mandatory in the UI

If you want to be extra fancy, you can add a red asterisk to the mandatory fields. Here's a neat though possibly overcomplicated approach to do this:  define a function that takes an input label and adds an asterisk to it:

~~~
labelMandatory <- function(label) {
  tagList(
    label,
    span("*", class = "mandatory_star")
  )
}
~~~

To use it, simply wrap the `label` argument of both mandatory input element with `labelMandatory`. For example, `textInput("name", labelMandatory("Name"), "")`. To make the asterisk red, we need to add some CSS, so define the CSS in the global scope:

~~~
appCSS <- ".mandatory_star { color: red; }"
~~~

And add the CSS to the app by calling `shinyjs::inlineCSS(appCSS)` in the UI.

The complete code so far should look like this:

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
    
    # Enable the Submit button when all mandatory fields are filled out
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

### tell others

http://www.quora.com/How-would-you-use-Shiny-to-take-input-information-from-a-form-field-and-create-a-csv-file  
https://groups.google.com/forum/#!topic/shiny-discuss/g1p9HZmEpmo  
https://twitter.com/jalapic/status/598153400228929537  
