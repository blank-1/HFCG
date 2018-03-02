<%@ page import="com.xt.cfp.core.constants.EnterpriseTypeEnum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<%
	EnterpriseTypeEnum[] values = EnterpriseTypeEnum.values();
	request.setAttribute("EnterpriseTypeEnums",values);
%>
<body>
<div id="enterprise_baseInfo" class="container-fluid" style="padding: 50px 0px 0px 10px;width:50%;">
<form class="form-horizontal" id="enterprise_baseInfo_form" method="post">
	<table width="100%" style="font-size: 12px;">
		
	  <tr>
      	<td colspan="2" style="font-weight: bold;">
      		企业信息：<br/><br/>
      	</td>
      </tr>
      
      <tr>
          <td>
          		
				<div class="control-group">
                    <label class="control-label">公司类型：</label>
                    <div class="controls">
						<c:forEach items="${EnterpriseTypeEnums}" var="v">

							<c:if test="${enterprise.enterpriseType eq v.value}">${v.desc}</c:if>
						</c:forEach>
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">组织机构代码：</label>
                    <div class="controls">
                    	${enterprise.organizationCode }
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">税务登记证代码：</label>
                    <div class="controls">
                    	${enterprise.taxRegistrationCode }
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">法人姓名：</label>
                    <div class="controls">
                    	${enterprise.legalPersonName }
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">经营年限：</label>
                    <div class="controls">
                    	${enterprise.operatingPeriod }
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">经营范围：</label>
                    <div class="controls">
                    	${enterprise.operatingRange }
                    </div>
                </div>

				<div class="control-group">
                    <label class="control-label">企业信息：</label>
                    <div class="controls">
                    	${enterprise.information }
                    </div>
                </div>
                
				<div class="control-group market">
                    <label class="control-label">主营收入：</label>
                    <div class="controls">
                    	${enterprise.mainRevenue }万元
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">净利润：</label>
                    <div class="controls">
                    	${enterprise.netProfit }万元
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">净资产：</label>
                    <div class="controls">
                    	${enterprise.netAssets }万元
                    </div>
                </div>
                

          </td>
          
          <td>
          		
				<div class="control-group">
                    <label class="control-label">公司名称：</label>
                    <div class="controls">
                    	${enterprise.enterpriseName }
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">开户许可证号：</label>
                    <div class="controls">
                    	${enterprise.accountNumber }
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">营业执照注册号：</label>
                    <div class="controls">
                    	${enterprise.businessRegistrationNumber }
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">法人身份证号：</label>
                    <div class="controls">
                    	${enterprise.legalPersonCode }
                    </div>
                </div>
                
                <div class="control-group">
                    <label class="control-label">注册资金：</label>
                    <div class="controls">
                    	${enterprise.registeredCapital }元
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">毛利润：</label>
                    <div class="controls">
                    	${enterprise.grossProfit }万元
                    </div>
                </div>
                
                <div class="control-group market">
                    <label class="control-label">净利润：</label>
                    <div class="controls">
                    	${enterprise.netProfit }万元
                    </div>
                </div>

          </td>
      </tr>
      
      <tr>
      	<td colspan="2" style="font-weight: bold;">
      		<br/><br/>
      		相关附件：<br/><br/>
      	</td>
      </tr>
      
      <tr>
          <td colspan="2">
          		<!-- 法人身份证 -->
          		<span style="font-size: 16px;">法人身份证</span>
          		<br/><hr>
          		
          		<div id="legalIdentityCard">
          			<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 0}">
          				<div id='img_${cusvo.fileName}_div' style='display : inline-block;position : relative;margin-right: 15px;'>
          					<font style='font-size: 12px;'>${cusvo.isdisplay==0?'前台显示':'不显示' }</font><br/>
          					<img id='img_${cusvo.fileName }' alt='' width="100px;" height="100px;" src='${picPath}${cusvo.attachment.url }' onclick='showBig(${cusvo.snapshotId})'> 
          				</div>
          				</c:if>
					</c:forEach>
				</div>
				<div id="legalIdentityCard_name">
          			<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 0}">
          					<span id='img_${cusvo.fileName }_span' style='margin: 20px;'>${cusvo.attachment.fileName }</span>
          				</c:if>
					</c:forEach>
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 法人个人征信 -->
				<span style="font-size: 16px;">法人个人征信</span>
          		<br/><hr>
          		<div id="legalPersonalCredit">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 1}">
          				<div id='img_${cusvo.fileName}_div' style='display : inline-block;position : relative;margin-right: 15px;'>
          					<font style='font-size: 12px;'>${cusvo.isdisplay==0?'前台显示':'不显示' }</font><br/>
          					<img id='img_${cusvo.fileName }' alt='' width="100px;" height="100px;" src='${picPath}${cusvo.attachment.url }' onclick='showBig(${cusvo.snapshotId})'> 
          				</div>
          				</c:if>
					</c:forEach>
				</div>
				<div id="legalPersonalCredit_name">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 1}">
          					<span id='img_${cusvo.fileName }_span' style='margin-right: 20px;'>${cusvo.attachment.fileName }</span>
          				</c:if>
					</c:forEach>
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 税务登记证 -->
				<span style="font-size: 16px;">税务登记证</span>
          		<br/><hr>
          		<div id="taxRegistrationCertificate">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 2}">
          				<div id='img_${cusvo.fileName}_div' style='display : inline-block;position : relative;margin-right: 15px;'>
          					<font style='font-size: 12px;'>${cusvo.isdisplay==0?'前台显示':'不显示' }</font><br/>
          					<img id='img_${cusvo.fileName }' alt='' width="100px;" height="100px;" src='${picPath}${cusvo.attachment.url }' onclick='showBig(${cusvo.snapshotId})'> 
          				</div>
          				</c:if>
					</c:forEach>
				</div>
				<div id="taxRegistrationCertificate_name">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 2}">
          					<span id='img_${cusvo.fileName }_span' style='margin-right: 20px;'>${cusvo.attachment.fileName }</span>
          				</c:if>
					</c:forEach>
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 营业执照 -->
				<span style="font-size: 16px;">营业执照</span>
          		<br/><hr>
          		<div id="businessLicense">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 3}">
          				<div id='img_${cusvo.fileName}_div' style='display : inline-block;position : relative;margin-right: 15px;'>
          					<font style='font-size: 12px;'>${cusvo.isdisplay==0?'前台显示':'不显示' }</font><br/>
          					<img id='img_${cusvo.fileName }' alt='' width="100px;" height="100px;" src='${picPath}${cusvo.attachment.url }' onclick='showBig(${cusvo.snapshotId})'> 
          				</div>
          				</c:if>
					</c:forEach>
				</div>
				<div id="businessLicense_name">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 3}">
          					<span id='img_${cusvo.fileName }_span' style='margin-right: 20px;'>${cusvo.attachment.fileName }</span>
          				</c:if>
					</c:forEach>
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 组织机构代码证 -->
				<span style="font-size: 16px;">组织机构代码证</span>
          		<br/><hr>
          		<div id="organizationCodeCertificate">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 4}">
          				<div id='img_${cusvo.fileName}_div' style='display : inline-block;position : relative;margin-right:15px;'>
          					<font style='font-size: 12px;'>${cusvo.isdisplay==0?'前台显示':'不显示' }</font><br/>
          					<img id='img_${cusvo.fileName }' alt='' width="100px;" height="100px;" src='${picPath}${cusvo.attachment.url }' onclick='showBig(${cusvo.snapshotId})'> 
          				</div>
          				</c:if>
					</c:forEach>
				</div>
				<div id="organizationCodeCertificate_name">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 4}">
          					<span id='img_${cusvo.fileName }_span' style='margin-right: 20px;'>${cusvo.attachment.fileName }</span>
          				</c:if>
					</c:forEach>
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 开户许可证 -->
				<span style="font-size: 16px;">开户许可证</span>
          		<br/><hr>
          		<div id="openingPermit">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 5}">
          				<div id='img_${cusvo.fileName}_div' style='display : inline-block;position : relative;margin-right: 15px;'>
          					<font style='font-size: 12px;'>${cusvo.isdisplay==0?'前台显示':'不显示' }</font><br/>
          					<img id='img_${cusvo.fileName }' alt='' width="100px;" height="100px;" src='${picPath}${cusvo.attachment.url }' onclick='showBig(${cusvo.snapshotId})'> 
          				</div>
          				</c:if>
					</c:forEach>
				</div>
				<div id="openingPermit_name">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 5}">
          					<span id='img_${cusvo.fileName }_span' style='margin-right: 20px;'>${cusvo.attachment.fileName }</span>
          				</c:if>
					</c:forEach>
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 验资报告 -->
				<span style="font-size: 16px;">验资报告</span>
          		<br/><hr>
          		<div id="theCapitalVerificationReport">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 6}">
          				<div id='img_${cusvo.fileName}_div' style='display : inline-block;position : relative;margin-right: 15px;'>
          					<font style='font-size: 12px;'>${cusvo.isdisplay==0?'前台显示':'不显示' }</font><br/>
          					<img id='img_${cusvo.fileName }' alt='' width="100px;" height="100px;" src='${picPath}${cusvo.attachment.url }' onclick='showBig(${cusvo.snapshotId})'> 
          				</div>
          				</c:if>
					</c:forEach>
				</div>
				<div id="theCapitalVerificationReport_name">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 6}">
          					<span id='img_${cusvo.fileName }_span' style='margin-right: 20px;'>${cusvo.attachment.fileName }</span>
          				</c:if>
					</c:forEach>
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 经营场所租凭合同 -->
				<span style="font-size: 16px;">经营场所租凭合同</span>
          		<br/><hr>
          		<div id="businessPremisesLeaseContract">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 7}">
          				<div id='img_${cusvo.fileName}_div' style='display : inline-block;position : relative;margin-right: 15px;'>
          					<font style='font-size: 12px;'>${cusvo.isdisplay==0?'前台显示':'不显示' }</font><br/>
          					<img id='img_${cusvo.fileName }' alt='' width="100px;" height="100px;" src='${picPath}${cusvo.attachment.url }' onclick='showBig(${cusvo.snapshotId})'> 
          				</div>
          				</c:if>
					</c:forEach>
				</div>
				<div id="businessPremisesLeaseContract_name">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 7}">
          					<span id='img_${cusvo.fileName }_span' style='margin-right: 20px;'>${cusvo.attachment.fileName }</span>
          				</c:if>
					</c:forEach>
				</div>
				<hr style="margin-bottom: 20px;">
				
				<!-- 近三年的财务报表 -->
				<span style="font-size: 16px;">近三年的财务报表</span>
          		<br/><hr>
          		<div id="nearlyThreeYearsOfFinancialStatements">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 8}">
          				<div id='img_${cusvo.fileName}_div' style='display : inline-block;position : relative;margin-right: 15px;'>
          					<font style='font-size: 12px;'>${cusvo.isdisplay==0?'前台显示':'不显示' }</font><br/>
          					<img id='img_${cusvo.fileName }' alt='' width="100px;" height="100px;" src='${picPath}${cusvo.attachment.url }' onclick='showBig(${cusvo.snapshotId})'> 
          				</div>
          				</c:if>
					</c:forEach>
				</div>
				<div id="nearlyThreeYearsOfFinancialStatements_name">
					<c:forEach var="cusvo" items="${cusvoList }">
          				<c:if test="${cusvo.type == 8}">
          					<span id='img_${cusvo.fileName }_span' style='margin-right: 20px;'>${cusvo.attachment.fileName }</span>
          				</c:if>
					</c:forEach>
				</div>
				<hr style="margin-bottom: 20px;">

          </td>
      </tr>
	</table>
</form>
</div>

</body>
</html>
