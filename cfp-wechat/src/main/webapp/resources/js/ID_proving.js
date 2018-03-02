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
		$("#next-btn").on("click",function(){
			var codeVal = $("#ID-number").val();
			if(codeVal == ""){
				swal(
					'身份证号码不能为空',
					'',
					'warning'
				)
			  }else if(!(/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/.test(codeVal))){
				swal(
					'请输入正确的身份证号码',
					'',
					'warning'
				)
			}else if(codeVal.length < 18 && codeVal.length > 15){
				swal(
						'不是完整的18或15位证件号码',
						'',
						'warning'
					)
			}else{//跳转路径
				var pass_type = $("#pass_type").val();
				var mobile_no = $("#mobile_no").val();
				var dtoken = $("#dtoken").val();
				$.ajax({
					url:rootPath+'/person/identityProving_authentication',
					type:"post",
					data:{
							"pass_type":pass_type,
							"mobile_no":mobile_no,
							"idcard":codeVal,
							"dtoken":dtoken
						},
					async:false,
					success:function(data){
						if(data.result == 'success'){
							$("#identityProving_Form").submit();
						}else if(data.result == 'error'){
							swal(
									data.errMsg,
									'',
									'warning'
								)
						}else{
							swal(
									'网络异常，请稍后重试！',
									'',
									'warning'
								)
						}
					},
					error: function(e) {
						swal(
								'网络异常，请稍后重试！',
								'',
								'warning'
							)
					}
				});
			}
			
		});

		$("#ID-number").on("input",function(){
			main.limitNUM(18)
		})
	})
})
