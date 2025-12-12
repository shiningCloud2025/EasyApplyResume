-- 为 general_jobAdviceArticle 表添加 deleted 字段
-- 用于支持逻辑删除功能

ALTER TABLE general_jobAdviceArticle 
ADD COLUMN deleted INT DEFAULT 0 NOT NULL COMMENT '逻辑删除标识:0-未删除,1-已删除';

-- 为 deleted 字段添加索引，提高查询效率
CREATE INDEX idx_deleted ON general_jobAdviceArticle(deleted);

-- 验证字段是否添加成功
-- SELECT * FROM information_schema.COLUMNS 
-- WHERE TABLE_NAME = 'general_jobAdviceArticle' AND COLUMN_NAME = 'deleted';

