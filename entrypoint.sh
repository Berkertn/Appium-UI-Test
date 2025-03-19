#!/bin/bash

mkdir -p /root/appium-logs

adb start-server
sleep 2

adb connect host.docker.internal:5555
adb connect host.docker.internal:5557

adb wait-for-device

tail -f /dev/null