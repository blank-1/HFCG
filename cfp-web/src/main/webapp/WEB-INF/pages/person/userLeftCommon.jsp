<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<!-- pLeft start -->
        <div class="img-g girl">
        <c:if test="${userExt.sex eq '0'}">
        <img src="${ctx}/images/personal/head_icon_05.jpg" />
        </c:if>
        <c:if test="${userExt.sex ne '0'}">
        <img src="${ctx}/images/personal/head_icon_07.jpg" />
        </c:if>
        </div>
        <p class="user-name">${sessionScope.currentUser.loginName}</p>
        <p class="user-iphone">手机号：${sessionScope.currentUser.encryptMobileNo}</p>
        <ul class="authen clearFloat">
            <c:if test="${empty userExt.mobileNo }"><li class="li-01"><img src="${ctx}/images/news_icon/li-01_1.jpg" /></li></c:if>
            <c:if test="${!empty userExt.mobileNo }"><li class="li-01"><img src="${ctx}/images/news_icon/li-01_2.jpg" /></li></c:if>
            <c:if test="${empty userExt.isVerified || userExt.isVerified eq '0'}"><li class="li-02"><img src="${ctx}/images/news_icon/li-02_1.jpg" /></li></c:if>
            <c:if test="${userExt.isVerified eq '1'||userExt.isVerified eq '2'}"><li class="li-02"><img src="${ctx}/images/news_icon/li-02_2.jpg" /></li></c:if>
        </ul>
        <p class="visite-g"><a class="user-btn" href="${ctx}/person/to_invite_friends">邀请好友</a></p>
        <!-- p-nav-g start -->
        <div class="p-nav-g clearFloat">
            <h2 class="user-h2-bg1 user-h2bor">
				<a href="${ctx }/person/account/overview" class="parent">账户总览</a></i>
			</h2>
            <h2 class="user-h2-bg2">
				<a href="javascript:;" class="parent">我的理财</a><i class="ibottom"></i>
			</h2>
            <ul class="ul-hide">
                <li><b>·</b><a href="${ctx }/finance/toMyCreditRightList">出借债权</a></li>
                <li><b>·</b><a href="${ctx }/finance/toAllMyFinanceList">省心计划</a></li>
                <li><b>·</b><a href="${ctx }/finance/toTurnCreditRightList">债权转让</a></li>
            </ul>
            <h2 class="user-h2-bg3">
				<a href="javascript:;" class="parent">资金管理</a><i class="itop"></i>
			</h2>
            <ul>
                <li><b>·</b><a href="${ctx }/person/toFundManage">资金流水</a></li>
                <li><b>·</b><a href="${ctx}/person/to_lendorder_list">订单查询</a></li>
                <li><b>·</b><a href="${ctx}/person/toIncome">充值</a></li>
                <li><b>·</b><a href="${ctx}/person/toWithDraw">提现</a></li>
                <li><b>·</b><a href="${ctx}/person/toVoucher">优惠券</a></li>
                <li><b>·</b><a href="${ctx}/bankcard/to_bankcard_list">银行卡管理</a></li>
            </ul>
            <h2 class="user-h2-bg4">
				<a href="javascript:;">个人信息</a><i class="itop"></i>
			</h2>
            <ul class="ul-hide">
                <li><b>·</b><a href="${ctx}/person/to_userInfo">个人信息</a></li>
                <li><b>·</b><a href="${ctx }/person/toAuthentication">验证消息</a></li>
                <li><b>·</b><a href="${ctx }/person/to_pass_manage">密码管理</a></li>
                <li><b>·</b><a href="${ctx }/message/toUserMessage">消息管理</a></li>
                <li><b>·</b><a href="${ctx }/person/to_invite_friends">邀请好友</a></li>
            </ul>
        </div>
        <!-- p-nav-g end -->
    
    <script type="text/javascript">
    $(function(){
    	$(".p-nav-g>h2").click(function(){
    		if($(this).find("i").hasClass("ibottom")){
    			$(this).next().slideUp(500,function(){
    				bottomB();
    			}).siblings("ul").slideUp(500);
    			$(this).find("i").removeClass("ibottom").addClass("itop").parent().siblings("h2").find("i").removeClass("ibottom").addClass("itop");
    			
    		}else{
    		
    			$(this).next().slideDown(500,function(){
    				bottomB();
    			}).siblings("ul").slideUp(500);
    			$(this).find("i").removeClass("itop").addClass("ibottom").parent().siblings("h2").find("i").removeClass("ibottom").addClass("itop");
    			
    		}
    	});
    	
    	initNavMenu() ;
    	
		//初始化面包屑文案和链接以及左侧菜单栏的显示
		function initNavMenu(){
			var tab = $("#titleTab").val();
			if(tab != null){
				var hindex = tab.split("-")[0];
				var uindex = tab.split("-")[1];
				var target = $(".p-nav-g ");
				if(target.attr("class")){
					target.find("h2").each(function(index){
						if(index == hindex){
							try{
								$(this).find('i').attr("class","ibottom");
								var nextObj = $(this).next();
								if(nextObj != null && nextObj.is("ul")){
									nextObj.show();
								}
								if(hindex != 0){
									$(".person-link .container li").eq(1).html("").append($(this).find("a").clone(true)).append(">");
								}else{
									$(".person-link .container li").eq(1).find("span").html($(this).find("a").html());
								}
							}catch(e){};
							
						}else{
							try{
								$(this).find('i').attr("class","itop");
								var nextObj = $(this).next();
								if(nextObj != null && nextObj.is("ul")){
									nextObj.hide();
								}
							}catch(e){};
						}
					});
					var nextUlObj = target.find("h2").eq(hindex).next();
					if(nextUlObj != null && nextUlObj.is("ul")){
						nextUlObj.find("li").eq(uindex).attr("class","active").siblings("li").attr("class","");
						$(".person-link .container li").eq(2).find("span").html(nextUlObj.find("li").eq(uindex).find("a").html());
						//$(".person-link .container li").eq(2).attr("href",(nextUlObj.find("li").eq(uindex).find("a").attr("href")));
					}
				}
			}
		}
		
		bottomB();
    });
</script>
    <!-- pLeft end -->