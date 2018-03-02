<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<body>

	
<div id="msg_msgsList" class="container-fluid" style="padding: 5px 0px 0px 10px">
	
	<div id="msg_conditional_query" style="padding:3px; ">
    	<span>类型:</span>
        <select id="message_type"  class="easyui-combobox">
            <option value="">全部</option>
            <option value="1">公告</option>
            <option value="2">站内</option>
            
        </select>
        <!-- <customUI:dictionaryTable key="${1 }" id="message_type" isWholeUse="true" wholeValue="" desc="false" constantTypeCode="attachmentType"></customUI:dictionaryTable>
    	<customUI:dictionaryTable key="${1 }" desc="true" constantTypeCode="attachmentType"></customUI:dictionaryTable> -->
    	<span>标题:</span>
    	<input id="message_name" style="line-height:18px;border:1px solid #ccc">
    	
    	<span>发布开始时间:</span>
    	<input id="message_start_time" class="easyui-datebox"></input>
    	<span>发布结束时间:</span>
    	<input id="message_end_time" class="easyui-datebox" ></input>
    	
    	<a href="#" class="easyui-linkbutton" plain="true" onclick="doSearch()">Search</a>
    </div>
    <div style="text-align:right; padding:0px; margin-bottom: 5px;margin-top: 0px; margin-right: 50px;">
    	<a href="#" class="easyui-linkbutton" onclick="addStation()">发送站内信</a>&nbsp;&nbsp;
    	<a href="#" class="easyui-linkbutton" onclick="addNotice()">发送公告</a>
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
            url: '${ctx}/jsp/message/station_notice_list',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.77,
            singleSelect: true,
            rownumbers: true,
            columns:[[
                      {field:'type', width:100,title:'消息类型',
                    	  formatter:function(value,row,index){
  	                        if(value == "0"){
  	                        	return "系统消息";
  	                        }else if(value == "1"){
  	                        	return "公告";
  	                        }else if(value == "2"){
  	                        	return "站内消息";
  	                        }	
                       	}
                      },
                      {field:'name', width:160,title:'消息标题'},
                      {field:'sendTime', width:160,title:'发布日期',
                    	  formatter:function(value,row,index){
                    		  if(value != "" && value != null){
                    			  var d = new Date(value);
      	  						return d.format('yyyy-MM-dd'); 
                    		  }else {
                    			  return "--"
                    		  } 
                         	}   
                      },
                      {field:'endTime', width:160,title:'有效日期',
                    	  formatter:function(value,row,index){
                    		  if(value != "" && value != null){
                    			  var d = new Date(value);
      	  						return d.format('yyyy-MM-dd'); 
                    		  }else {
                    			  return "--"
                    		  }
                       	}  
                      },
                      {field:'reciverType', width:160,title:'发送至',
                    	  formatter:function(value,row,index){
    	                        if(value == "0"){
    	                        	return "全部";
    	                        }else if(value == "1"){
    	                        	return "借款";
    	                        }else if(value == "2"){
    	                        	return "出借";
    	                        }else if(value == "3"){
    	                        	return "系统/导入用户";
    	                        } 	
                         	}   
                      },
                      {field:'senderName', width:100,title:'发送人员名称'},
                      {field:'topSign', width:100,title:'是否置顶',
                    	  formatter:function(value,row,index)
                    	  {
               		 		if(value == "1"){
               		 			return "否";
               		 		}else if (value == "0"){
               		 			return "是";
               		 		}else {
               		 			return "--";
               		 		}
                    	  }
                      },
                      {field:'num', width:100,title:'读取率',
                    	  formatter:function(value,row,index){
  	                        return "---";	
                       	}  
                      },
                      {field:'action',title:'操作',width:160,align:'center',
                          formatter:function(value,row,index){
                               var value = '<a href="#" class="label label-info" onclick="showMsg('+index+')">详情</a> &nbsp;';
                               value += '<a class="label label-important" onclick="delMsg(' + index + ')">撤回</a> &nbsp;';
                               if(row.topSign == 0){
                            	   value += '<a class="label label-important" onclick="topSign(' + index + ')">取消置顶</a> &nbsp;';
                               }else if(row.topSign == 1){
                            	   value += '<a class="label label-important" onclick="topSign(' + index + ')">置顶</a> &nbsp;';
                               }
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
    		message_type:$('#message_type').combobox('getValue'),
    		message_start_time: $('#message_start_time').datebox('getValue'),
    		message_end_time: $('#message_end_time').datebox('getValue')
    	});
    }
function topSign(index){
	$("#msg_msgsList_list").datagrid("selectRow",index);
	var selRow = $("#msg_msgsList_list").datagrid("getSelected");
	if (selRow) {
		$.messager.confirm('确认信息','是否确认要修改置顶状态？',function(r){
			if(r){
				try {
	            	$.post("${ctx}/jsp/message/topSign?msgId="+selRow.msgId+"&topSign="+selRow.topSign,
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
	                alert("删除失败！"+e);
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
                href: '${ctx}/jsp/message/to_notice_station_show?msgId=' + selRow.msgId,
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
    /**
    *删除
    */
	function delMsg(index){
   		$("#msg_msgsList_list").datagrid("selectRow",index);
    	var selRow = $("#msg_msgsList_list").datagrid("getSelected");
    	if (selRow) {
    		$.messager.confirm('确认信息','是否确认删除该条消息？',function(r){
    			if(r){
    				try {
                    	$.post("${ctx}/jsp/message/to_msg_del?msgId="+selRow.msgId,
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
                        alert("删除失败！"+e);
                        return;
                    }
    			}
    		});
    	}
   	}
	/**
     * 执行：添加公告
     */
	 function addNotice() {
	        $("#msg_msgsList_list").after("<div id='notice_msg_add' style=' padding:10px; '></div>");
	        $("#notice_msg_add").dialog({
	            resizable: false,
	            title: '发送公告',
	            href: '${ctx}/jsp/message/to_notice_add',
	            width: 900,
	            height: 550,
	            modal: true,
	            top: 100,
	            left: 200,
	            buttons: [
	                {
	                    text: '提交',
	                    iconCls: 'icon-ok',
	                    handler: function () {
	                        $("#notice_msg_add").contents().find("#notice_msg_add_form").submit();
	                    }
	                },
	                {
	                    text: '取消',
	                    iconCls: 'icon-cancel',
	                    handler: function () {
	                        $("#notice_msg_add").dialog('close');
	                    }
	                }
	            ],
	            onClose: function () {
	                $(this).dialog('destroy');
	            }
	        });
	    }
	 /**
	     * 执行：添加站内信
	     */
	 function addStation() {
	        $("#msg_msgsList_list").after("<div id='station_msg_add' style=' padding:10px; '></div>");
	        $("#station_msg_add").dialog({
	            resizable: false,
	            title: '发送站内信',
	            href: '${ctx}/jsp/message/to_station_add',
	            width: 900,
	            height: 550,
	            modal: true,
	            top: 100,
	            left: 200,
	            buttons: [
	                {
	                    text: '提交',
	                    iconCls: 'icon-ok',
	                    handler: function () {
	                        $("#station_msg_add").contents().find("#station_msg_add_form").submit();
	                    }
	                },
	                {
	                    text: '取消',
	                    iconCls: 'icon-cancel',
	                    handler: function () {
	                        $("#station_msg_add").dialog('close');
	                    }
	                }
	            ],
	            onClose: function () {
	                $(this).dialog('destroy');
	            }
	        });
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