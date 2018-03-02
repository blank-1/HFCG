<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
<script type="text/javascript">
	var baseUrl = "${ctx}";	
</script>
<link type="text/css" rel="stylesheet" href="${ctx}/js/umeditor/themes/default/css/umeditor.css">
<script type="text/javascript" charset="utf-8" src="${ctx}/js/umeditor/umeditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/js/umeditor/umeditor.min.js"></script>
<script type="text/javascript" src="${ctx}/js/umeditor/lang/zh-cn/zh-cn.js"></script>

</head>
<body>
<div class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form class="form-horizontal" id="addWechatNotice_form" method="post" action="${ctx}/jsp/notice/add">
		<h3 style="margin-left: 700px;">发布微信公告</h3>
        
        <div class="control-group">
            <label class="control-label"><span style="color: red">*</span>标题：</label>

           <div class="controls">
                <input type="text" style="width: 580px;height: 30px;"
                       class="easyui-validatebox" required="true" validType="length[4,20]"
                       id="noticeTitle" name="noticeTitle" />
            </div>
        </div>
        
        <div class="control-group">
	        <label class="control-label"><span style="color: red">*</span>简介：</label>
	        
	        <div class="controls" style="margin-top: 10px;">
	            <textarea name="noticeSynopsis"  name="noticeSynopsis"
	            	autocomplete="off" class="easyui-validatebox" style="height: 60px; width: 580px;"
	            	required="true"  validType="length[10,200]"></textarea>
	        </div>
    	</div>
        
        <div class="control-group" style="margin-top: 10px;">
            <label class="control-label"><span style="color: red">*</span>内容：</label>
            
            <div class="controls" style="padding-left: 375px;">
				<script type="text/plain" id="noticeContent" name="noticeContent" style="width:580px;height:450px;"></script>                
            </div>
        </div>
        
    </form>
    <input style="margin-left: 865px;width: 95px" type="button" class="btn" value="取消" onclick="cancelButton();" >
    <input style="width: 95px" type="button" class="btn btn-success" value="发布" onclick="saveButton();" >
</div>

<script type="text/javascript">
   //实例化编辑器
   var um = UM.getEditor('noticeContent');
   
   //发布公告
   function saveButton(){
	   var form = $("#addWechatNotice_form");
       var validate = form.form('validate');
       
       if(um.getContent().length <= 0){
    	   $.messager.alert('提示', '公告内容不能为空！', 'info');
    	   return false;
       }

       if(validate){
           $.ajax({
               url:'${ctx}/jsp/notice/add',
               data:form.serialize(),
               type:"POST",
               success:function(msg){
                   if(msg=='success'){
                       $.messager.alert('提示', '保存成功！', 'info',function(){
                    	   //刷新父页面列表
	                       window.opener.$("#wechat_notice_list_list").datagrid("reload");
	                       //关闭当前页
	                       window.close();
                       });
                   }else{
                 	  $.messager.alert('提示', '保存失败！'+msg, 'info');
                   }
               }
           });
       }
   }
   
   //取消操作
   function cancelButton(){
	   window.close();
   }
</script> 

</body>
</html>