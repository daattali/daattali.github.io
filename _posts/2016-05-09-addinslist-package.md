---
title: "addinslist package: An RStudio addin to discover and install RStudio addins"
tags: [rstats, packages, shiny]
date: 2016-05-09 11:20:00 -0700
gh-repo: daattali/addinslist
gh-badge: [star, watch, follow]
---

[RStudio addins](https://rstudio.github.io/rstudioaddins/) were released in early 2016 to provide anyone with the ability to add "extensions" to RStudio. This feature has quickly become popular, but discoverability was a problem: there was just no easy way to know what addins exist.

To address that issue, one month ago I created a [GitHub repo with a list of RStudio addins](https://github.com/daattali/addinslist#addinslist-table) that was meant to serve as a resource for discovering (and showcasing) RStudio addins. This repo received a lot of feedback, mainly around the idea of developing it into a package that would allow users to browse the addins and install the ones they find interesting. So that's what I did. The package is called `addinslist`.

If you install the `addinslist` package (with the command `install.packages("addinslist")`), your RStudio will gain a new addin in the *Addins* menu called *Browse RStudio addins*. The screenshot below shows how to access this addin.

[![Addins menu](https://raw.githubusercontent.com/daattali/addinslist/master/inst/media/addins-menu.png)](https://raw.githubusercontent.com/daattali/addinslist/master/inst/media/addins-menu.png)

Running this addin will show you a nice table with information about different available addins. This list automatically fetches new information once a day, so you don't need to update the package in order to see new addins that have been added since you installed the package.  You can click on any row in the table to install the package that contains that addin. If a row is green, it means you already have that addin (clicking it will *uninstall* the corresponding package). This is how the addin browser/installer looks:  

[![addinslist addin](https://raw.githubusercontent.com/daattali/addinslist/master/inst/media/addin.png)](https://raw.githubusercontent.com/daattali/addinslist/master/inst/media/addin.png)

If you want to add an addin to this list, feel free to make a pull request [on GitHub](https://github.com/daattali/addinslist).

I should mention that [Colin Gillespie](https://github.com/csgillespie) actually thought of this idea first and created [a similar addin](https://github.com/csgillespie/addinmanager), but unfortunately I was not aware of it until after my work was done. 
