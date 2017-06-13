var app = angular.module('onlineJudgeApp', [ 'ngRoute', 'appCtrls' ]);
// 让子页面里面可以动态加载script的标签
app.directive('loadScript', function() {
	return {
		restrict : 'EA',
		terminal : true,
		link : function(scope, element, attr) {
			if (attr.ngSrc) {
				var s = document.createElement('script');
				s.src = attr.ngSrc;
				document.body.appendChild(s);
			}
		}
	};
});

// 转换日期显示样式的过滤器
app.filter("stringDateFormat", function($filter) {
	return function(self, format) {
		if (self != null) {
			return $filter('date')(new Date(self), format);
		}
		return "";
	};
});

// 编译文件内容为html元素的的过滤器
app.filter("formatToHtml", function($sce) {
	return function(html, id) {
		$("#" + id).html(html);
		return "";
	}
});

// session存储数据
app.factory('sessionDataBase', [ '$window', function($window) {
	return { // 存储单个属性
		set : function(key, value) {
			$window.sessionStorage.setItem(key, value);
		}, // 读取单个属性
		get : function(key, defaultValue) {
			return $window.sessionStorage.getItem(key) || defaultValue;
		}, // 存储对象，以JSON格式存储
		setObject : function(key, value) {
			$window.sessionStorage.setItem(key, angular.toJson(value));
		}, // 读取对象
		getObject : function(key) {
			return angular.fromJson($window.sessionStorage.getItem(key));
		},
		remove : function(key) {
			return $window.sessionStorage.removeItem(key);
		},
		clear : function() {
			$window.sessionStorage.clear();
		}

	}
} ]);

// 定义一个 拦截器 ，稍等将会把它作为 Interceptors 的处理函数
app.factory('httpInterceptor', [ '$q', '$injector', '$rootScope',
		'sessionDataBase',
		function($q, $injector, $rootScope, sessionDataBase) {
			var httpInterceptor = {
				'responseError' : function(response) {
					if (response.status == 301) {
						alert("请登录");
						sessionDataBase.remove("user");
						$rootScope.userData = null;
					}
					return $q.reject(response);
				},
				'response' : function(response) {
					return response;
				},
				'request' : function(config) {
					return config;
				},
				'requestError' : function(config) {
					return $q.reject(config);
				}
			}
			return httpInterceptor;
		} ]);

app
		.config(function($httpProvider, $routeProvider) {
			// 添加对应的 Interceptors
			$httpProvider.interceptors.push("httpInterceptor");
			if (!$httpProvider.defaults.headers.get) {
				$httpProvider.defaults.headers.get = {};
			}
			$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
			// 禁用IE对ajax的缓存
			$httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
			$httpProvider.defaults.headers.get['Pragma'] = 'no-cache';

			$routeProvider.when('/welcome', {
				templateUrl : 'welcome.html'
			}).when('/announcement/index', {
				templateUrl : 'announcement/announcement.html'
			}).when('/problem/index', {
				templateUrl : 'problem/index.html'
			}).when('/problem/detail', {
				templateUrl : 'problem/detail.html'
			}).when('/user/index', {
				templateUrl : 'user/index.html'
			}).when('/competition/index', {
				templateUrl : 'competition/index.html'
			}).when('/competition/answer', {
				templateUrl : 'competition/answer.html'
			}).when('/announcement/index', {
				templateUrl : 'announcement/index.html'
			}).when('/leaderboard/index', {
				templateUrl : 'leaderboard/index.html'
			}).otherwise({
				redirectTo : '/welcome'
			});
		});

// 定义一个通用的分页service
app.service('$pageService', [ '$http', function($http) {
	this.loadingData = function($scope, wantPageNumber, url) {
		$http({
			method : "get",
			url : url,
			params : {
				pageShowCount : $scope.page.pageShowCount,
				wantPageNumber : wantPageNumber
			}
		}).success(function(response) {
			$scope.isLoadingData = false;
			$scope.page.currentPage = response.currentPage;
			$scope.page.totalCount = response.totalCount;
			$scope.page.totalPage = response.totalPage;
			$scope.page.datas = response.result;
			$scope.isCanNext = false;
			$scope.isCanPre = false;

			if ($scope.page.totalPage > $scope.page.currentPage) {
				$scope.isCanNext = true;
			}

			if ($scope.page.currentPage > 1) {
				$scope.isCanPre = true;
			}
		}).error(function(response) {
		});
	}

	this.changePage = function(isNext, $scope, url) {
		var page = $scope.page.currentPage;
		if (isNext) {
			page++;
		} else {
			page--;
		}

		this.loadingData($scope, page, url)
	};
} ]);