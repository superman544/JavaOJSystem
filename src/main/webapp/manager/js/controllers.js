var managerAppCtrls = angular.module('managerAppCtrls', []);

managerAppCtrls.controller('managerIndexCtr', function($scope, $http) {
			$http.get("../AdminManagerController/getNickname").success(
					function(response) {
						if (response.success) {
							$scope.nickname = response.nickname;
						}
					});

		});

managerAppCtrls.filter("timeShow", function() {
			return function(data) {
				var seconds = 1000;
				var minute = seconds * 60;
				var hour = minute * 60;
				var day = hour * 24;
				var time = "";
				var temp;

				if (data > day) {
					temp = Math.floor(data / day);
					time += (temp + "天");
					data -= (temp * day);
				}

				if (data > hour) {
					temp = Math.floor(data / hour);
					time += (temp + "小时");
					data -= (temp * hour);
				}

				if (data > minute) {
					temp = Math.floor(data / minute);
					time += (temp + "分钟");
					data -= (temp * minute);
				}

				if (data > seconds) {
					temp = Math.floor(data / seconds);
					time += (temp + "秒");
				}

				return time;
			};
		});

managerAppCtrls.controller('managerSystemCtr', function($scope, $http,
				$location) {
			var websocket;
			var hostPort = $location.host() + ":" + $location.port();

			if ('WebSocket' in window) {
				console.log("启用WebSocket");
				// 工程名这样就写死了，但是真正部署应用的时候，应该做到URL去工程名
				websocket = new WebSocket("ws://" + hostPort
						+ "/JavaOJSystem/manager/OJSystemWebSocketServer");
			} else if ('MozWebSocket' in window) {
				console.log("启用MozWebSocket");
				websocket = new MozWebSocket("ws://" + hostPort
						+ "/JavaOJSystem/manager/OJSystemWebSocketServer");
			} else {
				console.log("启用sockjs");
				// 模拟过，好像是支持到IE7的
				websocket = new SockJS("http://" + hostPort
						+ "manager/sockjs/OJSystemWebSocketServer");
			}
			websocket.onopen = function(evnt) {

			};
			websocket.onmessage = function(message) {
				var obj = jQuery.parseJSON(message.data);
				if (obj.responseType == 'PendingHandleProblemCount') {
					$scope.pendingHandleProblemCount = obj.data;
				} else if (obj.responseType == 'JavaSandboxStatus') {
					$scope.allSandboxStatus = obj.data;
				} else if (obj.responseType == 'OperationSystemInfo') {
					$scope.operationSystemInfo = obj.data;
				} else if (obj.responseType == 'AppSystemInfo') {
					$scope.appInfo = obj.data;
				}

				$scope.$apply();
			};
			websocket.onerror = function(evnt) {
			};
			websocket.onclose = function(evnt) {
			};

			$scope.open = function() {
				$http.get("../OJSystemController/openNewJavaSandbox").success(
						function(response) {
							alert("发送请求成功");
						});
			}

			$scope.closeAll = function() {
				$http.post("../OJSystemController/closeAllJavaSandbox")
						.success(function(response) {
									alert("发送请求成功");
								});
			}

			$scope.closeOne = function(index) {
				var sandboxStatus = $scope.allSandboxStatus[index];
				$http
						.post("../OJSystemController/closeJavaSandboxByIdCard?idCard="
								+ sandboxStatus.idCard).success(
								function(response) {
									alert("发送请求成功");
								});
			}

		});// managerSystemCtr

managerAppCtrls.controller('managerCompetitionAccountCtr', function($scope,
		$http) {
	$scope.selectedId = -1;
	$scope.loadCompetition = function() {
		$http.get("../AdminCompetitionAccountController/getAllCompetitionIds")
				.success(function(response) {
							$scope.allCompetitionIds = response.allCompetitionIds;
						});
	}
	$scope.loadCompetition();

	$scope.getAllAccount = function() {
		var selectedCompetitionId = $("#selectedCompetitionId").val();

		if (selectedCompetitionId == null) {
			alert("请先选择具体的比赛编号");
			return;
		}

		$http.get("../AdminCompetitionAccountController/getCompetitionAccount/"
				+ selectedCompetitionId).success(function(response) {
					$scope.allAccounts = response.allAccounts;
				});
	}

	$scope.detail = function(index) {
		$scope.currentDetailObj = $scope.allAccounts[index];

		$("#detailDialog").modal("show");
	};

	$scope.edit = function(index) {
		$scope.currentUpdateObj = angular.copy($scope.allAccounts[index]);
		$scope.currentUpdateObj.index = index;
		$("#updateDialog").modal("show");
	};

	$scope.editSubmit = function() {
		// 用于封装提交的信息
		var submitData = angular.copy($scope.currentUpdateObj);

		$http({
					method : "post",
					data : jQuery.param(submitData),
					url : "../AdminCompetitionAccountController/update",
					headers : {
						"Content-Type" : "application/x-www-form-urlencoded"
					}
				}).success(function(data, status, headers, config) {
					if (data.success) {
						alert("更新成功");
						// 更新界面的内容
						var index = submitData.index;
						$scope.allAccounts[index] = submitData;
						$("#updateDialog").modal("hide");
					}
				}).error(function(response, status, headers, config) {
					$scope.error = {};
					$scope.error = response;
				});
	};
});// managerCompetitionAccountCtr

managerAppCtrls.controller('managerCompetitionCtr', function($scope, $http,
		$filter, $pageService) {
	$scope.isLoadingData = true;
	$scope.isCanPre = false;
	$scope.isCanNext = false;
	$scope.page = {
		currentPage : 1,
		pageShowCount : 6,
		datas : null,
		totalCount : null,
		totalPage : null
	}

	// 首次加载数据
	$pageService.loadingData($scope, $scope.page.currentPage,
			"../AdminCompetitionController/list");

	$scope.refresh = function() {
		$pageService.loadingData($scope, $scope.page.currentPage,
				"../AdminCompetitionController/list");
		alert("开始刷新数据");
	}

	$scope.changePage = function(isNext) {
		$pageService.changePage(isNext, $scope,
				"../AdminCompetitionController/list")
	};

	$scope.detail = function(index) {
		$scope.currentDetailObj = $scope.page.datas[index];

		$("#detailDialog").modal("show");
	};

	$scope.add = function() {
		$scope.currentAddObj = {};
		$scope.currentAddObj.isPublish = false;
		$scope.currentAddObj.isCanDeclare = false;
		$("#addDialog").modal("show");
	};

	$scope.addSubmit = function() {
		$scope.currentAddObj.competitionBeginTime = $("#currentAddObjCompetitionBeginTime")
				.val();
		$scope.currentAddObj.competitionEndTime = $("#currentAddObjCompetitionEndTime")
				.val();
		$scope.currentAddObj.competitionApplyBeginTime = $("#currentAddObjCompetitionApplyBeginTime")
				.val();
		$scope.currentAddObj.competitionApplyEndTime = $("#currentAddObjCompetitionApplyEndTime")
				.val();
		console.log($scope.currentAddObj);
		$http({
					method : "post",
					data : jQuery.param($scope.currentAddObj),
					url : "../AdminCompetitionController/add",
					headers : {
						"Content-Type" : "application/x-www-form-urlencoded"
					}
				}).success(function(response) {
					if (response.success) {
						alert("添加成功");
						$("#addDialog").modal("hide");
					}
				}).error(function(response) {
					if (response.message != null) {
						alert(response.message);
						return;
					}
					alert("添加失败");
					$scope.error = {};
					$scope.error = response;
				});
	};

	$scope.delete = function(index) {
		if (confirm("你确定要删除吗？")) {
			var deleteId = $scope.page.datas[index].competitionId;
			$http.post("../AdminCompetitionController/delete?id=" + deleteId)
					.success(function(data, status, headers, config) {
								if (data.success) {
									alert("删除成功");
									$scope.page.datas.splice(index, 1);
									$scope.page.totalCount--;
								} else {
									alert("删除失败");
								}
							}).error(
							function(response, status, headers, config) {
								alert("删除失败");
							});
		}
	};

	$scope.edit = function(index) {
		$scope.currentUpdateObj = angular.copy($scope.page.datas[index]);
		$scope.currentUpdateObj.index = index;
		$scope.currentUpdateObj.competitionEndTime = $filter('date')(
				new Date($scope.currentUpdateObj.competitionEndTime),
				'yyyy-MM-dd HH:mm:ss');
		$scope.currentUpdateObj.competitionBeginTime = $filter('date')(
				new Date($scope.currentUpdateObj.competitionBeginTime),
				'yyyy-MM-dd HH:mm:ss');
		if ($scope.currentUpdateObj.isCanDeclare) {
			$scope.currentUpdateObj.competitionApplyEndTime = $filter('date')(
					new Date($scope.currentUpdateObj.competitionApplyEndTime),
					'yyyy-MM-dd HH:mm:ss');
			$scope.currentUpdateObj.competitionApplyBeginTime = $filter('date')(
					new Date($scope.currentUpdateObj.competitionApplyBeginTime),
					'yyyy-MM-dd HH:mm:ss');
		}
		$("#updateDialog").modal("show");
	};

	$scope.editSubmit = function() {
		// 用于封装提交的信息
		var submitData = angular.copy($scope.currentUpdateObj);
		submitData.competitionBeginTime = $("#currentUpdateObjCompetitionBeginTime")
				.val();
		submitData.competitionEndTime = $("#currentUpdateObjCompetitionEndTime")
				.val();
		if ($scope.currentUpdateObj.isCanDeclare) {
			submitData.competitionApplyBeginTime = $("#currentUpdateObjCompetitionApplyBeginTime")
					.val();
			submitData.competitionApplyEndTime = $("#currentUpdateObjCompetitionApplyEndTime")
					.val();
		}
		$http({
					method : "post",
					data : jQuery.param(submitData),
					url : "../AdminCompetitionController/update",
					headers : {
						"Content-Type" : "application/x-www-form-urlencoded"
					}
				}).success(function(data, status, headers, config) {
					if (data.success) {
						alert("更新成功");
						// 更新界面的内容
						var index = submitData.index;
						$scope.page.datas[index] = submitData;
						$("#updateDialog").modal("hide");
					}
				}).error(function(response, status, headers, config) {
					$scope.error = {};
					$scope.error = response;
				});
	};

	$scope.report = function(index) {
		$scope.currentReportObj = $scope.page.datas[index];

		$("#reportDialog").modal("show");
	}

	$scope.createReport = function() {
		$http.post("../AdminCompetitionController/createReport?id="
				+ $scope.currentReportObj.competitionId).success(
				function(response) {
					alert("发送请求成功");
					$("#reportDialog").modal("hide");
				});
	}

	$scope.downloadReport = function() {
		window
				.open("../AdminCompetitionController/downloadReport?competitionId="
						+ $scope.currentReportObj.competitionId);
	}

	$scope.judgeSubmit = function(isRunNow) {
		$http.post("../AdminCompetitionController/judge?id="
				+ $scope.currentJudgeObj.competitionId + "&isRunNow="
				+ isRunNow).success(function(response) {
					alert("发送请求成功");
					$("#judgeDialog").modal("hide");
				});
	}

	$scope.judge = function(index) {
		$scope.currentJudgeObj = $scope.page.datas[index];

		$("#judgeDialog").modal("show");
	}

	$scope.closeSubmit = function(isRunNow) {
		$http.post("../AdminCompetitionController/close?id="
				+ $scope.currentCloseObj.competitionId + "&isRunNow="
				+ isRunNow).success(function(response) {
					alert("发送请求成功");
					$("#closeDialog").modal("hide");
				});
	}

	$scope.close = function(index) {
		$scope.currentCloseObj = $scope.page.datas[index];

		$("#closeDialog").modal("show");
	}
});// managerCompetitionCtr

managerAppCtrls.controller('managerCompetitionApplicationCtr', function($scope,
		$http) {
	$scope.selectedId = -1;
	$scope.loadCompetition = function() {
		$http
				.get("../AdminCompetitionApplicationController/getAllCompetitionIds")
				.success(function(response) {
							$scope.allCompetitionIds = response.allCompetitionIds;
						});
	}
	$scope.loadCompetition();

	$scope.getAllApplication = function() {
		var selectedCompetitionId = $("#selectedCompetitionId").val();

		if (selectedCompetitionId == null) {
			alert("请先选择具体的比赛编号");
			return;
		}

		$http
				.get("../AdminCompetitionApplicationController/getAllApplication?competitionId="
						+ selectedCompetitionId).success(function(response) {
							$scope.allApplications = response.allApplications;
						});
	}

	$scope.detail = function(index) {
		$scope.currentDetailObj = $scope.allApplications[index];

		$("#detailDialog").modal("show");
	};

	$scope.sendEmail = function(index) {
		$scope.currentSendObj = $scope.allApplications[index];
		$scope.currentSendObj.index = index;
		$("#sendEmailDialog").modal("show");
	}

	$scope.sendEmailSubmit = function() {
		var submitData = angular.copy($scope.currentSendObj);
		$http({
			method : "post",
			data : jQuery.param(submitData),
			url : "../AdminCompetitionApplicationController/sendApplicationEmail",
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		}).success(function(response) {
			if (response.success) {
				alert("发送成功");
				$scope.allApplications[submitData.index].isHaveSendEmail = true;
				$("#sendEmailDialog").modal("hide");
			}
		}).error(function(response) {
					alert("发送失败");
					$scope.error = {};
					$scope.error = response;
				});
	}

	$scope.dispatchAccount = function(index) {
		var application = {};
		application.competitionId = $scope.allApplications[index].competitionId;
		application.applicationId = $scope.allApplications[index].competitionApplicationId;
		$http({
					method : "post",
					data : jQuery.param(application),
					url : "../AdminCompetitionApplicationController/dispatchAccount",
					headers : {
						"Content-Type" : "application/x-www-form-urlencoded"
					}
				}).success(function(response) {
			if (response.success) {
				alert("分配成功");
				$scope.allApplications[index].competitionAccountId = response.dispatchAccountId;
			}
		}).error(function(response) {
					alert("分配失败");
					$scope.error = {};
					$scope.error = response;
				});
	}

	$scope.cancelDispatchAccount = function(index) {
		var application = {};
		application.accountId = $scope.allApplications[index].competitionAccountId;
		application.applicationId = $scope.allApplications[index].competitionApplicationId;
		$http({
			method : "post",
			data : jQuery.param(application),
			url : "../AdminCompetitionApplicationController/cancelDispatchAccount",
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		}).success(function(response) {
					if (response.success) {
						alert("取消分配成功");
						$scope.allApplications[index].competitionAccountId = -1;
					}
				}).error(function(response) {
					alert("取消分配失败");
					$scope.error = {};
					$scope.error = response;
				});
	}
});// managerCompetitionApplicationCtr

managerAppCtrls.filter("problemTypeFilter", function() {
			return function(problemTypeId, problemTypes) {
				for (var index in problemTypes) {
					if (problemTypes[index].problemTypeId == problemTypeId) {
						return problemTypes[index].problemTypeName;
					}
				}
				return "";
			};
		});

managerAppCtrls.controller('managerProblemCtr', function($scope, $http,
		$pageService) {
	$scope.isLoadingData = true;
	$scope.isCanPre = false;
	$scope.isCanNext = false;
	$scope.page = {
		currentPage : 1,
		pageShowCount : 6,
		datas : null,
		totalCount : null,
		totalPage : null
	}

	$scope.loadAllProblemTypes = function() {
		$http.get("../AdminProblemTypeController/findAll").success(
				function(response) {
					$scope.problemTypes = response.problemTypes;
				});
	}

	$scope.loadAllProblemTypes();

	// 首次加载数据
	$pageService.loadingData($scope, $scope.page.currentPage,
			"../AdminProblemController/list");

	$scope.refresh = function() {
		$pageService.loadingData($scope, $scope.page.currentPage,
				"../AdminProblemController/list");
		$scope.loadAllProblemTypes();
		alert("开始刷新数据");
	}

	$scope.changePage = function(isNext) {
		$pageService.changePage(isNext, $scope,
				"../AdminProblemController/list")
	};

	$scope.detail = function(index) {
		$scope.currentDetailObj = $scope.page.datas[index];

		$("#detailDialog").modal("show");
	};

	$scope.add = function() {
		$scope.currentAddObj = {};
		$scope.currentAddObj.isPublish = false;
		$scope.addUmEditor = UM.getEditor('problemAddEditor');
		$("#addDialog").modal("show");
	};

	$scope.addSubmit = function() {
		$scope.currentAddObj.problemContent = $scope.addUmEditor.getContent();
		$http({
					method : "post",
					data : jQuery.param($scope.currentAddObj),
					url : "../AdminProblemController/add",
					headers : {
						"Content-Type" : "application/x-www-form-urlencoded"
					}
				}).success(function(response) {
					if (response.success) {
						alert("添加成功");
						$("#addDialog").modal("hide");
					}
				}).error(function(response) {
					alert("添加失败");
					$scope.error = {};
					$scope.error = response;
				});
	};

	$scope.edit = function(index) {
		$scope.currentUpdateObj = angular.copy($scope.page.datas[index]);
		$scope.currentUpdateObj.index = index;
		$scope.updateUmEditor = UM.getEditor('problemUpdateEditor');
		$scope.updateUmEditor
				.setContent($scope.currentUpdateObj.problemContent);
		$("#updateDialog").modal("show");
	};

	$scope.editSubmit = function() {
		// 用于封装提交的信息
		var submitData = angular.copy($scope.currentUpdateObj);
		submitData.problemContent = $scope.updateUmEditor.getContent();
		$http({
					method : "post",
					data : jQuery.param(submitData),
					url : "../AdminProblemController/update",
					headers : {
						"Content-Type" : "application/x-www-form-urlencoded"
					}
				}).success(function(data, status, headers, config) {
					if (data.success) {
						alert("更新成功");
						// 更新界面的内容
						var index = submitData.index;
						$scope.page.datas[index] = submitData;
						$("#updateDialog").modal("hide");
					}
				}).error(function(response, status, headers, config) {
					$scope.error = {};
					$scope.error = response;
				});
	};

	$scope.delete = function(index) {
		if (confirm("你确定要删除吗？")) {
			var deleteId = $scope.page.datas[index].problemId;
			$http.post("../AdminProblemController/delete?id=" + deleteId)
					.success(function(data, status, headers, config) {
								if (data.success) {
									alert("删除成功");
									$scope.page.datas.splice(index, 1);
									$scope.page.totalCount--;
								} else {
									alert(data.message);
								}
							}).error(
							function(response, status, headers, config) {
								alert("删除失败");
							});
		}
	};

	$scope.files = function(index) {
		var problem = angular.copy($scope.page.datas[index]);
		$scope.isRequesting = true;
		$http
				.get("../AdminProblemController/showProblemStandardFiles?problemId="
						+ problem.problemId).success(function(response) {
							$scope.standardFiles = response.standardFiles;
							$scope.isRequesting = false;
						}).error(function(response) {
							alert(response.message);
							$scope.isRequesting = false;
						});

		// 每一次弹出窗口，都重新添加一次控件，因为控件重新渲染一次，可以解决很多问题
		$("#problemFileUploadForm")
				.html('<label>输入输出文件</label> <input id="files" name="files" type="file" multiple data-min-file-count="2">');

		$('#files').fileinput({
			language : 'zh',
			overwriteInitial : false,
			uploadAsync : false,
			maxFileSize : 1000,// 上传文件最大的尺寸
			maxFilesNum : 1,// 上传最大的文件数量
			uploadUrl : '../AdminProblemController/uploadProblemStandardFile?problemId='
					+ problem.problemId,
			allowedFileExtensions : ['txt'],
			browseLabel : '选择',
			removeLabel : '移除',
			removeTitle : '清除选中文件',
			cancelLabel : '取消',
			cancelTitle : '取消进行中的上传',
			uploadLabel : '上传',
			uploadTitle : '上传选中文件',
			dropZoneTitle : "支持拖拽上传,允许同时上传多个文件<br/><br/>input文件的数量必须等于output文件的数量<br/><br/>并且命名规范要为input1.txt,input2.txt……对应的也应该有output1.txt,output2.txt……",
			msgFilesTooLess : '你必须选择最少 <b>{n}</b> {files} 来上传.而且input文件的数量必须等于output文件的数量 ',
			slugCallback : function(filename) {
				return filename.replace('(', '_').replace(']', '_')
			}

		}).on("fileuploaded", function(event, data) {
					console.log(event);
					console.log(data);
					alert(data.message);
				});
		$("#filesDialog").modal("show");
	};

	$scope.downloadFiles = function(index) {
		var standardFile = $scope.standardFiles[index];
		console.log("下载输入文件");
		window
				.open("../AdminProblemController/downloadFileByPath?fileType=in&filePath="
						+ standardFile.inputFilePath);
		console.log("下载输出文件");
		window
				.open("../AdminProblemController/downloadFileByPath?fileType=out&filePath="
						+ standardFile.outputFilePath);
	}

	$scope.deleteFiles = function(index) {
		var standardFile = $scope.standardFiles[index];
		console.log(standardFile);
		var submitData = {};
		submitData.inputFilePath = standardFile.inputFilePath;
		submitData.outputFilePath = standardFile.outputFilePath;
		$http({
					method : "post",
					data : jQuery.param(submitData),
					url : "../AdminProblemController/deleteProblemStandardFile",
					headers : {
						"Content-Type" : "application/x-www-form-urlencoded"
					}
				}).success(function(data, status, headers, config) {
					if (data.success) {
						alert("删除成功");
						$scope.standardFiles.splice(index, 1);
					} else {
						alert("删除失败");
					}
				}).error(function(response, status, headers, config) {
					alert("删除失败");
				});
	}
});// managerProblemCtr

managerAppCtrls.controller('managerAnnouncementCtr', function($scope, $http,
				$pageService) {
			$scope.isLoadingData = true;
			$scope.isCanPre = false;
			$scope.isCanNext = false;
			$scope.page = {
				currentPage : 1,
				pageShowCount : 6,
				datas : null,
				totalCount : null,
				totalPage : null
			}

			// 首次加载数据
			$pageService.loadingData($scope, $scope.page.currentPage,
					"../AdminAnnouncementController/list");

			$scope.refresh = function() {
				$pageService.loadingData($scope, $scope.page.currentPage,
						"../AdminAnnouncementController/list");
				alert("开始刷新数据");
			}

			$scope.changePage = function(isNext) {
				$pageService.changePage(isNext, $scope,
						"../AdminAnnouncementController/list")
			};

			$scope.detail = function(index) {
				$scope.currentDetailObj = $scope.page.datas[index];

				$("#detailDialog").modal("show");
			};

			$scope.add = function() {
				$scope.currentAddObj = {};
				$scope.addUmEditor = UM.getEditor('announcementAddEditor');
				$("#addDialog").modal("show");
			};

			$scope.addSubmit = function() {
				$scope.currentAddObj.announcementContent = $scope.addUmEditor
						.getContent();
				$http({
							method : "post",
							data : jQuery.param($scope.currentAddObj),
							url : "../AdminAnnouncementController/add",
							headers : {
								"Content-Type" : "application/x-www-form-urlencoded"
							}
						}).success(function(response) {
							if (response.success) {
								alert("添加成功");
								$("#addDialog").modal("hide");
							}
						}).error(function(response) {
							alert("添加失败");
							$scope.error = {};
							$scope.error = response;
						});
			};

			$scope.edit = function(index) {
				$scope.currentUpdateObj = angular
						.copy($scope.page.datas[index]);
				$scope.currentUpdateObj.index = index;
				$scope.updateUmEditor = UM
						.getEditor('announcementUpdateEditor');
				$scope.updateUmEditor
						.setContent($scope.currentUpdateObj.announcementContent);
				$("#updateDialog").modal("show");
			};

			$scope.editSubmit = function() {
				// 用于封装提交的信息
				var submitData = angular.copy($scope.currentUpdateObj);
				submitData.announcementContent = $scope.updateUmEditor
						.getContent();
				$http({
							method : "post",
							data : jQuery.param(submitData),
							url : "../AdminAnnouncementController/update",
							headers : {
								"Content-Type" : "application/x-www-form-urlencoded"
							}
						}).success(function(data, status, headers, config) {
							if (data.success) {
								alert("更新成功");
								// 更新界面的内容
								var index = submitData.index;
								$scope.page.datas[index] = submitData;
								$("#updateDialog").modal("hide");
							}
						}).error(function(response, status, headers, config) {
							$scope.error = {};
							$scope.error = response;
						});
			};

			$scope.delete = function(index) {
				if (confirm("你确定要删除吗？")) {
					var deleteId = $scope.page.datas[index].announcementId;
					$http.post("../AdminAnnouncementController/delete?id="
							+ deleteId).success(
							function(data, status, headers, config) {
								if (data.success) {
									alert("删除成功");
									$scope.page.datas.splice(index, 1);
									$scope.page.totalCount--;
								} else {
									alert("删除失败");
								}
							}).error(
							function(response, status, headers, config) {
								alert("删除失败");
							});
				}
			};
		});// managerAnnouncementCtr

managerAppCtrls.controller('managerProblemTypeCtr', function($scope, $http,
				$pageService) {
			$scope.isLoadingData = true;
			$scope.isCanPre = false;
			$scope.isCanNext = false;
			$scope.page = {
				currentPage : 1,
				pageShowCount : 6,
				datas : null,
				totalCount : null,
				totalPage : null
			}

			// 首次加载数据
			$pageService.loadingData($scope, $scope.page.currentPage,
					"../AdminProblemTypeController/list");

			$scope.refresh = function() {
				$pageService.loadingData($scope, $scope.page.currentPage,
						"../AdminProblemTypeController/list");
				alert("开始刷新数据");
			}

			$scope.changePage = function(isNext) {
				$pageService.changePage(isNext, $scope,
						"../AdminProblemTypeController/list")
			};

			$scope.add = function() {
				$scope.currentAddObj = {};

				$("#addDialog").modal("show");
			};

			$scope.addSubmit = function() {

				$http({
							method : "post",
							data : jQuery.param($scope.currentAddObj),
							url : "../AdminProblemTypeController/add",
							headers : {
								"Content-Type" : "application/x-www-form-urlencoded"
							}
						}).success(function(response) {
							if (response.success) {
								alert("添加成功");
								$("#addDialog").modal("hide");
							}
						}).error(function(response) {
							alert("添加失败");
							$scope.error = {};
							$scope.error = response;
						});
			};

			$scope.delete = function(index) {
				if (confirm("你确定要删除吗？")) {
					var deleteId = $scope.page.datas[index].problemTypeId;
					$http.post("../AdminProblemTypeController/delete?id="
							+ deleteId).success(
							function(data, status, headers, config) {
								if (data.success) {
									alert("删除成功");
									$scope.page.datas.splice(index, 1);
									$scope.page.totalCount--;
								} else {
									alert("删除失败");
								}
							}).error(
							function(response, status, headers, config) {
								alert("删除失败");
							});
				}
			};

			$scope.edit = function(index) {
				$scope.currentUpdateObj = angular
						.copy($scope.page.datas[index]);
				$scope.currentUpdateObj.index = index;

				$("#updateDialog").modal("show");
			};

			$scope.editSubmit = function() {
				// 用于封装提交的信息
				var submitData = angular.copy($scope.currentUpdateObj);

				$http({
							method : "post",
							data : jQuery.param(submitData),
							url : "../AdminProblemTypeController/update",
							headers : {
								"Content-Type" : "application/x-www-form-urlencoded"
							}
						}).success(function(data, status, headers, config) {
							if (data.success) {
								alert("更新成功");
								// 更新界面的内容
								var index = submitData.index;
								$scope.page.datas[index] = submitData;
								$("#updateDialog").modal("hide");
							}
						}).error(function(response, status, headers, config) {
							$scope.error = {};
							$scope.error = response;
						});
			}
		});// managerProblemTypeCtr

managerAppCtrls.controller('managerUserCtr', function($scope, $http,
				$pageService) {
			$scope.isLoadingData = true;
			$scope.isCanPre = false;
			$scope.isCanNext = false;
			$scope.page = {
				currentPage : 1,
				pageShowCount : 6,
				datas : null,
				totalCount : null,
				totalPage : null
			}

			// 首次加载数据
			$pageService.loadingData($scope, $scope.page.currentPage,
					"../AdminUserController/list");

			$scope.refresh = function() {
				$pageService.loadingData($scope, $scope.page.currentPage,
						"../AdminUserController/list");
				alert("开始刷新数据");
			}

			$scope.changePage = function(isNext) {
				$pageService.changePage(isNext, $scope,
						"../AdminUserController/list")
			};

			$scope.detail = function(index) {
				$scope.currentDetailObj = $scope.page.datas[index];
				$("#detailDialog").modal("show");
			};

			$scope.ban = function(index) {
				if (confirm("你确定要封禁吗？封禁后将不能登陆系统。")) {
					var userId = $scope.page.datas[index].userId;
					$http.post("../AdminUserController/ban?userId=" + userId
							+ "&isBan=true").success(
							function(data, status, headers, config) {
								if (data.success) {
									alert("封禁成功");
									$scope.page.datas[index].isBan = true;
								} else {
									alert("封禁失败");
								}
							}).error(
							function(response, status, headers, config) {
								alert("封禁失败");
							});
				};
			};

			$scope.notBan = function(index) {
				if (confirm("你确定要解封吗？解封后将不能登陆系统。")) {
					var userId = $scope.page.datas[index].userId;
					$http.post("../AdminUserController/ban?userId=" + userId
							+ "&isBan=false").success(
							function(data, status, headers, config) {
								if (data.success) {
									alert("解封成功");
									$scope.page.datas[index].isBan = false;
								} else {
									alert("解封失败");
								}
							}).error(
							function(response, status, headers, config) {
								alert("封禁失败");
							});
				};
			};
		});// managerUserCtr

// 转换权限显示样式的过滤器
managerAppCtrls.filter("permissionExplain", function() {
			return function(self, allPermission) {
				var permissions = self.split(",");
				var returnPermissions = [];

				for (var n = 0; n < permissions.length; n++) {
					returnPermissions.push(allPermission[permissions[n]]);
				}

				// 从当前域中获取到权限的信息
				return returnPermissions.join("，");
			};
		});

managerAppCtrls.controller('managerRoleCtr', function($scope, $http,
		$pageService) {
	$scope.isLoadingData = true;
	$scope.isCanPre = false;
	$scope.isCanNext = false;
	$scope.page = {
		currentPage : 1,
		pageShowCount : 6,
		datas : null,
		totalCount : null,
		totalPage : null
	}

	// 首次加载数据
	$pageService.loadingData($scope, $scope.page.currentPage,
			"../AdminRoleController/list");

	$scope.refresh = function() {
		$pageService.loadingData($scope, $scope.page.currentPage,
				"../AdminRoleController/list");
		$scope.loadAllPermission(true);
		alert("开始刷新数据");
	}

	$scope.changePage = function(isNext) {
		$pageService.changePage(isNext, $scope, "../AdminRoleController/list")
	};

	$scope.loadAllPermission = function(refresh) {
		if ($scope.allPermission == null || refresh) {
			// 先让其不为空，免得多次调用
			$scope.allPermission = [];
			$http.get("../AdminRoleController/allPermission").success(
					function(response) {
						$scope.allPermissionArray = response.allPermission;
						var temp = {};

						for (var n = 0; n < response.allPermission.length; n++) {
							temp[response.allPermission[n].name] = response.allPermission[n].explanation;
						}
						$scope.allPermission = temp;
					});
		}
	}

	$scope.loadAllPermission(false);

	$scope.add = function() {
		$scope.addMaterial = {};
		$scope.currentAddObj = {};
		$scope.addMaterial.allPermission = angular
				.copy($scope.allPermissionArray);

		$("#addDialog").modal("show");
	};

	$scope.addSubmit = function() {
		$scope.currentAddObj.permissions = [];

		for (var n = 0; n < $scope.addMaterial.allPermission.length; n++) {
			if ($scope.addMaterial.allPermission[n].isCheck) {
				$scope.currentAddObj.permissions
						.push($scope.addMaterial.allPermission[n].name);
			}
		}

		$scope.currentAddObj.permissions = $scope.currentAddObj.permissions
				.join(",");
		$http({
					method : "post",
					data : jQuery.param($scope.currentAddObj),
					url : "../AdminRoleController/add",
					headers : {
						"Content-Type" : "application/x-www-form-urlencoded"
					}
				}).success(function(response) {
					if (response.success) {
						alert("添加成功");
						$("#addDialog").modal("hide");
					}
				}).error(function(response) {
					alert("添加失败");
					$scope.error = {};
					$scope.error = response;
				});
	};

	$scope.delete = function(index) {
		if (confirm("你确定要删除吗？")) {
			var deleteId = $scope.page.datas[index].roleId;
			$http.post("../AdminRoleController/delete?id=" + deleteId).success(
					function(data, status, headers, config) {
						if (data.success) {
							alert("删除成功");
							$scope.page.datas.splice(index, 1);
							$scope.page.totalCount--;
						} else {
							alert("删除失败");
						}
					}).error(function(response, status, headers, config) {
						alert("删除失败");
					});
		}
	};

	$scope.edit = function(index) {
		$scope.currentUpdateObj = angular.copy($scope.page.datas[index]);
		$scope.currentUpdateObj.index = index;
		$scope.updateMaterial = {};
		$scope.updateMaterial.allPermission = angular
				.copy($scope.allPermissionArray);

		var tempP = $scope.currentUpdateObj.permissions.split(",");

		for (var n = 0; n < tempP.length; n++) {
			for (var m = 0; m < $scope.updateMaterial.allPermission.length; m++) {
				if ($scope.updateMaterial.allPermission[m].name == tempP[n]) {
					$scope.updateMaterial.allPermission[m].isCheck = true;
				}
			}
		}

		$("#updateDialog").modal("show");
	};

	$scope.editSubmit = function() {
		// 用于封装提交的信息
		var submitData = angular.copy($scope.currentUpdateObj);
		submitData.permissions = [];

		for (var m = 0; m < $scope.updateMaterial.allPermission.length; m++) {
			if ($scope.updateMaterial.allPermission[m].isCheck == true) {
				submitData.permissions
						.push($scope.updateMaterial.allPermission[m].name);
			}
		}

		submitData.permissions = submitData.permissions.join(",");
		$http({
					method : "post",
					data : jQuery.param(submitData),
					url : "../AdminRoleController/update",
					headers : {
						"Content-Type" : "application/x-www-form-urlencoded"
					}
				}).success(function(data, status, headers, config) {
					if (data.success) {
						alert("更新成功");
						// 更新界面的内容
						var index = submitData.index;
						$scope.page.datas[index] = submitData;
						$("#updateDialog").modal("hide");
					}
				}).error(function(response, status, headers, config) {
					$scope.error = {};
					$scope.error = response;
				});
	}

});// managerRoleCtr

managerAppCtrls.controller('managerManagerCtr', function($scope, $http,
				$pageService) {
			$scope.isLoadingData = true;
			$scope.isCanPre = false;
			$scope.isCanNext = false;
			$scope.page = {
				currentPage : 1,
				pageShowCount : 6,
				datas : null,
				totalCount : null,
				totalPage : null
			}

			// 首次加载数据
			$pageService.loadingData($scope, $scope.page.currentPage,
					"../AdminManagerController/list");

			$scope.refresh = function() {
				$pageService.loadingData($scope, $scope.page.currentPage,
						"../AdminManagerController/list");
				alert("开始刷新数据");
			}

			$scope.changePage = function(isNext) {
				$pageService.changePage(isNext, $scope,
						"../AdminManagerController/list")
			};

			$scope.add = function() {
				$scope.addMaterial = {};
				$scope.addMaterial.allRole = [];

				$http.get("../AdminRoleController/findAll").success(
						function(response) {
							$scope.addMaterial.allRole = response.allRole;
						});

				$("#addDialog").modal("show");
			};

			$scope.addSubmit = function() {
				$http({
							method : "post",
							data : jQuery.param($scope.currentAddObj),
							url : "../AdminManagerController/add",
							headers : {
								"Content-Type" : "application/x-www-form-urlencoded"
							}
						}).success(function(response) {
							if (response.success) {
								alert("添加成功");
								$("#addDialog").modal("hide");
							}
						}).error(function(response) {
							$scope.error = {};
							$scope.error = response;
						});

			};

			$scope.delete = function(index) {
				if (confirm("你确定要删除吗？")) {
					var deleteId = $scope.page.datas[index].managerId;
					$http.post("../AdminManagerController/delete?id="
							+ deleteId).success(
							function(data, status, headers, config) {
								if (data.success) {
									alert("删除成功");
									$scope.page.datas.splice(index, 1);
									$scope.page.totalCount--;
								} else {
									alert("删除失败");
								}
							}).error(
							function(response, status, headers, config) {
								alert("删除失败");
							});
				}
			};

			$scope.edit = function(index) {
				$scope.currentUpdateObj = angular
						.copy($scope.page.datas[index]);
				$scope.currentUpdateObj.index = index;
				$scope.currentUpdateObj.password = null;
				$scope.currentUpdateObj.roleId = -1;
				$scope.updateMaterial = {};
				$scope.updateMaterial.allRole = [];

				$http.get("../AdminRoleController/findAll").success(
						function(response) {
							$scope.updateMaterial.allRole = response.allRole;
						});

				$("#updateDialog").modal("show");
			};

			$scope.editSubmit = function() {
				// 用于封装提交的信息
				var submitData = angular.copy($scope.currentUpdateObj);

				$http({
							method : "post",
							data : jQuery.param(submitData),
							url : "../AdminManagerController/update",
							headers : {
								"Content-Type" : "application/x-www-form-urlencoded"
							}
						}).success(function(data, status, headers, config) {
							if (data.success) {
								alert("更新成功");
								// 更新界面的内容
								var index = submitData.index;
								$scope.page.datas[index] = submitData;
								$("#updateDialog").modal("hide");
							}
						}).error(function(response, status, headers, config) {
							$scope.error = {};
							$scope.error = response;
						});
			}

		});// managerManagerCtr
