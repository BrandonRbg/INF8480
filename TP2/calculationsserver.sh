#!/bin/bash

pushd $(dirname $0) > /dev/null
basepath=$(pwd)
popd > /dev/null

cat << EndOfMessage
HELP:
./calculationsserver.sh

EndOfMessage

java -cp "$basepath"/calculationsserver.jar:"$basepath"/shared.jar -Djava.security.policy="$basepath"/policy ca.polymtl.inf8480.tp2.calculationsserver.CalculationsServer $*