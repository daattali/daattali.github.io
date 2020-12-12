---
title: "Migrating from TravisCI to GitHub Actions for R packages"
subtitle: "Learn how to easily start using GitHub Actions to automatically validate your package"
thumbnail-img: "/assets/img/blog/migrating-travis-to-github/cover.png"
tags: [rstats, github actions, tutorial, youtube]
permalink: /blog/migrating-travis-to-github/
date: 2020-12-12 12:00:00 -0500
---

If you develop an R package on GitHub and aren't using GitHub Actions, this post is for you.

Whether you want to move your repository from TravisCI to GitHub Actions, or you're not sure why that's necessary, or even if you don't know what GitHub Actions is, you should definitely read on.

If you prefer a video walkthrough instead of reading, you can watch a video version of this post:

<div style="text-align:center;">
  <a class="btn btn-lg btn-cta" href="https://youtu.be/K4x-uqLl_m4"><i class="fab fa-youtube"></i> Watch on YouTube</a>
</div>

# Table of contents

- [What is CI/CD?](#cicd)
- [TravisCI Rise to Power](#travis)
- [End of an Era: Travis Open Source is No More](#death)
- [GitHub Actions to the Rescue](#gha)
  - [Using GitHub Actions](#using-gha)
    - [1. Remove Travis from your project](#step1)
    - [2. Add a GitHub Action into your project](#step2)
- [Video](#video)

# What is CI/CD? {#cicd}
    
For those who aren't familiar with TravisCI - it's a popular CI/CD provider, which stands for Continuous Integration/Continuous Delivery. It might sound scary, but it's just a fancy term for something very useful: automating software workflows based on certain triggers in your GitHub repository. 

Some simple examples of what R developers use TravisCI for:

- Automatically run CRAN tests on your R package every time you commit new code
- Automatically run CRAN tests on your R package every time someone makes a pull request to your package
- Automatically build the {pkgdown} documentation website for your package on every commit
- Automatically generating a test coverage report 

You get the idea. Automating a workflow that you would otherwise do manually on a GitHub repository.

# TravisCI Rise to Power {#travis}

Ever since TravisCI started supporting R in early 2015 (thanks to Craig Citro, Jim Hester, and Jeroen Ooms), it's been the first thing I add to any R package I make.

TravisCI is not the only option--there are other CI/CD providers, and GitHub Actions is one of them. But since 2015 and until recently, Travis has been the standard choice for most R package developers, including myself. Every time I committed code to an R package GitHub repository, Travis would alert me within a few minutes if my new code broke something.

I, like many other developers, had a love-hate relationship with Travis for years: I loved it because it gave me the peace of mind of knowing that my package is constantly being checked, but hated it when it "randomly" complained that my tiny insignificant change somehow broke the package.

<div style="text-align:center;">
  <a href="/assets/img/blog/migrating-travis-to-github/travis.png">
    <img src="/assets/img/blog/migrating-travis-to-github/travis.png" alt="travis">
  </a>
</div>

Travis has served us all very well. 

# End of an Era: Travis Open Source is No More {#death}

As we all know, all good things come to an end. ROpenSci [recently wrote a post](https://ropensci.org/technotes/2020/11/19/moving-away-travis/) about why they're moving away from Travis and recommend others to do the same. Without getting into the politics of it all, this is the summary: Travis has operated independently for years, until getting acquired last year by a private firm. Shortly afterwards, many Travis engineers were laid off, enterprise services were being given priority, and the open-source portion of Travis started suffering. GitHub Actions was just released a few months prior, and because of the issues with Travis, many R users started jumping ship and switched to GitHub Actions.

I've been observing from the sidelines over the past year as the R community slowly switched from Travis as the default CI/CD to GitHub Actions. I was perfectly content with Travis and had no reason to learn GitHub Actions.

Until now. 

In ROpenSci's post, they reference a recent [announcement by TravisCI](https://blog.travis-ci.com/2020-11-02-travis-ci-new-billing) that puts the nail in the open-source Travis' coffin. Starting in 2021, they will no longer offer a free tier. Any new or existing non-paying Travis user will be given a certain amount of one-time "credits" and when they run out, they will no longer be able to use Travis for free.

I'm not opposed to this business model--I know very well from my own experience that building open source products and maintaining them is extremely difficult, and I'm not here to judge that business decision. But I use Travis for all my open source R packages that don't generate any income, so I would prefer to stay with a fully open source stack for these projects.

# GitHub Actions to the Rescue {#gha}

Because of this billing change, I finally have a very good reason to move away from Travis, and the obvious contender is GitHub Actions. I'll be moving all my R packages from Travis to GitHub Actions soon.

## Using GitHub Actions {#using-gha}

So how do we move away from Travis and use GitHub Actions instead? Below is the process I did with [{shinyjs}](https://github.com/daattali/shinyjs). If you don't currently use Travis or any other CI/CD service, you can just skip the first step.

### 1. Remove Travis from your project {#step1}

This includes:
  
- Remove the `travis.yml` configuration file
- Remove any references to `travis.yml` you may have in any ignore files (such as `.Rbuildignore`) 
- Remove any references to Travis in the README, since you probably had a "build status" badge
- Log into the TravisCI website and remove the GitHub repository from using Travis

### 2. Add a GitHub Action into your project {#step2}

You can do this by hand. But luckily for us, the excellent [{usethis} package](https://github.com/r-lib/usethis) package offers a super simple way to use GitHub Actions, by calling various functions. For my use case, I just want to use GitHub Actions to run CRAN checks after every commit or pull request, so I simply call `usethis::use_github_action_check_standard()`. If you look at the output in the console, it'll tell you exactly what steps were taken, and you'll be told that there is just one more step you should do manually: add the status badge to the README.

<div style="text-align:center;">
  <a href="/assets/img/blog/migrating-travis-to-github/usethis.png">
    <img src="/assets/img/blog/migrating-travis-to-github/usethis.png" alt="travis">
  </a>
</div>

And that's all you have to do. Just call that one {usethis} function.

The default configuation file that gets added runs the checks on Windows, Mac, and Ubuntu (using two different R versions). That's plenty of checking for me. Now every time new code is pushed to GitHub, GitHub will automatically run CRAN checks in all these environments and there'll be a little status icon next to the last commit message. If the checks pass it'll be a green checkmark, otherwise a red X. Clicking on that icon will show more details.

<div style="text-align:center;">
  <a href="/assets/img/blog/migrating-travis-to-github/gha.png">
    <img src="/assets/img/blog/migrating-travis-to-github/gha.png" alt="travis">
  </a>
</div>

If you want to use a different GitHub Action, the {usethis} package has a few more functions that provide different actions, and the [{actions} package](https://github.com/r-lib/actions) has even more good examples of useful GitHub Actions for R. If none of the actions there match your use case, you can read the official GitHub Actions documentation to write your own actions.

# Video {#video}

Lastly, if any of the above was unclear, you can watch me migrate my own package from Travis to GitHub Actions in realtime in the video below:

<div class="youtube-embed-container">
<iframe src="https://www.youtube-nocookie.com/embed/K4x-uqLl_m4" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
</div>
