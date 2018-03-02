// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"main" : ["main"],
		"swal":["lib/sweetalert2"]
	}
})
require(['jquery',"main","swal"],function($,main,swal) {
	$(function() {
		var overscroll = function(el) {//阻止浏览量默认滚动
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

		// 倒计时
		
		$("#checkCodeB").on("click",function(){

			var phoneVal = $("#phoneNumber").val();
			if(phoneVal == ""){
				swal(
					'手机号不能为空',
					'',
					'warning'
				)
			}else if(!(/^1[3|4|5|8|7][0-9]\d{4,8}$/.test(phoneVal))){
				swal(
					'请输入正确的手机格式',
					'',
					'warning'
				)
			}else{
				main.countDown()
			}
			
		});


		$("#next-btn").on("click",function(){
			var codeVal = $("#Veri-in").val();
			if(codeVal == ""){
				swal(
					'验证码不能为空',
					'',
					'warning'
				)
			}else{//跳转路径

			}
			
		})

		$("#phoneNumber").on("input",function(){
			main.limitNUM(11);
		});
		$("#Veri-in").on("input",function(){
			main.limitNUM(6);
		})
	})
})
