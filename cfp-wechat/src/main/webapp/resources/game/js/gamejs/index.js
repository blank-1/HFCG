// JavaScript Document
$(function() {
	var overscroll = function(el) {//阻止浏览量默认滚动
		  el.addEventListener('touchstart', function() {
		    var top = el.scrollTop
		      , totalScroll = el.scrollHeight
		      , currentScroll = top + el.offsetHeight
		    if(top === 0) {
		      el.scrollTop = 1
		    } else if(currentScroll === totalScroll) {
		      el.scrollTop = top - 1
		    }
		  })
	  el.addEventListener('touchmove', function(evt) {
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
	$("#btn").addClass("animate01").on("touchend",function (e) {
		e.stopPropagation();
		e.preventDefault();
		 window.location.href=rootPath+"/game/yearBillData";
	})
	$("#audioCtl").on("touchend",function(e) {
		e.stopPropagation();
		e.preventDefault();
		$(this).toggleClass("audioCtl2");
		if ($(this).hasClass("audioCtl2")) {
			$("#media")[0].pause();
		}else {
			$("#media")[0].play();
		}
	})

})
