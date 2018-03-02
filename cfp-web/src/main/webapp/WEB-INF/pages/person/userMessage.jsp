<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<%@ page import="java.util.Date" %>
<%@include file="../common/taglibs.jsp"%>

<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心-消息管理</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx }/js/jquery_page.js"></script><!-- jquery_page javascript 分页 -->
	<script type="text/javascript" src="${ctx }/js/infor_1.js"></script>
</head>

<body class="body-back-gray">
	<input type="hidden" id="hidem" data-value="545247000" />
		<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->
<!-- tabp start -->
<%request.setAttribute("tab","5-3");%>
 <input type="hidden" id="titleTab" value="3-3" />
<!-- tabp end -->
<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">个人信息</a>></li>
        <li><span>消息管理</span></li>
    </ul>
</div>
<!-- person-link end -->


<!-- container start -->
<div class="container clearFloat">
    <!-- pLeft start -->
    <div class="pLeft clearFloat">
    </div>
    <!-- pLeft end -->

    <!-- pRight start -->
    <div class="pRight clearFloat">
        <div class="p-Right-top">
			<p class="xxgl-title">消息管理</p>
			<hr />
			<div class="lcjh-state">
				<p>
					<span class="lcjh-state-t">消息类型</span>
					<span class="lcjh-state-one  msgtype"  value="-1">全部</span>
					<span class="msgtype"  value="2" >站内信</span>
					<span class="msgtype"  value="1" >平台公告</span>
					<span class="msgtype"   value="0" >资金通知</span>
				</p>
				<p>
					<span class="lcjh-state-t">消息状态</span>
					<span class="msgstate lcjh-state-one" value="-1" >全部</span>
					<span class="msgstate"    value="1" >已读</span>
					<span class="msgstate"    value="0" >未读</span>
				</p>
			</div>
		</div>
        <div class="p-Right-xxgl">
			<ul id="userMessage" >
				<li class="p-Right-ulli-bg">
					<ul class="p-Right-ulli-borb">
						<li class="p-Right-xxgl-100 xxgl-hide">类型</li>
						<li class="p-Right-xxgl-350">标题</li>
						<li class="p-Right-xxgl-100 xxgl-hide">状态</li>
						<li class="p-Right-xxgl-100 xxgl-hide">发送人</li>
						<li class="p-Right-xxgl-225">日期</li>
					</ul>
				</li>
				<li>
					<ul class="p-Right-ulli-borb">
						<li class="p-Right-xxgl-100 xxgl-hide">站内信</li>
						<li class="p-Right-xxgl-350"><a href="javascript:;" data-mask="mask"  data-id="pingt_ggao" >新版蚂蚁投资正式上线，全新用户体验等你探索……</a></li>
						<li class="p-Right-xxgl-100 xxgl-hide pre_red">未读</li>
						<li class="p-Right-xxgl-100 xxgl-hide">财富派团队</li>
						<li class="p-Right-xxgl-225">2015-12-18 4:25:13</li>
					</ul>
				</li>
			</ul>
		</div>
		<div class="tcdPageCode mt-20"></div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->

<div class="zhezhao"></div>
<div class="zhezhao1"></div>
<!-- masklayer start  -->
<div class="masklayer" id="login">
	<div class="logind">
	<h2><span>登录财富派</span> <a href="javascript:" class="logina" data-id="close"></a></h2>
             <!-- equity start -->
            <div class="login">
                <form action="" method="post" name="form" class="">
                    <div class="input-group">
                        <label for="username">
                            <input type="text" value="" id="unlogin" maxlength="20" name="username" placeholder="用户名/手机号" class="ipt-input" />
                        </label>
                        <em class="hui"></em>
                    </div>
                    <div class="input-group clearFloat">
                        <label for="password">
                            <input type="password" value="" id="pwlogin" maxlength="16" name="password" placeholder="密码" class="ipt-input" />
                        </label>
                        <em class="passwordem floatLeft"></em><a class="write floatRight passworda mr-10"  href="re_password1.html">忘记密码？</a>
                    </div>
                    <div class="btn-group">
                        <button type="button" id="submit-login" class="btn btn-error mt-0">登录</button>
                        <a class="write floatRight passworda mr-10" href="register.html">账号</a>
                    </div>
                </form>
                
            </div><!-- equity start -->
</div>
</div><!-- masklayer end -->
<!-- masklayer start  -->
<div class="masklayer" id="jieshou_infor">
	<div class="shenf_yanz">
		<h2 class="clearFloat"><span>消息设置</span><a href="javascript:" data-id="close"></a></h2>
		<div class="jieshou_infor_main">
			<dl class="xuan1">
				<dd>手机接收消息</dd>
				<dd><label><input type="checkbox" checked="checked" onclick="selectAll(this);" />全选</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="a" />体现通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="a" />投标通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="a" />回款通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="a" />登录通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="a" />密码修改通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="a" />借款审批通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="a" />还款通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="a" />平台推广</label></dd>
			</dl>                                 
			
			<dl class="xuan3">
				<dd>平台接收消息</dd>
				<dd><label><input type="checkbox" checked="checked" onclick="selectAll3(this);" />全选</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="c" />体现通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="c" />投标通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="c" />回款通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="c" />登录通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="c" />密码修改通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="c" />借款审批通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="c" />还款通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="c" />平台推广</label></dd>
			</dl>
			<dl class="xuan4">
				<dd>平台接收消息</dd>
				<dd><label><input type="checkbox" checked="checked" onclick="selectAll4(this);" />全选</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="e" />体现通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="e" />投标通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="e" />回款通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="e" />登录通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="e" />密码修改通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="e" />借款审批通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="e" />还款通知</label></dd>
				<dd><label><input type="checkbox" checked="checked" class="e" />平台推广</label></dd>
			</dl>
		</div>
		<div class="infor_btn">
			<button type="button" class="infor_keep">保存</button>
			<button type="button" class="infor_cancel">取消</button>
		</div>
	</div>
</div>
<!-- masklayer end -->
<!-- masklayer start  -->
<div class="masklayer" id="pingt_ggao">

		<h2 class="clearFloat"><span>站内信</span><a href="javascript:" data-id="close"></a></h2>
		<div class="pingt_ggao">
			<div class="clear_20"></div>
			<p><span id="messageName" >新版蚂蚁投资正式上线，全新用户体验等你探索</span></p>
			<div class="clear_5"></div>
			<p><font id="messageSendTime">发布日期：2015-05-26 12:35:20</font></p>
			<div class="clear_15"></div>
			<p><img id="imgAddress" src="${ctx }/images/pingt_ggao.jpg" width='482' height='169'/></p>
			<div style="height:10px;clear:both;"></div>
			<p style="text-align:left;" id="messageContent">新版蚂蚁投资正式上线，全新用户体验等你探索。新版蚂蚁投资正式上线，全新用户体验等你探索。新版蚂蚁投资正式上线，全新用户体验等你探索。</p>
			<div class="clear_30"></div>
			<p><a href="javascript:" data-id="close" ><button id="infor_true">确定</button></a></p>
			<div class="clear_30"></div>
		</div>

</div>
<!-- masklayer end -->
<!-- masklayer start  -->
<div class="masklayer" id="pingt_ggao2">

		<h2 class="clearFloat"><span>平台公告2</span><a href="javascript:" data-id="close"></a></h2>
		<div class="pingt_ggao">
			<div style="height:20px;clear:both;"></div>
			<p><span>新版蚂蚁投资正式上线，全新用户体验等你探索</span></p>
			<div style="height:5px;clear:both;"></div>
			<p><font>发布日期：2015-05-26 12:35:20</font></p>
			<div style="height:15px;clear:both;"></div>
			<p style="text-align:left;">新版蚂蚁投资正式上线，全新用户体验等你探索。新版蚂蚁投资正式上线，全新用户体验等你探索。新版蚂蚁投资正式上线，全新用户体验等你探索。</p>
			<div style="height:100px;clear:both;"></div>
			<p><a href="javascript:" data-id="close" ><button>确定</button></a></p>
		</div>

</div>
<!-- masklayer end -->

<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->


</body>
</html>