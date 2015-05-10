---
layout: post
title: "How to get your very own RStudio Server and Shiny Server with DigitalOcean"
tags: [professional, rstats, r, r-bloggers, shiny, rstudio, sysadmin]
date: 2015-05-09 21:30:00 -0700
---

If you've always wanted to have an RStudio Server of your own so that you can access R from anywhere, or your own Shiny Server to host your awesome shiny apps, [DigitalOcean](https://www.digitalocean.com/?refcode=358494f80b99) (DO) can help you get there easily.

DigitalOcean provides virtual private servers (they call each server a *droplet*), which means that you can pay *$5/month* to have your own server "in the cloud" that you can access from anywhere and host anything on.  Check out [my DO droplet](http://daattali.com/) to see it in action! Use [my referral link](https://www.digitalocean.com/?refcode=358494f80b99) to get $10 in credits, which is enough to give you a private server for your first 2 months.

I only found out about DO a couple of months ago when I asked my supervisor, [Jenny Bryan](https://twitter.com/JennyBryan), if there was a way for me to do some R-ing when I'm away from my machine. She told me that she doesn't have a solution for me, but that I should check out DigitalOcean, so I did. And it turns out that it's very convenient for hosting my own RStudio Server and anything else I'd like to host, and very affordable **even for my student self**. :)

This post will cover how to set up a machine from scratch, setup R, RStudio Server, Shiny Server, and a few other helpful features on a brand new DO droplet (remember: droplet = your machine in the cloud). The tutorial might seem lengthy, but it's actually very simple, I'm just breaking up every step into very fine details.

# Table of contents

- [Step 1: Sign up to DigitalOcean](#sign-up)
- [Step 2: Create a new droplet](#create-droplet)
- [Step 3: Log in to your very own shiny new server](#login)
- [Step 4: Ensure you don't shoot yourself in the foot](#safety-first)
- [Step 5: See your droplet in a browser](#nginx)
- [Step 6: Install R](#install-r)
- [Step 7: Install RStudio Server](#install-rstudio)
- [Step 8: Install Shiny Server](#install-shiny)
- [Step 9: Make pretty URLs for RStudio Server and Shiny Server](#reverse-proxy)
- [Step 10: Custom domain name](#custom-domain)
- [Resources](#resources)

<h1 id="sign-up">Step 1: Sign up to DigitalOcean</h1>

Go to [DigitalOcean](https://www.digitalocean.com/?refcode=358494f80b99) (use this referral link to get 2 months!) and sign up. Registration is quick and painless, but I think in order to verify your account (and to get your $10) you have to provide credit card details.  If you go to [your Billing page](https://cloud.digitalocean.com/settings/billing) hopefully you will see the $10 credit.

<h1 id="create-droplet">Step 2: Create a new droplet</h1>

Now let's claim one of DO's machines as our own! It's so simple that you definitely don't need my instructions, just click on the big "Create Droplet" button and choose your settings. I chose the smallest/weakest machine ($5/month plan) and it's good enough for me. I also chose San Francisco because it's the closest to me, though it really wouldn't make much of a noticeable difference where the server is located. For OS, I chose to go with the default Ubuntu 14.04 x64.  I highly recommend you add an SSH key at the last step if you know how to do that. If not, either read up on it or just proceed without an SSH key.

*Note: all subsequent steps assume that you are also using the weakest server possible with Ubuntu 14.04 x64. If you chose different settings, the general instructions will still apply but some of the specific commands/URLs might need to change.*

Even though you probably don't need it, here's a short GIF showing me creating a new droplet:
![Create droplet]({{ site.url }}/img/blog/digital-ocean/do-create.gif)

<h1 id="login">Step 3: Log in to your very own shiny new server</h1>

Once the droplet is ready (can take a few minutes), you'll be redirected to a page that shows you information about the new droplet, including its IP. From now on, I'll use the random IP address `123.456.1.2` for the rest of this post, but remember to always substitute your actual droplet's IP with this one.

One option to log into your droplet is through the "Access" tab on the page you were redirected to, but it's slow and ugly, so I prefer logging in on my own machine. If you're on a unix machine, you can just use `ssh 123.456.1.2`. I'm on Windows, so I use [PuTTY](http://www.chiark.greenend.org.uk/~sgtatham/putty/) to SSH ("login") into other machines. Use the IP that you see on the page, with the username `root`.  If you used an SSH key then you don't need to provide a password; otherwise, a password was sent to your email.

You should be greeted with a welcome message and some stats about the server that look like this:
![Login screen]({{ site.url }}/img/blog/digital-ocean/login.png)

<h1 id="safety-first">Step 4: Ensure you don't shoot yourself in the foot</h1>

The first thing I like to do is add a non-root user so that we won't accidentally do something stupid as "root". Let's add a user named "dean" and give him admin power.  You will be asked to give some information for this new user.

```
adduser dean
gpasswd -a dean sudo
```

From now on I will generally log into this server as "dean" instead of "root".  If I'll need to run any commands requiring admin abilities, I just have to prepend the command with `sudo`.  Let's say goodbye to "root" and switch to "dean".

```
su - dean
```

<h1 id="nginx">Step 5: See your droplet in a browser</h1>

Right now if you try to visit `http://123.456.1.2` in a browser, you'll get a "webpage not available error". Let's make our private server serve a webpage there instead, as a nice visual reward for getting this far. Install nginx:

```
sudo apt-get update
sudo apt-get install nginx
```

Now if you visit `http://123.456.1.2`, you should see a welcome message to nginx. Instant gratification!

### Quick nginx references

The default file that is served is located at `/usr/share/nginx/html/index.html`, so if you want to change what that webpage is showing, just edit that file with `sudo vim /usr/share/nginx/html/index.html`. For example, I just put a bit of text redirecting to other places [in my index page](http://daattali.com/).  The configuration file is located at `/etc/nginx/nginx.conf`. 

When you edit an HTML file, you will be able to see the changes immediately when you refresh the page, but if you make configuration changes, you need to restart nginx. In the future, you can stop/start/restart nginx with

```
sudo service nginx stop
sudo service nginx start
sudo service nginx restart
```

<h1 id="install-r">Step 6: Install R</h1>

To ensure we get the most recent version of R, we need to first add `trusty` to our `sources.list`:

```
sudo sh -c 'echo "deb http://cran.rstudio.com/bin/linux/ubuntu trusty/" >> /etc/apt/sources.list'
```

Now add the public keys:

```
gpg --keyserver keyserver.ubuntu.com --recv-key E084DAB9
gpg -a --export E084DAB9 | sudo apt-key add -
```

Now we're ready to install R

```
sudo apt-get update
sudo apt-get install r-base
```

You should now be able to run R and hopefully be greeted with a message containing the latest R version. 

```
R
```

![R welcome]({{ site.url }}/img/blog/digital-ocean/R-welcome.png)

Now you need to quit R (`quit()`) because there are a couple small things to adjust on the server so that R will work well.

If you also chose the weakest machine type like I did, many packages won't be able to install because of not enough memory. We need to add 1G of swap space:

```
sudo /bin/dd if=/dev/zero of=/var/swap.1 bs=1M count=1024
sudo /sbin/mkswap /var/swap.1
sudo /sbin/swapon /var/swap.1
sudo sh -c 'echo "/var/swap.1 swap swap defaults 0 0 " >> /etc/fstab'
```

Now installing most packages will work, but before installing any package, I always like having `devtools` available so that I can install GitHub packages. `devtools` will currently not be able to get installed (though, annoyingly enough, it will not throw any errors, it will simply not install) because it needs `libcurl` support. So let's install it:

```
sudo apt-get install libcurl4-gnutls-dev
```

Ok, now we can start installing R packages, both from CRAN and from GitHub!

```
R
install.packages("devtools", repos='http://cran.rstudio.com/')
devtools::install_github("daattali/shinyjs")
```

Feel free to play around in the R console now.

<h1 id="install-rstudio">Step 7: Install RStudio Server</h1>

Great, R is working, but RStudio has become such an integral part of our lives that we can't do any R without it! 

Quit R (`quit()`) and install some pre-requisites:

```
sudo apt-get install libapparmor1 gdebi-core
```

Download the latest RStudio Server - consult [RStudio Downloads page](http://www.rstudio.com/products/rstudio/download-server/) to get the URL for the latest version. Then install the file you downloaded. These next two lines are using the latest version as of writing this post.

```
wget http://download2.rstudio.org/rstudio-server-0.98.1103-amd64.deb
sudo gdebi rstudio-server-0.98.1103-amd64.deb
```

Done! By default, RStudio uses port 8787, so to access RStudio go to `http://107.170.217.55:8787` and you should be greeted with an RStudio login page. (If you forgot what your droplet's IP is, you can find out by running `hostname -I`)

![RStudio]({{ site.url }}/img/blog/digital-ocean/rstudio.png)

You can log in to RStudio with any user/password that are available on the droplet. For example, I would log in with username `dean` and my password. If you want to let your friend Joe have access to your RStudio, you can create a new user for them with `adduser joe`.

Go ahead and play around in R a bit, to make sure it works fine. I usually like to try out a `ggplot2` function, to ensure that graphics are working properly.

<h1 id="install-shiny">Step 8: Install Shiny Server</h1>

You can safely skip this step if you don't use `shiny` and aren't interested in being able to host Shiny apps yourself.

To install Shiny Server, first install the `shiny` package:

```
sudo su - -c "R -e \"install.packages('shiny', repos='http://cran.rstudio.com/')\""
```

Just like when we installed RStudio, again we need to get the URL of the latest Shiny Server [from the Shiny Server downloads page](http://www.rstudio.com/products/shiny/download-server/), download the file, and then install it.  These are the two commands using the version that is most up-to-date right now:

```
wget http://download3.rstudio.org/ubuntu-12.04/x86_64/shiny-server-1.3.0.403-amd64.deb
sudo gdebi shiny-server-1.3.0.403-amd64.deb
```

Shiny Server is now installed and running. Assuming there were no problems, if you go to `http://107.170.217.55:3838/` you should see Shiny Server's default homepage, which includes some instructions and two Shiny apps:

![Shiny Server]({{ site.url }}/img/blog/digital-ocean/shiny.png)

If you see an error on the bottom Shiny app, it's probably because you don't have the `rmarkdown` R package installed (the instructions on the default Shiny Server page mention this). After installing `rmarkdown` in R, the bottom Shiny app should work as well.  I suggest you read through the instructions page at `http://107.170.217.55:3838/`. A few important points as reference:

- Shiny Server log is at `/var/log/shiny-server.log`.
- The default Shiny Server homepage you're seeing is located at `/srv/shiny-server/index.html` - you can edit it or remove it.
- Any Shiny app directory that you place under `/srv/shiny-server/` will be served as a Shiny app. For example, there is a default app at `/srv/shiny-server/sample-apps/hello/`, which means you can run the app by going to `http://107.170.217.55:3838/sample-apps/hello/`.
- The config file for Shiny Server is at `/etc/shiny-server/shiny-server.conf`.
- **Important!** If you look in the config file, you will see that by default, apps are ran as user "shiny". It's important to understand which user is running an app because things like file permissions and personal R libraries will be different for each user and it might cause you some headaches until you realize it's because the app should not be run as "shiny". Just keep that in mind.

<h1 id="reverse-proxy">Step 9: Make pretty URLs for RStudio Server and Shiny Server</h1>

This is optional and a little more advanced. You might have noticed that to access both RStudio and Shiny Server, you have to remember weird port numbers (`:8787` and `:3838`). Not only is it hard and ugly to remember, but some workplace environments often block access to those ports, which means that many people/places won't be able to access these pages. The solution is to use a reverse proxy, so that nginx will listen on port 80 (default HTTP port) at the URL `/shiny` and will *internally* redirect that to port 3838. Same for RStudio - we can have nginx listen at `/rstudio` and redirect it to port 8787. This is why my Shiny apps can be reached at [daattali.com/shiny/](http://daattali.com/shiny/) which is an easy URL to type, but also at [daattali.com:3838](http://daattali.com:3838).

You need to edit the nginx config file `/etc/nginx/sites-enabled/default`:

```
sudo vim /etc/nginx/sites-enabled/default
```

Add the following lines right after the line that reads `server_name localhost;`:

```
location /shiny/ {
  proxy_pass http://127.0.0.1:3838/;
}

location /rstudio/ {
  proxy_pass http://127.0.0.1:8787/;
}
```

Since we changed the nginx config, we need to restart nginx for it to take effect.

```
sudo service nginx restart
```

Now you should be able to go to `http://107.170.217.55/shiny/` or `http://107.170.217.55/rstudio/`. Much better!

<h1 id="custom-domain">Step 10: Custom domain name</h1>

If you have a custom domain that you want to host your droplet on, that's not too hard to set up.  For example, my main droplet's IP is [198.199.117.12](http://198.199.117.12), but I also purchased the domain [daattali.com](http://daattali.com/) so that it would be able to host my droplet with a much simpler URL.

There are two main steps involved: you need to configure your domain on DO, and to change your domain servers from your registrar to point to DO.

### Configure your domain

In the DO website, click on "DNS" at the top, and then we want to add a domain.  Select your droplet from the appropriate input box, and put in your domain name in the URL field. Do not add "www" to the beginning of your domain. Then click on "Create Domain".

You will now get to a page where you can enter more details. 

- The "A" row should have `@` in the first box and the droplet's IP in the second box.
- In the three "NS" fields, you should have the values `ns1.digitalocean.com.`, `ns2.digitalocean.com.`, `ns3.digitalocean.com.`
- You also need to add a "CNAME" record, so click on "Add Record", choose "CNAME", enter `www` in the first box, and your domain's name in the second box. You need to append a dot (`.`) to the end of the domain name.

Here is what my domain settings look like, make sure yours look similar (note the dot suffix on all the domain names):

![DigitalOcean DNS settings]({{ site.url }}/img/blog/digital-ocean/DigitalOceanDNS.png) 

### Change your domain servers to DigitalOcean

You also need to configure your domain registrar by adding the 3 nameservers `ns1.digitalocean.com`, `ns2.digitalocean.com`, `ns3.digitalocean.com`. It's fairly simple, but the exact instructions are different based on your registrar, so [here is a guide](https://www.digitalocean.com/community/tutorials/how-to-point-to-digitalocean-nameservers-from-common-domain-registrars) with all the common registrars and how to do this step with each of them.

I use Namecheap, so this is what my domain configuration needs to look like:
![Namecheap domain servers]({{ site.url }}/img/blog/digital-ocean/namecheap-domain-server.png) 

**And that's it! Now you have a nicely configured private web server with your very own RStudio and Shiny Server, and you can do anything else you'd like on it.** 

<h1 id="resources">Resources</h1>

This is a list of the main blog/StackOverflow/random posts I had to consult while getting all this to work. 

- [https://www.raspberrypi.org/documentation/remote-access/web-server/nginx.md](https://www.raspberrypi.org/documentation/remote-access/web-server/nginx.md)
- [http://www.sysads.co.uk/2014/06/install-r-base-3-1-0-ubuntu-14-04/](http://www.sysads.co.uk/2014/06/install-r-base-3-1-0-ubuntu-14-04/)
- [http://stackoverflow.com/questions/17173972/how-do-you-add-swap-to-an-ec2-instance](http://stackoverflow.com/questions/17173972/how-do-you-add-swap-to-an-ec2-instance)
- [http://stackoverflow.com/questions/20923209/problems-installing-the-devtools-package](http://stackoverflow.com/questions/20923209/problems-installing-the-devtools-package)
- [https://www.digitalocean.com/community/tutorials/how-to-set-up-rstudio-on-an-ubuntu-cloud-server](https://www.digitalocean.com/community/tutorials/how-to-set-up-rstudio-on-an-ubuntu-cloud-server)
- [http://www.rstudio.com/products/rstudio/download-server/](http://www.rstudio.com/products/rstudio/download-server/)
- [http://withr.me/set-up-shiny-server-on-www-dot-digitalocean-dot-com/](http://withr.me/set-up-shiny-server-on-www-dot-digitalocean-dot-com/)
- [https://www.digitalocean.com/community/tutorials/how-to-point-to-digitalocean-nameservers-from-common-domain-registrars](https://www.digitalocean.com/community/tutorials/how-to-point-to-digitalocean-nameservers-from-common-domain-registrars) 
- [https://www.digitalocean.com/community/tutorials/how-to-set-up-a-host-name-with-digitalocean](https://www.digitalocean.com/community/tutorials/how-to-set-up-a-host-name-with-digitalocean)  

## Disclaimer

I'm not a sysadmin and a lot of this stuff was learned very quickly from random Googling, so it's very possible that some steps here are not the very best way of performing some tasks. If anyone has any comments on anything in this document, I'd love to [hear about it](./aboutme#contact)!
