-- 知识点表prerequisiteId字段恢复脚本
-- 直接在数据库中执行这些SQL语句，可以手动恢复被清空的prerequisiteId字段

-- 1. 首先备份当前的知识点表（可选，建议执行）
-- CREATE TABLE knowledge_points_backup AS SELECT * FROM knowledge_points;

-- 2. 恢复微积分部分的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 1 WHERE point_id = 2; -- 函数的连续性 依赖 函数的极限
UPDATE knowledge_points SET prerequisite_id = 2 WHERE point_id = 3; -- 导数与微分 依赖 函数的连续性
UPDATE knowledge_points SET prerequisite_id = 3 WHERE point_id = 4; -- 微分中值定理 依赖 导数与微分
UPDATE knowledge_points SET prerequisite_id = 3 WHERE point_id = 5; -- 不定积分 依赖 导数与微分
UPDATE knowledge_points SET prerequisite_id = 5 WHERE point_id = 6; -- 定积分 依赖 不定积分
UPDATE knowledge_points SET prerequisite_id = 6 WHERE point_id = 7; -- 定积分的应用 依赖 定积分
UPDATE knowledge_points SET prerequisite_id = 6 WHERE point_id = 8; -- 反常积分 依赖 定积分

-- 3. 恢复多元函数微积分学的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 2 WHERE point_id = 9;  -- 多元函数的极限与连续 依赖 函数的连续性
UPDATE knowledge_points SET prerequisite_id = 3 WHERE point_id = 10; -- 偏导数与全微分 依赖 导数与微分
UPDATE knowledge_points SET prerequisite_id = 10 WHERE point_id = 11; -- 多元函数的极值 依赖 偏导数与全微分
UPDATE knowledge_points SET prerequisite_id = 6 WHERE point_id = 12; -- 重积分 依赖 定积分
UPDATE knowledge_points SET prerequisite_id = 12 WHERE point_id = 13; -- 曲线积分与曲面积分 依赖 重积分

-- 4. 恢复无穷级数的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 1 WHERE point_id = 14; -- 常数项级数 依赖 函数的极限
UPDATE knowledge_points SET prerequisite_id = 14 WHERE point_id = 15; -- 幂级数 依赖 常数项级数
UPDATE knowledge_points SET prerequisite_id = 14 WHERE point_id = 16; -- 傅里叶级数 依赖 常数项级数

-- 5. 恢复常微分方程的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 3 WHERE point_id = 17; -- 微分方程的基本概念 依赖 导数与微分
UPDATE knowledge_points SET prerequisite_id = 17 WHERE point_id = 18; -- 一阶微分方程 依赖 微分方程的基本概念
UPDATE knowledge_points SET prerequisite_id = 17 WHERE point_id = 19; -- 高阶线性微分方程 依赖 微分方程的基本概念
UPDATE knowledge_points SET prerequisite_id = 18 WHERE point_id = 20; -- 微分方程的应用 依赖 一阶微分方程

-- 6. 恢复线性代数部分的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 21 WHERE point_id = 22; -- 矩阵 依赖 行列式
UPDATE knowledge_points SET prerequisite_id = 22 WHERE point_id = 23; -- 向量组的线性相关性 依赖 矩阵
UPDATE knowledge_points SET prerequisite_id = 23 WHERE point_id = 24; -- 线性方程组 依赖 向量组的线性相关性
UPDATE knowledge_points SET prerequisite_id = 22 WHERE point_id = 25; -- 特征值与特征向量 依赖 矩阵
UPDATE knowledge_points SET prerequisite_id = 25 WHERE point_id = 26; -- 二次型 依赖 特征值与特征向量

-- 7. 恢复概率论与数理统计部分的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 27 WHERE point_id = 28; -- 条件概率与独立性 依赖 随机事件及其概率
UPDATE knowledge_points SET prerequisite_id = 27 WHERE point_id = 29; -- 随机变量及其分布 依赖 随机事件及其概率
UPDATE knowledge_points SET prerequisite_id = 29 WHERE point_id = 30; -- 多维随机变量及其分布 依赖 随机变量及其分布
UPDATE knowledge_points SET prerequisite_id = 29 WHERE point_id = 31; -- 随机变量的数字特征 依赖 随机变量及其分布
UPDATE knowledge_points SET prerequisite_id = 31 WHERE point_id = 32; -- 大数定律与中心极限定理 依赖 随机变量的数字特征

-- 8. 恢复初级会计实务第一章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 33 WHERE point_id = 34; -- 会计职能 依赖 会计概念与目标
UPDATE knowledge_points SET prerequisite_id = 34 WHERE point_id = 35; -- 会计基本假设与会计基础 依赖 会计职能
UPDATE knowledge_points SET prerequisite_id = 35 WHERE point_id = 36; -- 会计信息质量要求 依赖 会计基本假设与会计基础
UPDATE knowledge_points SET prerequisite_id = 36 WHERE point_id = 37; -- 会计要素及其确认与计量 依赖 会计信息质量要求
UPDATE knowledge_points SET prerequisite_id = 37 WHERE point_id = 38; -- 会计科目与账户 依赖 会计要素及其确认与计量
UPDATE knowledge_points SET prerequisite_id = 38 WHERE point_id = 39; -- 会计凭证与账簿 依赖 会计科目与账户
UPDATE knowledge_points SET prerequisite_id = 39 WHERE point_id = 40; -- 财产清查 依赖 会计凭证与账簿
UPDATE knowledge_points SET prerequisite_id = 40 WHERE point_id = 41; -- 财务报告 依赖 财产清查

-- 9. 恢复初级会计实务第二章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 42 WHERE point_id = 43; -- 应收及预付款项 依赖 货币资金
UPDATE knowledge_points SET prerequisite_id = 43 WHERE point_id = 44; -- 交易性金融资产 依赖 应收及预付款项
UPDATE knowledge_points SET prerequisite_id = 44 WHERE point_id = 45; -- 存货 依赖 交易性金融资产
UPDATE knowledge_points SET prerequisite_id = 45 WHERE point_id = 46; -- 固定资产 依赖 存货
UPDATE knowledge_points SET prerequisite_id = 46 WHERE point_id = 47; -- 无形资产和长期待摊费用 依赖 固定资产

-- 10. 恢复初级会计实务第三章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 48 WHERE point_id = 49; -- 应付及预收款项 依赖 短期借款
UPDATE knowledge_points SET prerequisite_id = 49 WHERE point_id = 50; -- 应付职工薪酬 依赖 应付及预收款项
UPDATE knowledge_points SET prerequisite_id = 50 WHERE point_id = 51; -- 应交税费 依赖 应付职工薪酬
UPDATE knowledge_points SET prerequisite_id = 51 WHERE point_id = 52; -- 长期借款 依赖 应交税费

-- 11. 恢复初级会计实务第四章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 53 WHERE point_id = 54; -- 资本公积 依赖 实收资本或股本
UPDATE knowledge_points SET prerequisite_id = 54 WHERE point_id = 55; -- 留存收益 依赖 资本公积

-- 12. 恢复初级会计实务第五章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 56 WHERE point_id = 57; -- 费用 依赖 收入
UPDATE knowledge_points SET prerequisite_id = 57 WHERE point_id = 58; -- 利润 依赖 费用

-- 13. 恢复初级会计实务第六章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 59 WHERE point_id = 60; -- 利润表 依赖 资产负债表
UPDATE knowledge_points SET prerequisite_id = 60 WHERE point_id = 61; -- 现金流量表 依赖 利润表
UPDATE knowledge_points SET prerequisite_id = 61 WHERE point_id = 62; -- 所有者权益变动表 依赖 现金流量表

-- 14. 恢复初级会计实务第七章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 63 WHERE point_id = 64; -- 产品成本核算的要求和一般程序 依赖 管理会计概述
UPDATE knowledge_points SET prerequisite_id = 64 WHERE point_id = 65; -- 产品成本的归集和分配 依赖 产品成本核算的要求和一般程序
UPDATE knowledge_points SET prerequisite_id = 65 WHERE point_id = 66; -- 产品成本计算方法 依赖 产品成本的归集和分配

-- 15. 恢复初级会计实务第八章的知识点依赖关系
UPDATE knowledge_points SET prerequisite_id = 67 WHERE point_id = 68; -- 政府单位会计核算 依赖 政府会计概述

-- 16. 验证恢复结果
SELECT COUNT(*) AS total_points,
       SUM(CASE WHEN prerequisite_id IS NOT NULL THEN 1 ELSE 0 END) AS points_with_prerequisite,
       SUM(CASE WHEN prerequisite_id IS NULL THEN 1 ELSE 0 END) AS points_without_prerequisite
FROM knowledge_points;

-- 17. 查看恢复详情（可选）
-- SELECT point_id, title, prerequisite_id, 
--        (SELECT title FROM knowledge_points WHERE point_id = kp.prerequisite_id) AS prerequisite_title
-- FROM knowledge_points kp
-- WHERE prerequisite_id IS NOT NULL;

-- 执行完成后，可以删除备份表（如果创建了）
-- DROP TABLE IF EXISTS knowledge_points_backup;

-- 提示：执行此脚本后，知识点表的prerequisiteId字段将恢复为正确的依赖关系。