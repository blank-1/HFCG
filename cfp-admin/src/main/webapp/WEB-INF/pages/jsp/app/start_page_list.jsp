<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<body>
<div id="AppStartPageList" class="container-fluid" style="padding: 5px 0px 0px 10px;">
    <table id="AppStartPageList_list"></table>
</div>

<script language="javascript">
	/**
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#AppStartPageList_list").datagrid({
            idField: 'appStartPageId',
            title: 'APP启动页列表',
            url: '${ctx}/jsp/app/start_page_list',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            columns:[[
                      {field:'appStartPageId',checkbox:true},
                      {field:'appType', width:100,title:'APP类型',align:'center'},
                      {field:'pageTitle', width:160,title:'标题'},
                      {field:'picUrl', width:160,title:'图片请求路径'},
                      {field:'picPath', width:160,title:'图片物理路径'},
                      {field:'updateTime', width:140,title:'最后更改时间'},
                      {field:'status', width:80,title:'状态',align:'center'},
                      {field:'adminDisplayName', width:80,title:'操作人',align:'center'},
                      {field:'action',title:'操作',width:160,align:'center',
                          formatter:function(value,row,index){
                               var result = "<a class='label label-info' onclick='showStartPage(" + index + ")'>详情</a>&nbsp;";
                               result += "<a class='label label-success' onclick='editStartPage(" + index + ")'>修改</a>&nbsp;";
                               if(row.status == 1){
                         			result += "<a class='label' onclick='doState(" + row.appStartPageId + ",0)'>禁用</a>&nbsp;";	
                         		}else{
                         			result += "<a class='label label-important' onclick='doState(" + row.appStartPageId + ",1)'>启用</a>&nbsp;";	
                         		}
                               return result;
                          }
                      }
            ]],
           	onBeforeLoad: function (value, rec) {
           		//APP类型
           		var appType = $(this).datagrid("getColumnOption", "appType");
                if (appType) {
                	appType.formatter = function (value, rowData, rowIndex) {
                        if (value == 2) {
                            return "<font style='color: black;'>IOS</font>";
                        } else if(value == 3) {
                            return "<font style='color: black;'>ANDROID</font>";
                        }
                    }
                }
                //最后更改时间
                var updateTime = $(this).datagrid("getColumnOption", "updateTime");
                if (updateTime) {
                	updateTime.formatter = function (value, rowData, rowIndex) {
                		if(value != null && value != ""){
                			return getDateTimeStr(new Date(value));
                		}
                    }
                }
           		//状态
           		var status = $(this).datagrid("getColumnOption", "status");
                if (status) {
             	   status.formatter = function (value, rowData, rowIndex) {
                        if (value == 1) {
                            return "<font style='color: green;'>正常</font>";
                        } else if(value == 0) {
                            return "<font style='color: red;'>禁用</font>";
                        }
                    }
                }
           }
       });
    }
    
    /**
     * 执行：弹出编辑层。
     */
    function editStartPage(index) {
    	$("#AppStartPageList_list").datagrid("selectRow",index);
        var selRow = $("#AppStartPageList_list").datagrid("getSelected");
        if (selRow) {
            $("#AppStartPageList").after("<div id='editStartPage' style=' padding:10px; '></div>");
            $("#editStartPage").dialog({
                resizable: false,
                title: 'APP启动页信息',
                href: '${ctx}/jsp/app/to_start_page_edit?appStartPageId=' + selRow.appStartPageId,
                width: 500,
                height: 300,
                modal: true,
                top: 100,
                left: document.body.clientHeight * 0.5,
                buttons: [
                    {
                        text: '提交',
                        iconCls: 'icon-ok',
                        handler: function () {
                        	$.messager.confirm('确认信息','确认提交信息？',function(r){
                    			if(r){
		                            $("#editStartPage").contents().find("#editStartPage_form").submit();
                    			}
                        	});
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#editStartPage").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要修改的数据!", "error");
        }
    }
    
    /**
     * 执行：弹出详情层。
     */
    function showStartPage(index) {
    	$("#AppStartPageList_list").datagrid("selectRow",index);
    	var selRow = $("#AppStartPageList_list").datagrid("getSelected");
    	if (selRow) {
            $("#AppStartPageList").after("<div id='showStartPage' style=' padding:10px; '></div>");
            $("#showStartPage").dialog({
                resizable: false,
                title: 'APP启动页信息',
                href: '${ctx}/jsp/app/to_start_page_show?appStartPageId=' + selRow.appStartPageId,
                width: 800,
                height: 500,
                modal: true,
                top: 100,
                left: 300,
                buttons: [
                    {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#showStartPage").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要查看的数据!", "error");
        }
    }
    
	/**
     * 执行：启用、禁用。
     */
	function doState(appStartPageId, status){
		var msg = "确定要禁用吗？";
		if(status == 1){
			msg = "确定要启用吗？";
		}
	    $.messager.confirm("操作提示", msg, function (r) {
	        if (r) {
				$.post('${ctx}/jsp/app/doState',
	               {
						appStartPageId:appStartPageId,
						status:status
	               },
	               function(data){
	                   if(data.result == 'success'){
	                       $.messager.alert("操作提示", "操作成功！", "info");
	                       $('#AppStartPageList_list').datagrid('reload');
	                   }else if(data.result == 'error'){
	                       if(data.errCode == 'check'){
	                           $.messager.alert("验证提示", data.errMsg, "warning");
	                       }else{
	                           $.messager.alert("系统提示", data.errMsg, "warning");
	                       }
	                   }else{
	                       $.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
	                   }
	            },'json');
	        }
	    });
	}
    
    $(function(){
    	loadList();	
    });
</script>
</body>