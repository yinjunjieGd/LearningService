-- 初级会计实务（course_id=4）知识点prerequisiteId字段更新脚本
-- 直接在数据库中执行这些SQL语句，可以手动更新course_id=4的知识点的prerequisiteId字段

-- 1. 首先备份当前的知识点表（可选，建议执行）
-- CREATE TABLE knowledge_points_backup AS SELECT * FROM knowledge_points;

-- 2. 恢复初级会计实务第一章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 33 WHERE point_id = 34 AND course_id = 4; -- 会计职能 依赖 会计概念与目标
UPDATE knowledge_points SET prerequisite_id = 34 WHERE point_id = 35 AND course_id = 4; -- 会计基本假设与会计基础 依赖 会计职能
UPDATE knowledge_points SET prerequisite_id = 35 WHERE point_id = 36 AND course_id = 4; -- 会计信息质量要求 依赖 会计基本假设与会计基础
UPDATE knowledge_points SET prerequisite_id = 36 WHERE point_id = 37 AND course_id = 4; -- 会计要素及其确认与计量 依赖 会计信息质量要求
UPDATE knowledge_points SET prerequisite_id = 37 WHERE point_id = 38 AND course_id = 4; -- 会计科目与账户 依赖 会计要素及其确认与计量
UPDATE knowledge_points SET prerequisite_id = 38 WHERE point_id = 39 AND course_id = 4; -- 会计凭证与账簿 依赖 会计科目与账户
UPDATE knowledge_points SET prerequisite_id = 39 WHERE point_id = 40 AND course_id = 4; -- 财产清查 依赖 会计凭证与账簿
UPDATE knowledge_points SET prerequisite_id = 40 WHERE point_id = 41 AND course_id = 4; -- 财务报告 依赖 财产清查

-- 3. 恢复初级会计实务第二章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 42 WHERE point_id = 43 AND course_id = 4; -- 应收及预付款项 依赖 货币资金
UPDATE knowledge_points SET prerequisite_id = 43 WHERE point_id = 44 AND course_id = 4; -- 交易性金融资产 依赖 应收及预付款项
UPDATE knowledge_points SET prerequisite_id = 44 WHERE point_id = 45 AND course_id = 4; -- 存货 依赖 交易性金融资产
UPDATE knowledge_points SET prerequisite_id = 45 WHERE point_id = 46 AND course_id = 4; -- 固定资产 依赖 存货
UPDATE knowledge_points SET prerequisite_id = 46 WHERE point_id = 47 AND course_id = 4; -- 无形资产和长期待摊费用 依赖 固定资产

-- 4. 恢复初级会计实务第三章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 48 WHERE point_id = 49 AND course_id = 4; -- 应付及预收款项 依赖 短期借款
UPDATE knowledge_points SET prerequisite_id = 49 WHERE point_id = 50 AND course_id = 4; -- 应付职工薪酬 依赖 应付及预收款项
UPDATE knowledge_points SET prerequisite_id = 50 WHERE point_id = 51 AND course_id = 4; -- 应交税费 依赖 应付职工薪酬
UPDATE knowledge_points SET prerequisite_id = 51 WHERE point_id = 52 AND course_id = 4; -- 长期借款 依赖 应交税费

-- 5. 恢复初级会计实务第四章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 53 WHERE point_id = 54 AND course_id = 4; -- 资本公积 依赖 实收资本或股本
UPDATE knowledge_points SET prerequisite_id = 54 WHERE point_id = 55 AND course_id = 4; -- 留存收益 依赖 资本公积

-- 6. 恢复初级会计实务第五章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 56 WHERE point_id = 57 AND course_id = 4; -- 费用 依赖 收入
UPDATE knowledge_points SET prerequisite_id = 57 WHERE point_id = 58 AND course_id = 4; -- 利润 依赖 费用

-- 7. 恢复初级会计实务第六章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 59 WHERE point_id = 60 AND course_id = 4; -- 利润表 依赖 资产负债表
UPDATE knowledge_points SET prerequisite_id = 60 WHERE point_id = 61 AND course_id = 4; -- 现金流量表 依赖 利润表
UPDATE knowledge_points SET prerequisite_id = 61 WHERE point_id = 62 AND course_id = 4; -- 所有者权益变动表 依赖 现金流量表

-- 8. 恢复初级会计实务第七章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 63 WHERE point_id = 64 AND course_id = 4; -- 产品成本核算的要求和一般程序 依赖 管理会计概述
UPDATE knowledge_points SET prerequisite_id = 64 WHERE point_id = 65 AND course_id = 4; -- 产品成本的归集和分配 依赖 产品成本核算的要求和一般程序
UPDATE knowledge_points SET prerequisite_id = 65 WHERE point_id = 66 AND course_id = 4; -- 产品成本计算方法 依赖 产品成本的归集和分配

-- 9. 恢复初级会计实务第八章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 67 WHERE point_id = 68 AND course_id = 4; -- 政府单位会计核算 依赖 政府会计概述

-- 10. 验证更新结果
SELECT COUNT(*) AS total_accounting_points,
       SUM(CASE WHEN prerequisite_id IS NOT NULL THEN 1 ELSE 0 END) AS points_with_prerequisite,
       SUM(CASE WHEN prerequisite_id IS NULL THEN 1 ELSE 0 END) AS points_without_prerequisite
FROM knowledge_points
WHERE course_id = 4;

-- 11. 查看更新详情（可选）
-- SELECT point_id, title, prerequisite_id, 
--        (SELECT title FROM knowledge_points WHERE point_id = kp.prerequisite_id) AS prerequisite_title
-- FROM knowledge_points kp
-- WHERE course_id = 4
-- ORDER BY point_id;