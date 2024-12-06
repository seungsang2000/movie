<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/header.jsp"%>

<div class="hero user-hero">
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="hero-ct">
					<h1>임시 비밀번호 발급</h1>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="page-single">
	<div class="container">
		<div class="row ipad-width">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="form-style-1 user-pro">
						<h4>01. Profile details</h4>
						<div class="row">
							<div class="col-md-12 form-it">
								<label>임시 비빌번호</label>
								<input type="text" placeholder="Kennedy" value="${password}" readonly>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<input class="submit" type="submit" value="save">
							</div>
						</div>	
				</div>
			</div>
		</div>
	</div>
</div>


<%@ include file="/WEB-INF/jsp/include/footer.jsp" %>