<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/header.jsp"%>

<div class="hero common-hero">
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="hero-ct">
					<h1>영화 검색 - ${query}</h1>
					<ul class="breadcumb">
						<li class="active"><a href="/">Home</a></li>
						<li> <span class="ion-ios-arrow-right"></span>영화 검색 결과</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="page-single">
	<div class="container">
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="topbar-filter fw">
					<p>Found <span>${searchResult.total_results} movies</span> in total</p>
					<label>Sort by:</label>
					<select>
						<option value="popularity">Popularity Descending</option>
						<option value="popularity">Popularity Ascending</option>
						<option value="rating">Rating Descending</option>
						<option value="rating">Rating Ascending</option>
						<option value="date">Release date Descending</option>
						<option value="date">Release date Ascending</option>
					</select>
					<a href="movielist.html" class="list"><i class="ion-ios-list-outline "></i></a>
					<a  href="moviegridfw.html" class="grid"><i class="ion-grid active"></i></a>
				</div>
				<div class="flex-wrap-movielist mv-grid-fw">
						<c:forEach var="movie" items="${searchResult.movies}">
						<div class="movie-item-style-2 movie-item-style-1">
							<img src="${movie.img_url}" alt="">
							<div class="hvr-inner">
	            				<a  href="/singleMovie.do?id=${movie.id}"> Read more <i class="ion-android-arrow-dropright"></i> </a>
	            			</div>
							<div class="mv-item-infor">
								<h6><a href="/singleMovie.do?id=${movie.id}">${movie.title}</a></h6>
								<p class="rate"><i class="ion-android-star"></i><span>${movie.vote_average}</span> /10</p>
							</div>
						</div>		
						</c:forEach>			
						
						
				</div>		
				<div class="topbar-filter">
					<label>Movies per page:</label>
					<select>
						<option value="range">20 Movies</option>
						<option value="saab">10 Movies</option>
					</select>
					
					<div class="pagination2">
					    <span>Page ${page} of ${searchResult.total_pages}:</span>
					
					    <!-- 이전 페이지 링크 -->
					    <c:if test="${page > 1}">
					        <a href="?type=movie&query=${query}&page=${page - 1}">
					            <i class="ion-arrow-left-b"></i>
					        </a>
					    </c:if>
					
					    <!-- 첫 페이지 링크 -->
					    <c:if test="${searchResult.total_pages > 2 && page > 2}">
					        <a href="?type=movie&query=${query}&page=1">1</a>
					    </c:if>
					
					    <!-- 생략 기호 -->
					    <c:if test="${searchResult.total_pages > 3 && page > 3}">
					        <span>...</span>
					    </c:if>
					
					    <!-- 이전 페이지 링크 -->
					    <c:if test="${page > 1}">
					        <a href="?type=movie&query=${query}&page=${page - 1}">${page - 1}</a>
					    </c:if>
					
					    <!-- 현재 페이지 강조 -->
					    <a class="active">${page}</a>
					
					    <!-- 다음 페이지 링크 -->
					    <c:if test="${page < searchResult.total_pages}">
					        <a href="?type=movie&query=${query}&page=${page + 1}">${page + 1}</a>
					    </c:if>
					
					    <!-- 생략 기호 -->
					    <c:if test="${searchResult.total_pages > 3 && page < searchResult.total_pages - 2}">
					        <span>...</span>
					    </c:if>
					
					    <!-- 마지막 페이지 링크 -->
					    <c:if test="${searchResult.total_pages > 2 && page < searchResult.total_pages - 1}">
					        <a href="?type=movie&query=${query}&page=${searchResult.total_pages}">${searchResult.total_pages}</a>
					    </c:if>
					
					    <!-- 다음 페이지 링크 -->
					    <c:if test="${page < searchResult.total_pages}">
					        <a href="?type=movie&query=${query}&page=${page + 1}">
					            <i class="ion-arrow-right-b"></i>
					        </a>
					    </c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/jsp/include/footer.jsp" %>