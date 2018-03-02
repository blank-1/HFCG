<%@ page import="com.xt.cfp.core.constants.CardType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp"%>
<html>
<head>
    <title></title>

</head>
<body>
<form class="form-horizontal" id="add_customer_Card_form" method="post" action="">
	<input type="hidden" name="userId" id="userId" value="${user.userId}"/>
	<input type="hidden" name="cardCustomerName" id="cardCustomerName" value="${user.realName}"/>
	<input type="hidden" name="customerCardId" id="customerCardId" value="${card.customerCardId}"/>
    <div class="control-group">
        <label class="control-label">持卡人：</label>
        <div class="controls">
            ${user.realName}
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>持卡类型：</label>
        <div class="controls">
		<select name="cardType" style="width: 200px" id="cardType" class="easyui-combobox" editable="false">
            <option <c:if test="${card.cardType eq '3'}">selected="selected"</c:if> value="<%=CardType.FULL_CARD.getValue()%>">全能卡</option>
        <%--    <option <c:if test="${card.cardType eq '1'}">selected="selected"</c:if> value="<%=CardType.MONEY_CARD.getValue()%>">打款卡</option>
            <option <c:if test="${card.cardType eq '2'}">selected="selected"</c:if> value="<%=CardType.DRAW_CARD.getValue()%>">划扣卡</option>
--%>
        </select>
        </div>
    </div>

    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>银行卡号：</label>

        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="银行卡号不能为空"
                   name="cardCode" id="cardCode" value="${card.cardCode}">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>所属银行：</label>

        <div class="controls">
           <input type="text" style="width: 200px" required="true" missingMessage="所属银行不能为空" editable="false"
                   name="bankCode1" id="bankCode1">
        </div>
    </div>
    <input type="hidden" style="width: 100px"  name="bankCode" id="bankCode">
    <input type="hidden" style="width: 100px"  name="cityCodeHF" id="cityCodeHF">
    <input type="hidden" style="width: 100px"  name="registeredBank" id="registeredBank">
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>开户支行：</label>
        <div class="controls">
            <input type="text" style="width: 200px" id="province" readonly="readonly">
      <%--      <input type="text" name="provinceCityId" style="width: 100px" id="city">--%>
        </div>
    </div>

    <%--<div class="control-group">
        <label class="control-label"><span style="color: red">*</span>开户支行：</label>
        <div class="controls">
            <input id="bank_name_front" type="text" name="registeredBank" class="easyui-validatebox" value="${card.registeredBank}" required="true" missingMessage="开户支行不能为空" style="width: 181px;position: absolute;">
            <input id="bank_name"  type="text" style="width: 200px">
            <input id="bankLineNumber" name="bankLineNumber" type="hidden" style="width: 200px" value="${card.bankLineNumber}">
        </div>
    </div>--%>

	<%--<div class="control-group">
        <label class="control-label"><span style="color: red">*</span>预留手机号：</label>

        <div class="controls">
        	<input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="预留手机号不为空"
                   name="mobile" id="mobile" value="${card.mobile}" }>
        </div>
    </div>--%>
</form>
</div>
<script language="javascript" >
<%--$("#bankCode").combobox({--%>
    <%--url: '${ctx}/jsp/custom/customer/searchBank?typeCode=bank',--%>
    <%--textField: 'bankValue',--%>
    <%--valueField: 'bankId',--%>
    <%--onLoadSuccess:function(){--%>
        <%--$("#bankCode").combobox("select",'${card.bankCode}');--%>
    <%--}--%>
<%--});--%>

//加载省份下拉框【籍贯】。

$("#bank_name").combobox({
    url:'${ctx}/jsp/custom/customer/getBankName',
    textField: 'brabank_name',
    valueField: 'prcptcd',
    method:'get',
    onSelect: function (rec) {
       $("#bank_name_front").val(rec.brabank_name);
       $("#bankLineNumber").val(rec.prcptcd);
    }
});


$("#bank_name_front").blur(function(){
    var name = Trim($("#bank_name_front").val(),"g") ;
    var cardNo = $("#cardCode").val();
    var bankCode1 = $("#bankCode1").val();

    var cityId = Trim($("#city").combobox("getValue"),"g") ;
    if(cardNo==''){
        $.messager.alert('提示', '请先输入银行卡号', 'info');
        return ;
    }
    if(cityId==''){
        $.messager.alert('提示', '请先选择省市', 'info');
        return ;
    }
    if(name==''){
        $("#bank_name_front").val("")
        $.messager.alert('提示', '开户支行不能为空', 'info');
        return ;
    }

  $("#bank_name").combobox("reload",
            '${ctx}/jsp/custom/customer/getBankName?cardNo='+cardNo+'&bankName='+encodeURIComponent(name)+'&cityId=' +cityId);


});

$("#add_customer_Card_form").form({
    url: '${ctx}/jsp/custom/customer/saveCustomerCard',
    onSubmit: function () {
        var result = $("#add_customer_Card_form").form("validate");
        if(!luhmCheck($("#cardCode").val())){
        	$.messager.alert("系统提示", "银行卡格式错误!", "error");
        	result= false;
        	return result;
        }
        var bankCode1 = $("#bankCode1").val();
        $("#capAcntNo").val($("#cardCode").val());
        console.log("======");
        if(bankCode1==''){
            $.messager.alert('提示', '不支持的银行卡，请联系网站管理员', 'info');
            result= false;
            return result;
        }
        var province = $("#province").val();

        if(province==''){
            $.messager.alert('提示', '开户地址不能为空', 'info');
            result= false;
            return result;
        }
    },
    success: function (data) {
        Utils.loaded();

        if (data == "success") {
            $.messager.alert("系统提示", "保存成功!", "info");
            parent.$("#toAddCustomerCard").dialog("close");
            //刷新tab页面
            parent.$("#customer_card_list").datagrid("reload",{});
        }else if(data == "-1"){
        	$.messager.alert("系统提示", "普通用户只能拥有一张银行卡!", "error");
        }else{
            var d = eval("(" + data + ")");
        	$.messager.alert("系统提示", "新增银行卡号失败!"+d.errMsg, "error");
        }
    }
})

//银行卡校验
function luhmCheck(bankno) {
	var lastNum = bankno.substr(bankno.length - 1, 1);//取出最后一位（与luhm进行比较）

	var first15Num = bankno.substr(0, bankno.length - 1);//前15或18位
	var newArr = new Array();
	for (var i = first15Num.length - 1; i > -1; i--) { //前15或18位倒序存进数组
		newArr.push(first15Num.substr(i, 1));
	}
	var arrJiShu = new Array(); //奇数位*2的积 <9
	var arrJiShu2 = new Array(); //奇数位*2的积 >9

	var arrOuShu = new Array(); //偶数位数组
	for (var j = 0; j < newArr.length; j++) {
		if ((j + 1) % 2 == 1) {//奇数位
			if (parseInt(newArr[j]) * 2 < 9)
				arrJiShu.push(parseInt(newArr[j]) * 2);
			else
				arrJiShu2.push(parseInt(newArr[j]) * 2);
		} else
			//偶数位
			arrOuShu.push(newArr[j]);
	}

	var jishu_child1 = new Array();//奇数位*2 >9 的分割之后的数组个位数
	var jishu_child2 = new Array();//奇数位*2 >9 的分割之后的数组十位数
	for (var h = 0; h < arrJiShu2.length; h++) {
		jishu_child1.push(parseInt(arrJiShu2[h]) % 10);
		jishu_child2.push(parseInt(arrJiShu2[h]) / 10);
	}

	var sumJiShu = 0; //奇数位*2 < 9 的数组之和
       var sumOuShu=0; //偶数位数组之和
       var sumJiShuChild1=0; //奇数位*2 >9 的分割之后的数组个位数之和
	var sumJiShuChild2 = 0; //奇数位*2 >9 的分割之后的数组十位数之和
	var sumTotal = 0;
	for (var m = 0; m < arrJiShu.length; m++) {
		sumJiShu = sumJiShu + parseInt(arrJiShu[m]);
	}

	for (var n = 0; n < arrOuShu.length; n++) {
		sumOuShu = sumOuShu + parseInt(arrOuShu[n]);
	}

	for (var p = 0; p < jishu_child1.length; p++) {
		sumJiShuChild1 = sumJiShuChild1 + parseInt(jishu_child1[p]);
		sumJiShuChild2 = sumJiShuChild2 + parseInt(jishu_child2[p]);
	}
	//计算总和
	sumTotal = parseInt(sumJiShu) + parseInt(sumOuShu)
			+ parseInt(sumJiShuChild1) + parseInt(sumJiShuChild2);

	//计算Luhm值
	var k = parseInt(sumTotal) % 10 == 0 ? 10 : parseInt(sumTotal) % 10;
	var luhm = 10 - k;

	if (lastNum == luhm) {
		return true;
	} else {
		return false;
	}
}





</script>



<link rel="stylesheet" type="text/css" href="${ctx}/newjs/openaccount.css">
<link rel="stylesheet" type="text/css" href="${ctx}/newjs/reset.css">
<link rel="stylesheet" type="text/css" href="${ctx}/newjs/top.css">
<link rel="stylesheet" type="text/css" href="${ctx}/newjs/dialog.css">
<script type="text/javascript" src="${ctx}/newjs/functions.js"></script>
<script type="text/javascript" src="${ctx}/newjs/bankCityAddress.js"></script>
<script type="text/javascript" src="${ctx}/newjs/openaccount.js"></script>
<div class="adr_box" style="display: none;">
    <ul class="province_list">

        <li procode="110" class="">北京市</li><li procode="120">天津市</li><li procode="130">河北省</li><li procode="140">山西省</li><li procode="150">内蒙古自治区</li><li procode="210">辽宁省</li><li procode="220">吉林省</li><li procode="230">黑龙江省</li><li procode="310">上海市</li><li procode="320">江苏省</li><li procode="330">浙江省</li><li procode="340">安徽省</li><li procode="350">福建省</li><li procode="360">江西省</li><li procode="370">山东省</li><li procode="410">河南省</li><li procode="420" class="">湖北省</li><li procode="430">湖南省</li><li procode="440" class="province_current">广东省</li><li procode="450">广西壮族自治区</li><li procode="460">海南省</li><li procode="500">重庆市</li><li procode="510">四川省</li><li procode="520">贵州省</li><li procode="530">云南省</li><li procode="540">西藏自治区</li><li procode="610">陕西省</li><li procode="620">甘肃省</li><li procode="630">青海省</li><li procode="640">宁夏回族自治区</li><li procode="650">新疆维吾尔自治区</li></ul>
    <ul class="city_list"><li citycode="5810">广州</li><li citycode="5820">韶关市</li><li citycode="5822">始兴县</li><li citycode="5823">南雄市</li><li citycode="5824">仁化县</li><li citycode="5825">乐昌市</li><li citycode="5826">翁源县</li><li citycode="5827">乳源瑶族自治县</li><li citycode="5828">新丰县</li><li citycode="5840" class="city_current">深圳</li><li citycode="5850">珠海市</li><li citycode="5860">汕头市</li><li citycode="5863">南澳县</li><li citycode="5880">佛山市</li><li citycode="5890">江门市</li><li citycode="5892">台山市</li><li citycode="5893">恩平市</li><li citycode="5894">开平市</li><li citycode="5895">鹤山市</li><li citycode="5910">湛江市</li><li citycode="5911">吴川市</li><li citycode="5912">廉江市</li><li citycode="5913">遂溪县</li><li citycode="5914">雷州市</li><li citycode="5915">徐闻县</li><li citycode="5920">茂名市</li><li citycode="5921">信宜市</li><li citycode="5922">高州市</li><li citycode="5923">电白县</li><li citycode="5924">化州市</li><li citycode="5930">肇庆市</li><li citycode="5931">高要市</li><li citycode="5932">四会市</li><li citycode="5933">广宁县</li><li citycode="5934">怀集县</li><li citycode="5935">封开县</li><li citycode="5936">德庆县</li><li citycode="5950">惠州市</li><li citycode="5952">博罗县</li><li citycode="5953">惠东县</li><li citycode="5954">龙门县</li><li citycode="5960">梅州市</li><li citycode="5962">大埔县</li><li citycode="5963">丰顺县</li><li citycode="5964">五华县</li><li citycode="5965">兴宁市</li><li citycode="5966">平远县</li><li citycode="5967">蕉岭县</li><li citycode="5970">汕尾市</li><li citycode="5971">海丰县</li><li citycode="5972">陆丰市</li><li citycode="5973">陆河县</li><li citycode="5980">河源市</li><li citycode="5981">紫金县</li><li citycode="5982">龙川县</li><li citycode="5983">连平县</li><li citycode="5984">和平县</li><li citycode="5985">东源县</li><li citycode="5990">阳江市</li><li citycode="5991">阳西县</li><li citycode="5992">阳春市</li><li citycode="5993">阳东县</li><li citycode="6010">清远市</li><li citycode="6011">佛冈县</li><li citycode="6012">英德市</li><li citycode="6013">阳山县</li><li citycode="6014">连州市</li><li citycode="6015">连山壮族瑶族自治县</li><li citycode="6016">连南瑶族自治县</li><li citycode="6017">清新县</li><li citycode="6020">东莞市</li><li citycode="6030">中山市</li><li citycode="5869">潮州市</li><li citycode="5862">饶平县</li><li citycode="5865">揭阳市</li><li citycode="5871">揭东县</li><li citycode="5866">揭西县</li><li citycode="5868">惠来县</li><li citycode="5867">普宁市</li><li citycode="5937">云浮市</li><li citycode="5938">新兴县</li><li citycode="5939">郁南县</li><li citycode="5941">罗定市</li><li citycode="5821">曲江县</li><li citycode="5861">澄海市</li><li citycode="5864">潮阳市</li><li citycode="5872">潮安营业部</li><li citycode="5956">大亚湾营业部</li></ul>
</div>
</div>
</li>

</ul>

</div>


<style>

    .adr_box {
        display: none;
        position: absolute;
        top: 37px;
        left: -9px;
        width: 462px;
        height: 200px;
        padding: 5px 0 10px;
        border: 2px solid #cadef0;
        border-radius: 3px;
        background: #fff;
        z-index: 99;
    }


    .province_list {
        float: left;
        width: 238px;
        height: 100%;
        border-right: 2px solid #cadef0;
        overflow-y: auto;
    }

</style>
</body>
</html>