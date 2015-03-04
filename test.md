---
layout: default
aa:
  - "bb"
  - "cc"
  - "asdfds": "vvvvvv"
  - "ddd"
---

one:
{% for a in page.aa %}
{{ a }}
{% endfor %}

<br/>
two:
{% for a in page.aa %}
{% for o in a %}
{{ o }}
{{ o[0] }}
:
{{ o[1] }}
{% endfor %}
{% endfor %}
