#!/bin/bash
APP_LOG_PATH=/logs

export JAVA_OPTS="$JAVA_OPTS \
        -server \
        -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:G1HeapRegionSize=8m -XX:+ParallelRefProcEnabled -XX:-ResizePLAB \
        -verbose:gc -Xloggc:${APP_LOG_PATH}/gc/gc.`date '+%Y%m%d%H%M'`.log -XX:+PrintGCDetails \
        -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${APP_LOG_PATH}/gc/heapdump_`date '+%Y%m%d%H%M'`.hprof"
