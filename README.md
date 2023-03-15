# cloud-capstone-backend
My final project for Udacity's _Cloud Developer Nanodegree_. This is an alternative implementation of the backend of the Todo application from chapter 4 of the Nanodegree based on Java and Spring Boot. The React frontend for this application is in the repository https://github.com/maloef/cloud-capstone-frontend. This is exactly the same code as in chapter 4 - I only updated the endpoint configuration.

## General remarks
* The backend behaves in the same way as the backend from chapter 4. Like in chapter 4, Auth0 is used for authentication.
* The application uses a DynamoDB table and an S3 bucket. At startup, both are created if they don't exist yet.
* The latest Docker image can be pulled from ECR with `docker pull public.ecr.aws/g4d6r5w8/cloud-capstone:latest`.
* Continuous delivery:
    * New Docker images for backend and frontend are built automatically with GitHub actions when code is pushed to GitHub. This is configured in the two files in the folder `.github/workflows`.
    * GitHub pushes these images to AWS Elastic Container Registry.
    * The lastest image can be deployed with `kubectl rollout restart deploy cloud-capstone-backend` and `kubectl rollout restart deploy cloud-capstone-frontend`.

## Setting up the project
* Check out the code.
* Make sure to have AWS credentials with sufficient rights to create a DynamoDB table and an S3 bucket.
* Use Maven to build the project (`mvn verify`) and to run the application locally (`mvn spring-boot:run`).
* If the application is running, you can test it
  * either with the frontend project (https://github.com/maloef/cloud-capstone-frontend) after setting the apiEndpoint in `src/config.ts` to `localhost:8080`
  * or with the Postman collection provided after entering a valid JWT for the `authToken` variable.
    