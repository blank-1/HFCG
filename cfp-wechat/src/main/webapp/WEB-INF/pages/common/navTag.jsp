<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="${ctx }/css/home.css">
<ul class="l_bottomTap" style="font-size:62.5%;">
	<a href="${ctx}/?<%=Math.random()%>" style="display:block"><li class="l_homeR"><span></span><span>首页</span></li></a>
	<li class="l_moneyG" onclick="location.href='${ctx}/finance/list?<%=Math.random()%>'"><span></span><span>理财</span></li>
	<li class="l_positionG" onclick="location.href='${ctx}/person/toEntityShop?<%=Math.random()%>'"><span></span><span>体验店</span></li>
	<c:if test="${empty sessionScope.currentUser}">
		<li class="l_personG" onclick="location.href='${ctx}/user/toLogin?<%=Math.random()%>'"><span></span><span>我的</span></li>
	</c:if>
	<c:if test="${not empty sessionScope.currentUser}">
		<li class="l_personG" onclick="location.href='${ctx}/person/account/overview?<%=Math.random()%>'"><span></span><span>我的</span></li>
	</c:if>
</ul>
 <input value="${usersources }" id="sources"  type="hidden"/> 
<script type="text/javascript">
	$(document).ready(function(){
		if($("#sources").val()!=""){
			//app
			$(".l_bottomTap").hide();
		}
		var lis=$(".l_bottomTap").find("li");
		$(lis).each(function(i,v){
			var cl=$(v).attr("class");
			cl=cl.replace("R","G");
			$(v).attr("class",cl);
		});
		var url=location.href;
		if(url.indexOf("/finance/list")>0){
			var li=lis[1];
			var cl=$(li).attr("class");
			cl=cl.replace("G","R");
			$(li).attr("class",cl);
		}else if(url.indexOf("/person/toEntityShop")>0){
			var li=lis[2];
			var cl=$(li).attr("class");
			cl=cl.replace("G","R");
			$(li).attr("class",cl);
		}else if(url.indexOf("/person/account/overview")>0){
			var li=lis[3];
			var cl=$(li).attr("class");
			cl=cl.replace("G","R");
			$(li).attr("class",cl);
		}else{
			var li=lis[0];
			var cl=$(li).attr("class");
			cl=cl.replace("G","R");
			$(li).attr("class",cl);
		}
	})
</script>
<script>
var overscroll = function(el) {
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
</script>