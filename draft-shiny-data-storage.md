---
layout: post
title: "Persistent data storage in Shiny apps"
---

Shiny apps often need to save data, either to load it back into a different session or to simply log some information. However, common methods of storing data from R may not work well with Shiny. Functions like `write.csv()` and `saveRDS()` save data locally, but consider how [shinyapps.io](http://www.shinyapps.io/) works. Shinyapps.io is a popular server for hosting Shiny apps. It is designed to distribute your Shiny app across different servers, which means that if a file is saved during one session on some server, then loading the app again later will probably direct you to a different server where the previously saved file doesn’t exist. 

On other occasions, you may use data that is too big to store locally with R in an efficient manner.

This guide will explain seven methods for storing persistent data remotely with a Shiny app. You will learn how to store:

* **Arbitrary data** can be stored as a **file** in some sort of a **file system** ([local file system](#local), [Dropbox](#dropbox), [Amazon S3](#s3))
* **Structured rectangular data** can be stored as a **table** in a **relational database or table-storage service**  ([SQLite](#sqlite), [MySQL](#mysql), [Google Sheets](#gsheets))
* **Semi-structured data** can be stored as a **collection** in a **NoSQL database** ([MongoDB](#mongodb))

The article explains the theory behind each method, and augments the theory with working examples that will make it clear and easy for you to use these methods in your own apps.

As a complement to this article, you can see a [live demo of a Shiny app](http://daattali.com/shiny/persistent-data-storage/) that uses each of the seven storage methods to save and load data ([source code on GitHub](https://github.com/daattali/shiny-server/tree/master/persistent-data-storage)). This article expands on Jeff Allen’s [article regarding sharing data across sessions](http://shiny.rstudio.com/articles/share-data.html).

# Basic Shiny app without data storage

To demonstrate how to store data using each storage type, we'll start with a simple form-submission Shiny app that 

1. collects some information from the user 
2. stores their response, and 
3. shows all previous responses 

Initially the app will only save responses within its R session. We will learn later how to modify the app to use each different storage type.

Here is the code for the basic app that we will be using as our starting point—copy it into a file named `app.R`. (In case you didn't know: Shiny apps don't *have to* be broken up into separate `ui.R` and `server.R` files, they can be completely defined in one file [as this Shiny article explains](http://shiny.rstudio.com/articles/app-formats.html))

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

The above app is very simple—there is a table that shows all responses, three input fields, and a **Submit** button that will take the data in the input fields and save it. You might notice that there are two functions that are not defined but are used in the app: `saveData(data)` and `loadData()`. These two functions are the only code that affects how the data is stored/retrieved, and we will redefine them for each data storage type. In order to make the app work for now, here's a trivial implementation of the save and load functions that simply stores responses in the current R session. 

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

Before continuing further, make sure this basic app works for you and that you understand every line in it—it is not difficult, but take the two minutes to go through it. The code for this app is also available as a [gist](https://gist.github.com/daattali/c4db11d81f3c46a7c4a5) and you can run it either by copying all the code to your RStudio IDE or by running `shiny::runGist("c4db11d81f3c46a7c4a5")`. 

# Local vs remote storage

Before diving into the different storage methods, one important distinction to understand is *local storage* vs *remote storage*.

Local storage means saving a file on the same machine that is running the Shiny application. Functions like `write.csv()`, `write.table()`, and `saveRDS()` implement local storage because they will save a file on the machine running the app. Local storage is generally faster than remote storage, but it should only be used if you always have access to the machine that saves the files.

Remote storage means saving data on another server, usually a reliable hosted server such as Dropbox, Amazon, or a hosted database.  One big advantage of using hosted remote storage solutions is that they are much more reliable and can generally be more trusted to keep your data alive and not corrupted.

When going through the different storage type options below, keep in mind that if your Shiny app is hosted on *shinyapps.io*, you will have to use a remote storage method for the time being. RStudio plans to implement persistent storage on shinyapps.io soon. In the meantime, using local storage is only an option if you’re hosting your own Shiny Server, though that comes at the price of having to manage a server and should only be done if you’re comfortable with administering a server.

# Persistent data storage methods

Using the above Shiny app, we can store and retrieve responses in many different ways. Here we will go through seven ways to achieve data persistence that can be easily integrated into Shiny apps. For each method, we will explain the method and provide a version of `saveData()` and `loadData()` that implements the method. To use a method as the storage type in the example app, run the app with the appropriate version of `saveData()` and `loadData()`.

As a reminder, you can see all the seven different storage types being used, along with the exact code used, [in this live Shiny app](http://daattali.com/shiny/persistent-data-storage/).

Here is a summary of the different storage types we will learn to use.

| Method            |       Data type      | Local storage | Remote storage | R package    |
|-------------------|----------------------|:-------------:|:--------------:|--------------|
| Local file system | Arbitrary data       |      YES      |                | -            |
| Dropbox           | Arbitrary data       |               |       YES      | rdrop2       |
| Amazon S3         | Arbitrary data       |               |       YES      | RAmazonS3    |
| SQLite            | Structured data      |      YES      |                | RSQLite      |
| MySQL             | Structured data      |      YES      |       YES      | RMySQL       |
| Google Sheets     | Structured data      |               |       YES      | googlesheets |
| MongoDB           | Semi-structured data |      YES      |       YES      | rmongodb     |

## Store arbitrary data in a file

This is the most flexible option to store data since files allow you to store any type of data, whether it is a single value, a big *data.frame*, or any arbitrary data. There are two common cases for using files to store data: 

1. you have one file that gets repeatedly overwritten and used by all sessions (like the example in [Jeff Allen's article](http://shiny.rstudio.com/articles/share-data.html)), or
2. you save a new file every time there is new data 
 
In our case we'll use the latter because we want to save each response as its own file. We can use the former option, but then we would introduce the potential for [race conditions](https://en.wikipedia.org/wiki/Race_condition#File_systems) which will overcomplicate the app. A race condition happens when two users submit a response at the exact same time, but since the file cannot deal with multiple edits simultaneously, one user will overwrite the response of the other user. 


When saving multiple files, it is important to save each file with a different file name to avoid overwriting files. There are many ways to do this. For example, you can simply use the current timestamp and an *md5 hash* of the data being saved as the file name to ensure that no two form submissions have the same file name.

Arbitrary data can be stored in a file either on the local file system or on remote services such as Dropbox or Amazon S3.

### 1. Local file system (**local**) {#local}

The most trivial way to save data from Shiny is to simply save each response as its own file on the current server. To load the data, we simply load all the files in the output directory. In our specific example, we also want to concatenate all of the data files together into one *data.frame*.

**Setup:** The only setup required is to create an output directory (responses in this case) and to ensure that the Shiny app has file permissions to read/write in that directory.
 

**Code:**

~~~
outputDir <- "responses"

saveData <- function(data) {
  data <- t(data)
  # Create a unique file name
  fileName <- sprintf("%s_%s.csv", as.integer(Sys.time()), digest::digest(data))
  # Write the file to the local system
  write.csv(
    x = data,
    file = file.path(outputDir, fileName), 
    row.names = FALSE, quote = TRUE
  )
}

loadData <- function() {
  # Read all the files into a list
  files <- list.files(outputDir, full.names = TRUE)
  data <- lapply(files, read.csv, stringsAsFactors = FALSE) 
  # Concatenate all data together into one data.frame
  data <- do.call(rbind, data)
  data
}
~~~

### 2. Dropbox (**remote**) {#dropbox}

If you want to store arbitrary files with a remote hosted solution instead of the local file system, you can store files on [Dropbox](https://www.dropbox.com). Dropbox is a file storing service which allows you to host any file, up to a certain maximum usage. The free account provides plenty of storage space and should be enough to store most data from Shiny apps.

This approach is similar to the previous approach that used the local file system. The only difference is that now that files are being saved to and loaded from Dropbox. You can use the [`rdrop2`](https://github.com/karthik/rdrop2) package to interact with Dropbox from R. Note that `rdrop2` can only move existing files onto Dropbox, so we still need to create a local file before storing it on Dropbox.

**Setup:** You need to have a Dropbox account and create a folder to store the responses. You will also need to add authentication to `rdrop2` with any approach [suggested in the package README](https://github.com/karthik/rdrop2#accessing-dropbox-on-shiny-and-remote-servers). The authentication approach I chose was to authenticate manually once and to copy the resulting `.httr-oauth` file that gets created into the Shiny app's folder. 

**Code:**

~~~
library(rdrop2)
outputDir <- "responses"

saveData <- function(data) {
  data <- t(data)
  # Create a unique file name
  fileName <- sprintf("%s_%s.csv", as.integer(Sys.time()), digest::digest(data))
  # Write the data to a temporary file locally
  filePath <- file.path(tempdir(), fileName)
  write.csv(data, filePath, row.names = FALSE, quote = TRUE)
  # Upload the file to Dropbox
  drop_upload(filePath, dest = outputDir)
}

loadData <- function() {
  # Read all the files into a list
  filesInfo <- drop_dir(outputDir)
  filePaths <- filesInfo$path
  data <- lapply(filePaths, drop_read_csv, stringsAsFactors = FALSE)
  # Concatenate all data together into one data.frame
  data <- do.call(rbind, data)
  data
}
~~~

### 3. Amazon S3 (**remote**) {#s3}

Another popular alternative to Dropbox for hosting files online is [Amazon S3](http://aws.amazon.com/s3/), or *S3* in short. Just like with Dropbox, you can host any type of file on S3, but instead of placing files inside directories, in S3 you place files inside of *buckets*. You can use the [`RAmazonS3`](http://www.omegahat.org/RAmazonS3/) package to interact with S3 from R. Note that the package is a few years old and is not under active development, so use it at your own risk.

**Setup:** You need to have an [Amazon Web Services](http://aws.amazon.com/) account and to create an S3 bucket to store the responses. As the [package documentation](http://www.omegahat.org/RAmazonS3/s3amazon.html) explains, you will need to set the `AmazonS3` global option to enable authentication.

**Code:**

~~~
library(RAmazonS3)

s3BucketName <- "my-unique-s3-bucket-name"
options(AmazonS3 = c('login' = "secret"))

saveData <- function(data) {
  # Create a unique file name
  fileName <- sprintf("%s_%s.csv", as.integer(Sys.time()), digest::digest(data))
  # Upload the data to S3
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
  # Get a list of all files
  files <- listBucket(s3BucketName)$Key
  files <- as.character(files)
  # Read all files into a list
  data <- lapply(files, function(x) {
      raw <- getFile(s3BucketName, x, virtual = TRUE)
      read.csv(text = raw, stringsAsFactors = FALSE)
  })
  # Concatenate all data together into one data.frame
  data <- do.call(rbind, data)
  data  
}
~~~

## Store structured data in a table

If the data you want to save is structured and rectangular, storing it in a table would be a good option. Loosely defined, structured data means that each observation has the same fixed fields, and rectangular data means that all observations contain the same number of fields and fit into a nice 2D matrix. A *data.frame* is a great example of such data, and thus data.frames are ideal candidates to be stored in tables such as relational databases. 

Structured data must have some *schema* that defines what the data fields are. In a *data.frame*, the number and names of the columns can be thought of as the schema. In tables with a header row, the header row can be thought of as the schema.

Structured data can be stored in a table either in a relational database (such as SQLite or MySQL) or in any other table-hosting service such as Google Sheets. If you have experience with database interfaces in other languages, you should note that R does not currently have support for prepared statements, so any SQL statements have to be constructed manually. One advantage of using a relational database is that with most databases it is safe to have multiple users using the database concurrently without running into race conditions thanks to [transaction support](https://en.wikipedia.org/wiki/Database_transaction).

### 4. SQLite (**local**) {#sqlite}

SQLite is a very simple and light-weight relational database that is very easy to set up. SQLite is serverless, which means it stores the database locally on the same machine that is running the shiny app. You can use the [`RSQLite`](https://github.com/rstats-db/RSQLite) package to interact with SQLite from R. To connect to a SQLite database in R, the only information you need to provide is the location of the database file.  

To store data in a SQLite database, we loop over all the values we want to add and use a [SQL INSERT](http://www.w3schools.com/sql/sql_insert.asp) statement to add the data to the database. It is essential that the schema of the database matches exactly the names of the columns in the Shiny data, otherwise the SQL statement will fail. To load all previous data, we use a plain [SQL SELECT *](http://www.w3schools.com/sql/sql_select.asp) statement to get all the data from the database table.

**Setup:** First, you must have SQLite installed on your server. Installation is fairly easy; for example, on an Ubuntu machine you can install SQLite with `sudo apt-get install sqlite3 libsqlite3-dev`. If you use shinyapps.io, SQLite is already installed on the shinyapps.io server, which will be a handy feature in future versions of shinyapps.io, which will include persistent local storage.

You also need to create a database and a table that will store all the responses. When creating the table, you need to set up the schema of the table to match the columns of your data. For example, if you want to save data with columns "name" and "email" then you can create the SQL table with `CREATE TABLE responses(name TEXT, email TEXT);`. Make sure the shiny app has write permissions on the database file and its parent directory.

**Code:**

~~~
library(RSQLite)
sqlitePath <- "/path/to/sqlite/database"
table <- "responses"

saveData <- function(data) {
  # Connect to the database
  db <- dbConnect(SQLite(), sqlitePath)
  # Construct the update query by looping over the data fields
  query <- sprintf(
    "INSERT INTO %s (%s) VALUES ('%s')",
    table, 
    paste(names(data), collapse = ", "),
    paste(data, collapse = "', '")
  )
  # Submit the update query and disconnect
  dbGetQuery(db, query)
  dbDisconnect(db)
}

loadData <- function() {
  # Connect to the database
  db <- dbConnect(SQLite(), sqlitePath)
  # Construct the fetching query
  query <- sprintf("SELECT * FROM %s", table)
  # Submit the fetch query and disconnect
  data <- dbGetQuery(db, query)
  dbDisconnect(db)
  data
}
~~~

### 5. MySQL (**local or remote**) {#mysql}

MySQL is a very popular relational database that is similar to SQLite but is more powerful. MySQL databases can either be hosted locally (on the same machine as the Shiny app) or online using a hosting service.

This method is very similar to the previous SQLite method, with the main difference being where the database is hosted. You can use the [`RMySQL`](https://github.com/rstats-db/RMySQL) package to interact with MySQL from R. Since MySQL databases can be hosted on remote servers, the command to connect to the server involves more parameters, but the rest of the saving/loading code is identical to the SQLite approach. To connect to a MySQL database, you need to provide the following parameters: host, port, dbname, user, password.

**Setup:** You need to create a MySQL database (either locally or using a web service that hosts MySQL databases) and a table that will store the responses. As with the setup for SQLite, you need to make sure the table schema is properly set up for your intended data.

**Code:**

~~~
library(RMySQL)

options(mysql = list(
  "host" = "127.0.0.1",
  "port" = 3306,
  "user" = "myuser",
  "password" = "mypassword"
))
databaseName <- "myshinydatabase"
table <- "responses"

saveData <- function(data) {
  # Connect to the database
  db <- dbConnect(MySQL(), dbname = databaseName, host = options()$mysql$host, 
      port = options()$mysql$port, user = options()$mysql$user, 
      password = options()$mysql$password)
  # Construct the update query by looping over the data fields
  query <- sprintf(
    "INSERT INTO %s (%s) VALUES ('%s')",
    table, 
    paste(names(data), collapse = ", "),
    paste(data, collapse = "', '")
  )
  # Submit the update query and disconnect
  dbGetQuery(db, query)
  dbDisconnect(db)
}

loadData <- function() {
  # Connect to the database
  db <- dbConnect(MySQL(), dbname = databaseName, host = options()$mysql$host, 
      port = options()$mysql$port, user = options()$mysql$user, 
      password = options()$mysql$password)
  # Construct the fetching query
  query <- sprintf("SELECT * FROM %s", table)
  # Submit the fetch query and disconnect
  data <- dbGetQuery(db, query)
  dbDisconnect(db)
  data
}
~~~

### 6. Google Sheets (**remote**) {#gsheets}

If you don’t want to deal with the formality and rigidity of a database, another option for storing tabular data is in a Google Sheet. One nice advantage of Google Sheets is that they are easy to access from anywhere; but unlike with databases, with Google Sheets data can be overwritten with multiple concurrent users.

You can use the [`googlesheets`](https://github.com/jennybc/googlesheets) package to interact with Google Sheets from R. To connect to a specific sheet, you will need either the sheet’s title or key (preferably key, as it is unique). It is very easy to store or retrieve data from a Google Sheet, as the code below shows.

**Setup:** All you need to do is create a Google Sheet and set the top row with the names of the fields. You can do that either via a web browser or by using the `googlesheets` package. You also need to have a Google account. The `googlesheets` package uses a similar approach to authentication as `rdrop2`, and thus you also need to authenticate in a similar fashion, such as by copying a valid `.httr-oauth` file to your Shiny directory.

**Code:**

~~~
library(googlesheets)

table <- "responses"

saveData <- function(data) {
  # Grab the Google Sheet
  sheet <- gs_title(table)
  # Add the data as a new row
  gs_add_row(sheet, input = data)
}

loadData <- function() {
  # Grab the Google Sheet
  sheet <- gs_title(table)
  # Read the data
  gs_read_csv(sheet)
}
~~~

## Store semi-structured data in a NoSQL database

If you have data that is not fully structured but is also not completely free-form, a good middle ground can be using a NoSQL database. NoSQL databases can also be referred to as schemaless databases because they do not use a formal schema. NoSQL databases still offer some of the benefits of a traditional relational database, but are more flexible because every entry can use different fields. If your Shiny app needs to store data that has several fields but there is no unifying schema for all of the data to use, then using a NoSQL database can be a good option.

There are many NoSQL databases available, but here we will only show how to use mongoDB.

### 7. MongoDB (**local or remote**) {#mongodb}

MongoDB is one of the most popular NoSQL databases, and just like MySQL it can be hosted either locally or remotely. There are many web services that offer mongoDB hosting, including [MongoLab](https://mongolab.com/) which gives you free mongoDB databases. In mongoDB, entries (in our case, responses) are stored in a *collection* (the equivalent of an S3 bucket or a SQL table).

You can use either the [`rmongodb`](https://github.com/mongosoup/rmongodb) or [`mongolite`](https://github.com/jeroenooms/mongolite) package to interact with mongoDB from R. In this example we will use `rmongodb`, but `mongolite` is a perfectly good alternative. As with the relational database methods, all we need to do in order to save/load data is connect to the database and submit the equivalent of an update or select query. To connect to the database you need to provide the following: db, host, username, password. When saving data to mongoDB, the data needs to be converted to BSON (binary JSON) in order to be inserted into a mongoDB collection. MongoDB automatically adds a unique "id" field to every entry, so when retrieving data, we manually remove that field.

**Setup:** All you need to do is create a mongoDB database—either locally or using a web service such as MongoLab. Since there is no schema, it is not mandatory to create a collection before populating it.

**Code:**

~~~
library(rmongobd)

options(mongodb = list(
  "host" = "ds012345.mongolab.com:61631",
  "username" = "myuser",
  "password" = "mypassword"
))
databaseName <- "myshinydatabase"
collectionName <- "myshinydatabase.responses"

saveData <- function(data) {
  # Connect to the database
  db <- mongo.create(db = databaseName, host = options()$mongodb$host, 
      username = options()$mongodb$username, password = options()$mongodb$password)
  # Convert the data to BSON (Binary JSON)
  data <- mongo.bson.from.list(as.list(data))
  # Insert the data into the mongo collection and disconnect
  mongo.insert(db, collectionName, data)
  mongo.disconnect(db)
}

loadData <- function() {
  # Connect to the database
  db <- mongo.create(db = databaseName, host = options()$mongodb$host, 
      username = options()$mongodb$username, password = options()$mongodb$password)
  # Get a list of all entries
  data <- mongo.find.all(db, collectionName)
  # Read all entries into a list
  data <- lapply(data, data.frame, stringsAsFactors = FALSE)
  # Concatenate all data together into one data.frame 
  data <- do.call(rbind, data)
  # Remove the ID variable
  data <- data[ , -1, drop = FALSE]
  # Disconnect
  mongo.disconnect(db)
  data
}
~~~

# Conclusion

Persistent storage lets you do more with your Shiny apps. You can even use persistent storage to access and write to remote data sets that would otherwise be too big to manipulate in R.

The following table can serve as a reminder of the different storage types and when to use them. Remember that any method that uses local storage can only be used on Shiny Server, while any method that uses remote storage can be also used on *shinyapps.io*.

| Method            |       Data type      | Local storage | Remote storage | R package    |
|-------------------|----------------------|:-------------:|:--------------:|--------------|
| Local file system | Arbitrary data       |      YES      |                | -            |
| Dropbox           | Arbitrary data       |               |       YES      | rdrop2       |
| Amazon S3         | Arbitrary data       |               |       YES      | RAmazonS3    |
| SQLite            | Structured data      |      YES      |                | RSQLite      |
| MySQL             | Structured data      |      YES      |       YES      | RMySQL       |
| Google Sheets     | Structured data      |               |       YES      | googlesheets |
| MongoDB           | Semi-structured data |      YES      |       YES      | rmongodb     |
