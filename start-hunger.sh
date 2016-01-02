#!/bin/sh

HOST="localhost"
AGENT="hunger"
MACHINE="hannah"
AGENT_PATH=hannah.HungerAgent

echo "Attempting to start agent with settings:"
printf "Host: \t%s\nAgent: \t%s\nMachine: \t%s\nPath: \t%s\n" "$HOST" "$AGENT" "$MACHINE" "$AGENT_PATH" | boxes -d capgirl

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST -agents $AGENT:$AGENT_PATH"
