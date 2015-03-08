$(document).ready(function() {
  $(".page-heading h1").typed({
    strings : ["Hi, I'm Dean"],
	typeSpeed : 50,
	callback: function() { $(".page-heading").toggleClass("move-up"); }
  });
  
});