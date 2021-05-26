var custom = {

  scrollBoxCheck : false,

  init : function() {

    // Check if there is a scrollbox to initialize
    if ($("#scroll-box").length > 0) {
      if ($("article").length > 0) {
        custom.scrollBoxCheck = Math.min(1500, $("article").offset().top + $("article").height() * 0.4);
        $("#scroll-box-close").click(function() {
          $("body").removeClass("scroll-box-on");
        });
      }
    }

    $(window).scroll(function() {
      // Check if the scrollbox should be made visible
      if (custom.scrollBoxCheck) {
        if ($(window).scrollTop() > custom.scrollBoxCheck) {
          custom.scrollBoxCheck = false;
          $("body").addClass("scroll-box-on");
        }
      }

    });

    // set up Google Analytics event tracking
    if (typeof ga === "function") {
      $("a[data-ga-event]").click(function() {
        ga('send', 'event', $(this).data("ga-category"), $(this).data("ga-action"), $(this).data("ga-label"));
      });
    }
  }
};

document.addEventListener('DOMContentLoaded', custom.init);
