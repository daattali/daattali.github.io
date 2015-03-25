# Knitr's best hidden gem: spin

## spin can help you avoid repeating yourself and write reports much faster

### What is spin?

In short - `spin` is similar to `knit` in that it takes as input a file with R code + formatting + text and produces a nice readable report, such as an HTML or PDF document. The difference lies in the input: `spin` operates on R scripts (`.R`), while `knit` requires a literate programming file (`.Rmd`, `.Rnw`, etc.). This might not sound like a big deal, but it has some important advantages that I'll describe soon.

*Disclaimer: I may have a simplistic and non-comprehensive view of spin and knit; knit certainly has many features that I've yet to explore.*

### People don't know about spin

Anyone who loves the idea of dynamic report generation with R is probably a big fan of [`knitr`](http://yihui.name/knitr/) and its flagship function - `knit`.  But not many people seem to know about `knit`'s awesome cousin - `spin`. Ever since my supervisor [Jenny Bryan](http://www.stat.ubc.ca/~jenny/) told me about `spin`, I've been using it extensively and it has almost completely replaced my use of the traditional `knit`.

I was recently at an R workshop taught by [Hadley Wickham](http://had.co.nz/) and chatted with some fellow R enthusiasts about knitr.  Out of the 10 useRs I asked, none of them were aware of `knitr::spin`. I also conducted a similar survey at the labs where I work, and while evreyone I asked was an avid user of `knitr`, nobody knew about `spin` (my sample size is rather small and I haven't done any proper statistical significance testing, but I'd say the lack of awareness of `spin` is indeed significant).

### The problem I solve with spin

I've noticed a common approach that I was following and that I know many of my colleagues follow as well when creating R markdown (`.Rmd`) documents.  There are many usecases to R markdown - whether it be an homework assignment, a report to show a peer about your data, or just a report for yourself to more easily see and store results - and a common workflow is this:

- Write a bunch of interactive statements in an .R script
- Once your R code is ready, polish the code and make sure it runs smoothly when sourced
- Copy the code to a .Rmd file, add text with formatting, surround code in code chunks
- Repeat the above several times until the final .Rmd is ready

The main problem with this is that your whole analysis is duplicated
