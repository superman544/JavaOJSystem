
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `announcement_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告编号',
  `announcement_title` varchar(30) DEFAULT NULL COMMENT '公告标题',
  `announcement_introduction` varchar(250) DEFAULT NULL COMMENT '公告简介',
  `announcement_content` text COMMENT '公告内容',
  `announcement_create_manager_id` int(11) DEFAULT NULL COMMENT '公告创建者编号',
  `announcement_create_time` datetime DEFAULT NULL COMMENT '公告创建时间',
  `announcement_publish_time` datetime DEFAULT NULL COMMENT '公告发布时间',
  `is_publish` bit(1) DEFAULT b'0' COMMENT '是否发布',
  PRIMARY KEY (`announcement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of announcement
-- ----------------------------

-- ----------------------------
-- Table structure for competition
-- ----------------------------
DROP TABLE IF EXISTS `competition`;
CREATE TABLE `competition` (
  `competition_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '比赛编号',
  `competition_name` varchar(30) DEFAULT NULL COMMENT '比赛标题',
  `competition_description` varchar(400) DEFAULT NULL COMMENT '比赛描述',
  `competition_apply_begin_time` datetime DEFAULT NULL,
  `competition_apply_end_time` datetime DEFAULT NULL,
  `competition_begin_time` datetime DEFAULT NULL COMMENT '比赛开始时间',
  `competition_end_time` datetime DEFAULT NULL COMMENT '比赛结束时间',
  `competition_players_count` int(11) DEFAULT NULL COMMENT '比赛选手人数（对应多少个账号）',
  `is_close` bit(1) DEFAULT b'0' COMMENT '是否已经关闭比赛（关闭之后，将会进行资源回收）',
  `is_publish` bit(1) DEFAULT b'0' COMMENT '是否已经发布',
  `is_can_declare` bit(1) DEFAULT b'0' COMMENT '该比赛是否允许申报参加（默认为不允许）',
  `is_judge` bit(1) DEFAULT b'0' COMMENT '是否已经对本次比赛提交的代码，进行过判题',
  `competition_problem_ids` varchar(400) DEFAULT NULL COMMENT '题目编号信息用,分割',
  `competition_content_root_path` varchar(1023) DEFAULT NULL COMMENT '比赛内容根目录（比赛代码，报表等）',
  PRIMARY KEY (`competition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of competition
-- ----------------------------

-- ----------------------------
-- Table structure for competition_account
-- ----------------------------
DROP TABLE IF EXISTS `competition_account`;
CREATE TABLE `competition_account` (
  `competition_account_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '比赛账号编号',
  `login_account` varchar(32) DEFAULT NULL COMMENT '登陆账号',
  `login_password` varchar(32) DEFAULT NULL COMMENT '登陆账号对应的密码',
  `competition_id` int(11) DEFAULT NULL COMMENT '该账号对应于哪一个比赛',
  `account_remark` varchar(300) DEFAULT NULL COMMENT '账号备注信息，可用于记录选手一些基本信息',
  `account_score` varchar(20000) DEFAULT NULL COMMENT '这个账号该次比赛信息（JSON格式数据：每题得分，运行时间，内存消耗，提交的代码语言）',
  `account_code_root_path` varchar(1000) DEFAULT NULL COMMENT '这个账号该次比赛提交的代码保存的根目录',
  `is_use` bit(1) DEFAULT b'0' COMMENT '是否已经使用了',
  PRIMARY KEY (`competition_account_id`),
  UNIQUE KEY `unique_competition_account` (`login_account`,`competition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of competition_account
-- ----------------------------

-- ----------------------------
-- Table structure for competition_application
-- ----------------------------
DROP TABLE IF EXISTS `competition_application`;
CREATE TABLE `competition_application` (
  `competition_application_id` varchar(36) NOT NULL COMMENT '报名编号（UUID生成36位）',
  `email` varchar(36) NOT NULL COMMENT '用于通知结果的邮箱',
  `phone` varchar(36) NOT NULL COMMENT '用于通知结果的手机号码',
  `application_people_count` int(11) NOT NULL COMMENT '报名人数',
  `application_summary` varchar(30) DEFAULT NULL COMMENT '报名申请摘要',
  `application_content` varchar(3000) DEFAULT NULL COMMENT '报名申请信息',
  `competition_id` int(11) NOT NULL COMMENT '报名申请的比赛编号',
  `level` int(11) DEFAULT '1' COMMENT '处理等级',
  `is_have_send_email` bit(1) DEFAULT b'0' COMMENT '是否已经发送了通知邮件',
  `is_have_handle` bit(1) DEFAULT b'0' COMMENT '是否已经处理了',
  `competition_account_id` int(11) DEFAULT NULL COMMENT '分配给这个申请的比赛账号',
  PRIMARY KEY (`competition_application_id`),
  UNIQUE KEY `unique_competition_account` (`email`,`competition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of competition_application
-- ----------------------------

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager` (
  `manager_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员编号',
  `account` varchar(20) NOT NULL COMMENT '账号',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `nickname` varchar(20) DEFAULT NULL COMMENT '昵称',
  `role_id` int(11) DEFAULT NULL COMMENT '角色编号',
  PRIMARY KEY (`manager_id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES ('1', '123123', '4297F44B13955235245B2497399D7A93', '创始人', '1');

-- ----------------------------
-- Table structure for problem
-- ----------------------------
DROP TABLE IF EXISTS `problem`;
CREATE TABLE `problem` (
  `problem_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '题目编号',
  `problem_type_id` int(11) DEFAULT NULL COMMENT '题目分类编号',
  `problem_name` varchar(50) DEFAULT NULL COMMENT '题目名字',
  `input_file_root_path` varchar(255) DEFAULT NULL COMMENT '题目标准输入根目录',
  `output_file_root_path` varchar(255) DEFAULT NULL COMMENT '题目标准输出根目录',
  `problem_label` varchar(60) DEFAULT NULL COMMENT '题目标签（JSON格式数据，用，分割）',
  `time_limit` int(11) DEFAULT NULL COMMENT '题目时间限制',
  `memory_limit` int(11) DEFAULT NULL COMMENT '题目内存限制',
  `problem_creator_id` int(11) DEFAULT NULL COMMENT '题目创建者编号',
  `problem_difficulty` int(11) DEFAULT '1' COMMENT '题目难度',
  `problem_value` int(11) DEFAULT '1' COMMENT '题目价值',
  `problem_version` int(11) DEFAULT '1' COMMENT '题目版本号。每一次更新题目相关考核信息时，版本号都会加1',
  `problem_content` text COMMENT '题目内容',
  `is_publish` bit(1) DEFAULT b'0' COMMENT '是否发布',
  `all_right_people_ids` text COMMENT '所有答对的用户ID编号集',
  `total_submit_count` int(11) DEFAULT '0' COMMENT '总提交人数',
  `total_right_count` int(11) DEFAULT '0' COMMENT '总答对人数',
  PRIMARY KEY (`problem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of problem
-- ----------------------------

-- ----------------------------
-- Table structure for problem_type
-- ----------------------------
DROP TABLE IF EXISTS `problem_type`;
CREATE TABLE `problem_type` (
  `problem_type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '题目分类编号',
  `problem_type_name` varchar(20) DEFAULT NULL COMMENT '题目分类名字',
  `problem_type_description` varchar(150) DEFAULT NULL COMMENT '题目分类描述',
  PRIMARY KEY (`problem_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of problem_type
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `role_name` varchar(20) NOT NULL COMMENT '角色名称',
  `permissions` varchar(1024) DEFAULT NULL COMMENT '角色权限',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '超级管理员', 'ManagerAdd,ManagerDelete,ManagerUpdate,ManagerFind,RoleAdd,RoleDelete,RoleUpdate,RoleFind,UserFind,UserBan,AnnouncementAdd,AnnouncementDelete,AnnouncementUpdate,AnnouncementFind,AnnouncementPublish,CompetitionAdd,CompetitionDelete,CompetitionUpdate,CompetitionFind,CompetitionPublish,CompetitionClose,CompetitionReport,CompetitionJudge,CompetitionApplicationUpdate,CompetitionApplicationNotify,CompetitionApplicationFind,CompetitionApplicationDispatch,CompetitionApplicationReport,CompetitionAccountUpdate,CompetitionAccountFind,ProblemAdd,ProblemDelete,ProblemUpdate,ProblemFind,ProblemStandardFile,ProblemTypeAdd,ProblemTypeDelete,ProblemTypeUpdate,ProblemTypeFind,SandboxWatch,SandboxOpen,SandboxClose');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `account` varchar(20) NOT NULL COMMENT '账号',
  `password` char(32) NOT NULL COMMENT '密码',
  `nickname` varchar(20) DEFAULT NULL COMMENT '昵称',
  `source_file_root_path` varchar(255) DEFAULT NULL COMMENT '保存代码源文件的根目录',
  `right_problem_count` int(11) DEFAULT '0' COMMENT '做对的题目数量',
  `have_done_problem` int(11) DEFAULT '0' COMMENT '一共做过的题目数量',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `submit_record_table_name` varchar(150) DEFAULT NULL COMMENT '用于记录该用户的提交记录表的名称',
  `is_ban` bit(1) DEFAULT b'0' COMMENT '是否被封禁，默认值为false',
  `total_solve_value` int(11) DEFAULT '0' COMMENT '一共解决题目的总价值',
  `last_submit_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
