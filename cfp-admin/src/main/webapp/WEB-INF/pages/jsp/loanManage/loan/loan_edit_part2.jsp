<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../../common/common.jsp" %>
<html>
<body>
<div id="loan_add_part2" class="container-fluid" style="padding: 10px 0px 0px 10px;width:50%;">
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="saveButtonEdit2();">
<form class="form-horizontal" id="loan_add_part2_form" method="post">
	<input type="hidden" id="loanApplicationId" name="loanApplicationId" value="${loanApplicationId}">
	<input type="hidden" id="loanApplicationName" name="loanApplicationName" value="${loan.loanApplicationName}">
	<table width="100%">
      <tr>
          <td>
          		<!-- 借款-开始 -->
          		<h4>借款信息[编辑]</h4>
          		<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款用途：</label>
                    <div class="controls">
                        <input style="width: 200px" class="easyui-combobox" required="true" 
                        	id="loanUseage" name="loanUseage">
                    </div>
                </div>
			    <div class="control-group">
					  <label class="control-label"><span style="color: red;">*</span>借款用途描述：</label>
					  <div class="controls">
								<textarea style="width: 450px; height: 100px;"
										  class="easyui-validatebox" required="true" validType="length[2,200]"
										  name="loanUseageDesc" id="loanUseageDesc">${loan.loanUseageDesc}</textarea>
					  </div>
				</div>
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款产品：</label>
                    <div class="controls">
                        <input name="loanProductId" id="loanProductId" style="width: 200px" >
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">年利率：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="annualRate"></span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">期限：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="dueTime"></span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">还款方式：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="repaymentType"></span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款金额：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-numberbox" required="true" validType="length[2,10]"
                        	name="loanBalance" id="loanBalance" value="${loan.loanBalance}"><a style="font-size: 12px;cursor: pointer;" id="showRepaymentPlan" onclick="showRepaymentPlan();">查看还款计划</a>
                        	<br/>
                        	<table id="feesItemList" class="table table-bordered table-condensed" style="font-size: 12px;width: 300px;">
                        	
                        	</table>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>描述：</label>
                    <div class="controls">
                        <textarea style="width: 450px; height: 100px;" 
                        		class="easyui-validatebox" required="true" validType="length[2,200]"
                                name="applicationDesc" id="applicationDesc">${loan.applicationDesc}</textarea>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">线下编号：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,30]"
                        	name="offlineApplyCode" id="offlineApplyCode" value="${loan.offlineApplyCode}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">借款人姓名：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="">${trueName}</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">借款人身份证号：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="">${idCard}</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">性别：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="">${sex}</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">出生日期：</label>
                    <div class="controls">
                        <span style="font-size: 12px;" id="">${birthday}</span>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>借款人手机：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-numberbox" required="true" validType="length[0,11]"
                        	name="mobilePhone" id="mobilePhone" value="${basic.mobilePhone}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">借款人邮箱：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-validatebox" validType="length[0,30]"
                        	name="email" id="email" value="${basic.email}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">最高学历：</label>
                    <div class="controls">
                        <input style="width: 200px" id="education" name="education">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">婚姻状况：</label>
                    <div class="controls">
                        <input style="width: 200px" id="isMarried" name="isMarried">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">有无子女：</label>
                    <div class="controls">
                        <input type="radio" id="childStatus" name="childStatus" value="1" ${basic.childStatus==1?'checked':''}><font style="font-size: 12px;">无</font>
                        <input type="radio" id="childStatus" name="childStatus" value="2" ${basic.childStatus==2?'checked':''}><font style="font-size: 12px;">有</font>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">资产情况：</label>
                    <div class="controls">
	                    <c:forEach items="${basic.assetsInfo}" var="assets">
	                    	<c:if test="${assets == 1}">
	                    		<c:set var="a1" value="1"/>
	                    	</c:if>
	                    	<c:if test="${assets == 2}">
	                    		<c:set var="a2" value="2"/>
	                    	</c:if>
	                    </c:forEach>
                        <input type="checkbox" id="assetsInfo" name="assetsInfo" value="1" ${a1==1?'checked':''}><font style="font-size: 12px;">有房</font>
                        <input type="checkbox" id="assetsInfo" name="assetsInfo" value="2" ${a2==2?'checked':''}><font style="font-size: 12px;">有车</font>
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">月均收入：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-numberbox" validType="length[0,10]"
                        	name="monthlyIncome" id="monthlyIncome" value="${basic.monthlyIncome}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">信用卡最高额度：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-numberbox" validType="length[0,10]"
                        	name="maxCreditValue" id="maxCreditValue" value="${basic.maxCreditValue}">
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">籍贯：</label>
                    <div class="controls" >
                        <select name="bornAddr_provence" id="bornAddr_provence" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty provinceInfos}">
	                        	<c:forEach items="${provinceInfos}" var="provinceInfo">
	                        		<c:if test="${provinceInfo.provinceId == bornAddr.province}">
	                        			<option value="${provinceInfo.provinceId}" selected="selected">${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        		<c:if test="${provinceInfo.provinceId != bornAddr.province}">
	                        			<option value="${provinceInfo.provinceId}" >${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        <span style="font-size: 12px;">省</span>
                        <select name="bornAddr_city" id="bornAddr_city" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty bornCitys}">
	                        	<c:forEach items="${bornCitys}" var="bornCity">
	                        		<c:if test="${bornCity.cityId == bornAddr.city}">
	                        			<option value="${bornCity.cityId}" selected="selected">${bornCity.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${bornCity.cityId != bornAddr.city}">
	                        			<option value="${bornCity.cityId}" >${bornCity.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        <span style="font-size: 12px;">市</span>
                        <select name="bornAddr_district" id="bornAddr_district" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty bornDistricts}">
	                        	<c:forEach items="${bornDistricts}" var="bornDistrict">
	                        		<c:if test="${bornDistrict.cityId == bornAddr.district}">
	                        			<option value="${bornDistrict.cityId}" selected="selected">${bornDistrict.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${bornDistrict.cityId != bornAddr.district}">
	                        			<option value="${bornDistrict.cityId}" >${bornDistrict.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        <span style="font-size: 12px;">区/县</span><br/>
                        <input type="text" style="width: 440px;margin-top: 3px;" class="easyui-validatebox" validType="length[2,50]" 
                        name="bornAddr_detail" id="bornAddr_detail" value="${bornAddr.detail}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">户口所在地：</label>
                    <div class="controls" >
                        <select name="registAddr_provence" id="registAddr_provence" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty provinceInfos}">
	                        	<c:forEach items="${provinceInfos}" var="provinceInfo">
	                        		<c:if test="${provinceInfo.provinceId == registAddr.province}">
	                        			<option value="${provinceInfo.provinceId}" selected="selected">${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        		<c:if test="${provinceInfo.provinceId != registAddr.province}">
	                        			<option value="${provinceInfo.provinceId}" >${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        <span style="font-size: 12px;">省</span>
                        <select name="registAddr_city" id="registAddr_city" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty registCitys}">
	                        	<c:forEach items="${registCitys}" var="registCity">
	                        		<c:if test="${registCity.cityId == registAddr.city}">
	                        			<option value="${registCity.cityId}" selected="selected">${registCity.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${registCity.cityId != registAddr.city}">
	                        			<option value="${registCity.cityId}" >${registCity.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        <span style="font-size: 12px;">市</span>
                        <select name="registAddr_district" id="registAddr_district" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty registDistricts}">
	                        	<c:forEach items="${registDistricts}" var="registDistrict">
	                        		<c:if test="${registDistrict.cityId == registAddr.district}">
	                        			<option value="${registDistrict.cityId}" selected="selected">${registDistrict.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${registDistrict.cityId != registAddr.district}">
	                        			<option value="${registDistrict.cityId}" >${registDistrict.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        <span style="font-size: 12px;">区/县</span><br/>
                        <input type="text" style="width: 440px;margin-top: 3px;" class="easyui-validatebox" validType="length[2,50]" 
                        name="registAddr_detail" id="registAddr_detail" value="${registAddr.detail}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">现住址：</label>
                    <div class="controls" >
                        <select name="residenceAddr_provence" id="residenceAddr_provence" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty provinceInfos}">
	                        	<c:forEach items="${provinceInfos}" var="provinceInfo">
	                        		<c:if test="${provinceInfo.provinceId == residenceAddr.province}">
	                        			<option value="${provinceInfo.provinceId}" selected="selected">${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        		<c:if test="${provinceInfo.provinceId != residenceAddr.province}">
	                        			<option value="${provinceInfo.provinceId}" >${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        <span style="font-size: 12px;">省</span>
                        <select name="residenceAddr_city" id="residenceAddr_city" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty residenceCitys}">
	                        	<c:forEach items="${residenceCitys}" var="residenceCity">
	                        		<c:if test="${residenceCity.cityId == residenceAddr.city}">
	                        			<option value="${residenceCity.cityId}" selected="selected">${residenceCity.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${residenceCity.cityId != residenceAddr.city}">
	                        			<option value="${residenceCity.cityId}" >${residenceCity.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        <span style="font-size: 12px;">市</span>
                        <select name="residenceAddr_district" id="residenceAddr_district" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty residenceDistricts}">
	                        	<c:forEach items="${residenceDistricts}" var="residenceDistrict">
	                        		<c:if test="${residenceDistrict.cityId == residenceAddr.district}">
	                        			<option value="${residenceDistrict.cityId}" selected="selected">${residenceDistrict.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${residenceDistrict.cityId != residenceAddr.district}">
	                        			<option value="${residenceDistrict.cityId}" >${residenceDistrict.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        <span style="font-size: 12px;">区/县</span><br/>
                        <input type="text" style="width: 440px;margin-top: 3px;" class="easyui-validatebox" validType="length[2,50]" 
                        name="residenceAddr_detail" id="residenceAddr_detail" value="${residenceAddr.detail}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">现住址邮政编码：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,6]"
                        	name="zipcode" id="zipcode" value="${basic.zipcode}">
                    </div>
                </div>
                <!-- 借款-结束 -->
                
                <hr><!-- ----------------------------------------------------------------------------- -->
                
                <!-- 风控-开始 -->
                <h4>风控步骤</h4>
                
                <div class="control-group">
                    <label class="control-label"><span style="color: red;">*</span>风控步骤：</label>
                    <div class="controls">
                        <textarea style="width: 450px; height: 100px;" 
                        		class="easyui-validatebox" required="true" validType="length[2,1500]"
                                name="riskControlInformation" id="riskControlInformation">${loan.riskControlInformation}</textarea>
                    </div>
                </div>
                <!-- 风控-结束 -->
                
                <hr><!-- ----------------------------------------------------------------------------- -->
                
                <!-- 工作-开始 -->
                <h4>工作信息</h4>
                
				<div class="control-group">
                    <label class="control-label">单位性质：</label>
                    <div class="controls">
                        <input style="width: 200px" id="companyNature" name="companyNature">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">单位名称：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-validatebox" validType="length[2,50]"
                        	name="companyName" id="companyName" value="${work.companyName}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">单位地址：</label>
                    <div class="controls" >
                        <select name="workingAddr_provence" id="workingAddr_provence" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty provinceInfos}">
	                        	<c:forEach items="${provinceInfos}" var="provinceInfo">
	                        		<c:if test="${provinceInfo.provinceId == workingAddr.province}">
	                        			<option value="${provinceInfo.provinceId}" selected="selected">${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        		<c:if test="${provinceInfo.provinceId != workingAddr.province}">
	                        			<option value="${provinceInfo.provinceId}" >${provinceInfo.provinceName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        
                        <span style="font-size: 12px;">省</span>
                        <select name="workingAddr_city" id="workingAddr_city" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty workingCitys}">
	                        	<c:forEach items="${workingCitys}" var="workingCity">
	                        		<c:if test="${workingCity.cityId == workingAddr.city}">
	                        			<option value="${workingCity.cityId}" selected="selected">${workingCity.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${workingCity.cityId != workingAddr.city}">
	                        			<option value="${workingCity.cityId}" >${workingCity.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        
                        <span style="font-size: 12px;">市</span>
                        <select name="workingAddr_district" id="workingAddr_district" style="width: 130px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty workingDistricts}">
	                        	<c:forEach items="${workingDistricts}" var="workingDistrict">
	                        		<c:if test="${workingDistrict.cityId == workingAddr.district}">
	                        			<option value="${workingDistrict.cityId}" selected="selected">${workingDistrict.cityName}</option>
	                        		</c:if>
	                        		<c:if test="${workingDistrict.cityId != workingAddr.district}">
	                        			<option value="${workingDistrict.cityId}" >${workingDistrict.cityName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        
                        <span style="font-size: 12px;">区/县</span><br/>
                        <input type="text" style="width: 440px;margin-top: 5px;" class="easyui-validatebox" validType="length[2,50]" 
                        name="workingAddr_detail" id="workingAddr_detail" value="${workingAddr.detail}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">单位电话：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px;margin-top: 3px;"
                        	class="easyui-validatebox" validType="length[0,20]"
                        	name="companyPhone" id="companyPhone" value="${work.companyPhone}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">职务：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-validatebox" validType="length[0,30]"
                        	name="post" id="post" value="${work.post}">
                    </div>
                </div>
                
				<div class="control-group">
                    <label class="control-label">入职时间：</label>
                    <div class="controls">
                        <input type="text" style="width: 200px"
                        	class="easyui-datebox"
                        	name="joinDate" id="joinDate" value="<fmt:formatDate value="${work.joinDate}" pattern="yyyy-MM-dd"/>">
                    </div>
                </div>
                <!-- 工作-结束 -->
                
				<hr><!-- ----------------------------------------------------------------------------- -->
                
                <!-- 联系人-开始 -->
                <h4>联系人信息</h4>
                
                <!-- 联系人数组隐藏域 -->
                <input type="hidden" id="controlsArray" name="controlsArray">
                
                <!-- 联系人单元块-开始 -->
				<div class="control-group" id="contacts">
                    <label class="control-label">关系：</label>
                    <div class="controls" >
                        <select id="relationType" name="relationType" style="width: 100px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty relationTypes}">
	                        	<c:forEach items="${relationTypes}" var="relationType">
	                        		<c:if test="${relationType.constantValue == contactsSnapshot.relationType}">
	                        			<option value="${relationType.constantValue}" selected="selected">${relationType.constantName}</option>
	                        		</c:if>
	                        		<c:if test="${relationType.constantValue != contactsSnapshot.relationType}">
	                        			<option value="${relationType.constantValue}" >${relationType.constantName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        
                        <select id="relation" name="relation" style="width: 100px">
                        	<option value="">请选择</option>
                        	<c:if test="${not empty relations}">
	                        	<c:forEach items="${relations}" var="relation">
	                        		<c:if test="${relation.constantValue == contactsSnapshot.relation}">
	                        			<option value="${relation.constantValue}" selected="selected">${relation.constantName}</option>
	                        		</c:if>
	                        		<c:if test="${relation.constantValue != contactsSnapshot.relation}">
	                        			<option value="${relation.constantValue}" >${relation.constantName}</option>
	                        		</c:if>
	                        	</c:forEach>
	                        </c:if>
                        </select>
                        
                        <span style="font-size: 12px;">姓名：</span>
                        <input type="text" style="width: 100px" class="easyui-validatebox" validType="length[0,10]" 
                        name="concactName" id="concactName" value="${contactsSnapshot.concactName}">
                        <span style="font-size: 12px;">手机号：</span>
                        <input type="text" style="width: 100px" class="easyui-validatebox" validType="length[0,11]" 
                        name="concatPhone" id="concatPhone" value="${contactsSnapshot.concatPhone}">
                        <i class="icon-plus" style="margin-left: 5px;cursor: pointer;" title="添加联系人" onclick="onContacts_plus()"></i>
                    </div>
                </div>
                <!-- 联系人单元块-结束 -->
                
                <!-- 联系人-结束 -->
                
				<hr id="contacts_end"><!-- ----------------------------------------------------------------------------- -->
                
                <!-- 银行卡-开始 -->
                <h4>银行卡信息</h4>
                
                <!-- 借款标情况-开始 -->
                <div id="div_loanMark">
					<div class="control-group">
	                    <label class="control-label"><span style="color: red;">*</span>银行：</label>
	                    <div class="controls" >
							<input type="hidden"  id="bankCode" name="bankCode" value="${card.bankCode }"/>
	                    <%--    <input style="width: 200px" id="bankCode" name="bankCode">--%>
							<c:if test="${card.bankCode  eq '403'}">  <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国邮政储蓄银行股份有限公司" disabled="disabled">
							</c:if>

							<c:if test="${card.bankCode  eq '102'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国工商银行 " disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '103'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国农业银行 " disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '104'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国银行 " disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '105'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国建设银行" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '301'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="交通银行 " disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '302'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中信银行 " disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '303'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国光大银行" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '304'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="华夏银行" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '305'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="中国民生银行" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '306'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="广东发展银行" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '307'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="平安银行股份有限公司" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '308'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="招商银行" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '309'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="兴业银行" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '310'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="上海浦东发展银行" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '319'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="徽商银行" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '313'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="其他城市商业银行 " disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '314'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="其他农村商业银行" disabled="disabled"></c:if>
							<c:if test="${card.bankCode  eq '315'}">    <input type="text" style="width: 200px" id="bankName" name="bankName" value="恒丰银行" disabled="disabled"></c:if>

						</div>
	                </div>
	                
	                <div class="control-group">
	                    <label class="control-label"><span style="color: red;">*</span>开户行：</label>
	                    <div class="controls">
	                       	<input type="text" style="width: 355px;"
	                        	class="easyui-validatebox" required="true" validType="length[0,50]"
	                        	name="registeredBank" id="registeredBank" value="${card.registeredBank}">
	                    </div>
	                </div>
	                
					<div class="control-group">
	                    <label class="control-label"><span style="color: red;">*</span>卡号：</label>
	                    <div class="controls">
	                        <input type="text" style="width: 200px;"
	                        	class="easyui-validatebox" required="true" validType="length[0,30]"
	                        	name="cardCode" id="cardCode" value="${card.cardCode}">
	                    </div>
	                </div>
	                
					<div class="control-group">
	                    <label class="control-label"><span style="color: red;">*</span>开户名：</label>
	                    <div class="controls">
	                        <input type="text" style="width: 200px"
	                        	class="easyui-validatebox" required="true" validType="length[0,30]"
	                        	name="cardCustomerName" id="cardCustomerName" value="${card.cardcustomerName}">
	                    </div>
	                </div>
                </div>
                <!-- 借款标情况-结束 -->
                
                <!-- 债权标情况-开始 -->
                <div id="div_rightsMark">
					<div class="control-group">
	                    <label class="control-label"><span style="color: red;">*</span>打款卡：</label>
	                    <div class="controls" >
	                        <input name="inCardId" id="inCardId" style="width: 200px" >
	                    </div>
	                </div>
	                
					<div class="control-group">
	                    <label class="control-label"><span style="color: red;">*</span>划扣卡：</label>
	                    <div class="controls" >
	                        <input name="outCardId" id="outCardId" style="width: 200px" >
	                    </div>
	                </div>
                </div>
                <!-- 债权标情况-结束 -->
                <!-- 银行卡-结束 -->
          </td>
      </tr>
	</table>
</form>
<input style="float: right;width: 100px" type="button" class="btn  btn-primary"  value="保 存" onclick="saveButtonEdit2();">
</div>

<script type="text/javascript">

// 执行:保存。
function saveButtonEdit2(){
	
	// 验证
	if(!$("#loan_add_part2_form").form('validate')){
		return false;
	}
	
	// 单独获取联系人信息。
	var contactsArray = new Array();
	var c = {};
	c.relationType = $("#relationType").combobox("getValue");	
	c.relation = $("#relation").combobox("getValue");	
	c.concactName = $("#concactName").val();	
	c.concatPhone = $("#concatPhone").val();
	contactsArray[0] = c;
	if(i > 0){
		for(var j=1;j <= i;j++){
			var cs = {};
			var isE = false;
			if($("#relationType_" + j).length > 0){
				cs.relationType = $("#relationType_" + j).combobox("getValue");
				isE = true;
			}
			if($("#relation_" + j).length > 0){
				cs.relation = $("#relation_" + j).combobox("getValue");
				isE = true;
			}
			if($("#concactName_" + j).length > 0){
				cs.concactName = $("#concactName_" + j).val();
				isE = true;
			}
			if($("#concatPhone_" + j).length > 0){
				cs.concatPhone = $("#concatPhone_" + j).val();
				isE = true;
			}
			if(isE==true){
				contactsArray[j] = cs;	
			}
		}	
	}
	// 将联系人数组信息存入隐藏区域。
	$("#controlsArray").attr("value", JSON.stringify(contactsArray));
	
	// 获取下列数值的文字信息。
	var loanUseage_Str = $("#loanUseage").combobox("getText");//借款用途
	var loanProductId_Str = $("#loanProductId").combobox("getText");//借款产品
	var dueTime_Str = $("#dueTime").html();//期限
	var loanApplicationName_Str = loanUseage_Str + '-' + loanProductId_Str + '-' + dueTime_Str;//借款合同名称
	$("#loanApplicationName").attr("value",loanApplicationName_Str);
	
	$.post('${ctx}/jsp/loanManage/loan/save_loan_part2?r=' + Math.random(),
   		$("#loan_add_part2_form").serialize(),
   		function(data){
   	    	if(data.result == 'success'){
   	    		$.messager.alert("操作提示", "保存成功！", "info");
   	    		
   	    	}else if(data.result == 'error'){
   	    		if(data.errCode == 'check'){
   	    			$.messager.alert("验证提示", data.errMsg, "info");
   	    		}else{
   	    			$.messager.alert("系统提示", data.errMsg, "info");
   	    		}
   	    	}else{
   	    		$.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
   	    	}
   	 },'json');
	
}

/* 借款信息-开始  */

	// 加载借款用途下拉框。
	$("#loan_add_part2_form #loanUseage").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=loanUseage&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE',
	    onLoadSuccess: function (record) {
	    	$("#loan_add_part2_form #loanUseage").combobox("select", '${loan.loanUseage}');
	    }
	});
	
	// 借款产品下拉框。
	$("#loan_add_part2_form #loanProductId").combobox({
	    url: '${ctx}/jsp/product/loan/loadLoanProduct?selectedDisplay=selected',
	    textField: 'PRODUCTNAME',
	    valueField: 'LOANPRODUCTID',
	    onLoadSuccess: function (record) {
	    	$("#loan_add_part2_form #loanProductId").combobox("select", '${loan.loanProductId}');
	    	showLoanProductDetail(${loan.loanProductId});
	    },
	    onSelect: function (record) {
	    	showLoanProductDetail(record.LOANPRODUCTID);
	    }
	});
	
	// 展示借款产品的详细
	function showLoanProductDetail(loanproductid){
		if(undefined != loanproductid && loanproductid != ''){
			$.post('${ctx}/jsp/product/loan/getLoanProductDetail',
	   	   		{
	   				loanproductid:loanproductid
	   	   		},
	   	   		function(data){
	   	   	    	if(data.result == 'success'){
	   	   	    		
	   	   	 			// 单项填充
	   	   	    		$("#annualRate").html(data.data.annualRate + '%');//年利率
	   	   	    		$("#dueTime").html(data.data.dueTime);//期限时长
	   	   	    		$("#repaymentType").html(data.data.repaymentType);//还款方式
	   	   	    		
	   	   	    		// 列表填充
	   	   	    		$("#feesItemList").empty();// 先清空。
	   	   	    		$.each(data.data.feesItemList,function(n,v){
	   	   	    			var f = "<tr><td>"+ v.itemType +"</td><td>"+ v.chargeCycle +"</td><td>"+ v.itemName +"</td></tr>";
	   	   	    			$("#feesItemList").append(f);
	   	   	    		});
	   	   	    		
	   	   	    	}else if(data.result == 'error'){
	   	   	    		//$.messager.alert("系统提示", data.errMsg, "info");
	   	   	    	}else{
	   	   	    		//$.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
	   	   	    	}
	   	   	 },'json');
		}
	}

	// 加载最高学历下拉框。
	$("#loan_add_part2_form #education").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=education&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE',
	    onLoadSuccess: function (record) {
	    	$("#loan_add_part2_form #education").combobox("select", '${basic.education}');
	    }
	});
	
	// 加载婚姻状况下拉框。
	$("#loan_add_part2_form #isMarried").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=isMarried&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE',
    	onLoadSuccess: function (record) {
	    	$("#loan_add_part2_form #isMarried").combobox("select", '${basic.isMarried}');
	    }
	});
	
	//加载单位性质下拉框。
	$("#loan_add_part2_form #companyNature").combobox({
	    url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=companyNature&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE',
	    onLoadSuccess: function (record) {
	    	$("#loan_add_part2_form #companyNature").combobox("select", '${work.companyNature}');
	    }
	});
	
	//加载开户行下拉框。
	<%--$("#loan_add_part2_form #bankCode").combobox({--%>
	    <%--url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=bank&parentConstant=0&selectedDisplay=selected',--%>
	    <%--textField: 'CONSTANTNAME',--%>
	    <%--valueField: 'CONSTANTID',--%>
	    <%--onLoadSuccess: function (record) {--%>
	    	<%--$("#loan_add_part2_form #bankCode").combobox("select", '${card.bankCode}');--%>
	    <%--}--%>
	<%--});--%>
	
	//加载关系类型下拉框。
	$("#loan_add_part2_form #relationType").combobox({
	    //url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=relationType&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE',
	    //idField: 'CONSTANTID',
	    onSelect: function (record) {
	    	var relationTypeValue = $("#loan_add_part2_form #relationType").combobox("getValue")
	    	$.post('${ctx}/jsp/constant/getConstantDefineId',
   	   		{
	    		constantTypeCode:'relationType',
	    		parentConstant:0,
	    		constantValue:relationTypeValue
   	   		},
   	   		function(data){
   	   	    	if(data){
	   	   	    	$("#loan_add_part2_form #relation").combobox("reload",
	   	 	        	'${ctx}/jsp/constant/loadSelect?constantTypeCode=relation&parentConstant=' + data.constantDefineId + '&selectedDisplay=selected');
   	   	    	}
   	   	    },'json');
	    }
	});
	
	//加载关系下拉框。
	$("#loan_add_part2_form #relation").combobox({
	    //url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=relation&parentConstant=0&selectedDisplay=selected',
	    textField: 'CONSTANTNAME',
	    valueField: 'CONSTANTVALUE'
	});
	
	//加载省份下拉框【籍贯】。
	$("#loan_add_part2_form #bornAddr_provence").combobox({
		//url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
	    textField: 'PROVINCENAME',
	    valueField: 'PROVINCEID',
	    onSelect: function (record) {
	        $("#loan_add_part2_form #bornAddr_city").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
	    }
	});
	//加载城市下拉框【籍贯】。
	$("#loan_add_part2_form #bornAddr_city").combobox({
		//url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID',
	    onSelect: function (record) {
	    	var provinceId = $("#bornAddr_provence").combobox("getValue");
	        $("#loan_add_part2_form #bornAddr_district").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?provinceId=' + provinceId + '&pCityId=' + record.CITYID + '&selectedDisplay=selected');
	    }
	});
	//加载区县下拉框【籍贯】。
	$("#loan_add_part2_form #bornAddr_district").combobox({	
		//url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID'
	});
	
	//加载省份下拉框【户口所在地】。
	$("#loan_add_part2_form #registAddr_provence").combobox({
	    //url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
	    textField: 'PROVINCENAME',
	    valueField: 'PROVINCEID',
	    onSelect: function (record) {
	        $("#loan_add_part2_form #registAddr_city").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
	    }
	});
	//加载城市下拉框【户口所在地】。
	$("#loan_add_part2_form #registAddr_city").combobox({
	    //url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID',
	    onSelect: function (record) {
	    	var provinceId = $("#registAddr_provence").combobox("getValue");
	        $("#loan_add_part2_form #registAddr_district").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?provinceId=' + provinceId + '&pCityId=' + record.CITYID + '&selectedDisplay=selected');
	    }
	});
	//加载区县下拉框【户口所在地】。
	$("#loan_add_part2_form #registAddr_district").combobox({
	    //url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID'
	});
	
	//加载省份下拉框【现住址】。
	$("#loan_add_part2_form #residenceAddr_provence").combobox({
	    //url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
	    textField: 'PROVINCENAME',
	    valueField: 'PROVINCEID',
	    onSelect: function (record) {
	        $("#loan_add_part2_form #residenceAddr_city").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
	    }
	});
	//加载城市下拉框【现住址】。
	$("#loan_add_part2_form #residenceAddr_city").combobox({
	    //url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID',
	    onSelect: function (record) {
	    	var provinceId = $("#residenceAddr_provence").combobox("getValue");
	        $("#loan_add_part2_form #residenceAddr_district").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?provinceId=' + provinceId + '&pCityId=' + record.CITYID + '&selectedDisplay=selected');
	    }
	});
	//加载区县下拉框【现住址】。
	$("#loan_add_part2_form #residenceAddr_district").combobox({
	    //url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID'
	});

/* 借款信息-结束  */

/* 工作信息-开始 */
	
	//加载省份下拉框【工作】。
	$("#loan_add_part2_form #workingAddr_provence").combobox({
	    //url: '${ctx}/jsp/constant/loadProvince?selectedDisplay=selected',
	    textField: 'PROVINCENAME',
	    valueField: 'PROVINCEID',
	    onSelect: function (record) {
	        $("#loan_add_part2_form #workingAddr_city").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=' + record.PROVINCEID + '&selectedDisplay=selected');
	    }
	});
	//加载城市下拉框【工作】。
	$("#loan_add_part2_form #workingAddr_city").combobox({
	    //url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID',
	    onSelect: function (record) {
	    	var provinceId = $("#workingAddr_provence").combobox("getValue");
	        $("#loan_add_part2_form #workingAddr_district").combobox("reload",
	        	'${ctx}/jsp/constant/loadCity?provinceId=' + provinceId + '&pCityId=' + record.CITYID + '&selectedDisplay=selected');
	    }
	});
	//加载区县下拉框【工作】。
	$("#loan_add_part2_form #workingAddr_district").combobox({
	    //url: '${ctx}/jsp/constant/loadCity?pCityId=0&provinceId=0&selectedDisplay=selected',
	    textField: 'CITYNAME',
	    valueField: 'CITYID'
	});
	
/* 工作信息-结束 */

/* 联系人-开始 */
 	// 执行：添加联系人。
 	var i = 0;
	function onContacts_plus(){
		i += 1;
		var c = "<div class='control-group' id='contacts_" + i + "'>" +
	        "<label class='control-label'>关系：</label>" +
	        "<div class='controls' >" +
	            "<input style='width: 100px' name='relationType_" + i + "' id='relationType_" + i + "'>&nbsp;" +
                "<input style='width: 100px' name='relation_" + i + "' id='relation_" + i + "'>" +
	            "<span style='font-size: 12px;'>&nbsp;姓名：&nbsp;&nbsp;</span>" +
	            "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[2,50]' " +
	            "name='concactName_" + i + "' id='concactName_" + i + "'>" +
	            "<span style='font-size: 12px;'>&nbsp;&nbsp;手机号：&nbsp;</span>" +
	            "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[2,50]' " +
	            "name='concatPhone_" + i + "' id='concatPhone_" + i + "'>" +
	            "<i class='icon-trash' style='margin-left: 5px;cursor: pointer;' title='删除联系人' onclick='onContacts_trash(" + i + ");'></i>" +
	        "</div>" +
	    "</div>";
		$("#contacts_end").before(c);
		
	    //加载关系类型下拉框。
	    $("#loan_add_part2_form #relationType_" + i).combobox({
	        url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=relationType&parentConstant=0&selectedDisplay=selected',
	        textField: 'CONSTANTNAME',
	        valueField: 'CONSTANTVALUE',
	        idField: 'CONSTANTID',
	        onSelect: function (record) {
	            $("#loan_add_part2_form #relation_" + i).combobox("reload",
	            	'${ctx}/jsp/constant/loadSelect?constantTypeCode=relation&parentConstant=' + record.CONSTANTID + '&selectedDisplay=selected');
	        }
	    });

	    //加载关系下拉框。
	    $("#loan_add_part2_form #relation_" + i).combobox({
	        url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=relation&parentConstant=0&selectedDisplay=selected',
	        textField: 'CONSTANTNAME',
	        valueField: 'CONSTANTVALUE'
	    });
		
	}
	
	// 执行：删除联系人。
	function onContacts_trash(contacts_num){
		$("#contacts_" + contacts_num).remove();
	}
	
	// 执行：联系人加载回显。
	function loadShowContacts(){
		var relationTypeJsonArray = ${relationTypeJsonArray};
		var relationsJsonArray_207 = ${relationsJsonArray_207};
		var relationsJsonArray_208 = ${relationsJsonArray_208};
		var relationsJsonArray_209 = ${relationsJsonArray_209};
		
		var contactsSnapshots = ${contactsSnapshots};
		if(null != contactsSnapshots && contactsSnapshots.length > 0){
			for(var ic=0;ic < contactsSnapshots.length;ic++){
				
				var relationType = contactsSnapshots[ic].relationType;
				var relation = contactsSnapshots[ic].relation;
				var concactName = '';
				var concatPhone = '';
				if(undefined != contactsSnapshots[ic].concactName){
					concactName = contactsSnapshots[ic].concactName;
				}
				if(undefined != contactsSnapshots[ic].concatPhone){
					concatPhone = contactsSnapshots[ic].concatPhone;
				}
				
				var relationsJsonArray = null;
				if(relationType == 1){
					relationsJsonArray = relationsJsonArray_207;
				}else if(relationType == 2){
					relationsJsonArray = relationsJsonArray_208;
				}else if(relationType == 3){
					relationsJsonArray = relationsJsonArray_209;
				}
				
				i += 1;// 增加全局变量i的值。
				
				var c = "<div class='control-group' id='contacts_" + i + "'>" ;
		        	c += "<label class='control-label'>关系：</label>" ;
		        	c += "<div class='controls' >" ;
		        
	                c += "<select id='relationType_" + i + "' name='relationType_" + i + "' style='width: 100px'>" ;
	                c += "<option value=''>请选择</option>" ;
		        		
	                	if(null != relationTypeJsonArray && relationTypeJsonArray.length > 0){
		        			for(var it=0;it < relationTypeJsonArray.length;it++){
		        				if(relationTypeJsonArray[it].constantValue == relationType){
		        					c += "<option value='" + relationTypeJsonArray[it].constantValue + "' selected='selected'>" + relationTypeJsonArray[it].constantName + "</option>" ;
		        				}else{
		        					c += "<option value='" + relationTypeJsonArray[it].constantValue + "' >" + relationTypeJsonArray[it].constantName + "</option>" ;
		        				}
		        			}
		        		}
	                c += "</select>" ;
	                
					c += "<select id='relation_" + i + "' name='relation_" + i + "' style='width: 100px'>" ;
					c += "<option value=''>请选择</option>" ;
					
						if(null != relationsJsonArray && relationsJsonArray.length > 0){
		        			for(var it=0;it < relationsJsonArray.length;it++){
		        				if(relationsJsonArray[it].constantValue == relation){
		        					c += "<option value='" + relationsJsonArray[it].constantValue + "' selected='selected'>" + relationsJsonArray[it].constantName + "</option>" ;
		        				}else{
		        					c += "<option value='" + relationsJsonArray[it].constantValue + "' >" + relationsJsonArray[it].constantName + "</option>" ;
		        				}
		        			}
		        		}
	                c += "</select>" ;
	                
	                c += "<span style='font-size: 12px;'>&nbsp;姓名：&nbsp;&nbsp;</span>" ;
	                c += "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[2,50]' " ;
	                c += "name='concactName_" + i + "' id='concactName_" + i + "' value='" + concactName + "'>" ;
	                c += "<span style='font-size: 12px;'>&nbsp;&nbsp;手机号：&nbsp;</span>" ;
	                c += "<input type='text' style='width: 100px' class='easyui-validatebox' validType='length[2,50]' " ;
	                c += "name='concatPhone_" + i + "' id='concatPhone_" + i + "' value='" + concatPhone + "'>" ;
	                c += "<i class='icon-trash' style='margin-left: 5px;cursor: pointer;' title='删除联系人' onclick='onContacts_trash(" + i + ");'></i>" ;
	                c += "</div>" ;
	                c += "</div>";
				$("#contacts_end").before(c);
				
			    //加载关系类型下拉框。
			    $("#loan_add_part2_form #relationType_" + i).combobox({
			        //url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=relationType&parentConstant=0&selectedDisplay=selected',
			        textField: 'CONSTANTNAME',
			        valueField: 'CONSTANTVALUE',
			        //idField: 'CONSTANTID',
			        onSelect: function (record) {
			        	var CONSTANTID = 0;
			        	if(record.CONSTANTVALUE == 1){
			        		CONSTANTID = 207;
			        	}else if(record.CONSTANTVALUE == 2){
			        		CONSTANTID = 208;
			        	}else if(record.CONSTANTVALUE == 3){
			        		CONSTANTID = 209;
			        	}
			            $("#loan_add_part2_form #relation_" + i).combobox("reload",
			            	'${ctx}/jsp/constant/loadSelect?constantTypeCode=relation&parentConstant=' + CONSTANTID + '&selectedDisplay=selected');
			        }
			    });

			    //加载关系下拉框。
			    $("#loan_add_part2_form #relation_" + i).combobox({
			        //url: '${ctx}/jsp/constant/loadSelect?constantTypeCode=relation&parentConstant=0&selectedDisplay=selected',
			        textField: 'CONSTANTNAME',
			        valueField: 'CONSTANTVALUE'
			    });
				
			}
		}
		
	}
/* 联系人-结束 */

/* 银行卡信息-开始 */
 	// 执行：银行卡初始化事件。
 	function intoMark(){
		if(1 == ${subjectType}){// 借款标
			$("#div_loanMark").show();
			$("#div_rightsMark").hide();
		}else{// 债权标
			$("#div_loanMark").hide();
			$("#div_rightsMark").show();
		}
	}
	
 	// 执行：改变银行卡表单验证规则。
	function intoValidate(){
		if(1 == ${subjectType}){
			// 去掉
			// inCardId
			// outCardId
		}else{
			// 去掉
			// bankCode
			// registeredBank
			$('#registeredBank').validatebox({
    			required: false
			});
			// cardCode
			$('#cardCode').validatebox({
    			required: false
			});
			// cardCustomerName
			$('#cardCustomerName').validatebox({
    			required: false
			});
		}
	}
 	
	if(1 != ${subjectType}){
		//加载打款卡下拉框。
		$("#loan_add_part2_form #inCardId").combobox({
		    url: '${ctx}/jsp/custom/customer/loadCustomerCard?selectedDisplay=selected&originalUserId=${originalUserId}',
		    textField: 'CARDCUSTOMERNAME',
		    valueField: 'CUSTOMERCARDID',
		    onLoadSuccess: function (record) {
		    	$("#loan_add_part2_form #inCardId").combobox("select", '${loan.inCardId}');
		    }
		});
		
		//加载划扣卡下拉框。
		$("#loan_add_part2_form #outCardId").combobox({
		    url: '${ctx}/jsp/custom/customer/loadCustomerCard?selectedDisplay=selected&originalUserId=${originalUserId}',
		    textField: 'CARDCUSTOMERNAME',
		    valueField: 'CUSTOMERCARDID',
		    onLoadSuccess: function (record) {
		    	$("#loan_add_part2_form #outCardId").combobox("select", '${loan.outCardId}');
		    }
		});
	}
	
	// 展示还款计划列表
 	function showRepaymentPlan(){
 	    if ($("#loanProductId").combobox("getValue") && $("#loanBalance").val()) {
 	    	$("#showRepaymentPlan").after("<div id='repaymentPlan' style=' padding:10px; '></div>");
 	        $("#repaymentPlan").dialog({
 	            resizable: false,
 	            title: '还款计划',
 	            href: '${ctx}/jsp/loanManage/loan/toShowRepaymentPlan?loanProductId='+ $("#loanProductId").combobox("getValue") +'&balance=' + $("#loanBalance").val(),
 	            width: 600,
 	            modal: true,
 	            height: 300,
 	            top: 100,
 	            left: 400,
 	            buttons: [
 	                {
	                    text: '取消',
	                    iconCls: 'icon-cancel',
	                    handler: function () {
	                        $("#repaymentPlan").dialog('close');
	                    }
                	}
 	            ],
 	            onClose: function () {
 	                $("#repaymentPlan").dialog('destroy');
 	            }
 	        });
 	    }else{
 	    	$.messager.alert("验证提示", "请选择借款产品并填写借款金额", "info");
 	    }
 	}
	
/* 银行卡信息-结束 */

$(function(){
	intoMark();
	intoValidate();
	loadShowContacts();
	
});

</script>
</body>
</html>
