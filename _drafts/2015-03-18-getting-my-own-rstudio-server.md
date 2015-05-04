add non-root super user so that you can't mess things up and have to run commands with "sudo" to do anything requiring admin powers

`adduser dean`
`gpasswd -a dean sudo`


http://daattali.com:8787/

https://www.digitalocean.com/community/tutorials/how-to-set-up-rstudio-on-an-ubuntu-cloud-server (had to use a newer RStudio Server version, add more swap space as i said in comments, install libcurl to get devtools to install (apt-get install libcurl4-gnutls-dev), and to get most updated R version http://www.sysads.co.uk/2014/06/install-r-base-3-1-0-ubuntu-14-04/ 

might need to add swap space depending on how much ram the server has

https://www.digitalocean.com/community/tutorials/how-to-point-to-digitalocean-nameservers-from-common-domain-registrars

https://www.digitalocean.com/community/tutorials/how-to-set-up-a-host-name-with-digitalocean

use my referral link

![DigitalOcean DNS settings]({{ site.url }}/img/blog/DigitalOceanDNS.png)

shiny server: http://withr.me/set-up-shiny-server-on-www-dot-digitalocean-dot-com/

nginx to have an index page


add monitor to the server http://www.monitorix.org/doc-debian.html  http://daattali.com:8080/health


reverse proxy in /etc/nginx/sites-enabled/default to listen on port 80 at /shiny/ and forward to shiny server
