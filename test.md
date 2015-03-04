---
layout: default
bigimgs: "kjhkj"
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
