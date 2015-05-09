How to easily get your very own RStudio Server and Shiny Server with DigitalOcean

If you've always wanted to have an RStudio Server of your own so that you can access R from anywhere, or your own Shiny Server to host your awesome shiny apps, [DigitalOcean (DO)](https://www.digitalocean.com/?refcode=358494f80b99) can help you get there easily. DigitalOcean provides virtual private servers (they call each server a droplet), which means you can pay $5/month to have your own server "in the cloud" that you can access from anywhere and host anything on it. Check out [my DO droplet](http://daattali.com/) to see it in action!  If you use [my referral link](https://www.digitalocean.com/?refcode=358494f80b99), you'll get $10 in credits, which is enough to give you a private server for 2 months.

This all started a couple of months ago when I asked my supervisor, [Jenny Bryan](https://twitter.com/JennyBryan), if there was a way for me to do some R-ing when I'm away from my machine. She told me that she doesn't have a solution for me, but that I should check out [DO]((https://www.digitalocean.com/?refcode=358494f80b99)), so I did. And it turns out that it's very convenient for hosting my own RStudio Server and anything else I'd like to host, and very affordable **even for my student self**!

This post will cover how to set up R, RStudio Server, Shiny Server, and a few other helpful features on a brand new DO droplet (remember: droplet = your machine in the cloud). The tutorial might seem length, but it's actually very simple, I'm just breaking up every step into very fine details.

### Summary

- [Step 1: Sign up to DigitalOcean](#sign-up)
- [Step 2: Create a new droplet](#create-droplet)
- [Step 3: Log in to your very own shiny new server](#login)
- [Step 4: Ensure you don't shoot yourself in the foot](#safety-first)
- [Step 5: See your droplet in a browser](#nginx)
- [Step 6: Install R](#install-r)

## Step 1: Sign up to DigitalOcean (~ 3 min) {

Go to [DigitalOcean](https://www.digitalocean.com/?refcode=358494f80b99) (use this referral link to get 2 months!) and sign up. Registration is quick and painless, but I think in order to verify your account (and to get your $10) you have to provide credit card details.  If you go to [your Billing page](https://cloud.digitalocean.com/settings/billing) hopefully you will see the $10 credit. This is the last time I'm going to mention money, I promise :p

## Step 2: Create a new droplet (~ 2 min)

Now let's claim one of DO's machines as our own! It's very simple that you definitely don't need my instructions, just click on the big "Create Droplet" button and choose your settings. I chose the smallest/weakest machine ($5/month plan) and it's good enough for me. I also chose San Francisco because it's the closest to me, though it really wouldn't make much of a noticeable difference where the server is located. For OS, I chose to go with the default Ubuntu 14.04 x64.  I highly recommend you add an SSH key at the last step if you know how to do that. If not, either read up on it or just proceed without an SSH key.

*Note: all subsequent steps assume that you are also using the weakest server possible with Ubuntu 14.04 x64. If you chose different settings, the general instructions will still apply but some of the specific commands/URLs might need to change.*

Even though you probably don't need it, here's a short GIF showing me creating a new droplet:
~[Create droplet]({{ site.url }}/img/blog/digital-ocean/do-create.gif)

# Step 3: Log in to your very own shiny new server (< 1 min)

Once the droplet is ready (can take a few minutes), you'll be redirected to a page that shows you information about the new droplet, including its IP. From now on, I'll use the random IP address `123.456.1.2` for the rest of this post, but remember to always substitute that with your droplet's IP (it should be visible on the main DO page).

One option to log into your droplet is through the "Access" tab on that page, but it's slow and ugly, so I prefer logging in on my own machine. If you're on a unix machine, you can just use `ssh 123.456.1.2`. I'm on Windows, so I use [PuTTY](http://www.chiark.greenend.org.uk/~sgtatham/putty/) to SSH ("login") into other machines. Use the IP that you see on the page, with the username `root`.  If you used an SSH key then you don't need to provide a password; otherwise, a password was sent to your email.

# Step 4: Ensure you don't shoot yourself in the foot (< 1 min)

The first thing I like to do is add a non-root user so that we won't accidentally do something stupid as "root". Let's add a user named "dean" and give him admin power.  You will be asked to give some information for this new user.

```
adduser dean
gpasswd -a dean sudo
```

From now on I will generally log into this server as "dean".  If I'll need to run any commands requiring admin abilities, I just have to prepend the command with `sudo`.  Let's say goodbye to "root" and switch to "dean".

```
su - dean
```

# Step 5: See your droplet in a browser (~ 2 min)

Right now if you try to visit `http://123.456.1.2` in a browser, you'll get a "webpage not available error". Let's make our private server serve a webpage there instead, as a nice visual reward for getting this far. Install nginx:

```
sudo apt-get update
sudo apt-get install nginx
```

Now if you visit `http://123.456.1.2`, you should see a welcome message to nginx. Instant gratification!

#### Quick nginx references

The default file that is served is located at `/usr/share/nginx/html/index.html`, so if you want to change what that webpage is showing, just edit that file with `sudo vim /usr/share/nginx/html/index.html`.  The configuration file is located at `/etc/nginx/nginx.conf`. 

When you edit an HTML file, you will be able to see the change immediately when you refresh the page, but if you make configuration changes, you need to restart nginx. In the future, you can stop/start/restart nginx with

```
sudo service nginx stop
sudo service nginx start
sudo service nginx restart
```


# Step 6: Install R (~ 3 min)

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

Before installing any packages, I like to make sure I have `devtools` so that I can install any GitHub package. Annoyingly, when you try to `install.packages("devtools")`, it will not install but you won't see any error messages. We need to install `libcurl` on the machine first, so quit R (`quit()`) and run the following command:

```
sudo apt-get install libcurl4-gnutls-dev
```



https://www.digitalocean.com/community/tutorials/how-to-set-up-rstudio-on-an-ubuntu-cloud-server (had to use a newer RStudio Server version, add more swap space as i said in comments, install libcurl to get devtools to install ()



```
# custom domain

![DigitalOcean DNS settings]({{ site.url }}/img/blog/DigitalOceanDNS.png)  
https://www.digitalocean.com/community/tutorials/how-to-point-to-digitalocean-nameservers-from-common-domain-registrars   
https://www.digitalocean.com/community/tutorials/how-to-set-up-a-host-name-with-digitalocean   
```

http://daattali.com/rstudio



might need to add swap space depending on how much ram the server has

shiny server: http://withr.me/set-up-shiny-server-on-www-dot-digitalocean-dot-com/

add monitor to the server http://www.monitorix.org/doc-debian.html  http://daattali.com:8080/health

reverse proxy in /etc/nginx/sites-enabled/default to listen on port 80 at /shiny/ and forward to shiny server




```
# resources
http://withr.me/set-up-shiny-server-on-www-dot-digitalocean-dot-com/
http://www.sysads.co.uk/2014/06/install-r-base-3-1-0-ubuntu-14-04/ 
https://www.digitalocean.com/community/tutorials/how-to-set-up-rstudio-on-an-ubuntu-cloud-server
https://www.raspberrypi.org/documentation/remote-access/web-server/nginx.md
http://www.monitorix.org/doc-debian.html
https://www.digitalocean.com/community/tutorials/how-to-point-to-digitalocean-nameservers-from-common-domain-registrars 
https://www.digitalocean.com/community/tutorials/how-to-set-up-a-host-name-with-digitalocean  
http://stackoverflow.com/questions/20923209/problems-installing-the-devtools-package
```
