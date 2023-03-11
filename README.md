# cloud-capstone-backend
My capstone project for Udacity's AWS Cloud Developer Nanodegree

# AWS Setup

1. In the AWS console, navigate to Elastic Container Registry and create a new private repository named cloud-capstone. According to the AWS documentation, the repository has to be private if we want to use continuous deployment:
"App Runner doesn't support automatic (continuous) deployment for Amazon ECR Public images." (https://docs.aws.amazon.com/apprunner/latest/dg/service-source-image.html)

2. Create an IAM role named `ecrAccessRole` and give the permission `AWSAppRunnerServicePolicyForECRAccess` to it. Create a `Trust relationship` that allows the service `build.apprunner.amazonaws.com` to assume this role in order to fetch the newest image from the ECR.


    {
        "Version": "2012-10-17",
        "Statement": [
            {
               "Effect": "Allow",
               "Principal": {
                   "AWS": "arn:aws:iam::<YOUR_AWS_ACCOUNT_ID>:root",
                   "Service": "build.apprunner.amazonaws.com"
                },
                "Action": "sts:AssumeRole",
                "Condition": {}
            }
        ]
    }


3. Create an autoscaling configuration that will be used by our service. This step is optional - if no autoscaling configuration is defined, a default configuration will be used.

    
    aws apprunner create-auto-scaling-configuration --cli-input-json file://auto-scaling-configuration.json

4. Create an AppRunner service named `cloud-capstone` that is based on the latest ECR image. When there is a new container image in ECR, the service will be redeployed automatically.


    aws apprunner create-service --cli-input-json file://cloud-capstone-service.json
