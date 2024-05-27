#!/bin/bash
npm install -g aws-cdk-local aws-cdk
chmod -R 777 /etc/localstack/init/ready.d
(cd /CDK || return; npm install; cdklocal bootstrap; cdklocal deploy)