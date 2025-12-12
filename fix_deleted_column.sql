-- 为 general_jobAdviceArticle 表添加 deleted 字段（如果不存在）
-- 这个字段用于 MyBatis-Plus 的逻辑删除功能

ALTER TABLE `general_jobAdviceArticle` 
ADD COLUMN IF NOT EXISTS `deleted` int(1) DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除';

-- 查询表结构确认字段已添加
DESCRIBE `general_jobAdviceArticle`;