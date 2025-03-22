import * as cdk from 'aws-cdk-lib';
import {Construct} from 'constructs';
import * as dynamodb from 'aws-cdk-lib/aws-dynamodb'
import {ProjectionType} from 'aws-cdk-lib/aws-dynamodb'
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as apigw from 'aws-cdk-lib/aws-apigateway';
import {Tags} from "aws-cdk-lib";

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);
    const primaryTable = new dynamodb.TableV2(this, 'primary_table', {
      partitionKey: { name: 'pk', type: dynamodb.AttributeType.STRING },
      sortKey: {name: 'sk', type: dynamodb.AttributeType.STRING},
      tableName: "primary_table",
      globalSecondaryIndexes: [{
        indexName: "gsi",
        partitionKey: {name: "gsi_pk", type: dynamodb.AttributeType.STRING},
        sortKey: {name: "gsi_sk", type: dynamodb.AttributeType.STRING},
        projectionType: ProjectionType.ALL
      }]
    });

    const orderTable = new dynamodb.TableV2(this, 'order_table', {
      partitionKey: {name: 'meal_id', type: dynamodb.AttributeType.STRING},
      sortKey: {name: 'date_of_meal', type: dynamodb.AttributeType.STRING},
      tableName: "order_table",
      globalSecondaryIndexes: [{
        indexName: 'uid_gsi',
        partitionKey: {name: 'uid', type: dynamodb.AttributeType.STRING},
        sortKey: {name: "meal_id", type: dynamodb.AttributeType.STRING},
        projectionType: dynamodb.ProjectionType.ALL
      }]
    })

    const micronautLambda = new lambda.Function(this, 'MicronautLambda', {
      runtime: lambda.Runtime.JAVA_21, // Micronaut typically uses Java; adjust if necessary.
      handler: 'io.micronaut.function.aws.proxy.payload1.ApiGatewayProxyRequestEventFunction::handleRequest', // Change this to your actual handler.
      code: lambda.Code.fromAsset('/builds/restApiHandler/lambda-handler-1.0.0-all.jar'), // The directory containing your packaged JAR.
      memorySize: 1024,
      timeout: cdk.Duration.seconds(10),
      environment: {
        "MICRONAUT_ENVIRONMENTS": "mock_auth",
        "STATELESS_SESSION_AUTH": "false",
        "DYNAMODB_DOMAIN": "localstack"
      }
    });

    const restApi= new apigw.LambdaRestApi(this, 'MicronautApi', {
      handler: micronautLambda,
      proxy: true,
    });

    // This tag allows us to use a static endpoint to call the api
    // Eg: http://localhost:4566/_aws/execute-api/api/prod
    // See: https://docs.localstack.cloud/user-guide/aws/apigateway/#:~:text=Copy-,Custom%20IDs%20for%20API%20Gateway%20resources%20via%20tags,-You%20can%20assign
    Tags.of(restApi).add('_custom_id_', 'api')
  }
}
