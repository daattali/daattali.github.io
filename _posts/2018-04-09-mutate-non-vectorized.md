---
title: How to use dplyr's mutate in R without a vectorized function
tags: [professional, rstats, r-bloggers, tutorial]
permalink: /blog/mutate-non-vectorized/
date: 2018-04-09 12:00:00 -0400
---

**TL;DR: Use the `Vectorize()` function!**

If you're reading this, you've either encountered this problem before, or you just got to this article out of curiousity (in which case you probably don't know what problem I'm talking about).

A few days ago I was given code by a client for a function that, given a path to a patient's file, generates a useful ID for the patient. I won't post the actual function, but it was something along the lines of this:

```r
library(stringr)
library(dplyr)

patient_name <- function(path) {
  path_list <- str_split(path, "/") %>% unlist()
  paste(path_list[length(path_list) - 1], path_list[length(path_list)], sep = "_")
}
```

Given a path `some/path/abc/001.txt`, this function will return `abc_001.txt`. So far, so good. (There are better ways to implement this function, but that's not the point here).

I had a dataframe with file paths as a column, and I needed to add this ID as another column. Normally, this would be easily achieved with a simple `mutate()` (from `dplyr`):

```r
df <- data.frame(path = c("some/path/abc/001.txt", "another/directory/xyz/002.txt"),
                 stringsAsFactors = FALSE)
df %>% mutate(patient_name = patient_name(path))
```

This code does run, but it's not correct. Here's the output:

```
                           path patient_name
1         some/path/abc/001.txt  xyz_002.txt
2 another/directory/xyz/002.txt  xyz_002.txt
```

You can see the result is incorrect, and it's because `patient_name()` is not a vectorized function - it assumes that its input is just a single path, and doesn't know how to properly work when given multiple paths.

Usually when I write my own code, I try to make my functions vectorized, so that you can call them with both a single element or with a vector. But in this case, I wasn't allowed to modify the code to make the function vectorized. So how do we easily vectorize `patient_name()` without modifying its code? We can use the `Vectorize()` function!

```r
patient_name_v <- Vectorize(patient_name)
df %>% mutate(patient_name = patient_name_v(path))
```

This actually gives us the correct result:

```
                           path patient_name
1         some/path/abc/001.txt  abc_001.txt
2 another/directory/xyz/002.txt  xyz_002.txt
```

This is only one usecase of the `Vectorize()` function. It can come in handy whenever you need to vectorize a non-vectorized function.

For example, it seems that the `nrow()` function is not vectorized, because if I try to create a list with two dataframes in it and get the number of rows, I get `NULL`:

```r
dflist <- list(mtcars, iris)
nrow(dflist)
# NULL
```

I hypothesized that vectorizing it will do the trick, and it indeed seems to work!

```r
(Vectorize(nrow))(dflist)
# [1]  32 150
```

**UPDATE:** As is usually the case with useful R functions, there are other packages (particularly `purrr` as a few people have mentioned) that have functions to achieve similar things that may be more efficient and flexible for different situations. I can't comment on when you should use each method - whatever works for your case and makes sense for you will be the right approach. Also note that `Vectorize()` has two parameters `SIMPLIFY` and `USE.NAMES` that both default to `TRUE` and I suggest setting them to `FALSE`.
