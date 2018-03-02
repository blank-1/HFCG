// JavaScript Document
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"publicJS":["main"],
		"swal":["lib/sweetalert2"],
	}
})
require(['jquery','publicJS','swal'],function($,publicJS,swalr) {
	$(function() {
			var overscroll = function(el) {
			el.addEventListener('touchstart', function() {
				var top = el.scrollTop
					, totalScroll = el.scrollHeight
					, currentScroll = top + el.offsetHeight
				if(top === 0) {
					el.scrollTop = 1
				} else if(currentScroll === totalScroll) {
					el.scrollTop = top - 1
				}
			});
			el.addEventListener('touchmove', function(evt) {
				if(el.offsetHeight < el.scrollHeight)
					evt._isScroller = true
			});
		};
		overscroll(document.querySelector('.l_NewScroll'));
		document.body.addEventListener('touchmove', function(evt) {
			if(!evt._isScroller) {
				evt.preventDefault()
			}
		});

        loadCarList();
        var loanId=$("#creditorRightsId").val();
        if(loanId!=null&&loanId!=undefined&&loanId!=""){
            $("#reqUrl").val("/finance/getCreditorRightsLender");
		}else{
            $("#reqUrl").val("/finance/getLender");
		}

        var pageNo=1,pageSize=20,totalPage=1;
        loadList(pageNo,pageSize);

		$("header p").on("click",function(){
			$(this).addClass("l_focus").siblings().removeClass("l_focus");
			switch($(this).index()){
				case 0:
					$(".l_borrowInfo").show().siblings("section").hide();
					break;
				case 1:
					$(".l_photoBox").show().siblings("section").hide();
					break;
				case 2:
					$(".l_photoBox2").show().siblings("section").hide();
					break;
				case 3:
					$(".l_borrowList").show().siblings("section").hide();
					break;
				case 4:
					$(".l_repayList").show().siblings("section").hide();
					break;
			}

		})
		publicJS.swiper("l_sections");
		if($(".l_sections2").has("li")){	publicJS.swiper("l_sections2");}

		$(".l_repayList>ul").on("touchstart touchmove",function(e) {
			e.stopPropagation();
		})

		$(document).on("scroll",function() {
			if (!$(".l_borrowList").is(":hidden")) {
				if (publicJS.scrollLoading("l_borrowList","l_NewScroll")) {
                    pageNo+=1;
                    if(pageNo<=totalPage){
                        loadList(pageNo,pageSize);
                        return false;
                    }else{
						$(".l_lastTip").html("已加载完全部数据");
					}
				}
			}
		})

        function loadCarList(){
            var html = "";
            $.ajax({
                url: rootPath + "/finance/getCardList",
                type: "post",
                data: {
                    "pageSize": 200,
                    "pageNo": 1,
                    "loanApplicationId": $("#loanApplicationId").val()
                },
                success: function (data) {
                    var d_rows = data.rows;
                    if(d_rows!=undefined&&d_rows.length>0){
                        var pageCount = data.totalPage;
                        for (var i = 0; i < d_rows.length; i++) {
                            var data = d_rows[i];
                            var change = data.changeDesc == null ? "" : data.changeDesc;
                            html+="<table class='l_carTable' cellspacing='0'>"+
									"<tr>"+
										"<td>类型</td>"+
										"<td>汽车品牌</td>"+
									"</tr>"+
									"<tr>"+
										"<td>"+data.arrived+"抵</td>"+
										"<td>"+data.automobileBrand+"</td>"+
									"</tr>"+
									"<tr>"+
										"<td>汽车型号</td>"+
										"<td>市场价格（万）</td>"+
									"</tr>"+
									"<tr>"+
										"<td>"+data.carModel+"</td>"+
										"<td>"+fmoney(data.marketPrice, 2)+"</td>"+
									"</tr>"+
									"<tr>"+
										"<td>车架号</td>"+
										"<td>变更信息</td>"+
									"</tr>"+
									"<tr>"+
										"<td>"+data.frameNumber+"</td>"+
										"<td>"+change+"</td>"+
									"</tr>"+
							      "</table>"
                        }
                        $('#carInfo').html(html);
                    }
                }
            });
        }

        function loadList(page,rows){
            var url=$("#reqUrl").val();
            var html="";
            $.ajax({
                url:rootPath+url,
                type:"post",
                data: {
                    "pageSize": rows,
                    "pageNo": page,
                    "loanApplicationId":$("#loanApplicationId").val(),
                    "creditorRightsId":$("#creditorRightsId").val()
                },
                success: function (data) {
                    var d_rows = data.rows;
                    totalPage = data.totalPage;
                    pageNo = data.currentPage;
                    for (var i = 0; i < d_rows.length; i++) {
                        var data = d_rows[i];
                        html+="<li>"+
								"<p>"+data.jmLendName+"</p>"+
								"<p>"+dateTimeFormatter(data.lendTime)+"</p>"+
								"<p>"+data.lendAmount.toFixed(2)+"元</p>"+
						      "</li>";
                    }
                    $('#touzi_list').append(html);
					if(page==totalPage){
                        $(".l_lastTip").html("已加载完全部数据");
					}
                }
            });
        }


    })
//js结尾
})
