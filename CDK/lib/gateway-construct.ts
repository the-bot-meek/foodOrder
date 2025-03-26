import * as cdk from "aws-cdk-lib";
import {Construct} from "constructs";
import * as lambda from "aws-cdk-lib/aws-lambda";
import * as apigw from "aws-cdk-lib/aws-apigateway";
import {TableConstruct} from "./table-construct";


interface GatewayStackProps {
    lambdaEnvs: {
        [key: string]: string;
    };
    assetPath: string;
    tableConstruct: TableConstruct
}

export class GatewayConstruct extends Construct {
    readonly function: lambda.Function;
    readonly restApi: apigw.LambdaRestApi;

    constructor(scope: Construct, id: string, props: GatewayStackProps) {
        super(scope, id);

        this.function = new lambda.Function(this, 'MicronautLambda', {
            runtime: lambda.Runtime.JAVA_21, // Micronaut typically uses Java; adjust if necessary.
            handler: 'io.micronaut.function.aws.proxy.payload1.ApiGatewayProxyRequestEventFunction::handleRequest', // Change this to your actual handler.
            code: lambda.Code.fromAsset(props.assetPath), // The directory containing your packaged JAR.
            memorySize: 1024,
            timeout: cdk.Duration.minutes(10),
            environment: props.lambdaEnvs
        });

        this.restApi = new apigw.LambdaRestApi(this, 'MicronautApi', {
            handler: this.function,
            proxy: true,
        });

        props.tableConstruct.orderTable.grantReadData(this.function);
        props.tableConstruct.orderTable.grantWriteData(this.function);

        props.tableConstruct.primaryTable.grantReadData(this.function);
        props.tableConstruct.primaryTable.grantWriteData(this.function);

    }
}