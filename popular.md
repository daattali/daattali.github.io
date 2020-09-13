---
layout: page
title: "Hi, I'm Dean"
subtitle: R-Shiny Expert / Open-Source Developer / Tireless Traveller
css: "/assets/css/index.css"
share-title: "Dean Attali - R-Shiny consultant | Popular posts"
share-description: "List of all the most popular posts by Dean Attali."
support_promo_box: true
cover-img:
  - "/assets/img/big-imgs/costa-rica-house.jpeg" : "Montezuma, Costa Rica (2011)"
  - "/assets/img/big-imgs/grouse-grind.jpeg" : "Vancouver, Canada (2014)"
  - "/assets/img/big-imgs/hawaii1.jpeg" : "Kauai, HI, USA (2014)"
  - "/assets/img/big-imgs/hawaii2.jpeg" : "Kauai, HI, USA (2014)"
  - "/assets/img/big-imgs/hongkong-cliff-dive.jpeg" : "Hong Kong (2014)"
  - "/assets/img/big-imgs/hongkong-infinity-pool.jpeg" : "Hong Kong (2014)"
  - "/assets/img/big-imgs/israel-dive1.jpeg" : "Eilat, Israel (2013)"
  - "/assets/img/big-imgs/israel-dive2.jpeg" : "Eilat, Israel (2013)"
  - "/assets/img/big-imgs/israel-dive3.jpeg" : "Eilat, Israel (2013)"
  - "/assets/img/big-imgs/laos-pond2.jpeg" : "Luang Prabang, Laos (2014)"
  - "/assets/img/big-imgs/laos-pond1.jpeg" : "Luang Prabang, Laos (2014)"
  - "/assets/img/big-imgs/laos-pond3.jpeg" : "Luang Prabang, Laos (2014)"
  - "/assets/img/big-imgs/st-martin.jpeg" : "St Maarten (2014)"
  - "/assets/img/big-imgs/tanzania.jpeg" : "Mt Kilimanjaro, Tanzania (2012)"
  - "/assets/img/big-imgs/vietnam-beach.jpg" : "Mui Ne, Vietnam (2013)"
  - "/assets/img/big-imgs/vietnam-climb.jpeg" : "Cat Ba, Vietnam (2013)"
  - "/assets/img/big-imgs/vietnam-climb2.jpeg" : "Cat Ba, Vietnam (2013)" 
  - "/assets/img/big-imgs/vietnam-dunes.jpeg" : "Mui Ne, Vietnam (2013)"
  - "/assets/img/big-imgs/vietnam-dunes2.jpeg" : "Mui Ne, Vietnam (2013)"
  - "/assets/img/big-imgs/vietnam-fruits.jpeg" : "Nha Trang, Vietnam (2013)"
  - "/assets/img/big-imgs/vietnam-hat.jpeg" : "Hoi An, Vietnam (2013)"
  - "/assets/img/big-imgs/vietnam-jump.jpeg" : "Mui Ne, Vietnam (2013)"
  - "/assets/img/big-imgs/vietnam-ricefield.jpeg" : "Sapa, Vietnam (2013)"
  - "/assets/img/big-imgs/vietnam-scooter.jpeg" : "Da Nang, Vietnam (2013)"
  - "/assets/img/big-imgs/vietnam-walk.jpeg" : "Sapa, Vietnam (2013)"
  - "/assets/img/big-imgs/california-skydive.jpeg" : "Davis, CA, USA (2008)"
  - "/assets/img/big-imgs/california-surf.jpeg" : "Los Angeles, CA, USA (2008)"
  - "/assets/img/big-imgs/california-surf2.jpeg" : "Los Angeles, CA, USA (2008)" 
  - "/assets/img/big-imgs/california-surf3.jpeg" : "Santa Cruz, CA, USA (2009)"
  - "/assets/img/big-imgs/costa-rica.jpeg" : "Arenal, Costa Rica (2011)"
  - "/assets/img/big-imgs/rio-hanggliding.JPG" : "Rio de Janeiro, Brazil (2015)"  
  - "/assets/img/big-imgs/rio-steps.jpg" : "Rio de Janeiro, Brazil (2015)"  
  - "/assets/img/big-imgs/hawaii-turtles.jpg" : "Oahu, HI, USA (2016)"  
  - "/assets/img/big-imgs/banff-mountains.jpg" : "Banff, Canada (2017)"  
  - "/assets/img/big-imgs/delft-canal.jpg" : "Delft, Netherlands (2017)"  
  - "/assets/img/big-imgs/delft-duck.jpg" : "Delft, Netherlands (2017)"  
  - "/assets/img/big-imgs/israel-camel.jpg" : "Arad, Israel (2018)"  
  - "/assets/img/big-imgs/israel-haifa.jpg" : "Haifa, Israel (2018)"  
  - "/assets/img/big-imgs/maui-volcano.jpg" : "Maui, HI, USA (2018)"  
---

<div class="list-filters">
  <a href="/" class="list-filter">All posts</a>
  <a href="/popular" class="list-filter filter-selected">Most Popular</a>
  <a href="/tutorials" class="list-filter">Tutorials</a>
  <a href="/tags" class="list-filter">Index</a>
</div>

{% assign posts = paginator.posts | default: site.posts %}

<div class="posts-list">
  {% for post in site.tags.popular %}
  <article class="post-preview">

    {%- capture thumbnail -%}
      {% if post.thumbnail-img %}
        {{ post.thumbnail-img }}
      {% elsif post.cover-img %}
        {% if post.cover-img.first %}
          {{ post.cover-img[0].first.first }}
        {% else %}
          {{ post.cover-img }}
        {% endif %}
      {% else %}
      {% endif %}
    {% endcapture %}
    {% assign thumbnail=thumbnail | strip %}

    {% if site.feed_show_excerpt == false %}
    {% if thumbnail != "" %}
    <div class="post-image post-image-normal">
      <a href="{{ post.url | absolute_url }}" aria-label="Thumbnail">
        <img src="{{ thumbnail | absolute_url }}" alt="Post thumbnail">
      </a>
    </div>
    {% endif %}
    {% endif %}

    <a href="{{ post.url | absolute_url }}">
      <h2 class="post-title">{{ post.title }}</h2>

      {% if post.subtitle %}
        <h3 class="post-subtitle">
        {{ post.subtitle }}
        </h3>
      {% endif %}
    </a>

    <p class="post-meta">
      {% assign date_format = site.date_format | default: "%B %-d, %Y" %}
      Posted on {{ post.date | date: date_format }}
    </p>

    {% if thumbnail != "" %}
    <div class="post-image post-image-small">
      <a href="{{ post.url | absolute_url }}" aria-label="Thumbnail">
        <img src="{{ thumbnail | absolute_url }}" alt="Post thumbnail">
      </a>
    </div>
    {% endif %}

    {% unless site.feed_show_excerpt == false %}
    {% if thumbnail != "" %}
    <div class="post-image post-image-short">
      <a href="{{ post.url | absolute_url }}" aria-label="Thumbnail">
        <img src="{{ thumbnail | absolute_url }}" alt="Post thumbnail">
      </a>
    </div>
    {% endif %}

    <div class="post-entry">
      {% assign excerpt_length = site.excerpt_length | default: 50 %}
      {{ post.excerpt | strip_html | xml_escape | truncatewords: excerpt_length }}
      {% assign excerpt_word_count = post.excerpt | number_of_words %}
      {% if post.content != post.excerpt or excerpt_word_count > excerpt_length %}
        <a href="{{ post.url | absolute_url }}" class="post-read-more">[Read&nbsp;More]</a>
      {% endif %}
    </div>
    {% endunless %}

    {% if site.feed_show_tags != false and post.tags.size > 0 %}
    <div class="blog-tags">
      Tags:
      {% for tag in post.tags %}
      <a href="{{ '/tags' | absolute_url }}#{{- tag -}}">{{- tag -}}</a>
      {% endfor %}
    </div>
    {% endif %}

   </article>
  {% endfor %}
</div>

{% if paginator.total_pages > 1 %}
<ul class="pagination main-pager">
  {% if paginator.previous_page %}
  <li class="page-item previous">
    <a class="page-link" href="{{ paginator.previous_page_path | absolute_url }}">&larr; Newer Posts</a>
  </li>
  {% endif %}
  {% if paginator.next_page %}
  <li class="page-item next">
    <a class="page-link" href="{{ paginator.next_page_path | absolute_url }}">Older Posts &rarr;</a>
  </li>
  {% endif %}
</ul>
{% endif %}
