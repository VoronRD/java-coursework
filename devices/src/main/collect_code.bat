@echo off
setlocal enabledelayedexpansion

set OUTPUT_FILE=code.txt
set SKIP_DIRS=target .git .idea node_modules dist .mvn logs bundles frontend

:: Очищаем файл
type nul > "%OUTPUT_FILE%"

echo Сканирование файлов...

:: Функция добавления файла
call :addFile "pom.xml"

:: Сканируем все сервисы
call :scanDir "java"
call :scanDir "resources"

echo. >> "%OUTPUT_FILE%"
echo ============================================ >> "%OUTPUT_FILE%"
echo КОНЕЦ СБОРКИ >> "%OUTPUT_FILE%"
echo ============================================ >> "%OUTPUT_FILE%"

echo Готово! Код сохранён в файл: %OUTPUT_FILE%
pause
exit /b

:addFile
if exist "%~1" (
    echo. >> "%OUTPUT_FILE%"
    echo ================== FILE: %~1 ================== >> "%OUTPUT_FILE%"
    echo. >> "%OUTPUT_FILE%"
    type "%~1" >> "%OUTPUT_FILE%"
    echo. >> "%OUTPUT_FILE%"
)
exit /b

:scanDir
if exist "%~1\" (
    echo Сканирование папки: %~1
    for /R "%~1" %%f in (*.java *.xml *.yml *.yaml *.properties *.sql *.json *.js *.ts *.html *.css *.sh *.md *.txt .gitignore Dockerfile) do (
        set skip=0
        for %%d in (%SKIP_DIRS%) do (
            echo %%~f | findstr /i /c:"\%%d\" >nul && set skip=1
        )
        if !skip!==0 call :addFile "%%f"
    )
)
exit /b