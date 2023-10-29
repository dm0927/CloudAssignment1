# Programming Assignment 1: Image Classification and Text Detection on AWS

## Services Utilized
- **AWS EC2**: Used to deploy Java applications.
- **AWS S3**: Storage for images.
- **AWS Rekognition**: Image classification and text detection service.
- **AWS SQS**: Messaging service to coordinate between image classification and text detection.

## Step-by-Step Guide

### AWS Learner's Lab Setup
1. Access AWS Learner's Lab through your student account.
2. Follow the provided README file for setup instructions and access your AWS credentials.

### AWS CLI Configuration
1. Install AWS CLI.
2. Configure AWS CLI with your credentials using the `aws configure` command.
   - You will need your Access Key, Secret Key, and Session Token.
   - You can set up multiple profiles for different AWS accounts.

### AWS EC2 Instance Configuration
1. Navigate to the AWS Management Console.
2. Select "Services" and then "EC2."
3. Choose "Instances" and click "Launch Instances."
4. Name your instance and select "Amazon Linux" as the OS.
5. Choose the instance type (e.g., "t2.micro").
6. Create or select a key pair for SSH access.
7. Launch the instance.

### SSH Access to EC2 Instances
1. Once the EC2 instances are created, download the SSH key (e.g., CS643-Cloud.pem).
2. Open your terminal and navigate to the directory where the key is located.
3. Use the following command to connect to the EC2 instance:
    ```
      ssh -i "CS643-Cloud.pem" ec2-user@ec2-50-17-36-229.compute-1.amazonaws.com
    ```
4. Confirm the connection by typing "yes."

### AWS SQS Configuration
1. Go to the "Amazon SQS" page on the AWS Management Console.
2. Choose "Create Queue."
3. Set the queue type and provide a name (e.g., "carsinformation.fifo").
4. Configure queue settings and access policies.
5. You can define the queue name directly in your code, and AWS will create it if it doesn't exist.

### Java Application for Image Recognition
1. Develop a Java application to:
- Retrieve images from the S3 bucket.
- Use AWS Rekognition to classify the images and obtain labels and confidence scores.
- Mark images labeled as "Car" with confidence > 90% and send their names to the AWS SQS message queue.
- Send a termination message (-1) as the last message.
2. Compile and package the application into a JAR file for deployment.

### Java Application for Text Detection
1. Create another Java application to:
- Retrieve messages one by one from the AWS SQS queue.
- Use AWS Rekognition to perform text detection on images.
- Record detected text in an "ImageText.txt" file with their respective indexes.
- Continuously check for new messages in the queue.
2. Compile and package this application into a separate JAR file.

### Deployment on EC2 Instances
1. SSH into the EC2 instances using their respective IP addresses.
2. Configure AWS credentials on each instance using the `aws configure` command.
3. Install OpenJDK Java (e.g., OpenJDK 19.0.1).
4. Move the Java installation to the appropriate directory.
5. Edit the `/etc/profile` file to set Java environment variables.
- Example content to add:
```
    export JAVA_HOME=/usr/share/jdk-19.0.1
    export PATH=$JAVA_HOME/bin:$PATH
    export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```

6. Verify the Java installation.
7. Transfer the JAR files to the EC2 instances using a tool like Cyberduck.
8. Run the image classification and text detection applications in parallel on the EC2 instances.
- Example commands:
  - For the car recognition app: `java -jar car-recognition-app-0.0.1-SNAPSHOT.jar`
  - For the text detection app: `java -jar text-detection-app-0.0.1-SNAPSHOT.jar`

By following these steps, you can set up an image classification and text detection system on AWS using EC2 instances, S3, Rekognition, and SQS. Make sure to adapt the instructions as needed for your specific project.
