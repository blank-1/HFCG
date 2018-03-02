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
		
		//隐藏显示密码切换
		var flag = false;
		$("#eye").on("click",function(){
			if(flag){
                $("#pwd-number").prop("type","password");
				$(this).addClass("eye-close");
                flag = false;
			}else{
                $("#pwd-number").prop("type","text");
				$(this).removeClass("eye-close");
                flag = true;
			}
		})
		
		$("#next-btn").on("click",function(){
			var codeVal = $("#pwd-number").val();
			if(codeVal == ""){
				swal(
					'密码不能为空',
					'',
					'warning'
				)
			}else if( codeVal.length > 16 || codeVal.length < 4){
				swal(
					'请输入正确的密码位数',
					'',
					'warning'
				)
			}	else if((/[^A-Za-z0-9]/g.test($(this).val()))){
				swal(
						'您设置的密码含有非法字符',
						'',
						'warning'
					)
			}
			else{//跳转路径
		
				//【server】保存新密码【开始】
				var newPass = $("#passWord").val();
				var pass_type = $("#pass_type").val();
				var mobile_no = $("#mobile_no").val();
				var dtoken = $("#dtoken").val();
				$.ajax({
					url:rootPath+'/person/installNewPassword_savePass',
					type:"post",
					data:{
							"pass_type":pass_type,
							"mobile_no":mobile_no,
							"newPass":codeVal,
							"dtoken":dtoken
						},
					async:false,
					success:function(data){
						if(data.result == 'success'){
							swal(
									'保存密码成功',
									'',
									'success'
								)
								$("#installNewPassword_Form").submit();
							 
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
				//【server】保存新密码【结束】
				
				
			}
			
		})

		$("#pwd-number").on("input",function(){
			main.limitNUM(16);
		})
	})
})
