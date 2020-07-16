---
title: "CRANalerts: Get email alerts when a CRAN package gets updated"
tags: [professional, rstats, r-bloggers, packages, shiny]
share-img: "https://deanattali.com/img/blog/cranalerts/screenshot.PNG"
permalink: /blog/cranalerts/
date: 2018-04-11 05:00:00 -0400
gh-repo: daattali/cranalerts
gh-badge: [follow, star]
---

**UPDATE July 2020: The code for this service is now public [on GitHub](https://github.com/daattali/cranalerts)**

Have you ever found yourself asking "how can I make sure I don't miss the next version release of package X"?

That's the exact problem I set out to solve with [CRANalerts](https://cranalerts.com/). Simply provide your email address and an R package name, and every time the package gets updated on CRAN in the future, you'll get notified.

<div style="text-align:center;">
  <a class="btn btn-lg btn-success" href="https://cranalerts.com/">Check out CRANalerts</a>
</div>

<div style="margin-bottom: 25px;"></div>

<div style="text-align:center;">
  <a href="https://cranalerts.com/">
    <img src="https://deanattali.com/img/blog/cranalerts/screenshot.PNG" alt="CRANalerts">
  </a>
</div>

It all started in January, when [Jim Hester](https://github.com/jimhester) was getting ready to submit a new version of `readr` to CRAN. The new version would have broken my code, so Jim submitted a [PR](https://github.com/daattali/ddpcr/pull/21) to my package that would ensure my code will still work after the update. I didn't want to accept the PR until `readr` was actually on CRAN, so I just started checking CRAN every few days to see when `readr` is updatd already.

Over the next two months, I received the usual emails asking me when various packages of mine are going to be updated on CRAN. This happens often, but I never paid much attention to it. Usually when I got asked "how do I know when shinyjs is updated on CRAN?" my response was "check the page every week". Yup, very helpful, I know!

But now that this problem became relevant to me, I eventually figured that manually checking a website continually is no good, so I sent out [a tweet](https://twitter.com/daattali/status/977200184043044864) asking if there's any service that will alert me when a CRAN package gets updated. People gave some suggestions, but there was nothing specifically for my purpose. So, since the weekend was coming up and I had a lot of actual work to do, what better way to procrastinate than to build this service? And so that's exactly what I did last weekend.

<div style="text-align:center;">
  <a href="https://xkcd.com/1319/">
    <img src="https://deanattali.com/img/blog/cranalerts/automation.png" alt="relevant xkcd">
  </a>
</div>

For those wondering—yes, of course I had to make this a Shiny app. I actually started building it with Node.js initially, but I quickly thought "nah, go Shiny or go home!". To be honest, I think this is a case where Shiny was not necessarily the absolute best tool for the job, but I did want to see if I could build a decent-ish service with Shiny. And so be it. [CRANalerts](https://cranalerts.com/) is fully a Shiny app.

You'll hopefully agree that this website doesn't *look* like a Shiny app. UI/UX is always very important to me; I make sure that whatever I build is not only functional, but looks good and is intuitive and user-friendly. But getting it to look pretty was not a trivial task, here is what CRANalerts looked like after 1 day of work:

<div style="text-align:center;">
  <a href="https://cranalerts.com/">
    <img src="https://deanattali.com/img/blog/cranalerts/nocss.PNG" alt="no css">
  </a>
</div>

Beautiful, right? In the everything-is-beautiful-in-its-own-unique-way kind of beautiful. A draft version of CRANalerts was ready after a day, but it actually took me another full day to just come up with a design and implement it to a point where I thought it looked good enough. Literally half the time was spent on coding, and half on making it pretty.

while it is important for me to make my tools look nice, I  also realize it's not my forte—it doesn't come quickly or naturally to me, and it ends up taking up a lot more of my time than I would like. **If anyone has a good eye for design and would like to help me on my next projects, I would really appreciate your help, feel free to [send me a message](https://deanattali.com/contact/)!** (You don't have to be a graphic designer by profession, I think a lot of people in the R community have much better artistic skills than myself.)

Keep in mind that this entire service was built in a weekend and did not have the resources get properly thoroughly tested, so it's possible you'll encounter problems. If you do have any feedback, feel free to let me know. And if you'd like to support my work on this project and future projects (including supporting server costs, AWS fees, maintenance of tools, etc), I appreciate any donations :)

<form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top" style="text-align: center;">
  <input type="hidden" name="cmd" value="_s-xclick">
  <input type="hidden" name="encrypted" value="-----BEGIN PKCS7-----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-----END PKCS7-----
  ">
  <input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
  <img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
</form>
