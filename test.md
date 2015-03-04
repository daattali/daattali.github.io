---
layout: default
aa:
  - "bb"
  - "cc"
---

one:
{% for a in page.aa %}
{{ a }}
{% endfor %}

<br/>
two:
{% for a in page.aa %}
{{ a[0] }}
{% endfor %}
