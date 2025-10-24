-- 查看user_learning_progress表结构
SHOW COLUMNS FROM user_learning_progress;

-- 查看最近的记录
SELECT id, user_id, course_id, 
       CASE 
           WHEN knowledge_pic IS NULL THEN 'NULL'
           WHEN knowledge_pic = '' THEN 'EMPTY'
           ELSE CONCAT('LENGTH: ', LENGTH(knowledge_pic))
       END as knowledge_pic_status,
       create_time, update_time
FROM user_learning_progress
ORDER BY update_time DESC
LIMIT 5;
