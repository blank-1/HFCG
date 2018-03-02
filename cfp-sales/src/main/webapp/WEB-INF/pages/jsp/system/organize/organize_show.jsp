<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp"%>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="function_showFunction_form" method="post" style="padding: 3px 0px 0px 10px">
    
    <div class="control-group">
    	<label class="control-label" id="showParentNode" style="width:auto;"><b id="parentNodeName"></b> >> <b id="childNodeName"></b></label>
    </div>
    
    <div class="control-group">
        <label class="control-label">组织机构编码</label>
        <div class="controls-text" id="functionName" style="width:500px;">${functionInfo.organizeCode}&nbsp;</div>
    </div>
      
    <div class="control-group">
        <label class="control-label">组织机构名称</label>
        <div class="controls-text" id="functionDesc" style="width:500px;">${functionInfo.organizeName}&nbsp;</div>
    </div>
      
	<div class="control-group">
		<label class="control-label">相关属性</label>
		<div class="controls-text" id="functionCode" style="width:500px;">&nbsp;</div>
	</div>

	<div class="control-group">
		<label class="control-label">描述</label>
		<div class="controls-text" id="functionCode" style="width:500px;">${functionInfo.description}&nbsp;</div>
	</div>

    
    <div id="function_functionsList_toolbar" style="height:auto;padding:3px 111px">
	    <a href="javascript:void(0);" class="btn btn-primary"
							iconCls="icon-edit" plain="true" onclick="editOrganize();">&nbsp;&nbsp;修改&nbsp;&nbsp;</a>
	</div>
</form>

<script language="JAVASCRIPT" >
	
	function init(){
		//显示当前标签的父级标签
		
		var childName = '${childName}';
		var parentName = '${parentName}';
		if(childName != null){
			$("#childNodeName").html(childName);
		}
		if(parentName != null){
			$("#parentNodeName").html(parentName);
		}
		
		//显示权限对应Url

	}
	
	function editOrganize() {
		getEditingInfo();
		//判断是否选中节点 
		if(editingId==-1){
			alert("请先选择组织节点");
			return;
		}
		
		$("#showOrganize").tabs('update', {
			tab: $("#showOrganize").tabs('getTab',"组织机构信息"),
			options: {
				href: "${ctx}/jsp/system/organize/editOrganize?editingId="+editingId
			}
		});
		$("#showOrganize").tabs('select',"组织机构信息");
	}
	
	
	// 暂时未用！！！
	function deleteFunction() {
		getEditingInfo();
		//判断是否选中节点 
		if(editingId==-1){
			alert("请先选择权限节点");
			return;
		}
		
		$.messager.confirm('确认信息','是否确认删除该权限？',function(r){
			if(r){
				try {
                	$.post("${path}/jsp/function/deleteFunction.do?editingId="+editingId,
                        	function(data){
                        		if(data == "success"){
                        			$.messager.alert("系统提示", "操作成功!", "info");
                        			parent.$("#showFunctionTree").treegrid("reload");//刷新树结构
                             	    parent.$("#showFunctionTree").treegrid({
                         				onLoadSuccess : function() {
                         	            	   parent.$("#showFunctionTree").treegrid("select",editingPid);
                         	            	   parent.$("#showFunction").tabs('update', {
                         		           			tab: $("#showFunction").tabs('getTab',"权限信息"),
                         		           			options: {
                         		           				href: "${ctx}/jsp/system/function/to_function_show?functionId="+editingPid
                         		           			}
                         		           	   });
                         		           	   parent.$("#showFunction").tabs('select',"权限信息");
                         				}
                         		    });
                        		}
                        		if(data == "error"){
                        			$.messager.alert("系统提示", "操作失败!", "info");
                        			return;
                        		}
                        		if(data == "used"){
                        			$.messager.alert("系统提示", "该权限被角色关联，不可以删除!", "info");
                        			return;
                        		}
                	  		});
                } catch (e) {
                    alert("删除失败！");
                    return;
                }
			}
		});
	}
	
	init();
</script>
</body>
</html>