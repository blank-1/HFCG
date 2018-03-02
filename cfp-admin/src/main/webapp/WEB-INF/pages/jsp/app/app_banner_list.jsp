<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<body>
<div id="AppBannerList" class="container-fluid" style="padding: 5px 0px 0px 10px;">

	<div id="AppBannerList_list_toolbar" style="height:auto">
        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addAppBanner();">新增</a>
    </div>
    
    <table id="AppBannerList_list"></table>
</div>

<script language="javascript">
	/**
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#AppBannerList_list").datagrid({
            idField: 'appBannerId',
            title: 'APP Banner页列表',
            url: '${ctx}/jsp/app/app_banner_list',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#AppBannerList_list_toolbar',
            columns:[[
                      {field:'appBannerId',checkbox:true},
                      {field:'appType', width:100,title:'平台',align:'center'},
                      {field:'orderBy', width:100,title:'banner顺序',align:'center'},
                      {field:'bannerName', width:250,title:'活动名称'},
                      {field:'state', width:80,title:'状态',align:'center'},
                      {field:'updateTime', width:140,title:'最后更改时间'},
                      {field:'adminDisplayName', width:80,title:'操作人',align:'center'},
                      {field:'action',title:'操作',width:200,align:'center',
                          formatter:function(value,row,index){
                               var result = "<a class='label label-info' onclick='showStartPage(" + index + ")'>详情</a>&nbsp;&nbsp;";
                               result += "<a class='label label-success' onclick='editStartPage(" + index + ")'>修改</a>&nbsp;&nbsp;";
                               if(row.state == 1){
                         			result += "<a class='label' onclick='doAppBannerState(" + row.appBannerId + ",0)'>禁用</a>&nbsp;&nbsp;";	
                         		}else{
                         			result += "<a class='label label-important' onclick='doAppBannerState(" + row.appBannerId + ",1)'>启用</a>&nbsp;&nbsp;";	
                         		}
                               result += "<a class='label label-inverse' onclick='doAppBannerDelete(" + row.appBannerId + ",1)'>删除</a>&nbsp;&nbsp;";
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
           		var state = $(this).datagrid("getColumnOption", "state");
                if (state) {
                	state.formatter = function (value, rowData, rowIndex) {
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
     * 执行：弹出添加层。
     */
    function addAppBanner(index) {
    	$("#AppBannerList").after("<div id='addAppBanner' style=' padding:10px; '></div>");
        $("#addAppBanner").dialog({
            resizable: false,
            title: 'APP Banner添加',
            href: '${ctx}/jsp/app/to_app_banner_add',
            width: 550,
            height: 600,
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
	                            $("#addAppBanner").contents().find("#addAppBanner_form").submit();
                			}
                    	});
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#addAppBanner").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }
    
    /**
     * 执行：弹出编辑层。
     */
    function editStartPage(index) {
    	$("#AppBannerList_list").datagrid("selectRow",index);
        var selRow = $("#AppBannerList_list").datagrid("getSelected");
        if (selRow) {
            $("#AppBannerList").after("<div id='editAppBanner' style=' padding:10px; '></div>");
            $("#editAppBanner").dialog({
                resizable: false,
                title: 'APP Banner编辑',
                href: '${ctx}/jsp/app/to_app_banner_edit?appBannerId=' + selRow.appBannerId,
                width: 550,
                height: 600,
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
		                            $("#editAppBanner").contents().find("#editAppBanner_form").submit();
                    			}
                        	});
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#editAppBanner").dialog('close');
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
    	$("#AppBannerList_list").datagrid("selectRow",index);
    	var selRow = $("#AppBannerList_list").datagrid("getSelected");
    	if (selRow) {
            $("#AppBannerList").after("<div id='showAppBanner' style=' padding:10px; '></div>");
            $("#showAppBanner").dialog({
                resizable: false,
                title: 'APP Banner页信息',
                href: '${ctx}/jsp/app/to_app_banner_show?appBannerId=' + selRow.appBannerId,
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
                            $("#showAppBanner").dialog('close');
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
	function doAppBannerState(appBannerId, state){
		var msg = "确定要禁用吗？";
		if(state == 1){
			msg = "确定要启用吗？";
		}
	    $.messager.confirm("操作提示", msg, function (r) {
	        if (r) {
				$.post('${ctx}/jsp/app/doAppBannerState',
	               {
						appBannerId:appBannerId,
						state:state
	               },
	               function(data){
	                   if(data.result == 'success'){
	                       $.messager.alert("操作提示", "操作成功！", "info");
	                       $('#AppBannerList_list').datagrid('reload');
	                   }else if(data.result == 'error'){
	                       if(data.errCode == 'check'){
	                           $.messager.alert("验证提示", data.errMsg, "info");
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
	
	/**
     * 执行：删除。
     */
	function doAppBannerDelete(appBannerId, state){
	    $.messager.confirm("操作提示", "确定要删除吗？", function (r) {
	        if (r) {
				$.post('${ctx}/jsp/app/doAppBannerDelete',
	               {
						appBannerId:appBannerId
	               },
	               function(data){
	                   if(data.result == 'success'){
	                       $.messager.alert("操作提示", "操作成功！", "info");
	                       $('#AppBannerList_list').datagrid('reload');
	                   }else if(data.result == 'error'){
	                       if(data.errCode == 'check'){
	                           $.messager.alert("验证提示", data.errMsg, "info");
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