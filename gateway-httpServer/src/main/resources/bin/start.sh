#!/usr/bin/env bash
noJavaHome=false
if [ -z "$JAVA_HOME" ] ; then
    noJavaHome=true
fi

if [ ! -e "$JAVA_HOME/bin/java" ] ; then
    noJavaHome=true
fi

if $noJavaHome ; then
    echo
    echo "Error: JAVA_HOME environment variable is not set."
    echo
    exit 1
fi

#$0表示当前脚本的路径.
cd `dirname $0`/..

BASE_HOME=`pwd`

if [ -z "$BASE_HOME" ] ; then
    echo
    echo "Error: BASE_HOME environment variable is not defined correctly."
    echo
    exit 1
fi
#$1表示脚本跟的参数，加debug的话表示是调试模式，jmx用于远程监控该jvm.
JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y "
fi
JAVA_JMX_OPTS=""
if [ "$1" = "jmx" ]; then
    JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
fi
#==============================================================================

#set JAVA_OPTS
JAVA_OPTS="-server -Xms8g -Xmx8g -Xmn4g -Xss256k"
JAVA_OPTS="$JAVA_OPTS -XX:+AggressiveOpts"
JAVA_OPTS="$JAVA_OPTS -XX:+UseBiasedLocking"
JAVA_OPTS="$JAVA_OPTS -XX:+UseFastAccessorMethods"
JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"
JAVA_OPTS="$JAVA_OPTS -XX:+CMSParallelRemarkEnabled"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSCompactAtFullCollection"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"
JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"
JAVA_OPTS="$JAVA_OPTS $JAVA_JMX_OPTS"
JAVA_OPTS="$JAVA_OPTS $JAVA_DEBUG_OPTS"



#==============================================================================
CLASS_PATH=""
for i in "$BASE_HOME"/lib/*.jar
do
    if [ -n "$CLASS_PATH" ]
    then
        CLASS_PATH="$CLASS_PATH:$i"
    else
        CLASS_PATH="$i"
    fi
done
#=================================================================

#startup server
RUN_CMD="$JAVA_HOME/bin/java"
RUN_CMD="$RUN_CMD -classpath $CLASS_PATH -Dlog4j.configurationFile=$BASE_HOME/conf/log4j.xml"
RUN_CMD="$RUN_CMD $JAVA_OPTS"
RUN_CMD="$RUN_CMD com.jwcjlu.gateway.httpServer.bootstrap.EwayBootstrap 1>>/dev/null 2>&1 &"

echo $RUN_CMD
eval $RUN_CMD
