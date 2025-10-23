-- 初级会计实务知识点数据插入SQL脚本
-- 用于插入course_id=4的知识点数据到knowledge_points表中

-- 先检查是否已存在course_id=4的知识点数据
SELECT COUNT(*) FROM knowledge_points WHERE course_id = 4;

-- 如果需要清空现有数据（可选），取消下面这行注释
-- DELETE FROM knowledge_points WHERE course_id = 4;

-- 插入第一章：会计概述的知识点
INSERT INTO knowledge_points (point_id, course_id, title, description, prerequisite_id)
VALUES 
  (33, 4, '会计概念与目标', '理解会计的基本概念、特征和目标', NULL),
  (34, 4, '会计职能', '掌握会计的基本职能和拓展职能', 33),
  (35, 4, '会计基本假设与会计基础', '掌握会计基本假设和会计基础', 34),
  (36, 4, '会计信息质量要求', '掌握会计信息质量要求的内容', 35),
  (37, 4, '会计要素及其确认与计量', '掌握会计要素的确认与计量原则', 36),
  (38, 4, '会计科目与账户', '掌握会计科目与账户的分类和关系', 37),
  (39, 4, '会计凭证与账簿', '掌握会计凭证的种类和账簿的分类', 38),
  (40, 4, '财产清查', '掌握财产清查的方法和处理', 39),
  (41, 4, '财务报告', '理解财务报告的概念和组成', 40);

-- 插入第二章：资产的知识点
INSERT INTO knowledge_points (point_id, course_id, title, description, prerequisite_id)
VALUES 
  (42, 4, '货币资金', '掌握库存现金、银行存款和其他货币资金的核算', NULL),
  (43, 4, '应收及预付款项', '掌握应收票据、应收账款、预付账款和其他应收款的核算', 42),
  (44, 4, '交易性金融资产', '掌握交易性金融资产的取得、持有和出售的核算', 43),
  (45, 4, '存货', '掌握存货的确认、计量和清查', 44),
  (46, 4, '固定资产', '掌握固定资产的确认、计量、折旧和处置', 45),
  (47, 4, '无形资产和长期待摊费用', '掌握无形资产的确认、计量和摊销', 46);

-- 插入第三章：负债的知识点
INSERT INTO knowledge_points (point_id, course_id, title, description, prerequisite_id)
VALUES 
  (48, 4, '短期借款', '掌握短期借款的核算', NULL),
  (49, 4, '应付及预收款项', '掌握应付票据、应付账款、预收账款和其他应付款的核算', 48),
  (50, 4, '应付职工薪酬', '掌握应付职工薪酬的内容和核算', 49),
  (51, 4, '应交税费', '掌握增值税、消费税等主要税种的核算', 50),
  (52, 4, '长期借款', '掌握长期借款的核算', 51);

-- 插入第四章：所有者权益的知识点
INSERT INTO knowledge_points (point_id, course_id, title, description, prerequisite_id)
VALUES 
  (53, 4, '实收资本或股本', '掌握实收资本或股本的核算', NULL),
  (54, 4, '资本公积', '掌握资本公积的来源和核算', 53),
  (55, 4, '留存收益', '掌握盈余公积和未分配利润的核算', 54);

-- 插入第五章：收入、费用和利润的知识点
INSERT INTO knowledge_points (point_id, course_id, title, description, prerequisite_id)
VALUES 
  (56, 4, '收入', '掌握销售商品收入、提供劳务收入的核算', NULL),
  (57, 4, '费用', '掌握营业成本、税金及附加和期间费用的核算', 56),
  (58, 4, '利润', '掌握利润的构成和结转', 57);

-- 插入第六章：财务报表的知识点
INSERT INTO knowledge_points (point_id, course_id, title, description, prerequisite_id)
VALUES 
  (59, 4, '资产负债表', '掌握资产负债表的编制方法', NULL),
  (60, 4, '利润表', '掌握利润表的编制方法', 59),
  (61, 4, '现金流量表', '理解现金流量表的编制基础和方法', 60),
  (62, 4, '所有者权益变动表', '理解所有者权益变动表的内容和结构', 61);

-- 插入第七章：管理会计基础的知识点
INSERT INTO knowledge_points (point_id, course_id, title, description, prerequisite_id)
VALUES 
  (63, 4, '管理会计概述', '理解管理会计的概念和目标', NULL),
  (64, 4, '产品成本核算的要求和一般程序', '掌握产品成本核算的要求和一般程序', 63),
  (65, 4, '产品成本的归集和分配', '掌握产品成本的归集和分配方法', 64),
  (66, 4, '产品成本计算方法', '掌握品种法、分批法和分步法的特点和应用', 65);

-- 插入第八章：政府会计基础的知识点
INSERT INTO knowledge_points (point_id, course_id, title, description, prerequisite_id)
VALUES 
  (67, 4, '政府会计概述', '理解政府会计的概念和特点', NULL),
  (68, 4, '政府单位会计核算', '掌握政府单位会计的基本业务核算', 67);

-- 验证插入结果
SELECT COUNT(*) AS inserted_count FROM knowledge_points WHERE course_id = 4;
SELECT * FROM knowledge_points WHERE course_id = 4 ORDER BY point_id;

-- 提示：如果需要更安全地执行插入（避免主键冲突），可以使用INSERT IGNORE语句
-- 例如：INSERT IGNORE INTO knowledge_points (point_id, course_id, title, description, prerequisite_id) VALUES (...);

-- 或者使用REPLACE语句（会替换已存在的数据）
-- 例如：REPLACE INTO knowledge_points (point_id, course_id, title, description, prerequisite_id) VALUES (...);