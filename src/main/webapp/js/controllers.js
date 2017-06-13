var appCtrls = angular.module('appCtrls', []);

appCtrls
		.controller(
				'leaderboardCtr',
				function($scope, $http) {
					$http
							.get("UserController/leaderboard")
							.success(
									function(response) {
										$scope.haveDoneProblemTop50 = response.haveDoneProblemTop50;
										$scope.rightProblemTop50 = response.rightProblemTop50;
										$scope.sloveProblemTotalValueTop50 = response.sloveProblemTotalValueTop50;
									});
				});

appCtrls.controller('problemDetailCtr', function($scope, $http, $timeout,
		sessionDataBase) {
	$scope.problemDetailObj = sessionDataBase.getObject("problemDetailObj");
	$scope.answerStatus = {};
	$scope.answerStatus.disabled = false;
	$scope.answerStatus.buttonText = "提交代码";

	$scope.answerDialogShow = function() {
		$scope.answerData = {};
		$scope.answerData.codeLanguage = "java";
		$scope.answerData.submitProblemId = $scope.problemDetailObj.problemId;
		$scope.answerData.code = null;
		$("#answerDialog").modal("show");
	}

	$scope.answerSubmit = function() {
		$scope.answerStatus.disabled = true;
		$scope.answerStatus.buttonText = "稍后才能再提交……";
		$timeout(function() {
			$scope.answerStatus.disabled = false;
			$scope.answerStatus.buttonText = "提交代码";
		}, 5000);

		$http({
			method : "post",
			data : jQuery.param($scope.answerData),
			url : "ProblemController/submitAnswer",
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		}).success(function(data, status, headers, config) {
			if (data.success) {
				alert("提交成功,在个人信息中可以看到结果");
				$("#answerDialog").modal("hide");
			} else {
				alert("提交失败：" + data.message);
			}
		}).error(function(response, status, headers, config) {
			if (response.message != null) {
				alert(response.message);
			} else {
				$scope.error = {};
				$scope.error = response;
			}
		});
	}
});

// 省略显示过滤器
appCtrls.filter("omitDisplay", function() {
	return function(data, length) {
		if (data.length > length) {
			return "信息过多，请在详情中查看"
		} else {
			return data;
		}
	}
});

appCtrls
		.controller(
				'userCtr',
				function($rootScope, $scope, $http, $pageService,
						sessionDataBase) {
					$scope.isUpdate = false;
					$scope.updateButtonText = "修改信息";
					$scope.isCanPre = false;
					$scope.isCanNext = false;
					$scope.page = {
						currentPage : 1,
						pageShowCount : 10,
						datas : null,
						totalCount : null,
						totalPage : null
					}

					// 首次加载数据
					$pageService.loadingData($scope, $scope.page.currentPage,
							"SubmitRecordController/list");

					$scope.changePage = function(isNext) {
						$pageService.changePage(isNext, $scope,
								"SubmitRecordController/list")
					};

					$scope.edit = function() {
						$scope.editData = {};
						$scope.isUpdate = !$scope.isUpdate;
						if ($scope.isUpdate) {
							$scope.updateButtonText = "关闭修改";
						} else {
							$scope.updateButtonText = "修改信息";
						}
					}

					$scope.editSubmit = function() {
						$http(
								{
									method : "post",
									data : jQuery.param($scope.editData),
									url : "UserController/updateSubmit",
									headers : {
										"Content-Type" : "application/x-www-form-urlencoded"
									}
								}).success(
								function(data, status, headers, config) {
									if (data.success) {
										$rootScope.userData = data.user;
										sessionDataBase.setObject("user",
												$scope.userData);
										$scope.isUpdate = false;
										$scope.updateButtonText = "修改信息";
										alert("修改成功");
									} else {
										alert(data.message);
									}
								}).error(
								function(response, status, headers, config) {
									if (response.message != null) {
										alert(response.message);
									} else {
										$scope.error = {};
										$scope.error = response;
									}
								});
					}

					$scope.sendEmail = function() {
						$http.get("UserController/sendUpdateCodeEmail")
								.success(function(response) {
									if (response.success) {
										alert("邮件发送成功，请尽快查看邮件中的验证码");
									} else {
										alert("系统错误错误，邮件发送失败。");
									}
								});
					}

					$scope.detail = function(index) {
						var detailObj = $scope.page.datas[index];
						$http
								.get(
										"SubmitRecordController/submitDetail?submitId="
												+ detailObj.submitId
												+ "&tableName="
												+ detailObj.submitRecordTableName)
								.success(
										function(response) {
											if (response.success) {
												$scope.submitDetails = response.submitDetails;
												$("#detailDialog")
														.modal("show");
											} else {
												alert(response.message);
											}
										}).error(function(response) {
									alert(response.message);
								});
					}

					$scope.download = function(index) {
						alert("请不要禁止弹出窗口，否则将无法下载两个文件");
						var obj = $scope.submitDetails.items[index];
						console.log("下载输入文件");
						window
								.open("SubmitRecordController/downloadFileByPath?fileType=in&filePath="
										+ obj.inputFilePath);
						console.log("下载输出文件");
						window
								.open("SubmitRecordController/downloadFileByPath?fileType=out&filePath="
										+ obj.outputFilePath);
					};
				});

appCtrls.controller('indexCtr', function($rootScope, $scope, $http,
		sessionDataBase) {
	$rootScope.userData = sessionDataBase.getObject("user");

	$scope.showLogin = function() {
		$scope.loginData = {};
		$("#loginDialog").modal("show");
	};

	$scope.changeVerificationCode = function() {
		$http.get("UserController/changeVerificationCode").success(
				function(response) {
					$("#verificationCode").attr('src',
							response.verificationCode);
				});
	};

	$scope.showRegister = function() {
		$scope.registerData = {};

		$scope.changeVerificationCode();
		$("#registerDialog").modal("show");
	};

	$scope.loginSubmit = function() {
		$http({
			method : "post",
			data : jQuery.param($scope.loginData),
			url : "UserController/login",
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		}).success(function(data, status, headers, config) {
			if (data.success) {
				$rootScope.userData = data.user;
				sessionDataBase.setObject("user", $scope.userData);
				$("#loginDialog").modal("hide");
			} else {
				alert(data.message);
			}
		}).error(function(response, status, headers, config) {
			if (response.message != null) {
				alert(response.message);
			} else {
				$scope.error = {};
				$scope.error = response;
			}
		});
	};

	$scope.registerSubmit = function() {

		$http({
			method : "post",
			data : jQuery.param($scope.registerData),
			url : "UserController/register",
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		}).success(function(data, status, headers, config) {
			if (data.success) {
				alert("注册成功");
				$("#registerDialog").modal("hide");
			} else {
				alert(data.message);
			}
		}).error(function(response, status, headers, config) {
			if (response.message != null) {
				alert(response.message);
			} else {
				$scope.error = {};
				$scope.error = response;
			}
		});

	};

	$scope.loginOut = function() {
		$http.get("UserController/logout");
		sessionDataBase.remove("user");
		$rootScope.userData = null;
	}

	$scope.showForget = function() {
		$scope.forgetData = {};

		$("#forgetDialog").modal("show");
	};

	$scope.sendForgetEmail = function() {
		$http.get(
				"UserController/sendForgetPasswordEmail/"
						+ $scope.forgetData.account).success(
				function(response) {
					if (response.success) {
						alert("邮件发送成功，请尽快查看邮件中的验证码");
					} else {
						alert(response.message);
					}
				}).error(function(response) {
			alert(response.message);
		});
	};

	$scope.forgetUpdate = function() {
		$http({
			method : "post",
			data : jQuery.param($scope.forgetData),
			url : "UserController/updatePasswordSubmit",
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		}).success(function(data, status, headers, config) {
			if (data.success) {
				alert("修改成功");
				$("#forgetDialog").modal("hide");
			} else {
				alert(data.message);
			}
		}).error(function(response, status, headers, config) {
			if (response.message != null) {
				alert(response.message);
			} else {
				$scope.error = {};
				$scope.error = response;
			}
		});
	};
});

appCtrls.controller('competitionCtr', function($scope, $http, $pageService,
		$location, sessionDataBase) {
	$scope.isCanPre = false;
	$scope.isCanNext = false;
	$scope.page = {
		currentPage : 1,
		pageShowCount : 5,
		datas : null,
		totalCount : null,
		totalPage : null
	}

	// 首次加载数据
	$pageService.loadingData($scope, $scope.page.currentPage,
			"CompetitionController/list");

	$scope.changePage = function(isNext) {
		$pageService.changePage(isNext, $scope, "CompetitionController/list")
	};

	$scope.apply = function(index) {
		$scope.applyObj = angular.copy($scope.page.datas[index]);
		alert("验证申请中，请点击确定后，等待一下弹窗出现");
		$http.get(
				"CompetitionController/applyToken?id="
						+ $scope.applyObj.competitionId).success(
				function(response) {
					if (response.success) {
						$scope.applyObj.token = response.applyToken;
						$("#applyDialog").modal("show");
					} else {
						alert(response.message);
					}
				}).error(function(response) {
			alert(response.message);
		});
	}

	$scope.applySubmit = function() {
		$scope.applyObj.applicationPeopleCount = 1;
		$http({
			method : "post",
			data : jQuery.param($scope.applyObj),
			url : "CompetitionController/apply",
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		}).success(function(data, status, headers, config) {
			if (data.success) {
				alert("申请成功");
				$("#applyDialog").modal("hide");
			} else {
				alert(data.message);
			}
		}).error(function(response, status, headers, config) {
			if (response.message != null) {
				alert(response.message);
			} else {
				$scope.error = {};
				$scope.error = response;
			}
		});
	}

	$scope.login = function(index) {
		$scope.loginObj = angular.copy($scope.page.datas[index]);
		alert("登录验证中，请点击确定后，等待一下弹窗出现");
		$http.get(
				"CompetitionController/loginToken?id="
						+ $scope.loginObj.competitionId).success(
				function(response) {
					if (response.success) {
						$scope.loginObj.token = response.loginToken;
						$("#loginDialog").modal("show");
					} else {
						alert(response.message);
					}
				}).error(function(response) {
			alert(response.message);
		});
	}

	$scope.loginSubmit = function() {
		$http({
			method : "post",
			data : jQuery.param($scope.loginObj),
			url : "CompetitionController/login",
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		}).success(function(data, status, headers, config) {
			if (data.success) {
				$("#loginDialog").modal("hide");
				$scope.loginObj.password = null;
				$scope.loginObj.token = null;
				sessionDataBase.setObject("loginObj", $scope.loginObj);
				$location.path("/competition/answer");
			} else {
				alert(data.message);
			}
		}).error(function(response, status, headers, config) {
			if (response.message != null) {
				alert(response.message);
			} else {
				$scope.error = {};
				$scope.error = response;
			}
		});
	}

	$scope.checkTime = function(time) {
		var now = new Date();
		var time = new Date(time);
		if (time > now) {
			return true;
		}
		return false;
	}
});

appCtrls
		.controller(
				'competitionAnswerCtr',
				function($scope, $http, $location, sessionDataBase) {
					$scope.isLoadingData = true;
					$scope.loginObj = sessionDataBase.getObject("loginObj");
					$scope.competitionData = sessionDataBase
							.getObject("competitionData");

					if ($scope.competitionData == null
							|| $scope.loginObj.competitionId != $scope.competitionData.competitionId) {
						$http
								.get("CompetitionController/getCompetitionData")
								.success(
										function(response) {
											$scope.isLoadingData = false;
											if (response.success) {
												$scope.competitionData = response;
												sessionDataBase.setObject(
														"competitionData",
														$scope.competitionData);
											} else {
												alert(response.message);
											}
										}).error(function(response) {
									$scope.isLoadingData = false;
									alert(response.message);
								});
					} else {
						$scope.isLoadingData = false;
					}

					$scope.detail = function(index) {
						$scope.detailProblemObj = $scope.competitionData.competitionProblems[index];
					}

					$scope.answerDialogShow = function() {
						$scope.answerData = {};
						$scope.answerData.codeType = "java";
						$scope.answerData.problemId = $scope.detailProblemObj.problemId;
						$scope.answerData.code = null;
						$("#competitionAnswerDialog").modal("show");
					}

					$scope.answerSubmit = function() {
						$http(
								{
									method : "post",
									data : jQuery.param($scope.answerData),
									url : "CompetitionController/submitCompetitionProblemAnswer",
									headers : {
										"Content-Type" : "application/x-www-form-urlencoded"
									}
								})
								.success(
										function(data, status, headers, config) {
											if (data.success) {
												alert("提交成功");
												$("#competitionAnswerDialog")
														.modal("hide");
												$scope.detailProblemObj.isHaveSubmit = true;
												// 更新一下存储的数据，避免刷新后已经提交的状态就没了
												sessionDataBase.setObject(
														"competitionData",
														$scope.competitionData);
											} else {
												alert("提交失败：" + data.message);
											}
										}).error(
										function(response, status, headers,
												config) {
											if (response.message != null) {
												alert(response.message);
											} else {
												$scope.error = {};
												$scope.error = response;
											}
										});
					}

					$scope.logout = function() {
						if (window.confirm('你确定要退出登录吗？')) {
							$http.get("CompetitionController/logout");
							sessionDataBase.remove("loginObj");
							sessionDataBase.remove("competitionData");
							$location.path("/competition/index");
						}
					}
				});

// 编译文件内容为html元素的的过滤器
appCtrls.filter("imageRandom",
		function() {
			return function(data, width, height, index) {
				var n = index % 4;
				if (n >= 0 && n < 1) {
					return "http://lorempixel.com/" + width + "/" + height
							+ "/city/";
				} else if (n >= 1 && n < 2) {
					return "http://lorempixel.com/" + width + "/" + height
							+ "/cats/";
				} else if (n >= 2 && n < 3) {
					return "http://lorempixel.com/" + width + "/" + height
							+ "/food/";
				} else {
					return "http://lorempixel.com/" + width + "/" + height
							+ "/nature/";
				}

			}
		});

appCtrls.filter("problemTypeFilter", function() {
	return function(problemTypeId, problemTypes) {
		for ( var index in problemTypes) {
			if (problemTypes[index].problemTypeId == problemTypeId) {
				return problemTypes[index].problemTypeName;
			}
		}
		return "";
	};
});

appCtrls
		.controller(
				'problemCtr',
				function($rootScope, $scope, $http, sessionDataBase) {
					$scope.loadAllProblemType = function() {
						$http
								.get("ProblemTypeController/findAll")
								.success(
										function(response) {
											$scope.allProblemType = response.allProblemType;
										});
					}
					$scope.loadAllProblemType();

					$scope.isCanPre = false;
					$scope.isCanNext = false;
					$scope.page = {
						currentPage : 0,
						wantPageNumber : 1,
						pageShowCount : 10,
						datas : null,
						totalCount : null,
						totalPage : null,
						method : "list"
					}

					$scope.loadingData = function() {
						$http({
							method : "get",
							url : "ProblemController/" + $scope.page.method,
							params : $scope.page
						})
								.success(
										function(response) {
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
					$scope.loadingData();

					$scope.changePage = function(isNext) {
						if (isNext) {
							$scope.page.wantPageNumber++;
						} else {
							$scope.page.wantPageNumber--;
						}

						this.loadingData();
					};

					$scope.search = function() {
						// 重置搜索对象
						$scope.page = {
							currentPage : 0,
							wantPageNumber : 1,
							pageShowCount : 10,
							datas : null,
							totalCount : null,
							totalPage : null,
							method : "list"
						}

						$scope.loadingData();
					}

					$scope.searchById = function() {
						// 重置搜索对象
						$scope.page = {
							currentPage : 0,
							wantPageNumber : 1,
							pageShowCount : 10,
							datas : null,
							totalCount : null,
							totalPage : null,
							method : "/search/" + $scope.searchId
						}

						$scope.loadingData();
					}

					$scope.searchByName = function() {
						// 重置搜索对象
						$scope.page = {
							currentPage : 0,
							wantPageNumber : 1,
							pageShowCount : 10,
							datas : null,
							totalCount : null,
							totalPage : null,
							method : "/searchByName",
							problemName : $scope.searchName
						}

						$scope.loadingData();
					}

					$scope.searchByValue = function() {
						// 重置搜索对象
						$scope.page = {
							currentPage : 0,
							wantPageNumber : 1,
							pageShowCount : 10,
							datas : null,
							totalCount : null,
							totalPage : null,
							method : "/searchByValue",
							problemValue : $scope.searchValue
						}

						$scope.loadingData();
					}

					$scope.searchByDifficulty = function() {
						// 重置搜索对象
						$scope.page = {
							currentPage : 0,
							wantPageNumber : 1,
							pageShowCount : 10,
							datas : null,
							totalCount : null,
							totalPage : null,
							method : "/searchByDifficulty",
							problemDifficulty : $scope.searchDifficulty
						}

						$scope.loadingData();
					}

					$scope.searchByType = function() {
						// 重置搜索对象
						$scope.page = {
							currentPage : 0,
							wantPageNumber : 1,
							pageShowCount : 10,
							datas : null,
							totalCount : null,
							totalPage : null,
							method : "/searchByType",
							problemTypeId : $scope.searchTypeId
						}

						$scope.loadingData();
					}

					$scope.jumpDetail = function(index) {
						var problemDetailObj = $scope.page.datas[index];
						problemDetailObj.typeName = null;
						for ( var n in $scope.allProblemType) {
							if ($scope.allProblemType[n].problemTypeId == problemDetailObj.problemTypeId) {
								problemDetailObj.typeName = $scope.allProblemType[n].problemTypeName;
								break;
							}
						}

						sessionDataBase.setObject("problemDetailObj",
								problemDetailObj);
						return true;
					}
				});

appCtrls.controller('announcementCtr', function($scope, $http, $pageService) {
	$scope.goBack = function() {
		$('#menu-container .detail').fadeOut(1000, function() {
			$('#menu-container .homepage').fadeIn(1000);
		});
	};

	$scope.showDetail = function(index) {
		$scope.currentDetailObj = $scope.page.datas[index];
		$('#menu-container .homepage').fadeOut(1000, function() {
			$('#menu-container .detail').fadeIn(1000);
		});
	};

	$scope.isCanPre = false;
	$scope.isCanNext = false;
	$scope.page = {
		currentPage : 1,
		pageShowCount : 4,
		datas : null,
		totalCount : null,
		totalPage : null
	}

	// 首次加载数据
	$pageService.loadingData($scope, $scope.page.currentPage,
			"AnnouncementController/list");

	$scope.changePage = function(isNext) {
		$pageService.changePage(isNext, $scope, "AnnouncementController/list")
	};

});