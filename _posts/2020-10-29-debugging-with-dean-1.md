---
title: "Debugging with Dean: My first YouTube screencast"
subtitle: "I wanted to solve a real bug in real-time, to show you my thought process. Here's how it turned out."
thumbnail-img: "/assets/img/blog/debugging-with-dean-1/cover.png"
tags: [rstats, shiny, tutorial, youtube]
permalink: /blog/debugging-with-dean-1/
date: 2020-10-29 11:00:00 -0400
carbonads-sm-horizontal: true
---

If you're impatient and want to watch the video immediately, you can [watch it directly on YouTube](https://youtu.be/sP_VB9OFJP0?start=45). *Just remember to subscribe so you won't miss my next videos!*

## Table of contents

- [Background](#background)
- [A typical Saturday morning](#making)
- [To share or not to share?](#sharing)
- [The initial response](#response)
- [My whoopsie](#oops)
- [It's ready: **Debugging with Dean!**](#announcing)
- [Into the future](#future)

## Background {#background}

A few days ago, [a bug report](https://github.com/daattali/timevis/issues/103) was opened in one of my R packages {timevis}. I wasn't able to reproduce the issue, so I asked the submitter to share his exact input data. 

This was not unusual for me. Between all my packages, tutorials, and other online content, I'm often asked to look at a bug in a Shiny app. My clients also commonly ask me for help troubleshooting and fixing bugs in their complex apps. Along with fixing the issue at hand, I've also developed a habit of sending them detailed emails with a walkthrough of my exact debugging process. I've received very positive feedback from these emails, and I've been told many times that I should share my debugging process with more people.

## A typical Saturday morning {#making}

Getting back to the {timevis} issue: When I woke up on Saturday morning I saw that there was a response with a dataset attached. Instead of looking into the issue right away, I had an idea: I could do a screencast showing how I address a bug report, in real-time!

So I did a quick Google search to find a free screencast software, turned on my webcam, and talked through my thought process while recording my screen. I even came up with a (catchy?) name while recording - Debugging with Dean. I had no script, no proper camera or microphone (I used the ones that are integrated into my laptop), and no expectations.

Ten minutes and one quick debugging session later, I had my first webcam-made video!

## To share or not to share? {#sharing}

I showed the video to my teenage brother, who himself hosts two podcasts ([Entertainment Bites](https://open.spotify.com/show/4Bko0gJWU5QTTqDHRHVGvl) and [For the Boys](https://open.spotify.com/show/6ExXxqyrbFeycahNYZmDur)). He was disgusted. He said that my microphone quality is appalling, and he wondered how he could be related to someone so stupid that wouldn't even make a script for the intro. Because I didn't even know if anybody would watch my video and if my efforts would be well-received, in a typical big brother fashion I brushed him off and proceeded on my way.

I asked on Twitter if anybody would be interested in seeing this:

<blockquote class="twitter-tweet"><p lang="en" dir="ltr">Would people be interested in screencasts of me debug/troubleshoot GitHub issues that are opened against my packages, to see my debugging process? <a href="https://twitter.com/hashtag/rstats?src=hash&amp;ref_src=twsrc%5Etfw">#rstats</a> I&#39;ve never done a screencast but was thinking this would be useful?</p>&mdash; Dean Attali (@daattali) <a href="https://twitter.com/daattali/status/1320057103466246144?ref_src=twsrc%5Etfw">October 24, 2020</a></blockquote> <script async src="https://platform.twitter.com/widgets.js" charset="utf-8"></script>

The response was pretty clear, so I put up the video on YouTube. I didn't do any editing (which is why the first few seconds are me awkwardly being silent and orienting myself!) and posted the raw video to YouTube right away.

## The initial response {#response}

I announced it on Twitter immediately. 

Despite the poor equipment and set up, I was very surprised by the amount of positive response. Right away I was getting views, likes, comments, even people subscribing to my "channel" which I never meant for. It was overwhelming!

Even though the "bug" in the video was very simple and was "solved" in less than a minute, viewers told me that they still got a lot out of the video, just by seeing my process: The fact that I used a real-life example, my demonstration of good practices and why they help me, showing why a reprex is useful, talking about what I do with my working environment before I begin, showing my productivity keyboard shotcuts, reviewing documentation as part of debugging, the authenticity of it all... I was very happy that people found value in it :)

This was all very encouraging. But I did remove my announcement of the video after a few minutes because of one issue...

## My whoopsie {#oops}

I tweeted the video and expected people to just watch it and that's all. I didn't expect any further engagement.

Soon after I tweeted the video, someone emailed me to tell me that by going to my "channel", they're able to see a lot of my personal videos and playlists. Videos of my little brother dancing and singing, or playlists I made for road trips and certain events, are not exactly what most people would want to see as the follow-up to a debugging session. I removed the tweet so that it won't get too much more traffic. 

## It's ready: **Debugging with Dean!** {#announcing}

After getting over my embarrassment and making sure my YouTube channel is cleaned up, I'm ready to finally officially announce my first video: **Debugging With Dean**!

<div style="text-align:center;">
  <a class="btn btn-lg btn-cta" href="https://youtu.be/sP_VB9OFJP0?start=45"><i class="fab fa-youtube"></i> Watch on YouTube</a>
</div>

<style>.embed-container { position: relative; margin-top: 20px; padding-bottom: 56.25%; height: 0; overflow: hidden; max-width: 100%; } .embed-container iframe { position: absolute; top: 0; left: 0; width: 100%; height: 100%; }</style>
<div class="embed-container">
<iframe src="https://www.youtube-nocookie.com/embed/sP_VB9OFJP0?start=45" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
</div>

## Into the future {#future}

If I continue to get good response, I might make more videos. Make sure to subscribe on YouTube so you won't miss my next videos! There are more debugging session videos I could make, and a lot of other Shiny content inside of me that I'd like to share. 

I think my brother is right and I should at least invest in a microphone for any future videos. So I asked Eric Nantz (host of [The R Podcast](https://r-podcast.org/) and [Shiny Developer Series](https://shinydevseries.com/)) for suggestions, and I already placed an order for a fancy microphone.

I would be very happy to make more videos, but they do take a lot of time away from my private paying clients. So if you enjoy my content and want me to make more, **please consider [supporting my efforts](https://github.com/sponsors/daattali)**.

Lastly, I would very warmly welcome any and all feedback, especially any constructive criticism, so don't hesitate to comment on the video or [write to me](https://deanattali.com/contact)!
