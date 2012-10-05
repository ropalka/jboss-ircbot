#!/bin/sh

IRCBOT_HOME=`pwd`/..
CP=$CP:$IRCBOT_HOME/lib/fossnova-factory-finder.jar
CP=$CP:$IRCBOT_HOME/lib/fossnova-json-api.jar
CP=$CP:$IRCBOT_HOME/lib/fossnova-json-impl.jar
CP=$CP:$IRCBOT_HOME/lib/ircbot-api.jar
CP=$CP:$IRCBOT_HOME/lib/ircbot-impl.jar
CP=$CP:$IRCBOT_HOME/lib/ircbot-plugins.jar
CP=$CP:$IRCBOT_HOME/lib/jboss-logging.jar
CP=$CP:$IRCBOT_HOME/lib/jboss-modules.jar
CP=$CP:$IRCBOT_HOME/lib/jboss-msc.jar
CP=$CP:$IRCBOT_HOME/lib/x2jb-annotation-provider.jar
CP=$CP:$IRCBOT_HOME/lib/x2jb-core.jar
CP=$CP:$IRCBOT_HOME/lib/x2jb-default-handlers.jar

#JAVA_OPTS="$JAVA_OPTS -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=n"

java -classpath $CP $JAVA_OPTS org.jboss.ircbot.Main $IRCBOT_HOME/conf/config.xml
