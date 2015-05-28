---
layout: post
title: "How to mock a Google Form with Shiny + different ways to use persistent data with Shiny"
---

TODO write this up  

A lot of the writeup about each storage method is already in the shiny app

add rdrop2


NOTES
====
- draft shiny app available at http://daattali.com/shiny/google-form-mock/
- mention that sql packages don't support input sanitization and kinda weird to hack together sql statements with sprintf rather than using prepared statements  
- one more approach im not demonstrating or talking about: storing all responses in ONE file (either locally or remotely). I don't want to deal with concurrency issues that arise from a few users submitting at the same time, even though it's very unlikely to happen unless we're dealing with a huge volume of submissions. This only means that when I want to write, the only operation I have to do is a write, rather than read the existing file and append to it.
- after publishing post: add comment on http://shiny.rstudio.com/articles/share-data.html  
- show jenny the problems i'm having with googlesheets
