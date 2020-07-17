---
title: "Beautiful Jekyll v4: Huge updates to one of the most popular GitHub Pages themes"
tags: [jekyll, personal, professional, github, blog]
share-img: "https://deanattali.com/img/blog/beautiful-jekyll.png"
permalink: /blog/beautifuljekyll4/
date: 2020-07-17 11:00:00 -0400
gh-repo: daattali/beautiful-jekyll
gh-badge: [fork, star, follow]
---

[Beautiful Jekyll](https://github.com/daattali/beautiful-jekyll#readme) allows you to create a website similar to mine in under 5 minutes. It started out as a modest weekend project 5 years ago that I never expected anyone outside my home to see. Today, it's one of the top Jekyll/GitHub Pages themes with over 10,000 GitHub stars+forks<sup><a class="footnote-ref" href="#footnote">*</a></sup>.

{: .box-note}
If you've never tried Beautiful Jekyll and always wanted to have a website, now's a *great* time to get started!

<div style="text-align:center;">
  <a class="btn btn-lg btn-success" href="https://beautifuljekyll.com/">Check out Beautiful Jekyll</a>
</div>

Starting from day 1, the goal with [Beautiful Jekyll](https://beautifuljekyll.com/) has been to allow anyone--and I mean literally *anyone* regardless of coding skill--to create a simple, clean, customziable website within minutes. From 2015-2019 I incrementally added new features to make the theme easier to use and more customizable, but I was always wary about making large-scale changes. 

Then COVID-19 happened. 

I found myself stuck at home and had some time to dedicate to many of my [projects](https://deanattali.com/projects/). The past several months have been a very long journey with hundreds of hours (not an exaggeration) and some very (VERY) late nights, but I'm happy to announce that Beautiful Jekyll has graduated from v1 to v4. Yes, that's right, it jumped 3 major versions! I was planning on just doing a v2, but I kept getting excited by more big changes and I couldn't stop until I was fully satisfied. 

## What changed?

A lot.

From adding many small features, to completely re-working the documentation and the codebase, to migrating to Bootstrap 4, there was a lot to do. I had myself a list of tasks I wanted to accomplish, and on top of that I took a good look at all the open bug reports and feature requests that people submitted and tackled almost all of them.

For a full list of new features and changes, see the [CHANGELOG](https://github.com/daattali/beautiful-jekyll/blob/master/CHANGELOG.md). Major highlights include:

- Migrated from Bootstrap 3 to Bootstrap 4. This was not fun.
- Made the theme available as a [Ruby gem](https://rubygems.org/gems/beautiful-jekyll-theme) for advanced users
- Made the theme available using `remote_theme` through [GitHub Pages](https://beautifuljekyll.com/getstarted#method_remote_theme_github) (or through a [Ruby Jekyll site](https://beautifuljekyll.com/getstarted#method_remote_theme_jekyll)) for intermediate users
- Added accessibility features, improved performance of the theme, updated jQuery to improve security
- Rewrote and greatly simplified the entire documentation to make it as simple and easy to understand
- Rewrote almost the entire codebase
- Since the web today is very photo-heavy, added a few options for images in posts: `thumbnail-image`, `share-img`, and `cover-img`
- The demo website now lives in its very own domain [beautifuljekyll.com](https://beautifuljekyll.com/) and is hosted in a separate [GitHub repository](https://github.com/daattali/beautiful-jekyll-demo) that showcases how to use the new `remote_theme` feature

### Moving forward

As anyone who builds tools knows, no software is ever truly finished. There are always more features to add, and maintaining the current codebase is in itself a part time job. One of my goals with the next release is to transition from CSS to Sass which would require a major rewrite of a large portion of the code, but otherwise I don't plan any more major changes for a while.

**On a related note, the countless of hours I spend on this project are done with no pay, often while sacrificing time that I would otherwise use to work for clients and therefore literally causing me to lose money.**

**One of my biggest goals is to have a few people [sponsor me](https://github.com/sponsors/daattali) to help this project flourish, so that I can focus more on projects that help the community instead of client work. Alternatively, one-time [donations](https://paypal.me/daattali) are also greatly appreciated if you find this project useful.**

<p align="center">
  <a style="display: inline-block;" href="https://github.com/sponsors/daattali">
    <img height="40" src="https://i.imgur.com/034B8vq.png" />
  </a>
  &nbsp;&nbsp;
  <a style="display: inline-block;" href="https://paypal.me/daattali">
    <img height="40" src="https://camo.githubusercontent.com/0e9e5cac101f7093336b4589c380ab5dcfdcbab0/68747470733a2f2f63646e2e6a7364656c6976722e6e65742f67682f74776f6c66736f6e2f70617970616c2d6769746875622d627574746f6e40312e302e302f646973742f627574746f6e2e737667" />
  </a>
</p>


### Updating from older versions

Over the years, as new features consistently got added to the theme, I often received questions from users asking me how can they incorporate the latest changes from the theme into their existing site. 

Up until the recent updates, Beautiful Jekyll only had instructions for how to set up the site using the simple, yet least flexible, method of forking the GitHub repository. One major limitation of this method is that if you fork the theme and then I add new features, there's no easy way for you to sync your project with my updated version. You could save all your blog posts and any custom pages you created, make a fresh fork of the current repository so that you'll have the latest features, and then add back all your own content. This works, and if you only want to sync your site to Beautiful Jekyll very rarely then it's ok, but this is clearly not ideal.

One of the big focuses of the recent updates was to allow for [more ways to use Beautiful Jekyll](https://beautifuljekyll.com/getstarted/#install-steps-hard). While forking the project is still the easiest way to use Beautiful Jekyll and is the recommended way for beginners, advanced users can now use the theme as a [Ruby Gem](https://rubygems.org/gems/beautiful-jekyll-theme) if they want to install Jekyll on a private server, or use GitHub's `remote_theme` feature. Using these new methods, you'll be able to always keep up to date with the latest version of Beautiful Jekyll automatically. For an example of a GitHub website that uses the `remote_theme` feature, you can see the [repository that serves the demo site](https://github.com/daattali/beautiful-jekyll-demo).


<span id="footnote"></span>_*At the time of writing this, Beautiful Jekyll is the second-highest starred Jekyll theme on GitHub and the second-most forked repository.
