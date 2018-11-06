#!/bin/bash

pushd $(dirname $0) > /dev/null
basepath=$(pwd)
popd > /dev/null

cat << EndOfMessage
HELP:
./loadbalancer.sh

EndOfMessage

java -cp "$basepath"/loadbalancer.jar:"$basepath"/shared.jar -Djava.security.policy="$basepath"/policy ca.polymtl.inf8480.tp2.loadbalancer.LoadBalancer $*