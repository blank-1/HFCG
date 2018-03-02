<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@include file="../common/taglibs.jsp"%>
<%@include file="../common/common_js.jsp"%>

<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心-个人信息</title>
	<script type="text/javascript" src="${ctx}/js/infor_6.js"></script><!-- action javascript -->
</head>

<body class="body-back-gray">
	
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->

<!-- tabp start -->
<%request.setAttribute("tab","5-0");%>
 <input type="hidden" id="titleTab" value="3-0" />
<!-- tabp end -->
	
<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">个人信息</a>></li>
        <li><span>个人信息</span></li>
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
			<p class="grxx-title">个人信息</p>
		</div>
        <div class="p-Right-grxx">
			<div class="per_center">
				<div class="per-cenri">
					<form action="" method="post" name="infoform">
						<div class="per-inputbox">
							<label>
								<span>用户名</span>
								<input type="text" id="per-username" value="${username}"  maxlength="" placeholder="请输入用户名" autocomplete="off" disabled="disabled"/>
								<div class="user-xg"><button type="button" id="xiugai" >修改</button></div>
							</label>
							<em></em>
						</div>
						<div class="per-inputbox">
							<label>
								<span>姓名</span>
								<p>
								<c:if test="${isVerified == 1||isVerified == 2}">
									<font>${realName}</font>
								</c:if>
								<c:if test="${isVerified == 0}">
									<font><a href="javascript:;" data-mask='mask' data-id="shenfen">身份认证</a></font>
								</c:if>
								</p>
							</label>
							<div class="clear_10"></div>	
						</div>
						<div class="per-inputbox">
							<label>
								<span>性别</span>
								<p><font>${sex}</font></p>
							</label>
							<div class="clear_10"></div>	
						</div>
						<div class="per-inputbox">
							<label>
								<span>出生日期</span>
								<p><font><fmt:formatDate value="${birthday}"/></font></p>
							</label>
							<div class="clear_10"></div>	
						</div>
						<div class="per-inputbox">
							<label>
								<span>手机号</span>
								<p><font>${mobileNo}</font></p>
							</label>
							<div class="clear_10"></div>	
						</div>
						<div class="per-inputbox">
							<div class="clear_10"></div>	
							<label>
								<span>学历</span>
								<!-- select start -->
								<select class="xueli" id="per-education" disabled="disabled">  
									<option value="">--</option>
								</select>
							</label>
							<em></em>
						</div>
						<div class="per-inputbox">
							<div class="clear_10"></div>	
							<label>
								<span>常住地区</span>
								<select id="per-province" disabled="disabled" onchange="onProvinceSelect();">  
									<option value="">--</option>  
								</select>  
								<select id="per-city" disabled="disabled" style="width:80">  
									<option value="">--</option>
								</select>
							</label>
							<em></em>
							<div class="clear_10"></div>	
						</div>
						<div class="per-inputbox">
							<label>
								<span>详细地址</span>
								<input type="text" id="per-address" value="${detail}" maxlength="" placeholder="请输入详细地址" autocomplete="off" disabled="disabled" />
							</label>
							<em></em>
						</div>
						<div class="per-inputbox">
							<button type="button" id="xiugai2" >修改</button>
							<div class="clear_70" ></div>					
						</div>
						<div class="per-inputbox">
							<button type="button" id="per-keep" >保存</button>
							<div class="clear_70"  ></div>					
						</div>
						
					</form>
				</div>
			</div>
			<div class="clear_10"></div>
		</div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->

<div style="clear:both;height:100px;"></div>
	<!-- info end -->
	
<!-- alert start  -->
<div class="zhezhao5"></div>
<div class="masklayers masklback" id="userInfo_alert">
	<h2 class="clearFloat"><span id="userInfo_alert_h"></span></h2>
	<div class="shenf_yanz_main">
		<p class="myp"><img id="userInfo_alert_img" src="" /><span id="userInfo_alert_p"></span></p>
		<div class="input_box_shenf myp2">
			<a href="javascript:;" data-id="close" id="queren"><button>确认</button></a>
		</div>
	</div>
</div>
<!-- alert end -->

<%@include file="authenticationShenfen.jsp"%>
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

<script type="text/javascript">
	// 学历下拉框处理
	function educationSelect(){
		var education = <c:if test="${empty education}">''</c:if><c:if test="${not empty education}">${education}</c:if>;
		$.post('${ctx}/constant/loadSelect?constantTypeCode=education&parentConstant=0&r=' + Math.random(),
       		{},
       		function(data){
       	    	if(data){
       	    		var optionStr = "";
       	    		for(var i=0;i < data.length;i++){
       	    			if(education == data[i].value){
       	    				optionStr += "<option value='"+data[i].value+"' selected='selected'>"+data[i].text+"</option>";	
       	    			}else{
       	    				optionStr += "<option value='"+data[i].value+"'>"+data[i].text+"</option>";	
       	    			}
       	    		}
       	    		$("#per-education").append(optionStr);
       	    	}
       	},'json');
	}
	
	// 省份下拉框处理
	function provinceSelect(){
		var province = <c:if test="${empty province}">''</c:if><c:if test="${not empty province}">${province}</c:if>;
		$.post('${ctx}/constant/loadProvince?r=' + Math.random(),
       		{},
       		function(data){
       	    	if(data){
       	    		var optionStr = "";
       	    		for(var i=0;i < data.length;i++){
       	    			if(province == data[i].value){
       	    				optionStr += "<option value='"+data[i].value+"' selected='selected'>"+data[i].text+"</option>";	
       	    			}else{
       	    				optionStr += "<option value='"+data[i].value+"'>"+data[i].text+"</option>";	
       	    			}
       	    		}
       	    		$("#per-province").append(optionStr);
       	    	}
       	},'json');
	}
	
	// 城市下拉框处理
	function citySelect(){
		var province = <c:if test="${empty province}">''</c:if><c:if test="${not empty province}">${province}</c:if>;
		var city = <c:if test="${empty city}">''</c:if><c:if test="${not empty city}">${city}</c:if>;
		if(province != '' && city != ''){
			$.post('${ctx}/constant/loadCity?pCityId=0&provinceId='+province+'&?r=' + Math.random(),
	       		{},
	       		function(data){
	       	    	if(data){
	       	    		var optionStr = "";
	       	    		for(var i=0;i < data.length;i++){
	       	    			if(city == data[i].value){
	       	    				optionStr += "<option value='"+data[i].value+"' selected='selected'>"+data[i].text+"</option>";	
	       	    			}else{
	       	    				optionStr += "<option value='"+data[i].value+"'>"+data[i].text+"</option>";	
	       	    			}
	       	    		}
	       	    		$("#per-city").append(optionStr);
	       	    	}
	       	},'json');
		}
	}
	
	// 省份选择事件
	function onProvinceSelect(){
		var province = $("#per-province").val();
		$("#per-city").empty().append("<option value=''>所在城市</option>");
		if(province != ""){
			$.post('${ctx}/constant/loadCity?pCityId=0&provinceId='+province+'&?r=' + Math.random(),
		       		{},
		       		function(data){
		       	    	if(data){
		       	    		var optionStr = "";
		       	    		for(var i=0;i < data.length;i++){
		       	    			optionStr += "<option value='"+data[i].value+"'>"+data[i].text+"</option>";	
		       	    		}
		       	    		$("#per-city").append(optionStr);
		       	    	}
		       	},'json');
		}
	}
	
	$(function(){
		educationSelect();
		provinceSelect();
		citySelect();
	});
</script>

</body>
</html>
