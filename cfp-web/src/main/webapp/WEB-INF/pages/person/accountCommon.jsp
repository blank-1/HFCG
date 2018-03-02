<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%
    String tab = (String)request.getAttribute("tab");
    String[] tabs = null;
    if(tab!=null){
      tabs = tab.split("-");
    }else{
      tabs = new String[]{"1"};
    }
    request.setAttribute("tabs",tabs);
    request.setAttribute("tab",tab);
  %>
<div class="tabpul <c:if test="${tabs[0] eq '1'}">personindex</c:if> clearFloat">
  <!-- ftitle start -->
  <div class="ftitle_pre">
    <div class="ptitle clearFloat">
      <ul class="tabulper wrapper clearFloat">
        <li class="zheicon <c:if test="${tabs[0] eq '1'}">action</c:if>"><a href="${ctx }/person/account/overview">账户总览</a></li>
        <li data-id="0"  class="hengicon <c:if test="${tabs[0] eq '2'}">action</c:if>"><a href="#">我的理财</a><i></i></li>
        <%-- <li data-id="1"  class="myjiek <c:if test="${tabs[0] eq '3'}">action</c:if>"><a href="#">我的借款</a><i></i></li> --%>
        <li data-id="1"  class="zijingl <c:if test="${tabs[0] eq '4'}">action</c:if>"><a href="#">资金管理</a><i></i></li>
        <li data-id="2"  class="prexinxi <c:if test="${tabs[0] eq '5'}">action</c:if>"><a href="#">个人信息</a><i></i></li>
      </ul>
    </div>
  </div><!-- ftitle end -->
  <!-- tab start -->
  <div class="tab pre_main clearFloat" <c:if test="${tabs[0] eq '2'}">style="display: block;" </c:if>>
    <div class="pre_maintitle<c:if test="${tabs[0] eq '2'}"> preaction ding</c:if>">
      <ul class="tabul2">
        <li class="action_ri <c:if test="${tab eq '2-1'}">action2</c:if>"  ><a href="${ctx }/finance/toMyCreditRightList">出借债权</a><i></i></li>
        <li data-id="table2" <c:if test="${tab eq '2-2'}">class="action2"</c:if>><a href="${ctx }/finance/toAllMyFinanceList">省心计划</a><i></i></li>
        <li data-id="table2" <c:if test="${tab eq '2-3'}">class="action2"</c:if>><a href="${ctx }/finance/toTurnCreditRightList">债权转让</a><i></i></li>
      </ul>
    </div>
    <%-- <div class="pre_maintitle <c:if test="${tabs[0] eq '3'}"> preaction ding</c:if>">
      <ul class="tabul2">
        <li class="action_ri2">我的借款1<i></i></li>
        <li>我的借款2<i></i></li>
      </ul>
    </div> --%>
    <div class="pre_maintitle <c:if test="${tabs[0] eq '4'}"> preaction ding</c:if>">
      <ul class="tabul3 preaction">
      	<li class="action_ri390 <c:if test="${tab eq '4-0'}">action2</c:if>"  ><a href="${ctx }/person/toFundManage">资金流水</a><i></i></li>
        <li <c:if test="${tab eq '4-1'}">class="action2"</c:if>><a href="${ctx}/person/to_lendorder_list">订单查询</a><i></i></li>
        <li <c:if test="${tab eq '4-2'}">class="action2"</c:if>><a href="${ctx}/person/toIncome">充值</a><i></i></li>
        <li <c:if test="${tab eq '4-3'}">class="action2"</c:if>><a href="${ctx}/person/toWithDraw">提现</a><i></i></li>
        <li <c:if test="${tab eq '4-4'}">class="action2"</c:if>><a href="${ctx}/person/toVoucher">财富券</a><i></i></li>
        <li <c:if test="${tab eq '4-5'}">class="action2"</c:if>><a href="${ctx}/bankcard/to_bankcard_list">银行卡管理</a><i></i></li>
      </ul>
    </div>
    <div class="pre_maintitle <c:if test="${tabs[0] eq '5'}"> preaction ding</c:if>">
      <ul class="tabul2">
      	<li class="action_ri_infor <c:if test="${tab eq '5-0'}">action2</c:if>"><a href="${ctx}/person/to_userInfo">个人信息</a><i></i></li>
        <li <c:if test="${tab eq '5-1'}">class="action2"</c:if>><a href="${ctx }/person/toAuthentication">验证信息</a><i></i></li>
        <li <c:if test="${tab eq '5-2'}">class="action2"</c:if>><a href="${ctx }/person/to_pass_manage">密码管理</a><i></i></li>
        <li <c:if test="${tab eq '5-3'}">class="action2"</c:if>><a href="${ctx }/message/toUserMessage">消息管理</a><i></i></li>
        <li <c:if test="${tab eq '5-4'}">class="action2"</c:if>><a href="${ctx }/person/to_invite_friends">邀请好友</a><i></i></li>
      </ul>
    </div>
  </div>
</div>
