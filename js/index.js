$(document).ready(function() {
  $(".page-heading h1").typed({
    strings : ["Hi, I'm Dean"],
	callback: function() { $(".page-heading").toggleClass("move-up"); }
  });
  
});