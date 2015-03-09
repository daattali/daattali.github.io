// Shorten the navbar after scrolling a little bit down
$(window).scroll(function() {
    if ($(".navbar").offset().top > 50) {
        $(".navbar").addClass("top-nav-short");
    } else {
        $(".navbar").removeClass("top-nav-short");
    }
});

// On mobile, hide the avatar when expanding the navbar menu
$('#main-navbar').on('show.bs.collapse', function () {
  $(".navbar").addClass("top-nav-expanded");
})
$('#main-navbar').on('hidden.bs.collapse', function () {
  $(".navbar").removeClass("top-nav-expanded");
})

// If the page was given multiple large images to randomly select from
$(document).ready(function() {
  if ($("#header-big-imgs").length > 0) {
    var bigImgEl = $("#header-big-imgs");
    var numImgs = bigImgEl.attr("data-num-img");
	var randNum = Math.floor((Math.random() * numImgs) + 1);
    var src = bigImgEl.attr("data-img-src-" + randNum);
    $(".intro-header.big-img").css("background-image", 'url(' + src + ')');
	var desc = bigImgEl.attr("data-img-desc-" + randNum);
	if (typeof desc !== typeof undefined && desc !== false) {
	  $(".intro-header.big-img").append("<span class='img-desc'>" + desc + "</span>")
    }
	
	var getNextImg = function() {
		var bigImgEl = $("#header-big-imgs");
		var numImgs = bigImgEl.attr("data-num-img");
		var randNum = Math.floor((Math.random() * numImgs) + 1);
		var src = bigImgEl.attr("data-img-src-" + randNum);
		var prefetchImg = new Image();
		prefetchImg.src = src;
		//prefetchImg.onload = function() {
		setTimeout(function(){
		var desc = bigImgEl.attr("data-img-desc-" + randNum);
		if (typeof desc !== typeof undefined && desc !== false) {
		  $(".img-desc").text(desc);
		} else {
		  $(".img-desc").remove();
		}
		var img = $("<div></div>").addClass("big-img-transition").css("background-image", 'url(' + src + ')');
		$(".intro-header.big-img").prepend(img);
		setTimeout(function(){
			img.css("opacity", "1");}, 50);
			img.one("transitioned webkitTransitionEnd oTransitionEnd MSTransitionEnd", function(){
			  $(".intro-header.big-img").css("background-image", 'url(' + src + ')');
			  img.remove();
			  getNextImg();
			});		
		}, 6500);

	}
	getNextImg();
	//}	
  }
});
