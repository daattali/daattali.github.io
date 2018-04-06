---
title: How to use dplyr::mutate in R without a vectorized function
tags: [professional, rstats, r-bloggers, tutorial]
permalink: /blog/mutate-non-vectorized/
date: 2018-04-09 11:00:00 -0400
---

TL;DR: Use the `Vectorize()` function! It can be used for any other intance where you ever need to create a vectorized version of a function. But let's back up a bit.

If you're reading this, you've either encountered this problem before, or you just got to this article out of curiousity (in which case you probably don't know what problem I'm talking about).

A few days ago I was given code by a client for a function that, given a path to a patient's record file, generates a useful ID for the patient.

library(stringr)
library(dplyr)

patient_name <- function(path) {
  path_list <- str_split(path, "/") %>% unlist()
  paste(path_list[length(path_list) - 1], path_list[length(path_list)], sep = "_")
}
