<%@ page import="com.xt.cfp.core.constants.LoanTypeEnum" %>
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
      <div style="border-bottom: 1px none black;clear: both;">
        <fieldset style="height:auto">
          <legend>借款信息</legend>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款用途：</label>
            <div class="controls">
                <customUI:dictionaryTable constantTypeCode="loanUseage" desc="true" key="${loanApplication.loanUseage}"/>
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 标的来源：</label>
            <div class="controls">
                ${sourceStr}
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 线下编号：</label>
              <div class="controls">
                  ${loanApplication.offlineApplyCode}
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款类型：</label>
              <div class="controls">
                  <c:if test="${loanApplication.loanType eq '0'}">信贷</c:if>
                  <c:if test="${loanApplication.loanType eq '1'}">房贷</c:if>
                  <c:if test="${loanApplication.loanType eq '7'}">个人房产直投</c:if>
                  <c:if test="${loanApplication.loanType eq '8'}">个人车贷</c:if>
                  <c:if test="${loanApplication.loanType eq '9'}"><%=LoanTypeEnum.LOANTYPE_CASH_LOAN.getDesc()%></c:if>
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款产品：</label>
              <div class="controls">
                  ${product.productName}
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款金额：</label>
              <div class="controls">
                  ${loanApplication.loanBalance}
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 批复产品：</label>
              <div class="controls">
                  ${product.productName}
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 批复金额：</label>
              <div class="controls">
                  ${loanApplication.confirmBalance}
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 年利率：</label>
              <div class="controls">
                  ${loanApplication.annualRate}%
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款描述：</label>
              <div class="controls">
                  ${loanApplication.applicationDesc}
              </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款人姓名：</label>
              <div class="controls">
                  ${customer.trueName}
              </div>
          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款人身份证号：</label>
             <div class="controls">
                 ${customer.idCard}
             </div>
          </div>
         <c:if test="${loanApplication.loanType ne '9'}">
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 性别：</label>
             <div class="controls">
                 <customUI:dictionaryTable constantTypeCode="sex" desc="true" key="${customer.sex}"/>
             </div>
          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 出生日期：</label>
             <div class="controls">
                 <fmt:formatDate value="${customer.birthday}" pattern="yyyy年MM月dd日" />
             </div>

          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款人手机：</label>
             <div class="controls">
                 ${customer.mobilePhone}
             </div>
          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 借款人邮箱：</label>
             <div class="controls">
                 ${customer.email}
             </div>
          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 最高学历：</label>
             <div class="controls">
                 <customUI:dictionaryTable  desc="true"  constantTypeCode="education"  key="${customer.education}"/>
             </div>
          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 婚姻状况：</label>
           <div class="controls">
               <customUI:dictionaryTable constantTypeCode="isMarried" desc="true" key="${customer.isMarried}"/>
           </div>
          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 子女状况：</label>
             <div class="controls">
                 <customUI:dictionaryTable  desc="true"  constantTypeCode="childStatus"  key="${customer.childStatus}"/>
             </div>
          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 月均收入：</label>
             <div class="controls">
                    <fmt:formatNumber value="${customer.monthlyIncome}" pattern="#,##0.00"/>元
             </div>
          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 信用卡最高额度：</label>
             <div class="controls">
                     <fmt:formatNumber value="${customer.maxCreditValue}" pattern="#,##0.00"/>元
             </div>
          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 现住址：</label>
             <div class="controls">
                 <c:if test="${customerAdress.provinceStr ne customerAdress.cityStr}">
                     ${customerAdress.provinceStr}
                 </c:if>
                 ${customerAdress.cityStr}${customerAdress.districtStr}${customerAdress.detail}
             </div>
          </div>
         <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 邮政编码：</label>
             <div class="controls">
                 ${customer. zipcode}
             </div>
          </div>
        </c:if>
        </fieldset>
      </div>
    <c:if test="${loanApplication.loanType ne '9'}">
      <div style="border-bottom: 1px none black;">
        <fieldset style="height:215px">
          <legend>工作信息</legend>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 单位名称：</label>
            <div class="controls">
              ${work.companyName}
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 单位性质：</label>
            <div class="controls">
              <customUI:dictionaryTable constantTypeCode="companyNature" key="${work.companyNature}" desc="true"/>
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 单位地址：</label>
            <div class="controls">
                <c:if test="${workAdress.provinceStr ne workAdress.cityStr}">
                    ${workAdress.provinceStr}
                </c:if>
              ${workAdress.cityStr}${workAdress.districtStr}${workAdress.detail}
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 单位电话：</label>
            <div class="controls">
              ${work.companyPhone}
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 职务：</label>
            <div class="controls">
              ${work.post}
            </div>
          </div>
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 入职时间：</label>
            <div class="controls">

                <fmt:formatDate value="${work.joinDate}" pattern="yyyy年MM月dd日" />
            </div>
          </div>
        </fieldset>
      </div>
      <div>
        <fieldset style="height:165px">
        <legend>联系人信息</legend>
            <c:if test="${empty contacts}" >无</c:if>
            <c:if test="${not empty contacts}" >
              <c:forEach items="${contacts}" var="contact">
                <div class="control-group">
                  <label class="control-label"  style="padding-top: 0px;">  <customUI:dictionaryTable constantTypeCode="relationType" desc="true" key="${contact.relationType}"/>
                      <c:if test="${not empty contact.relation }">
                      -
                      </c:if>
                    <customUI:dictionaryTable constantTypeCode="relation" desc="true" key="${contact.relation}" parentkey="${contact.relationType}"/>：</label>
                  <div class="controls">
                     ${contact.concactName}&nbsp;&nbsp;${contact.concatPhone}
                  </div>
                </div>
              </c:forEach>
            </c:if>
      </fieldset>
      </div>
    </c:if>
      <div style="border-bottom: 1px none black;">
        <fieldset style="height:250px;">
          <legend>银行卡信息</legend>
            打款卡
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
          划扣卡
            <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 开户银行：</label>
                <div class="controls">
                    ${outCard.registeredBank}
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 卡号：</label>
                <div class="controls">
                    ${outCard.cardCode}
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" style="padding-top: 0px;"> 开户名：</label>
                <div class="controls">
                    ${outCard.cardCustomerName}
                </div>
            </div>
        </fieldset>
      </div>
    <%--  <div>
        <fieldset style="height:65px">
          <legend>事件时间</legend>
          <div class="control-group">
            <label class="control-label"  style="padding-top: 0px;"> 创建：</label>
              <div class="controls">
                  <fmt:formatDate value="${loanApplication.createTime}" pattern="yyyy年MM月dd日 hh时mm分ss秒" />
              </div>
          </div>
     &lt;%&ndash;     <div class="control-group">
            <label class="control-label"  style="padding-top: 0px;"> 风控初审：</label>
              <fmt:formatDate value="${loanApplication.createTime}" pattern="yyyy年MM月dd日 hh时mm分ss秒" />
          </div>
          <div class="control-group">
            <label class="control-label"  style="padding-top: 0px;"> 风控复审：</label>
          </div>&ndash;%&gt;
          <div class="control-group">
            <label class="control-label"  style="padding-top: 0px;"> 发标复审：</label>
          </div>
          <div class="control-group">
            <label class="control-label"  style="padding-top: 0px;"> 发标：</label>
              <fmt:formatDate value="${loanApplication.publishTime}" pattern="yyyy年MM月dd日 hh时mm分ss秒" />
          </div>
          <div class="control-group">
            <label class="control-label"  style="padding-top: 0px;"> 满标：</label>
              <fmt:formatDate value="${loanApplication.fullTime}" pattern="yyyy年MM月dd日 hh时mm分ss秒" />
          </div>
          <div class="control-group">
          <label class="control-label"  style="padding-top: 0px;"> 满标复审：</label>
        </div>
          <div class="control-group">
            <label class="control-label"  style="padding-top: 0px;"> 放款复审：</label>
          </div>
          <div class="control-group">
            <label class="control-label"  style="padding-top: 0px;"> 放款：</label>
          </div>
          <div class="control-group">
          <label class="control-label"  style="padding-top: 0px;"> 第1期还款：</label>
        </div>
          <div class="control-group">
            <label class="control-label"  style="padding-top: 0px;"> 结清：</label>
          </div>
        </fieldset>
      </div>--%>
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
