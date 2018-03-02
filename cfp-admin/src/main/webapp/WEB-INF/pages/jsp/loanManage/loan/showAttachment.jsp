<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<head>
</head>
<body>
<div id="config" class="easyui-layout"  fit=true>
    <div data-options="region:'west',split:true" style="width:250px;border-left:0px;border-bottom:0px;">
        <ul id="categoryTree"></ul>
        <div id="mm" class="easyui-menu" style="padding:5px;width:150px;"></div>
    </div>
    <div id="centerArea" data-options="region:'center'" style="border: 0px;width: 100%;height: 100%;">
        <%--<iframe id="content" src="#" class="easyui-layout" style="border: 0px;width: 100%;height: 100%;" fit=true></iframe>--%>
    </div>
</div>
<div style="margin-bottom: 70px;"></div>
<script type="text/javascript">

    $(function() {
        initTree();

    });
    function initTree(){
        var _ROOT = -1;
        $('#categoryTree').tree({
            url:'${ctx}/jsp/loanManage/loan/attachment?loanApplicationId=${loanApplicationId}',
            loadFilter: function(data){
                if (data.nodes){
                    return data.nodes;
                } else {
                    return data;
                }
            },
            onSelect: function(node){
                if(node.id != _ROOT){
                    $("#centerArea").load("${ctx}/jsp/loanManage/loan/detailAttachment?type="+node.id+"&loanApplicationId="+node.attributes.loanApplicationId);
                    <%--$("#content").attr("src","${ctx}/jsp/loanManage/loan/detailAttachment?type="+node.id+"&loanApplicationId="+node.attributes.loanApplicationId);--%>
                }
            },
            onLoadSuccess:function(node,data){
                $("#categoryTree ul li:eq(0)").find("div").addClass("tree-node-selected");   //设置第一个节点高亮
                var n = $("#categoryTree").tree("getSelected");
                if(n!=null)
                    $("#categoryTree").tree("select",n.target);    //相当于默认点击了一下第一个节点，执行onSelect方法
            }
        });

        $("#content").attr("src","");
    }
</script>
</body>