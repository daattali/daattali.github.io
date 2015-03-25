# Knitr's best hidden gem: spin

## Stop knitting & start spinning - spin can help you write reports much faster and avoid repeating yourself

Anyone who loves the idea of dynamic report generation with R is probably a big fan of [`knitr`](http://yihui.name/knitr/) and its flagship function - `knit`.  But not many people seem to know about `knit`'s awesome cousin - `spin`. 

### What is spin?

In short: `knitr::spin` is similar to `knitr::knit` in that it takes as input a file with R code + formatting + text and produces a nice readable report, such as an HTML or markdown document. The difference lies in the input: `spin` operates on R scripts (`.R`), while `knit` requires a literate programming file (`.Rmd`, `.Rnw`, etc.). This might not sound like a big deal, but it has some important advantages that I'll describe soon.

*Disclaimer: I may have a simplistic and non-comprehensive view of spin and knit; knit certainly has many features that I've yet to explore. I'm also going to talk only about the Rmd/md formats and not about any Sweave/LaTeX/HTML for simplicity.*

Using `spin` to turn your R script into markdown output is very simple:

- Any line beginning with `#'` is treated as a markdown directive (`#' # title` will be a header, `#' some **bold** text` results in `some `**`bold`**` text`)
- Any line beginning with `#+` is parsed as code chunk options
- Run `knitr::spin` on the file
 

### The problem that spin solves

I've noticed a problem with the common approach that I adapted (and I know my colleagues use a similar approach) when creating R markdown (`.Rmd`) documents.  There are many usecases to R markdown - whether it be an homework assignment, a report to show a peer about your data, or just a report for yourself to more easily see and store results - and a common workflow is this:

- Write a bunch of interactive statements in an .R script
- Once my R code is ready, polish the code and make sure it runs smoothly when `source`d
- Copy the code to a .Rmd file, add text with formatting, surround code in code chunks
- Repeat the above several times until the final .Rmd is ready

The main thing to note here is that my whole analysis is now duplicated in two files: in the original R file as regular code, and in the Rmd file as code chunks. I'm breaking the DRY principle (Don't Repeat Yourself).  The problem is that now, because my code is in two places, I have a lot more work to maintain the code.  What if next week I want to change a parameter? I'd have to remember to make the exact same change in both the R source code and the Rmd file, so that the report will still match the R script. Having to manage only one source as opposed to keeping two in sync makes my life much simpler. 

### Enter: `spin`

With `spin`, your original R source code is being converted into Rmd and then into a final report format. It's as if `spin` is saving you the step of going from R to Rmd. The big benefit here, again, is not just saving the time in writing the Rmd, but the fact that you now have only one copy of your code that you need to maintain.

Once your have some R code that runs, if you want to create a markdown report from it, it's very easy with `spin`.  You could just go ahead and run `spin` on pure R, and it'll work - you'll get back a markdown file with all the results. It won't be pretty yet, but it'll just work right away.  To format it better, all you need to do is add some markdown (text) prepended with `#'`, and potentially add chunk options  with `#+` before every code chunk.  You don't have to copy your code to a different file format and have it be broken until it's inside a proper code chunk.

**Bonus:** if you use `spin`, you can still run your code like a normal R file. You can select a bunch of lines and run them, or just `source` the entire file, and it'll run as R code. When you want to create a report, just call `spin`. If you're used to writing Rmd files, you may also be a bit annoyed that you can't simply select lines and run them if those lines are not all inside a code chunk. It's not an R file, and can't be treated that way.

**Bonus #2:** I find it much easier and faster to convert a regular R script to a spin-formatted R script than to a Rmd file. To convert a block of code into a code chunk for `spin`, you just have to add `#+` before the chunk.  To convert a block of code into a code chunk in Rmd, you need to add text in two different places - `{r}` before the chunk and ```` ``` ```` after.

### People don't know about spin

The reason I'm writing this is because I believe that `spin` is very useful but is not very well known.

I was recently at an R workshop taught by [Hadley Wickham](http://had.co.nz/) and chatted with some fellow R enthusiasts about knitr.  Out of the 10 useRs I asked, none of them were aware of `knitr::spin`. I also conducted a similar survey at the labs where I work, and while everyone I asked was an avid user of `knitr`, nobody knew about `spin`. I know that my sample size is rather small and I haven't done any proper statistical significance testing, but I'd say the lack of awareness of `spin` is quite significant.


### `knit` is still amazing

I'm not trying to say that `knit` has no place - it's very popular for a good reason. But in my opinion, before producing a HTML/markdown file from your R code, you should consider whether using `spin` (pure R code) or `knit` (need to manually convert your R to Rmd) will be more efficient.   I think if the output is very text-heavy and focuses a lot more on formatting and visual, and only includes some bits of R code here and there, then `knit` would be more appropriate.  But if a report is primarily a way to showcase a lot of code, with intermittent text, then `spin` can save you a lot of trouble.

### Example

Ever since my supervisor [Jenny Bryan](http://www.stat.ubc.ca/~jenny/) told me about `spin`, I've been using it extensively and it has replaced my use of `knit` in many circumstances. Every time I have some code that I need to show in a nice format to someone else, I just add some text with `#'` and add code chunk options where needed. For example, [here](https://github.com/daattali/UBC-STAT545/blob/master/hw/hw12_web-scraping-api/hw12_web-scraping-api.md) is the markdown output from using `spin` on [this](https://github.com/daattali/UBC-STAT545/blob/master/hw/hw12_web-scraping-api/hw12_web-scraping-api.R) R file. I might be repeating myself now with what I'm saying (and violating DRY!), but it would have been a lot of work to make a Rmd file after writing all the R code, and it would have been annoying to change both files any time in the future.
