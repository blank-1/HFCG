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
		  if(!evt._isScroller) {
		    evt.preventDefault()
		  }
		})

		//清空手机号
		//
		$("#remove-btn").on("click",function(){
			$("#name").val("");
		});

		$("#checkCodeB").on("click",function(){
			var phoneVal = $("#name").val();
			if(phoneVal == ""){
				swal('手机号不能为空','','warning');
			}else if(!(/^1[34578]\d{9}$/.test(phoneVal))){
				swal('请输入正确的手机格式','','warning');
			}else{
			 	$.ajax({
					url:rootPath+'/person/updateMobileMsgSend',
					type:"post",
					data:{
							"mobileNo":phoneVal
						},
					async:false,
					success:function(data){
                        var _data =  eval("("+data+")");
						if(_data.isSuccess){
							main.countDown();//倒计时读秒
						}else{
                            swal(_data.info,'','warning');
						}
					},
					error: function(e) {
						swal("网络异常，请稍后重试！",'','warning');
					}
				});
			
			}
			
		});


		$("#complete").on("click",function(){
			var nameVal = $("#name").val(),
				phoneVal = $("#Veri-in").val();
			if(nameVal == "" || phoneVal == ""){
				swal({
				  text: '手机号或者验证码不能为空',
  				  timer: 2000,
  				  type:'error'
				})
			}else{
				$("#loading").show();
				$.ajax({
					url:rootPath+'/person/updateMobile',
					type:"post",
					data:{
							"phone":nameVal,
							"valid":phoneVal
						},
					success:function(data){
						setTimeout(function(){
                            $("#loading").hide();
                            var _data =  eval("("+data+")");
                            if(_data.isSuccess){
                                swal('成功','','success');
                                location.href=rootPath+"/person/personInformationVerified";
                            }else{
                                swal(_data.info,'','warning');
                            }
						},1000);

					},
					error: function(e) {
                        $("#loading").hide();
						swal("网络异常，请稍后重试！",'','warning');
					}
				});
			}
		})

		$("#Veri-in").on("input",function(){
			main.limitNUM(6);
		})
	})
})
