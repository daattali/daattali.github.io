# How to Set Up Shiny Server on Ubuntu 14.04

### Introduction

[Shiny](http://shiny.rstudio.com/) is an R package that allows users to convert R code into an interactive webpage. [Shiny server](http://www.rstudio.com/products/shiny/shiny-server/) is a server provided by RStudio that can be used to host and manage Shiny applications on the web. Other than hosting Shiny applications, Shiny Server can also host [interactive R markdown documents](http://rmarkdown.rstudio.com/). Shiny Server has both a free Open Source version and a paid Professional version that includes more features.

In this guide, we will learn how to set up the Open Source Shiny Server on a DigitalOcean droplet running Ubuntu 14.04. If your droplet is running a different version of Ubuntu or a different Linux distribution, most of the instructions will still apply, but you may need to modify some of the commands to match your specific distribution. You can also use this guide to set up Shiny Server Professional. Following this guide to completion should take about 10-15 minutes.

<$>[note]
**Note:** for the remainder of this guide, whenever you see <^>your_server_ip<^> you need to replace it with your droplet's IP.
<$>

## Step 1 - Logging into your droplet

To begin, you should be logged into your DigitalOcean droplet.  You can follow the guide on [how to connect to your droplet](https://www.digitalocean.com/community/tutorials/how-to-connect-to-your-droplet-with-ssh) if you need help with that.

## Step 2 - Changing to a non root user

Using the default **root** user is generally not recommended, so let's add another user called **sammy**. You can choose a different name if you'd like. If you already have a non **root** user and you know how to switch to it, you can skip this step.

```
adduser sammy
```

The system will ask you for a password and for some basic information about the new user - all that information is optional. We want to give **sammy** administrator privileges so that we can install new software with that user.

```
gpasswd -a sammy sudo
```

Now we can switch to using **sammy**.

```
su - sammy
```

For the rest of this guide, we will be issuing commands as **sammy**.

## Step 3 - Installing R

Before we can install Shiny Server, we need to make sure our droplet has R installed on it.

First we want to ensure that we get the most recent version of R by adding the correct repository to the sources list found at `/etc/apt/sources.list`.  For Ubuntu 14.04 we want to add `trusty`; if you're using a different Ubuntu version then you can use [this document](http://cran.r-project.org/bin/linux/ubuntu/README) to find out what repository to add instead.

```
sudo sh -c 'echo "deb http://cran.rstudio.com/bin/linux/ubuntu trusty/" >> /etc/apt/sources.list'
```

Next we need to add the public keys.

```
gpg --keyserver keyserver.ubuntu.com --recv-key E084DAB9
gpg -a --export E084DAB9 | sudo apt-key add -
```

After setting the repository, we should update the list of available packages.

```
sudo apt-get update
```

Now we're ready to install R.

```
sudo apt-get install r-base
```

At this point, you should have an installation of the latest R version on your droplet.  You can test this by running `R` 

```
R
```

You should see output similar to the following.

```
R version 3.2.1 (2015-06-18) -- "World-Famous Astronaut"
Copyright (C) 2015 The R Foundation for Statistical Computing
Platform: x86_64-pc-linux-gnu (64-bit)

R is free software and comes with ABSOLUTELY NO WARRANTY.
You are welcome to redistribute it under certain conditions.
Type 'license()' or 'licence()' for distribution details.

  Natural language support but running in an English locale

R is a collaborative project with many contributors.
Type 'contributors()' for more information and
'citation()' on how to cite R or R packages in publications.

Type 'demo()' for some demos, 'help()' for on-line help, or
'help.start()' for an HTML browser interface to help.
Type 'q()' to quit R.
```

You can quit R to return to your droplet with the `q()` function.

```
q(save = "no")
```

## Step 4 - Adding swap space

If your droplet has less than 2GB of RAM, you will need to add *swap space*. If you're curious, you can read more about what *swap space* is [in this atricle](https://www.digitalocean.com/community/tutorials/how-to-add-swap-on-ubuntu-14-04).  Without adding more swap space, we will not be able to install many R packages. Let's add 1GB of swap space.

```
sudo /bin/dd if=/dev/zero of=/var/swap.1 bs=1M count=1024
sudo /sbin/mkswap /var/swap.1
sudo /sbin/swapon /var/swap.1
sudo sh -c 'echo "/var/swap.1 swap swap defaults 0 0 " >> /etc/fstab'
```

## Step 5 - Installing Shiny

The last step before installing Shiny Server is to install the `shiny` R package. We will install `shiny` in a way that will make it available to all users on the server.

```
sudo su - -c "R -e \"install.packages('shiny', repos='http://cran.rstudio.com/')\""
```

<$>[note]
**Note:** if you're familiar with R, you might be tempted to install packages directly from R instead of from the command line. The approach used here is the safest way to ensure the installed package gets installed for all users and not just for **sammy**.
<$>

## Step 6 - Installing Shiny Server

We're going to install Shiny Server with the **GDebi** tool, so first we need to install it.

```
sudo apt-get install gdebi-core
```

Now we're ready to download Shiny Server. Assuming your droplet is running 64-bit Ubuntu, use the following command to download Shiny Server. 

```
wget -O shiny-server.deb http://download3.rstudio.org/ubuntu-12.04/x86_64/shiny-server-1.3.0.403-amd64.deb
```

This will download Shiny Server version 1.3.0.403, which is the most up-to-date at the time of this writing. If you want to download the newest version, you can consult [the official Shiny Server download page](http://www.rstudio.com/products/shiny/download-server/) to find the latest version and change the URL accordingly. If you're running a 32-bit operating system or a non Ubuntu distribution, you may need to consult the [Shiny Server download page](http://www.rstudio.com/products/shiny/download-server/) for specific instructions for your operating system. If you are installing Shiny Server Professional, RStudio will give you the URL of the file to download.

Now use **GDebi** to install the file that was downloaded.

```
sudo gdebi shiny-server.deb
```

Shiny Server should now be installed and running on port `3838`. You should be able to see a default welcome screen at `http://<^>your_server_ip<^>:3838/`.

![Shiny Server default welcome page](http://i.imgur.com/jUo5juv.png)

You can make sure your Shiny Server is working properly by going to `http://<^>your_server_ip<^>:3838/sample-apps/hello/`.

## Step 7 - Hosting interactive R markdown documents

Shiny Server is useful not only for hosying Shiny applications, but also for hosting interactive R markdown documents. You can learn more about interactive R markdown documents [on RStudio's official Rmarkdown site](http://rmarkdown.rstudio.com/). At this point you should have a working Shiny Server that can host Shiny applications, but it cannot yet host interactive R markdown documents because the `rmarkdown` R package isn't installed. Shiny Server comes with a sample interactive document that is available at `http://<^>your_server_ip<^>:3838/sample-apps/rmd/`. If you go to that URL right now, you will see an error. Let's install the `rmarkdown` package to fix that.

```
sudo su - -c "R -e \"install.packages('rmarkdown', repos='http://cran.rstudio.com/')\""
```

Now Shiny Server is set up to host interactive R markdown documents as well as Shiny applications.  To verify that interactive documents work, go to `http://<^>your_server_ip<^>:3838/sample-apps/rmd/` and ensure there is no error.

## Step 8 - Install Shiny Server Professional

Only perform this step if you have purchased a Shiny Server Professional license and would like to use the license on this server.

After purchasing the license, RStudio will give you a URL to use to download the Shiny Server Pro file. Download the Shiny Server Pro file.

```
wget -O shiny-server-pro.deb <^>Shiny_Server_Pro_URL<^>
```

Install Shiny Server Pro.

```
sudo gdebi shiny-server-pro.deb
```

You will also be given a Product Key that will be required to activate Shiny Server Pro.

```
sudo /opt/shiny-server/bin/license-manager activate <^>Product_Key<^>
```

Restart Shiny Server Pro so that the activated version will get started.

```
sudo reload shiny-server
```

## Next steps

You now have a functioning Shiny Server that can host Shiny applications or documents. The configuration file for Shiny Server is at `/etc/shiny-server/shiny-server.conf` and by default it is configured to serve applications in the `/srv/shiny-server/` directory. This means that any Shiny application that is placed at `/srv/shiny-server/<^>app_name<^>` will be available to the public at `http://<^>your_server_ip<^>:3838/<^>app_name<^>/`. It's a good idea to have e look at the [Shiny Server Administrator's Guide](http://rstudio.github.io/shiny-server/latest/) to learn how to customize the server to your exact needs and how to manage it.

### Conclusion

In this guide, we went through the steps required to set up Shiny Server on an Ubuntu 14.04 droplet.  By setting up Shiny Server, we are able to host Shiny applications and interactive R documents on the web in a way that is accessible to the public.
