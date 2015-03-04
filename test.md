---
layout: default
aa:
  - "/img/big-imgs/costa-rica-house.JPG" : "kjhkj"
---

{% for a in page.aa %}
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
