# cloud-capstone
My final project for Udacity's _Cloud Developer Nanodegree_. This is an alternative implementation of the backend of the Todo application from chapter 4 of the Nanodegree based on Java and Spring Boot. I reused the React frontend from chapter 4 without any functional changes. Backend and frontend are deployed together in a Kubernetes cluster.

## General remarks
* The application uses a DynamoDB table and an S3 bucket. At startup, both are created if they don't exist yet.
* The latest Docker image can be pulled from ECR with `docker pull public.ecr.aws/g4d6r5w8/cloud-capstone-backend:latest` and `docker pull public.ecr.aws/g4d6r5w8/cloud-capstone-frontend:latest`
* Continuous delivery:
    * New Docker images for backend and frontend are built automatically with GitHub actions when code is pushed to GitHub. This is configured in the two files in the folder `.github/workflows`.
    * GitHub pushes these images to AWS Elastic Container Registry.
    * The lastest image can be deployed with `kubectl rollout restart deploy cloud-capstone-backend` and `kubectl rollout restart deploy cloud-capstone-frontend`.

## Setting up the backend project
* Check out the code.
* Make sure to have AWS credentials with sufficient rights to create a DynamoDB table and an S3 bucket.
* Use Maven to build the project (`mvn verify`) and to run the application locally (`mvn spring-boot:run`).
* If the application is running, you can test it locally
  * either with the frontend project after changing apiEndpoint and callbackUrl to the values that have been commented out
  * or with the Postman collection provided after entering a valid JWT for the `authToken` variable.
    