---
title: "\"package 'foo' is not available\" - What to do when R tells you it can't install a package"
share-title: "'package 'foo' is not available' - What to do when R tells you it can't install a package"
subtitle: "This common error means the package you're looking for can't be installed from CRAN. This is how you solve it."
thumbnail-img: "/assets/img/blog/packageunavailable/packageunavailable.png"
tags: [rstats, packages, shiny, tutorial]
permalink: /blog/packageunavailable/
date: 2020-10-05 11:00:00 -0400
---

The R language is infamous among its users for often having unhelpful error messages. One of the error messages that almost any R user in history has seen is "package 'foo' is not available (for R version x.y.z)", which would come after trying to install package "foo" from CRAN using `install.packages("foo")`.

Sometimes this message comes at very unexpected times, like when you're sure that you were able to install this package every day for the past year, so why would it not succeed now? While this message isn't completely cryptic, it isn't too helpful either. All it tells us is that your R session thinks the package is unavailable on CRAN, but there can be many reasons why, and each one requires a different solution.

Below are some of the common reasons I've run into this error, and they're listed in chronological order of what I look for when I get the error. 

{:.box-note}
Note: This is not meant to be an exhaustive list of all the reasons a package won't install, but it should hopefully cover most cases. If you have any more to add, let me know!

# Table of contents

- [1. Misspelling](#misspelling)
- [2. Check your network](#yournetwork)
- [3. Check CRAN's network](#crannetwork)
- [4. Make sure the package is released on CRAN](#released)
- [5. Make sure the package hasn't been removed from CRAN](#deleted)
  - [Option 1: Use the source GitHub repository](#deleted-github)
  - [Option 2: Use GitHub's read-only CRAN mirror](#deleted-mirror)
  - [Option 3: Install an archived CRAN version](#deleted-archive)
  - [Option 4: Install from source](#deleted-source)

## 1. Misspelling {#misspelling}

  The first thing I do is make sure I spelled the package correctly. This means that capitalization must be correct too! For example, if you run `install.packages("Rmarkdown")`, it will fail because the package name is "rmarkdown" (lower case). Look up the package name and verify you have the correct spelling. Protip: using copy-paste is a good way to ensure that. 

## 2. Check your network {#yournetwork}

  After verifying the package spelling is correct, the next thing I do is make sure I have internet connection. This may sound obvious, but it's very easy to overlook, and sometimes you can be so caught up in writing code that you didn't notice the one-hour WiFi at the coffee shop has expired and that you're no longer connected. If your computer can't reach CRAN, it makes sense that R can't find the package on CRAN!

## 3. Check CRAN's network {#crannetwork}

  On a handful of occasions, I wasn't able to install any package even though I clearly had internet. This is pretty rare, but it is possible for the [default CRAN mirror](https://cran.r-project.org) to be down. So after verifying my own network connection, I've learned to also verify that the CRAN main mirror is up by visiting [its website](https://cran.r-project.org). If it's down, you can either wait until it's back up, or use a different CRAN mirror (using the `repos` parameter of `install.packages()`).

## 4. Make sure the package is released on CRAN {#released}

Often times you receive a piece of code from a colleague that has 10 different `library()` calls at the top using packages you've never heard of before. You try to install them all, and one of the packages gives you this error. It's not uncommon for this error to happen simply because the package is not actually on CRAN. The package you're trying to install might be on GitHub or Bioconductor or any other repository. Usually a quick Google search will help you find where the package is hosted. If it's not on CRAN, you'll need to follow the package's instructions for how to install it.

## 5. Make sure the package hasn't been removed from CRAN {#deleted}

Sometimes a package that was previously on CRAN is no longer on CRAN, which can be very frustrating. Many scripts and packages make the assumption that once a package is on CRAN, that means it's  permanent and stable and that `install.packages()` should work forever. It's an understandable assumption to make, but sometimes packages do get removed from CRAN (usually because they no longer pass all the CRAN checks, and the maintainer hasn't fixed it in the given time).

You can check if a package is still on CRAN by visiting its CRAN page (https://cran.r-project.org/package=PACKAGE_NAME). If you hit a 404 error page, that means the pacakge was likely never on CRAN. If you see all the information about the package, that means the package is on CRAN and should be installable. However, if you get a message like "Package ‘foo’ was removed from the CRAN repository.", then the package is no longer available. 

When you find yourself in this situation, you have a few options to try.

#### Option 1: Use the source GitHub repository {#deleted-github}

A few months ago, I had a script that was running every few days over several months, and one day it randomly stopped working. It turns out that it failed because it tried to install the package `aws.s3`, but the package has been recently [orphaned](https://cran.r-project.org/src/contrib/Orphaned/README) and removed from CRAN. Many R packages are hosted on GitHub, and a quick Google search of "r aws.s3 github" revealed that this package [is on GitHub](https://github.com/cloudyr/aws.s3) as well. Any R package on GitHub can be installed using `remotes::install_github()`, so I simply replaced the `install.packages()` call with `remotes::install_github("cloudyr/aws.s3")`.

#### Option 2: Use GitHub's read-only CRAN mirror {#deleted-mirror}

If the package doesn't have its own GitHub repository, another option is to use CRAN's GitHub mirror. Any package on CRAN, including packages that have been deleted, has a read-only GitHub repository that acts as a mirror for the package's source code. Using the [GitHub CRAN mirror](https://github.com/cran/aws.s3), I could install the package using the same function and just specifying the CRAN repository: `remotes::install_github("cran/aws.s3")`.

#### Option 3: Install an archived CRAN version {#deleted-archive}

Just last week, I tried running a very old script of mine that used the `ProbitSpatial` package. The CRAN page for the package tells me that the package was archived a few months ago. When a package is archived, any previous versions that were released to CRAN in the past are available in CRAN's archive of that package. By looking at the archive you can see what versions exist, and any version in CRAN's archive can be installed using `devtools::install_version()`. In my case, there was a version "1.0" in the archive, so I could install the package with `devtools::install_version("ProbitSpatial", "1.0")`.

#### Option 4: Install from source {#deleted-source}

If you happen to get your hands on the package's source code (in the form of a `.tar.gz` file), you can install the package using that file. I personally haven't had to resort to that, but it is an option. You can install a package if you have the source locally using `install.packages('/path/to/package/ProbitSpatial_0.1.tar.gz', repos = NULL, type = 'source')`.
