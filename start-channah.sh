#!/bin/sh

HOST="192.168.1.116"
AGENT="hannah"
AGENT_CLASS=hannah.ConcurrentMoodHannah

printf "Machine %s" "$AGENT" | figlet

echo "Attempting to start agent with settings:"
printf "Host: \t%s\nAgent: \t%s\nPath: \t%s\n" "$HOST" "$AGENT" "$AGENT_CLASS"

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST -agents $AGENT:$AGENT_CLASS"
