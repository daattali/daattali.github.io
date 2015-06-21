# How to Set Up Shiny Server on Ubuntu 14.04

### Introduction

[Shiny server](http://www.rstudio.com/products/shiny/shiny-server/) is enables users to host R Shiny applications on the web. Shiny

## Step 1 - Downloading ...

### Conclusion




---

```
adduser dean
gpasswd -a dean sudo
su - dean
sudo sh -c 'echo "deb http://cran.rstudio.com/bin/linux/ubuntu trusty/" >> /etc/apt/sources.list'
gpg --keyserver keyserver.ubuntu.com --recv-key E084DAB9
gpg -a --export E084DAB9 | sudo apt-key add -
sudo apt-get update
sudo apt-get install r-base
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


note:  (or warning)
<$>[note]
**Note:** ...
<$>
