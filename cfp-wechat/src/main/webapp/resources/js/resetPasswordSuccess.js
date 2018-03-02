// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
        "swal":["lib/sweetalert2"]
	}
})
require(['jquery','swal'],function($,swal) {
	$(function() {
		let overscroll = function(el) {//阻止浏览量默认滚动
		  el.addEventListener('touchstart', function() {
		    let top = el.scrollTop
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

        var timing = 4,
            timer;    
        let Verification = {
            _init(){//自定义函数 
                
               Verification.autoSkip();
            },
            autoSkip(){
                var autoSkipBtn = $("#autoSkip");
                timing--;
                autoSkipBtn.html(timing);
                timer = setTimeout(Verification.autoSkip, 1000);             
                if(timing == 0){
                    clearTimeout(timer);
                    autoSkipBtn.html("0");

                    window.location.href="/person/moreInformation"//跳转地址
                        //倒计时结束
                }
            }
               
        }
        Verification._init()

	})

})
