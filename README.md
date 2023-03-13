# cloud-capstone-backend
My final project for Udacity's _Cloud Developer Nanodegree_. This is an alternative implementation of the backend of the Todo application from chapter 4 of the Nanodegree. The React frontend for this application is in the repository https://github.com/maloef/cloud-capstone-frontend. This is exactly the same code as in chapter 4 - I only updated the endpoint configuration.

## Technical remarks
* The backend behaves in the same way as the backend from chapter 4. Like in chapter 4, Auth0 is used for authentication.
* The application can be started locally with `mvn spring-boot:run`.
* Continuous deployment:
    * A new Docker image is built automatically with GitHub actions when code is pushed to GitHub. This is configured in the file `.github/workflows/deploy-to-ecr.yml`.
    * GitHub pushes this image to AWS Elastic Container Registry.

TBC