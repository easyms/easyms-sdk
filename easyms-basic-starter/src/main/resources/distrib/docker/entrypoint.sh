#!/bin/bash

set -x

CERT_PATH=@certsPath@

echo "Importing pem(s) and cer(s) if any ..."
[ 0 -lt $(ls $CERT_PATH/{*.pem,*.cer,*.crt} 2>/dev/null | wc -w) ] && ls $CERT_PATH/{*.pem,*.cer,*.crt} 2>/dev/null | xargs  -I {} sh -c 'export certName=`basename {} | cut -d. -f1`;export certFile={};echo "adding certificate $certName from $certFile ..."; $JAVA_HOME/bin/keytool -import -alias ${certName} -cacerts -storepass changeit -noprompt -file $certFile'

echo "Launching java process. Command is :  exec java -cp /app/resources:/app/classes:/app/libs/* @mainClassName@"
exec java -cp "/app/resources:/app/classes:/app/libs/*" @mainClassName@