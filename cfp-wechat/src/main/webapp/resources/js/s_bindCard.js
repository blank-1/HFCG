// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"main" : ["main"],
		"swal":["lib/sweetalert2"]
	}
})
require(['jquery',"main","swal"],function($,main,swal){
	$(function(){

		//绑卡协议勾选
		$(".checked").on("click",function() {
			$(this).toggleClass("checked");
		})

		$("#bindCardBtn").on("click",function(){
			$("#loading").show();
			var cardId = $("#cardId").val();
			setTimeout(function(){
				if(cardId == "" || !/^\d{16}|\d{19}$/.test(cardId.replace(/\s/g, ""))){
                    $("#loading").hide();
					swal('请输入正确的银行卡号','','warning');
                    return;
				}
				$.ajax({
					url:rootPath+'/bankcard/check_card',
					type:"post",
					data:{
						cardNo:cardId,
					},
					success:function(data){
						if(data.result == 'success'){
							bindCard(cardId)
						}else if(data.result == 'error'){
                            $("#loading").hide();
							swal(data.errMsg,'','warning');
						}else{
                            $("#loading").hide();
							swal("网络异常，请稍后操作！",'','warning');
						}
					}
				})
            },2000);
		})

		function bindCard(bankid){
            $.ajax({
                url:rootPath+'/bankcard/llRecharge',
                type:"post",
                data:{
                    cardNo:bankid,
                    rechargeAmount:0.01
                },
                error : function(data) {
                    var data = data.responseText;
                    var _data = eval('('+data+')');
                    $("#loading").hide();
                    swal(_data.errorMsg,'','warning');
                },
                success:function(data){
                    console.log(data);
                    var _data =  eval("("+data+")");
                    if(!_data.isSuccess){
                        /*if(_data.id=='redirect'){*/
                            $("#loading").hide();
                            swal(_data.info,'','warning');
                        /*}*/
                    }else{
                        $("#loading").hide();
                        $("#req_data").val(_data.info);
                        $("#llRechargeForm").submit();
                    }
                }
            });
		}

        $("#cardId").on("input",function(){
            //formatBankNo(this);
            var str=$("#cardId").val();
            str=formatCardNo(str);
            $(".l_bindCardtext").html(str);
            $(".l_bindCardtext").slideDown();
            if(str==""){
                $(".l_bindCardtext").slideUp();
			}

        })

		$("#cardHelp").click(function(){
			$('.Maskbox').fadeIn(300);
			$('.cardHelp').slideDown(500);
		})
		$(".closeBtn").click(function(){
			$('.Maskbox').fadeOut(500);
			$('.cardHelp').slideUp(200);
		})

		function formatCardNo(str){
            //str=str.replace(/\D/g,'').replace(/....(?!$)/g,'$& ');
			var temp="";
			for(var i=1;i<=str.length;i++){
				if(i%4==0){
                    temp+=str.charAt(i-1)+" ";
				}else{
                    temp+=str.charAt(i-1);
				}
			}
			return temp;
		}
	})
})
