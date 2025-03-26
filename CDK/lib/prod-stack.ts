import * as cdk from "aws-cdk-lib";
import {Construct} from "constructs";
import {TableConstruct} from "./table-construct";
import {GatewayConstruct} from "./gateway-construct";

export class ProdStack extends cdk.Stack {
    constructor(scope: Construct, id: string, props?: cdk.StackProps) {
        super(scope, id, props);

        const table: TableConstruct = new TableConstruct(this, 'TableConstruct')
        const gatewayConstruct: GatewayConstruct = new GatewayConstruct(this, 'GatewayConstruct', {
            lambdaEnvs: {
                "MICRONAUT_ENVIRONMENTS": "prod",
                "PRIMARY_TABLE_NAME": table.primaryTable.tableName,
                "ORDER_TABLE_NAME": table.orderTable.tableName,
                "OAUTH_CLIENT_ID": process.env.OAUTH_CLIENT_ID??'not set',
                "OAUTH_CLIENT_SECRET": process.env.OAUTH_CLIENT_SECRET??'not set',
                "OAUTH_DOMAIN": process.env.OAUTH_DOMAIN??'not set',
                "JWT_GENERATOR_SIGNATURE_SECRET": process.env.JWT_GENERATOR_SIGNATURE_SECRET??'not set'
            },
            assetPath: '../lambda-handler/build/libs/lambda-handler-1.0.0-all.jar',
            tableConstruct: table
        })
    }
}