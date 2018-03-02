// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
        "swal":["lib/sweetalert2"]
	}
})
require(['jquery',"swal"],function($,swal) {

	$(function() {
        $("#citylisting").html("");//清理省列表
        $('#areaListing').html("");//清空市列表

		var overscroll = function(el) {//阻止浏览量默认滚动
		  el.addEventListener('touchstart', function() {
		    var top = el.scrollTop
		      , totalScroll = el.scrollHeight
		      , currentScroll = top + el.offsetHeight
		    //If we're at the top or the bottom of the containers
		    //scroll, push up or down one pixel.
		    //
		    //this prevents the scroll from "passing through" to
		    //the body.
		    if(top === 0) {
		      el.scrollTop = 1
		    } else if(currentScroll === totalScroll) {
		      el.scrollTop = top - 1
		    }
		  })
		  el.addEventListener('touchmove', function(evt) {
		    //if the content is actually scrollable, i.e. the content is long enough
		    //that scrolling can occur
		    if(el.offsetHeight < el.scrollHeight)
		      evt._isScroller = true
		  })
		}
		overscroll(document.querySelector('.l_NewScroll'));
		document.body.addEventListener('touchmove', function(evt) {
		  //In this case, the default behavior is scrolling the body, which
		  //would result in an overflow.  Since we don't want that, we preventDefault.
		  if(!evt._isScroller) {
		    evt.preventDefault()
		  }
		})
		$(".Branch-list").on("touchstart touchmove",function(e){
			e.stopPropagation();
		})
		$(".listing").on("touchstart touchmove",function(e){
			e.stopPropagation();
		})
		
	})

	//支行搜索
	$("#search-txt").on("input",function(){
		var context=$("#search-txt").val();
		if(context==null||context.trim()==""){
			if(cid==""){
                $("#search-txt").attr("readonly","readonly");
                $("#Branch-list").slideUp(300);
			}
			return;
		}
        $("#Branch-list").slideDown(300);
        $.ajax({
            url:rootPath + "/person/choiceBranchQueryResult",
            type: "POST",
            dataType: "json",
            data:{"w_seachCid":$("#w_seachCid").val(),"w_seachMag":context},
            success:function(result){
                html = "";
                $.each(result,function(key,val){
                    html+="<li id='" + val.prcptcd + "'>"+val.brabank_name+"</li>";
                })
                $("#Branch-list").html(html);
            },
            error:function(){
                /*swal('查询失败','','warning');*/
            }
        })

	})
	$("#search-txt").on("blur",function(){
		$("#Branch-list").slideUp(300);
	})
	var province="",city="",pid="",cid="";
	//选择省市
	$("#region-box").on("click",function(evet){
		evet.stopPropagation();
        $.ajax({
            url:rootPath + "/person/loadLianLianCitySelect?pProvinceCityId=0",
            type: "POST",
            dataType: "json",
            success:function(data){
                html = "";
                $.each(data,function(key,val){
                    html+="<li id='" + val.id + "'>"+val.text+"</li>";
                })
                $("#citylisting").html(html);
                $("#select-mask").slideDown(100);
            },
            error:function(){
                swal('错误','','warning');
            }
        })

	})

	$("#citylisting").on("click","li",function(evet){
        evet.stopPropagation();
        province=$(this).html();
        pid=$(this).attr("id");
        $.ajax({
            url:rootPath + "/person/loadLianLianCitySelect?pProvinceCityId=" + pid,
            type: "POST",
            dataType: "json",
            success:function(data){
                html = "";
                $.each(data,function(key,val){
                    html+="<li id='" + val.id + "'>"+val.text+"</li>";
                })
                $("#areaListing").html(html);
                $(".area").addClass("cur").siblings().removeClass("cur");
                $("#cityBox").animate({
                    "margin-left":-1*($("#citylisting").width())+'px'
                },300);
            },
            error:function(){
                swal('错误','','warning');
            }
        })
	})

	$("#areaListing").on("click","li",function(){
        city=$(this).html();
        cid=$(this).attr("id");
		$("#address").html(province +"&nbsp;&nbsp;"+ city);
        $("#w_seachCid").val(cid);
        $("#search-txt").removeAttr("readonly");
	})

	$(document).on("click",function(){	
		$("#select-mask").slideUp(300);		
	})

	$("#Branch-list").on("click","li",function(evet){//获取支行信息
		evet.stopPropagation();
		var thisVal = $(this).html();	
		$("#search-txt").val(thisVal);
        $("#prcptcd").attr("value",$(this).attr("id"));
        $("#brabank_name").attr("value",thisVal);
        $("#brabank_city").attr("value",cid);
	})
	
	$("#Selected").on("click","span",function(evet){//城市tab切换
		evet.stopPropagation();
		$(this).addClass("cur").siblings().removeClass('cur');
		var thisInd = $(this).index(),
			cityBoxW = $("#cityBox ul:first-child").width();
			console.log(cityBoxW)
		$("#cityBox").animate({
			"margin-left":-(thisInd)*(cityBoxW)+'px'
		},300)
	})

    $("#search_btn").on("click",function(evet){//获取支行信息
        var context=$("#search-txt").val();
        if(context==null||context.trim()==""){
            if(cid==""){
                $("#search-txt").attr("readonly","readonly");
                $("#Branch-list").slideUp(300);
            }
            return;
        }
        $("#Branch-list").slideDown(300);
        $.ajax({
            url:rootPath + "/person/choiceBranchQueryResult",
            type: "POST",
            dataType: "json",
            data:{"w_seachCid":$("#w_seachCid").val(),"w_seachMag":context},
            success:function(result){
                html = "";
                $.each(result,function(key,val){
                    html+="<li id='" + val.prcptcd + "'>"+val.brabank_name+"</li>";
                })
                $("#Branch-list").html(html);
            },
            error:function(){
				swal('查询失败','','warning');
            }
        })
    })

    $(".sure").on("click",function(evet){
        evet.stopPropagation();
        $("#branchForm").submit();
	})
})
