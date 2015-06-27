---
layout: post
title: "Persistent data storage in Shiny apps"
---

Shiny apps can be used for a wide variety of applications, ranging from simply running R code interactively to building complete multi-page web apps. Sometimes Shiny apps need to be able to save data, either to load it back in a different session or to simply log some information. The most trivial method of storing data - saving a file to the local machine (for example, using `write.csv()` or `saveRDS()`) - should not be used when hosting on *[shinyapps.io](http://www.shinyapps.io/)* because it only allows for *temporary* data storage rather than *persistent*. In short, *shinyapps.io* is designed to distribute your Shiny app across different servers, which means that if a file is saved during one session on some server, then loading the app again later will probably direct you to a different server where the previously saved file doesn't exist. More information on this issue can be found in Jeff Allen's [article regarding sharing data across sessions](http://shiny.rstudio.com/articles/share-data.html). In his article, Jeff also mentions that there are several ways to save (and load) data from Shiny, which can be grouped into three categories.

The main purpose of this guide is to not only explain in theory how to store data in Shiny apps, but also show full working examples, in order to make it clear and easy for anyone to use these concepts in their own apps. In this guide we'll learn about each of the three main ways to store data, and see how to implement each one in a real-world Shiny app. This post will introduce seven specific storage methods and will include complete working code for each that shows how to integrate each one into a Shiny app.

As a complement to this article, you can see a [live demo of a Shiny app](http://daattali.com/shiny/persistent-data-storage/) that uses each of the seven storage methods to save and load data.

The three categories of data storage methods depend on the type of data:

- **Arbitrary data** can be stored as a **file** in some sort of a **file system** ([local file system](#local), [Dropbox](#dropbox), [Amazon S3](#s3))
- **Structured rectangular data** can be stored as a **table** in a **relational database or table-storage service**  ([SQLite](#sqlite), [MySQL](#mysql), [Google Sheets](#gsheets))
- **Semi-structured data** can be stored as a **collection** in a **schemaless database** ([MongoDB](#mongodb))

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
  data <- as.data.frame(t(data))
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

Before continuing further, make sure this basic app works for you and that you understand every line in it - it's not difficult, but take the two minutes to go through it. The code for this app is also available as a [gist](https://gist.github.com/daattali/c4db11d81f3c46a7c4a5) and you can run it either by copying all the code to your RStudio or by running `runGist("c4db11d81f3c46a7c4a5")`. 

## Local vs remote storage

One important distinction to understand is *local storage* vs *remote storage*. Local storage means saving a file on the same machine that is running the Shiny application. Using functions such as `write.csv()`/`write.table()`/`saveRDS()` and others are considered local storage because they will save a file on the machine running the app. Local storage is generally faster, but it should only be used if you always have access to the machine that saved the files. Remote storage means saving data on another server, usually a reliable hosted server such as Dropbox, Amazon, or a hosted database.  One big advantage of using hosted remote storage solutions is that they are much more reliable and can generally be more trusted to keep your data alive and not corrupted. When going through the different storage type options below, keep in mind that if your Shiny app is hosted on *shinyapps.io*, you will have to use a remote storage method. Using local storage is only an option if you're hosting your own Shiny Server, though that comes at the price of having to manage a server and should only be done if you're comfortable with administering a server.

## Persistent data storage methods

Using the above Shiny app, we can use many different ways to store and retrieve responses. Here we will go through seven ways to achieve data persistence that can be easily integrated into Shiny apps. Each method will be explained, and to use a method as the storage type in the example app, use the given code for `saveData()` and `loadData()` to replace the existing functions.

### File storage

This is the most flexible option since files allow you to store any type of data, whether it's just a single value, a big *data.frame*, or any arbitrary data.  There are two common scenarios when using files to store data: either you have one file that gets repeatedly overwritten and used by all sessions (like the example in [Jeff Allen's article](http://shiny.rstudio.com/articles/share-data.html), or you save a new file every time there is new data.  In our case we'll use the latter, because we want to save each response as its own file (we can use the former option, but then we would introduce the potential for [race conditions](https://en.wikipedia.org/wiki/Race_condition#File_systems) which will overcomplicate the app).

When saving multiple files, it's important to make sure that each file you save has a different file name, because if new data is saved with an existing file name, it will overwrite the data in the file. There are many ways to do this, such as simply using the current timestamp and an *md5 hash* of the data being saved as the file name to ensure that no two form submissions have the same file name.

As a reminder, you can see all the seven different storage types being used, along with the exact code used, [in this live Shiny app](http://daattali.com/shiny/persistent-data-storage/).

#### Local file system (**local**) {#local}

The most trivial way of saving data from Shiny is by simply saving each response as its own file on the current server. To load the data, we simply load all the files in the output directory. 

**Setup:** The only required setup is to create an output directory (`responses` in this case) and ensure the Shiny app has file permissions to read/write in that directory.  

**Code**:

~~~
outputDir <- "responses"

saveData <- function(data) {
  data <- t(data)
  fileName <- sprintf("%s_%s.csv", as.integer(Sys.time()), digest::digest(data))
  write.csv(
    x = data,
    file = file.path(outputDir, fileName), 
    row.names = FALSE, quote = TRUE
  )
}

loadData <- function() {
  files <- list.files(outputDir, full.names = TRUE)
  data <- lapply(files, read.csv, stringsAsFactors = FALSE) 
  data <- do.call(rbind, data)
  data
}
~~~

#### Dropbox (**remote**) {#dropbox}

If you want to store arbitrary files but use a remote hosted solution instead of the local file system (either because you're using *shinyapps.io* or simply because you want a more trusted system), you can store files on [Dropbox](https://www.dropbox.com). Dropbox is a file storing service which allows you to host any file, up to a certain maximum usage. The free account provides plenty of storage space and should be enough for storing most data from Shiny apps.

This approach is similar to the previous approach using the local file system, with the only difference that now that files are being saved to and loaded from Dropbox. You can use the [`rdrop2`](https://github.com/karthik/rdrop2) package to interact with Dropbox from R.

**Setup:** You need to have a Dropbox account and create a folder to store the responses. You will also need to add authentication to `rdrop2` using any approach [suggested in the package README](https://github.com/karthik/rdrop2#accessing-dropbox-on-shiny-and-remote-servers). The authentication approach I chose was to authenticate manually once and copy the resulting `.httr-oauth` file that gets created into the Shiny app's folder. 

**Code:**

~~~
library(rdrop2)
outputDir <- "responses"

saveData <- function(data) {
  data <- t(data)
  fileName <- sprintf("%s_%s.csv", as.integer(Sys.time()), digest::digest(data))
  filePath <- file.path(tempdir(), fileName)
  write.csv(data, filePath, row.names = FALSE, quote = TRUE)
  drop_upload(filePath, dest = outputDir)
}

loadData <- function() {
  filesInfo <- drop_dir(outputDir)
  filePaths <- filesInfo$path
  data <- lapply(filePaths, drop_read_csv, stringsAsFactors = FALSE)
  data <- do.call(rbind, data)
  data
}
~~~

#### Amazon S3 (**remote**) {#s3}

Another popular alternative to Dropbox for hosting files online is [Amazon S3](http://aws.amazon.com/s3/), or *S3* in short. Just like with Dropbox, you can host any type of file on S3, but instead of placing files inside directories, in S3 you place files inside a *bucket*. You can use the [RAmazonS3](http://www.omegahat.org/RAmazonS3/) package to interact with S3 from R. Note that the package is a few years old and is not under active development, so use it at your own risk.

**Setup:** You need to have an [Amazon Web Services](http://aws.amazon.com/) account and create an S3 bucket to store the responses. As the [package documentation](http://www.omegahat.org/RAmazonS3/s3amazon.html) explains, you will need to enable authentication by setting the `AmazonS3` global option.

**Code:**

~~~
library(RAmazonS3)
s3BucketName <- "my-unique-s3-bucket-name"
options(AmazonS3 = c('login' = "secret"))

saveData <- function(data) {
  fileName <- sprintf("%s_%s.csv", as.integer(Sys.time()), digest::digest(data))
  addFile(
    I(paste0(
      paste(names(data), collapse = ","), 
      "\n",
      paste(data, collapse = ",")
    )),
    s3BucketName, 
    fileName,
    virtual = TRUE
  )
}

loadData <- function() {
  files <- listBucket(s3BucketName)$Key
  files <- as.character(files)
  data <- lapply(files, function(x) {
      raw <- getFile(s3BucketName, x, virtual = TRUE)
      read.csv(text = raw, stringsAsFactors = FALSE)
  })
  data <- do.call(rbind, data)
  data  
}
~~~

#### Local file system (**local**) {#local}

sdfdss

**Setup:**

**Code:**

~~~
saveData <- function(data) {
}

loadData <- function() {
}
~~~

#### Local file system (**local**) {#local}

sdfdss

**Setup:**

**Code:**

~~~
saveData <- function(data) {
}

loadData <- function() {
}
~~~

#### Local file system (**local**) {#local}

sdfdss

**Setup:**

**Code:**

~~~
saveData <- function(data) {
}

loadData <- function() {
}
~~~

#### Local file system (**local**) {#local}

sdfdss

**Setup:**

**Code:**

~~~
saveData <- function(data) {
}

loadData <- function() {
}
~~~

## Conclusion

show table
