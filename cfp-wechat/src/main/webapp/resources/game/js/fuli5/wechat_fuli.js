window.onload=function(){
	$.ajax({
		url:"http://m.caifupad.com/user/getConfig",
		type:"post",
		success: function (data) {
			var _data = eval('('+data+')');
			//alert(_data.isSuccess);
			if(_data.isSuccess){
				//alert(timestamp+"==="+nonceStr+"==="+signature);
				var resultMap = _data.resultMap
				//alert(resultMap.timestamp);
				timestamp = resultMap.timestamp;
				nonceStr = resultMap.nonceStr;
				signature = resultMap.signature;
				//alert(timestamp+"==="+nonceStr+"==="+signature);
				wx.config({
				      debug: false,
				      appId: resultMap.appid,
				      timestamp: resultMap.timestamp,
				      nonceStr: resultMap.nonceStr,
				      signature: resultMap.signature,
				      jsApiList: [
				        'onMenuShareTimeline',
				        'onMenuShareAppMessage',
				        'hideAllNonBaseMenuItem',
				        'hideMenuItems',
				        'showAllNonBaseMenuItem'
				      ]
				  });
			}
		}	
	});
}

wx.ready(function () {

	wx.onMenuShareAppMessage({
	   title: '庆圣诞，迎元旦',
	   desc: '双节同庆，福礼同享',
	   link: 'http://m.caifupad.com/gamejs/fuli5/ActivePageForChristmas.html',
	   imgUrl: 'http://m.caifupad.com/gamejs/fuli5/fuli_share.jpg',
	   trigger: function (res) {
		   // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
		   //alert('用户点击发送给朋友');
	   },
	   success: function (res) {
	     //alert('已分享');
	   },
	   cancel: function (res) {
	     //alert('已取消');
	   },
	   fail: function (res) {
	     alert("分享失败");
	   }
	});
	//2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
	 wx.onMenuShareTimeline({
	   title: '庆圣诞，迎元旦',
	   link: 'http://m.caifupad.com/gamejs/fuli5/ActivePageForChristmas.html',
	   imgUrl: 'http://m.caifupad.com/gamejs/fuli5/fuli_share.jpg',
	   trigger: function (res) {
	     // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
	     //alert('用户点击分享到朋友圈');
	   },
	   success: function (res) {
	     //alert('已分享');
	   },
	   cancel: function (res) {
	     //alert('已取消');
	   },
	   fail: function (res) {
	     alert("分享失败");
	   }
	 });
 
	 wx.hideMenuItems({
	     menuList: [
	       'menuItem:readMode', // 阅读模式
	       'menuItem:copyUrl', // 复制链接
	       'menuItem:share:QZone',//分享到QQ空间	
	       'menuItem:share:weiboApp',//分享到微博
	       'menuItem:openWithQQBrowser',//用QQ浏览器打开
	       'menuItem:openWithSafari',//用safari打开
	       'menuItem:share:facebook', //分享到FB
	       'menuItem:share:qq' //分享到qq
	     ],
	     success: function (res) {
	       //alert('已隐藏“阅读模式”，“分享到朋友圈”，“复制链接”等按钮');
	     },
	     fail: function (res) {
	       //alert(JSON.stringify(res));
	     }
	   });
});


wx.error(function (res) {
  //alert(res.errMsg);
 //window.location.reload();
});

