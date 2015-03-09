var keshPage = {

  kisstimeout : null,
  kisssound : null,
  kissimg : null,

  init : function() {
	$("#nokesh").click(function() {
	  $("#page-initial").hide();
	  $("#page-nokesh").show();
	});

	$("#yeskesh").click(function() {
	  $("#page-initial").hide();
	  $("#page-yeskesh").show();
	});

	$("#submitqs").click(function() {
	  $("#lies").hide();

	  var clickesyes = false;
	  $("#verifyqs .no").each(function(idx){
	    if ($(this)[0].checked) clickesyes = true;
	  })
	  if (!$("#verifyqs .yes")[0].checked) {
		clickesyes = true;
	  }

  	  if (clickesyes) {
	    $("#lies").fadeIn(500);
	  } else {
	    $("#page-initial").hide();
	    $("#page-yeskesh").hide();
	    $("#page-vday").show();
	    $('.carousel').carousel({
		  interval: 4000
	    });
	    $("#vdaymsg-txt").typed({
		  strings: ["Sorry about those questions, I just had to make sure :p",
			  	    "I'm probably on a plane right now, so I hope this can help you feel my love from thousands of miles away.<br/>" +
				    "You must be running on a dangerously low level of kisses right now, so I tried to make sure you get your dosage."],
		  backDelay: 2500,
		  typeSpeed: 0,
		  callback : function() { $("#kissbtn").show(); }
	    });
	  }
	});

	kissimg = $("#kissimg");
	kisssound = $("#kisssound")[0];
	$("#kissbtn").click(function() {
	  kisssound.pause();
	  kisssound.currentTime = 0;
	  kisssound.play();

	  var img = $(document.createElement("img"));
	  img.attr('src', 'img/kesh/kiss.jpg');
	  var size = Math.round(Math.random() * 300 + 100);  // kiss image size 100-400 px
	  var rotate = Math.round(Math.random() * 360);
	  var left = Math.round(Math.random() * (window.innerWidth - size));
	  var top = Math.round(Math.random() * (window.innerHeight - size));
	  img.css({
	    'width' : size,
	    'left' : left,
	    'top' : top,
	    'transform' : 'rotate(' + rotate + 'deg)'
	  });
	  img.addClass('kissimg');
	  document.body.appendChild(img[0]);
	  img.hide().fadeIn(600, function() {
	    kisstimeout = setTimeout(
		  function() { img.hide(); },
		  1000
	    );
	  });
	});
  }
};

document.addEventListener('DOMContentLoaded', keshPage.init);
