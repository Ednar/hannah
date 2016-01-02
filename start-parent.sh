#!/bin/sh


HOST="localhost"
AGENT="parent"
AGENT_PATH=parent.ParentAgent

echo "Attempting to start agent with settings:"
printf "Host: \t%s\nAgent: \t%s\nMachine: \t%s\nPath: \t%s\n" "$HOST" "$AGENT" "$AGENT_PATH" | boxes -d capgirl

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST -agents $AGENT:$AGENT_PATH"
