import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';
import * as dynamodb from 'aws-cdk-lib/aws-dynamodb'

export class CdkStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);
    const primaryTable = new dynamodb.TableV2(this, 'primary_table', {
      partitionKey: { name: 'pk', type: dynamodb.AttributeType.STRING },
      sortKey: {name: 'sk', type: dynamodb.AttributeType.STRING},
      tableName: "primary_table",
    });

    const orderTable = new dynamodb.TableV2(this, 'order_table', {
      partitionKey: {name: 'meal_id', type: dynamodb.AttributeType.STRING},
      sortKey: {name: 'date_of_meal', type: dynamodb.AttributeType.STRING},
      tableName: "order_table",
      globalSecondaryIndexes: [{
        indexName: 'uid_gsi',
        partitionKey: {name: 'uid', type: dynamodb.AttributeType.STRING},
        projectionType: dynamodb.ProjectionType.ALL
      }]
    })
  }
}
