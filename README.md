# cloud-capstone-backend
My capstone project for Udacity's AWS Cloud Developer Nanodegree

# AWS Setup

1. In the AWS console, navigate to Elastic Container Registry and create a new private repository named cloud-capstone. According to the AWS documentation, the repository has to be private if we want to use continuous deployment:
"App Runner doesn't support automatic (continuous) deployment for Amazon ECR Public images." (https://docs.aws.amazon.com/apprunner/latest/dg/service-source-image.html)

2. Create a role named `ecrAccessRole` that will be assumed by the service `build.apprunner.amazonaws.com`


    aws iam create-role --role-name ecrAccessRole --assume-role-policy-document file://ecrAccessRole-policy.json


3. Allow this role to access the Elastic Container Repository (ECR)


    aws iam attach-role-policy --role-name ecrAccessRole --policy-arn arn:aws:iam::aws:policy/AWSAppRunnerServicePolicyForECRAccess

4. Create a role named `capstoneInstanceRole` that will be assumed by the service `tasks.apprunner.amazonaws.com`


    aws iam create-role --role-name capstoneInstanceRole --assume-role-policy-document file://capstoneInstanceRole-policy.json

5. Allow this role to access DynamoDB


    aws iam attach-role-policy --role-name capstoneInstanceRole --policy-arn arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess

6. Create an autoscaling configuration that will be used by our service. This step is optional - if no autoscaling configuration is defined, a default configuration will be used.

    
    aws apprunner create-auto-scaling-configuration --cli-input-json file://auto-scaling-configuration.json

7. Create an AppRunner service named `cloud-capstone` that is based on the latest ECR image. When there is a new container image in ECR, the service will be redeployed automatically. The service uses the role `capstoneInstanceRole` do access DynamoDB.


    aws apprunner create-service --cli-input-json file://cloud-capstone-service.json
