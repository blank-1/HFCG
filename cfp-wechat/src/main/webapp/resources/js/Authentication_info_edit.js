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

		//清空手机号
		//
		$("#remove-btn").on("click",function(){
			$("#name").val("");
		});

		$("#checkCodeB").on("click",function(){
            var phoneVal = $("#mobile_dis").val();
            if(phoneVal==""){
                phoneVal = $("#name").val();
			}else{
                phoneVal = $("#mobile_no").val();
			}
			if(phoneVal == ""){
				swal(
					'手机号不能为空',
					'',
					'warning'
				)
			}else if(!(/^1[34578]\d{9}$/.test(phoneVal))){
				swal(
					'请输入正确的手机格式',
					'',
					'warning'
				)
			}else{
				/*********未登录情况下修改密码的 王亚东***********/
			 				$.ajax({
					url:rootPath+'/person/forgetPswFinal_sendMsg',
					type:"post",
					data:{
							"telNum":phoneVal,
							"pass_type":"nologin"
						},
					async:false,
					success:function(data){
						if(data.result == 'success'){
							main.countDown();//倒计时读秒
						}else if(data.result == 'error'){
							swal({
								  title:data.errMsg,
								  type:'warning'
								})
						}else{
							swal({
								  title:"网络异常，请稍后重试！",
								  type:'warning'
								})
							
						}
					},
					error: function(e) {
						swal({
							  title:"网络异常，请稍后重试！",
							  type:'warning'
							})
					}
				});
			
			}
			
		});


		$("#complete").on("click",function(){
            var nameVal = $("#mobile_dis").val();
            if(nameVal==""){
                nameVal = $("#name").val();
            }else{
                nameVal = $("#mobile_no").val();
            }
			var	phoneVal = $("#Veri-in").val();
			if(nameVal == "" || phoneVal == ""){
				swal({
				  text: '手机号或者验证码不能为空',
  				  timer: 2000,
  				  type:'error'
				})
			}else{
				$("#loading").show();
				setTimeout(function(){
                    $.ajax({
                        url:rootPath+'/person/forgetPswFinal_checkMsg',
                        type:"post",
                        data:{
                            "pass_type":"nologin",
                            "telNum":nameVal,
                            "checkCode":phoneVal
                        },
                        async:false,
                        success:function(data){
                            $("#loading").hide();
                            if(data.result == 'success'){
                                swal({
                                    title:'成功',
                                    timer: 2000,
                                    type:'success'
                                })
                                $("#dtoken").attr("value",data.data);
                                $("#mobile_no").attr("value",nameVal);
                                $("#forgetPswFinal_Form").submit();

                            }else if(data.result == 'error'){
                                swal({
                                    title:data.errMsg,
                                    type:'warning'
                                })
                            }else{
                                swal({
                                    title:"网络异常，请稍后重试！",
                                    type:'warning'
                                })
                            }
                        },
                        error: function(e) {
                            $("#loading").hide();
                            swal({
                                title:"网络异常，请稍后重试！",
                                type:'warning'
                            })
                        }
                    });
				},1500);
			}
		})
		//
		$("#Veri-in").on("input",function(){
			main.limitNUM(6);
		})

        $("#name").on("input",function(){
            main.limitNUM(11);
        })
	})
})
