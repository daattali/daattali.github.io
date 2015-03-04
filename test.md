---
layout: default
bigimg:
  - "/img/big-imgs/costa-rica-house.JPG" : "Montezuma, Costa Rica (2011)"
---

{% for a in page.bigimg %}
{% for o in a %}
{% if o[0] %}
{{ o[0] }}
:
{{ o[1] }}
{% else %}
{{ o }}
{% endif %}
{% endfor %}
{% endfor %}
