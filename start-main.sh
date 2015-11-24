#!/bin/sh

figlet -f isometric1 "Main"

mvn compile exec:java

figlet -f doom "shutdown" | boxes -d netdata