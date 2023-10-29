package com.pa1.textdetectionapp.textdetectionapp.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class TextDetectionAppApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(TextDetectionAppApplication.class, args);
		S3Service s3Service = new S3Service();
		SQSService sqsService = new SQSService();
		SqsClient sqsClient = sqsService.getSqsClient();
		String queueUrl = sqsService.getQueueUrl(sqsClient);

		TextDetectionService textDetectionService = new TextDetectionService();
		RekognitionClient rekognitionClient = textDetectionService.getRekognitionClient();

		log.info("queueUrl: {}", queueUrl);
		Map<String, String> mp = new HashMap<>();
		while(true) {
			Message message = sqsService.receiveMessage(sqsClient, queueUrl);
			if(message==null) {
				Thread.sleep(1000);
				continue;
			}
			DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(message.receiptHandle()).build();
			sqsClient.deleteMessage(deleteMessageRequest);
			if(message.body().equals("-1")) {
				break;
			}  else {
				Image image = s3Service.s3FetchByName(message.body());
				textDetectionService.detectTextFromImage(rekognitionClient, image, message.body(), mp);
			}
		}
		FileWriteService fileWriteService = new FileWriteService();
		fileWriteService.fileWrite(mp);
	}

}
