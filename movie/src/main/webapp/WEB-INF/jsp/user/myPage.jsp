<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/header.jsp"%>

<div class="hero user-hero">
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="hero-ct">
					<h1>마이페이지</h1>
					<ul class="breadcumb">
						<li class="active"><a href="#">Home</a></li>
						<li> <span class="ion-ios-arrow-right"></span>마이페이지</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="page-single">
	<div class="container">
		<div class="row ipad-width">
			<div class="col-md-3 col-sm-12 col-xs-12">
				<div class="user-information">
					<div class="user-img">
						<a href="#"><img src="${pageContext.request.contextPath}/images/uploads/user-img.png" alt="" onerror="this.src='${pageContext.request.contextPath}/images/human.png';"><br></a>
						<a href="#" class="redbtn">이미지 변경</a>
					</div>
					<div class="user-fav">
						<p>세부 목록</p>
						<ul>
							<li  class="active"><a href="#">프로필</a></li>
							<li><a href="userfavoritelist.html">선호 영화</a></li>
							<li><a href="userrate.html">평가한 영화</a></li>
						</ul>
					</div>
					<div class="user-fav">
						<p>기타</p>
						<ul>
							<li><a href="#">비밀번호 변경</a></li>
							<li><a href="#">로그아웃</a></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="col-md-9 col-sm-12 col-xs-12">
				<div class="form-style-1 user-pro" action="#">
					<form action="#" class="user">
						<h4>01. 프로필 세부정보</h4>
						<div class="row">
							<div class="col-md-6 form-it">
								<label>유저명</label>
								<input type="text" placeholder="유저명을 입력하세요" value="${currentUser.username}">
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 form-it">
								<label>Email Address</label>
								<input type="text" placeholder="이메일을 입력하세요" value="${currentUser.email}">
							</div>
						</div>
						<div class="row">
							<div class="col-md-2">
								<input class="submit" type="submit" value="저장">
							</div>
						</div>	
					</form>
					<form action="#" class="password">
						<h4>02. 비밀번호 변경</h4>
						<div class="row">
							<div class="col-md-6 form-it">
								<label>기존 비밀번호</label>
								<input type="text" placeholder="**********">
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 form-it">
								<label>새 비밀번호</label>
								<input type="text" placeholder="***************">
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 form-it">
								<label>새 비밀번호 확인</label>
								<input type="text" placeholder="*************** ">
							</div>
						</div>
						<div class="row">
							<div class="col-md-2">
								<input class="submit" type="submit" value="비밀번호 변경">
							</div>
						</div>	
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/jsp/include/footer.jsp" %>