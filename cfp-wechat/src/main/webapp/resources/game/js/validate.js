//表单验证

$("#w-collar").on("click",function(){
	var phoneVal=$("#w-phoneNum").val();
	 if(phoneVal==""){
	 	$("#errorMsg").show().html("输入内容不能为空"); 
	 }else if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(phoneVal))){
        $("#errorMsg").show().html("请输入正确的手机号").css({
        'color':"#f00"
       });;
    }else if(phoneVal.length<11){
        $("#errorMsg").show().html("不是完整的11位手机号").css({
        'color':"#f00"
       });
     }else{
	   $("#errorMsg").show().html("输入正确,正在跳转中…").css({
		'color':"#56f18d"
	   })
	   $("#frm").submit();
    }
})