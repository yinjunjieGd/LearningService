@echo off

REM 初级会计实务知识点数据插入批处理文件
REM 双击此文件可直接运行数据插入操作

echo 正在准备插入初级会计实务知识点数据...
echo 请确保已正确配置数据库连接信息

echo.
echo 开始编译项目...
mvn clean compile

if %errorlevel% neq 0 (
    echo 编译失败，请检查项目配置和依赖
echo 按任意键退出...
pause >nul
exit /b 1
)

echo.
echo 编译成功，开始执行数据插入...
mvn exec:java -Dexec.mainClass="com.gaodun.learningservice.utils.AccountingKnowledgePointsDataInserter"

if %errorlevel% neq 0 (
    echo 数据插入失败，请检查数据库连接和权限
echo 按任意键退出...
pause >nul
exit /b 1
)

echo.
echo 数据插入操作已完成！
echo 请查看上面的输出信息确认插入结果
echo 按任意键退出...
pause >nul