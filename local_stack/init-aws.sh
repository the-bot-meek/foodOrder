#!/bin/bash
# This is not intended to be run by a developer. This script is used to run the CDK project inside of local stack
# This script doesn't work with the community edition of localstack if PERSISTENCE is enabled as cloudformation stacks aren't persisted
chmod -R 777 /etc/localstack/init/ready.d
npm --prefix /CDK ci

aws configure set aws_access_key_id "1234"
aws configure set aws_secret_access_key "1234"
aws configure set default.region "local"


has_bootstrap_run=$(awslocal cloudformation describe-stacks --region eu-west-1 --query "contains(Stacks[*].StackName,'CDKToolkit')")
if [ "$has_bootstrap_run" == "false" ];
  then npm --prefix /CDK run localstack-bootstrap;
fi
npm --prefix /CDK run localstack-deploy
