@echo off
REM === НАСТРОЙКИ ===
set IMAGE_NAME=auth
set IMAGE_TAG=latest

REM === СБОРКА ОБРАЗА ===
docker build -t %IMAGE_NAME%:latest .

echo ============================================
echo УСПЕХ: Образ %IMAGE_NAME%:%IMAGE_TAG% опубликован!
goto :eof
:error
echo ============================================
echo ОШИБКА! Проверь шаги выше.
exit /b 1
