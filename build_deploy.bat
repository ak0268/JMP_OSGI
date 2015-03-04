@echo off
cd iface_bundle
call mvn clean install -Pauto-deploy
if ERRORLEVEL 1 goto PAUSE
cd ..
cd console_impl_fragment
call mvn clean install -Pauto-deploy
if ERRORLEVEL 1 goto PAUSE
cd ..
cd file_impl_bundle
call mvn clean install -Pauto-deploy
if ERRORLEVEL 1 goto PAUSE
:PAUSE
pause