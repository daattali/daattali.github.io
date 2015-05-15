---
layout: post
title: "Analyzing R-Bloggers' posts via Twitter"
tags: [professional, rstats, r, r-bloggers, shiny, twitter]
date: 2015-05-17 21:30:00 -0700
fb-img: TODO choose an image
markdown: kramdown
---

~~~
sdfsdfsdsdf
lsdf
~~
~~~

## sdfsd {#g}

For those who don't know, every time a new blog post gets added to [R-Bloggers](http://www.r-bloggers.com/), it gets a corresponding tweet by [@Rbloggers](https://twitter.com/rbloggers), which gets seen by Rbloggers' ~20k followers fairly fast. And every time **my** post gets published, I can't help but check up on how many people gave that tweet some Twitter love, ie. "favorite"d or "retweet"ed it. It's even more exciting than getting a Facebook "like" on a photo from Costa Rica! 

Seeing all these tweets and how some tweets get much more attention than others has gotten me thinking. Are there some power users who post almost all the content, or so many blogs contribute equally?  Which posts were the most shared?  Which blog produces the highest quality posts consistently? Is there a correlation between how often a specific blog outputs posts and how successful these posts are?  Are there more posts during the weekdays then weekends? And of course the holy grail of bloggers - is there a day when it's better to post to get more shares?

To answer these questions, I of course turned to R. I used the `twitteR` package to get information about the latest 3200 tweets made by Rbloggers, and used Hadley's `httr` to scrape each blog post to get the post's author. Unfortunately Twitter does not allow us to fetch any tweets older than that (if you know of a workaround, please let me know), so the data here will be looking at tweets made from September 2013 until now (mid May 2015). That's actually a nice start date because it's exactly when I started grad school and when I first used R. So you can think of this analysis as "R-Bloggers' tweets since Dean's R life started" :)

I'm going to use some terminology very loosely and interchangeably throughout this post:  

- "blog" == "author" == "contributor"  
- "tweet" == "post"  
- "successful" post == "viral" == "highly shared" == "high score" == "high quality" == "gets Twitter love"

It's clear that all those terms not necessarily the same thing (for example, varlity does not necessarily mean high quality), but I'll be using them all as the same.

There is also [an accompanying interactive document](http://daattali.com/shiny/rbloggers-twitter/#data-exlorationvisualization) to accompany this post.  That document has a few interactive plots/tables for data that is better explored interactively than with an image, and it also contains all the source code that was used to make the analysis and all the figures here. The source code is also [available on GitHub](https://github.com/daattali/shiny-server/tree/master/rbloggers-twitter) as thhe raw text version of the interactive document.  In this post I will not be putting too much length code, especially not for the plots.

Before going any further, I'd like to say that this is not intended to be a comprehensive analysis and definitely has many weaknesses. It's just for fun. I'm not even going to be making any statistical significance tests at any point or do any quantitative analysis. Maybe titling this as "Analyzing" is wrong and should instead be "Exploring"? This post looks exclusively at data directly related to @Rbloggers tweets; I am not looking at data from any other social media or how many times the post was shared via R-Bloggers website rather than through Twitter. I'm also not looking at how much discussion (replies) a tweet generates. I wanted to include data from the number of times the "Tweet" button was pressed directly from R-Bloggers, but it looks like most older posts have 0 (maybe the button is a recent addition to R-Bloggers) so it'll introduce an unfair bias towards new posts.

## Data preparation {#data-prep}

This is the boring part - get the data from Twitter, fill in missing pieces of information, clean up... Feel free to skip to the more [exciting part](#exploration).

### Get data from Twitter

As mentioned above, I could only grab the last 3200 tweets made by @Rbloggers, which equates to all tweets since Sept 2013.  For each tweet I kept several pieces of information: tweet ID, tweet date, day of the week the tweet was made, number of times tweet was favorited, number of times tweet was retweeted, tweet text, and the last URL in the tweet text.  The tweet text is essentially a blog post's title that has been truncated. I keep the last URL that appears in every tweet because that URL always points to the article on R-Bloggers. I'm only storing the date but losing the actual time, which is another weakness. Also, the dates are according to UTC, and many (most?) Rbloggers followers are in America, so it might not be the most correct.

Anyway, after authenticating with Twitter, here's the code to get this information using `twitteR`:

```
MAX_TWEETS <- 3200
tweets_raw <- userTimeline('Rbloggers', n = MAX_TWEETS,
                           includeRts = FALSE, excludeReplies = TRUE)

tweets <- 
  ldply(tweets_raw, function(x) {
    data_frame(id = x$id,
               date = as.Date(x$created),
               day = weekdays(date),
               favorites = x$favoriteCount,
               retweets = x$retweetCount,
               title = x$text,
               url = x$urls %>% .[['url']] %>% tail(1)
    )
  })

rm(tweets_raw)  # being extremely memory conscious
```

Remember the full source code can be viewed [here](http://daattali.com/shiny/rbloggers-twitter/#get-data-from-twitter).

### Scrape R-Bloggers to get author info

Since I had some questions about the post authors and a tweet doesn't give that information, I resorted to scraping the R-Bloggers post linked in each tweet using `httr` to find the author. This part takes a bit of time to run. There were a few complications, mostly with authors whose name is their email and R-Bloggers attemps to hide it, but here is how I accomplished this step:

```
# Get the author of a single post given an R-Bloggers post URL
get_post_author_single <- function(url) {
  if (is.null(url)) {
    return(NA)
  }

  # get author HTML node
  author_node <- 
    GET(url) %>%
    httr::content("parsed") %>%
    getNodeSet("//a[@rel='author']")
  if (author_node %>% length != 1) {
    return(NA)
  }

  # get author name
  author <- author_node %>% .[[1]] %>% xmlValue

  # r-bloggers hides email address names so grab the email a different way
  if (nchar(author) > 100 && grepl("document\\.getElementsByTagName", author)) {
    author <- author_node %>% .[[1]] %>% xmlGetAttr("title")
  }

  author  
}

# Get a list of URL --> author for a list of R-Bloggers post URLs
get_post_author <- function(urls) {
  lapply(urls, get_post_author_single) %>% unlist
}

# Add the author to each tweet.
# This will take several minutes because we're scraping r-bloggers 3200 times (don't run this frequently - we don't want to overwork our beloved R-Bloggers server)
tweets %<>% mutate(author = get_post_author(url))  

# Remove NA author (these are mostly jobs postings, plus a few blog posts that have been deleted)
tweets %<>% na.omit
```

### Clean up data

It's time for some clean up:

- Remove the URL and `#rstats` hashtag from every tweet's title  

- Older posts all contain the text "This article was originally posted on ... and kindly contributed by ..." - try to remove that as well

- Order the day factor levels in order from Monday - Sunday

- Truncate very long author names with an ellipsis

- Merge duplicate tweets (tweets with the same author and title that are posted within a week)

After removing duplicates and previously removing tweets about job postings, we are left with 2979 tweets (down from 3200).  You can see what the data looks like [here](http://daattali.com/shiny/rbloggers-twitter/#summary-of-all-posts) or see the code for the cleanup on that page as well.

### Add a score metric

Now that we have almost all the info we need for the tweets, there is one thing missing. It'd be useful to have a metric for how successful a tweet is using the very little bit of information we have. This is of course very arbitrary. I chose to score a tweet’s success as a linear combination of its "# of favorites" and "# of retweets". Since there are roughly twice as many favorites as retweets in total, retweets get twice the weight. Very simple formula :) 

```
sum(tweets$favorites) / sum(tweets$retweets)   # result = 2.1
tweets$score <- tweets$favorites + tweets$retweets * 2
```

## Exploration {#exploration}

Time for the fun stuff!  I'm only going to make a few plots, you can get the data [from GitHub](https://github.com/daattali/shiny-server/tree/master/rbloggers-twitter) if you want to play around with it.

### Preliminary look at posts' success on Twitter

First I'd like to see a simple scatterplot showing the number of favorites and retweets for each blog post.

![Tweets score]({{ site.url }}/img/blog/tbloggers-twitter/tweets-score.gif)

Looks like most posts are close to the (0, 0) area, with 20 favorites and 10 retweets being the maximum boundary for most. A very small fraction of tweets make it past the 40 favorites or 20 retweets. 

Looks like there are about 10 posts that are that are much higher up than everyone else, so let's see what the top 10 most shared Rbloggers posts on Twitter were since Sep 2013.

| title | date | author | favorites | retweets | score |
|-------|------|--------|-----------|----------|-------|
| A new interactive interface for learning R online, for free | 2015-04-14 | DataCamp | 78 | 49 | 176 |
| Introducing Radiant: A shiny interface for R | 2015-05-04 | 85 | 44 | 173 |
| Choosing R or Python for data analysis? An infographic | 2015-05-12 | 59 | 54 | 168 |
| Why the Ban on P-Values? And What Now? | 2015-03-07 | 47 | 47 | 141 |
| Machine Learning in R for beginners | 2015-03-26 | 68 | 29 | 126 |
| Free Stanford online course on Statistical Learning (with R) starting on 19 Jan 2015 | 2014-11-22 | 54 | 35 | 124 |
| Four Beautiful Python, R, MATLAB, and Mathematica plots with LaTeX | 2014-12-20 | 57 | 29 | 115 |
| In-depth introduction to machine learning in 15 hours of expert videos | 2014-09-24 | 64 | 20 | 104 |
| Learn Statistics and R online from Harvard | 2015-01-17 | 49 | 27 | 103 |
| R Tutorial on Reading and Importing Excel Files into R | 2015-04-04 | 61 | 20 | 101 |

**Remember to check out the [accompanying interactive doc + source code](http://daattali.com/shiny/rbloggers-twitter/)!**
