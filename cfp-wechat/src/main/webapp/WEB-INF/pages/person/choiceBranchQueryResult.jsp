<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<title>查询结果</title>
<%@include file="../common/common_js.jsp"%>
<script src="${ctx}/js/ChoiceBranch.js" type="text/javascript"></script>
<style type="text/css">
body,html{
	width: 100%;
	height: 100%;
}
</style> 
</head>
<body class="body" style="background: #f5f9fa;position: relative;">
	<div class="w_wrapper">
		<div class="w_seachMsg" style="margin: 0;">
			<p style="color: #4b4b4b;width: 90%;display: block;margin: .5rem auto 0 auto;background: #e2f3fa;text-indent: .5rem; padding-top: .5rem;">地址：</p>
			<div class="w_selCity">	
				<div class="w_Province"  id="w_Province">
					<label class="showProvince">省份</label>
					<div class="Province"  id="Province">
					</div>
				</div>
				<div class="w_market"  id="w_market">
					<label class="showCity">市区</label>
					<div class="Province"  id="market">
					</div>
				</div>
			</div>
		</div>
		<div class="w_BranchMsg" style="margin-top: 1rem;height: auto;">
			<!-- 提交表单开始 -->
			<form action="${ctx}/person/choiceBranchQueryResult" id="queryForm" name="queryForm" method="post">
				<span class="w_titMsg">支行关键字</span>
				<div class="w_inputBox">
					<input type="text" id="w_seachMag" class="w_seachMag" name="w_seachMag" style="width: 100%;" value="${w_seachMag }">
				</div>
				
				<input type="hidden" id="w_seachPid" name="w_seachPid" value="${w_seachPid }">
				<input type="hidden" id="w_seachCid" name="w_seachCid" value="${w_seachCid }">
				
				<input type="hidden" id="w_seachPidStr" name="w_seachPidStr" value="${w_seachPidStr }">
				<input type="hidden" id="w_seachCidStr" name="w_seachCidStr" value="${w_seachCidStr }">
			</form>
			<!-- 提交表单结束 -->
		</div>
		<a href="javaScript:void(0);" class="w_chickBack" id="w_chickBack" name="w_chickBack">检索支行</a>
		
		<!-- 无数据提示开始 -->
		<c:if test="${empty bankInfoList}">
			<div class="w_NoResult"><!--没有数据的时候让这个盒子出现-->
				<p>没有查询到支行</p>
			</div>
		</c:if>
		<!-- 无数据提示结束 -->
		
		<!-- 支行信息列表开始 -->
		<c:if test="${not empty bankInfoList}">
			<div class="w_resultBox"><!--搜索的数据渲染在这-->
				<ul class="w_lookResult" id="w_lookResult" >
					<c:forEach items="${bankInfoList }" var="bankInfo">
						<li>
							<p id="w_numbers">${bankInfo.prcptcd }</p>
							<p id="w_bankM">${bankInfo.brabank_name }</p>
						</li>
					</c:forEach>
				</ul>
				<p class="w_more">没有更多数据了</p>
			</div>
		</c:if>
		<!-- 支行信息列表结束 -->
		
		<!-- 提交支行表单开始 -->
		<form action="${ctx}/person/choiceBranch" id="branchForm" name="branchForm" method="post">
			<input type="hidden" id="prcptcd" name="prcptcd" >
			<input type="hidden" id="brabank_name" name="brabank_name" >
			<input type="hidden" id="brabank_city" name="brabank_city" >
		</form>
		<!-- 提交支行表单结束 -->
		
	</div>
	<script>
	    
		$(function(){
			// 页面加载显示
			pid = $("#w_seachPid").val();
			cid = $("#w_seachCid").val();
			var q_mag = $("#w_seachMag").val();
			var q_pidStr = $("#w_seachPidStr").val();
			var q_cidStr = $("#w_seachCidStr").val();
			$(".showProvince").html(q_pidStr);
			$(".showCity").html(q_cidStr);
			
			// 执行检索
			$("#w_chickBack").on("click",function(){
				$("#w_seachPid").attr("value",pid);
				$("#w_seachCid").attr("value",cid);
				$("#queryForm").submit();
			})
			
			// 选择支行信息事件
			$("#w_lookResult").on("click","li",function(){
				var w_numbers = $(this).find("p#w_numbers").html();
				var w_bankM = $(this).find("p#w_bankM").html();
				$("#prcptcd").attr("value",w_numbers);
				$("#brabank_name").attr("value",w_bankM);
				$("#brabank_city").attr("value",cid);
				$("#branchForm").submit();
			})
		})
		
		/* var lastH=$("#w_lookResult").find("li:last").offset().top;
		$(document).on("scroll",function(){
			
			console.log(lastH)
		}) */
		
		/*
		$(document).on("scroll",function(){
			if ($(window).scrollTop() + $(window).height() == $(document).height()) {
	            
	     	}
		})
		*/
		
	</script>
</body>
</html>