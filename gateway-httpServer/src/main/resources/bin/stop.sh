#check JAVA_HOME & java
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
#==============================================================================

#set JAVA_OPTS
JAVA_OPTS="-Xss256k"
#==============================================================================

#stop Server
ps -ef|grep 'EwayBootstrap' | grep 'java' |grep -v 'grep' |awk '{print $2}'|while read line
do
  eval "kill $line"
done
sleep 4

echo "com.jwcjlu.gateway.httpServer.bootstrap.EwayBootstrap is shutdown."

#==============================================================================
