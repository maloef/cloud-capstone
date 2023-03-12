# cloud-capstone-backend
My final project for Udacity's AWS Cloud Developer Nanodegree. This is an alternative implementation of the backend of the Todo application from chapter 4 of the Nanodegree. The implementation uses Java, Spring Boot, and AWS App Runner. The React frontend for this application is in the repository https://github.com/maloef/cloud-capstone-frontend. This is exactly the same code as in chapter 4 - I only updated the endpoint configuration.

# Technical remarks
* The backend behaves in the same way as the backend in chapter 4. Like in chapter 4, Auth0 is used for authentication.

* Continuous deployment: A new Docker image is built automatically with Github actions when code is pushed to GitHub. The image is pushed to AWS Elastic Container Registry. AWS App Runner automatically deploys the new version.

* The DynamoDB table and the S3 bucket that are used by the application are created on application start if they do not yet exist.

# How to set up the project in AWS
1. In the AWS console, navigate to Elastic Container Registry and create a new private repository named `cloud-capstone`. According to the AWS documentation, this repository has to be private if we want to use continuous deployment:
   "App Runner doesn't support automatic (continuous) deployment for Amazon ECR public images." (https://docs.aws.amazon.com/apprunner/latest/dg/service-source-image.html)

2. Create a role named `ecrAccessRole` that will be assumed by the service `build.apprunner.amazonaws.com`


    aws iam create-role --role-name ecrAccessRole --assume-role-policy-document file://ecrAccessRole-policy.json

3. Allow this role to access ECR


    aws iam attach-role-policy --role-name ecrAccessRole --policy-arn arn:aws:iam::aws:policy/AWSAppRunnerServicePolicyForECRAccess

4. Create a role named `capstoneInstanceRole` that will be assumed by the service `tasks.apprunner.amazonaws.com`


    aws iam create-role --role-name capstoneInstanceRole --assume-role-policy-document file://capstoneInstanceRole-policy.json

5. Allow this role to access DynamoDB and S3


    aws iam attach-role-policy --role-name capstoneInstanceRole --policy-arn arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess
    aws iam attach-role-policy --role-name capstoneInstanceRole --policy-arn arn:aws:iam::aws:policy/AmazonS3FullAccess

6. Create an autoscaling configuration that will be used by our service. This step is optional - if no autoscaling configuration is defined, a default configuration will be used.


    aws apprunner create-auto-scaling-configuration --cli-input-json file://auto-scaling-configuration.json

7. Create an AppRunner service named `cloud-capstone` that is based on the latest ECR image. When there is a new container image in ECR, the service will be redeployed automatically. The service uses the role `capstoneInstanceRole` do access DynamoDB.


    aws apprunner create-service --cli-input-json file://cloud-capstone-service.json
