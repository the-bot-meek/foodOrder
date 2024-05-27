#!/bin/bash
count=0
while true; do
  result=$(curl -s localhost:4566/_localstack/init | jq -r ".scripts[0].state")
  if [ "$result" == "SUCCESSFUL" ]; then
      echo "Init script finished SUCCESSFUL"
      exit 0
  elif [ "$result" == "RUNNING" ]; then
      echo "Init script is still $result"
      sleep 10
  elif [ "$result" == "ERROR" ]; then
      echo "Error in localstack init script, exit code 0"
      exit 1
  else
    echo "Unexpected response: $result"
  ((count++))
  if [ "$count" -ge 10 ]; then
    echo "Timeout exit code 0"
    exit 0
  fi
  sleep 10


  fi
done
exit 0