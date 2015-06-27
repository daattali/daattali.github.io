---
layout: post
title: "Persistent data storage in shiny apps"
---


NOTES
====
- saving data to persistent storage
- Store arbitrary data of any size in an object storage system: amazon S3, dropbox
- Store semi-structured data in an schemaless database (mongodb)
- Store rigidly structured data in a formal relational database (mysql, sqlite, googlesheets)
- store structured data locally
- shiny app available at http://daattali.com/shiny/persistent-data-storage/
- mention that sql packages don't support input sanitization and kinda weird to hack together sql statements with sprintf rather than using prepared statements  
- one more approach im not demonstrating or talking about: storing all responses in ONE file (either locally or remotely). I don't want to deal with concurrency issues that arise from a few users submitting at the same time, even though it's very unlikely to happen unless we're dealing with a huge volume of submissions. This only means that when I want to write, the only operation I have to do is a write, rather than read the existing file and append to it.
- after publishing post: add comment on http://shiny.rstudio.com/articles/share-data.html
- update gist
- update mimic-google-form blog post (and maybe shiny app as well)
- update persisiten-data-storage shiny app
