#!/bin/sh

if [[ "$#" != 3 ]]; then
    echo "Exempel: $0 (host) (agentnamn) (maskinnamn)" >&2
    exit 1
fi

HOST=$1
AGENT=$2
MACHINE=$3
AGENT_PATH=coffeeMachine.customerAgent.CustomerAgent

echo "Attempting to start agent with settings:"
printf "Host: \t%s\nAgent: \t%s\nMachine: \t%s\nPath: \t%s\n" "$HOST" "$AGENT" "$MACHINE" "$AGENT_PATH" | boxes -d capgirl

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST -agents $AGENT:$AGENT_PATH($MACHINE)"
