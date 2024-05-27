#!/bin/bash
npm install -g aws-cdk-local aws-cdk

(cd /CDK || return; npm install; cdklocal bootstrap; cdklocal deploy)