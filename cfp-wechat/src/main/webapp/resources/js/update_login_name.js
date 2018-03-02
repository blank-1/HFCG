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

		$("#remove-btn").on("click",function(){
			$("#name").val("");
		});


		$("#complete").on("click",function(){
			var nameVal = $("#name").val();
			if(nameVal == ""){
				swal({
				  text: '登录名不能为空',
  				  timer: 2000,
  				  type:'error'
				});
				return;
			}
            var	reg = /[^a-zA-Z0-9\u4E00-\u9FA5\_-]/;
            if(nameVal.length<4 || nameVal.length>=20 || reg.test(nameVal)){
                swal('请输入4 - 20位字符，支持汉字、字母、数字及 "-"、"_"组合','','warning');
                return;
            }
			$("#loading").show();
			$.ajax({
				url:rootPath+'/person/updateLoginName',
				type:"post",
				data:{
						"loginName":nameVal
					},
				success:function(data){
					setTimeout(function(){
						$("#loading").hide();
						var _data =  eval("("+data+")");
						if(_data.isSuccess){
							swal('成功','','success');
							location.href=rootPath+"/person/singlePersonInformation";
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
		})

		$("#Veri-in").on("input",function(){
			main.limitNUM(6);
		})
	})
})
