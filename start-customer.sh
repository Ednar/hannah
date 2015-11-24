#!/bin/sh

if [[ "$#" != 4 ]]; then
    echo "Exempel: $0 (host) (agentnamn) (önskat kaffe) (maskinnamn)" >&2
    exit 1
fi

HOST=$1
AGENT=$2
COFFEE=$3
MACHINE=$4
AGENT_PATH=coffeeMachine.customerAgent.CustomerAgent

echo "Attempting to start agent with settings:"
printf "Host: \t%s\nAgent: \t%s\nCoffee: \t%s\nMachine: \t%s\nPath: \t%s\n" "$HOST" "$AGENT" "$COFFEE" "$MACHINE" "$AGENT_PATH" | boxes -d capgirl

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST -agents $AGENT:$AGENT_PATH($COFFEE,$MACHINE)"
