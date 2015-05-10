http://rpubs.com/daattali/heatmapsGgplotVsLattice

all my extensions

google forms with shiny (use S3 or [mondodb](https://www.mongolab.com) or [mysql](http://www.freemysqlhosting.net/)  http://shiny.rstudio.com/articles/share-data.html 
mysql: con <- DBI::dbConnect(RMySQL::MySQL(), group = "deang", default.file=normalizePath("~/.my.cnf"))
not concerned about safety/input sanitization
doesnt seem like RMySQL supports preparesd statemetns so need to "hack" together sql statement creation with paste/sprintf
install sqlite: `sudo apt-get install sqlite3 libsqlite3-dev`, create database: `sqlite3 databasename.db`


analyze rbloggers twitter: what day of week is best to post? which posters get most attention? correlation betwwen posting freq and attention?
```
library(httr)
library(XML)
# for each rstudio tweet, find the "wp." URL, and scrape it
GET("http://www.r-bloggers.com/fortune-data-science-is-the-hot-new-job/") %>% content("parsed") %>% getNodeSet("//a[@rel='author']") %>% .[[1]] %>% {list(name = xmlValue(.), url = xmlGetAttr(., "href"))}
```
