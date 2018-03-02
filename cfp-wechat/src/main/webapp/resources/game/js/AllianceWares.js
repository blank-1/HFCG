
//判断手机号是否正确
$("#w_subBnt").on("tap",function(){
  var phoneVal=$("#phoneNumVal").val(),
      nameVal=$("#w_nameVal").val();
      if(phoneVal==""){
        $("#w_ui_maskBox").show();
        $("#w_errorMsg").html("输入内容不能为空"); 
      }else if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(phoneVal))){
          $("#w_ui_maskBox").show();
          $("#w_errorMsg").html("请输入正确的手机号")
      }else if(phoneVal.length<11){
        $("#w_ui_maskBox").show();
          $("#w_errorMsg").html("不是完整的11位手机号")
      }else{
           $("#frm").submit();
      }
})
$("#w_sureBtn").on("tap",function(){
     $("#w_ui_maskBox").hide();
     $("#phoneNumVal").val("")
})