<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/header.jsp"%>

	<div class="slider movie-items">
		<div class="container">
			<div class="row">
<!-- 				<div class="social-link">
					<p>Follow us:</p>
					<a href="#"><i class="ion-social-facebook"></i></a> <a href="#"><i
						class="ion-social-twitter"></i></a> <a href="#"><i
						class="ion-social-googleplus"></i></a> <a href="#"><i
						class="ion-social-youtube"></i></a>
				</div> -->
				<div class="slick-multiItemSlider">

						<c:forEach var="movie" items="${popularMovies}">
						<div class="movie-item">
							<div class="mv-img">
								<a href="/singleMovie.do?id=${movie.id}"><img src="${movie.img_url}" alt="" style="width: 285px; height: 437px; object-fit: cover;"></a>
							</div>
							<div class="title-in">
							<c:forEach var="genre" items="${movie.genre_names}">
								<div class="cate">
									<span class="blue"><a href="#">${genre}</a></span>
								</div>
							</c:forEach>
								<h6>
									<a href="/singleMovie.do?id=${movie.id}">${movie.title}</a>
								</h6>
								<p>
									<i class="ion-android-star"></i><span>${movie.vote_average}</span>
									/10
								</p>
							</div>
					</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	<div class="movie-items">
		<div class="container">
			<div class="row ipad-width">
				<div class="col-md-8">
					<div class="title-hd">
						<h2>in theater</h2>
						<a href="#" class="viewall">전체 보기 <i
							class="ion-ios-arrow-right"></i></a>
					</div>
					<div class="tabs">
						<ul class="tab-links">
							<li class="active"><a href="#tab1">#인기순</a></li>
							<li><a href="#tab2"> #곧 출시</a></li>
							<li><a href="#tab3"> #평점순 </a></li>
							<li><a href="#tab4"> #상영중</a></li>
						</ul>
						<div class="tab-content">
							<div id="tab1" class="tab active">
								<div class="row">
									<div class="slick-multiItem">
									<c:forEach var="movie" items="${popularMovies}">
										<div class="slide-it">
										
										<div class="movie-item">
												<div class="mv-img">
													<img src="${movie.img_url}" alt="" style="width: 185px; height: 284px; object-fit: cover;">
												</div>
												<div class="hvr-inner">
													<a href="/singleMovie.do?id=${movie.id}"> Read more <i
														class="ion-android-arrow-dropright"></i>
													</a>
												</div>
												<div class="title-in">
													<h6>
														<a href="/singleMovie.do?id=${movie.id}">${movie.title}</a>
													</h6>
													<p>
														<i class="ion-android-star"></i><span>${movie.vote_average}</span> /10
													</p>
												</div>
											</div>
											</div>
										</c:forEach>
									</div>
								</div>
							</div>
							<div id="tab2" class="tab">
								<div class="row">
									<div class="slick-multiItem">
									<c:forEach var="movie" items="${upComingMovies}">
										<div class="slide-it">
										
										<div class="movie-item">
												<div class="mv-img">
													<img src="${movie.img_url}" alt="" style="width: 185px; height: 284px; object-fit: cover;">
												</div>
												<div class="hvr-inner">
													<a href="/singleMovie.do?id=${movie.id}"> Read more <i
														class="ion-android-arrow-dropright"></i>
													</a>
												</div>
												<div class="title-in">
													<h6>
														<a href="/singleMovie.do?id=${movie.id}">${movie.title}</a>
													</h6>
													<p>
														<i class="ion-android-star"></i><span>${movie.vote_average}</span> /10
													</p>
												</div>
											</div>
											</div>
										</c:forEach>
									</div>
								</div>
							</div>
							<div id="tab3" class="tab">
								<div class="row">
									<div class="slick-multiItem">
									<c:forEach var="movie" items="${topRatedMovies}">
										<div class="slide-it">
										
										<div class="movie-item">
												<div class="mv-img">
													<img src="${movie.img_url}" alt="" style="width: 185px; height: 284px; object-fit: cover;">
												</div>
												<div class="hvr-inner">
													<a href="/singleMovie.do?id=${movie.id}"> Read more <i
														class="ion-android-arrow-dropright"></i>
													</a>
												</div>
												<div class="title-in">
													<h6>
														<a href="/singleMovie.do?id=${movie.id}">${movie.title}</a>
													</h6>
													<p>
														<i class="ion-android-star"></i><span>${movie.vote_average}</span> /10
													</p>
												</div>
											</div>
											</div>
										</c:forEach>
										
									</div>
								</div>
							</div>
							<div id="tab4" class="tab">
								<div class="row">
									<div class="slick-multiItem">
									<c:forEach var="movie" items="${nowPlayingMovies}">
										<div class="slide-it">
											<div class="movie-item">
												<div class="mv-img">
													<img src="${movie.img_url}" alt="" style="width: 185px; height: 284px; object-fit: cover;">
												</div>
												<div class="hvr-inner">
													<a href="/singleMovie.do?id=${movie.id}"> Read more <i
														class="ion-android-arrow-dropright"></i>
													</a>
												</div>
												<div class="title-in">
													<h6>
														<a href="/singleMovie.do?id=${movie.id}">${movie.title}</a>
													</h6>
													<p>
														<i class="ion-android-star"></i><span>${movie.vote_average}</span> /10
													</p>
												</div>
											</div>
										</div>
									</c:forEach>
										
									</div>
								</div>
							</div>
						</div>
					</div>
					
					
				</div>
				<div class="col-md-4">
					<div class="sidebar">
						<div class="celebrities">
							<h4 class="sb-title">주목받는 연예인</h4>
							<c:forEach var="person" items="${popularPeople}">
							<div class="celeb-item">
								<a href="#"><img src="${person.img_url}" alt=""
									width="70" height="70" object-fit: cover;></a>
								<div class="celeb-author">
									<h6>
										<a href="#">${person.name}</a>
									</h6>
									<span>${person.department}</span>
								</div>
							</div>
							</c:forEach>
							<a href="#" class="btn">모든 연예인 보기<i
								class="ion-ios-arrow-right"></i></a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="trailers">
		<div class="container">
			<div class="row ipad-width">
				<div class="col-md-12">
					<div class="title-hd">
						<h2>in theater</h2>
						<a href="#" class="viewall">View all <i
							class="ion-ios-arrow-right"></i></a>
					</div>
					<div class="videos">
						<div class="slider-for-2 video-ft">
							<div>
								<iframe class="item-video" src="" data-src=""></iframe>
							</div>
							<div>
								<iframe class="item-video" src="" data-src=""></iframe>
							</div>
							<div>
								<iframe class="item-video" src="" data-src=""></iframe>
							</div>
							<div>
								<iframe class="item-video" src="" data-src=""></iframe>
							</div>
							<div>
								<iframe class="item-video" src="" data-src=""></iframe>
							</div>
							<div>
								<iframe class="item-video" src="" data-src=""></iframe>
							</div>


						</div>
						<div class="slider-nav-2 thumb-ft">
							<div class="item">
								<div class="trailer-img">
									<img src="images/uploads/trailer7.jpg"
										alt="photo by Barn Images" width="4096" height="2737">
								</div>
								<div class="trailer-infor">
									<h4 class="desc">Wonder Woman</h4>
									<p>2:30</p>
								</div>
							</div>
							<div class="item">
								<div class="trailer-img">
									<img src="images/uploads/trailer2.jpg"
										alt="photo by Barn Images" width="350" height="200">
								</div>
								<div class="trailer-infor">
									<h4 class="desc">Oblivion: Official Teaser Trailer</h4>
									<p>2:37</p>
								</div>
							</div>
							<div class="item">
								<div class="trailer-img">
									<img src="images/uploads/trailer6.jpg"
										alt="photo by Joshua Earle">
								</div>
								<div class="trailer-infor">
									<h4 class="desc">Exclusive Interview: Skull Island</h4>
									<p>2:44</p>
								</div>
							</div>
							<div class="item">
								<div class="trailer-img">
									<img src="images/uploads/trailer3.png"
										alt="photo by Alexander Dimitrov" width="100" height="56">
								</div>
								<div class="trailer-infor">
									<h4 class="desc">Logan: Director James Mangold Interview</h4>
									<p>2:43</p>
								</div>
							</div>
							<div class="item">
								<div class="trailer-img">
									<img src="images/uploads/trailer4.png"
										alt="photo by Wojciech Szaturski" width="100" height="56">
								</div>
								<div class="trailer-infor">
									<h4 class="desc">Beauty and the Beast: Official Teaser
										Trailer 2</h4>
									<p>2: 32</p>
								</div>
							</div>
							<div class="item">
								<div class="trailer-img">
									<img src="images/uploads/trailer5.jpg"
										alt="photo by Wojciech Szaturski" width="360" height="189">
								</div>
								<div class="trailer-infor">
									<h4 class="desc">Fast&Furious 8</h4>
									<p>3:11</p>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- latest new v1 section-->
	<div class="latestnew">
		<div class="container">
			<div class="row ipad-width">
				<div class="col-md-8">
					<div class="ads">
						<img src="images/uploads/ads2.png" alt="" width="728" height="106">
					</div>
					<div class="title-hd">
						<h2>Latest news</h2>
					</div>
					<div class="tabs">
						<ul class="tab-links-3">
							<li class="active"><a href="#tab31">#Movies </a></li>
							<li><a href="#tab32"> #TV Shows </a></li>
							<li><a href="#tab33"> # Celebs</a></li>
						</ul>
						<div class="tab-content">
							<div id="tab31" class="tab active">
								<div class="row">
									<div class="blog-item-style-1">
										<img src="images/uploads/blog-it1.jpg" alt="" width="170"
											height="250">
										<div class="blog-it-infor">
											<h3>
												<a href="#">Brie Larson to play first female white house
													candidate Victoria Woodull in Amazon film</a>
											</h3>
											<span class="time">13 hours ago</span>
											<p>
												Exclusive: <span>Amazon Studios </span>has acquired Victoria
												Woodhull, with Oscar winning Room star <span>Brie
													Larson</span> polsed to produce, and play the first female
												candidate for the presidency of the United States. Amazon
												bought it in a pitch package deal. <span> Ben Kopit</span>,
												who wrote the Warner Bros film <span>Libertine</span> that
												has...
											</p>
										</div>
									</div>
								</div>
							</div>
							<div id="tab32" class="tab">
								<div class="row">
									<div class="blog-item-style-1">
										<img src="images/uploads/blog-it2.jpg" alt="" width="170"
											height="250">
										<div class="blog-it-infor">
											<h3>
												<a href="#">Tab 2</a>
											</h3>
											<span class="time">13 hours ago</span>
											<p>
												Exclusive: <span>Amazon Studios </span>has acquired Victoria
												Woodhull, with Oscar winning Room star <span>Brie
													Larson</span> polsed to produce, and play the first female
												candidate for the presidency of the United States. Amazon
												bought it in a pitch package deal. <span> Ben Kopit</span>,
												who wrote the Warner Bros film <span>Libertine</span> that
												has...
											</p>
										</div>
									</div>
								</div>
							</div>
							<div id="tab33" class="tab">
								<div class="row">
									<div class="blog-item-style-1">
										<img src="images/uploads/blog-it1.jpg" alt="" width="170"
											height="250">
										<div class="blog-it-infor">
											<h3>
												<a href="#">Tab 3</a>
											</h3>
											<span class="time">13 hours ago</span>
											<p>
												Exclusive: <span>Amazon Studios </span>has acquired Victoria
												Woodhull, with Oscar winning Room star <span>Brie
													Larson</span> polsed to produce, and play the first female
												candidate for the presidency of the United States. Amazon
												bought it in a pitch package deal. <span> Ben Kopit</span>,
												who wrote the Warner Bros film <span>Libertine</span> that
												has...
											</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="morenew">
						<div class="title-hd">
							<h3>More news on Blockbuster</h3>
							<a href="#" class="viewall">See all Movies news<i
								class="ion-ios-arrow-right"></i></a>
						</div>
						<div class="more-items">
							<div class="left">
								<div class="more-it">
									<h6>
										<a href="#">Michael Shannon Frontrunner to play Cable in
											“Deadpool 2”</a>
									</h6>
									<span class="time">13 hours ago</span>
								</div>
								<div class="more-it">
									<h6>
										<a href="#">French cannibal horror “Raw” inspires L.A.
											theater to hand out “Barf Bags”</a>
									</h6>

									<span class="time">13 hours ago</span>
								</div>
							</div>
							<div class="right">
								<div class="more-it">
									<h6>
										<a href="#">Laura Dern in talks to join Justin Kelly’s
											biopic “JT Leroy”</a>
									</h6>
									<span class="time">13 hours ago</span>
								</div>
								<div class="more-it">
									<h6>
										<a href="#">China punishes more than 300 cinemas for box
											office cheating</a>
									</h6>
									<span class="time">13 hours ago</span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-4">
					<div class="sidebar">
						<!-- <div class="sb-facebook sb-it">
						<h4 class="sb-title">Find us on Facebook</h4>
						<iframe src="#" data-src="https://www.facebook.com/plugins/page.php?href=https%3A%2F%2Fwww.facebook.com%2Ftemplatespoint.net%2F%3Ffref%3Dts&tabs=timeline&width=300&height=315px&small_header=true&adapt_container_width=false&hide_cover=false&show_facepile=true&appId" width="300" height="315" style="border:none;overflow:hidden" ></iframe>
					</div>
					<div class="sb-twitter sb-it">
						<h4 class="sb-title">Tweet to us</h4>
						<div class="slick-tw">
							<div class="tweet item" id="">Put your twiter id here
							</div>
							<div class="tweet item" id="">Put your 2nd twiter account id here
							</div>
						</div>					
					</div> -->
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--end of latest new v1 section-->
	<!-- footer section-->
	<footer class="ht-footer">
		<div class="container">
			<div class="flex-parent-ft">
				<div class="flex-child-ft item1">
					<a href="/"><img class="logo" src="images/logo1.png" alt=""></a>
					<p>
						5th Avenue st, manhattan<br> New York, NY 10001
					</p>
					<p>
						Call us: <a href="#">(+01) 202 342 6789</a>
					</p>
				</div>
				<div class="flex-child-ft item2">
					<h4>Resources</h4>
					<ul>
						<li><a href="#">About</a></li>
						<li><a href="#">Blockbuster</a></li>
						<li><a href="#">Contact Us</a></li>
						<li><a href="#">Forums</a></li>
						<li><a href="#">Blog</a></li>
						<li><a href="#">Help Center</a></li>
					</ul>
				</div>
				<div class="flex-child-ft item3">
					<h4>Legal</h4>
					<ul>
						<li><a href="#">Terms of Use</a></li>
						<li><a href="#">Privacy Policy</a></li>
						<li><a href="#">Security</a></li>
					</ul>
				</div>
				<div class="flex-child-ft item4">
					<h4>Account</h4>
					<ul>
						<li><a href="#">My Account</a></li>
						<li><a href="#">Watchlist</a></li>
						<li><a href="#">Collections</a></li>
						<li><a href="#">User Guide</a></li>
					</ul>
				</div>
				<div class="flex-child-ft item5">
					<h4>Newsletter</h4>
					<p>
						Subscribe to our newsletter system now <br> to get latest
						news from us.
					</p>
					<form action="#">
						<input type="text" placeholder="Enter your email...">
					</form>
					<a href="#" class="btn">Subscribe now <i
						class="ion-ios-arrow-forward"></i></a>
				</div>
			</div>
		</div>
		<div class="ft-copyright">
			<div class="ft-left">
				<p>
					<a target="_blank" href="https://www.templateshub.net">Templates
						Hub</a>
				</p>
			</div>
			<div class="backtotop">
				<p>
					<a href="#" id="back-to-top">Back to top <i
						class="ion-ios-arrow-thin-up"></i></a>
				</p>
			</div>
		</div>
	</footer>
	<!-- end of footer section-->

	<script src="js/jquery.js"></script>
	<script src="js/plugins.js"></script>
	<script src="js/plugins2.js"></script>
	<script src="js/custom.js"></script>
</body>


</html>
