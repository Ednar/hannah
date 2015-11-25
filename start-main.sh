#!/bin/sh

figlet -f isometric1 "Main"

mvn compile exec:java -Djava.awt.headless=true

figlet -f doom "shutdown" | boxes -d netdata