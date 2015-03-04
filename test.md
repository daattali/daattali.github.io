---
layout: default
aa:
  - "fd" : "kjhkj"
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
