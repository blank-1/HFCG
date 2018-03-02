<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- <%@ include file="../../common/common.jsp" %> --%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<style>
tr{
	height: 40px;
}
tr label{
	text-align: right;
}
.td_left{
	padding-left: 120px;
}
</style>
	<input type="hidden" value="${distributorVO.userId}" id="customer_userId"/>
	
	<div style="float:left; width:1300px;margin:0 10px 5px 10px;">
		<fieldset style="height:160px">
			<table>
				<tr>
                	<td nowrap="nowrap"><label>用户名：</label></td>
                	<td>${distributorVO.loginName}</td>
                	<td nowrap="nowrap" class="td_left"><label>性别：</label></td>
                	<td>${distributorVO.sex}</td>
                	<td nowrap="nowrap" class="td_left"><label>出生日期：</label></td>
                	<td>${distributorVO.birthStr}</td>
                	<td nowrap="nowrap" class="td_left"><label>身份证号：</label></td>
                	<td>${distributorVO.idCard}</td>
                </tr>
                <tr>
                	<td nowrap="nowrap"><label>客户姓名：</label></td>
                	<td>${distributorVO.realName}</td>
                	<td nowrap="nowrap" class="td_left"><label>学历：</label></td>
                	<td>${distributorVO.education}</td>
                	<td nowrap="nowrap" class="td_left"><label>手机号：</label></td>
                	<td>${distributorVO.mobile}</td>
                	<td nowrap="nowrap" class="td_left"><label>客户状态：</label></td>
                	<td>${distributorVO.status}</td>
                </tr>
                <tr>
                	<td nowrap="nowrap"><label>总邀请人数：</label></td>
                	<td>${distributorVO.firstNum+distributorVO.secondNum+distributorVO.thirdNum}</td>
                	<td nowrap="nowrap" class="td_left"><label>一级邀请人数：</label></td>
                	<td>${distributorVO.firstNum}</td>
                	<td nowrap="nowrap" class="td_left"><label>二级邀请人数：</label></td>
                	<td>${distributorVO.secondNum}</td>
                	<td nowrap="nowrap" class="td_left"><label>三级邀请人数：</label></td>
                	<td>${distributorVO.thirdNum}</td>
                </tr>
                <tr>
                	<td nowrap="nowrap"><label>总邀请贡献佣金：</label></td>
                	<td>${distributorVO.profit}</td>
                	<td nowrap="nowrap" class="td_left"><label>一级邀请贡献佣金：</label></td>
                	<td>${distributorVO.firstProfit}</td>
                	<td nowrap="nowrap" class="td_left"><label>二级邀请贡献佣金：</label></td>
                	<td>${distributorVO.secondProfit}</td>
                	<td nowrap="nowrap" class="td_left"><label>三级邀请贡献佣金：</label></td>
                	<td>${distributorVO.thirdProfit}</td>
                </tr>
			</table>
		</fieldset>
		
	</div>
	
	<div class="" style="float:left; width:1300px;margin:0 10px 5px 10px;">
        <fieldset style="height:75px">
            <legend>查询条件</legend>
            <form name="disCustomerDetailListForm" id="disCustomerDetailListForm" action="" class="fitem" autocomplete="off">
                <table>
                    <tr>
                        <td nowrap="nowrap"><label>邀请层级：</label></td>
                        <td>
                            <select id="customer_level" class="easyui-combobox" style="width:100px;" editable="false">
                                <option value="0">全部</option>
                                <option value="1">一级</option>
                                <option value="2">二级</option>
                                <option value="3">三级</option>
                            </select>
                        </td>
                        
                        <td nowrap="nowrap"><label>姓名：</label></td>
                        <td>
                            <input id="customer_name" name="customer_name" style="width:100px;" class="text"/>
                        </td>
						
						<td nowrap="nowrap"><label>用户名：</label></td>
                        <td>
                            <input id="customer_login" name="customer_login" style="width:100px;" class="text"/>
                        </td>
                        
                        <td nowrap="nowrap"><label>手机号：</label></td>
                        <td>
                            <input id="customer_mobile" name="customer_mobile" style="width:150px;" class="text"/>
                        </td>
                        
                        <td nowrap="nowrap"><label>时间：</label></td>
                        <td>
                            <input id="start_time" name="start_time" style="width:150px;" editable="false" class="easyui-datebox"/>--
                            <input id="end_time" name="end_time" style="width:150px;" editable="false" class="easyui-datebox"/>
                        </td>
                        
                        <td nowrap="nowrap"><label></label></td>
                        <td>
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton" onclick="javascript:toQueryCustomerList();" iconCls="icon-search">查询</a>
                            </div>
                        </td>
                    </tr>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="dis_customer" class="container-fluid" style="padding: 5px 0px 0px 10px">

        <table id="dis_customer_list"></table>
    </div>
    
<script language="javascript">
		
    $(function(){
        loadCustomerList();
    });
    
    function toQueryCustomerList(){
        $("#dis_customer_list").datagrid("reload", {
            "disLevel" : Trim($("#customer_level").datebox("getValue"),"g"),
            "userName" : Trim($("#customer_name").val(),"g"),
            "userLogin" : Trim($("#customer_login").val(),"g"),
            "userMobile" : Trim($("#customer_mobile").val(),"g"),
            "startTime" : Trim($("#start_time").datebox("getValue"),"g"),
            "endTime" : Trim($("#end_time").datebox("getValue"),"g")
        });
    }
    
    function loadCustomerList(){
        $("#dis_customer_list").datagrid({
            idField: "voucherProductId",
            url: "${ctx}/disActivity/disCustomerDetailList?userId="+Trim($("#customer_userId").val(),"g"),
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.68,
            height: document.body.clientHeight * 0.4,
            singleSelect: true,
            rownumbers: true,
            columns:[[
                {field:"realName", width:120,title:"姓名"},
                {field:"loginName", width:120,title:"用户名"},
                {field:"mobile", width:100,title:"手机号"},
                {field:"amount", width:150,title:"投资金额"},
                {field:"transforM", width:150,title:"转出金额"},
                {field:"profit", width:100,title:"贡献佣金"},
                {field:"shouldProfit", width:100,title:"预期贡献佣金"},
                {field:"creatTime", width:100,title:"邀请时间",formatter:dateTimeFormatter},
                {field:"disLevel", width:120,title:"邀请层级"}
            ]]
        });
    }

</script>