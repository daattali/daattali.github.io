# How to Set Up Shiny Server on Ubuntu 14.04

### Introduction

[Shiny](http://shiny.rstudio.com/) is an R package that allows users to convert R code into an interactive webpage. [Shiny server](http://www.rstudio.com/products/shiny/shiny-server/) is a server provided by RStudio that can be used to host and manage Shiny applications on the web. Other than hosting Shiny applications, Shiny Server can also host [interactive R markdown documents](http://rmarkdown.rstudio.com/). Shiny Server has both a free Open Source version and a paid Professional version.

In this guide, we will learn how to set up the Open Source Shiny Server on a DigitalOcean droplet running Ubuntu 14.04. If your droplet is running a different version of Ubuntu or a different Linux distribution, most of the instructions will still apply, but you may need to modify some of the commands to match your specific distribution. There will also be clear instructions on how to install a Shiny Server Professional if you purchased a license for it. Following this guide to completion should take about 10-15 minutes.

<$>[note]
**Note:** for the remainder of this guide, whenever you see <^>your_server_ip<^> you need to replace it with your droplet's IP.
<$>

## Step 1 - Logging into your droplet

To begin, you should be logged into your DigitalOcean droplet.  You can follow the guide on [how to connect to your droplet](https://www.digitalocean.com/community/tutorials/how-to-connect-to-your-droplet-with-ssh) if you need help with that.

## Step 2 - Changing to a non-root user

Using the default **root** user is generally not recommended, so let's add another user called **sammy**. You can choose a different name if you'd like. If you already have a non **root** user and you know how to switch to it, you can skip this step.

```
adduser sammy
```

We want to give **sammy** administrator privileges so that we can install new software with that user.

```
gpasswd -a sammy sudo
```

Now we can switch to using **sammy**.

```
su - sammy
```

## Step 3 - Installing R

Before we can install Shiny Server, we need to make sure our droplet has R on it.

First we want to ensure that we get the most recent version of R by adding the correct repository to the sources list found at `/etc/apt/sources.list`.  For Ubuntu 14.04 we want to add `trusty`; if you're using a different Ubuntu version then you can use [this document](http://cran.r-project.org/bin/linux/ubuntu/README) to find out what repository to add instead.

```
sudo sh -c 'echo "deb http://cran.rstudio.com/bin/linux/ubuntu trusty/" >> /etc/apt/sources.list'
```

After setting the repository, we should update the list of available packages.

```
sudo apt-get update
```

Next we need to add the public keys.

```
gpg --keyserver keyserver.ubuntu.com --recv-key E084DAB9
gpg -a --export E084DAB9 | sudo apt-key add -
```

Now we're ready to install R.

```
sudo apt-get install r-base
```

## Step 4 - 

### Conclusion




---

```

sudo /bin/dd if=/dev/zero of=/var/swap.1 bs=1M count=1024
sudo /sbin/mkswap /var/swap.1
sudo /sbin/swapon /var/swap.1
sudo sh -c 'echo "/var/swap.1 swap swap defaults 0 0 " >> /etc/fstab'
sudo su - -c "R -e \"install.packages('shiny', repos='http://cran.rstudio.com/')\""
sudo apt-get install gdebi-core
wget https://s3.amazonaws.com/rstudio-shiny-server-pro-build/ubuntu-12.04/x86_64/shiny-server-commercial-1.3.0.540-amd64.deb
sudo gdebi shiny-server-commercial-1.3.0.540-amd64.deb

sudo groupadd shiny-apps
sudo usermod -aG shiny-apps dean
sudo usermod -aG shiny-apps shiny
cd /srv/shiny-server
sudo chown -R dean:shiny-apps .
sudo chmod g+w .
sudo chmod g+s .
```

`<^>your_server_ip<^>` or `<^>111.111.111.111<^>` 

your_server as hostname (bold)

`http://<^>your_server_ip<^>/`

use sammy user (bold usernames)


IMAGES:
745x745 max
.png
#eeeeee border
imgur
