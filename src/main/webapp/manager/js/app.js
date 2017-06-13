var managerApp = angular.module('managerApp', [ 'ngRoute', 'managerAppCtrls' ]);
// 让子页面里面可以动态加载script的标签
managerApp.directive('loadScript', function() {
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
managerApp.filter("stringDateFormat", function($filter) {
	return function(self, format) {
		if (self != null) {
			return $filter('date')(new Date(self), format);
		}
		return "";
	};
});
// 编译文件内容为html元素的的过滤器
managerApp.filter("formatToHtml", function($sce) {
	return function(html, id) {
		$("#" + id).html(html);
		return "";
	}
});
// 定义一个 拦截器 ，稍等将会把它作为 Interceptors 的处理函数
managerApp.factory('httpInterceptor', [ '$q', '$injector',
		function($q, $injector) {
			var httpInterceptor = {
				'responseError' : function(response) {
					console.log(response);
					if (response.status == 301) {
						window.location = response.data;
					} else if (response.status == 403) {
						alert("你没有权限，进行改操作");
						return response;
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

managerApp
		.config(function($httpProvider, $routeProvider) {
			// 添加对应的 Interceptors
			$httpProvider.interceptors.push("httpInterceptor");
			if (!$httpProvider.defaults.headers.get) {
				$httpProvider.defaults.headers.get = {};
			}
			// Enables Request.IsAjaxRequest() in ASP.NET MVC
			$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
			// 禁用IE对ajax的缓存
			$httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
			$httpProvider.defaults.headers.get['Pragma'] = 'no-cache';

			$routeProvider.when('/index', {
				templateUrl : 'system/index.html'
			}).when('/manager/index', {
				templateUrl : 'manager/index.html'
			}).when('/role/index', {
				templateUrl : 'role/index.html'
			}).when('/user/index', {
				templateUrl : 'user/index.html'
			}).when('/problemType/index', {
				templateUrl : 'problemType/index.html'
			}).when('/problem/index', {
				templateUrl : 'problem/index.html'
			}).when('/announcement/index', {
				templateUrl : 'announcement/index.html'
			}).when('/competitionApplication/index', {
				templateUrl : 'competitionApplication/index.html'
			}).when('/competition/index', {
				templateUrl : 'competition/index.html'
			}).when('/competitionAccount/index', {
				templateUrl : 'competitionAccount/index.html'
			}).otherwise({
				redirectTo : '/index'
			});
		});

// 定义一个通用的分页service
managerApp.service('$pageService', [ '$http', function($http) {
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
			alert("数据加载失败");
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