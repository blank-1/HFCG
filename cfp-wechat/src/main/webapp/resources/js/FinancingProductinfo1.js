// JavaScript Document
//环形进度条函数

 $(function() {
	 
			$('.circle').each(function(index, el) {
				var x = 0
				var y = 200000;
				var num = x / y  * 360;
				if (num<=180) {
					$(this).find('.right').css('transform', "rotate(" + num + "deg)");
				} else {
					$(this).find('.right').css('transform', "rotate(180deg)");
					$(this).find('.left').css('transform', "rotate(" + (num - 180) + "deg)");
				};
			});

		});
//详情页页面的收益计算
function sy(){
if (money.value != '起投金额(100元)' ){
	var a=document.getElementById('money').value;
	/*-- 下方的0.02为暂定利率，并表流小数点后两位。可根据情况设置变量--*/
	document.getElementById("sy").innerHTML = (a*0.02).toFixed(2);
}else{
	document.getElementById("sy").innerHTML = "0.00"
}
};
//借款人基本信息展开效果
function down(){
	var down = document.getElementById('down');
	var arrow = document.getElementById('arrow');
if (down.style.display == 'none' ){
	down.style.display = 'block'
	arrow.src='img/arrow1.png'; 
}else{
	down.style.display = 'none'
	arrow.src='img/arrow.png';
}
};
//资金信息切换效果
	var lefttab = document.getElementById('lefttab');
	var righttab = document.getElementById('righttab');
	var info = document.getElementById('info');
	var list = document.getElementById('list');
function a(){
	 $('.lefttab').removeClass('borderh');
			 $('.righttab').addClass('borderh');
			 $('.info').css('display','block');
			 $('.list').css('display','none');

};
function b(){
	 $('.lefttab').addClass('borderh');
			 $('.righttab').removeClass('borderh');
			 $('.list').css('display','block');
			 $('.info').css('display','none');
};
