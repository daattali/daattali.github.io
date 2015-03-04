---
layout: default
aa:
  - "bb"
  - "cc"
  - "asdfds": "vvvvvv"
  - "ddd"
  - "assdffsd" : "/img/sdfdsfsdffgfd-f"
  - "vvv"
  - "/img/sdfdsfsdffgfd-f" : "pic"
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
