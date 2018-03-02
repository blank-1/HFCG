<%@tag pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ tag import="com.xt.cfp.core.service.ConstantDefineService"%>
<%@ tag import="com.xt.cfp.core.pojo.ConstantDefine" %>
<%@ tag import="java.util.List" %>
<%@ tag import="com.xt.cfp.core.util.ApplicationContextUtil" %>
<%@ tag import="org.apache.commons.lang.StringUtils" %>

<%@attribute name="constantTypeCode"  required="true" %>
<%@attribute name="key"  required="false" rtexprvalue="true" %>
<%@attribute name="id" required="false" %>
<%@attribute name="name" required="false" %>
<%@attribute name="style" required="false" %>
<%@attribute name="isWholeUse" required="false" type="java.lang.Boolean"  %>
<%@attribute name="wholeValue" required="false" %>
<%@attribute name="desc" required="true" type="java.lang.Boolean" %>
<%@attribute name="keyType" required="false" %>
<%@attribute name="parentkey" required="false"  rtexprvalue="true"%>


<%
    ConstantDefineService constantdefineservice = ApplicationContextUtil.getBean("constantDefineServiceImpl");
    if (desc){
        String result = "";
        try {
            if (StringUtils.isNotEmpty(keyType) && keyType.equals("CONSTANT_DEFINE_ID")){
                ConstantDefine constantDefine = constantdefineservice.findById(Long.parseLong(key));
                if (constantDefine!=null)
                    result = constantDefine.getConstantName();
            }else{
                ConstantDefine contant = new ConstantDefine();
                contant.setConstantValue(key.trim());
                contant.setConstantTypeCode(constantTypeCode);
                if(parentkey==null){
                    ConstantDefine constantByTypeCodeAndValue = constantdefineservice.findConstantByTypeCodeAndValue(contant);
                    if (constantByTypeCodeAndValue!=null)
                        result = constantByTypeCodeAndValue.getConstantName();
                }else{
                    ConstantDefine constant = constantdefineservice.findByTypeCodeAndValueAndParentValue(constantTypeCode, key, parentkey);
                    if (constant!=null)
                        result = constant.getConstantName();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        request.setAttribute("detail",result);

    }else{
        List<ConstantDefine> constantDefines = constantdefineservice.getConstantDefinesByType(constantTypeCode);
        request.setAttribute("constantDefines",constantDefines);
    }
%>
<c:if test="${desc eq 'false'}">

    <select id="${id}" class="easyui-combobox" name="${name}" style="
    <c:if test="${empty style}">width:200px;</c:if>
    <c:if test="${not empty style}">${style}</c:if>
    ">
        <c:if test="${isWholeUse}">
            <option value="${wholeValue}" selected="selected">全部</option>
        </c:if>
        <c:if test="${keyType eq 'CONSTANT_DEFINE_ID'}">
            <c:forEach items="${constantDefines}" var="constantDefine">
                <option value="${constantDefine.constantDefineId}"
                        <c:if test="${constantDefine.constantDefineId eq key}">selected="selected"</c:if>
                        >${constantDefine.constantName}</option>
            </c:forEach>
        </c:if>
        <c:if test="${empty keyType }">
            <c:forEach items="${constantDefines}" var="constantDefine">
                <option value="${constantDefine.constantValue}"
                        <c:if test="${constantDefine.constantValue eq key}">selected="selected"</c:if>
                        >${constantDefine.constantName}</option>
            </c:forEach>
        </c:if>


    </select>
</c:if>
<c:if test="${desc eq 'true'}">
    ${detail}
</c:if>

