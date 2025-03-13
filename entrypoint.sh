#!/bin/bash

# Appium Log Klasörü
mkdir -p /root/appium-logs

adb start-server
sleep 2

adb connect host.docker.internal:5555
adb connect host.docker.internal:5557

# 1. Appium Server
#appium --port 4724 --relaxed-security &
#echo "Appium Server started on port 4724"

# 2. Appium Server
#appium --port 4725 --relaxed-security &
#echo "Appium Server started on port 4725"


# ADB Servisini Bekle
adb wait-for-device

# Başlatıldıktan sonra Container açık kalsın
tail -f /dev/null
