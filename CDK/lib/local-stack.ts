import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import {Tags} from "aws-cdk-lib";
import {GatewayConstruct} from "./gateway-construct";
import {TableConstruct} from "./table-construct";
import {ProdStack} from "./prod-stack";

export class LocalStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);
        const table: TableConstruct = new TableConstruct(this, 'TableConstruct')

        const gatewayConstruct: GatewayConstruct = new GatewayConstruct(this, 'GatewayConstruct', {
            lambdaEnvs: {
                "MICRONAUT_ENVIRONMENTS": "prod",
                "STATELESS_SESSION_AUTH": "false",
                "DYNAMODB_DOMAIN": "localstack",
                "PRIMARY_TABLE_NAME": table.primaryTable.tableName,
                "ORDER_TABLE_NAME": table.orderTable.tableName,
            },
            assetPath: '/builds/restApiHandler/lambda-handler-1.0.0-all.jar',
            tableConstruct: table
        })

        // This tag allows us to use a static endpoint to call the api
        // Eg: http://localhost:4566/_aws/execute-api/api/prod
        // See: https://docs.localstack.cloud/user-guide/aws/apigateway/#:~:text=Copy-,Custom%20IDs%20for%20API%20Gateway%20resources%20via%20tags,-You%20can%20assign
        Tags.of(gatewayConstruct.restApi).add('_custom_id_', 'api')
    }
}
