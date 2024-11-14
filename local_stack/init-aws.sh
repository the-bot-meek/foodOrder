#!/bin/bash
# This is not intended to be run by a developer. This script is used to run the CDK project inside of local stack
npm install -g aws-cdk-local aws-cdk
chmod -R 777 /etc/localstack/init/ready.d
npm --prefix /CDK ci
npm --prefix /CDK run localstack-bootstrap
npm --prefix /CDK run clocalstack-deploy
