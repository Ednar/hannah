#!/bin/sh

if [[ "$#" != 2 ]]; then
    echo "Du måste ange host och agentnamn" >&2
    echo "Exempel: $0 (main container host) (agentnamn)" >&2
    exit 1
fi

HOST=$1
AGENT=$2
AGENT_CLASS=coffeeMachine.coffeeMachineAgent.CoffeeMachineAgent

printf "Machine %s" "$AGENT" | figlet

echo "Attempting to start agent with settings:"
printf "Host: \t%s\nAgent: \t%s\nPath: \t%s\n" "$HOST" "$AGENT" "$AGENT_CLASS"

mvn compile exec:java \
    -Dexec.mainClass=jade.Boot \
    -Dexec.args="-container -host $HOST -agents $AGENT:$AGENT_CLASS"