package com.pa1.carrecognitionapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.List;

@SpringBootApplication
@Slf4j
public class CarRecognition {
	private static String s3BucketName = "njit-cs-643";
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(CarRecognition.class, args);
		AWSS3 s3Service = new AWSS3();
		S3Client s3Client = s3Service.getS3Client();
		List<S3Object> images = s3Service.s3DataFetch(s3Client);
		AWSSQSService sqsService = new AWSSQSService();
		SqsClient sqsClient = sqsService.getSqsClient();
		String queueURL = sqsService.getQueueUrl(sqsClient);
		AWSRekognition rekognitionService = new AWSRekognition();
		RekognitionClient rekognitionClient = rekognitionService.getRekognitionClient();

		log.info("queueUrl: {}", queueURL);
		for(S3Object img : images) {
			if (rekognitionService.recognize(rekognitionClient, img, s3BucketName)) {
				log.info("The image has car label with required confidence: {}", img.key());
				if(sqsService.pushMessage(sqsClient, img.key(), queueURL))
					log.info("Message pushed successfully: {}", img.key());
				else
					log.info("Error while pushing the message: {}", img.key());
			} else {
				log.info("The image does not have a car label: {}", img.key());
			}
		}
		sqsService.pushMessage(sqsClient, "-1", queueURL);
		log.info("Queue Ended: -1");
	}
}
