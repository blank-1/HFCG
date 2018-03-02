<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<%@ page import="com.xt.cfp.core.constants.VoucherConstants" %>
<head>
</head>
<body>

    <div class="" style="float:left; width:1200px;margin:0 10px 5px 10px;">
        <fieldset style="height:40px">
            <legend>查询条件</legend>
            <form name="voucherProductList" id="voucherProductList" action="" class="fitem" autocomplete="off">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>客户状态：</label></td>
                        <td>
                            <select id="user_status" class="easyui-combobox" style="width:100px;" editable="false">
                                <option value="-1">全部</option>
                                <option value="0">正常</option>
                                <option value="1">冻结</option>
                                <option value="2">禁用</option>
                            </select>
                        </td>

                        <td nowrap="nowrap"><label>客户姓名：</label></td>
                        <td>
                            <input id="user_name" style="width:100px;"/>
                        </td>
						
						<td nowrap="nowrap"><label>用户名：</label></td>
                        <td>
                            <input id="user_login" style="width:100px;"/>
                        </td>
                        
                        <td nowrap="nowrap"><label>手机号：</label></td>
                        <td>
                            <input id="user_mobile" style="width:150px;"/>
                        </td>
                        
                        <td nowrap="nowrap"><label>身份证号：</label></td>
                        <td>
                            <input id="user_idcard" style="width:200px;"/>
                        </td>
                        
                        <td nowrap="nowrap"><label></label></td>
                        <td>
                        	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="javascript:toQueryVoucherProduct();" iconCls="icon-search">查询</a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="dis_activity_list" class="container-fluid" style="padding: 5px 0px 0px 10px">

        <table id="dis_activity_list_list"></table>
    </div>
    
    <div id="detail"></div>
<script language="javascript">
		
    $(function(){
        loadList();
    });
    
    function toQueryVoucherProduct(){
        $('#dis_activity_list_list').datagrid('reload', {
            'status' : Trim($('#user_status').datebox('getValue'),"g"),
            'userName' : Trim($("#user_name").val(),"g"),
            'userLogin' : Trim($("#user_login").val(),"g"),
            'userMobile' : Trim($("#user_mobile").val(),"g"),
            'userIdcard' : Trim($("#user_idcard").val(),"g")
        });
    }
    
    function loadList(){
        $("#dis_activity_list_list").datagrid({
            idField: 'userId',
            title: '分销客户列表',
            url: '${ctx}/disActivity/disCustomerList',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.6,
            singleSelect: true,
            rownumbers: true,
            columns:[[
                {field:'loginName', width:120,title:'用户名'},
                {field:'realName', width:120,title:'客户姓名'},
                {field:'mobile', width:100,title:'手机号'},
                {field:'idCard', width:150,title:'身份证号'},
                {field:'firstNum', width:100,title:'一级分销人数'},
                {field:'secondNum', width:100,title:'二级分销人数'},
                {field:'thirdNum', width:100,title:'三级分销人数'},
                {field:'profit', width:120,title:'佣金奖励（元）'},
                {field:'shouldProfit', width:120,title:'预期佣金奖励（元）'},
                {field:'status', width:80,title:'客户状态',formatter:function(value,row,index){
                	if(row.status=='0'){
                		return "正常";
                	}else if(row.status=='1'){
                		return "冻结";
                	}else if(row.status=='2'){
                		return "禁用";
                	}else{
                		return "";
                	}
                }},
                {field:'action',title:'操作',width:80,align:'center',
                    formatter:function(value,row,index){
                        var result = "";
                        result += "<a class='label label-info' onclick='detail(" + row.userId + ")'>详情</a> &nbsp;";
                        return result;
                    }
                }
            ]]
        });
    }

    function detail(userId){
    	$("#detail").after("<div id='disCustomerDetail' style=' padding:10px; '></div>");
    	$("#disCustomerDetail").dialog({
    	    resizable: false,
    	    title: '分销客户详情',
    	    href: '${ctx}/disActivity/disCustomerDetail?userId=' + userId,
    	    width: document.body.clientWidth * 0.75,
    	    height: document.body.clientHeight * 0.8,
    	    modal: true,
    	    buttons: [
    	        {
    	            text: '确定',
    	            iconCls: 'icon-ok',
    	            handler: function () {
    	                $("#disCustomerDetail").dialog('close');
    	            }
    	        }
    	    ],
    	    onClose: function () {
    	        $(this).dialog('destroy');
    	    }
    	});
    }
      
  	//格式化时间
    function dateTimeFormatter(val) {

        if (val == undefined || val == "")
            return "";
        var date;
        if(val instanceof Date){
            date = val;
        }else{
            date = new Date(val);
        }
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();

        var h = date.getHours();
        var mm = date.getMinutes();
        var s = date.getSeconds();

        var dateStr = y + '-' + (m < 10 ? ('0' + m) : m) + '-'
                + (d < 10 ? ('0' + d) : d);
        var TimeStr = h + ':' + (mm < 10 ? ('0' + mm) : mm) + ':'
                + (s < 10 ? ('0' + s) : s);

        return dateStr;
    }
  
</script>

</body>

