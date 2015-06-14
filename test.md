---
layout: post
url: "2015-06-14-mimicking-google-form-shiny"
title: "Mimicking a Google Form with a Shiny app"
tags: [professional, rstats, r, r-bloggers, shiny]
date: 2015-06-14 11:30:00 -0700
---

In this post we will walk through the steps required to build a shiny app that mimicks a Google Form. It will allow users to submit responses to some input fields, save their data, and allow admins to view the submitted responses.  

# Table of contents

- [Motivation](#motivation)
  - [Note about persistent storage]{#note-storage}


# Motivation {#motivation}

Last year I was fortunate enough to be a teaching assistant for [STAT545](https://twitter.com/stat545) - a course at the University of British Columbia, taught by [Jenny Bryan](https://twitter.com/JennyBryan), that introduces R into the lives of student scientists. (It was especially special to me because just 12 months prior, that course taught *me* how to write my first line in R.) To facilitate communication with the students, we wanted to gather some basic information from them, such as their prefered name, email, and Twitter and Github handles so that. We didn't want to simply have them all send us an email with this information, as we were trying to set an example of how to be techy and automated and modern :) Our first thought was to use a Google Form, but university policies didn't quite allow for that because the data must be stored within Canadian borders, and with Google Forms it'd probably end up somewhere in the US. We also decided earlier that day that one of the course modules will be about shiny, so we figured we'd put our money where our mouths were, and attempt to collect student data via a shiny app. I was given the task of developing this shiny app, which was a great learning experience. You can view the original code for that app [on GitHub](https://github.com/daattali/UBC-STAT545/tree/master/shiny-apps/request-basic-info) or [visit the app yourself to see it in action](http://daattali.com/shiny/request-basic-info/).

The idea of recording user-submitted form data can be applied to many differente scenarios. Seeing how successful the previous app was for our course, we decided to also collect all peer reviews of assignments in a similar shiny app. I created an app with a template for a marking sheet, and every week students would use the app to submit reviews for other students' work. This worked great for us - you can see the original code [on GitHub](https://github.com/daattali/UBC-STAT545/tree/master/shiny-apps/marking-sheet) or [try the app out yourself](http://daattali.com/shiny/peer-review/).

Since developing those apps, I've become a better shiny developer also wrote the [shinyjs package](https://github.com/daattali/shinyjs) to help with many user-experience stuff like hiding/disabling/resetting inputs. I've also seen multiple people asking how to do this kind of thing with shiny, so my hope is that this post will be useful for others who are also looking to create user-submitted forms with shiny.

#### Note about persistent storage {#note-storage}

One major component of this app is storing the user-submitted data in a way that would allow it to be retrieved later. This is an important topic of its own, and in a few days I will write a detailed post about all the different storage options and how to use them. In this post I will

