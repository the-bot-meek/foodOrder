import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {Tags} from "aws-cdk-lib";
import {GatewayConstruct} from "./gateway-construct";
import {TableConstruct} from "./table-construct";
import {ProdStack} from "./prod-stack";
import {LocalStack} from "./local-stack";

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    if (process.env.USER === 'localstack') {
      new LocalStack(this, 'LocalStack', props)
    } else {
      new ProdStack(this, 'ProdStack', props)
    }
  }
}
