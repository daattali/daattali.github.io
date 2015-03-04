---
layout: default
bigimgs:
  - "/img/big-imgs/costa-rica-house.JPG" : "Montezuma, Costa Rica (2011)"
  - "/img/big-imgs/grouse-grind.jpg" : "Vancouver, Canada (2014)"
  - "/img/big-imgs/hawaii1.jpg" : "Kauai, HI, USA (2014)"
  - "/img/big-imgs/hawaii2.jpg" : "Kauai, HI, USA (2014)"
  - "/img/big-imgs/hongkong-cliff-dive.jpeg" : "Hong Kong (2014)"
  - "/img/big-imgs/hongkong-infinity-pool.jpg" : "Hong Kong (2014)"
  - "/img/big-imgs/israel-camel.jpeg" : "Dead Sea, Israel (2010)"
  - "/img/big-imgs/israel-dive1.jpg" : "Eilat, Israel (2013)"
  - "/img/big-imgs/israel-dive2.jpg" : "Eilat, Israel (2013)"
  - "/img/big-imgs/israel-dive3.jpg" : "Eilat, Israel (2013)"
  - "/img/big-imgs/laos-pond2.jpg" : "Luang Prabang, Laos (2014)"
  - "/img/big-imgs/laos-pond1.jpg" : "Luang Prabang, Laos (2014)"
  - "/img/big-imgs/laos-pond3.jpg" : "Luang Prabang, Laos (2014)"
  - "/img/big-imgs/st-martin.jpg" : "St Maarten (2014)"
  - "/img/big-imgs/tanzania.jpg" : "Mt Kilimanjaro, Tanzania (2012)"
  - "/img/big-imgs/vietnam-beach.jpg" : "Mui Ne, Vietnam (2013)"
  - "/img/big-imgs/vietnam-climb.jpg" : "Cat Ba, Vietnam (2013)"
  - "/img/big-imgs/vietnam-climb2.jpg" : "Cat Ba, Vietnam (2013)" 
  - "/img/big-imgs/vietnam-dunes.jpg" : "Mui Ne, Vietnam (2013)"
  - "/img/big-imgs/vietnam-dunes2.jpg" : "Mui Ne, Vietnam (2013)"
  - "/img/big-imgs/vietnam-fruits.jpg" : "Nha Trang, Vietnam (2013)"
  - "/img/big-imgs/vietnam-hat.jpg" : "Hoi An, Vietnam (2013)"
  - "/img/big-imgs/vietnam-jump.jpg" : "Mui Ne, Vietnam (2013)"
  - "/img/big-imgs/vietnam-ricefield.jpg" : "Sapa, Vietnam (2013)"
  - "/img/big-imgs/vietnam-scooter.jpg" : "Da Nang, Vietnam (2013)"
  - "/img/big-imgs/vietnam-walk.jpg" : "Sapa, Vietnam (2013)"
  - "/img/big-imgs/california-skydive.JPG" : "Davis, CA, USA (2008)"
  - "/img/big-imgs/california-surf.JPG" : "Los Angeles, CA, USA (2008)"
  - "/img/big-imgs/california-surf2.JPG" : "Los Angeles, CA, USA (2008)" 
  - "/img/big-imgs/california-surf3.JPG" : "Santa Cruz, CA, USA (2009)"
  - "/img/big-imgs/costa-rica.JPG" : "Arenal, Costa Rica (2011)"
  - "/img/big-imgs/costa-rica-climb.JPG" : "Manuel Antonio, Costa Rica (2011)"
---

{% for bigimg in page.bigimgs %}
{% assign nu = forloop.index %}
{% for imginfo in bigimg %}
{{ nu }}
  {% if imginfo[0] %}
  {{ imginfo[0] }} : {{ imginfo[1] }} 
  {% else %}
   {{ imginfo }}
  {% endif %}
{% endfor %}
{% endfor %}
