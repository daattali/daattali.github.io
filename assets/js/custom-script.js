var custom = {

  scrollBoxCheck : false,
  purpleAdsCheck : false,

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

    if ($(window).width() < 750) {
      custom.purpleAdsCheck = 1500;
      $("body").addClass("purpleads-hide");
    }

    $(window).scroll(function() {
      // Check if the scrollbox should be made visible
      if (custom.scrollBoxCheck) {
        if ($(window).scrollTop() > custom.scrollBoxCheck) {
          custom.scrollBoxCheck = false;
          $("body").addClass("scroll-box-on");
        }
      }

      if (custom.purpleAdsCheck) {
        if ($(window).scrollTop() > custom.purpleAdsCheck) {
          custom.purpleAdsCheck = false;
          $("body").removeClass("purpleads-hide");
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
