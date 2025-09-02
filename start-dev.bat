@echo off
echo 🚀 Starting MinIO for CodeKaro...
echo.

REM Start MinIO automatically
echo 📁 Starting MinIO (so you don't need to run it in terminal)...
docker-compose up -d

echo.
echo ✅ MinIO is now running automatically!
echo.
echo 🌐 MinIO Console: http://localhost:9001
echo 👤 Username: minioadmin
echo 🔑 Password: minioadmin
echo.
echo 📋 Next steps:
echo 1. Upload test case files to MinIO (bucket: testcases)
echo 2. Start your Java services normally
echo 3. MinIO will keep running in background
echo.
pause