package com.pa1.carrecognitionapp.service;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.Map;

@Slf4j
public class AWSSQSService {
    private SqsClient sqsClient;

    private static String queueName = "carsinformation.fifo";

    public AWSSQSService() {
        this.sqsClient = SqsClient.builder().region(Region.US_EAST_1).build();
    }

    public SqsClient getSqsClient() {
        return sqsClient;
    }

    public String getQueueUrl(SqsClient sqsClient) {
        String queueName = "carsinformation.fifo";
        String queueUrl;
        GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder().queueName(queueName).build();

        try {
            queueUrl = sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl();
        } catch (QueueDoesNotExistException e) {
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().attributesWithStrings(Map.of("FifoQueue", "true", "ContentBasedDeduplication", "true")).queueName(queueName).build();
            sqsClient.createQueue(createQueueRequest);

            GetQueueUrlRequest getURLQue = GetQueueUrlRequest.builder().queueName(queueName).build();
            queueUrl = sqsClient.getQueueUrl(getURLQue).queueUrl();
        }
        return queueUrl;
    }

    public boolean pushMessage(SqsClient sqsClient, String imageKey, String queueUrl) {
        try {
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder().queueUrl(queueUrl).messageGroupId("CarText").messageBody(imageKey).build();
            String messageId = sqsClient.sendMessage(sendMsgRequest).sequenceNumber();
            log.info("Sequence number of the message: {}" , messageId);
            return true;
        } catch (Exception e) {
            log.info(String.valueOf(e));
            return false;
        }
    }
}
