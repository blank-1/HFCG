<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
     <form class="form-horizontal" id="addBindSource_form">
  <div>
    <div style="width:60%;border-right: 1px none black;float:left;">
    
    <!-- 项目开始 -->
      <div style="border-bottom: 1px none black;clear: both;">
        <fieldset style="height:280px">
          <legend>项目信息</legend>
          <div class="control-group">
          <label class="control-label" style="padding-top: 0px;"> 借款类型：</label>
              <div class="controls">
                  <c:if test="${loanApplication.loanType eq '2'}">企业车贷</c:if>
                  <c:if test="${loanApplication.loanType eq '3'}">企业信贷</c:if>
                  <c:if test="${loanApplication.loanType eq '4'}">企业保理</c:if>
                  <c:if test="${loanApplication.loanType eq '5'}">企业基金</c:if>
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款产品：</label>
              <div class="controls">
                  ${product.productName}
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 年利率：</label>
              <div class="controls">
                  ${loanApplication.annualRate}%
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 期限：</label>
              <div class="controls">
                  	${product.dueTime }${product.dueTimeType =='1'?'日':'月' }
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 还款方式：</label>
              <div class="controls">
                  	${product.repaymentType == '1'?'等额本息':'等额本金'}
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款金额：</label>
              <div class="controls">
                  ${loanApplication.loanBalance}
              </div>
          </div>
         <c:if test="${loanApplication.loanType ne '5'}">
              <div class="control-group">
                 <label class="control-label" style="padding-top: 0px;"> 借款用途：</label>
                 <div class="controls">
                     <customUI:dictionaryTable constantTypeCode="enterpriseLoanUseage" desc="true" key="${loanApplication.loanUseage}"/>
                 </div>
             </div>
         </c:if>
	   	  <div class="control-group">
	         <label class="control-label" style="padding-top: 0px;"> 线下合同/订单号：</label>
	           <div class="controls">
	               ${loanApplication.offlineApplyCode}
	           </div>
	       </div>
         <c:if test="${loanApplication.loanType eq '5'}">
              <div class="control-group">
                 <label class="control-label" style="padding-top: 0px;"> 定向委托投资类型：</label>
                 <div class="controls">
                     <customUI:dictionaryTable constantTypeCode="INVESTMENT_TYPE" desc="true" key="${foundationSnapshot.investmentType}"/>

                 </div>
             </div>
             <div class="control-group">
                 <label class="control-label" style="padding-top: 0px;"> 托管机构：</label>
                 <div class="controls">
                     ${coLtd.companyName}
                 </div>
             </div>
         </c:if>

        </fieldset>
      </div>
      <!-- 项目结束 -->
      
      <c:if test="${loanApplication.loanType == '2' }">
	      <!-- 车贷开始 -->
	      <div style="border-bottom: 1px none black;clear: both;">
	        <fieldset>
	          <legend>车贷</legend>
	          	  <div class="control-group">
			        <label class="control-label" style="padding-top: 0px;"> 借款描述：</label>
			        <div class="controls">
			            	${carLoanSnapshot.useDescription}
			        </div>
	       		</div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 项目地区：</label>
		              <div class="controls">
		                  	${carLoanSnapshot.provinceName }${carLoanSnapshot.cityName==carLoanSnapshot.provinceName?'':carLoanSnapshot.cityName }
		              </div>
		          </div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 项目描述：</label>
		              <div class="controls">
		                  	${carLoanSnapshot.projectDescription } 
		              </div>
		          </div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 内部评级：</label>
		              <div class="controls">
	                  	 <c:if test="${carLoanSnapshot.internalRating ==0}">A</c:if>
	                  	 <c:if test="${carLoanSnapshot.internalRating ==1}">AA</c:if>
	                  	 <c:if test="${carLoanSnapshot.internalRating ==2}">AAA</c:if>
	                  	 <c:if test="${carLoanSnapshot.internalRating ==3}">AAAA</c:if>
		              </div>
		          </div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 风险控制信息：</label>
		              <div class="controls">
		                  	${carLoanSnapshot.riskControlInformation }
		              </div>
		          </div>
		          
	          </fieldset>
	      </div>
	      <!-- 车贷结束 -->
      </c:if>
      
      <c:if test="${loanApplication.loanType == '3' }">
	      <!-- 信贷开始 -->
	      <div style="border-bottom: 1px none black;clear: both;">
	        <fieldset>
	          <legend>信贷</legend>
	          	<div class="control-group">
			        <label class="control-label" style="padding-top: 0px;"> 借款描述：</label>
			        <div class="controls">
			            	${creditSnapshot.useDescription}
			        </div>
	       		</div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 项目地区：</label>
		              <div class="controls">
		                  	${creditSnapshot.provinceName }${creditSnapshot.cityName==creditSnapshot.provinceName?'':creditSnapshot.cityName }
		              </div>
		          </div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 项目描述：</label>
		              <div class="controls">
		                  	${creditSnapshot.projectDescription }
		              </div>
		          </div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 内部评级：</label>
		              <div class="controls">
		                 <c:if test="${creditSnapshot.internalRating ==0}">A</c:if>
	                  	 <c:if test="${creditSnapshot.internalRating ==1}">AA</c:if>
	                  	 <c:if test="${creditSnapshot.internalRating ==2}">AAA</c:if>
	                  	 <c:if test="${creditSnapshot.internalRating ==3}">AAAA</c:if>
		              </div>
		          </div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 风险控制信息：</label>
		              <div class="controls">
		                  	${creditSnapshot.riskControlInformation }
		              </div>
		          </div>
		          
	          </fieldset>
	      </div>
	      <!-- 信贷结束 -->
      </c:if>
      
      <c:if test="${loanApplication.loanType == '4' }">
	      <!-- 保理开始 -->
	      <div style="border-bottom: 1px none black;clear: both;">
	        <fieldset>
	          <legend>保理</legend>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 应收账款说明：</label>
		              <div class="controls">
		                 	 应收账款说明${factoringSnapshot.accountReceivableDescription }
		              </div>
		          </div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 项目综合评价：</label>
		              <div class="controls">
		                  	项目综合评价${factoringSnapshot.projectComprehensiveEvaluati }
		              </div>
		          </div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 款项风险评价：</label>
		              <div class="controls">
		                 	 款项风险评价${factoringSnapshot.moneyRiskAssessment }
		              </div>
		          </div>
		          <div class="control-group">
		            <label class="control-label" style="padding-top: 0px;"> 项目风险保障方案：</label>
		              <div class="controls">
		             		<c:if test="${factoringSnapshot.fieldAdjustmentMark == 0}">
		             			<input type="checkbox" id="fieldAdjustmentMark" checked="checked" disabled="disabled">${factoringSnapshot.fieldAdjustmentValue }<br>
		             		</c:if>	
		             		
		             		<c:if test="${factoringSnapshot.repaymentGuaranteeMark == 0}">
		             			<input type="checkbox" id="repaymentGuaranteeMark" checked="checked" disabled="disabled">${factoringSnapshot.repaymentGuaranteeValue }<br>
		             		</c:if>	
		             		
		             		<c:if test="${factoringSnapshot.aidFundMark == 0}">
		             			<input type="checkbox" id="aidFundMark" checked="checked" disabled="disabled">${factoringSnapshot.aidFundValue }<br>
		             		</c:if>	
		                 	 
		              </div>
		          </div>
		          
	          </fieldset>
	      </div>
	      <!-- 保理结束 -->
      </c:if>
      
      <!-- 银行卡开始 -->
      <div style="border-bottom: 1px none black;">
        <fieldset style="height:150px;">
          <legend>银行卡信息</legend>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 开户银行：</label>
                  <div class="controls">
                    ${inCard.registeredBank}
                  </div>
              </div>

              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 卡号：</label>
                  <div class="controls">
                      ${inCard.cardCode}
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 开户名：</label>
                  <div class="controls">
                      ${inCard.cardCustomerName}
                  </div>
              </div>
        </fieldset>
      </div>
      <!-- 银行卡结束 -->
      
      <!-- 企业开始 -->
      <div style="border-bottom: 1px none black;clear: both;">
        <fieldset style="height:350px">
          <legend>企业信息</legend>
          	  <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 企业名称：</label>
                  <div class="controls">
                      	${enterpriseInfo.enterpriseName }
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 组织机构编码：</label>
                  <div class="controls">
                      	${enterpriseInfo.organizationCode }
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 企业法人：</label>
                  <div class="controls">
                      	${enterpriseInfo.legalPersonName }
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 法人身份证号：</label>
                  <div class="controls">
                      	${enterpriseInfo.legalPersonCode }
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 开户许可证号：</label>
                  <div class="controls">
                      	${enterpriseInfo.accountNumber }
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 税务登记证代码：</label>
                  <div class="controls">
                      	${enterpriseInfo.taxRegistrationCode }
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 营业执照注册号：</label>
                  <div class="controls">
                      	${enterpriseInfo.businessRegistrationNumber }
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 经营年限：</label>
                  <div class="controls">
                      	${enterpriseInfo.operatingPeriod }
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 注册资金：</label>
                  <div class="controls">
                      	${enterpriseInfo.registeredCapital }
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 经营范围：</label>
                  <div class="controls">
                      	${enterpriseInfo.operatingRange }
                  </div>
              </div>
          
          </fieldset>
      </div>
      <!-- 企业结束 -->
      
     <!-- 其它开始 -->
      <div style="border-bottom: 1px none black;clear: both;">
        <fieldset>
          <legend>其它信息</legend>
          	  <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 录入人员：</label>
                  <div class="controls">
                      	${loanApplication.recorderName }
                  </div>
              </div>
          	  <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 录入时间：</label>
                  <div class="controls">
                      	<fmt:formatDate value="${loanApplication.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 发标时间：</label>
                  <div class="controls">
                      	<fmt:formatDate value="${loanApplication.publishTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 满标时间：</label>
                  <div class="controls">
                      	<fmt:formatDate value="${loanApplication.fullTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 放款时间：</label>
                  <div class="controls">
                      	<fmt:formatDate value="${loanApplication.paymentDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 流标时间：</label>
                  <div class="controls">
                      	<fmt:formatDate value="${loanApplication.cancelTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                  </div>
              </div>
              <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 结清时间：</label>
                  <div class="controls">
                      	
                  </div>
              </div>
          </fieldset>
      </div>
      <!-- 其它结束 -->
    
        <div style="margin-bottom: 70px;"></div>
    </div>
    </div>

    <div>
      <div>

          <c:forEach items="${loanVerifyInfo}" var="verify">
              <fieldset style="height:165px">
                  <legend>
                      <c:forEach items="${verifyType}" var="type">
                          <c:if test="${type.value eq verify.verifyType}">${type.desc}</c:if>
                      </c:forEach>
                  </legend>
                  <div class="control-group">
                      <label class="control-label"  style="padding-top: 0px;"> 审核人员：</label>
                      <div class="controls">
                              ${verify.userName}
                      </div>
                  </div>
                  <div class="control-group">
                      <label class="control-label"  style="padding-top: 0px;"> 审核时间：</label>
                      <div class="controls">
                             <fmt:formatDate value="${verify.verifyTime}" pattern="yyyy年MM月dd日 hh时mm分ss秒" />
                      </div>
                  </div>
                  <div class="control-group">
                      <label class="control-label"  style="padding-top: 0px;">  审核结果：</label>
                      <div class="controls">
                            <c:if test="${verify.result eq '1'}">审核通过</c:if>
                            <c:if test="${verify.result eq '2'}">驳回</c:if>
                      </div>
                  </div>
                  <div class="control-group" >
                      <label class="control-label"  style="padding-top: 0px;">   审核意见：</label>
                      <div class="controls">
                              ${verify.verifySuggestion}
                      </div>
                  </div>
              </fieldset>

          </c:forEach>


      </div>
    </div>
  </div>
     </form>
</body>
</html>
