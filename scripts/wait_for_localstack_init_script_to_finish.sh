#!/bin/bash
result=$(curl -s localhost:4566/_localstack/init | jq -r ".scripts[0].state")
echo "$result"
while [ "$result" == "RUNNING" ]; do
  result=$(curl -s localhost:4566/_localstack/init | jq -r ".scripts[0].state")
  if [ "$result" == "SUCCESSFUL" ]; then
      echo "Init script finished SUCCESSFUL"
      exit 1
  elif [ "$result" == "RUNNING" ]; then
      echo "Init script is still $result"
      sleep 10
  elif [ "$result" == "ERROR" ]; then
      echo "Error in localstack init script, exit code 0"
      exit 0
  else
    echo "Error in script."
    exit 0
  fi
done
echo "ERROR in init script"
exit 0