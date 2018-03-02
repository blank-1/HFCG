// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"main" :["main"],
		"swal":["lib/sweetalert2"]
	}
})
require(['jquery','main','swal'],function($,main,swal) {
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

		//
		$("#complete").on("click",function(){
		    $(this).attr("disabled",true)
		    aa();
        })
        function aa(){
            $("#loading").show();
            setTimeout(function () {
                var NameVal = $("#name-case").val(),
                    IDVal = $("#id-case").val();
                if(NameVal == "" || IDVal ==""){
                    swal(
                        '名字或身份证不能为空',
                        '',
                        'warning'
                    )
                    $("#loading").hide();
                    $("#complete").attr("disabled",false);
                    return;
                }
                if(!/^[\u4e00-\u9fa5]{2,4}$/.test(NameVal)){
                    swal(
                        '名字格式不正确',
                        '',
                        'warning'
                    )
                    $("#loading").hide();
                    $("#complete").attr("disabled",false);
                    return;
                }
                if(!/^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/.test(IDVal)){
                    swal(
                        '身份证格式不正确',
                        '',
                        'warning'
                    )
                    $("#loading").hide();
                    $("#complete").attr("disabled",false);
                    return;
                }
                $.ajax({
                    url: rootPath+"/person/identityAuthentication",    //请求的url地址
                    dataType:"json",   //返回格式为json
                    async:true,//请求是否异步，默认为异步，这也是ajax重要特性
                    type:"POST",   //请求方式
                    data:{"trueName":NameVal,"idCard":IDVal},
                    success:function(req){
                        //请求成功时处理 result
                        if(req.result == 'success'){
                            $("#loading").hide();
                            swal(
                                '认证成功',
                                '',
                                'warning'
                            )
                            location.href=rootPath+"/person/account/overview";
                        }else {
                            swal(
                                req.errMsg,
                                '',
                                'warning'
                            )
                            $("#loading").hide();
                            $("#complete").attr("disabled",false);
                            return;

                        }
                    }
                })
            },2000);

        }
	})

})
