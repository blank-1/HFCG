<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<body>

	
<div id="msg_msgsList" class="container-fluid" style="padding: 5px 0px 0px 10px">

	<div id="msg_conditional_query" style="padding:3px;">
    	
    	<span>标题:</span>
    	<input id="message_name" style="line-height:18px;border:1px solid #ccc">
    	
    	<span>发布开始时间:</span>
    	<input id="message_start_time" class="easyui-datebox"></input>
    	<span>发布结束时间:</span>
    	<input id="message_end_time" class="easyui-datebox" ></input>
    	
    	<a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">Search</a>
    </div>
	
    <table id="msg_msgsList_list"></table>
</div>

<script language="javascript">
	/**
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#msg_msgsList_list").datagrid({
            idField: 'msgId',
            title: '消息列表',
            url: '${ctx}/jsp/message/system_list',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#admin_adminsList_toolbar',
            columns:[[
					  {field:'reciverId', width:160,hidden:true},  
                      {field:'name', width:160,title:'消息标题'},                     
                      {field:'sendTime', width:160,title:'发布日期',
                    	  formatter:function(value,row,index){
                    		  if(value != "" && value != null){
                    			  var d = new Date(value);
      	  						return d.format('yyyy-MM-dd'); 
                    		  }else {
                    			  return "--";
                    		  } 
                         	}   
                      },
                      {field:'loginName', width:160,title:'接收人用户名'},
                      {field:'status', width:100,title:'是否读取',
                    	  formatter:function(value,row,index){
  	                        if(value == '1'){
  	                        	return "是"
  	                        }
                    			return "否";	
                       	}  
                      },
                      {field:'action',title:'操作',width:160,align:'center',
                          formatter:function(value,row,index){
                               var value = '<a href="#" class="label label-info" onclick="showMsg('+index+')">详情</a> &nbsp;';
                               value += '<a class="label label-important" onclick="delMsg(' + index + ')">撤回</a>';
                               return value;
                          }
                      }
            ]]
           
       });
    }
    /**
     * 执行：条件查询
     */
    function doSearch(){
    	
    	$('#msg_msgsList_list').datagrid('load',{
    		message_name:$('#message_name').val(),
    		message_start_time: $('#message_start_time').datebox('getValue'),
    		message_end_time: $('#message_end_time').datebox('getValue')
    	});
    	
    }
   	function delMsg(index){
   		$("#msg_msgsList_list").datagrid("selectRow",index);
    	var selRow = $("#msg_msgsList_list").datagrid("getSelected");
    	if (selRow) {
    		$.messager.confirm('确认信息','是否确认删除该条消息？',function(r){
    			if(r){
    				try {
                    	$.post("${ctx}/jsp/message/to_msg_del?msgId="+selRow.msgId+"&reciverId="+selRow.reciverId,
                            	function(data){
                            		if(data == "success"){
                            			$.messager.alert("系统提示", "操作成功!", "info");
                            			$('#msg_msgsList_list').datagrid('reload');  
                            		}
                            		if(data == "error"){
                            			$.messager.alert("系统提示", "操作失败!", "info");
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
   	}
   	/**
     * 执行：弹出详情层。
     */
    function showMsg(index) {
    	$("#msg_msgsList_list").datagrid("selectRow",index);
    	var selRow = $("#msg_msgsList_list").datagrid("getSelected");
    	if (selRow) {
            $("#msg_msgsList").after("<div id='showMsg' style=' padding:10px; '></div>");
            $("#showMsg").dialog({
                resizable: false,
                title: '消息详情',
                href: '${ctx}/jsp/message/to_system_show?msgId=' + selRow.msgId,
                width: 700,
                height: 550,
                modal: true,
                top: 100,
                left: 300,
                buttons: [
                    {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#showMsg").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要查看的消息!", "info");
        }
    }
    
    // 未用！！！
    //封装查询条件到对象，重新加载数据
    function conditionSearch() {
    	var adminInfo={};
    	if($("#displayNameSearch").val() != ""){
    		adminInfo.displayName = $("#displayNameSearch").val();
    	}
    	if($("#adminCodes").val() != ""){
    		adminInfo.adminCode = $("#adminCodes").val();
    	}
    	if($("#email").val() != ""){
    		adminInfo.email = $("#email").val();
    	}
    	if($("#telPhone").val() != ""){
    		adminInfo.telPhone = $("#telPhone").val();
    	}
    	if($("#positionIds").combobox("getValue") != ""){
    		adminInfo.positionId = $("#positionIds").combobox("getValue");
    	}
    	if($("#adminStateValue").combobox("getValue") != ""){
    		adminInfo.adminState = $("#adminStateValue").combobox("getValue");
    	}
    	if($("#searchDepartment").val() != ""){
    		adminInfo.departmentCode = $("#searchDepartmentCode").val();
    	}else {
    		$("#searchDepartmentCode").val("");
    	}
    	$("#admin_adminsList_list").datagrid('load',adminInfo);
    }
    
    loadList();
</script>
</body>