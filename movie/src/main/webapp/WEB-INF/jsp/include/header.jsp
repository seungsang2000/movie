<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<!--[if IE 7]>
<html class="ie ie7 no-js" lang="en-US">
<![endif]-->
<!--[if IE 8]>
<html class="ie ie8 no-js" lang="en-US">
<![endif]-->
<!--[if !(IE 7) | !(IE 8)  ]><!-->
<html lang="ko" class="no-js">


<head>
<!-- Basic need -->
<title>영화 리뷰 플랫폼</title>
<meta charset="UTF-8">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="author" content="">
<link rel="profile" href="#">

<!--Google Font-->
<link rel="stylesheet" href='${pageContext.request.contextPath}/css/googleFont.css' />
<!-- Mobile specific meta -->
<meta name=viewport content="width=device-width, initial-scale=1">
<meta name="format-detection" content="telephone-no">

<!-- CSS files -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/plugins.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

<script src="${pageContext.request.contextPath}/js/jquery.js"></script>
	<script src="${pageContext.request.contextPath}/js/plugins.js"></script>
	<script src="${pageContext.request.contextPath}/js/plugins2.js"></script>
	<script src="${pageContext.request.contextPath}/js/custom.js"></script>
	
	<style>
	.disabled-btn {
	    background-color: #d3d3d3 !important; 
	    color: #888 !important; 
	    cursor: not-allowed !important; 
	}
	</style>
</head>
<body>
	<!--preloading-->
	<div id="preloader">
		<img class="logo" src="${pageContext.request.contextPath}/images/logo1.png" alt="" width="119"
			height="58">
		<div id="status">
			<span></span> <span></span>
		</div>
	</div>
	<!--end of preloading-->
	
	
	
	<!--로그인-->
	<div class="login-wrapper" id="login-content">
		<div class="login-content">
			<a href="#" class="close">x</a>
			<h3>Login</h3>
			<form method="post" action="#" id="login-form">
				<div class="row">
					<label for="username"> 유저명: <input type="text"
						name="username" id="username" placeholder="Hugh Jackman"
						pattern="^[a-zA-Z][a-zA-Z0-9-_\.]{8,20}$" required="required" />
					</label>
				</div>

				<div class="row">
					<label for="password"> Password: <input type="password"
						name="password" id="password" placeholder="******"
						pattern="^[a-zA-Z\d\W]{8,20}$"
						required="required" />
					</label>
				</div>
				<div class="row">
					<div class="remember">
						<div>
							<input type="checkbox" name="remember" value="Remember me"><span>Remember
								me</span>
						</div>
						<a href="#" id="forgotPassword">Forget password ?</a>
					</div>
				</div>
				<div class="row">
					<button type="submit">Login</button>
				</div>
			</form>
			<!-- <div class="row">
				<p>Or via social</p>
				<div class="social-btn-2">
					<a class="fb" href="#"><i class="ion-social-facebook"></i>Facebook</a>
					<a class="tw" href="#"><i class="ion-social-twitter"></i>twitter</a>
				</div>
			</div> -->
		</div>
	</div>
	<!--end of login form popup-->
	<!--비밀번호 팝업-->
	<div class="login-wrapper" id="forgotPassword-content">
	    <div class="login-content">
	        <a href="#" class="close">x</a>
	        <h3>비밀번호 복구</h3>
	        
	        <div class="row">
	            <label for="email">이메일:
	                <input type="text" name="email" id="email" placeholder="이메일을 입력하세요"
	                    pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$" required="required" />
	            </label>
	        </div>
	
	        <!-- 인증번호 입력 필드는 처음엔 숨김 -->
	        <div class="row" id="authKeyRow" style="display: none;">
	            <label for="authKey">인증번호:
	                <input type="text" name="authKey" id="authKey" placeholder="인증번호를 입력하세요"
	                    pattern="^[a-zA-Z\d\W]{8,20}$" />
	            </label>
	        </div>
	
	        <!-- 이메일 전송 버튼 -->
	        <div class="row" id="emailSendBtnRow">
	            <button type="button" id="emailSendBtn">이메일 전송</button>
	        </div>
	
	        <!-- 인증번호 전송 버튼, 처음엔 숨김 -->
	        <div class="row" id="authKeyBtnRow" style="display: none;">
	            <button type="button" id="authKeyBtn">인증번호 전송</button>
	        </div>
	    </div>
	</div>
	
	<!--비밀번호 팝업-->
	
	<!-- 임시비밀번호 -->
	<div class="login-wrapper" id="forgotPassword-content">
	    <div class="login-content">
	        <a href="#" class="close">x</a>
	        <h3>임시비밀번호</h3>
	        
	        <div class="row">
	            <label for="tempPassword">임시비밀번호:
	                <input type="text" name="tempPassword" id="tempPassword" placeholder=""
	                    pattern="^[a-zA-Z][a-zA-Z0-9-_\.]{8,20}$"  />
	            </label>
	        </div>
	        
	        계정의 비빌번호가 임시 비밀번호로 변경되었습니다. 해당 계정으로 로그인한 후, 비밀번호를 변경해주세요.
	
	        <!-- 이메일 전송 버튼 -->
	        <div class="row" id="returnMainPageBtnRow">
	            <button type="button" id="returnMainPageBtn">메인 화면으로 돌아가기</button>
	        </div>
	    </div>
	</div>
	<!-- 임시비밀번호 -->
	
	<!--signup form popup-->
	<div class="login-wrapper" id="signup-content">
		<div class="login-content">
			<a href="#" class="close">x</a>
			<h3>sign up</h3>
			<form method="post" action="#" id="signup-form">
				<div class="row">
					<label for="username-2"> 유저명: <input type="text"
						name="username" id="username-2" placeholder="movie"
						pattern="^[a-zA-Z][a-zA-Z0-9-_\.]{8,20}$" required="required" />
					</label>
				</div>

				<div class="row">
					<label for="email-2"> 이메일: <input type="email" name="email" id="email-2" placeholder="movie@movie.com" pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$" required="required" />
					</label>
				</div>
				<div class="row">
					<label for="password-2"> 비밀번호: <input type="password"
						name="password" id="password-2" placeholder=""
						pattern="^[a-zA-Z\d\W]{8,20}$"
						required="required" />
					</label>
				</div>
				<div class="row">
					<label for="repassword-2"> 비밀번호 재입력 : <input
						type="password" name="password" id="repassword-2" placeholder=""
						pattern="^[a-zA-Z\d\W]{8,20}$"
						required="required" />
					</label>
				</div>
				<div class="row">
					<button type="submit">회원가입</button>
				</div>
			</form>
		</div>
	</div>
	<!--end of signup form popup-->
	<!-- BEGIN | Header -->
	<header class="ht-header">
		<div class="container">
			<nav class="navbar navbar-default navbar-custom">
				<!-- Brand and toggle get grouped for better mobile display -->
				<div class="navbar-header logo">
					<div class="navbar-toggle" data-toggle="collapse"
						data-target="#bs-example-navbar-collapse-1">
						<span class="sr-only">Toggle navigation</span>
						<div id="nav-icon1">
							<span></span> <span></span> <span></span>
						</div>
					</div>
					<a href="/"><img class="logo" src="${pageContext.request.contextPath}/images/logo1.png" alt=""
						width="119" height="58"></a>
				</div>
				<!-- Collect the nav links, forms, and other content for toggling -->
				<div class="collapse navbar-collapse flex-parent"
					id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav flex-child-menu menu-left">
						<li class="hidden"><a href="#page-top"></a></li>
						<li class="dropdown first"><a href="/">HOME</a> <!-- <a class="btn btn-default dropdown-toggle lv1" data-toggle="dropdown">
							Home <i class="fa fa-angle-down" aria-hidden="true"></i>
							</a>
							<ul class="dropdown-menu level1">
								<li><a href="index-2.html">Home 01</a></li>
								<li><a href="homev2.html">Home 02</a></li>
								<li><a href="homev3.html">Home 03</a></li>
							</ul> --></li>
						<li class="dropdown first"><a
							class="btn btn-default dropdown-toggle lv1"
							data-toggle="dropdown" data-hover="dropdown"> movies<i
								class="fa fa-angle-down" aria-hidden="true"></i>
						</a>
							<ul class="dropdown-menu level1">
								<li class="dropdown"><a href="#" class="dropdown-toggle"
									data-toggle="dropdown">Movie grid<i
										class="ion-ios-arrow-forward"></i></a>
									<ul class="dropdown-menu level2">
										<li><a href="moviegrid.html">Movie grid</a></li>
										<li><a href="moviegridfw.html">movie grid full width</a></li>
									</ul></li>
								<li><a href="movielist.html">Movie list</a></li>
								<li><a href="moviesingle.html">Movie single</a></li>
								<li class="it-last"><a href="seriessingle.html">Series
										single</a></li>
							</ul></li>
						<li class="dropdown first"><a
							class="btn btn-default dropdown-toggle lv1"
							data-toggle="dropdown" data-hover="dropdown"> celebrities <i
								class="fa fa-angle-down" aria-hidden="true"></i>
						</a>
							<ul class="dropdown-menu level1">
								<li><a href="celebritygrid01.html">celebrity grid 01</a></li>
								<li><a href="celebritygrid02.html">celebrity grid 02 </a></li>
								<li><a href="celebritylist.html">celebrity list</a></li>
								<li class="it-last"><a href="celebritysingle.html">celebrity
										single</a></li>
							</ul></li>
						<li class="dropdown first"><a
							class="btn btn-default dropdown-toggle lv1"
							data-toggle="dropdown" data-hover="dropdown"> news <i
								class="fa fa-angle-down" aria-hidden="true"></i>
						</a>
							<ul class="dropdown-menu level1">
								<li><a href="bloglist.html">blog List</a></li>
								<li><a href="bloggrid.html">blog Grid</a></li>
								<li class="it-last"><a href="blogdetail.html">blog
										Detail</a></li>
							</ul></li>
						<li class="dropdown first"><a
							class="btn btn-default dropdown-toggle lv1"
							data-toggle="dropdown" data-hover="dropdown"> community <i
								class="fa fa-angle-down" aria-hidden="true"></i>
						</a>
							<ul class="dropdown-menu level1">
								<li><a href="userfavoritegrid.html">user favorite grid</a></li>
								<li><a href="userfavoritelist.html">user favorite list</a></li>
								<li><a href="userprofile.html">user profile</a></li>
								<li class="it-last"><a href="userrate.html">user rate</a></li>
							</ul></li>
					</ul>
					<ul class="nav navbar-nav flex-child-menu menu-right">
						<li class="dropdown first"><a
							class="btn btn-default dropdown-toggle lv1"
							data-toggle="dropdown" data-hover="dropdown"> pages <i
								class="fa fa-angle-down" aria-hidden="true"></i>
						</a>
							<ul class="dropdown-menu level1">
								<li><a href="landing.html">Landing</a></li>
								<li><a href="404.html">404 Page</a></li>
								<li class="it-last"><a href="comingsoon.html">Coming
										soon</a></li>
							</ul></li>
						<li><a href="#">Help</a></li>
						<sec:authorize access="isAuthenticated()">
							<li>
								<li><a href="/user/myPage.do">myPage</a></li>
							<li>
							<li class="btn">
        						<a href="/user/logout.do" onclick="event.preventDefault(); document.getElementById('logout-form').submit();">Logout</a>
   					 		</li>
   					 		<form id="logout-form" action="/user/logout.do" method="POST" style="display: none;">
							</form>
						</sec:authorize>
						
						<sec:authorize access="!isAuthenticated()">
						<li class="loginLink"><a href="#">LOG In</a></li>
						<li class="btn signupLink"><a href="#">sign up</a></li>
						</sec:authorize>
					</ul>
				</div>
				<!-- /.navbar-collapse -->
			</nav>

			<!-- top search form -->
			<form action="/search.do" method="GET">
			<div class="top-search">

				<select name="type">
					<option value="movie">영화</option>
					<option value="person">인물</option>
				</select> <input type="text" name="query" value="${query}" placeholder="검색어를 입력해주세요" required>

			</div>
			</form>
		</div>
	</header>
	<!-- END | Header -->
	

	
	<script>
	function isValidUsername(username){
	    const regex = /^[a-zA-Z][a-zA-Z0-9-_\.]{7,19}$/;
	    return regex.test(username);
	}
	
	function isValidPassword(password){
	    const regex = /^[a-zA-Z\d\W]{8,20}$/;
	    return regex.test(password)
	}
	
	$(function(){
	    let isUsernameValid = false;
	    let isPasswordValid = false;
	    
	    $('#signup-form').on("submit",function(e){
	        e.preventDefault();
	        const formData = {
	                username: $('#username-2').val(),
	                email: $('#email-2').val(),
	                password: $('#password-2').val(),
	                repassword: $('#repassword-2').val()
	        }
	        
	        if (!isValidUsername(formData.username)) {
	            alert('아이디는 영문자로 시작하며 8~20자여야 합니다.');
	            return;
	        }

	        if (!isValidPassword(formData.password)) {
	            alert('비밀번호는 8~20자로 설정해주세요.');
	            return;
	        }
	        
	        if(formData.password != formData.repassword){
	            alert("비밀번호가 일치하지 않습니다.");
	            return;
	        }
	        
	        $.ajax({
	            url : "/user/signup.do",
	            method : "POST",
	            contentType: "application/json",
	            data: JSON.stringify(formData),
	            success: function(response){
	                if(response.success){
	                    alert("회원가입 성공! ");
	                    location.reload();
	                } else{
	                    alert("회원가입 실패: "+response.message);
	                }
	            },
	            error: function(xhr, status, error) {
                    console.error("서버 오류:", status, error);
                    alert('서버와의 연결에 문제가 발생했습니다.');
                }
	        });
	    });
	    
	    $('#login-form').on("submit",function(e){
	        e.preventDefault();
	        const formData = {
	                username: $('#username').val(),
	                password: $('#password').val()
	        }
	        
	        if (!isValidUsername(formData.username)) {
	            alert('아이디는 영문자로 시작하며 8~20자여야 합니다.');
	            return;
	        }

	        if (!isValidPassword(formData.password)) {
	            alert('비밀번호는 8~20자로 설정해주세요.');
	            return;
	        }
	        
	        $.ajax({
	            url: "/user/login.do",
	            method: "POST",
	            data: formData,
	            success: function (response) {
	                if (response.success) {
	                    alert("로그인 성공!");
	                    location.reload(); // 페이지 새로고침
	                } else {
	                    alert("로그인 실패: " + response.message);
	                }
	            },
	            error: function (xhr) {
	                if (xhr.status === 401) {
	                    alert("로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다.");
	                } else {
	                    alert("서버 오류가 발생했습니다.");
	                }
	            }
	        });
	            
	        });
	    
		    $('#emailSendBtn').on('click', function() {
	            var email = $('#email').val();
	            
	            $('#emailSendBtn').prop('disabled', true).addClass("disabled-btn");
	            
	            // 이메일 전송 요청
	            $.ajax({
	                type: 'POST',
	                url: '/user/sendEmail.do',  // 서버로 요청 보낼 URL
	                data: { email: email },
	                success: function(response) {
	                    if (response.success) {
	                        alert("이메일이 성공적으로 전송되었습니다.");
	                        
	                        // 인증번호 입력 필드와 버튼을 보이게 함
	                        $('#authKeyRow').show();
	                        $('#authKeyBtnRow').show();
	                        $('#emailSendBtnRow').hide();  // 이메일 전송 버튼 숨김
	                    } else {
	                        alert(response.message || "이메일 전송에 실패했습니다.");
	                    }
	                },
	                error: function() {
	                    alert("서버 오류로 인해 이메일 전송에 실패했습니다.");
	                },
	                complete: function() {
	                    // 응답을 받은 후 이메일 전송 버튼 활성화
	                    $('#emailSendBtn').prop('disabled', false).removeClass("disabled-btn");
	                }
	            });
	        });
	
	        // 인증번호 전송 버튼 클릭 (새로 추가된 버튼)
	        $('#authKeyBtn').on('click', function() {
	            var authKey = $('#authKey').val();
	
	            // 인증번호 전송 요청 (예시로 authKey 사용)
	            $.ajax({
	                type: 'POST',
	                url: '/user/verifyAuthKey.do',  // 인증번호 검증 URL
	                data: { authKey: authKey },
	                success: function(response) {
	                    if (response.success) {
	                        alert("인증번호가 성공적으로 확인되었습니다.");
	                    } else {
	                        alert(response.message || "인증번호 검증에 실패했습니다.");
	                    }
	                },
	                error: function() {
	                    alert("서버 오류로 인해 인증번호 전송에 실패했습니다.");
	                }
	            });
	        });
	        
	        
	        $("#returnMainPageBtn").on('click', function(){
	        	    window.loaction.reload();
	        });
	        
	        
	        
	    });
	
	</script>