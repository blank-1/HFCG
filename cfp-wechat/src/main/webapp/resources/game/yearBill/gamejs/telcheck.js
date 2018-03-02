// JavaScript Document
$(function() {
	var overscroll = function(el) {//阻止浏览量默认滚动
		  el.addEventListener('touchstart', function() {
		    var top = el.scrollTop
		      , totalScroll = el.scrollHeight
		      , currentScroll = top + el.offsetHeight
		    if(top === 0) {
		      el.scrollTop = 1
		    } else if(currentScroll === totalScroll) {
		      el.scrollTop = top - 1
		    }
		  })
	  el.addEventListener('touchmove', function(evt) {
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
		$("input").on("focus",function() {
			var placeholder =$(this).attr("placeholder");
			$(this).attr("placeholder","");
			$(this).on("blur",function () {
					$(this).attr("placeholder",placeholder);
			})
		})
		$("#telNum").on("input",function () {
			notNum($(this));
		})
		$("#checkNum").on("input",function () {
			notNum($(this));
			if($(this).val().length==6){
				$("#subBtn").addClass("test2");
			}else {
				$("#subBtn").removeClass("test2");
			}
		})
		$("section>p>button").on("click",function (e) {
			e.stopPropagation();
			if ($("#telNum").val() == "") {
				swal(
					'请填写手机号码',
					'',
					'warning'
				)
				$("#subBtn").removeClass("test1");
			}else if (!(/^1[3|4|5|8|7][0-9]\d{4,8}$/.test($("#telNum").val()))) {
				swal(
					'请填写正确的手机号码',
					'',
					'warning'
				)
				$("#subBtn").removeClass("test1");
			}else{
				$("#subBtn").addClass("test1");
				count($(this));
			};

		})
		$("#subBtn").on("click",function (e) {
			if ($(this).hasClass("test1")&&$(this).hasClass("test2")) {
				alert("走你");
			}else {
				swal(
					'请填写完整信息',
					'',
					'warning'
				)
			}
		})


		function notNum(el) {
			var leg = el.val().length;
			if ((/[^0-9]/g.test(el.val()))) {
			el.val(el.val().substring(0,leg-1));
			}
		}

		function count(el){
			//验证码倒计时效果
			function countDown(num,el) {
				num-=1;
				if (num == 0) {
					el.text("获取验证码");
					el.removeAttr('disabled');
					el.removeClass("l_disable");
				}else{
					el.attr("disabled","");
					el.addClass("l_disable");
					el.text(num + "秒");
					setTimeout(function(){countDown(num--,el)},1000);
				}
			}
			countDown(60,el)

		}

})
