<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/header.jsp"%>

<div class="hero common-hero">
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="hero-ct">
					<h1> 영화관 검색</h1>
					<ul class="breadcumb">
						<li class="active"><a href="/">Home</a></li>
						<li> <span class="ion-ios-arrow-right"></span> 영화관 검색 </li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- blog detail section-->
<div class="page-single">
	<div class="container">
		<div class="row">
			<div class="col-md-9 col-sm-12 col-xs-12">
				<div id="map" style="width:110%;height:440px;"></div>
			</div>
			<div class="col-md-3 col-sm-12 col-xs-12">
				<div class="sidebar">
					<div class="sb-search sb-it">
						<h4 class="sb-title">Search</h4>
						<input type="text" placeholder="Enter keywords">
					</div>
					<div class="ads">
						<img src="images/uploads/ads1.png" alt="">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>



<script type="text/javascript" src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=73dfec78a0fef2120c0b35b9ad59ae64&autoload=false&libraries=services"></script>

<script>
// 현재 위치를 가져오는 함수
function getCurrentPosition() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            function(position) {
                const latitude = position.coords.latitude;
                const longitude = position.coords.longitude;
                console.log("Latitude:", latitude, "Longitude:", longitude);

                // 카카오맵 API 로딩 후 displayMap과 searchNearbyTheaters 호출
                kakao.maps.load(function() {
                    // 지도 초기화
                    const map = displayMap(latitude, longitude);  // 지도 객체 생성
                    // 영화관 검색
                    searchNearbyTheaters(map, latitude, longitude);  // 지도 객체를 넘김
                });
            },
            function(error) {
                console.error("Error getting location:", error);
                alert("위치 정보를 가져올 수 없습니다.");
            }
        );
    } else {
        alert("이 브라우저는 Geolocation을 지원하지 않습니다.");
    }
}

// 카카오맵을 초기화하고 마커를 표시하는 함수
function displayMap(latitude, longitude) {
    const mapContainer = document.getElementById('map'); 
    const mapOption = {
        center: new kakao.maps.LatLng(latitude, longitude), 
        level: 3 
    };

    const map = new kakao.maps.Map(mapContainer, mapOption); 

    
    const marker = new kakao.maps.Marker({
        position: new kakao.maps.LatLng(latitude, longitude),
        map: map
    });

    const infowindow = new kakao.maps.InfoWindow({
        content: `<div style="padding:5px;">현재 위치</div>`, removable : true
    });

    infowindow.open(map, marker);

    return map;  
}


function searchNearbyTheaters(map, latitude, longitude) {
    if (!kakao.maps.services) {
        console.error("kakao.maps.services is not available");
        return;
    }

    let currentInfoWindow = null;
    const places = new kakao.maps.services.Places(); 
    const position = new kakao.maps.LatLng(latitude, longitude); 

    places.keywordSearch('영화관', function(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
            
            // 검색된 장소 데이터 확인
            console.log("검색된 장소 데이터: ", data);
            
            for (let i = 0; i < data.length; i++) {
                const place = data[i];
                console.log("현재 장소: ", place); // 각 장소 데이터 확인
                
                // 마커 추가
                const marker = new kakao.maps.Marker({
                    position: new kakao.maps.LatLng(place.y, place.x),
                    map: map  
                });

                // 인포윈도우 생성
                const infowindow = new kakao.maps.InfoWindow({
                    content: "<div style='padding:5px;'>" + 
                             "<strong>" + (place.place_name ? place.place_name : '정보 없음') + "</strong><br>" +
                             "주소: " + (place.address_name ? place.address_name : '주소 없음') + "<br>" +
                             "전화: " + (place.phone ? place.phone : '전화번호 없음') + "<br>" +
                             "<a href='" + place.place_url + "' target='_blank'>상세 정보</a>" + 
                             "</div>", removable : true
                });

                // 마커 클릭 시 인포윈도우 열기
                kakao.maps.event.addListener(marker, 'click', function() {
                    if (currentInfoWindow) {
                        currentInfoWindow.close(); // 기존에 열린 InfoWindow 닫기
                    }
                    infowindow.open(map, marker); // 새 InfoWindow 열기
                    currentInfoWindow = infowindow; // 현재 열린 InfoWindow로 설정
                });
            }
        } else {
            alert("영화관 검색 결과가 없습니다.");
        }
    });
}

// 페이지 로딩 시 위치 가져오기
document.addEventListener("DOMContentLoaded", function() {
    getCurrentPosition();
});
</script>

<%@ include file="/WEB-INF/jsp/include/footer.jsp" %>