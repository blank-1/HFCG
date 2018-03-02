<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.LendProduct" %>
<%@ page import="com.xt.cfp.core.pojo.FeesItem" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="product_toSelLoanProduct_form" method="post">
    <div class="control-group">
        <label class="control-label">选择借款产品<span style="color: red">*</span></label>

        <div class="controls">
            <input id="loanProduct" name="loanProduct"
                   required="required" missingMessage= '请选择借款产品'
                    />
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">占比<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text"
                   class="easyui-numberbox" required="required" style="width: 80px" data-options="min:0,precision:2,max:100,value:0"
                   name="ratio" id="ratio" />%
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">费用<span style="color: red">*</span></label>

        <div class="controls">
            <input id="feeItem" name="feeItem" required="required" missingMessage= '请选择费用项目' />
        </div>
    </div>


</form>
<script language="JavaScript">
    $("#product_toSelLoanProduct_form #loanProduct").combobox({
        url: '${ctx}/jsp/product/loan/getLoanProductList?productType=${productType}',
        valueField: 'loanProductId',
        textField: 'productName'
    })
    $("#product_toSelLoanProduct_form #feeItem").combobox({
        url: '${ctx}/jsp/product/feesitem/getFeesItemsByItemKind?itemKind=<%=FeesItem.ITEMKIND_LEND%>&lendTypeSelect=1',//+$("#lendTypeSelect").val(),
        valueField: 'feesItemId',
        textField: 'itemName'
    })

    $("#product_toSelLoanProduct_form").form({
        onSubmit:function(data) {
            var result = $("#product_toSelLoanProduct_form").form("validate");
            if(!result){
                return result;
            }
            parent.$("#product_addLendProduct_form #loanProduct_list").datagrid("appendRow",{
                loanProductId:$("#product_toSelLoanProduct_form #loanProduct").combobox("getValue"),
                productName:$("#product_toSelLoanProduct_form #loanProduct").combobox("getText"),
                ratioStr:$("#product_toSelLoanProduct_form #ratio").val() + "%",
                ratio:$("#product_toSelLoanProduct_form #ratio").val()*1/100,
                itemName:$("#product_toSelLoanProduct_form #feeItem").combobox("getText"),
                feesItemId:$("#product_toSelLoanProduct_form #feeItem").combobox("getValue")
            });
            parent.$("#product_addLendProduct_form #existLoanRatio").val(parent.$("#product_addLendProduct_form #existLoanRatio").val()*1 + $("#product_toSelLoanProduct_form #ratio").val()*1/100);
            parent.$("#product_addLendProduct_form #loanProduct_list").datagrid("acceptChanges");

            $("#toAddLoanProduct").dialog('close');
            return false;
        }
    })


</script>
</body>
</html>
