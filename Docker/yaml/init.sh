#!/bin/bash

kubectl apply -f postgres.yaml

if [ $? -ne 0 ]; then
    echo "Error applying PostgreSQL configuration"
    exit 1
fi

# We do a sleep to make sure that PostgreSQL is ready
sleep 10

kubectl apply -f keycloak.yaml

if [ $? -ne 0 ]; then
    echo "Error applying Keycloak configuration"
    exit 1
fi