var initial=1;
if(initial==0){            //等于0是未验证
	$("#validate").html("未验证").addClass("Novalidate");
	$("#identity span").html("");
	$("#phoneNumber span").html("");
	$("#userName span").html("");
	$("#govalidate").show();
}else{                     //其他的是已经验证了
	$("#validate").html("已验证").addClass("validate");
	$("#govalidate").hide();
}