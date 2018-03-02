$(function() {
		var overscroll = function(el) {
			el.addEventListener('touchstart', function() {
				var top = el.scrollTop
					, totalScroll = el.scrollHeight
					, currentScroll = top + el.offsetHeight
				//If we're at the top or the bottom of the containers
				//scroll, push up or down one pixel.
				//
				//this prevents the scroll from "passing through" to
				//the body.
				if(top === 0) {
					el.scrollTop = 1
				} else if(currentScroll === totalScroll) {
					el.scrollTop = top - 1
				}
			})
			el.addEventListener('touchmove', function(evt) {
				//if the content is actually scrollable, i.e. the content is long enough
				//that scrolling can occur
				if(el.offsetHeight < el.scrollHeight)
					evt._isScroller = true
			})
		}
	overscroll(document.querySelector('.l_NewScroll'));
	document.body.addEventListener('touchmove', function(evt) {
		//In this case, the default behavior is scrolling the body, which
		//would result in an overflow.  Since we don't want that, we preventDefault.
		if(!evt._isScroller) {
			evt.preventDefault()
		}
		})
		var swiper = new Swiper('.swiper-container', {
				pagination : '.swiper-pagination',
				paginationClickable : true,
				autoplay : 3000,
				loop : true
		});

		// 撸喵动画效果

		$(".content").on("touchstart", function(e) {
						e.stopPropagation();
							$(".content").stop().animate();
							// console.log(startY +"==="+scrollStart);
				});
		$(".content").on("touchmove",function(e){
					e.stopPropagation();
					$(".content").stop().animate();
				})
		$(".content").on("touchend ",function(){
            var imgHeight = $(".l_catBox").outerHeight(),
                imgHead = Math.floor(imgHeight*0.38),
                winH= $("body").outerHeight(),
                boxH = $("#groom").outerHeight() + $("header").outerHeight()+$(".l_banner2nd").outerHeight(true);
			if (($(this).scrollTop()+winH-boxH)>(imgHead)) {
					$(this).animate({scrollTop:boxH+imgHead-winH}, 800);
			}
		})
		$(".content").on("scroll ",function(){
            var imgHeight = $(".l_catBox").outerHeight(),
                imgHead = Math.floor(imgHeight*0.38),
                winH= $("body").outerHeight(),
                boxH = $("#groom").outerHeight() + $("header").outerHeight()+$(".l_banner2nd").outerHeight(true);
	if (($(this).scrollTop()+winH+1)==(boxH+imgHeight)) {
					$(this).animate({scrollTop:boxH+imgHead-winH}, 800);
			}
		})
	var lis=$(".scroller").find("li[dataType='amount']");
		for(var i=0;i<lis.length;i++){
			if(i>0){
				var div=$(lis[i]).find("ol[class='list']").find("li:last").find("div");
				var radialObj = $(div).radialIndicator({
					   barWidth : 10,
					   barColor : "#FF5E61",
					   barBgColor :"#efefef",
					   displayNumber: false
				}).data('radialIndicator');
				var rate=$(lis[i]).find("input[name='ratePercent']").val();
				radialObj.animate(Math.floor(rate));
			}
		}
		$(".l_mask").fadeIn().children("section").addClass("l_in").children("#close").on("click",function () {
			$(".l_in").removeClass("l_in");
			$(".l_mask").fadeOut();
		}).siblings("#link").on("click",function () {

		});
})

