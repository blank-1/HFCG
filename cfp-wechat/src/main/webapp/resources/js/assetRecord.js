var pageNo,pageSize=20,totalPage;
$(function(){
    pushData(0);
    $("#w_vouList").on("click","li",function(){
    	if($(this).find("a").hasClass("w_active")){
    		return false;
		}
        var thisIndex = $(this).index(),
            moveW = $(".w_page").width();
        $(this).find("a").addClass("w_active").parent().siblings("li").find("a").removeClass("w_active");
        $(".orderList").animate({
            "margin-left":-thisIndex*moveW+"px"
        },300);
        if(thisIndex==0){
            $("#w_value").val('0');
        }else if(thisIndex==1){
            $("#w_value").val('1');
        }else if(thisIndex==2){
            $("#w_value").val('4');
        }else if(thisIndex==3){
            $("#w_value").val('2');
        }else if(thisIndex==4){
            $("#w_value").val('5');
        }
        pageNo=1;
        for(var i=0;i<5;i++){
            if(i!=thisIndex){
                $(".pay_page"+i).html("");
            }
        }
        pushData(thisIndex);
    })
})
function pushData(ind){
    $(".page"+ind).on("scroll",function(){
        var scrollTop= $(".page"+ind).find("ul").outerHeight(true),
            selfH=$(this).scrollTop()+$(window).height()+$(".l_lastTips").height()+1;
        if( scrollTop <= selfH){
            if(pageNo<=totalPage){
                searchHtml(pageNo,pageSize,ind);
            }
        }
    });
    searchHtml(pageNo,pageSize,ind);
}
function searchHtml(page,rows,inx){
    var thtml="";
    var w_value = [1,2,3,4,5];//定义一个数组
    if($("#w_value").val()=="1"){
        w_value = [1];
    }else if($("#w_value").val()=="2"){
        w_value = [2,3];
    }else if($("#w_value").val()=="4"){
        w_value = [4];
    }else if($("#w_value").val()=="5"){
        w_value = [5];
    }
    var d_value = ['t_7','t_1','t_6'];
    $.ajax({
        url:""+rootPath+"/person/fundManageList",
        type:"post",
        async:false,
        data:{"pageNo":page,"pageSize":rows,"flowType":w_value,"searchDate":d_value},
        success:function(data){
            var _data = data.rows;
            pageNo = data.currentPage;
            totalPage = data.totalPage;

            if(data.rows.length == 0){
                if(pageNo==1){
                    $(".pay_page"+inx).next().hide();
                    $(".pay_page"+inx).next().next().show();
                }else{
                    $(".pay_page"+inx).next("p[class='l_lastTips']").html("这已经是全部数据了");
                }

            }
            for(var i=0;i<_data.length;i++){
                var dataResult = _data[i];
                var type="";
                var content="";
                if(dataResult.changeType ==2 || dataResult.changeType == 3){
                    type="expenses";
                    content="<span class='zhichu'>支出</span> : <span>"+fmoney(dataResult.changeValue2, 2)+"</span>";
                }else if(dataResult.changeType == 1){
                    type="income";
                    content="<span class='shouru'>收入</span> : <span>"+fmoney(dataResult.changeValue2, 2)+"</span>";
                }else if(dataResult.changeType == 4){
                    type="Frozen";
                    content="<span class='dongjie'>冻结</span> : <span>"+fmoney(dataResult.changeValue2, 2)+"</span>";
                }else if(dataResult.changeType == 5){
                    type="Thaw";
                    content="<span class='jiedong'>解冻</span> : <span>"+fmoney(dataResult.changeValue2, 2)+"</span>";
                }
                var html="<li onclick='toDetail("+dataResult.hisId+")'>"+
                    "<dl>"+
                    "<dd class='"+type+"'> "+
                    "<img src="+rootPath+"/images/"+type+".png>"+
                    "</dd>"+
                    "<dt>"+
                    "<div>"+
                    "<p>"+dataResult.busType+"</p>"+
                    "<p>"+content+"</p>"+
                    "</div>"+
                    "<div>"+
                    "<p>"+dateTimeFormatter(dataResult.changeTime)+"</p>"+
                    "<p>余额 : <span class='w_balance'>"+fmoney(dataResult.valueAfter2, 2)+"</span></p>"+
                    "</div>"+
                    "</dt>"+
                    "</dl>"+
                    "</li>";
                thtml += html;
            }
            $(thtml).appendTo($(".pay_page"+inx));
            //修复加载更多
            if(typeof(page)=="undefined"){
                page=1;
            }
            if(page==data.totalPage){
                $(".pay_page"+inx).next("p[class='l_lastTips']").html("这已经是全部数据了");
            }
            pageNo++;
        }
    });
}


function toDetail(id){
    $("#id").val(id);
    $("#queryForm").submit();
    //location.href=rootPath+"/person/fundManageInfoDetail?id="+id;
}
