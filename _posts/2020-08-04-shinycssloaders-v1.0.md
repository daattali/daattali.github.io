---
title: "{shinycssloaders} v1.0: You can now use your own image, plus 3 years' worth of new features!"
tags: [professional, rstats, r-bloggers, packages, shiny]
share-img: "https://deanattali.com/img/blog/shinycssloaders/shinycssloaders.png"
permalink: /blog/shinycssloaders-v1.0/
date: 2020-07-28 11:00:00 -0400
gh-repo: daattali/shinycssloaders
gh-badge: [follow, star]
---

The [{shinycssloaders}](https://github.com/daattali/shinycssloaders/) package is a handy little tool that lets you easily add loading animations to a Shiny output (a plot, a table, etc.) while it's recalculating.

[![demo gif]({{ site.url }}/img/blog/shinycssloaders/shinycssloaders-demo-gif.gif)]({{ site.url }}/img/blog/shinycssloaders/shinycssloaders-demo-gif.gif)

It's extremely easy to use--all you need to do is wrap the output in `withSpinner()`. For example, `plotOutput("plot")` becomes `plotOutput("plot") %>% withSpinner()`.

Version 1.0.0 is now available [on CRAN](https://cran.r-project.org/package=shinycssloaders). Other than a minor release ealier this year, the last CRAN release was over 3 years ago, so there's lots of [goodies to unpack](https://github.com/daattali/shinycssloaders/blob/master/NEWS.md)! For a preview of what this package can do, check out a demo below.

<div style="text-align:center;">
<a class="btn btn-lg btn-success" href="https://daattali.com/shiny/shinycssloaders-demo/">Check out a demo</a>
</div>


## The road to {shinycssloaders}

If you're a veteran Shiny user, you may recognize that I'm not the original author of this package. It was created back in May 2017 by [András Sali](https://github.com/andrewsali) (both versions 0.1 and 0.2 were submitted within 3 days--how he got CRAN to allow that is a mystery to me). 

Over the years, there haven't been too many changes, so a new CRAN version was never released. But when I started using {shinycssloaders} in my own apps, I noticed a tiny issue: some debugging information was printed out to the JavaScript console. It's by no means a big deal, just a forgotten debugging statement that made it into CRAN, but I'm not [the only one](https://github.com/daattali/shinycssloaders/issues/6) that [noticed](https://github.com/daattali/shinycssloaders/issues/26). This was fixed in August 2017, but the since the package was never again submitted to CRAN, the official version still had that issue even 2 years after it was fixed.

The package seemed to have gone into dormant mode--it had an active user base, but no active development. But I didn't even want active development, all I wanted is to submit the current version to CRAN! So last year I emailed András asking him if there are any plans to continue support for {shinycssloaders}, and we decided that I'll take over. I was scared because I've never inherited an open source project from anyone before, and here I am signing up to maintain a codebase that I don't know anything about, for free, just because I wanted to know when the current code will be on CRAN... Eek.

## A bumpy start...

My initial goal with {shinycssloaders} was very uninspiring: take the current code, and submit to CRAN as version 0.3. And that's literally all I did. I informed CRAN that I'm the new maintainer and submitted the package as-is. Hooray, finally we won't have the debugging statement show up! I figured since this code has been on GitHub for over 2 years and many people (myself included) have been using it for a long time, it was safe to submit it.

But life would be too boring if things *just worked out* the way they should. 

Within a few days of the new version being on CRAN, I got flooded with emails and GitHub issues caused by the new version. To this day I don't comprehend how these issues existed in the GitHub version for over 2 years undetected. I started to regret taking over this package, because I was now finding myself spending hours and hours trying to master the codebase and deal with all these old-new issues. 

{shinycssloaders} and I were not off to a great start.

## ...But a bright future!

It took a while, but eventually all the bugs that were introduced with version 0.3 were fixed! But I wasn't ready to submit a new version just yet, version 0.4 needed to be more ambitious.

I looked through all the GitHub issues and PRs that have accumulated since 2017 and addressed all of them. That involved fixing many bugs and supporting some new features. Since I already read the entire codebase by this point, I decided to also do some major refactoring in order to simplify any future work. The documentation (mainly README) was verbose and unclear, so that was completely revamped as well.

I've added a few parameters, such as one that allows you to [use a custom image](https://github.com/daattali/shinycssloaders/issues/46) instead of the built-in animations, and another parameter that lets you [keep the output greyed](https://github.com/daattali/shinycssloaders/issues/22) out but still visible while the loading animation is on top of it (by default the output is hidden while the loading animation is shown). I also created a [demo shiny app](https://daattali.com/shiny/shinycssloaders-demo/) showcasing what the package does, as I think an interactive demo is the best way to quickly see what a package is all about.

For a complete list of bug fixes and new feautres, you can look at the **[NEWS file](https://github.com/daattali/shinycssloaders/blob/master/NEWS.md)**. Since so many new features and bug fixes were introduced, including changing the license from GPL to MIT, I decided to do a major version upgrade and call this version 1.0! [{shinycssloaders}](https://github.com/daattali/shinycssloaders/) is now at a good place and I invite you to use it in your next Shiny app!
