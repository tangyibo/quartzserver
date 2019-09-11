#!/usr/bin/env bash
#############################################
# !!!!!! Modify here please

DS_PROG="QuartzServer-0.0.1.jar"

#############################################

DS_HOME="${BASH_SOURCE-$0}"
DS_HOME="$(dirname "${DS_HOME}")"
DS_HOME="$(cd "${DS_HOME}"; pwd)"
DS_HOME="$(cd "$(dirname ${DS_HOME})"; pwd)"
#echo "Base Directory:${DS_HOME}"

DS_BIN_PATH=$DS_HOME/bin
DS_LIB_PATH=$DS_HOME/lib
DS_CONF_PATH=$DS_HOME/conf

DS_PID_FILE="${DS_HOME}/run/${DS_PROG}.pid"
DS_RUN_LOG="${DS_HOME}/run/run_${DS_PROG}.log"

[ -d "${DS_HOME}/run" ] || mkdir -p "${DS_HOME}/run"
[ -d "${DS_HOME}/logs" ] || mkdir -p "${DS_HOME}/logs"
cd ${DS_HOME}

echo -n `date +'%Y-%m-%d %H:%M:%S'`             >>${DS_RUN_LOG}
echo "---- Start service [${DS_MAIN}] process. ">>${DS_RUN_LOG}

# JVMFLAGS JVM参数可以在这里设置
JVMFLAGS=-Dfile.encoding=UTF-8

if [ "$JAVA_HOME" != "" ]; then
  JAVA="$JAVA_HOME/bin/java"
else
  JAVA=java
fi

nohup $JAVA -Xbootclasspath/a:$DS_CONF_PATH $JVMFLAGS -jar $DS_LIB_PATH/$DS_PROG >>${DS_RUN_LOG} 2>&1 &
echo "$JAVA -Xbootclasspath/a:$DS_CONF_PATH $JVMFLAGS -jar $DS_LIB_PATH/$DS_PROG >>${DS_RUN_LOG} 2>&1 &"

RETVAL=$?
PID=$!

echo ${PID} >${DS_PID_FILE}
exit ${RETVAL}


