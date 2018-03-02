<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
  <div>
    <div style="width:60%;border-right: 1px none black;float:left;">
     <form class="form-horizontal" id="addBindSource_form">
      <div style="border-bottom: 1px none black;clear: both;">
        <fieldset style="height:285px;width: 100%;">
          <legend>抵押信息</legend>
          <c:if test="${not empty house }">
          	 <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 抵押类型：</label>
	            <div class="controls">
	                <c:if test="${house.mortgageType eq '1'}">一抵</c:if>
	                <c:if test="${house.mortgageType eq '2'}">二抵</c:if>
	            </div>
	          </div>
	          <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 房屋类型：</label>
	            <div class="controls">
	                <customUI:dictionaryTable constantTypeCode="houseType" desc="true" key="${house.houseType}"/>
	            </div>
	          </div>
	       <%--   <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 房屋性质：</label>
	              <div class="controls">
	                  ${loanApplication.offlineApplyCode}
	              </div>
	          </div>--%>
	          <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 房屋地址：</label>
	              <div class="controls">
	                  ${houseAdress.provinceStr}${houseAdress.cityStr}${houseAdress.districtStr}${houseAdress.detail}
	              </div>
	          </div>
	          <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 房屋面积：</label>
	              <div class="controls">
	                  ${house.houseSize}
	              </div>
	          </div>
	          <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 购买价格：</label>
	              <div class="controls">
	                  ${house.buyValue}
	              </div>
	          </div>
	          <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 市场价值：</label>
	              <div class="controls">
	                  ${house.marketValue}
	              </div>
	          </div>
	          <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 评估价值：</label>
	              <div class="controls">
	                  ${house.assessValue}
	              </div>
	          </div>
	          <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 房产证字号：</label>
	              <div class="controls">
	                  ${house.houseCardNumber}
	              </div>
	          </div>
	          <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 备注信息：</label>
	              <div class="controls">
	                  ${house.desc}
	              </div>
	          </div>
          </c:if>
          <c:if test="${not empty car }">
          	  <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 车辆型号：</label>
	              <div class="controls">
	                  ${car.carModel}
	              </div>
	          </div>
          	  <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 行驶里程：</label>
	              <div class="controls">
	                  ${car.mileage}
	              </div>
	          </div>
          	  <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 购买时间：</label>
	              <div class="controls">
					  <fmt:formatDate value="${car.buyTime}" pattern="yyyy-MM-dd"/>
	              </div>
	          </div>
          	  <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 产品金额：</label>
	              <div class="controls">
	                  ${car.carMoney}
	              </div>
	          </div>
          	  <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 市场估计价格：</label>
	              <div class="controls">
	                  ${car.appraisal}
	              </div>
	          </div>
          	  <div class="control-group">
	            <label class="control-label" style="padding-top: 0px;"> 抵押类型：</label>
	              <div class="controls">
	                  <c:if test="${car.pledgeType eq '1'}">一抵</c:if>
	                  <c:if test="${car.pledgeType eq '2'}">二抵</c:if>
	              </div>
	          </div>
          </c:if>
        </fieldset>
      </div>
  </div>
</body>
</html>
