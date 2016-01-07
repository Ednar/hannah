#!/bin/sh

if [[ "$#" != 1 ]]; then
    echo "Du måste ange IP adress till main container" >&2
    exit 1
fi

HOST=$1
AGENT="hannah"
AGENT_CLASS="hannah.ConcurrentMoodHannah"

printf "%s" "$AGENT" | figlet

echo "Attempting to start agent with settings:"
printf "Host: \t%s\nAgent: \t%s\nPath: \t%s\n" "$HOST" "$AGENT" "$AGENT_CLASS"

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST -agents $AGENT:$AGENT_CLASS"
