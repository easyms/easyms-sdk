#!/bin/sh

set -x -e

BASEDIR=`dirname $0`/..
cd $BASEDIR
BASEDIR=`pwd`

prog="%_project.artifactId_%"
progParamName="processName"
version="%_project.version_%"
JAVA_CMD=${JAVA_HOME}/bin/java

jarFile="./lib/${prog}.jar"
configDir="${BASEDIR}/config"
runDir="${BASEDIR}/run"

logConfigFile="${configDir}/logback.xml"
PID_FILE="$runDir/application.pid"
JVM_ARGUMENTS="-D${progParamName}=${prog}"
JVM_ARGS="$(cat ${configDir}/jvm.args) ${JVM_ARGUMENTS}"


if [ -z ${SPRING_PROFILES_ACTIVE+x} ]; then
    SPRING_PROFILES_ACTIVE="docker";
fi


OPTIONS=$*
exec="exec ${JAVA_CMD} ${JVM_ARGS} -jar ${jarFile}"

## check config dir
[ ! -d "$configDir" ] && failure "missing config directory $configDir" && exit 6


CMD_LINE="$exec --spring.profiles.active=${SPRING_PROFILES_ACTIVE} --info.version=$version --spring.config.name=$prog --spring.pidfile=$PID_FILE --logging.config=${logConfigFile}  --spring.config.location=${configDir}/"
## start microservice
STARTUP_TIME=`date "+%Y%m%d-%H%M%S"`
echo "${STARTUP_TIME} : Running : ${CMD_LINE}"
eval ${CMD_LINE}

exit $?