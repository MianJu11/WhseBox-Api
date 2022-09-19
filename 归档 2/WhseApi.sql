
DROP TABLE IF EXISTS `typecho_cash_back_config`;
CREATE TABLE `typecho_cash_back_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `level_gap` int NULL DEFAULT NULL COMMENT '等级差',
  `ratio` int NULL DEFAULT NULL COMMENT '百分比返利',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `level_gap`(`level_gap`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `typecho_standard_fee`;
CREATE TABLE `typecho_standard_fee`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型：1-置顶；2-轮播；3-火急；4-刷新次数',
  `times` int NULL DEFAULT NULL COMMENT '天数/次数',
  `price` int NULL DEFAULT NULL COMMENT '价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `typecho_contents_renovate_task`;
CREATE TABLE `typecho_contents_renovate_task`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `content_id` int NULL DEFAULT NULL COMMENT '文章ID',
  `renovate_time` datetime NULL DEFAULT NULL COMMENT '刷新开始时间',
  `interval_time` int NULL DEFAULT NULL COMMENT '间隔时间（分钟）',
  `renovate_times` int NULL DEFAULT NULL COMMENT '刷新次数',
  `residue_times` int NULL DEFAULT NULL COMMENT '剩余刷新次数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '1-生效；0-失效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `typecho_contents_task`;
CREATE TABLE `typecho_contents_task`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `content_id` int NULL DEFAULT NULL COMMENT '文章ID',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '1-置顶；2-轮播；3-火急',
  `end_time` bigint NULL DEFAULT NULL COMMENT '终止时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '1-生效；0-失效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE typecho_users ADD renovate int;
ALTER TABLE typecho_users ADD inviterId int;
ALTER TABLE typecho_users ADD userLevel char;

ALTER TABLE typecho_contents ADD increaseViews int;
ALTER TABLE typecho_contents ADD carousel char;
ALTER TABLE typecho_contents ADD burning char;
ALTER TABLE typecho_contents ADD sticky char;
ALTER TABLE typecho_apiconfig ADD freeRenovateTimes int;
UPDATE typecho_apiconfig SET freeRenovateTimes = 10;
ALTER TABLE typecho_contents MODIFY COLUMN modified BIGINT