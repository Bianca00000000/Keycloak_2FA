#!/bin/bash

kubectl apply -f postgres.yaml

if [ $? -ne 0 ]; then
    echo "Eroare la aplicarea configurarii PostgreSQL"
    exit 1
fi

# Facem un sleep pentru a ne asigura ca PostgreSQL este gata
sleep 10

kubectl apply -f keycloak.yaml

if [ $? -ne 0 ]; then
    echo "Eroare la aplicarea configurarii Keycloak"
    exit 1
fi