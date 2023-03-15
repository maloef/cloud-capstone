package com.maloef.cdnd.capstone.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentHandler {

    @Value("${s3.image-bucket}")
    private String bucketName;

    @Value("${s3.signed-url-expires-in-seconds}")
    private int signedUrlExpiresInSeconds;

    private final AmazonS3 amazonS3;

    public void deleteAttachment(String key) {
        try {
            amazonS3.deleteObject(bucketName, key);
            log.info("deleted object {} from bucket {}", key, bucketName);
        } catch (Exception e) {
            log.error("could not delete object {} from bucket {}:", key, bucketName, e);
        }
    }

    public String createSignedUrl(String todoId) {
        DateTime expires = DateTime.now().plusSeconds(signedUrlExpiresInSeconds);
        URL url = amazonS3.generatePresignedUrl(bucketName, todoId, expires.toDate(), HttpMethod.PUT);

        log.info("created signed url {} for key {} that expires at {}", url, todoId, expires);
        return url.toString();
    }
}
