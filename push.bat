@echo off
echo Starting push
d:
cd /project/springboot-rabbitmq
set /p commit=commit:
title auto commit
git add -A
git commit -m %commit%
git push
echo end push
