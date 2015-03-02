---
layout: minimal
aaa:
  Home: ""
  About Me: "aboutme"
  Projects: "projects"
  Blog: "blog"
bb: fff
---
{{ page.bb }}

<br/>
one:
{% for link_info in page.aaa %}
		{% for link in link_info %}
      {{ link[1] }} sdfdsf {{ link[0] }}
    {% endfor %}
  {% endfor %}


<br/>
two:

{% for link_info in page.aaa %}
{{ link_info[0] }}aa{{ link_info[1] }}
{% endfor %}
 
