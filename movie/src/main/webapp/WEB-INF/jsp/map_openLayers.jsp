<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/header.jsp"%>

<!-- openlayers -->
<link rel="stylesheet" href="https://openlayers.org/en/v4.6.5/css/ol.css" type="text/css">
<script src="https://openlayers.org/en/v4.6.5/build/ol.js"></script>
 
<!-- proj4js-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/2.6.2/proj4.js"></script>

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
                <div class="map-container">
                    <div id="map" class="map" style="width:100%; height:440px;"></div>
                    <div id="location">
                        <div>
                            <span>좌표 : <code id="position"></code></span>
                        </div>
                        <div>
                            <span>좌표계 : </span>
                            <select id="coordinate">
                                <option value="EPSG:4326" selected>EPSG:4326 | 경위도</option>
                                <option value="EPSG:3857">EPSG:3857 | Google 좌표계</option>
                                <option value="EPSG:5179">EPSG:5179 | UTM-K</option>
                                <option value="EPSG:2097">EPSG:2097 | 중부원점(Bessel)</option>
                                <option value="EPSG:5174">EPSG:5174 | 보정된 중부원점(Bessel)</option>
                                <option value="EPSG:5181">EPSG:5181 | 중부원점(GRS80) [20만, 50만]</option>
                                <option value="EPSG:5186">EPSG:5186 | 중부원점(GRS80) [20만,  60만]</option>
                            </select>
                        </div>
                        <div>
                            <span>배경지도 : </span><select id="baseLayer"></select>
                        </div>
                        <div>
                            <button id="myLocation" class="myLocationBtn">내 위치로 이동</button>
                            <label style="margin-left: 5px;"><input type="checkbox" id="isTrakingLocation"> 내 위치로 자동 이동</label>
                        </div>
                    </div>
                </div>
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

<style>

	.map-container {
	    display: flex;
	    flex-direction: column; /* 수직으로 배치 */
	}

	/* #map과 #location에 적절한 높이를 설정 */
	#map {
	    flex-grow: 1; /* 공간을 차지하도록 설정 */
	}

	#location {
		margin-top: 10px;
		idth: 100%;
		 /* right: 10px; */
		bottom: 0px;
		padding: 10px;
		box-sizing: border-box;
		z-index: 1;
		background-color: rgba(0,0,0,0.6);
		font-size: 12px;
		color: #FFFFFF;
	}
        
	.myLocationBtn {
	    background-color: #dd003f;
	    color: #ffffff;
	    margin-top : 10px;
	    padding: 11px 25px;
	    -webkit-border-radius: 20px;
	    -moz-border-radius: 20px;
	    border-radius: 20px;
	}
	
	.ol-popup {
	    position: absolute;
	    background-color: white;
	    border: 1px solid #ccc;
	    padding: 10px;
	    font-size: 14px;
	    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
	    pointer-events: none;  /* 팝업이 지도 클릭을 방해하지 않게 */
	}

	.ol-popup:after {
	    content: '';
	    position: absolute;
	    bottom: -10px;
	    left: 50%;
	    margin-left: -10px;
	    border-width: 10px;
	    border-style: solid;
	    border-color: white transparent transparent transparent;
	}
</style>
	

<script>


    
        // dom ready
        $(document).ready(function() {
            init();
        });
    
        function init() {
 
            // 좌표계 설정
            initProj();
        
            // map 생성
            var map = new ol.Map({
                target: 'map',                          // Map 생성할 div id
                view: new ol.View({
                    center: [54300000, 4300000],        // 초기 지도 위치 좌표
                    zoom: 8                             // 초기 지도 위치 줌레벨
                }),
                logo: false,
                controls: ol.control.defaults({
                    attribution: false
                }),
            });
 
            // 풀스크린 컨트롤러
            map.addControl(new ol.control.FullScreen());
            
            // 배경지도 레이어 추가
            addBaseLayer(map);
 
            // 배경지도 선택 select
            initBaseLayerSelect(map);
 
            // Geolocation
            initGeolocation(map);;
 
        }
 
        function initProj() {
 
            // 경위도
            proj4.defs('EPSG:4326', '+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs');
 
            // google 좌표계
            proj4.defs('EPSG:3857', '+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +no_defs');
 
            // UTM-K 좌표계
            proj4.defs('EPSG:5179', '+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs');
 
            // 중부원점(Bessel)
            proj4.defs('EPSG:2097', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs');
 
            // 보정된 중부원점(Bessel)
            proj4.defs('EPSG:5174', '+proj=tmerc +lat_0=38 +lon_0=127.0028902777778 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43');
 
            // 중부원점(GRS80) [200,000 500,000]
            proj4.defs('EPSG:5181', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs');
 
            // 중부원점(GRS80) [200,000 600,000]
            proj4.defs('EPSG:5186', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs');
 
        }
 
        function addBaseLayer(map) {
 

            var googleRoadLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://mt0.google.com/vt/lyrs=m&hl=en&x={x}&y={y}&z={z}',
                    crossOrigin: 'anonymous'
                }),
                id: 'google_road',
                visible: false
            });
            map.addLayer(googleRoadLayer);
 

            var googleTerrainLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://mt0.google.com/vt/lyrs=p&hl=en&x={x}&y={y}&z={z}',
                    crossOrigin: 'anonymous'
                }),
                id: 'google_terrain',
                visible: false
            });
            map.addLayer(googleTerrainLayer);
 

            var googleAlteredRoadLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://mt0.google.com/vt/lyrs=r&hl=en&x={x}&y={y}&z={z}',
                    crossOrigin: 'anonymous'
                }),
                id: 'google_altered_road',
                visible: false
            });
            map.addLayer(googleAlteredRoadLayer);
 

            var googleSatelliteLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://mt0.google.com/vt/lyrs=s&hl=en&x={x}&y={y}&z={z}',
                    crossOrigin: 'anonymous'
                }),
                id: 'google_satellite',
                visible: false
            });
            map.addLayer(googleSatelliteLayer);
 

            var googleTerrainOnlyLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://mt0.google.com/vt/lyrs=t&hl=en&x={x}&y={y}&z={z}',
                    crossOrigin: 'anonymous'
                }),
                id: 'google_terrain_only',
                visible: false
            });
            map.addLayer(googleTerrainOnlyLayer);
 

            var googleHybridLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://mt0.google.com/vt/lyrs=y&hl=en&x={x}&y={y}&z={z}',
                    crossOrigin: 'anonymous'
                }),
                id: 'google_hybrid',
                visible: false
            });
            map.addLayer(googleHybridLayer);
 
 

            var osmStandardLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png',
                    crossOrigin: 'anonymous'
                }),
                id: 'osm_standard',
                visible: false
            });
            map.addLayer(osmStandardLayer);
 
            
            var osmCyclosmLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://a.tile-cyclosm.openstreetmap.fr/cyclosm/{z}/{x}/{y}.png',
                    crossOrigin: 'anonymous'
                }),
                id: 'osm_cyclosm',
                visible: false
            });
            map.addLayer(osmCyclosmLayer);
 
            
            var osmHumanitarianLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://tile-{a-c}.openstreetmap.fr/hot/{z}/{x}/{y}.png',
                    crossOrigin: 'anonymous'
                }),
                id: 'osm_humanitarian',
                visible: false
            });
            map.addLayer(osmHumanitarianLayer);
 
            
            var vworldBaseLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://xdworld.vworld.kr/2d/Base/service/{z}/{x}/{y}.png',
                    crossOrigin: 'anonymous'
                }),
                id: 'vworld_base',
                visible: false
            });
            map.addLayer(vworldBaseLayer);
 
            
            var vworldSatelliteLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://xdworld.vworld.kr/2d/Satellite/service/{z}/{x}/{y}.jpeg',
                    crossOrigin: 'anonymous'
                }),
                id: 'vworld_satellite',
                visible: false
            });
            map.addLayer(vworldSatelliteLayer);
 
            
            var vworldHybridLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://xdworld.vworld.kr/2d/Hybrid/service/{z}/{x}/{y}.png',
                    crossOrigin: 'anonymous'
                }),
                id: 'vworld_hybrid',
                visible: false
            });
            map.addLayer(vworldHybridLayer);
 
            
            var vworldGrayLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://xdworld.vworld.kr/2d/gray/service/{z}/{x}/{y}.png',
                    crossOrigin: 'anonymous'
                }),
                id: 'vworld_gray',
                visible: false
            });
            map.addLayer(vworldGrayLayer);
 
            // vworld_midnight
            var vworldMidnightLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://xdworld.vworld.kr/2d/midnight/service/{z}/{x}/{y}.png',
                    crossOrigin: 'anonymous'
                }),
                id: 'vworld_midnight',
                visible: false
            });
            map.addLayer(vworldMidnightLayer);
 
 
            // ------------------------------
            // naver layers
            // ------------------------------
            // naver base
            /* var naverBaseLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://map.pstatic.net/nrb/styles/basic/1646972073/{z}/{x}/{y}@2x.png',
                    tilePixelRatio: 2,              // 타일사이즈 512일때 해상도 비율
                    crossOrigin: 'anonymous'
                }),
                id: 'naver_base',
                visible: false
            });
            map.addLayer(naverBaseLayer);
 
            // naver satellite
            var naverSatelliteLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://map.pstatic.net/nrb/styles/satellite/1646972073/{z}/{x}/{y}@2x.png',
                    tilePixelRatio: 2,              // 타일사이즈 512일때 해상도 비율
                    crossOrigin: 'anonymous'
                }),
                id: 'naver_satellite',
                visible: false
            });
            map.addLayer(naverSatelliteLayer);
 
            // naver terrain
            var naverTerrainLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:3857',
                    url : 'https://map.pstatic.net/nrb/styles/terrain/1646972073/{z}/{x}/{y}@2x.png',
                    tilePixelRatio: 2,              // 타일사이즈 512일때 해상도 비율
                    crossOrigin: 'anonymous'
                }),
                id: 'naver_terrain',
                visible: false
            });
            map.addLayer(naverTerrainLayer); */
 
            // ------------------------------
            // daum(kakao) layers
            // ------------------------------
            var daumTileGrid = new ol.tilegrid.TileGrid({
                extent : [(-30000-524288), (-60000-524288), (494288+524288), (988576+524288)],
                tileSize : 256,
                resolutions : [4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1, 0.5, 0.25], 
                minZoom : 1
            });
 
            function getDaumTileUrlFunction(type) {
 
                var tileUrlFunction = function(tileCoord, pixelRatio, projection) {
 
                    var res = this.getTileGrid().getResolutions();
                    var sVal = res[tileCoord[0]];
                    
                    var yLength = 988576 - (-60000) + 524288 + 524288;
                    var yTile = yLength / (sVal * this.getTileGrid().getTileSize());
 
                    var tileGap = Math.pow(2, (tileCoord[0] -1));
                    yTile = yTile - tileGap;
                    
                    var xTile = tileCoord[1] - tileGap;
            
                    if (type == 'base') {
                        return 'https://map' + Math.floor( (Math.random() * (4 - 1 + 1)) + 1 ) + '.daumcdn.net/map_2d_hd/2111ydg/L' + (15 - tileCoord[0]) + '/' + (yTile + tileCoord[2]) + '/' + xTile + '.png';
                    } else if (type == 'satellite') {
                        return 'https://map' + Math.floor( (Math.random() * (4 - 1 + 1)) + 1 ) + '.daumcdn.net/map_skyview_hd/L' + (15 - tileCoord[0]) + '/' + (yTile + tileCoord[2]) + '/' + xTile + '.jpg';
                    } else if (type == 'hybrid') {
                        return 'https://map' + Math.floor( (Math.random() * (4 - 1 + 1)) + 1 ) + '.daumcdn.net/map_hybrid_hd/2111ydg/L' + (15 - tileCoord[0]) + '/' + (yTile + tileCoord[2]) + '/' + xTile + '.png';
                    }
 
                };
 
                return tileUrlFunction;
 
            }
 
            // daum base
            var daumBaseLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:5181',
                    tileGrid: daumTileGrid,
                    tileUrlFunction: getDaumTileUrlFunction('base'),
                    tilePixelRatio: 2,              // 타일사이즈 512일때 해상도 비율
                }),
                id: 'daum_base',
                visible: false
            });
            map.addLayer(daumBaseLayer);
 
            // daum satellite
            var daumSatelliteLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:5181',
                    tileGrid: daumTileGrid,
                    tileUrlFunction: getDaumTileUrlFunction('satellite'),
                    tilePixelRatio: 2,              // 타일사이즈 512일때 해상도 비율
                }),
                id: 'daum_satellite',
                visible: false
            });
            map.addLayer(daumSatelliteLayer);
 
            // daum hybrid
            var daumHybridLayer = new ol.layer.Tile({
                source: new ol.source.XYZ({
                    projection : 'EPSG:5181',
                    tileGrid: daumTileGrid,
                    tileUrlFunction: getDaumTileUrlFunction('hybrid'),
                    tilePixelRatio: 2,              // 타일사이즈 512일때 해상도 비율
                }),
                id: 'daum_hybrid',
                visible: false
            });
            map.addLayer(daumHybridLayer);
            
 
        }
 
        function initBaseLayerSelect(map) {
 
            // add select option
            var html = '';
            $.each(map.getLayers().getArray(), function(i, v) {
                html += '<option value="' + v.get('id') + '">' + v.get('id') + '</option>';
            });
            $('#baseLayer').append(html);
 
            // select event
            $('#baseLayer').change(function() {
 
                var layerId = $(this).val();
                $.each(map.getLayers().getArray(), function(i, v) {
                    if (layerId == v.get('id')) {
                        v.setVisible(true);
                    } else {
                        v.setVisible(false);
                    }
                });
 
            });
 
            // 초기값
            $('#baseLayer').val('daum_base').trigger('change');
 
        }
 
          // 지오로케이션
        function initGeolocation(map) {
          
              // [내위치로 이동] 버튼 이벤트
            $('#myLocation').click(function() {
                var pos = geolocation.getPosition();
                if (pos == null) {
                    return;
                }
                map.getView().animate({
                    center: pos,
                    zoom: 18,
                    duration: 1000
                });
            });
 
            // 좌표계 변경 이벤트
            $('#coordinate').change(function() {
               $('#position').text('');
               geolocation.changed();
            });
 
            var geolocation = new ol.Geolocation({
                tracking: true,
                projection: map.getView().getProjection()
            });
 
            window.geolocation = geolocation;
 
            function el(id) {
                return document.getElementById(id);
            }
 
            // 좌표 변경 오픈레이어스 이벤트
            var debounceTimer;
			geolocation.on('change', function() {
			    var pos = geolocation.getPosition();
			
			    // 위치 추적 시마다 호출되지만, 디바운스를 사용해 호출 간격을 조정
			    clearTimeout(debounceTimer);  // 이전 타이머를 취소
			    debounceTimer = setTimeout(function() {
			     	// 주변 영화관 정보 가져오기
	                fetchNearbyMovieTheaters(pos);  // 위치 기반 영화관 조회 함수
			        if ($('#isTrakingLocation').is(':checked') === true) {
			            if (pos != null) {
			                map.getView().animate({
			                    center: pos,
			                    duration: 500
			                });
			
			                
			            }
			        }
			
			        if (map.getView().getProjection().getCode() != $('#coordinate').val()) {
			            pos = ol.proj.transform(pos, map.getView().getProjection().getCode(), $('#coordinate').val());
			        }
			
			        el('position').innerText = pos;
			    }, 1000);  // 1초 후에 영화관 정보를 요청
			});
             /* geolocation.on('change', function() {
 
                var pos = geolocation.getPosition();
              
                  if($('#isTrakingLocation').is(':checked') == true) {
                  if (pos != null) {
                    //map.getView().setCenter(pos);
                    map.getView().animate({
                      center: pos,
                      duration: 500
                    });
                  }
                }
              
                if (map.getView().getProjection().getCode() != $('#coordinate').val()) {
                    pos = ol.proj.transform(pos, map.getView().getProjection().getCode(), $('#coordinate').val())
                }
         
                el('position').innerText = pos;

            });  */
 
            var accuracyFeature = new ol.Feature();
            geolocation.on('change:accuracyGeometry', function() {
                accuracyFeature.setGeometry(geolocation.getAccuracyGeometry());
            });
 
              // 포인트 객체
            var positionFeature = new ol.Feature();
            positionFeature.setStyle(new ol.style.Style({
                image: new ol.style.Circle({
                  radius: 6,
                  fill: new ol.style.Fill({
                      color: '#3399CC'
                  }),
                  stroke: new ol.style.Stroke({
                      color: '#fff',
                      width: 2
                  })
                })
            }));
 
             geolocation.on('change:position', function() {
                var coordinates = geolocation.getPosition();
                positionFeature.setGeometry(coordinates ? new ol.geom.Point(coordinates) : null);
            });
             
             var vectorSource = new ol.source.Vector({
                 features: [accuracyFeature, positionFeature]  // 피처 배열
             });
 
             var vectorLayer = new ol.layer.Vector({
                 map: map,
                 source: vectorSource
             });
             
             
             
         	 // 팝업 오버레이 설정
             var popupElement = document.createElement('div');
             popupElement.className = 'ol-popup';
             var popup = new ol.Overlay({
                 element: popupElement,
                 positioning: 'bottom-center',
                 stopEvent: false
             });
             map.addOverlay(popup);
             
             
             
          // 벡터 레이어에 클릭 이벤트 리스너 추가
             map.on('singleclick', function(evt) {
                 var feature = map.forEachFeatureAtPixel(evt.pixel, function(feature) { //forEachFeatureAtPixel은 원래 여러개의 feature를 반환하는 함수이나, 지금은 feature가 한개만 가져다 쓰도록 함
                     return feature;
                 });

                 if (feature) {
                     var coordinates = evt.coordinate;  // 클릭된 좌표

                     // 팝업 내용 설정
                     var popupContent = '클릭된 피처의 좌표: ' + coordinates;

                     // 팝업에 내용 넣기
                     popupElement.innerHTML = popupContent;

                     // 팝업 위치 설정
                     popup.setPosition(coordinates);
                 }
             });
 
        }
          
        function fetchNearbyMovieTheaters(position) {
            const apiUrl = '/theaters.do';

            const requestData = {
                lat: position.latitude,
                lng: position.longitude
            };

            fetch(apiUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            })
            .then(response => response.json())
            .then(data => {
                console.log("Nearby movie theaters:", data);
            })
            .catch(error => {
                console.error("Error fetching movie theaters:", error);
            });
        }

    
    </script>

<%@ include file="/WEB-INF/jsp/include/footer.jsp" %>