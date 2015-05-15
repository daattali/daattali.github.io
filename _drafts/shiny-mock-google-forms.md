---
layout: post
title: "How to mock a Google Form with Shiny + different ways to use persistent data with Shiny"
---

TODO write this up




NOTES
====

data persistence:  
- http://shiny.rstudio.com/articles/share-data.html 
- S3  
- [mondodb](https://www.mongolab.com)
- [mysql](http://www.freemysqlhosting.net/)
- sqlite
- gspreadr
- local storage

mysql: `con <- DBI::dbConnect(RMySQL::MySQL(), group = "deang", default.file=normalizePath("~/.my.cnf"))`
not concerned about safety/input sanitization
doesnt seem like `RMySQL` supports preparesd statemetns so need to "hack" together sql statement creation with paste/sprintf
install sqlite: `sudo apt-get install sqlite3 libsqlite3-dev`, create database: `sqlite3 databasename.db`


after publishing post: add comment on http://shiny.rstudio.com/articles/share-data.html
