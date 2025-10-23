@echo off
rem ==================================================================
rem 初级会计实务知识点题目生成与插入批处理脚本
rem 用于自动生成并插入course_id=4的知识点题目数据到questions表
rem 作者：AI Assistant
rem ==================================================================

cls

echo ==================================================================
echo                初级会计实务知识点题目生成器
 echo ==================================================================
echo 此脚本将自动生成并插入初级会计实务（course_id=4）的知识点题目数据
echo 到questions表中，请确保已经成功插入了知识点数据
echo 正在准备执行...
echo ==================================================================

echo 1. 首先清理并编译项目
call mvn clean compile
if %errorlevel% neq 0 (
echo 编译失败！请检查项目代码是否存在错误
echo 按任意键退出...
pause >nul
exit /b 1
)

echo 
echo 2. 开始生成并插入初级会计实务知识点题目数据
echo ======================================================
echo 正在执行AccountingKnowledgePointsQuestionsInserter...
echo ======================================================
call mvn exec:java -Dexec.mainClass="com.gaodun.learningservice.utils.AccountingKnowledgePointsQuestionsInserter"
if %errorlevel% neq 0 (
echo 题目生成与插入失败！请查看上面的错误信息
echo 按任意键退出...
pause >nul
exit /b 1
)

echo 
echo ==================================================================
echo 初级会计实务知识点题目生成与插入操作已完成！
echo 若要查看生成的题目数据，请直接查询数据库中的questions表
echo 或运行项目中的测试方法进行验证。
echo 按任意键退出...
echo ==================================================================
pause >nul