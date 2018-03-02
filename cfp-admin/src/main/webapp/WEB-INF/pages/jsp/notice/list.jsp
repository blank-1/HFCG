<%@page import="com.xt.cfp.core.constants.WechatNoticeConstants"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<%
    //发布状态
    WechatNoticeConstants.WechatNoticeStateEnum[] wechatNoticeState = WechatNoticeConstants.WechatNoticeStateEnum.values();
    request.setAttribute("wechatNoticeState", wechatNoticeState);
%>
<body>

    <div class="" style="float:left; width:1200px;margin:0 10px 5px 10px;">
        <fieldset style="height:65px">
            <legend>查询条件</legend>
            <form name="wechatNoticeList" id="wechatNoticeList" action="${ctx}/jsp/notice/list" class="fitem" autocomplete="off">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>发布标题：</label></td>
                        <td align="center"><input id="noticeTitle" name="noticeTitle"  value="" type="text" /></td>
                        
                        <td nowrap="nowrap"><label>状态：</label></td>
                        <td>
                            <select id="noticeState" class="easyui-combobox" name="noticeState" style="width:160px;">
                                <option value="">全部</option>
                                <c:forEach items="${wechatNoticeState}" var="wn">
                                    <option value="${wn.value}">${wn.desc}</option>
                                </c:forEach>
                            </select>
                        </td>

                        <td nowrap="nowrap"><label>发布时间：</label>
                        </td>
                        <td>
                            <input id="startDate" name="startDate" style="width:150px;" class="easyui-datebox"/>
							<font style="font-size: 12px;">至</font>
                            <input id="endDate" name="endDate" style="width:150px;" class="easyui-datebox"/>
                        </td>

                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryWechatNotice();" iconCls="icon-search">查询</a>
                            </div>
                        </td>
                        
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="wechat_notice_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="wechat_notice_list_toolbar" style="height:auto">
            <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addWechatNotice();">发布微信公告</a>
        </div>

        <table id="wechat_notice_list_list"></table>
    </div>

    <div id="detail"></div>
    
    <script language="javascript">

    	/**
         * 查询
         */
        function toQueryWechatNotice(){
            $("#wechat_notice_list_list").datagrid('reload', {
                'startDateStr' : Trim($('#startDate').datebox('getValue'),"g"),
                'endDateStr' : Trim($('#endDate').datebox('getValue'),"g"),
                'noticeState' : Trim($("#noticeState").combobox("getValue"),"g"),
                'noticeTitle' : Trim($("#noticeTitle").val(),"g")
            });
        }
        
    	/**
         * 列表加载
         */
        function loadList(){
            $("#wechat_notice_list_list").datagrid({
                idField: 'noticeId',
                title: '微信公告列表',
                url: '${ctx}/jsp/notice/list',
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#wechat_notice_list_toolbar',
                columns:[[
                    {field:'noticeTitle', width:240, title:'标题'},
                    {field:'publishTime', width:150, align:'center', title:'发布时间',
                    	formatter:function(value,row,index){
                    		if(value != null && value != "" )
                        	return getDateTimeStr(new Date(value));
                    	}
                    },
                    {field:'adminName', width:100, align:'center', title:'发布人'},
                    {field:'noticeStateStr', width:80, align:'center', title:'状态',
                    	styler: function (value, row, index) {
                    		if(row.noticeState != null && row.noticeState != ""){
                    			if(row.noticeState == '0'){
                    				return 'color:green';
                    			}else{
                    				return 'color:red';
                    			}
                    		}
                         }	
                    },
                    {field:'action', title:'操作', width:200, align:'center',
                        formatter:function(value,row,index){
                        	// 0.有效；1.无效
                            var result = "<a class='label label-info' onclick='detail(" + row.noticeId + ")'>查看</a> &nbsp;";
                            if(row.noticeState == '0'){
                            	result += "<a class='label' onclick='stopUse(" + row.noticeId + ")'>停用</a> &nbsp;";
                            }else if(row.noticeState == '1'){
                            	result += "<a class='label label-important' onclick='startUse(" + row.noticeId + ")'>启用</a> &nbsp;";
                            }
                            return result;
                        }
                    }
                ]]
            });
        }

        /**
         * 停用
         */
        function stopUse(id){
        	$.messager.confirm('停用提示','确定停用该条公告吗?',function(r){
				if(r){
					$.ajax({
		                url:'${ctx}/jsp/notice/stopUse?noticeId='+id,
		                type:"POST",
		                success:function(msg){
		                    if(msg=='success'){
		                        $.messager.alert('提示', '操作成功！！', 'info',function(){
		                        	toQueryWechatNotice();
		                        });
		                    }else {
		                        $.messager.alert('提示', '操作失败！！', 'info');
		                    }
		                }
		            });
				}
			});
        }

        /**
         * 启用
         */
        function startUse(id){
        	$.messager.confirm('启用提示','确定启用该条公告吗?',function(r){
				if(r){
					$.ajax({
		                url:'${ctx}/jsp/notice/startUse?noticeId='+id,
		                type:"POST",
		                success:function(msg){
		                    if(msg=='success'){
		                        $.messager.alert('提示', '操作成功！！', 'info',function(){
		                        	toQueryWechatNotice();
		                        });
		                    }else {
		                        $.messager.alert('提示', '操作失败！！', 'info');
		                    }
		                }
		            });
				}
			});
        }
		
     /**
      * 弹出添加公告页面
      */
      function addWechatNotice(index) {
      	var url = '${ctx}/jsp/notice/to_add';
      	window.open(url,'_blank',"top=0,left=0,width=" + document.body.clientWidth + ",height=" + (document.body.clientHeight+100));
      }
		
   /**
    * 详情
    */
	function detail(id){
	    $("#detail").after("<div id='notice_detail' style=' padding:10px; '></div>");
	    $("#notice_detail").dialog({
	        resizable: false,
	        title: '微信公告详情',
	        href: '${ctx}/jsp/notice/detail?noticeId='+id,
	        width: 800,
	        height: 550,
	        modal: true,
	        top: 50,
	        left: 400,
	        buttons: [
	            {
	                text: '确定',
	                iconCls: 'icon-ok',
	                handler: function () {
	                    $("#notice_detail").dialog('close');
	                }
	            }
	        ],
	        onClose: function () {
	            $(this).dialog('destroy');
	        }
	    });
	}
	
	$(function(){
	    loadList();
	});
    </script>

</body>
