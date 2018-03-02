<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>

<c:forEach var="bank" items="${bankList}"  varStatus="status">
	<c:if test="${status.index%4==0 &&(fn:length(bankList) != status.count)}">
		<c:choose>
            <c:when test="${status.count==1}">
          	  <div class="internateBank clearFloat">
              <div style="clear:both;"></div>
              <span id="gateway${bank.constantValue}"  code="${bank.constantValue}"  class="choose" ><img src="${ctx}/images/llbanklogo/b_${bank.constantValue}.jpg"    /><i></i></span>
            </c:when>
            <c:when test="${status.count==5}">
          	  <div class="internateBank clearFloat">
              <span id="gateway${bank.constantValue}"  code="${bank.constantValue}"  ><img src="${ctx}/images/llbanklogo/b_${bank.constantValue}.jpg"    /><i></i></span>
            </c:when>
            <c:otherwise>
                <div class="internateBank display-none cange clearFloat"> 
				<span id="gateway${bank.constantValue}"  code="${bank.constantValue}"  ><img src="${ctx}/images/llbanklogo/b_${bank.constantValue}.jpg"   /><i></i></span>
            </c:otherwise>
         </c:choose>
	</c:if>
	
	<c:if test="${status.index%4==0 &&(fn:length(bankList) == status.count)}">
		<c:choose>
            <c:when test="${status.count==1}">
          	  <div class="internateBank clearFloat">
              <div style="clear:both;"></div>
              <span id="gateway${bank.constantValue}"  code="${bank.constantValue}"  class="choose" ><img src="${ctx}/images/llbanklogo/b_${bank.constantValue}.jpg"    /><i></i></span>
            	  </div>
				<table cellpadding="0" cellspacing="0" class="internateTable"></table>
            </c:when>
            <c:when test="${status.count==5}">
          	  <div class="internateBank clearFloat">
              <span id="gateway${bank.constantValue}"  code="${bank.constantValue}"  ><img src="${ctx}/images/llbanklogo/b_${bank.constantValue}.jpg"    /><i></i></span>
                </div>
				<table cellpadding="0" cellspacing="0" class="internateTable"></table>
            </c:when>
            <c:otherwise>
                <div class="internateBank display-none cange clearFloat"> 
				<span id="gateway${bank.constantValue}"  code="${bank.constantValue}"  ><img src="${ctx}/images/llbanklogo/b_${bank.constantValue}.jpg"   /><i></i></span>
				  </div>
				<table cellpadding="0" cellspacing="0" class="internateTable"></table>
            </c:otherwise>
         </c:choose>
	</c:if>
	
	
	<c:if test="${(status.index%4==1 || status.index%4==2) &&(fn:length(bankList) != status.count)}">
		<span  id="gateway${bank.constantValue}" code="${bank.constantValue}" ><img src="${ctx}/images/llbanklogo/b_${bank.constantValue}.jpg"   /><i></i></span>
	</c:if>
	<c:if test="${(status.index%4==1 || status.index%4==2) &&(fn:length(bankList) == status.count)}">
		<span  id="gateway${bank.constantValue}" code="${bank.constantValue}" ><img src="${ctx}/images/llbanklogo/b_${bank.constantValue}.jpg"   /><i></i></span>
		</div>
		<table cellpadding="0" cellspacing="0" class="internateTable"></table>
	</c:if>
	<c:if test="${status.index%4==3}">
		<span  id="gateway${bank.constantValue}" code="${bank.constantValue}" ><img src="${ctx}/images/llbanklogo/b_${bank.constantValue}.jpg"   /><i></i></span>
             			 </div>
         <c:if test="${fn:length(bankList) == status.count}">
         	<table cellpadding="0" cellspacing="0" class="internateTable"></table>
         </c:if>
	</c:if>
</c:forEach>
                
