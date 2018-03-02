<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="list-box-main box-mb-none">
	<p class="list-box-title">
		<i class="list-i-6"></i>项目证明
	</p>
	<!-- 左右滚动部分 begin -->
	<div class="main_lunbo">
		<c:if test="${not empty customerUploadSnapshots}">
			<div class="leftbtn" id="left"></div>
		</c:if>
		<div id="photo">
			<div class="Cont">
				<div class="ScrCont" id="viewer">
					<div class="List1" id="viewerFrame">
						<c:forEach items="${customerUploadSnapshots}"
							var="customersnapshot">
							<div class="list-pic-p">
								<a href="javascript:" rel="lightbox[plants]" title="">
									<img src="${picPath }${customersnapshot.attachment.thumbnailUrl}" alt="" />
								</a>
								<p>${customersnapshot.attachment.fileName}</p>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
		<c:if test="${not empty customerUploadSnapshots}">
			<div class="rightbtn" id="right"></div>
		</c:if>
	</div>
	<!-- 左右滚动部分 end -->
	<div class="clear"></div>
</div>
<div class="list-box-main box-mb-none">
	<p class="list-box-title">
		<i class="list-i-6"></i>公司证明
	</p>
	<!-- 左右滚动部分 begin -->
	<div class="main_lunbo">
	<c:if test="${not empty customerUploadSnapshots}">
		<div class="leftbtn" id="left"></div>
	</c:if>
	<div id="photo">
		<div class="Cont">
			<div class="ScrCont" id="viewer2">
				<div class="List1" id="viewerFrame2">
					<c:forEach items="${enterpriseInfoSnapshots}"
						var="customersnapshot">
						<div class="list-pic-p">
							<a href="javascript:" rel="lightbox[plants]" title="">
								<img src="${picPath }${customersnapshot.attachment.thumbnailUrl}" alt="" />
							</a>
							<p>${customersnapshot.attachment.fileName}</p>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${not empty customerUploadSnapshots}">
		<div class="rightbtn" id="right"></div>
	</c:if>
</div>