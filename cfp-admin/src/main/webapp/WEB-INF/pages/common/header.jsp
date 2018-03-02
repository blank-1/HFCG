<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cfp-admin.com/jsp/taglib" prefix="mis" %>
<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <a class="brand" href="#" style="font-weight:bold;color: #ffffff" >财富派后台系统</a>
        <ul class="nav">
            <ul class="nav">
            
	            <li class="dropdown">
	                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
	                    	借款管理
	                    <b class="caret"></b>
	                </a>
	                <ul class="dropdown-menu">
                        <mis:PermisTag code="10001">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/loanManage/loan/toLoanApplicationList', '借款管理>>个人借款列表')">个人借款列表</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="010003">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/enterprise/loan/to_loan_list', '借款管理>>企业借款列表')">企业借款列表</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="10002">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/loanManage/loan/toLoanAppPublishDescEdit', '借款管理>>借款申请发标')">借款申请发标</a></li>
                        </mis:PermisTag>
	                </ul>
	            </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        	出借列表
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <mis:PermisTag code="20001">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/rights/all/to_rights_list', '出借列表>>所有债权列表')">所有债权列表</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="20002">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/rights/transferApply/to_transferApply_list', '出借列表>>转让中债权列表')">转让中债权列表</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="20003">
                        	<li><a href="#" onclick="goto('/xxxxx/xxxxx/xxxxx', '出借列表>>逾期债权列表')">逾期债权列表</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="20004">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/lendList/tofinancialPlanList', '出借列表>>省心计划列表')">省心计划列表</a></li>
                        </mis:PermisTag>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        	客户管理
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <mis:PermisTag code="30001">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/lendList/to_order_list', '客户管理>>订单列表')">订单列表</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="30002">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/custom/customer/customerInfoList', '客户管理>>客户列表')">客户列表</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="030003">
                        	<li><a href="#" onclick="goto('${ctx}/validCode/showValidCodes', '客户管理>>验证码列表')">验证码查询 </a></li>
                        </mis:PermisTag>
                    </ul>
                </li>
                
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        	财务管理
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                    	<mis:PermisTag code="40001">
                    		<li><a href="#" onclick="goto('${ctx}/jsp/financial/toWaitLoanList', '财务管理>>满标待放款')">满标待放款</a></li>
                    	</mis:PermisTag>
                        <mis:PermisTag code="40002">
                        	<li><a href="#" onclick="goto('${ctx}/withdraw/to_withDraw_list', '财务管理>>提现申请列表')">提现申请列表</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="40003">
                        	<li><a href="#" onclick="goto('${ctx}/rechargeOrder/to_rechargeOrderList', '财务管理>>充值列表')">充值列表</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="40004">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/financial/toRepaymentList', '财务管理>>到期还款')">到期还款</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="40005">
                        	<li><a href="#"onclick="goto('${ctx}/jsp/financial/toSystemFlowList', '财务管理>>平台流水')">平台流水</a></li>
                        </mis:PermisTag>
                    </ul>
                </li>
				
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        	报表管理
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                   	 	<mis:PermisTag code="500">
                        	<li><a href="#" onclick="goto('${ctx}/report/showReportList', '报表管理>>报表管理')">报表管理</a></li>
                        </mis:PermisTag>
                    </ul>
                </li>
                 
                 
				<li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        	合作商管理
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                    	<mis:PermisTag code="60001">
                        	<li><a href="#" onclick="goto('${ctx}/guaranteeCompany/to_guaranteeCompany_list', '合作商管理>>担保公司')">担保公司</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="60002">
                        	<li><a href="#" onclick="goto('${ctx}/bondSource/to_bondSource_list', '合作商管理>>渠道管理')">渠道管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="060003">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/enterprise/to_enterprise_list', '合作商管理>>借款/保理企业')">借款/保理企业</a></li>
                        </mis:PermisTag>
                    </ul>
                </li>
                
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        	产品&消息
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                    	<mis:PermisTag code="70001">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/product/lend/to_lend_product_list', '产品管理>>出借产品管理')">出借产品管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="70002">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/product/loan/to_loan_product_list', '产品管理>>借款产品管理')">借款产品管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="70003">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/product/feesitem/tofeesItemList', '产品管理>>费用管理')">费用管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="70004">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/message/to_station_notice_list', '消息中心>>消息管理')">消息管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="70005">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/message/to_system_list', '消息中心>>系统消息')">系统消息</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="070006">
                        	<li><a href="#" onclick="goto('${ctx}/voucher/showVoucherProduct', '财富券管理>>创建财富券')">创建财富券</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="070007">
                        	<li><a href="#" onclick="goto('${ctx}/voucher/showVoucher', '财富券管理>>财富券列表')">财富券列表</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="070008">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/app/to_start_page_list', '产品&消息>>APP启动页管理')">APP启动页管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="070009">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/app/to_app_banner_list', '产品&消息>>APP Banner页管理')">APP Banner页管理</a></li>
                        </mis:PermisTag>
                       
                        <mis:PermisTag code="070010">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/rate/product/to_list', '产品&消息>>加息券产品管理')">加息券产品管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="070011">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/rate/user/to_list', '产品&消息>>加息券发放管理')">加息券发放管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="070012">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/notice/to_list', '产品&消息>>微信公告管理')">微信公告管理</a></li>
                        </mis:PermisTag>
                          <mis:PermisTag code="070013">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/app/to_app_banner_list?type=1', '产品&消息>>微信 Banner页管理')">微信 Banner页管理</a></li>
                        </mis:PermisTag>
                    </ul>
                </li>
                
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        	分销管理
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                    	<mis:PermisTag code="080101">
                       		<li><a href="#" onclick="goto('${ctx}/disActivity/to_dis_activity_list', '分销管理>>分销产品列表')">分销产品列表</a></li>
                       	</mis:PermisTag>	
                       	<li><a href="#" onclick="goto('${ctx}/disActivity/dis_customer_list', '分销管理>>分销客户列表')">分销客户列表</a></li>
                    </ul>
                </li>
                
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        	文件报备
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                       	<li><a href="#" onclick="goto('${ctx}/manualReport/userReport', '文件报备>>个人开户')">个人开户</a></li>
                       	<li><a href="#" onclick="goto('${ctx}/manualReport/corpReport', '文件报备>>法人开户')">法人开户</a></li>
                       	<li><a href="#" onclick="goto('${ctx}/manualReport/loanReport', '文件报备>>项目新增')">项目新增</a></li>
                       	<li><a href="#" onclick="goto('${ctx}/manualReport/tradeReport?tradeType=2', '文件报备>>投标')">投标</a></li>
                       	<li><a href="#" onclick="goto('${ctx}/manualReport/tradeReport?tradeType=4', '文件报备>>满标放款')">满标放款</a></li>
                       	<li><a href="#" onclick="goto('${ctx}/manualReport/tradeReport?tradeType=5', '文件报备>>转让')">转让</a></li>
                       	<li><a href="#" onclick="goto('${ctx}/manualReport/tradeReport?tradeType=6', '文件报备>>回款')">回款</a></li>
                       	<li><a href="#" onclick="goto('${ctx}/manualReport/tradeReport?tradeType=8', '文件报备>>其它')">其它</a></li>
                       	<li><a href="#" onclick="goto('${ctx}/manualReport/tradeReport?tradeType=3', '文件报备>>流标')">流标</a></li>
                    </ul>
                </li>
                
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        	系统设置
                        <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <mis:PermisTag code="80001">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/system/admin/to_admin_list', '系统管理>>员工管理')">员工管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="80002">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/system/role/to_role_list', '系统管理>>角色管理')">角色管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="80003">
                        	<li><a href="#" onclick="goto('${ctx}/jsp/system/function/to_function_list', '系统管理>>权限管理')">权限管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="80004">
                        	<li><a href="#" onclick="goto('${ctx}/constant/to_constant_list', '系统管理>>字典管理')">字典管理</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="80005">
                        	<li><a href="#" onclick="goto('${ctx}/sysutils/toUtils', '系统管理>>工具')">工具</a></li>
                        </mis:PermisTag>
                        <mis:PermisTag code="80006">
                        	<li><a href="#" onclick="goto('${ctx}/timer/to_timers', '系统管理>>定时器')">定时器</a></li>
                        </mis:PermisTag>
                    </ul>
                </li>
            </ul>

        </ul>
        <ul class="nav pull-right" style="position: static;font-size: 12px">
            <c:if test="${empty sessionScope.loginUser}">
                <li id="loginUser" name="loginUser">
                    <a href="${ctx}/toLogin">登录</a>
                </li>
            </c:if>
            <c:if test="${not empty sessionScope.loginUser}">
                <li id="loginUser" name="loginUser">
                    <a onclick="goto('${ctx}/jsp/system/admin/to_edit_pass', '系统设置>>密码管理')" style="cursor: pointer;" title="密码修改">当前用户：<span id="displayName">${sessionScope.loginUser.displayName}</span></a>
                </li>
            </c:if>
            <li id="logout" name="logout">
                <a href="#" onclick="logout();">退出</a>
            </li>
        </ul>
    </div>
</div>
<div id="changePwdDiv"></div>
<script>

	// 页面跳转。
    function goto(url, title) {
        $("#breadcrumb").html(title);
        $('#main').panel('refresh', url);
    }

	// 登出。
    function logout() {
        $.post("${ctx}/logout",
	        function (data) {
	            if (data == "success") {
	                window.location.href = "${ctx}/toLogin";
	            }
	    },'text');
    }

</script>