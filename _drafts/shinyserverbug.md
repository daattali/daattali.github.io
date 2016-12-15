in newer shiny server Pro there's a bug with file permissions where if you try to write to write a file , your app can crash.

it's a bug that I reported last year and was fixed but it looks like at some point it was re-introduced. Temporary fix is to downgrade to a shiny server version that works:

sudo apt-get remove shiny-server
shiny-server-commercial-1.4.0.637-amd64.deb

if you need the features of the new shiny server and are running into this issue, there will be different workarounds depending on your server setup. You can contact me or RStudio (I would suggest RStudio since you're a paying a customer!)

