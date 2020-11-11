#!/bin/bash

CERT_PATH=@certsPath@
[ -d $CERT_PATH ] || mkdir $CERT_PATH

echo "copy pem(s) and cer(s) from app package if any ..."
[ -f /app/resources/*.pem ] && cp /app/resources/*.pem $CERT_PATH
[ -f /app/resources/*.cer ] && cp /app/resources/*.cer $CERT_PATH

echo "Importing pem(s) and cer(s) if any ..."
[ -f $CERT_PATH/*.pem ] && ls $CERT_PATH/*.pem | xargs  -I {} sh -c 'export certName=`basename {} | cut -d. -f1`;export certFile={};echo "adding certificate $certName from $certFile ..."; $JAVA_HOME/bin/keytool -import -alias ${certName} -cacerts -storepass changeit -noprompt -file $certFile'
[ -f $CERT_PATH/*.cer ] && ls $CERT_PATH/*.cer | xargs  -I {} sh -c 'export certName=`basename {} | cut -d. -f1`;export certFile={};echo "adding certificate $certName from $certFile ..."; $JAVA_HOME/bin/keytool -import -alias ${certName} -cacerts -storepass changeit -noprompt -file $certFile'

echo "Launching java process. Command is :  exec java -cp /app/resources:/app/classes:/app/libs/* @mainClassName@"
exec java -cp "/app/resources:/app/classes:/app/libs/*" @mainClassName@