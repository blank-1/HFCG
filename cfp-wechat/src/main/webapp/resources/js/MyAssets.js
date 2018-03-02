// JavaScript Document
//设定数字位数随字符数改变字体
$.each($(".xsize"),function(){
	var len = $(this).text().length;
	if(len<8){
		$(this).css("font-size","2rem");
	}else if(len>=8 && len<11){
	$(this).css("font-size","1.6rem");
	}else if(len>=11){
	$(this).css("font-size","1.2rem");	
	}
});
$.each($(".ysize"),function(){
	var len = $(this).text().length;
	if(len<8){
		$(this).css("font-size","3rem");
	}else if(len>=8 && len<11){
	$(this).css("font-size","2.6rem");
	}else if(len>=11){
	$(this).css("font-size","2.2rem");	
	}
});
$("#l_tips").on("click",function(e){
	e.preventDefault();
    e.stopPropagation();
    $("#l_formula").hasClass("l_wid")?$("#l_formula").removeClass("l_wid").slideUp():$("#l_formula").addClass("l_wid").slideDown();
})
$("#list1").on("click",function(e){
	e.preventDefault();
    e.stopPropagation();
    var flag=$("#dis_flag").val();
    if (flag=="1") {
      location.href=rootPath+"/person/account/myRecommend";
    }else{
      $(".mask").show();
    }
})
$("#listInfoClose").on("click",function(e){
	e.preventDefault();
    e.stopPropagation();
    $(".mask").hide();
})
$("#moreInformation").click(function(){ 
 window.location.href= rootPath+"/person/moreInformation";
	
}); 
 
	//rem自适应字体大小方法
	var docEl = document.documentElement,
	    //当设备的方向变化（设备横向持或纵向持）此事件被触发。绑定此事件时，
	    //注意现在当浏览器不支持orientationChange事件的时候我们绑定了resize 事件。
	    //总来的来就是监听当然窗口的变化，一旦有变化就需要重新设置根字体的值
	resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
	recalc = function() {
	    //设置根字体大小
	    docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
	};
	//绑定浏览器缩放与加载时间
	window.addEventListener(resizeEvt, recalc, false);
	document.addEventListener('DOMContentLoaded', recalc, false);
