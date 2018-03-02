<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../../../common/common.jsp"%>
<html>
<head>
<title></title>
</head>
<body>
	<!--  -->
	<div id="dd"
		style="width: 250px; overflow:auto;height: 380px; display: none; position: absolute; background: #fff; border: 1px red solid; z-index: 99; bottom: 20px; right: 306px; width: 281px">


		<table border='1' cellspacing="0" cellpadding="0" style="width:280px">
			<tr>
				<th>用户名</th>
				<th>姓名</th>
				<th>手机号</th>
			</tr>

			<c:forEach items="${loanOrientation2}" var="list">
				<tr>
					<td>${list.userName}</td>
					<td>${list.logName}</td>
					<td>${list.phone}</td>
				</tr>
			</c:forEach>

		</table>
	</div>
	<!--  -->

	<!-- z-index: 99;
bottom: 20px;
right: 10px; -->

	<div id="dd2"
		style="width: 250px;overflow:auto; height: 380px; display: none; position: absolute; background: #fff; border: 1px black solid; z-index: 99; bottom: 20px; right: 10px; width: 281px">


		<table border='1' cellspacing="0" cellpadding="0" style="width:280px">
			<tr>
				<th>投资人</th>
				<th>投资金额</th>
				<th>出资时间</th>
			</tr>

			<c:forEach items="${userInfo}" var="list">
				<tr>
					<td>${list.logName}</td>
					<td>${list.oPass}</td>
					<td>${list.phone}</td>
				</tr>
			</c:forEach>

		</table>
	</div>
	<!--  -->
	<div>
 	<form class="form-horizontal" id="update_orient_form" method="post"   enctype="multipart/form-data"  >
	<div class="cf"
		style="float: left; width: 99%; margin: 0 10px 5px 10px; height: inherit;">
		<div>
			<span>标的编号:</span> <input type="text" id=""  name="loan"
				style="width: 400px" size=10 readonly="readonly"
				value="${loanApplication.loanApplicationCode}" /> </br> 
				<span>标的标题:</span>
			<input type="text" id="" readonly="readonly" value="${loanApplication.loanApplicationName}" name=""
				style="width: 400px" size=10 /> </br> <span>标的金额:</span> 
				<input type="text" name="confirmBalance" value="${loanApplication.confirmBalance}"
				readonly="readonly" name="" style="width: 400px" size=10 /> </br> <span>已投金额:</span>
			<c:set var="sum" value="0"></c:set>
			<c:forEach items="${userInfo}" var="list">
				<c:set var="sum" value="${sum+list.oPass}"></c:set>
			</c:forEach>
			${sum }元： <input type="button" id="loanCount" readonly="readonly"
				name="loanApplicationName" style="width: 50px; position: absolute"
				size=10 value="查   看" /> </br>
			<div class="control-group">
				</span>定向类型：

				<c:if test="${not empty loanOrientation1}">
					<span>定向密码:</span>
					<input id="" type="text" readonly="readonly"
						style="line-height: 10px;" value="${loanOrientation1.oPassVo}"></input>
					</br>
				</c:if>
				<c:if test="${not empty loanOrientation2}">
                 定向用户:    <input readonly="readonly" id="oUser"
						type="button" style="line-height: 20px;" value="查  看" />
				</c:if>

			</div>
		
		
			<input id=""type="hidden" name="loanApplicationId" value="${loanApplication.loanApplicationId}"></input>
			<div class="control-group">
				<c:if test="${not empty loanOrientation1}">
					</span>设置定向：</label> 
					<input id="radioInputAll"  style="line-height: 10px;" type="radio" name="radioInput" >所有用户</input>
					<input id="radioInputAllValue"  name="allUser" style="line-height: 10px;" type="hidden"  > </input>
			    	</br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
			    	<input id="radioInputPerson" type="radio" name="radioInput" checked="checked" value="" >定向密码</input> 
					<input id="radioInputP" style="line-height: 10px;" type="text"name="opassword"></input> </br> 
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
					<input id="radioInputOnly"  type="radio" style="line-height: 30px;"  value="" name="radioInput">定向用户</input> 
				    <input id="radioInputA1"style="line-height: 10px;" type="file" name="file"></input>
				     <br/>
				     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
				     <input id="UserRadio" type="radio"   name="radioInput" >新手用户</input>
	             <input id="newUserRadio"   value=""  type="hidden" name="newUserRadio" ></input>
				</c:if>
				<c:if test="${not empty loanOrientation2}">
					</span>设置定向：</label> <input id="radioInputAll"  style="line-height: 10px;" type="radio" value="" name="radioInput">所有用户</input>
					<input id="radioInputAllValue" name="allUser" style="line-height: 10px;" type="hidden"  > </input>
			    	</br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
				    <input id="radioInputPerson" type="radio" value="" name="radioInput" on>定向密码</input> 
					<input id="radioInputP" style="line-height: 10px;" type="text"name="opassword"></input> </br> 
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
					<input id="radioInputOnly"type="radio" style="line-height: 30px;" checked="checked" value="" name="radioInput">定向用户</input> 
				    <input id="radioInputA1"style="line-height: 10px;" type="file" name="file"></input>
				     <br/>
				     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
				     <input id="UserRadio" type="radio"   name="radioInput" >新手用户</input>
	             <input id="newUserRadio"   value=""  type="hidden" name="newUserRadio" ></input>
				</c:if>
				
									<c:if test="${not empty loanOrientation3}">
							</span>设置定向：</label> <input id="radioInputAll"
					style="line-height: 10px;" type="radio" name="radioInput" >所有用户</input>
					<input id="radioInputAllValue"  name="allUser" style="line-height: 10px;" type="hidden"  > </input>
			    	</br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
				  &nbsp; &nbsp; <input id="radioInputPerson" type="radio" value="" name="radioInput" on>定向密码</input> 
					<input id="radioInputP" style="line-height: 10px;" type="text"name="opassword"></input> </br> 
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
					<input id="radioInputOnly"type="radio" style="line-height: 30px;" value="" name="radioInput">定向用户</input> 
				    <input id="radioInputA1"style="line-height: 10px;" type="file" name="file"></input>
				    <br/>
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
				     <input id="UserRadio" type="radio"  checked="checked"  name="radioInput" >新手用户</input>
	             <input id="newUserRadio"   value="newUserRadio"  type="hidden" name="newUserRadio" ></input>
				</c:if>
				
				
				<c:if test="${empty loanOrientation2 && empty loanOrientation1 && empty loanOrientation3}">
							</span>设置定向：</label> <input id="radioInputAll" checked="checked"
					style="line-height: 10px;" type="radio" name="radioInput" >所有用户</input>
					<input id="radioInputAllValue"  name="allUser" style="line-height: 10px;" type="hidden"  > </input>
			    	</br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
				  &nbsp; &nbsp; <input id="radioInputPerson" type="radio" value="" name="radioInput" on>定向密码</input> 
					<input id="radioInputP" style="line-height: 10px;" type="text"name="opassword"></input> </br> 
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
					<input id="radioInputOnly"type="radio" style="line-height: 30px;" value="" name="radioInput">定向用户</input> 
				    <input id="radioInputA1"style="line-height: 10px;" type="file" name="file"></input>
				    <br/>
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 
				     <input id="UserRadio" type="radio"   name="radioInput" >新手用户</input>
	             <input id="newUserRadio"   value=""  type="hidden" name="newUserRadio" ></input>
				</c:if>
			
			</div>


		
			
			
			</div>
			</div>
				<input type="submit" style='width: 70px;' value="保存"></input>
</form>
	<!-- <table cellpadding="0" cellspacing="0" style="width: 100%">
					<tr>

						<td style="text-align: right">
					
					   </td>
					</tr>
				</table> -->
</div>
			<script language="JavaScript">
		/* 	$("input[name='radioInput']").click(function(){
				alert("sad");
			}) */
		 
			 
			    $("#update_orient_form").form({
			        url:'${ctx}/jsp/loanPublish/loan/updateOrient',
			        success:function(data) {
			            if (data == 'success') {
			                $.messager.alert("系统提示", "定向标设置成功！", "info",function(){
			                	 $('#loan_orient_d').dialog("close");
			                  //  parent.$("#loan_publish").dialog('close');
			                    //window.close();
			                });
			            } else  if(data == 'repart'){
			            	 $.messager.alert("系统提示", "定向用户信息与数据库不匹配！", "info");
			            }else if(data == 'fail'){
			            	 $.messager.alert("系统提示", "定向设置失败！", "info");
			            }else if(data == 'notEquals'){
			            	 $.messager.alert("系统提示", "定向用户姓名不正确！", "info");
			            }
			            else {
			                $.messager.alert("系统提示", "借款申请发标失败！", "info");
			            }
			        }
			    });
		 
			
				$("#loanCount").click(function() {
					if (document.getElementById("dd2").style.display == "none") {
						document.getElementById("dd2").style.display = "block";
					} else {
						document.getElementById("dd2").style.display = "none";
					}
								});
				$("#oUser").click(function() {
					if (document.getElementById("dd").style.display == "none") {
						document.getElementById("dd").style.display = "block";
					} else {
						document.getElementById("dd").style.display = "none";
					}
				});
				/**判断select **/
				$("#radioInputA1").hide();
				$("#radioInputP").hide();
				//定向用户
				$("#radioInputPerson").click(function() {
					$("#radioInputA1").hide();
					$("#radioInputP").show();
					$("#radioInputAllValue").val("");
					$("#newUserRadio").val("");
				});
				//定向密码
				$("#radioInputOnly").click(function() {
					$("#radioInputP").hide();
					$("#radioInputA1").show();
					$("#radioInputAllValue").val("");
				    $("#newUserRadio").val("");
				});
				//定向 所有
				$("#radioInputAll").click(function() {
					$("#radioInputP").hide();
					$("#radioInputA1").hide();
					$("#radioInputAllValue").val("oType");
					$("#newUserRadio").val("");
					
				});
				
				
				  //新手用户
				$("#UserRadio").click(function(){
					  $("#radioInputP").hide();
					  $("#radioInputA1").hide();
					  $("#radioInputAllValue").val("");
					  $("#newUserRadio").val("newUserRadio");
				 });
				 
			</script>

		</div>
</body>
</html>