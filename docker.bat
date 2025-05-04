@echo off
REM Название образа
set IMAGE_NAME=Auth

echo Building Docker image: %IMAGE_NAME%...
docker build -t %IMAGE_NAME% .

echo Done!
pause