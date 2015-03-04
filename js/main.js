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
$("#header-big-imgs").load(function() {
console.log("F");
  if ($("#header-big-imgs").length > 0) {
    var bigImgEl = $("#header-big-imgs")[0];
    var numImgs = bigImgEl.getAttribute("data-num-img");
	var randNum = Math.floor((Math.random() * numImgs) + 1);
    var src = bigImgEl.getAttribute("data-big-img-" + randNum);
    $(".intro-header.big-img").css("background-image", 'url(' + src + ')');
  }
});

$(document).ready(function() {console.log("H");});