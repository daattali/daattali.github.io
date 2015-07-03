# How to Set Up R on Ubuntu 14.04

### Introduction

[R](http://www.r-project.org/) is a popular open source programming language that specializes in statistical computing and graphics.  It is widely used by statisticians for developing statistical software and performing data analysis. R is known to have a very active and community community One of R's strengths is that it is highly and easily extensible by allowing users to author and submit their own packages. The R community is known to be very active and is continuously adding user-generated statistical packages for specific areas of study. 

R can be installed on most major operating systems. In this guide, we will learn how to set up R on a DigitalOcean Droplet running Ubuntu 14.04.  If your Droplet is running a different version operating system, most of the instructions will still apply, but you may need to modify some of the commands.  Following this guide to completion should take about 5-10 minutes.

## Prerequisites

For this tutorial, you will need:

- Ubuntu 14.04 Droplet with at least 1 GB of RAM. If your Droplet has less than 1 GB of RAM, you need to add 1GB of swap space ([How To Add Swap on Ubuntu 14.04](https://www.digitalocean.com/community/tutorials/how-to-add-swap-on-ubuntu-14-04) gives you step-by-step instructions for adding swap space.)
- A non-root user with sudo privileges ([Initial Server Setup with Ubuntu 14.04](https://www.digitalocean.com/community/tutorials/initial-server-setup-with-ubuntu-14-04) explains how to set this up.)

All the commands in this tutorial should be run as a non-root user. If root access is required for the command, it will be preceded by `sudo`. [Initial Server Setup with Ubuntu 14.04](https://www.digitalocean.com/community/tutorials/initial-server-setup-with-ubuntu-14-04) explains how to add users and give them sudo access.

## Step 1 - Setting Up **APT**

To install R, we're going to use the **APT** (Advanced Packaging Tool) tool.  **APT** uses a special file that lists the sources of where packages should be downnloaded from. That file is `/etc/apt/sources.list`.  In order to get the most recent version of R, we need to add the correct *repository* to the list of sources by adding a line to the souces file. The exact line you need to add will vary depending on the exact Linux distribution. For Ubuntu 14.04, run the following command to add the correct repository to `/etc/apt/sources.list`.  

```command
sudo sh -c 'echo "deb http://cran.rstudio.com/bin/linux/ubuntu trusty/" >> /etc/apt/sources.list'
```

If you are running a different Ubuntu version, consult [this document](http://cran.r-project.org/bin/linux/ubuntu/README) for the correct repository to add.

In order to authenticate packages downloaded using **APT**, we can add a public key. The Ubuntu archives on the repository we are using are signed with a key with ID E084DAB9.  Add this key to your system.

```command
gpg --keyserver keyserver.ubuntu.com --recv-key E084DAB9
```

Next we need to add the key to **APT**.

```command
gpg -a --export E084DAB9 | sudo apt-key add -
```

## Step 2 - Installing R

Now that **APT** has been set up properly, we are ready to use it to install R.

First, we need to update the list of available packages since we updated the sources list.

```command
sudo apt-get update
```

Now we can install R.  We use the `-y` flag to automatically answer **Yes** when asked if we are sure we want to download the package.

```command
sudo apt-get -y install r-base
```

At this point, you should have an installation of the latest R version on your Droplet.  You can test this by running R.

```command
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

>
```

You are now inside the R interactive shell and can run arbitrary R commands.  

Quit R and return to your Droplet with the `q()` function.

```
q(save = "no")
```

### Step 3 -- Installing devtools Package



-------------------------



```command
sudo su - -c "R -e \"install.packages('shiny', repos='http://cran.rstudio.com/')\""
```

<$>[note]
**Note:** if you're familiar with R, you might be tempted to install packages directly from R instead of from the command line. The approach used here is the safest way to ensure the installed package gets installed for all users and not just for the user currently running R.
<$>

## Next Steps

You now have a functioning Shiny Server that can host Shiny applications or interactive documents. The configuration file for Shiny Server is at `/etc/shiny-server/shiny-server.conf`. By default it is configured to serve applications in the `/srv/shiny-server/` directory. This means that any Shiny application that is placed at `/srv/shiny-server/<^>app_name<^>` will be available to the public at `http://<^>your_server_ip<^>:3838/<^>app_name<^>/`. 

It's a good idea to have a look at the [Shiny Server Administrator's Guide](http://rstudio.github.io/shiny-server/latest/) to learn how to customize the server to your exact needs and how to manage it.

To learn more about writing Shiny applications, read the [tutorials on rstudio.com](http://shiny.rstudio.com/tutorial/).

To learn more about writing interactive R markdown documents, check out the R Markdown page on [rstudio.com](http://rmarkdown.rstudio.com/).

## Conclusion

In this guide, we went through the steps required to set up Shiny Server on an Ubuntu 14.04 Droplet.  By setting up Shiny Server, we are able to host Shiny applications and interactive R documents on the web in a way that is accessible to the public.
