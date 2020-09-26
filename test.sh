#!/bin/bash

curl 'http://localhost:8080/' ; echo

curl -d 'aauaiibbbru,Ivaana Bello'  -H 'Content-Type: text/plain' 'http://localhost:8080/submission' ; echo
curl -d 'eiiuuegiebeici,Cami Diaz'  -H 'Content-Type: text/plain' 'http://localhost:8080/submission' ; echo
curl -d 'eiiegbczu,Bob Bloch'       -H 'Content-Type: text/plain' 'http://localhost:8080/submission' ; echo

curl 'http://localhost:8080/results' ; echo
curl 'http://localhost:8080/health'  ; echo
curl 'http://localhost:8080/reset'   ; echo
