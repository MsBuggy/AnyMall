$('.carousel .carousel-items').show().unslider({
	autoplay : true,
	arrows : false,
	infinite : true,
	delay : 5000
});
$(window).scroll(function() {
	if (500 < $(document).scrollTop()) {
		 $(".sidenav-right").fadeIn();
		 $(".go-top").fadeIn();
	}else{
		$(".sidenav-right").fadeOut();
		$(".go-top").fadeOut();
	}
});

$(".go-top").click(function(){
	  $('html,body').animate({scrollTop:0}, 400);
});