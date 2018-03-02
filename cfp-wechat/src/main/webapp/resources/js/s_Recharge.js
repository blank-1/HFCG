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
		$("#nextBtn").on("click",function(){
			$("#loading").show();
			var moneyVal = $("#moneyInput").val();
			var cardNumber = $("#cardNumber").val().replace(/\s/g, "");
			setTimeout(function(){
				if ($(".bodyBj").hasClass("l_noCard")) {
					if(moneyVal == ""){
                        $("#loading").hide();
						swal('充值金额不能为空','','warning');
						return false;
					}
					if(!/^([0-9.]+)$/.test(moneyVal)){
                        $("#loading").hide();
						swal('请输入正确的充值金额','','warning');
						return false;
					}
					if(!/^([0-9]+)$/.test(cardNumber)){
                        $("#loading").hide();
						swal('请输入正确的银行卡号','','warning');
						return false;
					}
					if(moneyVal<100){
                        $("#loading").hide();
						swal('请输入大于100元的金额','','warning');
						return false;
					}
                    bankf($("#cardNumber").val());
				}else {
					if(moneyVal == ""){
                        $("#loading").hide();
						swal('充值金额不能为空','','warning');
                        return false;
					}else if(moneyVal ==""|| !/^([0-9.]+)$/.test(moneyVal)){
                        $("#loading").hide();
						swal('请输入正确的充值金额','','warning');
                        return false;
					}else if(moneyVal<100){
                        $("#loading").hide();
						swal('请输入大于100元的金额','','warning');
                        return false;
					}
				}
                $.ajax({
                    url:rootPath+"/recharge/fuuiRecharge",
                    type:"post",
                    data:$("#rechargeForm").serialize(),
                    dataType:'json',
                    error : function(data) {
                        console.log("error");
                        console.log(data);
                        var _data = data.responseText;
                        $("#loading").hide();
                        swal(_data.errorMsg,'','warning');
                    },
                    success:function(_data){
                        console.log(_data);
                        $("#loading").hide();
                        if(!_data.success){
                            //$("#loading").hide();
                            if(_data.id=='redirect'){
                                swal(_data.info,'','warning');
                            }else{
                                //校验错误
                                if(typeof(_data.tocken)!="undefined"){
                                    $("#token").val(_data.tocken);
                                }
                                if(_data.id=='valid' || _data.id=='cardNo' || _data.id=='bankid'){
                                    swal(_data.info,'','warning');
                                }else{
                                    swal(_data.info,'','warning');
                                }
                            }
                        }else{
                            $('#FM').attr('action',_data.params.actionUrl);
                            $('#FM [name="mchnt_cd"]').val(_data.params.mchnt_cd);
                            $('#FM [name="mchnt_txn_ssn"]').val(_data.params.mchnt_txn_ssn);
                            $('#FM [name="login_id"]').val(_data.params.login_id);
                            $('#FM [name="amt"]').val(_data.params.amt);
                            $('#FM [name="page_notify_url"]').val(_data.params.page_notify_url);
                            $('#FM [name="back_notify_url"]').val(_data.params.back_notify_url);
                            $('#FM [name="signature"]').val(_data.params.signature);
                            $('#FM').submit();
                        }
                    }
                });
            },2000);
		})

		$("#cardNumber").on("blur",function(){
        	bankf($("#cardNumber").val());
		})

        //判断银行卡号是否正确
        function bankf(bankv){
            var massage="";
            if(bankv=="" ){
                swal('请您输入银行卡号','','warning')
            }else{
                if(bankv.replace(/\s/g, "").length<=19 && bankv.replace(/\s/g, "").length>=15){
                    $.ajax({
                        url:rootPath+'/bankcard/check_card',
                        type:"post",
                        async:false,
                        data:{
                            cardNo:bankv
                        },
                        success:function(data){
                            if(data.result == 'error'){
                                swal(data.errMsg,'','warning');
                            }else if(data.result != 'success'){
                                swal("网络异常，请稍后操作！",'','warning');
                            }
                        }
                    });
                }else{
                    $("#loading").hide();
                    swal("银行卡号格式错误！",'','warning');
                }

            }
        }
	})
})
