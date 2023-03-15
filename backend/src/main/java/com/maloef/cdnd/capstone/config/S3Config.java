package com.maloef.cdnd.capstone.config;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.CORSRule;
import com.amazonaws.services.s3.model.SetBucketPolicyRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class S3Config {

    @Value("${s3.image-bucket}")
    private String bucketName;

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
        init(s3Client);

        return s3Client;
    }

    public void init(AmazonS3 s3Client) {
        if (!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
            configureBucketAccess(s3Client);
            configureBucketCorsConfig(s3Client);
        }
    }

    private void configureBucketAccess(AmazonS3 s3Client) {
        Policy publicReadPolicy = new Policy().withStatements(
                new Statement(Statement.Effect.Allow)
                        .withPrincipals(Principal.AllUsers)
                        .withActions(S3Actions.GetObject, S3Actions.PutObject, S3Actions.DeleteObject)
                        .withResources(new Resource("arn:aws:s3:::" + bucketName + "/*")));

        s3Client.setBucketPolicy(new SetBucketPolicyRequest()
                .withBucketName(bucketName)
                .withPolicyText(publicReadPolicy.toJson()));
    }

    private void configureBucketCorsConfig(AmazonS3 s3Client) {
        CORSRule corsRule = new CORSRule();
        corsRule.setAllowedMethods(CORSRule.AllowedMethods.GET, CORSRule.AllowedMethods.POST, CORSRule.AllowedMethods.PUT, CORSRule.AllowedMethods.DELETE, CORSRule.AllowedMethods.HEAD);
        corsRule.setAllowedOrigins("*");
        corsRule.setAllowedHeaders("*");

        BucketCrossOriginConfiguration configuration = new BucketCrossOriginConfiguration();
        configuration.setRules(Collections.singletonList(corsRule));

        s3Client.setBucketCrossOriginConfiguration(bucketName, configuration);
    }
}
