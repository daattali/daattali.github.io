---
title: "Need any more reason to love R-Shiny? Here: you can even use Shiny to create simple games!"
tags: [rstats, shiny, packages]
date: 2016-01-26 10:00:00 -0700
share-img: /assets/img/blog/lightsout/shinyapp.png
gh-repo: daattali/lightsout
gh-badge: [star, watch, follow]
---

TL;DR [Click here](https://daattali.com/shiny/lightsout/) to play a puzzle game written entirely in Shiny [(source code)](https://github.com/daattali/lightsout/tree/master/inst/shiny).

Anyone who reads my blog posts knows by now that I'm very enthusiastic about Shiny (the web app framework for R - if you didn't know what Shiny is then I suggest reading my [previous post about it](/blog/building-shiny-apps-tutorial)). One of my reasons for liking Shiny so much is that you can do so much more with it than what it was built for, and it's fun to think of new useful uses for it. Well, my latest realization is that you can even make simple games quite easily, as the [lightsouts package](https://github.com/daattali/lightsout) and its [companion web app/game](https://daattali.com/shiny/lightsout/) demonstrate! I'm actually currently on my way to San Francisco for the first ever [Shiny conference](https://www.r-bloggers.com/shiny-developer-conference-stanford-university-january-2016/), so this post comes at a great time.

First, some background. I was recently contacted by [Daniel Barbosa](https://www.linkedin.com/in/danielbarbosa/) who offered to hire me for a tiny project: write a solver for the Lights Out puzzle in R. After a few minutes of Googling I found out that Lights Out is just a simple puzzle game that can be solved mathematically. The game consists of a grid of lights that are either on or off, and clicking on any light will toggle it and its neighbours. The goal of the puzzle is to switch all the lights off. 

Here is a simple visual that shows what happens when pressing a light on a 5x5 board:

[![Lights Out instructions](/assets/img/blog/lightsout/instructions.png)](/assets/img/blog/lightsout/instructions.png)

The cool thing about Lights Out is that, as I mentioned, it can be solved mathematically. In other words, given any Lights Out board, there are a few algorithms that can be used to find the set of lights that need to be clicked in order to turn all the lights off. So when Daniel asked me to implement a Lights Out solver in R, it really just meant to write a function that would take a Lights Out board as input (easily represented as a binary matrix with 0 = light off and 1 = light on) and implement the algorithm that would determine which lights to click on. It turns out that there are a few different methods to do this, and I chose the one that involves mostly linear algebra because it was the least confusing to me. (If you're curious about the solving algorithm, you can view my code [here](https://github.com/daattali/lightsout/blob/master/R/solve.R).)

At the time of completing this solver function I was traveling but bedridden, so I thought "well, why not go the extra half mile and make a package out of this, so that the game is playable?", which is exactly what I did. The next day, the [`lightsout`](https://github.com/daattali/lightsout) package was born, and it was capable of letting users play a Lights Out game in the R console. You can see the [README of the package](https://github.com/daattali/lightsout) to get more information on that.

At this point you can predict what happened next. "Why don't I complete that mile and just write a small Shiny app that will use the gameplay logic from the package and wrap it in a graphical user interface? That way there'll be an actual useful game, not just some 1980 text-based game that gives people nightmares." 

Since the game logic was already fully implemented, making a Shiny app that encapsulates the game logic was very easy. You can play the Shiny-based game [online](https://daattali.com/shiny/lightsout/) or by downloading the package and running `lightsout::launch()`. Here is a screenshot of the app:

[![Lights Out game](/assets/img/blog/lightsout/shinyapp.png)](/assets/img/blog/lightsout/shinyapp.png)

You can view the code for the Shiny app to convince yourself of how simple it is [by looking in the package source code](https://github.com/daattali/lightsout/tree/master/inst/shiny). It only took ~40 lines of Shiny UI code, ~100 lines of Shiny server code, a little bit of styling with CSS, and absolutely no JavaScript. Yep, the game was built entirely in R, with 0 JavaScript (although I did make heavy use of [`shinyjs`](https://github.com/daattali/shinyjs)).

While this "game" might not be very impressive, I think it's still a nice accomplishment to know that it was fully developed in R-Shiny. More importantly, it serves as a simple proof-of-concept to show that Shiny can be leveraged to make simple web-based games if you already have the logic implemented in R.

Disclaimer: I realize this may not necessarily be super practical because R isn't used for these kinds of applications, but if anyone ever writes a chess or connect4 or any similar logic game in R, then complementing it with a similar Shiny app might make sense.
