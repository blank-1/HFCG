<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<%@include file="../../../common/common.jsp" %>
  </head>
  
  <body>
  
       <!-- 修改密码开始 -->
       <div id="div_pass_edit" style="padding: 5px 0px 0px 10px;">
			<div class="easyui-panel" title="密码修改" style="width:600px;" >
				<div style="padding:20px 0 20px 160px">
					<span id="span_pass_info" style="color: red;"></span>
					<table style="font-size: 12px;">
						<tr>
							<td>原始密码:</td>
							<td><input id="pass_edit_oldpass" type="password" /></td>
						</tr>
						<tr>
							<td>修改密码:</td>
							<td><input id="pass_edit_newpass" type="password" /></td>
						</tr>
						<tr>
							<td>重新输入:</td>
							<td><input id="pass_edit_renewpass" type="password" /></td>
						</tr>
					</table>
				</div>
				<div style="text-align:center;padding:5px">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="savePass()">保存</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="resetPass()">重置</a>
				</div>
			</div>
		</div>
       <!-- 修改密码结束 -->
       
       <script type="text/javascript">
		    // 保存新密码。
		   	function savePass(){
		   		var oldpass = $('#pass_edit_oldpass').val();
		   		var newpass = $('#pass_edit_newpass').val();
		   		var renewpass = $('#pass_edit_renewpass').val();
		   		
		   		if(null == oldpass || "" == oldpass){
		   			$('#span_pass_info').html('提示：原始密码不能为空');
		   			return;
		   		}else if(null == newpass || "" == newpass){
		   			$('#span_pass_info').html('提示：修改密码不能为空');
		   			return;
		   		}else if(newpass != renewpass){
		   			$('#span_pass_info').html('提示：两次输入新密码不一致');
		   			return;
		   		}
		   		
		   		$.post(
		   				"${ctx}/jsp/system/admin/editPass", 
		   				{ 
		   					"login_old": oldpass,
		   					"login_new": newpass
		   				},
		   				function(data){
		   				   if(null != data && "" != data){
		   					   
		   					   if(data.result == 'success'){
		   						   
		   						   $.messager.alert('提示', '密码修改成功，为了您的账号安全，请重新登录！','info',function(){
                                       window.location.href="index";
                                   });
		   						   
		   					   }else if(data.result == 'error'){
		   						   $.messager.alert('提示', data.errMsg,'info');
		   					   }
		   					   
		   				   }else{
		   					   $.messager.alert('提示', '密码保存失败！','info');
		   				   }
		   				}
		   			);
		   	}
		    
		    // 重置信息。
		    function resetPass(){
		    	$('#pass_edit_oldpass').attr('value','');
		   		$('#pass_edit_newpass').attr('value','');
		   		$('#pass_edit_renewpass').attr('value','');
		   		$('#span_pass_info').html('');
		    }
       </script>
  </body>
</html>
