// Dean Attali 2016

var main = {

  bigImgEl : null,
  numImgs : null,
  scrollBoxCheck : false,
  
  init : function() {
    
    if ($("meta[name='twitter:title'").length > 0) {
      var addthis_share = addthis_share || {}
      $.extend(addthis_share, {
      	passthrough : {
      		twitter: {
      			via: "daattali",
      			text: trim($("meta[name='twitter:title'").attr("content"), 100)
      		}
      	}
      });
      function trim(s, n){
        return (s.length > n) ? s.substr(0,n-3)+'...' : s;
      };
    }
    
    // Check if there is a scrollbox to initialize
    if ($("#scroll-box").length > 0 && Cookies.get('daScrollboxSubscribe2') === undefined) {
      if ($("article").length > 0) {
        main.scrollBoxCheck = $("article").offset().top + $("article").height() * 0.6;
        $("#scroll-box").css('right', '-' + $("#scroll-box").outerWidth() + 'px');
        $("#scroll-box-close").click(function() {
          $("body").removeClass("scroll-box-on");
          Cookies.set('daScrollboxSubscribe2', '1', { expires: 1 });
        });
        $("#mc-embedded-subscribe").click(function() {
          $("body").removeClass("scroll-box-on");
          Cookies.set('daScrollboxSubscribe2', '1', { expires: 365 });
        });
      }
    }
    
    $(window).scroll(function() {
      // Shorten the navbar after scrolling a little bit down
      if ($(".navbar").offset().top > 50) {
        $(".navbar").addClass("top-nav-short");
      } else {
        $(".navbar").removeClass("top-nav-short");
      }
      
      // Check if the scrollbox should be made visible
      if (main.scrollBoxCheck) {
        if ($(window).scrollTop() > main.scrollBoxCheck) {
          setTimeout( function(){ $("#scroll-box").css('right', '0'); }, 500);
          main.scrollBoxCheck = false;
          $("body").addClass("scroll-box-on");
        }
      }

    });
    
    // On mobile, hide the avatar when expanding the navbar menu
    $('#main-navbar').on('show.bs.collapse', function () {
      $(".navbar").addClass("top-nav-expanded");
    });
    $('#main-navbar').on('hidden.bs.collapse', function () {
      $(".navbar").removeClass("top-nav-expanded");
    });
	
    // On mobile, when clicking on a multi-level navbar menu, show the child links
    $('#main-navbar').on("click", ".navlinks-parent", function(e) {
      var target = e.target;
      $.each($(".navlinks-parent"), function(key, value) {
        if (value == target) {
          $(value).parent().toggleClass("show-children");
        } else {
          $(value).parent().removeClass("show-children");
        }
      });
    });	

    // show a message if there is one to show
    var qs = main.getQueryParams();
    if (qs.message) {
      $(".container")[0].innerHTML =
        '<div class="row"><div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">' +
          '<div class="alert alert-success" role="alert">' +
            qs.message +
        "</div></div></div>" +
        $(".container")[0].innerHTML;
    }

    // show the big header image	
    main.initImgs();
    
    // set up Google Analytics event tracking
    if (typeof ga === "function") {
      $("a[data-ga-event]").click(function() {
        ga('send', 'event', $(this).data("ga-category"), $(this).data("ga-action"), $(this).data("ga-label"));
      });
    }
  },
  
  initImgs : function() {
    // If the page was large images to randomly select from, choose an image
    if ($("#header-big-imgs").length > 0) {
      main.bigImgEl = $("#header-big-imgs");
      main.numImgs = main.bigImgEl.attr("data-num-img");

      // 2fc73a3a967e97599c9763d05e564189
	    // set an initial image
	    var imgInfo = main.getImgInfo();
	    var src = imgInfo.src;
	    var desc = imgInfo.desc;
  	  main.setImg(src, desc);
  	
	    // For better UX, prefetch the next image so that it will already be loaded when we want to show it
  	  var getNextImg = function() {
  	    var imgInfo = main.getImgInfo();
  	    var src = imgInfo.src;
  	    var desc = imgInfo.desc;		  
  	    
  		  var prefetchImg = new Image();
    		prefetchImg.src = src;
  		  // if I want to do something once the image is ready: `prefetchImg.onload = function(){}`
  		
    		setTimeout(function() {
    		  var img = $("<div></div>").addClass("big-img-transition").css("background-image", 'url(' + src + ')');
    		  $(".intro-header.big-img").prepend(img);
    		  setTimeout(function(){ img.css("opacity", "1"); }, 50);
  		  
  		    // after the animation of fading in the new image is done, prefetch the next one
    		  //img.one("transitioned webkitTransitionEnd oTransitionEnd MSTransitionEnd", function(){
  		    setTimeout(function() {
  		      main.setImg(src, desc);
  			    img.remove();
    			  getNextImg();
  		    }, 1000); 
    		  //});		
    		}, 6000);
    	};
	  
  	  // If there are multiple images, cycle through them
  	  if (main.numImgs > 1) {
    	  getNextImg();
  	  }
    }
  },
  
  getImgInfo : function() {
  	var randNum = Math.floor((Math.random() * main.numImgs) + 1);
    var src = main.bigImgEl.attr("data-img-src-" + randNum);
	  var desc = main.bigImgEl.attr("data-img-desc-" + randNum);
	
  	return {
  	  src : src,
  	  desc : desc
  	}
  },
  
  setImg : function(src, desc) {
  	$(".intro-header.big-img").css("background-image", 'url(' + src + ')');
  	if (typeof desc !== typeof undefined && desc !== false) {
  	  $(".img-desc").text(desc).show();
  	} else {
  	  $(".img-desc").hide();  
  	}
  },
 
 // get the GET parameters in the URL
 getQueryParams : function() {
    var qs = document.location.search.replace(/\?/g, "&").split("+").join(" ");

    var params = {}, tokens, re = /[?&]?([^=]+)=([^&]*)/g;

    while (tokens = re.exec(qs)) {
      params[decodeURIComponent(tokens[1])] = decodeURIComponent(tokens[2]);
    }

    return params;
  }  
};

document.addEventListener('DOMContentLoaded', main.init);
