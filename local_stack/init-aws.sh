#!/bin/bash
npm install -g aws-cdk-local aws-cdk

(cd /CDK || return; cdklocal bootstrap; npm install; cdklocal deploy)